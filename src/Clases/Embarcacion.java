package Clases;

import java.util.Objects;

public class Embarcacion {

    /// ATRIBUTOS

    private String nombre;
    private String matricula;
    private double precioDiario;
    private boolean disponible;
    private int capacidadPersonas;

    /// CONSTRUCTOR

    public Embarcacion(String nombre, String matricula, double precioDiario, boolean disponible, int capacidadPersonas) {
        this.nombre = nombre;
        this.matricula = matricula;
        this.precioDiario = precioDiario;
        this.disponible = disponible;
        this.capacidadPersonas = capacidadPersonas;
    }

    /// GETTERS

    public String getNombre() {
        return nombre;
    }

    public String getMatricula() {
        return matricula;
    }

    public double getPrecioDiario() {
        return precioDiario;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public int getCapacidadPersonas() {return capacidadPersonas;}

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    public void setCapacidadPersonas(int capacidadPersonas) {
        this.capacidadPersonas = capacidadPersonas;
    }

    public void setPrecioDiario(double precioDiario) {
        this.precioDiario = precioDiario;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /// ToString

    @Override
    public String toString() {
        return  "\n |nombre:'" + nombre + '\'' +
                "|\n |matricula:'" + matricula + '\'' +
                "|\n |precioDiario:" + precioDiario +
                "|\n |disponible:" + disponible +
                "|\n |capacidadPersonas:" + capacidadPersonas +
                '|';
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Embarcacion that)) return false;
        return Objects.equals(matricula, that.matricula);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(matricula);
    }


}
