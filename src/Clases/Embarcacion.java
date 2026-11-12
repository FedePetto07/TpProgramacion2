package Clases;

import Excepciones.DatoErroneoEx;
import Interfaces.CalcularPrecio;
import Interfaces.Identificable;

import java.util.Objects;

public abstract class Embarcacion implements Identificable<String> {

    /// ATRIBUTOS

    private String nombre;
    private String matricula;
    private double precioDiario;
    private boolean disponible;
    private int capacidadPersonas;

    /// CONSTRUCTOR

    public Embarcacion(String nombre, String matricula, double precioDiario, boolean disponible, int capacidadPersonas) throws DatoErroneoEx {

        if (nombre == null || nombre.isBlank()) {
            throw new DatoErroneoEx("El nombre no puede estar vacío.");
        }

        if (matricula == null || !matricula.matches("[A-Z]{3}[0-9]{3}")) {
            throw new DatoErroneoEx("La matrícula debe tener el formato ABC123.");
        }

        if (precioDiario < 10000) {
            throw new DatoErroneoEx("El precio diario debe ser como mínimo $10.000.");
        }

        if (capacidadPersonas < 2) {
            throw new DatoErroneoEx("La capacidad mínima es de 2 personas.");
        }

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

    @Override
    public String getID() {
        return this.matricula;
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
