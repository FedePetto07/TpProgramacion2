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


    public Venta(Cliente clienteComprador, Embarcacion embarcacion, double montoBase) throws DatoErroneoEx {

        if (clienteComprador == null) {
            throw new DatoErroneoEx("La venta debe tener un cliente comprador.");
        }

        if (embarcacion == null) {
            throw new DatoErroneoEx("La venta debe tener una embarcación asignada.");
        }

        if (montoBase <= 0 || !Double.isFinite(montoBase)) {
            throw new DatoErroneoEx("El monto base debe ser mayor a 0.");
        }

        if (reporteVenta == null) {
            throw new DatoErroneoEx("La venta debe tener un reporte asociado.");
        }

        if (montoFinal <= 0 || !Double.isFinite(montoFinal)) {
            throw new DatoErroneoEx("El monto final debe ser mayor a 0.");
        }

        if (montoFinal < montoBase) {
            throw new DatoErroneoEx("El monto final no puede ser menor al monto base.");
        }

        this.clienteComprador = clienteComprador;
        this.embarcacion = embarcacion;
        this.montoBase = montoBase;
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
