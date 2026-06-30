package com.desdeaqui.service;

import com.desdeaqui.model.Comentario;
import com.desdeaqui.model.Tip;
import com.desdeaqui.model.Usuario;
import com.desdeaqui.repository.ComentarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ComentarioService {

    @Autowired
    private ComentarioRepository comentarioRepository;

    public List<Comentario> listarPorTip(Integer tipId) {
        return comentarioRepository.findByTipIdOrderByCreadoEnDesc(tipId);
    }

    public Optional<Comentario> buscarPorId(Integer id) {
        return comentarioRepository.findById(id);
    }

    public long contarPorTip(Integer tipId) {
        return comentarioRepository.countByTipId(tipId);
    }

    public Comentario guardar(Tip tip, Usuario usuario, String contenido) {
        return comentarioRepository.save(new Comentario(tip, usuario, contenido));
    }

    public void eliminar(Integer id) {
        comentarioRepository.deleteById(id);
    }
}
