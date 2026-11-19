package Clases;
import Excepciones.AccionImposibleEx;
import Excepciones.DatoErroneoEx;
import Interfaces.CalcularPrecio;
import Interfaces.Identificable;

import java.time.temporal.ChronoUnit;
public class Alquiler implements CalcularPrecio, Identificable<Integer> {
    ///- int id
    /// - Reserva reserva
    /// - Double montoFinal
    private int id;
    private final Reserva reserva;
    private double montoFinal;
    private boolean isFinalizado;

    /// CONSTRUCTOR BDD

    public Alquiler(int id, Reserva reserva, double montoFinal, boolean isFinalizado) {
        this.reserva = reserva;
        this.id = id;
        this.montoFinal = montoFinal;
        this.isFinalizado = isFinalizado;
    }

    public Alquiler(Reserva reserva) throws DatoErroneoEx {

        if (reserva == null) {
            throw new DatoErroneoEx("El alquiler debe estar asociado a una reserva.");
        }

        this.reserva = reserva;
        this.montoFinal = calcularPrecio();
        this.isFinalizado = false;
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
   public boolean cambiarDisponibilidad()
   {
       if(reserva.getEmbarcacion().isDisponible())
       {
           reserva.getEmbarcacion().setDisponible(false);
           return false;
       }
       else
       {
           reserva.getEmbarcacion().setDisponible(true);
           return true;
       }

   }

    public void setId(int id) {
        this.id = id;
    }

    ///
    @Override
    public double calcularPrecio()
    {
        double precioDiario = reserva.getEmbarcacion().getPrecioDiario();
        long cantDias = ChronoUnit.DAYS.between(reserva.getFechaDeInicio(),reserva.getFechaDeFin());
        this.montoFinal += cantDias*precioDiario;
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
