package com.duocuc.minimarketplus.model;

/**
 * Representa una línea (producto + cantidad) dentro de un Carrito de compras.
 */
public class ItemCarrito {

    private Producto producto;
    private int cantidad;

    public ItemCarrito() {
    }

    public ItemCarrito(Producto producto, int cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    @Override
    public String toString() {
        return "ItemCarrito{producto=" + producto + ", cantidad=" + cantidad + '}';
    }
}
