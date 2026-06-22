package com.duocuc.minimarketplus.service;

import com.duocuc.minimarketplus.exception.MovimientoInvalidoException;
import com.duocuc.minimarketplus.exception.ProductoNoEncontradoException;
import com.duocuc.minimarketplus.model.Inventario;
import com.duocuc.minimarketplus.model.Producto;
import com.duocuc.minimarketplus.model.TipoMovimiento;
import com.duocuc.minimarketplus.repository.InventarioRepository;
import com.duocuc.minimarketplus.repository.ProductoRepository;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Servicio de dominio para la entidad Inventario.
 *
 * Reglas de negocio cubiertas (ver Paso 3 de la actividad):
 *  - Los campos tipoMovimiento y cantidad no pueden ser nulos ni vacíos.
 *  - El producto asociado al movimiento de inventario debe ser el correcto.
 */
public class InventarioService {

    private final InventarioRepository inventarioRepository;
    private final ProductoRepository productoRepository;

    public InventarioService(InventarioRepository inventarioRepository, ProductoRepository productoRepository) {
        this.inventarioRepository = inventarioRepository;
        this.productoRepository = productoRepository;
    }

    /**
     * Registra un movimiento (ENTRADA o SALIDA) de inventario para un producto.
     *
     * @param productoId     id del producto afectado
     * @param tipoMovimiento ENTRADA o SALIDA
     * @param cantidad       cantidad movida (debe ser mayor a 0)
     * @return el movimiento de Inventario registrado
     */
    public Inventario registrarMovimiento(Long productoId, TipoMovimiento tipoMovimiento, Integer cantidad) {
        if (tipoMovimiento == null) {
            throw new MovimientoInvalidoException("El tipo de movimiento no puede ser nulo");
        }
        if (cantidad == null || cantidad <= 0) {
            throw new MovimientoInvalidoException("La cantidad del movimiento no puede ser nula ni vacía");
        }

        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new ProductoNoEncontradoException(
                        "No se encontró el producto con id " + productoId));

        Inventario inventario = new Inventario();
        inventario.setProducto(producto);
        inventario.setTipoMovimiento(tipoMovimiento);
        inventario.setCantidad(cantidad);
        inventario.setFechaMovimiento(LocalDateTime.now());

        return inventarioRepository.save(inventario);
    }

    /**
     * Valida que el producto indicado sea efectivamente el producto asociado
     * al movimiento de inventario.
     */
    public boolean validarProductoAsociado(Inventario inventario, Producto producto) {
        if (inventario == null || inventario.getProducto() == null || producto == null) {
            return false;
        }
        return Objects.equals(inventario.getProducto().getId(), producto.getId());
    }
}
