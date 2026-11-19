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
    private final Cliente clienteComprador;
    private final Embarcacion embarcacion;
    private double monto;


    /// CONSTRUCTOR BDD
    public Venta(int id, Cliente clienteComprador, Embarcacion embarcacion, double monto) {
        this.id = id;
        this.clienteComprador = clienteComprador;
        this.embarcacion = embarcacion;
        this.monto = monto;
    }


    public Venta(Cliente clienteComprador, Embarcacion embarcacion) throws DatoErroneoEx {

        if (clienteComprador == null) {
            throw new DatoErroneoEx("La venta debe tener un cliente comprador.");
        }

        if (embarcacion == null) {
            throw new DatoErroneoEx("La venta debe tener una embarcación asignada.");
        }


        this.clienteComprador = clienteComprador;
        this.embarcacion = embarcacion;
        this.monto = embarcacion.calcularPrecio();
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

    public double getMonto() {
        return monto;
    }

    public void setId(int id) {
        this.id = id;
    }

    ////
    @Override
    public String toString()
    {

        return "\nVENTA{" +
                " \n |id:" + id +
                "| \n |Datos cliente:" + clienteComprador.toString() +
                "| \n |Datos embarcacion:" + embarcacion.toString() +
                '|';
    }
}
