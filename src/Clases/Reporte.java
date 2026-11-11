package Clases;

public class Reporte {

    private String descripcion;
    private double coste;
    private boolean cargoAgencia;

    public Reporte(String descripcion, double coste, boolean cargoAgencia) {
        this.descripcion = descripcion;
        this.coste = coste;
        this.cargoAgencia = cargoAgencia;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public double getCoste() {
        return coste;
    }

    public boolean isCargoAgencia() {
        return cargoAgencia;
    }

    @Override
    public String toString() {
        return "\nReporte{" +
                " \n |Descripcion: " + descripcion +
                "| \n |Coste: " + coste +
                (cargoAgencia ? "\na cargo de la agencia" : "\na cargo del cliente") +
                "\n}";
    }
}
