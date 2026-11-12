package Clases;

import Enums.Puesto;
import Excepciones.DatoErroneoEx;

import java.time.LocalDate;

public class Empleado extends Persona {

    private Puesto puesto;
    private double salario;

    // Constructor 1: con todos los datos (BDD)
    public Empleado(Long dni, String nombre, int edad, Long telefono, String email,
                    Puesto puesto, double salario, LocalDate fechaIngreso) throws DatoErroneoEx {
        super(dni, nombre, edad, telefono, email, fechaIngreso);

        if (puesto == null) {
            throw new DatoErroneoEx("El puesto no puede ser nulo.");
        }

        this.puesto = puesto;


        this.salario = salario;
    }

    // Constructor 2: empleado que ingresa hoy


    public Empleado(long dni, String nombre, int edad, long telefono, String email, Puesto puesto) throws DatoErroneoEx {
        super(dni, nombre, edad, telefono, email);

        if (puesto == null) {
            throw new DatoErroneoEx("El puesto no puede ser nulo.");
        }

        this.puesto = puesto;

        if (puesto == Puesto.GERENTE) {
            this.salario = 3500.0;
        }else {
            this.salario = 1300.0;
        }
    }

    public Puesto getPuesto() {
        return puesto;
    }

    public double getSalario() {
        return salario;
    }

    @Override
    public String toString() {
        return "\nEMPLEADO" + super.toString() +
                " \n |puesto=" + puesto +
                "| \n |salario=" + salario + "|";
    }
}