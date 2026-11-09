package Clases;

import Interfaces.CalcularPrecio;

public class Venta implements CalcularPrecio {
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

    public int getId() {
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
    public double calcularPrecio() {
        //////falta definir;
        return 0.0;
    }
    //////////////////////////
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
