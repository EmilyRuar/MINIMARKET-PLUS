package com.duocuc.minimarketplus.exception;

/**
 * Se lanza cuando no se encuentra el producto solicitado en el repositorio.
 */
public class ProductoNoEncontradoException extends RuntimeException {

    public ProductoNoEncontradoException(String message) {
        super(message);
    }
}
