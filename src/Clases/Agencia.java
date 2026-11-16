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


    //  ATRIBUTOS


    private HashMap<String, Embarcacion>embarcaciones;
    private Generica<Cliente> clientes;
    private Generica<Empleado> empleados;
    private Generica<Reserva> listaReservas;
    private Generica<Alquiler> listaAlquileres;
    private Generica<Venta> listaVentas;
    private Empleado empleadoLogueado;

    //  CONSTRUCTOR

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

        for (Empleado empleado : empleados.getLista()) {
            if (empleado.getID().equals(dni)) {
                throw new ElementoDuplicadoEx("Ya existe un empleado con ese DNI");
            }
            if (empleado.getEdad() < 18) {
                throw new DatoErroneoEx("El empleado no puede ser menor a 18");
            }
        }
        Empleado aux = new Empleado(dni, nombre, edad, telefono, email, puesto);
        empleados.guardar(aux);
        BDDManager.agregarEmpleado(aux, contraseña);
        return true;

    }

    public boolean eliminarEmpleado(int dni) throws AccionImposibleEx {
        if(empleados.BuscarPorId(dni) == null)
        {
            throw new AccionImposibleEx("No existe un empleado con ese DNI");
        }
        Iterator<Empleado> iterator = empleados.getLista().iterator();
        while (iterator.hasNext()) {
            Empleado empleado = iterator.next();
            if (empleado.getID().equals(dni)) {
                iterator.remove();
               int filasAfectadas =  BDDManager.borrarEmpleadoBDD(empleado.getID());
                if(filasAfectadas == 1)
                {return true;}
            }
        }

        return false;
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

    ///  CLIENTES

// AGREGAR Y ELIMINAR CLIENTES

    public boolean agregarCliente(Long dni, String nombre, int edad, Long telefono, String email, int numeroLicencia) throws ElementoDuplicadoEx, DatoErroneoEx {


        if (clientes.BuscarPorId(dni) != null) {
            throw new ElementoDuplicadoEx("Ya existe un cliente con ese DNI");
        }


        Cliente aux = new Cliente(dni, nombre, edad, telefono, email, numeroLicencia);
        clientes.guardar(aux);
        BDDManager.agregarCliente(aux);
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

// HISTORIAL DEL CLIENTE

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


    ///  EMBARCACIONES


    // AGREGAR Y ELIMINAR EMBARCACIONES

    public boolean registrarVelero(String nombre, String matricula, double precioDiario, boolean disponible, int cantidadVelas, Double longitudMetros, int capacidadPersonas, int cantidadCamarotes, boolean requiereCapitan) throws ElementoDuplicadoEx, DatoErroneoEx {
        if (embarcaciones.containsKey(matricula)) {
            throw new ElementoDuplicadoEx("Ya existe un velero con esa matricula");
        }

        embarcaciones.put(matricula, new Velero(nombre, matricula, precioDiario, disponible,cantidadVelas, longitudMetros,  capacidadPersonas, cantidadCamarotes));
        int guardado = BDDManager.agregarEmbarcacionBDD(nombre, matricula, precioDiario, disponible, cantidadVelas, longitudMetros, capacidadPersonas, cantidadCamarotes, requiereCapitan);
        if(guardado == 1)
        {
            return true;
        }
        return false;
    }

    public boolean registrarMotoDeAgua(String nombre, String matricula, double precioDiario, boolean disponible, int capacidadPersonas, int cilindrada, double limiteVelocidad, boolean requiereLicencia) throws ElementoDuplicadoEx, DatoErroneoEx {
        if (embarcaciones.containsKey(matricula)) {
            throw new ElementoDuplicadoEx("Ya existe una moto de agua con esa matricula");
        }
        embarcaciones.put(matricula, new MotoDeAgua(nombre, matricula, precioDiario, disponible, capacidadPersonas, cilindrada, limiteVelocidad, requiereLicencia));
        int guardado = BDDManager.agregarEmbarcacionBDD(nombre, matricula, precioDiario, disponible, capacidadPersonas, cilindrada, limiteVelocidad,requiereLicencia);
        if(guardado == 1)
        {
            return true;
        }
        return false;
    }

    public boolean registrarLancha(String nombre, String matricula, double precioDiario, boolean disponible, int potenciaMotor, double longitudMetros, int capacidadPersonas, TipoCombustible combustible) throws ElementoDuplicadoEx, DatoErroneoEx {
        if (embarcaciones.containsKey(matricula)){
            throw new ElementoDuplicadoEx("Ya existe una lancha con esa matricula");
        }

        embarcaciones.put(matricula, new Lancha(nombre, matricula, precioDiario, disponible, potenciaMotor, longitudMetros, capacidadPersonas, combustible));
        int guardado = BDDManager.agregarEmbarcacionBDD( nombre, matricula, precioDiario,disponible, potenciaMotor,  longitudMetros, capacidadPersonas,  combustible);

        if(guardado == 1)
        {
            return true;
        }
        return false;
    }

    public boolean eliminarEmbarcacion(String matricula) throws AccionImposibleEx, DatoErroneoEx
    {

        Embarcacion e = embarcaciones.get(matricula);

        if (embarcaciones.containsKey(matricula)) {
            if (tieneReservas(e)) {
                throw new DatoErroneoEx("No se puede eliminar: la embarcación tiene reservas asociadas.");
            }

            if (tieneAlquileres(e)) {
                throw new DatoErroneoEx("No se puede eliminar: la embarcación tiene alquileres asociados.");
            }
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

    public Embarcacion getEmbarcacion(String matricula) { return embarcaciones.get(matricula); }

    ///  RESERVAS

    // CREAR RESERVA

    public boolean agregarUnaReserva(int id, Cliente cliente, int cantidadPersonas, Embarcacion embarcacion, int añoInicio, int mesInicio, int diaInicio, int añoFin, int mesFin, int diaFin, ESTADO_RESERVA estadoReserva) throws AccionImposibleEx, ElementoDuplicadoEx, DatoErroneoEx
    {
        if (listaReservas.BuscarPorId(id) != null) {
            throw new ElementoDuplicadoEx(
                    "Ya existe una reserva con este identificador."
            );
        }

        if (embarcacion == null ||
                !embarcaciones.containsKey(embarcacion.getID())) {
            throw new AccionImposibleEx(
                    "La embarcación ingresada no existe."
            );
        }

        if (cliente == null || !clientes.getLista().contains(cliente)) {
            throw new AccionImposibleEx(
                    "El cliente ingresado no existe."
            );
        }

        LocalDate inicio = LocalDate.of(añoInicio, mesInicio, diaInicio);
        LocalDate fin = LocalDate.of(añoFin, mesFin, diaFin);
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
                id, cliente, cantidadPersonas, embarcacion,
                añoInicio, mesInicio, diaInicio,
                añoFin, mesFin, diaFin, estadoReserva
        );
        listaReservas.guardar(aux);
         int guardadoCorrectamente =  BDDManager.agregarReservaBDD(aux);
        if(guardadoCorrectamente == 1)
        {
            return true;
        }
        return false;
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


    public boolean IniciarAlquiler( int idReserva)
            throws AccionImposibleEx, DatoErroneoEx {

        Reserva aux = listaReservas.BuscarPorId(idReserva);

        if (aux == null) {
            throw new AccionImposibleEx("La reserva no existe.");
        }

        if (aux.getEstadoReserva() != ESTADO_RESERVA.CONFIRMADA) {
            throw new AccionImposibleEx("La reserva no está confirmada.");
        }

       /* if (listaAlquileres.BuscarPorId(id) != null) {
            throw new AccionImposibleEx("Ya existe un alquiler con ese identificador.");
        }el id es autoincremental*/
        Embarcacion embarcacion = aux.getEmbarcacion();
        if (embarcacion == null || !embarcacion.isDisponible()) {
            throw new AccionImposibleEx("La embarcación no está disponible.");
        }

        Alquiler auxAlquiler = new Alquiler(aux);

        if (!listaAlquileres.guardar(auxAlquiler)) {
            return false;
        }
        aux.getCliente().agregarAlquiler();

        int id= BDDManager.agregarAlquilerBDD(aux, auxAlquiler.getMontoFinal());
        auxAlquiler.setId(id);

        auxAlquiler.cambiarDisponibilidad();

        BDDManager.modificarDisponibilidad(aux.getEmbarcacion(), aux.getEmbarcacion().isDisponible());
        BDDManager.modificarCamposCLienteBDD(null,null,null,aux.getCliente().getID(),null,null,aux.getCliente().getCantidadAlquileres());
        listaReservas.eliminar(idReserva);

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

    ///  VENTAS

    public boolean iniciarVenta(int idAlquiler) throws AccionImposibleEx, DatoErroneoEx {

        Alquiler aux = listaAlquileres.BuscarPorId(idAlquiler);

        if (aux == null) {
            throw new AccionImposibleEx("El alquiler no existe.");
        }

        if (aux.isFinalizado()) {
            throw new AccionImposibleEx("El alquiler ya fue finalizado.");
        }

        double montoBase = aux.calcularPrecio();
        Venta nuevaVenta = new Venta(aux.getReserva().getCliente(), aux.getReserva().getEmbarcacion());

        if (!listaVentas.guardar(nuevaVenta)) {
            return false;
        }

        int id = BDDManager.agregarVentaBDD(aux.getReserva().getCliente(), aux.getReserva().getEmbarcacion(), nuevaVenta.getMonto());
        nuevaVenta.setId(id);

        aux.finalizarAlquiler();

        aux.getReserva().getEmbarcacion().setDisponible(false);
        BDDManager.modificarDisponibilidad(aux.getReserva().getEmbarcacion(), false);

        return true;
    }
    // REGISTRAR VENTA

   /* public boolean registrarVenta (int id, Cliente clienteComprador, Embarcacion embarcacion) throws AccionImposibleEx, DatoErroneoEx
    {
        if(!clientes.getLista().contains(clienteComprador))
        {
            throw new AccionImposibleEx("ERROR. el identificador del comprador ingresado no existe...");
        }
        if(embarcaciones.containsKey(embarcacion.getID()) == false)
        {
            throw new AccionImposibleEx("ERROR. la matricula ingresada no existe...");
        }

        Venta aux = new Venta(clienteComprador, embarcacion);
        return listaVentas.guardar(aux);
    }*/

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

    private boolean tieneReservas(Cliente cliente) {
        for (Reserva r : listaReservas.getLista()) {
            if (r.getCliente().getID().equals(cliente.getID()) ) {
                return true;
            }
        }
        return false;
    }

    private boolean tieneAlquileres(Cliente cliente) {
        for (Alquiler a : listaAlquileres.getLista()) {
            if (a.getReserva().getCliente().getID().equals(cliente.getID())) {
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

   /* private boolean alquilerFinalizado(Alquiler alquiler) {
        for (Venta v : listaVentas.getLista()) {
            if (v.getID() == alquiler.getID()) {
                return true;
            }
        }

        return false;
    }*/
}