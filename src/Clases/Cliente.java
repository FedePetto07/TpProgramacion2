package Clases;

import java.time.LocalDate;

public class Cliente extends Persona {
    private int numeroLicencia;
    private LocalDate fechaNacimiento;
    private int cantidadAlquileres;
    private LocalDate fechaIngreso;

    public Cliente(int dni, String nombre, int edad, int telefono, String email,
                   int numeroLicencia, LocalDate fechaNacimiento, int cantidadAlquileres) {
        super(dni, nombre, edad, telefono, email);
        this.numeroLicencia = numeroLicencia;
        this.fechaNacimiento = fechaNacimiento;
        this.cantidadAlquileres = cantidadAlquileres;
        this.fechaIngreso = LocalDate.now();
    }

    // Segundo constructor en caso de que el cliente no posea licencia
    public Cliente(int dni, String nombre, int edad, int telefono, String email,
                   LocalDate fechaNacimiento, int cantidadAlquileres) {
        super(dni, nombre, edad, telefono, email);
        this.fechaNacimiento = fechaNacimiento;
        this.cantidadAlquileres = cantidadAlquileres;
        this.fechaIngreso = LocalDate.now();
    }

    public int getNumeroLicencia() {
        return numeroLicencia;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public int getCantidadAlquileres() {
        return cantidadAlquileres;
    }

    public LocalDate getFechaIngreso() {
        return fechaIngreso;
    }

    public boolean esRecurrente(int mesesMinimos, int alquileresMinimos) {
        LocalDate limite = LocalDate.now().minusMonths(mesesMinimos);
        return fechaIngreso.isAfter(limite) || cantidadAlquileres >= alquileresMinimos;
    }

    @Override
    public String toString() {
        return "\nCLIENTE" + super.toString() +
                " \n |numeroLicencia=" + numeroLicencia +
                "| \n |fechaNacimiento=" + fechaNacimiento +
                "| \n |cantidadAlquileres=" + cantidadAlquileres +
                "| \n |fechaIngreso=" + fechaIngreso + "|";
    }
}