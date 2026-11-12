package Clases;
import Excepciones.DatoErroneoEx;
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

    /// CONSTRUCTOR BDD

    public Alquiler(int id, Reserva reserva) {
        this.reserva = reserva;
        this.id = id;
        this.montoFinal = calcularPrecio();
    }

    public Alquiler(Reserva reserva) throws DatoErroneoEx {

        if (reserva == null) {
            throw new DatoErroneoEx("El alquiler debe estar asociado a una reserva.");
        }

        this.reserva = reserva;
        this.montoFinal = calcularPrecio();
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
   public void cambiarDisponibilidad()
   {
       if(reserva.getEmbarcacion().isDisponible() == true)
       {
           reserva.getEmbarcacion().setDisponible(false);
       }
       else
       {
           reserva.getEmbarcacion().setDisponible(true);
       }

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
