package Clases;

import Enums.Puesto;
import Interfaces.Identificable;

public abstract class Persona implements Identificable<Integer> {

    private int dni;
    private String nombre;
    private int edad;
    private int telefono;
    private String email;

    public Persona(int dni, String nombre, int edad, int telefono, String email) {
        this.dni = dni;
        this.nombre = nombre;
        this.edad = edad;
        this.telefono = telefono;
        this.email = email;
    }

    @Override
    public Integer getID() {
        return dni;
    }

    public String getNombre() {
        return nombre;
    }

    public int getEdad() { return edad; }

    public int getTelefono() {
        return telefono;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "\nPERSONA" +
                " \n |dni=" + dni +
                "| \n |nombre=" + nombre +
                "| \n |Edad=" + edad +
                "| \n |telefono=" + telefono +
                "| \n |email=" + email +
                '}';
    }
}
