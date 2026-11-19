package Clases;

import Excepciones.DatoErroneoEx;

public class Velero extends Embarcacion {

    /// ATRIBUTOS

    private int cantidadVelas;
    private Double longitudMetros;
    private int cantidadCamarotes;
    private boolean requiereCapitan;

    /// CONSTRUCTOR BDD

    public Velero(String nombre, String matricula, double precioDiario, boolean disponible, int cantidadVelas, Double longitudMetros, int capacidadPersonas, int cantidadCamarotes, boolean requiereCapitan) throws DatoErroneoEx {
        super(nombre, matricula, precioDiario, disponible, capacidadPersonas);
        this.cantidadVelas = cantidadVelas;
        this.longitudMetros = longitudMetros;
        this.cantidadCamarotes = cantidadCamarotes;
        this.requiereCapitan = requiereCapitan;
    }

    public Velero(String nombre, String matricula, double precioDiario, boolean disponible, int cantidadVelas, Double longitudMetros, int capacidadPersonas, int cantidadCamarotes) throws DatoErroneoEx {
        super(nombre, matricula, precioDiario, disponible, capacidadPersonas);

        if (getCapacidadPersonas() > 12 || getCapacidadPersonas() <= 1) {
            throw new DatoErroneoEx("Las motos de agua tienen que tener de capacidad entre 2 y 12");
        }

        if (cantidadVelas < 1) {
            throw new DatoErroneoEx("El velero debe tener al menos una vela.");
        }

        if (longitudMetros == null || longitudMetros <= 5) {
            throw new DatoErroneoEx("La longitud debe ser mayor a 5.");
        }

        if (cantidadCamarotes < 0) {
            throw new DatoErroneoEx("La cantidad de camarotes no puede ser negativa.");
        }

        this.cantidadVelas = cantidadVelas;
        this.longitudMetros = longitudMetros;
        this.cantidadCamarotes = cantidadCamarotes;
        this.requiereCapitan = false;
    }

    /// GETTERS

    public int getCantidadVelas() {
        return cantidadVelas;
    }

    public Double getLongitudMetros() {
        return longitudMetros;
    }

    public int getCantidadCamarotes() {
        return cantidadCamarotes;
    }

    public boolean isRequiereCapitan() {
        return requiereCapitan;
    }

    /// SETTER Y METODOS

    public void setRequiereCapitan(boolean requiereCapitan) {
        this.requiereCapitan = requiereCapitan;
    }

    @Override
    public double calcularPrecio() {
        Double precio = 0.0;

        precio += cantidadVelas*1500.0;
        precio += longitudMetros*18000.0;
        precio += cantidadCamarotes*4000.0;
        precio += getCapacidadPersonas()*2000.0;

        return precio;
    }

    /// ToString

    @Override
    public String toString() {
        return "\nVELERO" + super.toString() +
                "\n |cantidadVelas: " + cantidadVelas +
                "|\n |longitudMetros: " + longitudMetros +
                "|\n |cantidadCamarotes: " + cantidadCamarotes +
                "|\n |requiereCapitan: " + requiereCapitan +
                '|';
    }

}
