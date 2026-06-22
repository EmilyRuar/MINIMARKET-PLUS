package com.duocuc.minimarketplus.model;

import java.time.LocalDateTime;

/**
 * Entidad Inventario: registra los movimientos de stock (ENTRADA / SALIDA)
 * asociados a un Producto.
 */
public class Inventario {

    private Long id;
    private Producto producto;
    private TipoMovimiento tipoMovimiento;
    private Integer cantidad;
    private LocalDateTime fechaMovimiento;

    public Inventario() {
    }

    public Inventario(Long id, Producto producto, TipoMovimiento tipoMovimiento, Integer cantidad,
                       LocalDateTime fechaMovimiento) {
        this.id = id;
        this.producto = producto;
        this.tipoMovimiento = tipoMovimiento;
        this.cantidad = cantidad;
        this.fechaMovimiento = fechaMovimiento;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public TipoMovimiento getTipoMovimiento() {
        return tipoMovimiento;
    }

    public void setTipoMovimiento(TipoMovimiento tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public LocalDateTime getFechaMovimiento() {
        return fechaMovimiento;
    }

    public void setFechaMovimiento(LocalDateTime fechaMovimiento) {
        this.fechaMovimiento = fechaMovimiento;
    }

    @Override
    public String toString() {
        return "Inventario{id=" + id + ", producto=" + producto + ", tipoMovimiento=" + tipoMovimiento
                + ", cantidad=" + cantidad + ", fechaMovimiento=" + fechaMovimiento + '}';
    }
}
