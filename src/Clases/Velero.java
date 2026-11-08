package Clases;

public class Velero extends Embarcacion {

    /// ATRIBUTOS

    private int cantidadVelas;
    private Double longitudMetros;
    private int capacidadPersonas;
    private int cantidadCamarotes;
    private boolean requiereCapitan;

    /// CONSTRUCTOR

    public Velero(String nombre, String matricula, double precioDiario, boolean disponible, int cantidadVelas, Double longitudMetros, int capacidadPersonas, int cantidadCamarotes, boolean requiereCapitan) {
        super(nombre, matricula, precioDiario, disponible);
        this.cantidadVelas = cantidadVelas;
        this.longitudMetros = longitudMetros;
        this.capacidadPersonas = capacidadPersonas;
        this.cantidadCamarotes = cantidadCamarotes;
        this.requiereCapitan = requiereCapitan;
    }

    /// GETTERS

    public int getCantidadVelas() {
        return cantidadVelas;
    }

    public Double getLongitudMetros() {
        return longitudMetros;
    }

    public int getCapacidadPersonas() {
        return capacidadPersonas;
    }

    public int getCantidadCamarotes() {
        return cantidadCamarotes;
    }

    public boolean isRequiereCapitan() {
        return requiereCapitan;
    }

    /// ToString

    @Override
    public String toString() {
        return "\nVELERO" + super.toString() +
                "\n |cantidadVelas: " + cantidadVelas +
                "|\n |longitudMetros: " + longitudMetros +
                "|\n |capacidadPersonas: " + capacidadPersonas +
                "|\n |cantidadCamarotes: " + cantidadCamarotes +
                "|\n |requiereCapitan: " + requiereCapitan +
                '|';
    }

}
