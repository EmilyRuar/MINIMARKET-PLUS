package com.duocuc.minimarketplus.repository;

import com.duocuc.minimarketplus.model.Inventario;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * Implementación en memoria de InventarioRepository.
 * En un microservicio real esta capa se reemplazaría por Spring Data JPA / JDBC.
 */
public class InMemoryInventarioRepository implements InventarioRepository {

    private final Map<Long, Inventario> datos = new ConcurrentHashMap<>();
    private final AtomicLong secuencia = new AtomicLong(1);

    @Override
    public Inventario save(Inventario inventario) {
        if (inventario.getId() == null) {
            inventario.setId(secuencia.getAndIncrement());
        }
        datos.put(inventario.getId(), inventario);
        return inventario;
    }

    @Override
    public Optional<Inventario> findById(Long id) {
        return Optional.ofNullable(datos.get(id));
    }

    @Override
    public List<Inventario> findByProductoId(Long productoId) {
        return datos.values().stream()
                .filter(i -> i.getProducto() != null && productoId.equals(i.getProducto().getId()))
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
