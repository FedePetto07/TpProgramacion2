package Clases;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Agencia {

    /// ATRIBUTOS

    private ArrayList<Embarcacion> embarcaciones;
    private HashMap<String, Cliente> clientes;
    private HashMap<String, Empleado> empleados;
    private Generica<Reserva> listaReservas;
    private Generica<Alquiler> listaAlquileres;
    private Generica<Venta> listaVentas;

    /// CONSTRUCTOR

    public Agencia() {
        this.embarcaciones = new ArrayList<>();
        this.clientes = new HashMap<>();
        this.empleados = new HashMap<>();
        this.listaReservas = new Generica<>();
        this.listaAlquileres = new Generica<>();
        this.listaVentas = new Generica<>();
    }



}
