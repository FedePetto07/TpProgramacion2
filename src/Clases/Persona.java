package Clases;

import Enums.Puesto;
import Excepciones.DatoErroneoEx;
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

    public Persona(long dni, String nombre, int edad, long telefono, String email) throws DatoErroneoEx {

        if (String.valueOf(dni).length() != 8) {
            throw new DatoErroneoEx("El DNI debe tener exactamente 8 dígitos.");
        }

        if (nombre == null || nombre.isBlank() || nombre.matches(".*\\d.*")) {
            throw new DatoErroneoEx("El nombre no puede estar vacío ni contener números.");
        }

        if (String.valueOf(telefono).length() != 10) {
            throw new DatoErroneoEx("El teléfono debe tener exactamente 10 dígitos.");
        }

        if (edad < 18) {
            throw new DatoErroneoEx("El cliente no puede ser menor de 18");
        }

        if (email == null || !email.endsWith("@gmail.com")) {
            throw new DatoErroneoEx("El email debe terminar en @gmail.com, y no puede estar vacio.");
        }

        this.dni = dni;
        this.nombre = nombre;
        this.edad = edad;
        this.telefono = telefono;
        this.email = email;
        this.fechaIngreso = LocalDate.now();
    }

    /// CONSTRUCTOR BDD

    public Persona(long dni, String nombre, int edad, long telefono, String email, LocalDate fechaIngreso) throws DatoErroneoEx {

        if (String.valueOf(dni).length() != 8) {
            throw new DatoErroneoEx("El DNI debe tener exactamente 8 dígitos.");
        }

        if (nombre == null || nombre.isBlank() || nombre.matches(".*\\d.*")) {
            throw new DatoErroneoEx("El nombre no puede estar vacío ni contener números.");
        }

        if (String.valueOf(telefono).length() != 10) {
            throw new DatoErroneoEx("El teléfono debe tener exactamente 10 dígitos.");
        }

        if (edad < 18) {
            throw new DatoErroneoEx("El cliente no puede ser menor de 18");
        }

        if (email == null || !email.endsWith("@gmail.com")) {
            throw new DatoErroneoEx("El email debe terminar en @gmail.com, y no puede estar vacio.");
        }

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

    public void setTelefono(long telefono) throws DatoErroneoEx {
        if (String.valueOf(telefono).length() != 10) {
            throw new DatoErroneoEx("El teléfono debe tener exactamente 10 dígitos.");
        }

        this.telefono = telefono;
    }

    public void setEmail(String email) throws DatoErroneoEx {

        if (email == null || !email.endsWith("@gmail.com")) {
            throw new DatoErroneoEx("El email debe terminar en @gmail.com, y no puede estar vacio.");
        }

        this.email = email;
    }

    public void setEdad(int edad) throws DatoErroneoEx {
        if (edad < 18) {
            throw new DatoErroneoEx("El cliente no puede ser menor de 18");
        }

        this.edad = edad;
    }

    public void setNombre(String nombre) throws DatoErroneoEx {

        if (nombre == null || nombre.isBlank() || nombre.matches(".*\\d.*")) {
            throw new DatoErroneoEx("El nombre no puede estar vacío ni contener números.");
        }

        this.nombre = nombre;
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
