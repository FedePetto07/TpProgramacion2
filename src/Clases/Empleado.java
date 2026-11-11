package Clases;

import Enums.Puesto;

import java.time.LocalDate;

public class Empleado extends Persona {

    private Puesto puesto;
    private double salario;

    // Constructor 1: con todos los datos (BDD)
    public Empleado(int dni, String nombre, int edad, int telefono, String email,
                    Puesto puesto, double salario, LocalDate fechaIngreso) {
        super(dni, nombre, edad, telefono, email, fechaIngreso);
        this.puesto = puesto;
        this.salario = salario;
    }

    // Constructor 2: empleado que ingresa hoy
    public Empleado(int dni, String nombre, int edad, int telefono, String email,
                    Puesto puesto, double salario) {
        this(dni, nombre, edad, telefono, email, puesto, salario, LocalDate.now());
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