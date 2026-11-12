package Clases;

import Enums.ESTADO_RESERVA;
import Enums.Puesto;
import Enums.TipoCombustible;
import Excepciones.AccionImposibleEx;
import Excepciones.DatoErroneoEx;
import Excepciones.ElementoDuplicadoEx;
import Excepciones.ObjetoNoEncontradoEx;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;


public class Agencia {

    /// ATRIBUTOS

    private HashMap<String, Embarcacion>embarcaciones;
    private Generica<Cliente> clientes;
    private Generica<Empleado> empleados;
    private Generica<Reserva> listaReservas;
    private Generica<Alquiler> listaAlquileres;
    private Generica<Venta> listaVentas;
    private ArrayList <Reporte>listaReportes;


    /// CONSTRUCTOR

    public Agencia() {
        this.embarcaciones = BDDManager.getEmbarcacionesBDD();
        this.clientes = BDDManager.getClientesBDD();
        this.empleados = BDDManager.getEmpleados();
        this.listaReportes = BDDManager.getReportesBDD();
        this.listaVentas = BDDManager.listaVenta(clientes,embarcaciones,listaReportes);
        this.listaReservas = BDDManager.getReservasBDD(clientes,embarcaciones);
        this.listaAlquileres = BDDManager.getAlquileresBDD(listaReservas);
    }

    /// EMPLEADOS

    // AGREGAR Y ELIMINAR EMPLEADOS

    public boolean agregarEmpleado(Long dni, String nombre, int edad, Long telefono, String email, Puesto puesto, String contraseña) throws ElementoDuplicadoEx, DatoErroneoEx {

        for (Empleado empleado : empleados.getLista()) {
            if (empleado.getID() == dni) {
                throw new ElementoDuplicadoEx("Ya existe un empleado con ese DNI");
            }
        }

        empleados.guardar(new Empleado(dni, nombre, edad, telefono, email, puesto));
        return true;
    }

    public boolean eliminarEmpleado(int dni) throws AccionImposibleEx {
        Iterator<Empleado> iterator = empleados.getLista().iterator();
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

        if (empleados.getLista().isEmpty()) {
            throw new AccionImposibleEx("La lista está vacía");
        }

        sb.append("Lista de empleados:\n");

        for (Empleado empleado : empleados.getLista()) {
            sb.append(empleado.toString());
        }

        return sb;
    }

    public String verEmpleadoId(int dni) throws AccionImposibleEx {
        for (Empleado empleado : empleados.getLista()) {
            if (empleado.getID() == dni) {
                return empleado.toString();
            }
        }

        throw new AccionImposibleEx("No existe un empleado con ese DNI");
    }

    /// CLIENTES

// AGREGAR Y ELIMINAR CLIENTES

    public boolean agregarCliente(Long dni, String nombre, int edad, Long telefono, String email, int numeroLicencia) throws ElementoDuplicadoEx, DatoErroneoEx {


        if (clientes.BuscarPorId(dni) != null) {
            throw new ElementoDuplicadoEx("Ya existe un cliente con ese DNI");
        }


        clientes.guardar(new Cliente(dni, nombre, edad, telefono, email, numeroLicencia));
        return true;
    }

    public boolean eliminarCliente(long dni) throws ObjetoNoEncontradoEx {
        if (!clientes.eliminar(dni)) {
            throw new ObjetoNoEncontradoEx("No existe un cliente con ese DNI");
        }

        return true;
    }

// VER CLIENTES

    public String verClienteDni(Long dni) throws ObjetoNoEncontradoEx {
        Cliente aux = clientes.BuscarPorId(dni);

        if (aux == null) {
            throw new ObjetoNoEncontradoEx("No existe un cliente con ese DNI");
        }

        return aux.toString();
    }

    public String listarClientes() throws ObjetoNoEncontradoEx {
        if (clientes.getLista().isEmpty()) {
            throw new ObjetoNoEncontradoEx("La lista está vacía");
        }

        return "Lista de clientes:\n" + clientes.listarElementos();
    }

// LIMPIAR CLIENTES NO RECURRENTES

    public int limpiarClientes(int mesesMinimos, int alquileresMinimos) throws ObjetoNoEncontradoEx {
        if (clientes.getLista().isEmpty()) {
            throw new ObjetoNoEncontradoEx("La lista está vacía");
        }

        int eliminados = 0;

        Iterator<Cliente> it = clientes.getLista().iterator();
        while (it.hasNext()) {
            Cliente c = it.next();
            if (!c.esRecurrente(mesesMinimos, alquileresMinimos) && !tieneReservas(c)
                    && !tieneAlquileres(c)) {
                it.remove();
                eliminados++;
            }
        }

        return eliminados;
    }

    public StringBuilder modificarDatosCliente(long dni, String nombreNuevo, int edadNueva, Long telefonoNuevo, String emailNuevo, int numeroLicenciaNuevo) throws DatoErroneoEx
    {

        Cliente aux = clientes.BuscarPorId(dni);

        StringBuilder informacionNueva = new StringBuilder();

        if (nombreNuevo != null) {
            aux.setNombre(nombreNuevo);
            informacionNueva.append("Se ha actualizado el nombre");
        }
        if (edadNueva > 0) {
            aux.setEdad(edadNueva);
            informacionNueva.append("Se ha actualizado la edad");
        }
        if (telefonoNuevo != null) {
            aux.setTelefono(telefonoNuevo);
            informacionNueva.append("Se ha actualizado el telefono");
        }
        if (emailNuevo != null) {
            aux.setEmail(emailNuevo);
            informacionNueva.append("Se ha actualizado el email");
        }
        if (numeroLicenciaNuevo > 0) {
            aux.setNumeroLicencia(numeroLicenciaNuevo);
            informacionNueva.append("Se ha actualizado el numero de licencia");
        }

        return informacionNueva;
    }

    public ArrayList<Reserva> getReservasCliente(long dni)
            throws ObjetoNoEncontradoEx {

        ArrayList<Reserva> resultado = new ArrayList<>();

        for (Reserva r : listaReservas.getLista()) {
            if (r.getCliente().getID() == dni) {
                resultado.add(r);
            }
        }

        if (resultado.isEmpty()) {
            throw new ObjetoNoEncontradoEx("El cliente no tiene reservas.");
        }

        return resultado;
    }

    public ArrayList<Alquiler> getAlquileresCliente(long dni)
            throws ObjetoNoEncontradoEx {

        ArrayList<Alquiler> resultado = new ArrayList<>();

        for (Alquiler a : listaAlquileres.getLista()) {
            if (a.getReserva().getCliente().getID() == dni) {
                resultado.add(a);
            }
        }

        if (resultado.isEmpty()) {
            throw new ObjetoNoEncontradoEx("El cliente no tiene alquileres.");
        }

        return resultado;
    }

    public ArrayList<Venta> getVentasCliente(long dni)
            throws ObjetoNoEncontradoEx {

        ArrayList<Venta> resultado = new ArrayList<>();

        for (Venta v : listaVentas.getLista()) {
            if (v.getClienteComprador().getID() == dni) {
                resultado.add(v);
            }
        }

        if (resultado.isEmpty()) {
            throw new ObjetoNoEncontradoEx("El cliente no tiene compras.");
        }

        return resultado;
    }

    /// EMBARCACIONES

    // AGREGAR Y ELIMINAR EMBARCACIONES

    public boolean registrarVelero(String nombre, String matricula, double precioDiario, boolean disponible, int cantidadVelas, Double longitudMetros, int capacidadPersonas, int cantidadCamarotes, boolean requiereCapitan) throws ElementoDuplicadoEx, DatoErroneoEx {
        if (embarcaciones.containsKey(matricula)) {
            throw new ElementoDuplicadoEx("Ya existe un velero con esa matricula");
        }

        embarcaciones.put(matricula, new Velero(nombre, matricula, precioDiario, disponible, cantidadVelas, longitudMetros, capacidadPersonas, cantidadCamarotes, requiereCapitan));
        return true;
    }

    public boolean registrarMotoDeAgua(String nombre, String matricula, double precioDiario, boolean disponible, int capacidadPersonas, int cilindrada, double limiteVelocidad, boolean requiereLicencia) throws ElementoDuplicadoEx, DatoErroneoEx {
        if (embarcaciones.containsKey(matricula)) {
            throw new ElementoDuplicadoEx("Ya existe una moto de agua con esa matricula");
        }


        embarcaciones.put(matricula, new MotoDeAgua(nombre, matricula, precioDiario, disponible, capacidadPersonas, cilindrada, limiteVelocidad, requiereLicencia));
        return true;
    }

    public boolean registrarLancha(String nombre, String matricula, double precioDiario, boolean disponible, int potenciaMotor, double longitudMetros, int capacidadPersonas, TipoCombustible combustible) throws ElementoDuplicadoEx, DatoErroneoEx {
        if (embarcaciones.containsKey(matricula)){
            throw new ElementoDuplicadoEx("Ya existe una lancha con esa matricula");
        }

        embarcaciones.put(matricula, new Lancha(nombre, matricula, precioDiario, disponible, potenciaMotor, longitudMetros, capacidadPersonas, combustible));
        return true;
    }

    public boolean eliminarEmbarcacion(String matricula) throws AccionImposibleEx, DatoErroneoEx
    {

        Embarcacion e = embarcaciones.get(matricula);

        if (embarcaciones.containsKey(matricula)) {
            embarcaciones.remove(matricula);

            if (tieneReservas(e)) {
                throw new DatoErroneoEx("No se puede eliminar: la embarcación tiene reservas asociadas.");
            }

            if (tieneAlquileres(e)) {
                throw new DatoErroneoEx("No se puede eliminar: la embarcación tiene alquileres asociados.");
            }

            return true;
        }

        throw new AccionImposibleEx("No existe un embarcacion con esa matricula");
    }

    //public StringBuilder actualizarEmbarcacion()

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

       public boolean agregarUnaReserva(int id, Cliente cliente, int cantidadPersonas, Embarcacion embarcacion, int añoInicio, int mesInicio, int diaInicio, int añoFin, int mesFin, int diaFin, ESTADO_RESERVA estadoReserva) throws AccionImposibleEx, ElementoDuplicadoEx, DatoErroneoEx
       {
           Clases.Reserva aux = listaReservas.BuscarPorId(id);
           if(aux != null)
           {
               throw new ElementoDuplicadoEx("ERROR. Ya existe una reserva con este identificador");
           }
           if(embarcaciones.containsKey(embarcacion.getID()) == false)
           {
               throw new AccionImposibleEx("ERROR. la matricula de la embarcacion ingresada no existe.");
           }
           if(!clientes.getLista().contains(cliente))
           {
               throw new AccionImposibleEx("ERROR. EL numero de la licencia del cliente ingresado, no existe.");
           }
           if(cantidadPersonas > embarcacion.getCapacidadPersonas()){
               throw new AccionImposibleEx("ERROR. La cantidad de personas es mayor a la capacidad de la embarcación.");
           }
           LocalDate inicio = LocalDate.of(añoInicio,mesInicio,diaInicio);
           LocalDate fin = LocalDate.of(añoFin,mesFin,diaFin);
         if(inicio.isAfter(fin))
         {
             throw new AccionImposibleEx("ERROR. La fecha de inicio es posterior a la de fin.");
         }

         aux = new Reserva(id, cliente, cantidadPersonas, embarcacion, añoInicio, mesInicio, diaInicio, añoFin,  mesFin, diaFin, estadoReserva);
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
     public String listarTodasLasReservas() throws ObjetoNoEncontradoEx
     {
         if (listaReservas.getLista().isEmpty()){
             throw new ObjetoNoEncontradoEx("La lista está vacía");
         }

         String rta = "";
         rta += "Lista de reservas:\n";
         rta += listaReservas.listarElementos();

         return rta;
     }
///////////////////////////////////////////////////////////////
       public boolean confirmarReservaIniciarAlquiler(int id, int idReserva) throws AccionImposibleEx
       {

           Reserva aux = null;

           aux = listaReservas.BuscarPorId(idReserva);

           if (aux.getEstadoReserva() != ESTADO_RESERVA.CONFIRMADA){
               throw new AccionImposibleEx("La reserva no está confirmada");
           }

           if (aux == null){
               throw new AccionImposibleEx("La reserva no existe");
           }

           Alquiler auxAlquiler = new Alquiler(id, aux);
           auxAlquiler.cambiarDisponibilidad();
           listaAlquileres.guardar(auxAlquiler);
           return true;
       }

       public String listarAlquileres() throws ObjetoNoEncontradoEx
       {
           if (listaAlquileres.getLista().isEmpty()){
               throw new ObjetoNoEncontradoEx("La lista está vacía");
           }

           String rta = "";
           rta += "Lista de alquileres:\n";
           rta += listaAlquileres.listarElementos();

           return rta;
       }

       public String verAlquiler(int id) throws ObjetoNoEncontradoEx
       {
           if  (listaAlquileres.BuscarPorId(id) == null){
               throw new ObjetoNoEncontradoEx("El alquiler no existe");
           }

           Alquiler auxAlquiler = listaAlquileres.BuscarPorId(id);

           return auxAlquiler.toString();
       }


      //////registrar Venta
     public boolean registrarVenta (int id, Cliente clienteComprador, Embarcacion embarcacion, double montoBase) throws AccionImposibleEx, DatoErroneoEx
     {
         if(!clientes.getLista().contains(clienteComprador))
         {
             throw new AccionImposibleEx("ERROR. el identificador del comprador ingresado no existe...");
         }
         if(embarcaciones.containsKey(embarcacion.getID()) == false)
         {
             throw new AccionImposibleEx("ERROR. la matricula ingresada no existe...");
         }
         if(montoBase<0)
         {
             throw new AccionImposibleEx("ERROR. el monto base debe ser mayor a 0");
         }
         Venta aux = new Venta(clienteComprador, embarcacion, montoBase);
         return listaVentas.guardar(aux);
     }
     //////verVentas
    public String listarVentas() throws ObjetoNoEncontradoEx
    {

        if (listaVentas.getLista().isEmpty()){
            throw new ObjetoNoEncontradoEx("La lista está vacía");
        }

        String rta = "";
        rta += "Lista de ventas:\n";
        rta += listaVentas.listarElementos();

        return rta;
    }
    //////verVentaid
    public String verVentaId(int idVenta) throws ObjetoNoEncontradoEx
    {

        Venta aux = listaVentas.BuscarPorId(idVenta);
        if(aux == null)
        {
            throw  new ObjetoNoEncontradoEx("ERROR. la venta buscada no existe");
        }

        return aux.toString();
    }

    /// Comprobaciones

    private boolean tieneReservas(Cliente cliente) {
        for (Reserva r : listaReservas.getLista()) {
            if (r.getCliente().getID() == cliente.getID()) {
                return true;
            }
        }
        return false;
    }

    private boolean tieneAlquileres(Cliente cliente) {
        for (Alquiler a : listaAlquileres.getLista()) {
            if (a.getReserva().getCliente().getID() == cliente.getID()) {
                return true;
            }
        }
        return false;
    }

    private boolean tieneReservas(Embarcacion embarcacion) {
        for (Reserva r : listaReservas.getLista()) {
            if (r.getEmbarcacion().getID().equals(embarcacion.getID())) {
                return true;
            }
        }
        return false;
    }

    private boolean tieneAlquileres(Embarcacion embarcacion) {
        for (Alquiler a : listaAlquileres.getLista()) {
            if (a.getReserva().getEmbarcacion().getID().equals(embarcacion.getID())) {
                return true;
            }
        }
        return false;
    }

}