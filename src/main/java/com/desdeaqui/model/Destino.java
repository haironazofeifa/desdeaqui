package com.desdeaqui.model;

import jakarta.persistence.*;
import java.util.List;

/**
 * Entidad que representa un destino turístico dentro del sistema Desde Aquí.
 *
 * Esta clase se mapea con la tabla "destinos" de la base de datos.
 * Cada destino contiene información como nombre, descripción, imagen,
 * zona geográfica, tipo de interés y nivel de presupuesto.
 *
 * Además, un destino puede tener muchos tips asociados.
 */
@Entity
@Table(name = "destinos")
public class Destino {

    /**
     * Identificador único del destino.
     * Se genera automáticamente en la base de datos.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Nombre del destino turístico.
     * Ejemplo: Tamarindo, Tortuguero, Volcán Arenal.
     */
    @Column(nullable = false, length = 100)
    private String nombre;

    /**
     * Descripción general del destino.
     */
    @Column(columnDefinition = "TEXT")
    private String descripcion;

    /**
     * Nombre o ruta de la imagen asociada al destino.
     */
    @Column(length = 255)
    private String imagen;

    /**
     * Zona geográfica del destino.
     * Ejemplos: pacifico, caribe, norte, sur, valle.
     */
    @Column(length = 50)
    private String zona; // pacifico | caribe | norte | sur | valle

    /**
     * Tipo de interés principal del destino.
     * Ejemplos: playa, aventura, naturaleza, cultura.
     */
    @Column(length = 50)
    private String interes; // playa | aventura | naturaleza | cultura

    /**
     * Nivel de presupuesto estimado para visitar el destino.
     * Ejemplos: $, $$, $$$.
     */
    @Column(length = 10)
    private String presupuesto; // $ | $$ | $$$

    /**
     * Lista de tips asociados al destino.
     *
     * Relación uno a muchos:
     * un destino puede tener muchos tips.
     *
     * Con cascade = CascadeType.ALL, las operaciones realizadas sobre
     * un destino pueden propagarse a sus tips relacionados.
     */
    @OneToMany(mappedBy = "destino", cascade = CascadeType.ALL)
    private List<Tip> tips; // un destino tiene muchos tips

    /**
     * Constructor vacío requerido por JPA.
     */
    public Destino() {
    }

    /**
     * Constructor con parámetros para crear una instancia de Destino.
     *
     * @param nombre        el nombre del destino
     * @param descripcion   la descripción del destino
     * @param imagen        el nombre o ruta de la imagen asociada
     * @param zona          la zona geográfica del destino
     * @param interes         el tipo de interés del destino
     * @param presupuesto   el nivel de presupuesto estimado
     */
    public Destino(String nombre, String descripcion, String imagen,
            String zona, String interes, String presupuesto) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.imagen = imagen;
        this.zona = zona;
        this.interes = interes;
        this.presupuesto = presupuesto;
    }

    
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