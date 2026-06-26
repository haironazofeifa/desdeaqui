package com.desdeaqui.service;

import com.desdeaqui.model.Destino;
import com.desdeaqui.repository.DestinoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class DestinoService {

    @Autowired
    private DestinoRepository destinoRepository;

    public List<Destino> listarTodos() {
        return destinoRepository.findAll();
    }

    public Optional<Destino> buscarPorId(Integer id) {
        return destinoRepository.findById(id);
    }

    public Destino guardar(Destino destino) {
        return destinoRepository.save(destino);
    }

    public void eliminar(Integer id) {
        destinoRepository.deleteById(id);
    }

    public List<Destino> filtrar(String zona, String interes, String presupuesto) {
        if ((zona == null || zona.isBlank()) &&
                (interes == null || interes.isBlank()) &&
                (presupuesto == null || presupuesto.isBlank())) {
            return destinoRepository.findAll();
        }

        return destinoRepository.filtrar(zona, interes, presupuesto);
    }
}