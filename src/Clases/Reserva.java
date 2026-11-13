package Clases;

import Enums.ESTADO_RESERVA;
import Excepciones.DatoErroneoEx;
import Interfaces.Identificable;

import java.time.DateTimeException;
import java.time.LocalDate;

public class Reserva implements Identificable<Integer> {
    ///- int id, Cliente cliente ,Embarcacion embarcacion ,
    ///LocalDate fechaInicio, LocalDate fechaFin ,EstadoReserva estado
    private int id;
    private Cliente cliente;
    private int cantidadPersonas;
    private Embarcacion embarcacion;
    private LocalDate fechaDeInicio;
    private LocalDate fechaDeFin;
    private ESTADO_RESERVA estadoReserva;

    /// CONSTRUCTOR BDD

    public Reserva(int id, Cliente cliente, int cantidadPersonas, Embarcacion embarcacion, int añoInicio, int mesInicio, int diaInicio, int añoFin, int mesFin, int diaFin, ESTADO_RESERVA estadoReserva) throws DatoErroneoEx {
        this.id = id;
        this.cliente = cliente;
        this.cantidadPersonas = cantidadPersonas;
        this.embarcacion = embarcacion;
        this.fechaDeInicio = LocalDate.of(añoInicio,mesInicio,diaInicio);
        this.fechaDeFin = LocalDate.of(añoFin,mesFin,diaFin);
        this.estadoReserva = estadoReserva;
    }

    public Reserva(Cliente cliente, int cantidadPersonas, Embarcacion embarcacion, int añoInicio, int mesInicio, int diaInicio, int añoFin, int mesFin, int diaFin, ESTADO_RESERVA estadoReserva) throws DatoErroneoEx {

        if (cliente == null) {
            throw new DatoErroneoEx("La reserva debe tener un cliente asignado.");
        }

        if (cantidadPersonas <= 0) {
            throw new DatoErroneoEx("La cantidad de personas debe ser mayor a 0.");
        }

        if (embarcacion == null) {
            throw new DatoErroneoEx("La reserva debe tener una embarcación asignada.");
        }

        if (cantidadPersonas > embarcacion.getCapacidadPersonas()) {
            throw new DatoErroneoEx("La cantidad de personas supera la capacidad de la embarcación.");
        }

        if (estadoReserva == null) {
            throw new DatoErroneoEx("El estado de la reserva no puede ser nulo.");
        }

        try {
            this.fechaDeInicio = LocalDate.of(añoInicio, mesInicio, diaInicio);
            this.fechaDeFin = LocalDate.of(añoFin, mesFin, diaFin);
        } catch (DateTimeException e) {
            throw new DatoErroneoEx("La fecha ingresada no es válida.");
        }

        if (fechaDeInicio.isBefore(LocalDate.now().plusDays(3))) {
            throw new DatoErroneoEx(
                    "La reserva debe realizarse con al menos 3 días de anticipación."
            );
        }

        if (!fechaDeFin.isAfter(fechaDeInicio)) {
            throw new DatoErroneoEx("La fecha de finalización debe ser posterior a la fecha de inicio.");
        }

        this.cliente = cliente;
        this.cantidadPersonas = cantidadPersonas;
        this.embarcacion = embarcacion;
        this.fechaDeInicio = fechaDeInicio;
        this.fechaDeFin = fechaDeFin;
        this.estadoReserva = estadoReserva;
    }

    ///////
    public Integer getID() {
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

    public int getCantidadPersonas() {return cantidadPersonas;}

    public void setEstadoReserva(ESTADO_RESERVA estadoReserva) {
        this.estadoReserva = estadoReserva;
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
                "| \n |Cantidad de personas=" + estadoReserva +
                "| \n |Datos embarcacion=" + embarcacion.toString() +
                "| \n |Fecha de inicio=" + fechaDeInicio +
                "| \n |Fecha de fin=" + fechaDeFin +
                "| \n |Estado de la reserva=" + estadoReserva +
                '}';
    }



}
