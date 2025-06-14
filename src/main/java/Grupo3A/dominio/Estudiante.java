package Grupo3A.dominio;

import java.math.BigDecimal;

public class Estudiante extends User {
    private int id;
    private String nombre;
    private int edad;
    private BigDecimal calificacion;

    public Estudiante (int id, String nombre, int edad, BigDecimal calificacion) {
        this.id = id;
        this.nombre = nombre;
        this.edad = edad;
        this.calificacion = calificacion;
    }

    public Estudiante() {

    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id;}

    public String getNombre() { return nombre; }

    public void setNombre(String nombre) { this.nombre = nombre;}

    public int getEdad() { return edad; }

    public void setEdad(int edad) { this.edad = edad; }

    public BigDecimal getCalificacion() { return calificacion; }

    public void setCalificacion(BigDecimal calificacion) { this.calificacion = calificacion; }
}
