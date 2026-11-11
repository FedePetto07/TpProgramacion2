package Clases;

import Enums.TipoCombustible;

public class Lancha extends Embarcacion {

    /// ATRIBUTOS

    private int potenciaMotor;
    private double longitudMetros;
    private TipoCombustible combustible;

    /// CONSTRUCTOR

    public Lancha(String nombre, String matricula, double precioDiario, boolean disponible, int potenciaMotor, double longitudMetros, int capacidadPersonas, TipoCombustible combustible) {
        super(nombre, matricula, precioDiario, disponible, capacidadPersonas);
        this.potenciaMotor = potenciaMotor;
        this.longitudMetros = longitudMetros;
        this.combustible = combustible;
    }

    /// GETTERS

    public int getPotenciaMotor() {
        return potenciaMotor;
    }

    public double getLongitudMetros() {
        return longitudMetros;
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
                "| \n |combustible=" + combustible +
                '}';
    }

}
