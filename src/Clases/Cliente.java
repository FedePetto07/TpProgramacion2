package Clases;

import Excepciones.DatoErroneoEx;

import java.time.LocalDate;

public class Cliente extends Persona {
    private int numeroLicencia;
    private int cantidadAlquileres;
    private LocalDate fechaIngreso;

    /// CONSTRUCTOR BDD

    public Cliente(long dni, String nombre, int edad, long telefono, String email, LocalDate fechaIngreso, int numeroLicencia, int cantidadAlquileres) throws DatoErroneoEx {
        super(dni, nombre, edad, telefono, email);
        this.numeroLicencia = numeroLicencia;
        this.cantidadAlquileres = cantidadAlquileres;
        this.fechaIngreso = fechaIngreso;
    }

    public Cliente(long dni, String nombre, int edad, long telefono, String email, int numeroLicencia) throws DatoErroneoEx {
        super(dni, nombre, edad, telefono, email);

        if (numeroLicencia != 0 && (numeroLicencia < 5 || numeroLicencia > 9)) {
            throw new DatoErroneoEx("La licencia debe ser 0 o tener un código entre 5 y 9.");
        }

        this.numeroLicencia = numeroLicencia;
        this.cantidadAlquileres = 0;
        this.fechaIngreso = LocalDate.now();
    }

    public void setNumeroLicencia(int numeroLicencia) throws DatoErroneoEx {

        if (numeroLicencia != 0 && (numeroLicencia < 5 || numeroLicencia > 9)) {
            throw new DatoErroneoEx("La licencia debe ser 0 o tener un código entre 5 y 9.");
        }

        this.numeroLicencia = numeroLicencia;
    }

    public void agregarAlquiler() {
        this.cantidadAlquileres = cantidadAlquileres + 1;
    }

    public int getNumeroLicencia() {
        return numeroLicencia;
    }


    public int getCantidadAlquileres() {
        return cantidadAlquileres;
    }

    public LocalDate getFechaIngreso() {
        return fechaIngreso;
    }

    public boolean esRecurrente(int mesesMinimos, int alquileresMinimos) {
        LocalDate limite = LocalDate.now().minusMonths(mesesMinimos);
        return getFechaIngreso().isAfter(limite) || cantidadAlquileres >= alquileresMinimos;
    }

    @Override
    public String toString() {
        return "\nCLIENTE" + super.toString() +
                " \n |numeroLicencia=" + numeroLicencia +
                "| \n |cantidadAlquileres=" + cantidadAlquileres + "|";
    }
}