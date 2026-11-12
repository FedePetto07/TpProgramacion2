package Clases;

import Excepciones.DatoErroneoEx;

import java.time.LocalDate;

public class Cliente extends Persona {
    private int numeroLicencia;
    private int cantidadAlquileres;

    /// CONSTRUCTOR BDD

    public Cliente(Long dni, String nombre, int edad, Long telefono, String email, LocalDate fechaIngreso, int numeroLicencia, int cantidadAlquileres) throws DatoErroneoEx {
        super(dni, nombre, edad, telefono, email, fechaIngreso);
        this.numeroLicencia = numeroLicencia;
        this.cantidadAlquileres = cantidadAlquileres;
    }

    public Cliente(Long dni, String nombre, int edad, Long telefono, String email, int numeroLicencia) throws DatoErroneoEx {
        super(dni, nombre, edad, telefono, email);

        if (numeroLicencia != 0 && (numeroLicencia < 5 || numeroLicencia > 9)) {
            throw new DatoErroneoEx("La licencia debe ser 0 o tener un código entre 5 y 9.");
        }

        this.numeroLicencia = numeroLicencia;
        this.cantidadAlquileres = 0;
    }


    public int getNumeroLicencia() {
        return numeroLicencia;
    }


    public int getCantidadAlquileres() {
        return cantidadAlquileres;
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