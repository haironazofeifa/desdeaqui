package com.desdeaqui.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tips")
public class Tip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String contenido;

    @Column(nullable = false, length = 50)
    private String categoria;

    @ManyToOne
    @JoinColumn(name = "destino_id", nullable = false)
    private Destino destino;

    @ManyToOne
    @JoinColumn(name = "autor_id")
    private Usuario autor;                          // quién escribió el tip

    @Column(name = "creado_en")
    private LocalDateTime creadoEn;                 // cuándo lo escribió

    // ── Constructores ──────────────────────────────
    public Tip() {}

    public Tip(String contenido, String categoria, Destino destino) {
        this.contenido  = contenido;
        this.categoria  = categoria;
        this.destino    = destino;
        this.creadoEn   = LocalDateTime.now();
    }

    public Tip(String contenido, String categoria, Destino destino, Usuario autor) {
        this.contenido  = contenido;
        this.categoria  = categoria;
        this.destino    = destino;
        this.autor      = autor;
        this.creadoEn   = LocalDateTime.now();
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

    public Usuario getAutor() { return autor; }
    public void setAutor(Usuario autor) { this.autor = autor; }

    public LocalDateTime getCreadoEn() { return creadoEn; }
    public void setCreadoEn(LocalDateTime creadoEn) { this.creadoEn = creadoEn; }
}