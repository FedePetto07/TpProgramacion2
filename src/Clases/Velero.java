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

    public void setRequiereCapitan(boolean requiereCapitan) {
        this.requiereCapitan = requiereCapitan;
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
