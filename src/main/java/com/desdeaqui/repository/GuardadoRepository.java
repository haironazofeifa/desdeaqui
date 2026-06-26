package com.desdeaqui.repository;

import com.desdeaqui.model.Guardado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface GuardadoRepository extends JpaRepository<Guardado, Integer> {
    List<Guardado> findByUsuarioId(Integer usuarioId);          // destinos guardados del usuario
    Optional<Guardado> findByUsuarioIdAndDestinoId(Integer usuarioId,
                                                    Integer destinoId);
    boolean existsByUsuarioIdAndDestinoId(Integer usuarioId,
                                           Integer destinoId);  // verificar si ya está guardado
    void deleteByUsuarioIdAndDestinoId(Integer usuarioId,
                                        Integer destinoId);     // eliminar un guardado
}