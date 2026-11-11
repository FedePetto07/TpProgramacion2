package Clases;

import Enums.Puesto;
import Enums.TipoCombustible;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class BDDManager {

     static final String url = "jdbc:mysql://localhost/";


     public static String Conectar(){

        try(Connection c = DriverManager.getConnection(url)){
            return "Conexion exitosa";
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return null;
     }

     public static ArrayList<Embarcacion> getEmbarcacionesBDD() {

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
         ArrayList<Embarcacion> listaEmbarcacion = new ArrayList<>();

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
                        listaEmbarcacion.add(embarcacion);
                    }
                }
            }

         }catch(SQLException ex){
             ex.printStackTrace();
         }

         return listaEmbarcacion;
     }

    public static ArrayList<Empleado> getEmpleadosBDD() {
        String query = """
        SELECT p.dni, p.nombre, p.edad, p.telefono, p.email,
               p.fecha_ingreso, e.puesto, e.salario
        FROM persona p
        INNER JOIN empleado e ON p.dni = e.dni
        """;

        ArrayList<Empleado> listaEmpleados = new ArrayList<>();

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

                listaEmpleados.add(empleado);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return listaEmpleados;
    }

    public static ArrayList<Cliente> getClientesBDD() {
        String query = """
        SELECT p.dni, p.nombre, p.edad, p.telefono, p.email,
               p.fecha_ingreso, c.numero_licencia, c.cantidad_alquileres
        FROM persona p
        INNER JOIN cliente c ON p.dni = c.dni
        """;

        ArrayList<Cliente> listaClientes = new ArrayList<>();

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

                listaClientes.add(cliente);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return listaClientes;
    }


}
