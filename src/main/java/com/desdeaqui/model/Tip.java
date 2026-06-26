package com.desdeaqui.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "tips")
public class Tip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String contenido;

    @Column(nullable = false, length = 50)
    private String categoria;           // transporte | comida | hospedaje | lugares

    @JsonIgnore
    @ManyToOne                          // muchos tips pertenecen a un destino
    @JoinColumn(name = "destino_id", nullable = false)
    private Destino destino;

    // ── Constructores ──────────────────────────────
    public Tip() {}

    public Tip(String contenido, String categoria, Destino destino) {
        this.contenido = contenido;
        this.categoria = categoria;
        this.destino = destino;
    }

    // ── Getters y Setters ──────────────────────────
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getContenido() { return contenido; }
    public void setContenido(String contenido) { this.contenido = contenido; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public Destino getDestino() { return destino; }
    public void setDestino(Destino destino) { this.destino = destino; }
}