package com.desdeaqui.model;

import jakarta.persistence.*;

@Entity
@Table(
    name = "puntuaciones_tips",
    uniqueConstraints = @UniqueConstraint(columnNames = {"tip_id", "usuario_id"})
)
public class PuntuacionTip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "tip_id", nullable = false)
    private Tip tip;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(nullable = false)
    private Integer estrellas;      // 1 a 5

    public PuntuacionTip() {}

    public PuntuacionTip(Tip tip, Usuario usuario, Integer estrellas) {
        this.tip = tip;
        this.usuario = usuario;
        this.estrellas = estrellas;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Tip getTip() { return tip; }
    public void setTip(Tip tip) { this.tip = tip; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public Integer getEstrellas() { return estrellas; }
    public void setEstrellas(Integer estrellas) { this.estrellas = estrellas; }
}