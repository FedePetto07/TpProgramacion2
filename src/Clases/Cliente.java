package Clases;

import java.time.LocalDate;

public class Cliente extends Persona {
    private int numeroLicencia;
    private int cantidadAlquileres;

    /// CONSTRUCTOR BDD

    public Cliente(int dni, String nombre, int edad, int telefono, String email, LocalDate fechaIngreso, int numeroLicencia, int cantidadAlquileres) {
        super(dni, nombre, edad, telefono, email, fechaIngreso);
        this.numeroLicencia = numeroLicencia;
        this.cantidadAlquileres = cantidadAlquileres;
    }

    public Cliente(int dni, String nombre, int edad, int telefono, String email,
                   int numeroLicencia, int cantidadAlquileres) {
        super(dni, nombre, edad, telefono, email);
        this.numeroLicencia = numeroLicencia;
        this.cantidadAlquileres = cantidadAlquileres;
    }

    // Segundo constructor en caso de que el cliente no posea licencia
    public Cliente(int dni, String nombre, int edad, int telefono, String email,
                   int cantidadAlquileres) {
        super(dni, nombre, edad, telefono, email);
        this.numeroLicencia = 0;
        this.cantidadAlquileres = cantidadAlquileres;
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