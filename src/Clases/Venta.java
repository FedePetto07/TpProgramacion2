package Clases;
import Excepciones.DatoErroneoEx;
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


    /// CONSTRUCTOR BDD
    public Venta(int id, Cliente clienteComprador, Embarcacion embarcacion, double montoBase, Reporte reporteVenta, double montoFinal) {
        this.id = id;
        this.clienteComprador = clienteComprador;
        this.embarcacion = embarcacion;
        this.montoBase = montoBase;
        this.reporteVenta = reporteVenta;
        this.montoFinal = montoFinal;
    }


    public Venta(Cliente clienteComprador, Embarcacion embarcacion,
                 double montoBase) throws DatoErroneoEx {

        if (clienteComprador == null) {
            throw new DatoErroneoEx("La venta debe tener un cliente comprador.");
        }

        if (embarcacion == null) {
            throw new DatoErroneoEx("La venta debe tener una embarcación asignada.");
        }

        if (montoBase <= 0 || !Double.isFinite(montoBase)) {
            throw new DatoErroneoEx("El monto base debe ser mayor a 0.");
        }

        this.clienteComprador = clienteComprador;
        this.embarcacion = embarcacion;
        this.montoBase = montoBase;
        this.montoFinal = montoBase;
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

    public Reporte iniciarReporte(String descripcion, double coste,
                                  boolean cargoAgencia) throws DatoErroneoEx {

        this.reporteVenta = new Reporte(this.id, descripcion, coste, cargoAgencia);
        calcularPrecio();

        return this.reporteVenta;
    }

    public void setMontoFinal(double montoFinal) {
        this.montoFinal = montoFinal;
    }

    public void calcularPrecio() {
        double monto = montoBase;

        if (reporteVenta != null) {
            if (reporteVenta.isCargoAgencia()) {
                monto -= reporteVenta.getCoste();
            } else {
                monto += reporteVenta.getCoste();
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
