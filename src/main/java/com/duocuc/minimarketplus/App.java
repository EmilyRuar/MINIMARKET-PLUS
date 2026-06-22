package com.duocuc.minimarketplus;

import com.duocuc.minimarketplus.model.Carrito;
import com.duocuc.minimarketplus.model.Producto;
import com.duocuc.minimarketplus.model.TipoMovimiento;
import com.duocuc.minimarketplus.model.Usuario;
import com.duocuc.minimarketplus.repository.InMemoryInventarioRepository;
import com.duocuc.minimarketplus.repository.InMemoryProductoRepository;
import com.duocuc.minimarketplus.repository.InventarioRepository;
import com.duocuc.minimarketplus.repository.ProductoRepository;
import com.duocuc.minimarketplus.service.CarritoService;
import com.duocuc.minimarketplus.service.InventarioService;

import java.math.BigDecimal;

/**
 * Punto de entrada de demostración del microservicio MINIMARKET PLUS.
 * No forma parte de las pruebas unitarias; sirve únicamente para ilustrar
 * el uso de los servicios Carrito e Inventario.
 */
public class App {

    public static void main(String[] args) {
        ProductoRepository productoRepository = new InMemoryProductoRepository();
        InventarioRepository inventarioRepository = new InMemoryInventarioRepository();

        Producto aceite = new Producto(10L, "Aceite Maravilla 1L", new BigDecimal("2890"), 15);
        productoRepository.save(aceite);

        CarritoService carritoService = new CarritoService(productoRepository);
        InventarioService inventarioService = new InventarioService(inventarioRepository, productoRepository);

        Usuario usuario = new Usuario(1L, "Camila Rojas", "camila.rojas@minimarketplus.cl");
        Carrito carrito = carritoService.crearCarrito(usuario);
        carritoService.agregarProducto(carrito, 10L, 3);

        System.out.println("Carrito generado: " + carrito);

        inventarioService.registrarMovimiento(10L, TipoMovimiento.SALIDA, 3);
        System.out.println("Movimiento de inventario registrado correctamente.");
    }
}
