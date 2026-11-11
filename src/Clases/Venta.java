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
    private double montoFinal;


    ////
    public Venta(int id, Cliente clienteComprador, Embarcacion embarcacion, double montoFinal) {
        this.id = id;
        this.clienteComprador = clienteComprador;
        this.embarcacion = embarcacion;
        this.montoFinal = montoFinal;
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
    ////
    @Override
    public String toString()
    {
        return "\nVENTA{" +
                " \n |id=" + id +
                "| \n |Datos cliente=" + clienteComprador.toString() +
                "| \n |Datos embarcacion=" + embarcacion.toString() +
                "| \n |Monto final" + montoFinal +
                '}';
    }
}
