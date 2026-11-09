package Clases;

import java.time.LocalDate;

public class Reserva  {
    ///- int id, Cliente cliente ,Embarcacion embarcacion ,
    ///LocalDate fechaInicio, LocalDate fechaFin ,EstadoReserva estado
    private int id;
    private Cliente cliente;
    private Embarcacion embarcacion;
    private LocalDate fechaDeInicio;
    private LocalDate fechaDeFin;
    private ESTADO_RESERVA estadoReserva;

    public Reserva(int id, Cliente cliente, Embarcacion embarcacion, int añoInicio, int mesInicio, int diaInicio, int añoFin, int mesFin, int diaFin, ESTADO_RESERVA estadoReserva) {
        this.id = id;
        this.cliente = cliente;
        this.embarcacion = embarcacion;
        this.fechaDeInicio = LocalDate.of(añoInicio,mesInicio,diaInicio);
        this.fechaDeFin = LocalDate.of(añoFin,mesFin,diaFin);
        this.estadoReserva = estadoReserva;
    }
    ///////
    public int getId() {
        return id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Embarcacion getEmbarcacion() {
        return embarcacion;
    }

    public LocalDate getFechaDeInicio() {
        return fechaDeInicio;
    }

    public LocalDate getFechaDeFin() {
        return fechaDeFin;
    }

    public ESTADO_RESERVA getEstadoReserva() {
        return estadoReserva;
    }
    ////////////////////

    public void confirmar()
    {
        this.estadoReserva = ESTADO_RESERVA.CONFIRMADA;
    }
    public void cancelar()
    {
        this.estadoReserva = ESTADO_RESERVA.CANCELADA;
    }

    ///
    @Override
    public String toString()
    {
        return "\nRESERVA{" +
                " \n |id=" + id +
                "| \n |Datos cliente=" + cliente.toString() +
                "| \n |Datos embarcacion=" + embarcacion.toString() +
                "| \n |Fecha de inicio=" + fechaDeInicio +
                "| \n |Fecha de fin=" + fechaDeFin +
                "| \n |Estado de la reserva=" + estadoReserva +
                '}';
    }



}
