package Clases;

import Enums.TipoCombustible;
import Excepciones.DatoErroneoEx;

public class Lancha extends Embarcacion {

    /// ATRIBUTOS

    private int potenciaMotor;
    private double longitudMetros;
    private TipoCombustible combustible;

    /// CONSTRUCTOR

    public Lancha(String nombre, String matricula, double precioDiario, boolean disponible, int potenciaMotor, double longitudMetros, int capacidadPersonas, TipoCombustible combustible) throws DatoErroneoEx {
        super(nombre, matricula, precioDiario, disponible, capacidadPersonas);

        if (potenciaMotor <= 15) {
            throw new DatoErroneoEx("La potencia del motor debe ser mayor a 15.");
        }

        if (longitudMetros < 3) {
            throw new DatoErroneoEx("La longitud debe ser mayor a 3 metros.");
        }

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

    @Override
    public double calcularPrecio() {
        Double precio = 0.0;

        precio += getCapacidadPersonas()*500;
        precio += longitudMetros*1000;
        precio += potenciaMotor*100;

        return precio;
    }

    /// ToString

    @Override
    public String toString() {
        return "\nLANCHA" + super.toString() +
                " \n |potenciaMotor=" + potenciaMotor +
                "| \n |longitudMetros=" + longitudMetros +
                "| \n |combustible=" + combustible +
                '|';
    }

}
