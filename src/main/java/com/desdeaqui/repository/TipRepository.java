package com.desdeaqui.repository;

import com.desdeaqui.model.Tip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TipRepository extends JpaRepository<Tip, Integer> {
    List<Tip> findByDestinoId(Integer destinoId); // tips de un destino

    List<Tip> findByCategoria(String categoria); // tips por categoría

    List<Tip> findByDestinoIdAndCategoria(Integer destinoId,
            String categoria); // combinar ambos filtros

    @Query("""
            SELECT t FROM Tip t
            LEFT JOIN PuntuacionTip p ON p.tip = t
            WHERE t.destino.id = :destinoId
              AND t.categoria  = :categoria
            GROUP BY t
            ORDER BY COALESCE(AVG(p.estrellas), 0) DESC
            """)

    List<Tip> findByDestinoIdAndCategoriaOrderByPromedio(
            @Param("destinoId") Integer destinoId,
            @Param("categoria") String categoria);

    @Query("SELECT COUNT(t) FROM Tip t WHERE t.autor.id = :usuarioId")
    long contarPorAutor(@Param("usuarioId") Integer usuarioId);
}