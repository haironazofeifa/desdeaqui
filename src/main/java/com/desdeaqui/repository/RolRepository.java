package com.desdeaqui.repository;

import com.desdeaqui.model.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolRepository extends JpaRepository<Rol, Integer> {
    Rol findByNombre(String nombre);    // útil para buscar el rol "ADMIN" o "VIAJERO"
}