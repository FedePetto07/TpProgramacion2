package Clases;
import Interfaces.CalcularPrecio;
import Interfaces.Identificable;

public class Venta implements Identificable<Integer> {
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
    public Venta(int id, Cliente clienteComprador, Embarcacion embarcacion, double montoBase, Reporte reporteVenta, double montoFinal) {
        this.id = id;
        this.clienteComprador = clienteComprador;
        this.embarcacion = embarcacion;
        this.montoBase = montoBase;
        this.reporteVenta = null;
        this.montoFinal = montoBase;
        this.reporteVenta = reporteVenta;
        this.montoFinal = montoFinal;
    }
    public Venta(int id, Cliente clienteComprador, Embarcacion embarcacion, double montoBase) {
        this.id = id;
        this.clienteComprador = clienteComprador;
        this.embarcacion = embarcacion;
        this.montoBase = montoBase;
        this.reporteVenta = null;
        this.montoFinal = montoBase;
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
       return this.reporteVenta = new Reporte(this.id, descripcion, coste, cargoAgencia);
    }

    public void setMontoFinal(double montoFinal) {
        this.montoFinal = montoFinal;
    }

    public void calcularPrecio() {
        double monto = montoFinal;
        if (reporteVenta != null){
            if (reporteVenta.isCargoAgencia()){
                monto = monto - reporteVenta.getCoste();
            }
            else {
                monto = monto + reporteVenta.getCoste();
            }
        }

        setMontoFinal(monto);
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
