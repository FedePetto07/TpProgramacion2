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


    ///  ATRIBUTOS


    private HashMap<String, Embarcacion>embarcaciones;
    private Generica<Cliente> clientes;
    private Generica<Empleado> empleados;
    private Generica<Reserva> listaReservas;
    private Generica<Alquiler> listaAlquileres;
    private Generica<Venta> listaVentas;
    private Empleado empleadoLogueado;

    ///  CONSTRUCTOR

    public Agencia() {
        this.embarcaciones = BDDManager.getEmbarcacionesBDD();
        this.clientes = BDDManager.getClientesBDD();
        this.empleados = BDDManager.getEmpleados();
        this.listaVentas = BDDManager.listaVenta(clientes,embarcaciones);
        this.listaReservas = BDDManager.getReservasBDD(clientes,embarcaciones);
        this.listaAlquileres = BDDManager.getAlquileresBDD(listaReservas);
        this.empleadoLogueado = null;
    }


    ///  SESIÓN


    public Empleado iniciarSesion(int dni, String passwordIngresada) throws AccionImposibleEx {

        String passwordGuardada = BDDManager.getPassword(dni);

        if (passwordGuardada == null || !passwordGuardada.equals(passwordIngresada)) {
            throw new AccionImposibleEx("DNI o contraseña incorrectos");
        }

        Empleado empleado = BDDManager.getEmpleado((long) dni);

        if (empleado == null) {
            throw new AccionImposibleEx("DNI o contraseña incorrectos");
        }

        empleadoLogueado = empleado;
        return empleado;
    }

    public void cerrarSesion() {
        empleadoLogueado = null;
    }

    public Empleado getEmpleadoLogueado() {
        return empleadoLogueado;
    }

    ///  EMPLEADOS

    // AGREGAR Y ELIMINAR EMPLEADOS

    public boolean agregarEmpleado(Long dni, String nombre, int edad, Long telefono, String email, Puesto puesto, String contraseña) throws ElementoDuplicadoEx, DatoErroneoEx {

        if (empleados.BuscarPorId(dni) != null) {
            throw new ElementoDuplicadoEx("Ya existe un empleado con ese DNI");
        }
        if (contraseña == null || contraseña.isBlank()) {
            throw new DatoErroneoEx("La contraseña no puede estar vacía.");
        }
        Empleado aux = new Empleado(dni, nombre, edad, telefono, email, puesto);
        if (!BDDManager.agregarEmpleado(aux, contraseña)) {
            return false;
        }
        empleados.guardar(aux);
        return true;

    }

    // ELIMINAR EMPLEADO

    public boolean eliminarEmpleado(long dni) throws AccionImposibleEx {
        Empleado empleado = empleados.BuscarPorId(dni);
        if (empleado == null) {
            throw new AccionImposibleEx("No existe un empleado con ese DNI");
        }
        if (BDDManager.borrarEmpleadoBDD(empleado.getID()) != 1) {
            return false;
        }
        empleados.getLista().remove(empleado);
        return true;
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

    // VER EMPLEADO POR ID

    public String verEmpleadoId(int dni) throws ObjetoNoEncontradoEx {
        for (Empleado empleado : empleados.getLista()) {
            if (empleado.getID() == dni) {
                return empleado.toString();
            }
        }

        throw new ObjetoNoEncontradoEx("No existe un empleado con ese DNI");
    }
    //////setter
    public String modificarDatosEmpleado(long dni, String nombreNuevo, Long telefonoNuevo, String emailNuevo, Puesto puesto, double salarioNuevo) throws ObjetoNoEncontradoEx, DatoErroneoEx
    {
        Empleado aux = empleados.BuscarPorId(dni);
        if(aux == null)
        {
            throw new ObjetoNoEncontradoEx("ERROR. el dni ingresado no existe en el sistema...");
        }
        StringBuilder informacionNuevaEmpleado = new StringBuilder();

        if (nombreNuevo != null) {
            if (nombreNuevo.isBlank() || nombreNuevo.matches(".*\\d.*"))
            {
                throw new DatoErroneoEx("El nombre nuevo no puede contener números.");
            }
            aux.setNombre(nombreNuevo);
            informacionNuevaEmpleado.append("Se ha actualizado el nombre. ");
        }

        if (telefonoNuevo != null) {
            if (String.valueOf(telefonoNuevo).length() != 10) {
                throw new DatoErroneoEx("El teléfono nuevo debe tener exactamente 10 dígitos.");
            }

            aux.setTelefono(telefonoNuevo);
            informacionNuevaEmpleado.append("Se ha actualizado el telefono. ");
        }

        if (emailNuevo != null) {
            if (!emailNuevo.endsWith("@gmail.com")) {
                throw new DatoErroneoEx("El email debe terminar en @gmail.com");
            }

            aux.setEmail(emailNuevo);
            informacionNuevaEmpleado.append("Se ha actualizado el email. ");
        }


        if (puesto != null) {
            aux.setPuesto(puesto);
            informacionNuevaEmpleado.append("Se ha actualizado el puesto. ");
        }

        if (salarioNuevo > 0) {
            aux.setSalario(salarioNuevo);
            informacionNuevaEmpleado.append("Se ha actualizado el salario. ");
        }

        ///////////////////////////////
        Double salarioNuevoBDD = (salarioNuevo > 0) ? salarioNuevo : null;
        String puestoParaBDD = (puesto != null) ? puesto.toString() : null;

        // CORREGIDO: Evitar NullPointerException si el puesto es null
        int modificado = BDDManager.modificarCamposUsuarioBDD(dni, nombreNuevo, telefonoNuevo, emailNuevo, puestoParaBDD, salarioNuevoBDD);

        if(modificado == 1) {
            return informacionNuevaEmpleado.toString();
        }

        return "Error en el guardado...";
    }
    ///  CLIENTES

// AGREGAR CLIENTES

    public boolean agregarCliente(Long dni, String nombre, int edad, Long telefono, String email, int numeroLicencia) throws ElementoDuplicadoEx, DatoErroneoEx {


        if (clientes.BuscarPorId(dni) != null) {
            throw new ElementoDuplicadoEx("Ya existe un cliente con ese DNI");
        }


        Cliente aux = new Cliente(dni, nombre, edad, telefono, email, numeroLicencia);
        clientes.guardar(aux);
        BDDManager.agregarCliente(aux);
        return true;
    }


// VER CLIENTE POR DNI

    public String verClienteDni(Long dni) throws ObjetoNoEncontradoEx {
        Cliente aux = clientes.BuscarPorId(dni);

        if (aux == null) {
            throw new ObjetoNoEncontradoEx("No existe un cliente con ese DNI");
        }

        return aux.toString();
    }

    // LISTAR CLIENTES

    public String listarClientes() throws ObjetoNoEncontradoEx {
        if (clientes.getLista().isEmpty()) {
            throw new ObjetoNoEncontradoEx("La lista está vacía");
        }

        return "Lista de clientes:\n" + clientes.listarElementos();
    }

    public Cliente getCliente(long dni) { return clientes.BuscarPorId(dni); }

// MODIFICAR CLIENTES

    public String modificarDatosCliente(long dni, String nombreNuevo, int edadNueva, Long telefonoNuevo, String emailNuevo, int numeroLicenciaNuevo) throws DatoErroneoEx, ObjetoNoEncontradoEx
    {

        Cliente aux = clientes.BuscarPorId(dni);
        if(aux == null)
        {
            throw  new ObjetoNoEncontradoEx("ERROR. el dni ingresado no existe en el sistema...");
        }
        StringBuilder informacionNueva = new StringBuilder();

        if (nombreNuevo != null) {
            if ( nombreNuevo.isBlank() || nombreNuevo.matches(".*\\d.*"))
            {
                throw new DatoErroneoEx("El nombre nuevo no puede contener números.");
            }
            aux.setNombre(nombreNuevo);
            informacionNueva.append("Se ha actualizado el nombre");
        }
        if (edadNueva >0 ) {
            if (edadNueva < 18) {
                throw new DatoErroneoEx("El cliente no puede ser menor de 18");
            }
            aux.setEdad(edadNueva);
            informacionNueva.append("Se ha actualizado la edad");
        }

        if (telefonoNuevo != null) {
            if (String.valueOf(telefonoNuevo).length() != 10) {
                throw new DatoErroneoEx("El teléfono nuevo debe tener exactamente 10 dígitos.");
            }

            aux.setTelefono(telefonoNuevo);
            informacionNueva.append("Se ha actualizado el telefono");
        }

        if (emailNuevo != null) {
            if (!emailNuevo.endsWith("@gmail.com")) {
                throw new DatoErroneoEx("El email debe terminar en @gmail.com");}

            aux.setEmail(emailNuevo);
            informacionNueva.append("Se ha actualizado el email");
        }
        if (numeroLicenciaNuevo > 0) {
            if (numeroLicenciaNuevo != 0 && (numeroLicenciaNuevo < 5 || numeroLicenciaNuevo > 9)) {
                throw new DatoErroneoEx("La licencia debe ser 0 o tener un código entre 5 y 9.");}

            aux.setNumeroLicencia(numeroLicenciaNuevo);
            informacionNueva.append("Se ha actualizado el numero de licencia");
        }
        ///////////////////////////////
        Integer edadParaBDD = (edadNueva > 0) ? edadNueva : null;
        Integer licenciaParaBDD = (numeroLicenciaNuevo > 0) ? numeroLicenciaNuevo : null;
        //
        int modificado = BDDManager.modificarCamposCLienteBDD(nombreNuevo,edadParaBDD,licenciaParaBDD,dni,telefonoNuevo,emailNuevo,null);
        if(modificado == 1)
        {return informacionNueva.toString();}

        return "Error en el guardado...";
    }

    // VER RESERVAS POR CLIENTE

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

    // VER ALQUILERES POR CLIENTE

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

    // VER VENTAS POR CLIENTE

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


    ///  EMBARCACIONES

    // AGREGAR VELERO

    public boolean registrarVelero(String nombre, String matricula, double precioDiario, boolean disponible, int cantidadVelas, Double longitudMetros, int capacidadPersonas, int cantidadCamarotes, boolean requiereCapitan) throws ElementoDuplicadoEx, DatoErroneoEx {
        if (embarcaciones.containsKey(matricula)) {
            throw new ElementoDuplicadoEx("Ya existe un velero con esa matricula");
        }

        Velero v = new Velero(nombre, matricula, precioDiario, disponible, cantidadVelas, longitudMetros, capacidadPersonas, cantidadCamarotes);
        v.setRequiereCapitan(requiereCapitan);

        if (BDDManager.agregarEmbarcacionBDD(v) != 1) {
            return false;
        }
        embarcaciones.put(matricula, v);
        return true;
    }

    // AGREGAR MOTO DE AGUA

    public boolean registrarMotoDeAgua(String nombre, String matricula, double precioDiario, boolean disponible, int capacidadPersonas, int cilindrada, double limiteVelocidad, boolean requiereLicencia) throws ElementoDuplicadoEx, DatoErroneoEx {
        if (embarcaciones.containsKey(matricula)) {
            throw new ElementoDuplicadoEx("Ya existe una moto de agua con esa matricula");
        }

        MotoDeAgua m = new MotoDeAgua(nombre, matricula, precioDiario, disponible, capacidadPersonas, cilindrada, limiteVelocidad, requiereLicencia);

        if (BDDManager.agregarEmbarcacionBDD(m) != 1) {
            return false;
        }

        embarcaciones.put(matricula, m);
        return true;
    }

    // AGREGAR LANCHA

    public boolean registrarLancha(String nombre, String matricula, double precioDiario, boolean disponible, int potenciaMotor, double longitudMetros, int capacidadPersonas, TipoCombustible combustible) throws ElementoDuplicadoEx, DatoErroneoEx {
        if (embarcaciones.containsKey(matricula)){
            throw new ElementoDuplicadoEx("Ya existe una lancha con esa matricula");
        }

        Lancha l = new Lancha(nombre, matricula, precioDiario, disponible, potenciaMotor, longitudMetros, capacidadPersonas, combustible);

        if (BDDManager.agregarEmbarcacionBDD(l) != 1) {
            return false;
        }
        embarcaciones.put(matricula, l);
        return true;
    }

    // LISTAR EMBARCACIONES

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

    // VER EMBARCACION POR MATRICULA

    public String verEmbarcacionMat(String matricula) throws ObjetoNoEncontradoEx {

        if (embarcaciones.containsKey(matricula)) {
            return embarcaciones.get(matricula).toString();
        }

        throw new ObjetoNoEncontradoEx("No existe un embarcacion con esa matricula");
    }

    public Embarcacion getEmbarcacion(String matricula) { return embarcaciones.get(matricula); }

    ///  RESERVAS

    // CREAR RESERVA

    public boolean agregarUnaReserva(int dniCliente, int cantidadPersonas, String matriculaEmbarcacion, int añoInicio, int mesInicio, int diaInicio, int añoFin, int mesFin, int diaFin) throws AccionImposibleEx, DatoErroneoEx, ObjetoNoEncontradoEx
    {

        Embarcacion embarcacion = null;

        for (Embarcacion e : embarcaciones.values()) {
            if (e.getID().equalsIgnoreCase(matriculaEmbarcacion)) {
                embarcacion = e;
                break;
            }
        }

        if (embarcacion == null ||
                !embarcaciones.containsKey(embarcacion.getID())) {
            throw new ObjetoNoEncontradoEx(
                    "La embarcación ingresada no existe."
            );
        }

        Cliente cliente = null;

        for (Cliente c : clientes.getLista()) {
            if (c.getID() == dniCliente) {
                cliente = c;
                break;
            }
        }

        if (cliente == null || !clientes.getLista().contains(cliente)) {
            throw new ObjetoNoEncontradoEx(
                    "El cliente ingresado no existe."
            );
        }
        LocalDate inicio;
        LocalDate fin;
        try {
            inicio = LocalDate.of(añoInicio, mesInicio, diaInicio);
             fin = LocalDate.of(añoFin, mesFin, diaFin);
        }
        catch (java.time.DateTimeException e)
        {
            throw new DatoErroneoEx("Las fechas ingresadas no son validas...");
        }
        if(inicio.isBefore(LocalDate.now().plusDays(3)))
        {
            throw new AccionImposibleEx("ERROR, la reserva debe hacerse con al menos tres dias de anticipacion... ");
        }
        if (!embarcacionDisponible(embarcacion, inicio, fin)) {
            throw new AccionImposibleEx(
                    "La embarcación no está disponible para esas fechas."
            );
        }

        Reserva aux = new Reserva(
                cliente, cantidadPersonas, embarcacion,
                añoInicio, mesInicio, diaInicio,
                añoFin, mesFin, diaFin
        );

        int idGenerado = BDDManager.agregarReservaBDD(aux);
        if (idGenerado <= 0) {
            return false;
        }
        aux.setId(idGenerado);
        listaReservas.guardar(aux);
        return true;
    }

    // CANCELAR RESERVA

    public String cancelarReserva(int idReserva) throws ObjetoNoEncontradoEx, AccionImposibleEx
    {
        Reserva aux = listaReservas.BuscarPorId(idReserva);

        if(aux == null)
        {
            throw new ObjetoNoEncontradoEx("ERROR. No existe el id ingresado de la reserva.");
        }

        if(aux.getEstadoReserva().equals(ESTADO_RESERVA.CANCELADA))
        {
            throw new AccionImposibleEx("ERROR. No se pudo cancelar la reserva. Ya está cancelada...");
        }

        if(aux.getEstadoReserva().equals(ESTADO_RESERVA.FINALIZADA))
        {
            throw new AccionImposibleEx("ERROR. La reserva ya está finalizada, no se puede cancelar...");
        }

        if(aux.getEstadoReserva().equals(ESTADO_RESERVA.CONFIRMADA))
        {
            LocalDate fechaActual = LocalDate.now();

            if(!aux.getFechaDeInicio().isAfter(fechaActual))
            {
                throw new AccionImposibleEx("ERROR. No se puede cancelar la reserva porque la fecha de inicio ya llegó o ya pasó.");
            }
        }

        aux.cancelar();
        BDDManager.modificarEstadoReservaBDD(aux.getID(),ESTADO_RESERVA.CANCELADA.toString());
        return "Cancelado correctamente";
    }

    // ELIMINAR RESERVA

    public boolean eliminarReserva(int idReserva) throws ObjetoNoEncontradoEx, AccionImposibleEx
    {
        Reserva aux = listaReservas.BuscarPorId(idReserva);

        if (aux == null) {
            throw new ObjetoNoEncontradoEx("ERROR. No existe el id ingresado de la reserva.");
        }
        if (aux.getEstadoReserva() != ESTADO_RESERVA.CANCELADA) {
            throw new AccionImposibleEx("ERROR. Solo se pueden eliminar reservas CANCELADAS (estado actual: " + aux.getEstadoReserva() + ").");
        }
        for (Alquiler a : listaAlquileres.getLista()) {
            if (a.getReserva().getID() == idReserva) {
                throw new AccionImposibleEx("ERROR. La reserva tiene un alquiler asociado, no se puede eliminar.");
            }
        }
        if (BDDManager.borrarReserva(idReserva) != 1) {
            return false;
        }
        listaReservas.getLista().remove(aux);
        return true;
    }

    // MODIFICAR ESTADO

    public void actualizarEstadoReserva(int id, ESTADO_RESERVA nuevoEstado)
            throws AccionImposibleEx, DatoErroneoEx, ObjetoNoEncontradoEx {

        Reserva reserva = listaReservas.BuscarPorId(id);
        if (reserva == null) {
            throw new ObjetoNoEncontradoEx("No existe una reserva con ese ID.");
        }
        if (nuevoEstado == null) {
            throw new DatoErroneoEx("El estado de la reserva no puede ser nulo.");
        }
        if(reserva.getEstadoReserva() == ESTADO_RESERVA.FINALIZADA)
        {
            throw new AccionImposibleEx("ERROR. la reserva ya esta finalizada, no se puede modificar su estado...");
        }
        if(reserva.getEstadoReserva() == ESTADO_RESERVA.CANCELADA)
        {
            throw new AccionImposibleEx("ERROR. la reserva ya esta cancelada, no se puede modificar su estado...");
        }
        if(reserva.getEstadoReserva().equals(ESTADO_RESERVA.CONFIRMADA)  && nuevoEstado.equals(ESTADO_RESERVA.PENDIENTE))
        {
            throw new AccionImposibleEx("ERROR. No puede volver a pendiente una reserva confirmada...");
        }
        reserva.setEstadoReserva(nuevoEstado);
        BDDManager.modificarEstadoReservaBDD(id,reserva.getEstadoReserva().toString());
    }

    // LISTAR RESERVA POR ID

    public String listarReservaPorID(int idBusqueda) throws ObjetoNoEncontradoEx
    {
        if(listaReservas.BuscarPorId(idBusqueda) == null)
        {
            throw new ObjetoNoEncontradoEx( "ERROR. No existe el id ingresado de la reserva.");
        }
        Reserva aux = listaReservas.BuscarPorId(idBusqueda);
        return aux.toString();
    }

    // VER TODAS LAS RESERVAS

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


    ///  ALQUILERES

    // INICIAR ALQUILER

    public boolean iniciarAlquiler(int idReserva) throws AccionImposibleEx, DatoErroneoEx {

        Reserva aux = listaReservas.BuscarPorId(idReserva);

        if (aux == null) {
            throw new AccionImposibleEx("La reserva no existe.");
        }

        if (aux.getEstadoReserva() != ESTADO_RESERVA.CONFIRMADA) {
            throw new AccionImposibleEx("La reserva no está confirmada.");
        }

        for (Alquiler a : listaAlquileres.getLista()) {
            if (a.getReserva().getID() == idReserva) {
                throw new AccionImposibleEx("Ya existe un alquiler asociado a esta reserva.");
            }
        }

        Embarcacion embarcacion = aux.getEmbarcacion();
        if (embarcacion == null || !embarcacion.isDisponible()) {
            throw new AccionImposibleEx("La embarcación no está disponible.");
        }

        Alquiler auxAlquiler = new Alquiler(aux);

        int idBDD = BDDManager.agregarAlquilerBDD(aux, auxAlquiler.getMontoFinal());
        if (idBDD <= 0) {
            return false;
        }

        auxAlquiler.setId(idBDD);

        if (!listaAlquileres.guardar(auxAlquiler)) {
            return false;
        }

        aux.getCliente().agregarAlquiler();
        auxAlquiler.cambiarDisponibilidad();

        BDDManager.modificarDisponibilidad(aux.getEmbarcacion(), aux.getEmbarcacion().isDisponible());
        BDDManager.modificarCamposCLienteBDD(null, null, null, aux.getCliente().getID(), null, null, aux.getCliente().getCantidadAlquileres());

        return true;
    }

    // LISTAR ALQUILERES

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

    // VER ALQUILER POR ID

    public String verAlquiler(int id) throws ObjetoNoEncontradoEx
    {
        if  (listaAlquileres.BuscarPorId(id) == null){
            throw new ObjetoNoEncontradoEx("El alquiler no existe");
        }

        Alquiler auxAlquiler = listaAlquileres.BuscarPorId(id);

        return auxAlquiler.toString();
    }

    ///  VENTAS

    // REGISTRAR VENTA

    public boolean registrarVenta(long dniCliente, String matriculaEmbarcacion) throws AccionImposibleEx, ObjetoNoEncontradoEx, DatoErroneoEx {

        Cliente cliente = clientes.BuscarPorId(dniCliente);
        if (cliente == null) {
            throw new ObjetoNoEncontradoEx("El cliente ingresado no existe.");
        }

        Embarcacion embarcacion = null;
        for (Embarcacion e : embarcaciones.values()) {
            if (e.getID().equalsIgnoreCase(matriculaEmbarcacion)) {
                embarcacion = e;
                break;
            }
        }
        if (embarcacion == null) {
            throw new ObjetoNoEncontradoEx("La embarcación ingresada no existe.");
        }

        for (Venta v : listaVentas.getLista()) {
            if (v.getEmbarcacion().getID().equals(embarcacion.getID())) {
                throw new AccionImposibleEx("La embarcación ya fue vendida.");
            }
        }

        if (!embarcacion.isDisponible()) {
            throw new AccionImposibleEx("La embarcación no está disponible (tiene un alquiler en curso).");
        }

        for (Reserva r : listaReservas.getLista()) {
            if (r.getEmbarcacion().getID().equals(embarcacion.getID())
                    && r.getEstadoReserva() != ESTADO_RESERVA.CANCELADA
                    && r.getEstadoReserva() != ESTADO_RESERVA.FINALIZADA) {
                throw new AccionImposibleEx("La embarcación tiene reservas pendientes o confirmadas.");
            }
        }

        Venta nuevaVenta = new Venta(cliente, embarcacion);

        int id = BDDManager.agregarVentaBDD(cliente, embarcacion, nuevaVenta.getMonto());
        if (id <= 0) {
            return false;
        }
        nuevaVenta.setId(id);
        listaVentas.guardar(nuevaVenta);

        embarcacion.setDisponible(false);
        BDDManager.modificarDisponibilidad(embarcacion, false);

        return true;
    }

    // VER TODAS LAS VENTAS

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

    // VER VENTAS POR ID

    public String verVentaId(int idVenta) throws ObjetoNoEncontradoEx
    {

        Venta aux = listaVentas.BuscarPorId(idVenta);
        if(aux == null)
        {
            throw  new ObjetoNoEncontradoEx("ERROR. la venta buscada no existe");
        }

        return aux.toString();
    }

    //  COMPROBACIONES (MÉTODOS PRIVADOS)

    private boolean embarcacionDisponible(Embarcacion embarcacion,
                                          LocalDate inicio,
                                          LocalDate fin) {

        for (Reserva r : listaReservas.getLista()) {

            if (r.getEmbarcacion().getID().equals(embarcacion.getID())
                    && r.getEstadoReserva() != ESTADO_RESERVA.CANCELADA) {

                if (!inicio.isAfter(r.getFechaDeFin())
                        && !fin.isBefore(r.getFechaDeInicio())) {
                    return false;
                }
            }
        }

        for (Alquiler a : listaAlquileres.getLista()) {
            Reserva r = a.getReserva();

            if (r.getEmbarcacion().getID().equals(embarcacion.getID())) {
                if (!inicio.isAfter(r.getFechaDeFin())
                        && !fin.isBefore(r.getFechaDeInicio())) {
                    return false;
                }
            }
        }

        return true;
    }
}