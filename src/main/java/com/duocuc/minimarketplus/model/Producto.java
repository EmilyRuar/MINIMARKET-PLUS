package com.duocuc.minimarketplus.model;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Entidad que representa un producto del catálogo de MINIMARKET PLUS.
 */
public class Producto {

    private Long id;
    private String nombre;
    private BigDecimal precio;
    private int stock;

    public Producto() {
    }

    public Producto(Long id, String nombre, BigDecimal precio, int stock) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     * Indica si el producto tiene stock suficiente para cubrir la cantidad solicitada.
     */
    public boolean tieneStockSuficiente(int cantidadSolicitada) {
        return this.stock >= cantidadSolicitada;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Producto)) return false;
        Producto producto = (Producto) o;
        return Objects.equals(id, producto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Producto{id=" + id + ", nombre='" + nombre + "', precio=" + precio + ", stock=" + stock + '}';
    }
}
