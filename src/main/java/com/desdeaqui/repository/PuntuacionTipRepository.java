package com.desdeaqui.repository;

import com.desdeaqui.model.PuntuacionTip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PuntuacionTipRepository extends JpaRepository<PuntuacionTip, Integer> {

    Optional<PuntuacionTip> findByTipIdAndUsuarioId(Integer tipId, Integer usuarioId);

    boolean existsByTipIdAndUsuarioId(Integer tipId, Integer usuarioId);

    // Promedio de estrellas de un tip
    @Query("SELECT AVG(p.estrellas) FROM PuntuacionTip p WHERE p.tip.id = :tipId")
    Double calcularPromedio(@Param("tipId") Integer tipId);

    // Total de valoraciones de un tip
    long countByTipId(Integer tipId);

    long countByUsuarioId(Integer usuarioId);
}