package com.duocuc.minimarketplus.service;

import com.duocuc.minimarketplus.exception.ProductoNoEncontradoException;
import com.duocuc.minimarketplus.exception.StockInsuficienteException;
import com.duocuc.minimarketplus.model.Carrito;
import com.duocuc.minimarketplus.model.ItemCarrito;
import com.duocuc.minimarketplus.model.Producto;
import com.duocuc.minimarketplus.model.Usuario;
import com.duocuc.minimarketplus.repository.ProductoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias de la entidad Carrito.
 *
 * Cobertura (Paso 2 de la actividad "Ejecutando y Analizando Pruebas
 * Unitarias en Microservicios con JUnit"):
 *  1) Disponibilidad de stock al agregar productos.
 *  2) Relación correcta Producto-Usuario dentro del carrito.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("CarritoService - Pruebas unitarias")
class CarritoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private CarritoService carritoService;

    private Usuario usuario;
    private Carrito carrito;

    @BeforeEach
    void setUp() {
        
        usuario = new Usuario(1L, "Camila Rojas", "camila.rojas@minimarketplus.cl");
        carrito = new Carrito(100L, usuario);
    }

    @Nested
    @DisplayName("Creación de carrito")
    class CreacionCarrito {

        @Test
        @DisplayName("crearCarrito() crea un carrito vacío asociado al usuario indicado")
        void crearCarrito_deberiaCrearCarritoVacio_conUsuarioAsociado() {
            Carrito nuevoCarrito = carritoService.crearCarrito(usuario);

            assertNotNull(nuevoCarrito);
            assertEquals(usuario, nuevoCarrito.getUsuario());
            assertTrue(nuevoCarrito.getItems().isEmpty());
        }

        @Test
        @DisplayName("crearCarrito() lanza excepción cuando el usuario es nulo")
        void crearCarrito_deberiaLanzarExcepcion_cuandoUsuarioEsNulo() {
            assertThrows(IllegalArgumentException.class, () -> carritoService.crearCarrito(null));
        }
    }

    @Nested
    @DisplayName("Prueba de disponibilidad de stock")
    class DisponibilidadStock {

        @Test
        @DisplayName("agregarProducto() lanza excepción cuando el carrito es nulo")
        void agregarProducto_deberiaLanzarExcepcion_cuandoCarritoEsNulo() {
            assertThrows(IllegalArgumentException.class,
                    () -> carritoService.agregarProducto(null, 10L, 1));
            verifyNoInteractions(productoRepository);
        }

        @Test
        @DisplayName("agregarProducto() agrega el producto cuando hay stock suficiente")
        void agregarProducto_deberiaAgregarProducto_cuandoHayStockSuficiente() {
            // Arrange
            Producto producto = new Producto(10L, "Aceite Maravilla 1L", new BigDecimal("2890"), 15);
            when(productoRepository.findById(10L)).thenReturn(Optional.of(producto));

            // Act
            ItemCarrito item = carritoService.agregarProducto(carrito, 10L, 3);

            // Assert
            assertEquals(1, carrito.getItems().size(), "El carrito debe contener 1 ítem");
            assertEquals(3, item.getCantidad());
            assertEquals(producto, item.getProducto());
            verify(productoRepository, times(1)).findById(10L);
        }

        @Test
        @DisplayName("agregarProducto() lanza StockInsuficienteException cuando el stock no alcanza")
        void agregarProducto_deberiaLanzarExcepcion_cuandoNoHayStockSuficiente() {
            // Arrange
            Producto producto = new Producto(11L, "Arroz Grado 1 1Kg", new BigDecimal("1190"), 2);
            when(productoRepository.findById(11L)).thenReturn(Optional.of(producto));

            // Act & Assert
            StockInsuficienteException ex = assertThrows(StockInsuficienteException.class,
                    () -> carritoService.agregarProducto(carrito, 11L, 5));

            assertTrue(ex.getMessage().contains("Stock insuficiente"));
            assertTrue(carrito.getItems().isEmpty(), "No debe agregarse ningún ítem si el stock no alcanza");
        }

        @Test
        @DisplayName("agregarProducto() lanza ProductoNoEncontradoException cuando el producto no existe")
        void agregarProducto_deberiaLanzarExcepcion_cuandoProductoNoExiste() {
            // Arrange
            when(productoRepository.findById(99L)).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(ProductoNoEncontradoException.class,
                    () -> carritoService.agregarProducto(carrito, 99L, 1));
            assertTrue(carrito.getItems().isEmpty());
        }

        @Test
        @DisplayName("agregarProducto() rechaza cantidades menores o iguales a cero")
        void agregarProducto_deberiaLanzarExcepcion_cuandoCantidadNoEsValida() {
            assertThrows(IllegalArgumentException.class,
                    () -> carritoService.agregarProducto(carrito, 10L, 0));
            verifyNoInteractions(productoRepository);
        }
    }

    @Nested
    @DisplayName("Validación de relación Producto-Usuario")
    class RelacionUsuario {

        @Test
        @DisplayName("validarUsuarioAsociado() retorna true cuando el usuario es el dueño del carrito")
        void validarUsuarioAsociado_deberiaRetornarTrue_cuandoUsuarioCoincide() {
            boolean resultado = carritoService.validarUsuarioAsociado(carrito, usuario);
            assertTrue(resultado, "El usuario asociado al carrito debe ser el correcto");
        }

        @Test
        @DisplayName("validarUsuarioAsociado() retorna false cuando el usuario no coincide")
        void validarUsuarioAsociado_deberiaRetornarFalse_cuandoUsuarioNoCoincide() {
            Usuario otroUsuario = new Usuario(2L, "Pedro Soto", "pedro.soto@minimarketplus.cl");
            boolean resultado = carritoService.validarUsuarioAsociado(carrito, otroUsuario);
            assertFalse(resultado, "Un usuario distinto al dueño no debe validarse como correcto");
        }

        @Test
        @DisplayName("validarUsuarioAsociado() retorna false cuando el carrito no tiene usuario")
        void validarUsuarioAsociado_deberiaRetornarFalse_cuandoCarritoSinUsuario() {
            Carrito carritoSinUsuario = new Carrito(101L, null);
            boolean resultado = carritoService.validarUsuarioAsociado(carritoSinUsuario, usuario);
            assertFalse(resultado);
        }
    }
}
