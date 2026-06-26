package com.desdeaqui.controller;

import com.desdeaqui.model.Tip;
import com.desdeaqui.service.TipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TipController {

    @Autowired
    private TipService tipService;

    @GetMapping("/tips")
    public List<Tip> obtenerTips(@RequestParam Integer destinoId,
                                 @RequestParam String categoria) {
        return tipService.listarPorDestinoYCategoria(destinoId, categoria);
    }
}
