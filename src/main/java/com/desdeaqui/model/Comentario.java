package com.desdeaqui.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidad que representa un comentario realizado por un usuario en un tip.
 *
 * Esta clase se mapea con la tabla "comentarios" de la base de datos.
 * Cada comentario pertenece a un tip específico y a un usuario autor.
 */
@Entity
@Table(name = "comentarios")
public class Comentario {

    /**
     * Identificador único del comentario.
     * Se genera automáticamente en la base de datos.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Tip al que pertenece el comentario.
     *
     * Relación muchos a uno:
     * muchos comentarios pueden pertenecer a un mismo tip.
     */
    @ManyToOne
    @JoinColumn(name = "tip_id", nullable = false)
    private Tip tip;

    /**
     * Usuario que realizó el comentario.
     *
     * Relación muchos a uno:
     * muchos comentarios pueden ser realizados por un mismo usuario.
     */
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    /**
     * Contenido textual del comentario.
     *
     * Se almacena como texto largo en la base de datos.
     */
    @Column(columnDefinition = "TEXT", nullable = false)
    private String contenido;

    /**
     * Fecha y hora en que se creó el comentario.
     *
     * Se establece automáticamente al crear un nuevo comentario.
     */
    @Column(name = "creado_en")
    private LocalDateTime creadoEn;


    /**
     * Constructor vacío requerido por JPA.
     */
    public Comentario() {}

    /**
     * Constructor utilizado para crear un nuevo comentario.
     *
     * @param tip tip al que pertenece el comentario.
     * @param usuario usuario que escribe el comentario.
     * @param contenido texto del comentario.
     */
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
