package com.desdeaqui.repository;

import com.desdeaqui.model.Destino;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DestinoRepository extends JpaRepository<Destino, Integer> {

    // Filtra por zona, interés y presupuesto — los parámetros son opcionales:
    // si vienen como null o vacío, ese filtro se ignora
    @Query("""
            SELECT d FROM Destino d
            WHERE (:zona       IS NULL OR :zona       = '' OR d.zona       = :zona)
              AND (:interes    IS NULL OR :interes    = '' OR d.interes    = :interes)
              AND (:presupuesto IS NULL OR :presupuesto = '' OR d.presupuesto = :presupuesto)
            """)
    List<Destino> filtrar(@Param("zona") String zona,
            @Param("interes") String interes,
            @Param("presupuesto") String presupuesto);
}