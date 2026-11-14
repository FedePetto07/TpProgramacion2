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
    private Reserva reserva;
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

    public void setMontoFinal(double montoFinal) throws AccionImposibleEx {
        if(isFinalizado){
            throw new AccionImposibleEx("El alquiler esta finalizado, por ende no se puede cambiar el monto");
        }
        this.montoFinal = montoFinal;
    }

    public void finalizarAlquiler()
   {
       this.isFinalizado = true;
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
