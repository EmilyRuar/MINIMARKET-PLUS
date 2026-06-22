package com.duocuc.minimarketplus.exception;

/**
 * Se lanza cuando los datos de un movimiento de inventario (tipoMovimiento o
 * cantidad) son nulos, vacíos o inconsistentes.
 */
public class MovimientoInvalidoException extends RuntimeException {

    public MovimientoInvalidoException(String message) {
        super(message);
    }
}
