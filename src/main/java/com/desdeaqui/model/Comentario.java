package com.desdeaqui.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comentarios")
public class Comentario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "tip_id", nullable = false)
    private Tip tip;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String contenido;

    @Column(name = "creado_en")
    private LocalDateTime creadoEn;

    public Comentario() {}

    public Comentario(Tip tip, Usuario usuario, String contenido) {
        this.tip = tip;
        this.usuario = usuario;
        this.contenido = contenido;
        this.creadoEn = LocalDateTime.now();
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Tip getTip() { return tip; }
    public void setTip(Tip tip) { this.tip = tip; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public String getContenido() { return contenido; }
    public void setContenido(String contenido) { this.contenido = contenido; }

    public LocalDateTime getCreadoEn() { return creadoEn; }
    public void setCreadoEn(LocalDateTime creadoEn) { this.creadoEn = creadoEn; }
}
