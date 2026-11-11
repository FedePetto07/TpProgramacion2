package Clases;

import Enums.ESTADO_RESERVA;
import Enums.Puesto;
import Enums.TipoCombustible;
import Excepciones.AccionImposibleEx;
import Excepciones.ElementoDuplicadoEx;
import Excepciones.ObjetoNoEncontradoEx;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
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
    //////////CREAR RESERVA

       public boolean agregarUnaReserva(int id, Cliente cliente, Embarcacion embarcacion, int añoInicio, int mesInicio, int diaInicio, int añoFin, int mesFin, int diaFin, ESTADO_RESERVA estadoReserva) throws AccionImposibleEx, ElementoDuplicadoEx
       {
           Clases.Reserva aux = listaReservas.BuscarPorId(id);
           if(aux != null)
           {
               throw new ElementoDuplicadoEx("ERROR. Ya existe una reserva con este identificador");
           }
           if(embarcaciones.containsKey(embarcacion.getMatricula()) == false)
           {
               throw new AccionImposibleEx("ERROR. la matricula de la embarcacion ingresada no existe.");
           }
           if(!clientes.contains(cliente))
           {
               throw new AccionImposibleEx("ERROR. EL numero de la licencia del cliente ingresado, no existe.");
           }
           LocalDate inicio = LocalDate.of(añoInicio,mesInicio,diaInicio);
           LocalDate fin = LocalDate.of(añoFin,mesFin,diaFin);
         if(inicio.isAfter(fin))
         {
             throw new AccionImposibleEx("ERROR. La fecha de inicio es posterior a la de fin.");
         }
         aux = new Reserva(id, cliente,embarcacion, añoInicio, mesInicio, diaInicio, añoFin,  mesFin, diaFin, estadoReserva);
         return listaReservas.guardar(aux);
       }

     ///////cancelar reserva
     public String cancelarReserva(int idReserva) throws ObjetoNoEncontradoEx,AccionImposibleEx
     {
         if(listaReservas.BuscarPorId(idReserva) == null)
         {
             throw new ObjetoNoEncontradoEx( "ERROR. No existe el id ingresado de la reserva.");
         }
         Reserva aux = listaReservas.BuscarPorId(idReserva);
         if(aux.getEstadoReserva().equals(ESTADO_RESERVA.CANCELADA))
         {
            throw new AccionImposibleEx ( "ERROR.No se pudo canclear la reserva. Ya esta cancelda...");
         }
         if(aux.getEstadoReserva().equals(ESTADO_RESERVA.FINALIZADA))
         {
             throw new AccionImposibleEx("ERROR. la reserva ya esta finalizada, no se puede cancelar...");
         }
         if(aux.getEstadoReserva().equals(ESTADO_RESERVA.CONFIRMADA))
         {
             LocalDate fechaActual = LocalDate.now();
             if(aux.getFechaDeInicio().isAfter(fechaActual))
             {
                 throw new AccionImposibleEx("ERROR. no se puede cancelar la reserva porque esta confirmada en la fecha determinada.");
             }
         }
         aux.cancelar();
        return "Cancelado correctamente";
     }
     ////////Listar por id
     public String listarPorID(int idBusqueda) throws ObjetoNoEncontradoEx
     {
         if(listaReservas.BuscarPorId(idBusqueda) == null)
         {
             throw new ObjetoNoEncontradoEx( "ERROR. No existe el id ingresado de la reserva.");
         }
         Reserva aux = listaReservas.BuscarPorId(idBusqueda);
         return aux.toString();
     }

     ////ver todas las reservas
     public String listarTodasLasReservas()
     {
         String rta = "";
         rta = listaReservas.listarElementos();
         return rta;
     }
///////////////////////////////////////////////////////////////
       public boolean confirmarReservaIniciarAlquiler(int id, int idReserva) throws AccionImposibleEx
       {

           Reserva aux = null;

           aux = listaReservas.BuscarPorId(idReserva);

           if (aux == null){
               throw new AccionImposibleEx("La reserva no existe");
           }

           Alquiler auxAlquiler = new Alquiler(id, aux);
           auxAlquiler.cambiarDisponibilidad();
           listaAlquileres.guardar(auxAlquiler);
           return true;
       }

      //////registrar Venta
     public boolean registrarVenta (int id, Cliente clienteComprador, Embarcacion embarcacion, double montoFinal) throws AccionImposibleEx
     {
         if(!clientes.contains(clienteComprador))
         {
             throw new AccionImposibleEx("ERROR. el identificador del comprador ingresado no existe...");
         }
         if(embarcaciones.containsKey(embarcacion.getMatricula()) == false)
         {
             throw new AccionImposibleEx("ERROR. la matricula ingresada no existe...");
         }
         if(montoFinal<0)
         {
             throw new AccionImposibleEx("ERROR. el monto final debe ser mayor a 0");
         }
         Venta aux = new Venta(id, clienteComprador, embarcacion, montoFinal);
         return listaVentas.guardar(aux);
     }
     //////verVentas
    public String verVentas()
    {
        String rta = "";
        rta = listaVentas.listarElementos();
        return rta;
    }
    //////verVentaid
    public String verVentaId(int idVenta) throws ObjetoNoEncontradoEx
    {
        String rta = "";
        Venta aux = listaVentas.BuscarPorId(idVenta);
        if(aux == null)
        {
            throw  new ObjetoNoEncontradoEx("ERROR. la venta buscada no existe");
        }
        rta = aux.toString();
        return rta;
    }
}