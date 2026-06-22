package com.duocuc.minimarketplus.repository;

import com.duocuc.minimarketplus.model.Inventario;

import java.util.List;
import java.util.Optional;

/**
 * Puerto de acceso a datos para la entidad Inventario.
 */
public interface InventarioRepository {

    Inventario save(Inventario inventario);

    Optional<Inventario> findById(Long id);

    List<Inventario> findByProductoId(Long productoId);
}
