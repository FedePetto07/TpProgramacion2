package Clases;

import Enums.ESTADO_RESERVA;
import Enums.Puesto;
import Enums.TipoCombustible;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class BDDManager {

     static final String url = "jdbc:mysql://localhost/";

     public static HashMap<String,Embarcacion> getEmbarcacionesBDD() {

         String query = """
        SELECT
            e.matricula AS matricula,
            e.nombre AS nombre,
            e.precio_diario AS precio_diario,
            e.disponible AS disponible,
            e.capacidad_personas AS capacidad_personas,
            e.tipo_embarcacion AS tipo_embarcacion,

            v.cantidad_velas AS cantidad_velas,
            v.longitud_metros AS longitud_velero,
            v.requiere_capitan AS requiere_capitan,
            v.cantidad_camarotes AS cantidad_camarotes,

            m.cilindrada AS cilindrada,
            m.requiere_licencia AS requiere_licencia,
            m.limite_velocidad AS limite_velocidad,

            l.potencia_motor AS potencia_motor,
            l.longitud_metros AS longitud_lancha,
            l.combustible AS combustible

        FROM embarcacion e
        LEFT JOIN velero v ON e.matricula = v.matricula
        LEFT JOIN moto_de_agua m ON e.matricula = m.matricula
        LEFT JOIN lancha l ON e.matricula = l.matricula
        """;
         HashMap<String,Embarcacion> listaEmbarcacion = new HashMap<>();

         try(Connection c = DriverManager.getConnection(url);
             PreparedStatement ps = c.prepareStatement(query)){

            try (ResultSet rs = ps.executeQuery()){
                while (rs.next()) {

                    String nombre = rs.getString("nombre");
                    String matricula = rs.getString("matricula");
                    double precio = rs.getDouble("precio_diario");
                    boolean disponible = rs.getBoolean("disponible");
                    int capacidad = rs.getInt("capacidad_personas");

                    String tipo = rs.getString("tipo_embarcacion");

                    Embarcacion embarcacion = null;

                    switch(tipo) {

                        case "Velero":

                            embarcacion = new Velero(
                                    nombre,
                                    matricula,
                                    precio,
                                    disponible,
                                    rs.getInt("cantidad_velas"),
                                    rs.getDouble("longitud_velero"),
                                    capacidad,
                                    rs.getInt("cantidad_camarotes"),
                                    rs.getBoolean("requiere_capitan")
                            );
                            break;

                        case "Moto de Agua":

                            embarcacion = new MotoDeAgua(
                                    nombre,
                                    matricula,
                                    precio,
                                    disponible,
                                    capacidad,
                                    rs.getInt("cilindrada"),
                                    rs.getDouble("limite_velocidad"),
                                    rs.getBoolean("requiere_licencia")
                            );
                            break;

                        case "Lancha":

                            embarcacion = new Lancha(
                                    nombre,
                                    matricula,
                                    precio,
                                    disponible,
                                    rs.getInt("potencia_motor"),
                                    rs.getDouble("longitud_lancha"),
                                    capacidad,
                                    TipoCombustible.valueOf(
                                            rs.getString("combustible")
                                    )
                            );
                            break;
                    }

                    if(embarcacion != null) {
                        listaEmbarcacion.put(matricula,embarcacion);
                    }
                }
            }

         }catch(SQLException ex){
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
                Empleado empleado = new Empleado(
                        rs.getInt("dni"),
                        rs.getString("nombre"),
                        rs.getInt("edad"),
                        rs.getInt("telefono"),
                        rs.getString("email"),
                        Puesto.valueOf(rs.getString("puesto")),
                        rs.getDouble("salario"),
                        LocalDate.parse(rs.getString("fecha_ingreso"))
                );

                empleados.guardar(empleado);
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
                Cliente cliente = new Cliente(
                        rs.getInt("dni"),
                        rs.getString("nombre"),
                        rs.getInt("edad"),
                        rs.getInt("telefono"),
                        rs.getString("email"),
                        LocalDate.parse(rs.getString("fecha_ingreso")),
                        rs.getInt("numero_licencia"),
                        rs.getInt("cantidad_alquileres")
                );

                listaClientes.guardar(cliente);
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
            boolean cargoAgencia = resultSet.getBoolean("cargoAgencia");

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
        String query = "SELECT * FROM Venta";
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
                int idClienteComprador = resultSet.getInt("idClienteComprador");
                String matriculaEmbarcacion = resultSet.getString("matriculaEmbarcacion");
                double montoBase = resultSet.getDouble("montoBase");
                int idReporteVenta = resultSet.getInt("idReporteVenta");
                double montoFinal = resultSet.getDouble("montoFinal");


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
    String query = "SELECT * FROM reservas";

    Generica <Reserva> listaReservas = new Generica<>();

    try(Connection connection = DriverManager.getConnection(url))
    {
        PreparedStatement preparedStatement = connection.prepareStatement(query);

        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next())
        {
            int id = resultSet.getInt("id");
            int idCliente = resultSet.getInt("idCLiente");
            int cantidadPersonas = resultSet.getInt("CantidadPersonas");
            String matriculaEmbarcacion = resultSet.getString("matriculaEmbarcacion");
             LocalDate fechaDeInicio = resultSet.getDate("fechaDeInicio").toLocalDate();
            LocalDate fechaDeFin = resultSet.getDate("fechaDeFin").toLocalDate();
            String estadoReserva = resultSet.getString("estadoReserva");

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

            Reserva auxR = new Reserva(id,aux,cantidadPersonas,auxE,fechaDeInicio.getYear(),fechaDeInicio.getMonthValue(),fechaDeInicio.getDayOfMonth(),fechaDeFin.getYear(),fechaDeFin.getMonthValue(),fechaDeFin.getDayOfMonth(),reserva);
            listaReservas.guardar(auxR);
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
            int idReserva = resultSet.getInt("idReserva");

            Reserva aux = r.BuscarPorId(idReserva);

            Alquiler auxA = new Alquiler(id, aux);
            listaAlquileres.guardar(auxA);
        }
    }
    catch (SQLException ex)
    {
        ex.printStackTrace();;
    }
    return listaAlquileres;
}

    public static void agregarEmbarcacionBDD(String nombre, String matricula, double precioDiario, boolean disponible, int cantidadVelas, Double longitudMetros, int capacidadPersonas, int cantidadCamarotes, boolean requiereCapitan)
    {
        int idAgregado = -1;
        String query = "INSERT INTO velero (nombre, matricula, precioDiario, disponible, cantidadVelas, longitudMetros, capacidadPersonas, cantidadCamarotes, requiereCapitan) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

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

    public static void agregarEmbarcacionBDD(String nombre, String matricula, double precioDiario, boolean disponible, int capacidadPersonas, int cilindrada, double limiteVelocidad, boolean requiereLicencia)
    {
        int idAgregado = -1;
        String query = "INSERT INTO velero (nombre, matricula, precioDiario, disponible, capacidadPersonas, cilindrada, limiteVelocidad, requiereLicencia) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

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

    public static void agregarEmbarcacionBDD(String nombre, String matricula, double precioDiario, boolean disponible, int potenciaMotor, double longitudMetros, int capacidadPersonas, TipoCombustible combustible)
    {
        int idAgregado = -1;
        String query = "INSERT INTO velero (nombre, matricula, precioDiario, disponible, potenciaMotor, longitudMetros, capacidadPersonas, combustible) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

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


}
