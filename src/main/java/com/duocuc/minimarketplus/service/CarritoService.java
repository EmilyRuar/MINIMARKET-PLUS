package com.duocuc.minimarketplus.service;

import com.duocuc.minimarketplus.exception.ProductoNoEncontradoException;
import com.duocuc.minimarketplus.exception.StockInsuficienteException;
import com.duocuc.minimarketplus.model.Carrito;
import com.duocuc.minimarketplus.model.ItemCarrito;
import com.duocuc.minimarketplus.model.Producto;
import com.duocuc.minimarketplus.model.Usuario;
import com.duocuc.minimarketplus.repository.ProductoRepository;

import java.util.Objects;

/**
 * Servicio de dominio para la entidad Carrito.
 *
 * Reglas de negocio cubiertas (ver Paso 2 de la actividad):
 *  - Un producto solo puede agregarse al carrito si existe stock suficiente.
 *  - El usuario asociado al carrito debe ser siempre el usuario correcto.
 */
public class CarritoService {

    private final ProductoRepository productoRepository;

    public CarritoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    /**
     * Crea un carrito vacío asociado a un usuario.
     */
    public Carrito crearCarrito(Usuario usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException("El usuario no puede ser nulo");
        }
        Carrito carrito = new Carrito();
        carrito.setUsuario(usuario);
        return carrito;
    }

    /**
     * Agrega un producto al carrito solo si existe stock suficiente.
     *
     * @param carrito    carrito de destino
     * @param productoId id del producto a agregar
     * @param cantidad   cantidad solicitada
     * @return el ItemCarrito agregado
     */
    public ItemCarrito agregarProducto(Carrito carrito, Long productoId, int cantidad) {
        if (carrito == null) {
            throw new IllegalArgumentException("El carrito no puede ser nulo");
        }
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero");
        }

        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new ProductoNoEncontradoException(
                        "No se encontró el producto con id " + productoId));

        if (!producto.tieneStockSuficiente(cantidad)) {
            throw new StockInsuficienteException(
                    "Stock insuficiente para el producto '" + producto.getNombre()
                            + "'. Disponible: " + producto.getStock() + ", solicitado: " + cantidad);
        }

        ItemCarrito item = new ItemCarrito(producto, cantidad);
        carrito.agregarItem(item);
        return item;
    }

    /**
     * Valida que el usuario indicado sea efectivamente el usuario propietario del carrito.
     */
    public boolean validarUsuarioAsociado(Carrito carrito, Usuario usuario) {
        if (carrito == null || carrito.getUsuario() == null || usuario == null) {
            return false;
        }
        return Objects.equals(carrito.getUsuario().getId(), usuario.getId());
    }
}
