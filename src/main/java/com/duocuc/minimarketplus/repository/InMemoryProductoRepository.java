package com.duocuc.minimarketplus.repository;

import com.duocuc.minimarketplus.model.Producto;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Implementación en memoria de ProductoRepository.
 * En un microservicio real esta capa se reemplazaría por Spring Data JPA / JDBC.
 * Se usa únicamente para fines demostrativos del proyecto (no es el foco de las
 * pruebas unitarias, que se centran en la capa de servicio usando Mockito).
 */
public class InMemoryProductoRepository implements ProductoRepository {

    private final Map<Long, Producto> datos = new ConcurrentHashMap<>();

    @Override
    public Optional<Producto> findById(Long id) {
        return Optional.ofNullable(datos.get(id));
    }

    @Override
    public Producto save(Producto producto) {
        datos.put(producto.getId(), producto);
        return producto;
    }
}
