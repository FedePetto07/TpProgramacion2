package Clases;

import Enums.ESTADO_RESERVA;
import Enums.Puesto;
import Enums.TipoCombustible;
import Excepciones.DatoErroneoEx;
import Interfaces.Identificable;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class BDDManager {

    static final String url = "jdbc:mysql://localhost/";

    public static boolean existeMatriculaBDD(String matricula) {

        String[] tablas = {"lancha", "velero", "moto_de_agua"};

        try (Connection c = DriverManager.getConnection(url)) {

            for (String tabla : tablas) {

                String query = "SELECT 1 FROM " + tabla +
                        " WHERE matricula = ?";

                try (PreparedStatement ps = c.prepareStatement(query)) {

                    ps.setString(1, matricula);

                    try (ResultSet rs = ps.executeQuery()) {

                        if (rs.next()) {
                            return true;
                        }
                    }
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return false;
    }

    public static HashMap<String, Embarcacion> getEmbarcacionesBDD() {

        HashMap<String, Embarcacion> listaEmbarcacion = new HashMap<>();

        String queryVelero = "SELECT * FROM velero";
        String queryMoto = "SELECT * FROM moto_de_agua";
        String queryLancha = "SELECT * FROM lancha";


        try (Connection c = DriverManager.getConnection(url)) {

            // VELEROS
            try (PreparedStatement ps = c.prepareStatement(queryVelero);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    String matricula = rs.getString("matricula");

                    try {

                        Embarcacion embarcacion = new Velero(
                                rs.getString("nombre"),
                                matricula,
                                rs.getDouble("precio_diario"),
                                rs.getBoolean("disponible"),
                                rs.getInt("cantidad_velas"),
                                rs.getDouble("longitud_metros"),
                                rs.getInt("capacidad_personas"),
                                rs.getInt("cantidad_camarotes"),
                                rs.getBoolean("requiere_capitan")
                        );

                        listaEmbarcacion.put(matricula, embarcacion);

                    }catch (DatoErroneoEx ex){
                        ex.getMessage();
                    }
                }
            }

            // MOTOS DE AGUA
            try (PreparedStatement ps = c.prepareStatement(queryMoto);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    String matricula = rs.getString("matricula");

                    try {

                        Embarcacion embarcacion = new MotoDeAgua(
                                rs.getString("nombre"),
                                matricula,
                                rs.getDouble("precio_diario"),
                                rs.getBoolean("disponible"),
                                rs.getInt("capacidad_personas"),
                                rs.getInt("cilindrada"),
                                rs.getDouble("limite_velocidad"),
                                rs.getBoolean("requiere_licencia")
                        );

                        listaEmbarcacion.put(matricula, embarcacion);
                    }catch (DatoErroneoEx ex){
                        ex.getMessage();
                    }
                }
            }

            // LANCHAS
            try (PreparedStatement ps = c.prepareStatement(queryLancha);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    String matricula = rs.getString("matricula");

                    try {

                        Embarcacion embarcacion = new Lancha(
                                rs.getString("nombre"),
                                matricula,
                                rs.getDouble("precio_diario"),
                                rs.getBoolean("disponible"),
                                rs.getInt("potencia_motor"),
                                rs.getDouble("longitud_metros"),
                                rs.getInt("capacidad_personas"),
                                TipoCombustible.valueOf(rs.getString("combustible"))
                        );

                        listaEmbarcacion.put(matricula, embarcacion);
                    }catch (DatoErroneoEx ex){
                        ex.getMessage();
                    }
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return listaEmbarcacion;
    }

    public static Generica<Empleado> getEmpleados() {
        String query = """
        SELECT p.dni, p.nombre, p.edad, p.telefono, p.email,
               p.fecha_ingreso, e.puesto, e.salario
        FROM persona p
        INNER JOIN empleado e ON p.dni = e.dni
        """;

        Generica<Empleado> empleados = new Generica<>();

        try (Connection c = DriverManager.getConnection(url);
             PreparedStatement ps = c.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                try {

                    Empleado empleado = new Empleado(
                            rs.getLong("dni"),
                            rs.getString("nombre"),
                            rs.getInt("edad"),
                            rs.getLong("telefono"),
                            rs.getString("email"),
                            Puesto.valueOf(rs.getString("puesto")),
                            rs.getDouble("salario"),
                            LocalDate.parse(rs.getString("fecha_ingreso"))
                    );

                    empleados.guardar(empleado);
                }catch (DatoErroneoEx ex){
                    ex.getMessage();
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return empleados;
    }

    public static Generica<Cliente> getClientesBDD() {
        String query = """
        SELECT p.dni, p.nombre, p.edad, p.telefono, p.email,
               p.fecha_ingreso, c.numero_licencia, c.cantidad_alquileres
        FROM persona p
        INNER JOIN cliente c ON p.dni = c.dni
        """;

        Generica<Cliente> listaClientes = new Generica<>();

        try (Connection c = DriverManager.getConnection(url);
             PreparedStatement ps = c.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                try {
                    Cliente cliente = new Cliente(
                            rs.getLong("dni"),
                            rs.getString("nombre"),
                            rs.getInt("edad"),
                            rs.getLong("telefono"),
                            rs.getString("email"),
                            LocalDate.parse(rs.getString("fecha_ingreso")),
                            rs.getInt("numero_licencia"),
                            rs.getInt("cantidad_alquileres")
                    );

                    listaClientes.guardar(cliente);
                }catch (DatoErroneoEx ex){
                    ex.getMessage();
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return listaClientes;
    }

    /////////////////////////////////////////////////////////////////////
    public static ArrayList<Reporte> getReportesBDD()
    {
        String query = "SELECT * FROM reportes";
        ArrayList<Reporte> listaReportes = new ArrayList<>();
        try(Connection connection = DriverManager.getConnection(url))
        {
            PreparedStatement preparedStatement = connection.prepareStatement(query);


            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next())
            {
                int id = resultSet.getInt("id");
                String descripcion = resultSet.getString("descripcion");
                double coste = resultSet.getDouble("coste");
                boolean cargoAgencia = resultSet.getBoolean("cargo_agencia");

                Reporte aux = new Reporte(id,descripcion,coste,cargoAgencia);
                listaReportes.add(aux);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return  listaReportes;
    }

    /////
    public static Generica<Venta> listaVenta(Generica<Cliente> c , HashMap<String,Embarcacion>e, ArrayList <Reporte> r)
    {
        String query = "SELECT * FROM venta";
        Generica<Venta> listaVenta = new Generica<>();

        HashMap<Integer, Reporte> hashReportes = new HashMap<>();
        for(Reporte contador : r)
        {
            hashReportes.put(contador.getId(),contador);
        }
//////////////////////////
        try(Connection connection = DriverManager.getConnection(url))
        {
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next())
            {
                int id = resultSet.getInt("id");
                int idClienteComprador = resultSet.getInt("dni_cliente_comprador");
                String matriculaEmbarcacion = resultSet.getString("matricula_embarcacion");
                double montoBase = resultSet.getDouble("monto_base");
                int idReporteVenta = resultSet.getInt("id_reporte_venta");
                double montoFinal = resultSet.getDouble("monto_final");


                Embarcacion auxE = e.get(matriculaEmbarcacion);
                Cliente auxC = c.BuscarPorId(idClienteComprador);
                Reporte auxR = hashReportes.get(idReporteVenta);

                Venta aux = new Venta(id,auxC,auxE,montoBase,auxR,montoFinal);
                listaVenta.guardar(aux);
            }
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
        return listaVenta;
    }
    //////////
    public static Generica<Reserva> getReservasBDD(Generica<Cliente> c, HashMap <String,Embarcacion> e)
    {
        String query = "SELECT * FROM reserva";

        Generica <Reserva> listaReservas = new Generica<>();

        try(Connection connection = DriverManager.getConnection(url))
        {
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next())
            {
                int id = resultSet.getInt("id");
                int idCliente = resultSet.getInt("dni_cliente");
                int cantidadPersonas = resultSet.getInt("cantidad_personas");
                String matriculaEmbarcacion = resultSet.getString("matricula_embarcacion");
                LocalDate fechaDeInicio = resultSet.getDate("fecha_inicio").toLocalDate();
                LocalDate fechaDeFin = resultSet.getDate("fecha_fin").toLocalDate();
                String estadoReserva = resultSet.getString("estado_reserva");

                ESTADO_RESERVA reserva = null;
                switch (estadoReserva)
                {
                    case "CANCELADA":
                        reserva = ESTADO_RESERVA.CANCELADA;
                        break;
                    case "CONFIRMADA":
                        reserva = ESTADO_RESERVA.CONFIRMADA;
                        break;
                    case "PENDIENTE":
                        reserva = ESTADO_RESERVA.PENDIENTE;
                        break;
                    case "FINALIZADA":
                        reserva = ESTADO_RESERVA.FINALIZADA;
                        break;
                }
                ////////

                Cliente aux = c.BuscarPorId(idCliente);
                Embarcacion auxE = e.get(matriculaEmbarcacion);

                try {
                    Reserva auxR = new Reserva(id,aux,cantidadPersonas,auxE,fechaDeInicio.getYear(),fechaDeInicio.getMonthValue(),fechaDeInicio.getDayOfMonth(),fechaDeFin.getYear(),fechaDeFin.getMonthValue(),fechaDeFin.getDayOfMonth(),reserva);
                    listaReservas.guardar(auxR);
                }catch (DatoErroneoEx ex){
                    ex.getMessage();
                }
            }
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();;
        }
        return listaReservas;
    }

    public static Generica<Alquiler> getAlquileresBDD(Generica<Reserva> r)
    {
        String query = "SELECT * FROM alquileres";
        Generica<Alquiler> listaAlquileres = new Generica<>();

        try(Connection connection = DriverManager.getConnection(url))
        {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next())
            {
                int id = resultSet.getInt("id");
                int idReserva = resultSet.getInt("id_reserva");
                Double montoFinal = resultSet.getDouble("monto_final");

                Reserva aux = r.BuscarPorId(idReserva);

                Alquiler auxA = new Alquiler(id, aux, montoFinal);
                listaAlquileres.guardar(auxA);
            }
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();;
        }
        return listaAlquileres;
    }

    /// VELERO

    public static void agregarEmbarcacionBDD(String nombre, String matricula, double precioDiario, boolean disponible, int cantidadVelas, Double longitudMetros, int capacidadPersonas, int cantidadCamarotes, boolean requiereCapitan)
    {
        String query = """
    INSERT INTO velero
    (nombre, matricula, precio_diario, disponible,
     cantidad_velas, longitud_metros, capacidad_personas,
     cantidad_camarotes, requiere_capitan)
    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
    """;

        try (Connection c = DriverManager.getConnection(url);
             PreparedStatement ps = c.prepareStatement(query)){

            ps.setString(1, nombre);
            ps.setString(2, matricula);
            ps.setDouble(3, precioDiario);
            ps.setBoolean(4, disponible);
            ps.setInt(5, cantidadVelas);
            ps.setDouble(6, longitudMetros);
            ps.setInt(7, cantidadCamarotes);
            ps.setInt(8, capacidadPersonas);
            ps.setBoolean(9, requiereCapitan);

            ps.executeUpdate();

        }catch (SQLException ex){
            ex.printStackTrace();
        }
    }

    /// MotoDeAgua

    public static void agregarEmbarcacionBDD(String nombre, String matricula, double precioDiario, boolean disponible, int capacidadPersonas, int cilindrada, double limiteVelocidad, boolean requiereLicencia)
    {
        String query = """
    INSERT INTO moto_de_agua
    (nombre, matricula, precio_diario, disponible,
     capacidad_personas, cilindrada, limite_velocidad, requiere_licencia)
    VALUES (?, ?, ?, ?, ?, ?, ?, ?)
    """;

        try (Connection c = DriverManager.getConnection(url);
             PreparedStatement ps = c.prepareStatement(query)){

            ps.setString(1, nombre);
            ps.setString(2, matricula);
            ps.setDouble(3, precioDiario);
            ps.setBoolean(4, disponible);
            ps.setInt(5, capacidadPersonas);
            ps.setInt(6, cilindrada);
            ps.setDouble(7, limiteVelocidad);
            ps.setBoolean(8, requiereLicencia);

            ps.executeUpdate();

        }catch (SQLException ex){
            ex.printStackTrace();
        }
    }

    /// LANCHA

    public static void agregarEmbarcacionBDD(String nombre, String matricula, double precioDiario, boolean disponible, int potenciaMotor, double longitudMetros, int capacidadPersonas, TipoCombustible combustible)
    {
        String query = """
    INSERT INTO lancha
    (nombre, matricula, precio_diario, disponible,
     potencia_motor, longitud_metros, capacidad_personas, combustible)
    VALUES (?, ?, ?, ?, ?, ?, ?, ?)
    """;

        try (Connection c = DriverManager.getConnection(url);
             PreparedStatement ps = c.prepareStatement(query)){

            ps.setString(1, nombre);
            ps.setString(2, matricula);
            ps.setDouble(3, precioDiario);
            ps.setBoolean(4, disponible);
            ps.setInt(5, potenciaMotor);
            ps.setDouble(6, longitudMetros);
            ps.setInt(7, capacidadPersonas);
            ps.setString(8, combustible.name());

            ps.executeUpdate();

        }catch (SQLException ex){
            ex.printStackTrace();
        }
    }

    /// CLIENTE

    public static void agregarCliente(Cliente c){

        String query = """
    INSERT INTO clientes
    (dni, nombre, edad, telefono, email, fecha_ingreso,
     numero_licencia, cantidad_alquileres)
    VALUES (?, ?, ?, ?, ?, ?, ?, ?)
    """;

        try (Connection con = DriverManager.getConnection(url);
             PreparedStatement ps = con.prepareStatement(query)){

            ps.setLong(1, c.getID());
            ps.setString(2, c.getNombre());
            ps.setInt(3, c.getEdad());
            ps.setLong(4, c.getTelefono());
            ps.setString(5, c.getEmail());
            ps.setDate(6, java.sql.Date.valueOf(LocalDate.now()));
            ps.setInt(7, c.getNumeroLicencia());
            ps.setInt(8, c.getCantidadAlquileres());
            ps.executeUpdate();

        }catch (SQLException ex){
            ex.printStackTrace();
        }

    }

    /*public static void agregarEmpleado(Empleado e, String password){

            String query = "INSERT INTO usuarios (dni, nombre, edad, telefono, email, fecha_ingreso,puesto, salario) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            String sqlPassword = "INSERT INTO password (dni_empleado, password_hash) VALUES (?, ?)";

            try (Connection c = DriverManager.getConnection(url);
                 PreparedStatement ps = c.prepareStatement(query)){

                ps.setLong(1, e.getID());
                ps.setString(2, e.getNombre());
                ps.setInt(3, e.getEdad());
                ps.setLong(4, e.getTelefono());
                ps.setString(5, e.getEmail());
                ps.setString(6, e.getPuesto().name());
                ps.setDouble(7, e.getSalario());
                ps.setDate(8, java.sql.Date.valueOf(LocalDate.now()));

                ps.executeUpdate();

                try(ResultSet rs = addUserStatement.getGeneratedKeys();) {
                    if (rs.next()) {
                        idAgregado = rs.getInt(1);
                        try (PreparedStatement addPasswordStatement = c.prepareStatement(sqlPassword)) {
                            addPasswordStatement.setInt(1, idAgregado);
                            addPasswordStatement.setString(2, password);

                            addPasswordStatement.executeUpdate();

                        }
                    }
                }
            }
        }catch (SQLException ex){
            ex.printStackTrace();
        }

    }*/

    public static void agregarReservaBDD(Reserva reserva) {

        String query = """
        INSERT INTO reserva
        (dni_cliente, cantidad_personas, matricula_embarcacion,
         fecha_inicio, fecha_fin, estado_reserva)
        VALUES (?, ?, ?, ?, ?, ?)
        """;

        String matricula = reserva.getEmbarcacion().getID();

        if (!existeMatriculaBDD(matricula)) {
            System.out.println("La embarcación no existe.");
            return;
        }

        try (Connection c = DriverManager.getConnection(url);
             PreparedStatement ps = c.prepareStatement(query)) {

            ps.setLong(1, reserva.getCliente().getID());
            ps.setInt(2, reserva.getCantidadPersonas());
            ps.setString(3, matricula);
            ps.setString(4, reserva.getFechaDeInicio().toString());
            ps.setString(5, reserva.getFechaDeFin().toString());
            ps.setString(6, reserva.getEstadoReserva().name());

            ps.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static boolean agregarEmpleado(Empleado e, String password) {

        String sqlUsuario = "INSERT INTO usuarios (dni, nombre, edad, telefono, email, puesto, salario, fecha_ingreso) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        String sqlPassword = "INSERT INTO password (dni_usuario, password) VALUES (?, ?)";

        try (Connection c = DriverManager.getConnection(url);
             PreparedStatement psUsuario = c.prepareStatement(sqlUsuario);
             PreparedStatement psPassword = c.prepareStatement(sqlPassword)) {

            psUsuario.setLong(1, e.getID());
            psUsuario.setString(2, e.getNombre());
            psUsuario.setInt(3, e.getEdad());
            psUsuario.setLong(4, e.getTelefono());
            psUsuario.setString(5, e.getEmail());
            psUsuario.setString(6, e.getPuesto().name());
            psUsuario.setDouble(7, e.getSalario());
            psUsuario.setString(8, LocalDate.now().toString());

            psUsuario.executeUpdate();

            psPassword.setLong(1, e.getID());
            psPassword.setString(2, password);

            psPassword.executeUpdate();

            return true;

        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static Empleado getEmpleado(Long dni){
        Empleado empleado= null;
        String getUserQuery = "SELECT * from usuarios where  dni= ?";

        try (Connection c = DriverManager.getConnection(url);
             PreparedStatement ps = c.prepareStatement(getUserQuery)) {

            ps.setLong(1,dni);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    dni = rs.getLong("dni");
                    String nombre = rs.getString("nombre");
                    int edad = rs.getInt("edad");
                    long telefono = rs.getLong("telefono");
                    String email = rs.getString("email");
                    LocalDate fechaIngreso = rs.getDate("fecha_ingreso").toLocalDate();
                    Puesto puesto = Puesto.valueOf(rs.getString("puesto"));
                    double salario = rs.getDouble("salario");
                    try {
                        empleado = new Empleado(dni,nombre,edad,telefono,email,puesto,salario, fechaIngreso);
                    }catch (DatoErroneoEx ex){
                        ex.getMessage();
                    }
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

        return empleado;
    }

    //Conseguir Contraseña
    public static String getPassword(int dni){
        String password = null;
        String getPassQuery = "SELECT * from password where dni_empleado = ?";

        try (Connection c = DriverManager.getConnection(url);
             PreparedStatement ps = c.prepareStatement(getPassQuery)) {
            ps.setInt(1, dni);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    password = rs.getString("password_hash");
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return password;
    }

    ///////////

    public static int agregarReporteABDD(String descripcion, double coste, boolean cargo_agencia)
    {
        String queryAdd = "INSERT INTO reporte (descripcion,coste,cargo_agencia) VALUES (?,?,?) ";
        int id = -1;
        try(Connection connection = DriverManager.getConnection(url))
        {
            PreparedStatement preparedStatement = connection.prepareStatement(queryAdd, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1,descripcion);
            preparedStatement.setDouble(2,coste);
            preparedStatement.setBoolean(3,cargo_agencia);

            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if(resultSet.next())
            {
                id = resultSet.getInt(1);
            }
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
        return id;
    }

    /////////////////////////////
    public static int agregarVentaBDD(Cliente clienteComprador,Embarcacion embarcacion, double montoBase, Reporte reporteVenta, Double montoFinal)
    {
        String query = "INSERT INTO venta (dni_cliente_comprador,matricula_embarcacion,monto_base,id_reporte_venta,monto_final)VALUES(?,?,?,?)";
        int id = -1;
        long dni_cliente_comprador = clienteComprador.getID();
        String matricula_embarcacion = embarcacion.getID();
        int idReporte = reporteVenta.getId();
        try(Connection connection = DriverManager.getConnection(url))
        {
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setLong(1,dni_cliente_comprador);
            preparedStatement.setString(2,matricula_embarcacion);
            preparedStatement.setDouble(3,montoBase);
            preparedStatement.setInt(4,idReporte);
            preparedStatement.setDouble(5,montoFinal);

            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();

            if(resultSet.next())
            {
                id = resultSet.getInt(1);
            }

        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
        return id;
    }

    public static int agregarAlquilerBDD(Reserva reserva, double montoFinal)
    {
        String query = "INSERT INTO alquileres (id_reserva,monto_final) VALUES (?,?)";
        int id = -1;

        try(Connection connection = DriverManager.getConnection(url))
        {
            PreparedStatement preparedStatement = connection.prepareStatement(query,Statement.RETURN_GENERATED_KEYS) ;

            preparedStatement.setInt(1,reserva.getID());
            preparedStatement.setDouble(2,montoFinal);

            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();

            if(resultSet.next())
            {
                id = resultSet.getInt(1);
            }

        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
        return id;
    }

/////////////////////////BORRAR///////////////////////////////////////////

public static int eliminarClienteBDD(int dniCliente)
{
    String query = "DELETE FROM clientes WHERE dni=?";
    int filasAfectadas= 0;
    try(Connection connection = DriverManager.getConnection(url))
    {
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1,dniCliente);

        preparedStatement.executeUpdate();
        filasAfectadas = preparedStatement.executeUpdate();
    }catch (SQLException ex)
    {
        ex.printStackTrace();;
    }
    return filasAfectadas;
}

public static int borrarEmpleadoBDD(int dniEmpleado)
{
    String query = "DELETE FROM usuarios WHERE dni=?";
    int filasAfectadas= 0;
    try(Connection connection = DriverManager.getConnection(url))
    {
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1,dniEmpleado);

        filasAfectadas = preparedStatement.executeUpdate();
    }catch (SQLException ex)
    {
        ex.printStackTrace();;
    }
    return filasAfectadas;
}

public static <T extends Identificable<String>> int borrarEmbarcacion(T embarcacion )
{
    String query = "";
    int filasAfectadas = -1;
    if(embarcacion instanceof Velero)
    {
        query = "DELETE FROM velero WHERE matricula=?";
    }
    if(embarcacion instanceof Lancha)
    {
        query = "DELETE FROM lancha WHERE matricula=?";
    }
    if(embarcacion instanceof MotoDeAgua)
    {
        query = query = "DELETE FROM moto_de_agua WHERE matricula=?";
    }
    try (Connection connection = DriverManager.getConnection(url))
    {
      PreparedStatement preparedStatement = connection.prepareStatement(query);

      preparedStatement.setString(1,embarcacion.getID());
      filasAfectadas = preparedStatement.executeUpdate();
    }catch (SQLException ex)
    {
        ex.printStackTrace();;
    }
    return filasAfectadas;
}

public static int borrarReserva(int idReserva)
{
    String query = "DELETE FROM Reserva WHERE id = ? ";
    int filasAfectadas= 0;
    try(Connection connection = DriverManager.getConnection(url))
    {
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1,idReserva);

        preparedStatement.executeUpdate();
        filasAfectadas = preparedStatement.executeUpdate();
    }catch (SQLException ex)
    {
        ex.printStackTrace();;
    }
    return filasAfectadas;
}

///////MODIFICAR////////

public static int  modificarCamposCLienteBDD(int idClientes, Long telefono, String email, Integer cantidadAlquileres)
{
    StringBuilder query = new StringBuilder("UPDATE clientes SET ");
    ArrayList<Object> parametros = new ArrayList<>();
    int filasModificadas = 0;

    if(telefono != null)
    {
        query.append("telefono = ?, ");
        parametros.add(telefono);
    }
    if(email != null)
    {
        query.append("email = ?, ");
        parametros.add(email);
    }
    if(cantidadAlquileres != null)
    {
        query.append("cantidad_alquileres = ?, ");
        parametros.add(cantidadAlquileres);
    }
    if (parametros.isEmpty()) {
        return 0;
    }
    //eliminar comoa
    query.setLength(query.length() - 2);
    query.append(" WHERE dni = ?");
    parametros.add(idClientes);

    try (Connection connection = DriverManager.getConnection(url))
    {
        PreparedStatement preparedStatement = connection.prepareStatement(query.toString());


        for (int i = 0; i < parametros.size(); i++) {
            preparedStatement.setObject(i + 1, parametros.get(i));
        }

        filasModificadas = preparedStatement.executeUpdate();


    }catch (SQLException ex)
    {
        ex.printStackTrace();
    }
    return filasModificadas;
}

////////////

public static int  modificarCamposUsuarioBDD(int idEmpleado, Long telefono, String email, String puesto, Double salario)
{
    StringBuilder query = new StringBuilder("UPDATE usuarios SET ");
    ArrayList<Object> parametros = new ArrayList<>();
    int filasModificadas = 0;

    if(telefono != null)
    {
        query.append("telefono = ?, ");
        parametros.add(telefono);
    }
    if(email != null)
    {
        query.append("email = ?, ");
        parametros.add(email);
    }
    if(puesto != null)
    {
        query.append("puesto = ?, ");
        parametros.add(puesto);
    }
    if(salario !=  null)
    {
        query.append("salario = ?, ");
        parametros.add(salario);
    }
    if (parametros.isEmpty()) {
        return 0;
    }
    //eliminar comoa
    query.setLength(query.length() - 2);
    query.append(" WHERE dni = ?");
    parametros.add(idEmpleado);

    try (Connection connection = DriverManager.getConnection(url))
    {
        PreparedStatement preparedStatement = connection.prepareStatement(query.toString());


        for (int i = 0; i < parametros.size(); i++) {
            preparedStatement.setObject(i + 1, parametros.get(i));
        }

        filasModificadas = preparedStatement.executeUpdate();


    }catch (SQLException ex)
    {
        ex.printStackTrace();
    }
    return filasModificadas;
}
//////////////////////////////////////////

    public int modificarEstadoReservaBDD(int id, String estado_reserva) {
        String query = "UPDATE reserva SET estado_reserva = ? WHERE id = ?";
        int filasAfectadas = 0;

        try (Connection connection = DriverManager.getConnection(url))
        {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,estado_reserva);
            preparedStatement.setInt(2,id);

           filasAfectadas =  preparedStatement.executeUpdate();
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
        return filasAfectadas;
    }

    ///////////////////////////////////
}