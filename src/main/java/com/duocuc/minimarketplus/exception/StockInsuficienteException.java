package com.duocuc.minimarketplus.exception;

/**
 * Se lanza cuando se intenta agregar al carrito una cantidad de producto
 * mayor al stock disponible en inventario.
 */
public class StockInsuficienteException extends RuntimeException {

    public StockInsuficienteException(String message) {
        super(message);
    }
}
