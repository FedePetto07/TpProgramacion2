package Clases;

import Enums.TipoCombustible;

public class Lancha extends Embarcacion {

    /// ATRIBUTOS

    private int potenciaMotor;
    private double longitudMetros;
    private int capacidadPersonas;
    private TipoCombustible combustible;

    /// CONSTRUCTOR

    public Lancha(String nombre, String matricula, double precioDiario, boolean disponible, int potenciaMotor, double longitudMetros, int capacidadPersonas, TipoCombustible combustible) {
        super(nombre, matricula, precioDiario, disponible);
        this.potenciaMotor = potenciaMotor;
        this.longitudMetros = longitudMetros;
        this.capacidadPersonas = capacidadPersonas;
        this.combustible = combustible;
    }

    /// GETTERS

    public int getPotenciaMotor() {
        return potenciaMotor;
    }

    public double getLongitudMetros() {
        return longitudMetros;
    }

    public int getCapacidadPersonas() {
        return capacidadPersonas;
    }

    public TipoCombustible getCombustible() {
        return combustible;
    }

    /// ToString

    @Override
    public String toString() {
        return "\nLANCHA" + super.toString() +
                " \n |potenciaMotor=" + potenciaMotor +
                "| \n |longitudMetros=" + longitudMetros +
                "| \n |capacidadPersonas=" + capacidadPersonas +
                "| \n |combustible=" + combustible +
                '}';
    }

}
