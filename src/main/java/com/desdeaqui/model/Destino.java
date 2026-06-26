package com.desdeaqui.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "destinos")
public class Destino {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(length = 255)
    private String imagen;

    @Column(length = 50)
    private String zona; // pacifico | caribe | norte | sur | valle

    @Column(length = 50)
    private String interes; // playa | aventura | naturaleza | cultura

    @Column(length = 10)
    private String presupuesto; // $ | $$ | $$$

    @OneToMany(mappedBy = "destino", cascade = CascadeType.ALL)
    private List<Tip> tips; // un destino tiene muchos tips

    // ── Constructores ──────────────────────────────
    public Destino() {
    }

    public Destino(String nombre, String descripcion, String imagen,
            String zona, String interes, String presupuesto) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.imagen = imagen;
        this.zona = zona;
        this.interes = interes;
        this.presupuesto = presupuesto;
    }

    // ── Getters y Setters ──────────────────────────
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public List<Tip> getTips() {
        return tips;
    }

    public void setTips(List<Tip> tips) {
        this.tips = tips;
    }

    public String getZona() {
        return zona;
    }

    public void setZona(String zona) {
        this.zona = zona;
    }

    public String getInteres() {
        return interes;
    }

    public void setInteres(String interes) {
        this.interes = interes;
    }

    public String getPresupuesto() {
        return presupuesto;
    }

    public void setPresupuesto(String presupuesto) {
        this.presupuesto = presupuesto;
    }
}