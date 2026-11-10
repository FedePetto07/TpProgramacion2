package Clases;

import Enums.Puesto;
import Enums.TipoCombustible;
import Excepciones.AccionImposibleEx;
import Excepciones.ElementoDuplicadoEx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class Agencia {

    /// ATRIBUTOS

    private HashMap<String, Embarcacion> embarcaciones;
    private ArrayList<Cliente> clientes;
    private ArrayList<Empleado> empleados;
    private Generica<Reserva> listaReservas;
    private Generica<Alquiler> listaAlquileres;
    private Generica<Venta> listaVentas;

    /// CONSTRUCTOR

    public Agencia() {
        this.embarcaciones = new HashMap<>();
        this.clientes = new ArrayList<>();
        this.empleados = new ArrayList<>();
        this.listaReservas = new Generica<>();
        this.listaAlquileres = new Generica<>();
        this.listaVentas = new Generica<>();
    }

    /// EMPLEADOS

    // AGREGAR Y ELIMINAR EMPLEADOS

    public boolean agregarEmpleado(int dni, String nombre, int edad, int telefono, String email, Puesto puesto, double salario) throws AccionImposibleEx, ElementoDuplicadoEx {

        for (Empleado empleado : empleados) {
            if (empleado.getID() == dni) {
                throw new ElementoDuplicadoEx("Ya existe un empleado con ese DNI");
            }
            if (empleado.getEdad() < 18) {
                throw new AccionImposibleEx("El empleado no puede ser menor a 18");
            }
        }

        empleados.add(new Empleado(dni, nombre, edad, telefono, email, puesto, salario));

        return true;
    }

    public boolean eliminarEmpleado(int dni) throws AccionImposibleEx {
        Iterator<Empleado> iterator = empleados.iterator();
        while (iterator.hasNext()) {
            Empleado empleado = iterator.next();
            if (empleado.getID() == dni) {
                iterator.remove();
                return true;
            }
        }

        throw new AccionImposibleEx("No existe un empleado con ese DNI");
    }

    // VER EMPLEADOS

    public StringBuilder listarEmpleados() throws AccionImposibleEx {
        StringBuilder sb = new StringBuilder();

        if (empleados.isEmpty()) {
            throw new AccionImposibleEx("La lista está vacía");
        }

        sb.append("Lista de empleados:\n");

        for (Empleado empleado : empleados) {
            sb.append(empleado.toString());
        }

        return sb;
    }

    public String verEmpleadoId(int dni) throws AccionImposibleEx {
        for (Empleado empleado : empleados) {
            if (empleado.getID() == dni) {
                return empleado.toString();
            }
        }

        throw new AccionImposibleEx("No existe un empleado con ese DNI");
    }

    /// CLIENTES


    /// EMBARCACIONES

    // AGREGAR Y ELIMINAR EMBARCACIONES

    public boolean registrarVelero(String nombre, String matricula, double precioDiario, boolean disponible, int cantidadVelas, Double longitudMetros, int capacidadPersonas, int cantidadCamarotes, boolean requiereCapitan) throws ElementoDuplicadoEx {
        if (embarcaciones.containsKey(matricula)) {
            throw new ElementoDuplicadoEx("Ya existe un velero con esa matricula");
        }

        embarcaciones.put(matricula, new Velero(nombre, matricula, precioDiario, disponible, cantidadVelas, longitudMetros, capacidadPersonas, cantidadCamarotes, requiereCapitan));
        return true;
    }

    public boolean registrarMotoDeAgua(String nombre, String matricula, double precioDiario, boolean disponible, int cilindrada, double limiteVelocidad, boolean requiereLicencia) throws ElementoDuplicadoEx {
        if (embarcaciones.containsKey(matricula)) {
            throw new ElementoDuplicadoEx("Ya existe una moto de agua con esa matricula");
        }

        embarcaciones.put(matricula, new MotoDeAgua(nombre, matricula, precioDiario, disponible, cilindrada, limiteVelocidad, requiereLicencia));
        return true;
    }

    public boolean registrarLancha(String nombre, String matricula, double precioDiario, boolean disponible, int potenciaMotor, double longitudMetros, int capacidadPersonas, TipoCombustible combustible) throws ElementoDuplicadoEx {
        if (embarcaciones.containsKey(matricula)){
            throw new ElementoDuplicadoEx("Ya existe una lancha con esa matricula");
        }

        embarcaciones.put(matricula, new Lancha(nombre, matricula, precioDiario, disponible, potenciaMotor, longitudMetros, capacidadPersonas, combustible));
        return true;
    }

    public boolean eliminarEmbarcacion(String matricula) throws AccionImposibleEx
    {
        if (embarcaciones.containsKey(matricula)) {
            embarcaciones.remove(matricula);
            return true;
        }

        throw new AccionImposibleEx("No existe un embarcacion con esa matricula");
    }

    // VER EMBARCACIONES

    public StringBuilder listarEmbarcaciones() throws AccionImposibleEx
    {
        if (embarcaciones.isEmpty()) {
            throw new AccionImposibleEx("La lista está vacía");
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Lista de embarcaciones:\n");
        for (Embarcacion embarcacion : embarcaciones.values()) {
            sb.append(embarcacion.toString());
        }

        return sb;
    }

    public String verEmbarcacionMat(String matricula) throws AccionImposibleEx {

        if (embarcaciones.containsKey(matricula)) {
            return embarcaciones.get(matricula).toString();
        }

        throw new AccionImposibleEx("No existe un embarcacion con esa matricula");
    }

}