package com.desdeaqui.repository;

import com.desdeaqui.model.Tip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TipRepository extends JpaRepository<Tip, Integer> {
    List<Tip> findByDestinoId(Integer destinoId);               // tips de un destino
    List<Tip> findByCategoria(String categoria);                // tips por categoría
    List<Tip> findByDestinoIdAndCategoria(Integer destinoId,
                                          String categoria);    // combinar ambos filtros
}