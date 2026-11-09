package Clases;

import java.time.LocalDate;

public class Cliente extends Persona{
    private int numeroLicencia;
    private LocalDate fechaNacimiento;
    private int cantidadAlquileres;

    public Cliente(int dni, String nombre, int edad, int telefono, String email,
                   int numeroLicencia, LocalDate fechaNacimiento, int cantidadAlquileres) {
        super(dni, nombre, edad, telefono, email);
        this.numeroLicencia = numeroLicencia;
        this.fechaNacimiento = fechaNacimiento;
        this.cantidadAlquileres = cantidadAlquileres;
    }

    //Segundo Constructor en caso de que el cliente no posee licencia
    public Cliente(int dni, String nombre, int edad, int telefono, String email,
                   LocalDate fechaNacimiento) {
        this(dni, nombre, edad, telefono, email, 0, fechaNacimiento, 0);
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

    @Override
    public String toString() {
        return "\nCLIENTE" + super.toString() +
                " \n |numeroLicencia=" + numeroLicencia +
                "| \n |fechaNacimiento=" + fechaNacimiento +
                "| \n |cantidadAlquileres=" + cantidadAlquileres + "|";
    }

}
