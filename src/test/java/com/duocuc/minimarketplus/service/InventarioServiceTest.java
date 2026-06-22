package com.duocuc.minimarketplus.service;

import com.duocuc.minimarketplus.exception.MovimientoInvalidoException;
import com.duocuc.minimarketplus.exception.ProductoNoEncontradoException;
import com.duocuc.minimarketplus.model.Inventario;
import com.duocuc.minimarketplus.model.Producto;
import com.duocuc.minimarketplus.model.TipoMovimiento;
import com.duocuc.minimarketplus.repository.InventarioRepository;
import com.duocuc.minimarketplus.repository.ProductoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias de la entidad Inventario.
 *
 * Cobertura (Paso 3 de la actividad "Ejecutando y Analizando Pruebas
 * Unitarias en Microservicios con JUnit"):
 *  1) Validación de información de movimiento (tipoMovimiento y cantidad no nulos/vacíos).
 *  2) Relación correcta Producto-Inventario.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("InventarioService - Pruebas unitarias")
class InventarioServiceTest {

    @Mock
    private InventarioRepository inventarioRepository;

    @Mock
    private ProductoRepository productoRepository;

    private InventarioService inventarioService;
    private Producto producto;

    @BeforeEach
    void setUp() {
        inventarioService = new InventarioService(inventarioRepository, productoRepository);
        producto = new Producto(20L, "Leche Entera 1L", new BigDecimal("990"), 50);
    }

    @Nested
    @DisplayName("Prueba de información de movimiento")
    class InformacionMovimiento {

        @Test
        @DisplayName("registrarMovimiento() lanza excepción cuando tipoMovimiento es nulo")
        void registrarMovimiento_deberiaLanzarExcepcion_cuandoTipoMovimientoEsNulo() {
            MovimientoInvalidoException ex = assertThrows(MovimientoInvalidoException.class,
                    () -> inventarioService.registrarMovimiento(20L, null, 10));

            assertTrue(ex.getMessage().contains("tipo de movimiento"));
            verifyNoInteractions(inventarioRepository);
        }

        @ParameterizedTest(name = "cantidad inválida = {0}")
        @NullSource
        @ValueSource(ints = {0, -5})
        @DisplayName("registrarMovimiento() lanza excepción cuando la cantidad es nula, cero o negativa")
        void registrarMovimiento_deberiaLanzarExcepcion_cuandoCantidadEsNulaOVacia(Integer cantidadInvalida) {
            MovimientoInvalidoException ex = assertThrows(MovimientoInvalidoException.class,
                    () -> inventarioService.registrarMovimiento(20L, TipoMovimiento.ENTRADA, cantidadInvalida));

            assertTrue(ex.getMessage().contains("cantidad"));
            verifyNoInteractions(inventarioRepository);
        }

        @Test
        @DisplayName("registrarMovimiento() lanza ProductoNoEncontradoException cuando el producto no existe")
        void registrarMovimiento_deberiaLanzarExcepcion_cuandoProductoNoExiste() {
            when(productoRepository.findById(999L)).thenReturn(Optional.empty());

            assertThrows(ProductoNoEncontradoException.class,
                    () -> inventarioService.registrarMovimiento(999L, TipoMovimiento.SALIDA, 5));
            verifyNoInteractions(inventarioRepository);
        }

        @Test
        @DisplayName("registrarMovimiento() registra correctamente un movimiento de ENTRADA válido")
        void registrarMovimiento_deberiaRegistrarCorrectamente_movimientoEntrada() {
            when(productoRepository.findById(20L)).thenReturn(Optional.of(producto));
            when(inventarioRepository.save(any(Inventario.class))).thenAnswer(invocation -> {
                Inventario inv = invocation.getArgument(0);
                inv.setId(500L);
                return inv;
            });

            Inventario resultado = inventarioService.registrarMovimiento(20L, TipoMovimiento.ENTRADA, 25);

            assertNotNull(resultado);
            assertEquals(500L, resultado.getId());
            assertEquals(TipoMovimiento.ENTRADA, resultado.getTipoMovimiento());
            assertEquals(25, resultado.getCantidad());
            assertNotNull(resultado.getFechaMovimiento());
        }

        @Test
        @DisplayName("registrarMovimiento() registra correctamente un movimiento de SALIDA válido")
        void registrarMovimiento_deberiaRegistrarCorrectamente_movimientoSalida() {
            when(productoRepository.findById(20L)).thenReturn(Optional.of(producto));
            ArgumentCaptor<Inventario> captor = ArgumentCaptor.forClass(Inventario.class);
            when(inventarioRepository.save(captor.capture())).thenAnswer(invocation -> invocation.getArgument(0));

            inventarioService.registrarMovimiento(20L, TipoMovimiento.SALIDA, 8);

            Inventario capturado = captor.getValue();
            assertEquals(TipoMovimiento.SALIDA, capturado.getTipoMovimiento());
            assertEquals(8, capturado.getCantidad());
            verify(inventarioRepository, times(1)).save(any(Inventario.class));
        }
    }

    @Nested
    @DisplayName("Validación de relación Producto-Inventario")
    class RelacionProductoInventario {

        @Test
        @DisplayName("validarProductoAsociado() retorna true cuando el producto coincide con el del movimiento")
        void validarProductoAsociado_deberiaRetornarTrue_cuandoProductoCoincide() {
            Inventario inventario = new Inventario(1L, producto, TipoMovimiento.ENTRADA, 10, null);
            boolean resultado = inventarioService.validarProductoAsociado(inventario, producto);
            assertTrue(resultado, "El producto asociado al movimiento de inventario debe ser el correcto");
        }

        @Test
        @DisplayName("validarProductoAsociado() retorna false cuando el producto no coincide")
        void validarProductoAsociado_deberiaRetornarFalse_cuandoProductoNoCoincide() {
            Producto otroProducto = new Producto(21L, "Pan Hallulla 1Kg", new BigDecimal("1490"), 30);
            Inventario inventario = new Inventario(2L, producto, TipoMovimiento.SALIDA, 4, null);

            boolean resultado = inventarioService.validarProductoAsociado(inventario, otroProducto);
            assertFalse(resultado);
        }

        @Test
        @DisplayName("validarProductoAsociado() retorna false cuando el inventario no tiene producto asociado")
        void validarProductoAsociado_deberiaRetornarFalse_cuandoInventarioSinProducto() {
            Inventario inventario = new Inventario(3L, null, TipoMovimiento.ENTRADA, 1, null);
            boolean resultado = inventarioService.validarProductoAsociado(inventario, producto);
            assertFalse(resultado);
        }
    }
}
