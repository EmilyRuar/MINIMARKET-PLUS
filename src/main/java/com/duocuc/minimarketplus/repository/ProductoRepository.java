package com.duocuc.minimarketplus.repository;

import com.duocuc.minimarketplus.model.Producto;

import java.util.Optional;

/**
 * Puerto de acceso a datos para la entidad Producto.
 * En este microservicio se simula (mock) en las pruebas unitarias con Mockito,
 * ya que la implementación real (JPA/JDBC) corresponde a la capa de persistencia.
 */
public interface ProductoRepository {

    Optional<Producto> findById(Long id);

    Producto save(Producto producto);
}
