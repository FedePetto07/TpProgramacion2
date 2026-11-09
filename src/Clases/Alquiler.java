package Clases;
import Interfaces.CalcularPrecio;
import Interfaces.Identificable;

import java.time.temporal.ChronoUnit;
public class Alquiler implements CalcularPrecio, Identificable<Integer> {
    ///- int id
    /// - Reserva reserva
    /// - Double montoFinal
    private int id;
    private Reserva reserva;
    private double montoFinal;

    public Alquiler(int id, Reserva reserva) {
        this.id = id;
        this.reserva = reserva;
    }

    @Override
    public Integer getID() {
        return id;
    }

    public Reserva getReserva() {
        return reserva;
    }

    public double getMontoFinal() {
        return montoFinal;
    }
    ///
    @Override
    public double calcularPrecio()
    {
        double precioDiario = reserva.getEmbarcacion().getPrecioDiario();
        long cantDias = ChronoUnit.DAYS.between(reserva.getFechaDeInicio(),reserva.getFechaDeFin());
        this.montoFinal = cantDias*precioDiario;

        return montoFinal;
    }
    ///////////
    public String toString()
    {
        return "\nAlquiler{" +
                " \n |id=" + id +
                "| \n |Datos reserva=" + reserva.toString() +
                "| \n |Monto final=" + montoFinal +
                '}';
    }
}
