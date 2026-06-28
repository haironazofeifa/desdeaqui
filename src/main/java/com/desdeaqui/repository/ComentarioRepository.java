package com.desdeaqui.repository;

import com.desdeaqui.model.Comentario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComentarioRepository extends JpaRepository<Comentario, Integer> {

    List<Comentario> findByTipIdOrderByCreadoEnDesc(Integer tipId);

    long countByTipId(Integer tipId);

    @Query("SELECT COUNT(c) FROM Comentario c WHERE c.usuario.id = :usuarioId")
long contarPorUsuario(@Param("usuarioId") Integer usuarioId);
}
