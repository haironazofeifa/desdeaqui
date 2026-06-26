package com.desdeaqui.service;

import com.desdeaqui.model.Destino;
import com.desdeaqui.model.Guardado;
import com.desdeaqui.model.Usuario;
import com.desdeaqui.repository.GuardadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class GuardadoService {

    @Autowired
    private GuardadoRepository guardadoRepository;

    public List<Guardado> listarPorUsuario(Integer usuarioId) {
        return guardadoRepository.findByUsuarioId(usuarioId);
    }

    public boolean estaGuardado(Integer usuarioId, Integer destinoId) {
        return guardadoRepository.existsByUsuarioIdAndDestinoId(usuarioId, destinoId);
    }

    public void guardarDestino(Usuario usuario, Destino destino) {
        if (!estaGuardado(usuario.getId(), destino.getId())) {
            guardadoRepository.save(new Guardado(usuario, destino));
        }
    }

    public void eliminarGuardado(Integer usuarioId, Integer destinoId) {
        guardadoRepository.deleteByUsuarioIdAndDestinoId(usuarioId, destinoId);
    }

    public void eliminarPorId(Integer id) {
        guardadoRepository.deleteById(id);
    }
}