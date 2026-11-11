package Clases;
import Interfaces.CalcularPrecio;
import Interfaces.Identificable;

public class Venta implements Identificable<Integer>, CalcularPrecio {
    ///- int id
    /// - Cliente comprador
    /// - Embarcacion embarcacion
    /// - Double montoTotal
    private int id;
    private Cliente clienteComprador;
    private Embarcacion embarcacion;
    private double montoBase;
    private Reporte reporteVenta;
    private double montoFinal;


    ////
    public Venta(int id, Cliente clienteComprador, Embarcacion embarcacion, double montoFinal) {
        this.id = id;
        this.clienteComprador = clienteComprador;
        this.embarcacion = embarcacion;
        this.montoFinal = montoFinal;
        this.reporteVenta = null;
    }

    public Integer getID() {
        return id;
    }

    public Cliente getClienteComprador() {
        return clienteComprador;
    }

    public Embarcacion getEmbarcacion() {
        return embarcacion;
    }

    public double getMontoFinal() {
        return montoFinal;
    }

    public Reporte getReporteVenta() {return reporteVenta;}

    public double getMontoBase() {
        return montoBase;
    }

    public Reporte iniciarReporte(String descripcion, double coste, boolean cargoAgencia)
    {
       return this.reporteVenta = new Reporte(descripcion, coste, cargoAgencia);
    }

    public void setMontoFinal(double montoFinal) {
        this.montoFinal = montoFinal;
    }

    @Override
    public double calcularPrecio() {
        double monto = montoFinal;
        if (reporteVenta != null){
            if (reporteVenta.isCargoAgencia()){
                monto = monto - reporteVenta.getCoste();
            }
            else {
                monto = monto + reporteVenta.getCoste();
            }
        }

        return monto;
    }

    ////
    @Override
    public String toString()
    {

        String detalleReporte = (reporteVenta != null) ? reporteVenta.toString() : "Sin incidentes";

        return "\nVENTA{" +
                " \n |id:" + id +
                "| \n |Datos cliente:" + clienteComprador.toString() +
                "| \n |Datos embarcacion:" + embarcacion.toString() +
                "\n |Reporte:" + detalleReporte +
                "| \n |Monto final" + montoFinal +
                '}';
    }
}
