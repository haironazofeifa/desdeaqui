package com.desdeaqui.model;

import jakarta.persistence.*;

@Entity
@Table(
    name = "guardados",
    uniqueConstraints = @UniqueConstraint(columnNames = {"usuario_id", "destino_id"})
)
public class Guardado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "destino_id", nullable = false)
    private Destino destino;

    // ── Constructores ──────────────────────────────
    public Guardado() {}

    public Guardado(Usuario usuario, Destino destino) {
        this.usuario = usuario;
        this.destino = destino;
    }

    // ── Getters y Setters ──────────────────────────
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public Destino getDestino() { return destino; }
    public void setDestino(Destino destino) { this.destino = destino; }
}