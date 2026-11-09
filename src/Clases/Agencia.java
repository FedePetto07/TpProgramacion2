package Clases;

import Enums.Puesto;
import Excepciones.AccionImposibleEx;
import Excepciones.ElementoDuplicadoEx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class Agencia {

    /// ATRIBUTOS

    private HashMap<String,Embarcacion> embarcaciones;
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

    public boolean agregarEmpleado(int dni, String nombre, int edad, int telefono, String email, Puesto puesto, double salario) throws AccionImposibleEx, ElementoDuplicadoEx
    {

        for (Empleado empleado : empleados) {
            if (empleado.getID() == dni)
            {
                throw new ElementoDuplicadoEx("Ya existe un empleado con ese DNI");
            }
            if (empleado.getEdad() < 18)
            {
                throw new AccionImposibleEx("El empleado no puede ser menor a 18");
            }
        }

        empleados.add(new Empleado(dni, nombre, edad, telefono, email, puesto, salario));

        return true;
    }

    public boolean eliminarEmpleado(int dni) throws AccionImposibleEx
    {
        Iterator<Empleado> iterator = empleados.iterator();
        while (iterator.hasNext())
        {
            Empleado empleado = iterator.next();
            if (empleado.getID() == dni)
            {
                iterator.remove();
                return true;
            }
        }

        throw new AccionImposibleEx("No existe un empleado con ese DNI");
    }

    public StringBuilder listarEmpleados() throws AccionImposibleEx
    {
        StringBuilder sb = new StringBuilder();

        if (empleados.isEmpty()){
            throw new AccionImposibleEx("La lista está vacía");
        }

        sb.append("Lista de empleados:\n");

        for (Empleado empleado : empleados) {
            sb.append(empleado.toString());
        }

        return sb;
    }

    public String verEmpleadoId(int dni) throws AccionImposibleEx
    {
            for (Empleado empleado : empleados) {
                if (empleado.getID() == dni)
                {
                    return empleado.toString();
                }
            }

        throw new AccionImposibleEx("No existe un empleado con ese DNI");
    }

}
