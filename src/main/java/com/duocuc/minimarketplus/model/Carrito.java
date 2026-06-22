package com.duocuc.minimarketplus.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Entidad Carrito: agrupa los productos que un Usuario desea comprar.
 */
public class Carrito {

    private Long id;
    private Usuario usuario;
    private List<ItemCarrito> items = new ArrayList<>();

    public Carrito() {
    }

    public Carrito(Long id, Usuario usuario) {
        this.id = id;
        this.usuario = usuario;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<ItemCarrito> getItems() {
        return items;
    }

    public void setItems(List<ItemCarrito> items) {
        this.items = items;
    }

    public void agregarItem(ItemCarrito item) {
        this.items.add(item);
    }

    @Override
    public String toString() {
        return "Carrito{id=" + id + ", usuario=" + usuario + ", items=" + items + '}';
    }
}
