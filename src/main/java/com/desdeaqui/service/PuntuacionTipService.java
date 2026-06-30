package com.desdeaqui.service;

import com.desdeaqui.model.PuntuacionTip;
import com.desdeaqui.model.Tip;
import com.desdeaqui.model.Usuario;
import com.desdeaqui.repository.PuntuacionTipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class PuntuacionTipService {

    @Autowired
    private PuntuacionTipRepository puntuacionRepository;

    // Guarda o actualiza la puntuación del usuario para ese tip
    public void puntuar(Tip tip, Usuario usuario, Integer estrellas) {
        Optional<PuntuacionTip> existente =
            puntuacionRepository.findByTipIdAndUsuarioId(tip.getId(), usuario.getId());

        if (existente.isPresent()) {
            // Ya puntuó antes → actualizar
            PuntuacionTip p = existente.get();
            p.setEstrellas(estrellas);
            puntuacionRepository.save(p);
        } else {
            // Primera vez → crear
            puntuacionRepository.save(new PuntuacionTip(tip, usuario, estrellas));
        }
    }

    // Promedio redondeado a 1 decimal (ej: 4.3)
    public double obtenerPromedio(Integer tipId) {
        Double promedio = puntuacionRepository.calcularPromedio(tipId);
        if (promedio == null) return 0.0;
        return Math.round(promedio * 10.0) / 10.0;
    }

    // Total de valoraciones
    public long obtenerTotal(Integer tipId) {
        return puntuacionRepository.countByTipId(tipId);
    }

    // Puntuación del usuario actual para ese tip (para mostrar su selección)
    public Integer obtenerPuntuacionDelUsuario(Integer tipId, Integer usuarioId) {
        return puntuacionRepository
            .findByTipIdAndUsuarioId(tipId, usuarioId)
            .map(PuntuacionTip::getEstrellas)
            .orElse(0);
    }
}