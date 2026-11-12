package Clases;

import Enums.Puesto;
import Interfaces.Identificable;

import java.time.LocalDate;
import java.util.Objects;

public abstract class Persona implements Identificable<Long> {

    private long dni;
    private String nombre;
    private int edad;
    private long telefono;
    private String email;
    private LocalDate fechaIngreso;

    public Persona(long dni, String nombre, int edad, long telefono, String email) {
        this.dni = dni;
        this.nombre = nombre;
        this.edad = edad;
        this.telefono = telefono;
        this.email = email;
        this.fechaIngreso = LocalDate.now();
    }

    public Persona(long dni, String nombre, int edad, long telefono, String email, LocalDate fechaIngreso) {
        this.dni = dni;
        this.nombre = nombre;
        this.edad = edad;
        this.telefono = telefono;
        this.email = email;
        this.fechaIngreso = fechaIngreso;
    }

    @Override
    public Long getID() {
        return dni;
    }

    public String getNombre() {
        return nombre;
    }

    public int getEdad() { return edad; }

    public Long getTelefono() {
        return telefono;
    }

    public String getEmail() {
        return email;
    }

    public LocalDate getFechaIngreso() {
        return fechaIngreso;
    }

    @Override
    public String toString() {
        return "\nPERSONA" +
                " \n |dni=" + dni +
                "| \n |nombre=" + nombre +
                "| \n |Edad=" + edad +
                "| \n |telefono=" + telefono +
                "| \n |email=" + email +
                "| \n |fechaIngreso=" + fechaIngreso +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Persona persona)) return false;
        return dni == persona.dni;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(dni);
    }
}
