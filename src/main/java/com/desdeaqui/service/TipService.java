package com.desdeaqui.service;

import com.desdeaqui.model.Tip;
import com.desdeaqui.repository.TipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class TipService {

    @Autowired
    private TipRepository tipRepository;

    public List<Tip> listarPorDestino(Integer destinoId) {
        return tipRepository.findByDestinoId(destinoId);
    }

    public List<Tip> listarPorDestinoYCategoria(Integer destinoId, String categoria) {
        return tipRepository.findByDestinoIdAndCategoriaOrderByPromedio(destinoId, categoria);
    }

    public Optional<Tip> buscarPorId(Integer id) {
        return tipRepository.findById(id);
    }

    public Tip guardar(Tip tip) {
        return tipRepository.save(tip);
    }

    public void eliminar(Integer id) {
        tipRepository.deleteById(id);
    }
}