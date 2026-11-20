//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
import Clases.*;
import Enums.*;
import Excepciones.*;

import java.security.MessageDigest;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    private static final int MAX_INTENTOS = 3;
    private static final Scanner sc = new Scanner(System.in);
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT);

    private static final Agencia agencia = new Agencia();



    @FunctionalInterface
    private interface Accion {
        void ejecutar() throws Exception;
    }

    private static class EntradaCerradaException extends RuntimeException {
        EntradaCerradaException() {
            super("Se cerró la entrada de datos.");
        }
    }

    //// MAIN ////
    public static void main(String[] args) {

       /////////////////////////////////////////
        try {
            while (true) {
                Empleado empleado = iniciarSesion(agencia);

                if (empleado == null) {
                    break; // DNI "0" o demasiados intentos fallidos: se cierra el programa
                }

                System.out.println("\nBienvenido/a " + empleado.getNombre() + " (" + empleado.getPuesto() + ")");
                menuPrincipal(empleado);
                agencia.cerrarSesion();
            }
        } catch (EntradaCerradaException e) {
            System.out.println("\n" + e.getMessage());
        } catch (Exception e) {
            System.out.println("\nError inesperado: " + e.getMessage());
        } finally {
            agencia.cerrarSesion();
            System.out.println("\nPrograma finalizado.");
        }
    }

    //// INICIAR SESION ////

    private static Empleado iniciarSesion(Agencia agencia) {
        System.out.println("\n=== INICIO DE SESIÓN (escriba \"0\" como DNI para cerrar el programa) ===");

        for (int intento = 1; intento <= MAX_INTENTOS; intento++) {
            try {
                String dniTexto = leerTexto("DNI: ");
                if (dniTexto.equals("0")) {
                    return null;
                }

                int dni = Integer.parseInt(dniTexto);
                String password = leerTexto("Contraseña: ");

                return agencia.iniciarSesion(dni, password);

            } catch (NumberFormatException e) {
                System.out.println("Error: el DNI debe ser numérico. (intento " + intento + "/" + MAX_INTENTOS + ")");
            } catch (AccionImposibleEx e) {
                System.out.println("Error: " + e.getMessage() + " (intento " + intento + "/" + MAX_INTENTOS + ")");
            }
        }

        System.out.println("Demasiados intentos fallidos.");
        return null;
    }

    //// PERMISOS////

    private static boolean esGerente(Empleado empleado) {
        return empleado != null && empleado.getPuesto() == Puesto.GERENTE;
    }

    /// Doble seguridad: ademas de ocultar la opcion del menu, se valida el permiso al ejecutar.
    private static void exigirGerente(Empleado empleado, String accion) throws AccionImposibleEx {
        if (!esGerente(empleado)) {
            throw new AccionImposibleEx("Acceso denegado: solo un GERENTE puede " + accion + ".");
        }
    }

    /// Ejecuta una accion atrapando todas las excepciones que puede lanzar.
    private static void ejecutar(Accion accion) {
        try {
            accion.ejecutar();
        } catch (EntradaCerradaException e) {
            throw e; // se maneja en el main
        } catch (DatoErroneoEx e) {
            System.out.println("Dato erróneo: " + e.getMessage());
        } catch (ElementoDuplicadoEx e) {
            System.out.println("Elemento duplicado: " + e.getMessage());
        } catch (ObjetoNoEncontradoEx e) {
            System.out.println("No encontrado: " + e.getMessage());
        } catch (AccionImposibleEx e) {
            System.out.println("Acción imposible: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error inesperado: " + e.getMessage());
        }
    }

    //// MENU PRINCIPAL ////

    private static void menuPrincipal(Empleado empleado) {
        int opcion;

        do {
            System.out.println("\n===== MENÚ PRINCIPAL =====");
            System.out.println("1. Clientes");
            System.out.println("2. Embarcaciones");
            System.out.println("3. Reservas");
            System.out.println("4. Alquileres");
            System.out.println("5. Ventas");
            if (esGerente(empleado)) {
                System.out.println("6. Empleados");
            }
            System.out.println("0. Cerrar sesión");

            opcion = leerOpcion();

            switch (opcion) {
                case 1 -> menuClientes();
                case 2 -> menuEmbarcaciones(empleado);
                case 3 -> menuReservas();
                case 4 -> menuAlquileres();
                case 5 -> menuVentas();
                case 6 -> {
                    if (esGerente(empleado)) {
                        menuEmpleados(empleado);
                    } else {
                        System.out.println("Acceso denegado: solo un GERENTE puede gestionar empleados.");
                    }
                }
                case 0 -> System.out.println("Sesión cerrada.");
                default -> System.out.println("Opción inválida.");
            }
        } while (opcion != 0);
    }

    //// EMPLEADOS (solo gerente)////

    private static void menuEmpleados(Empleado empleado) {
        int opcion;

        do {
            System.out.println("\n--- EMPLEADOS ---");
            System.out.println("1. Agregar empleado");
            System.out.println("2. Ver todos los empleados");
            System.out.println("3. Ver empleado por DNI");
            System.out.println("4. Modificar empleado");
            System.out.println("5. Eliminar empleado");
            System.out.println("0. Volver");

            opcion = leerOpcion();

            switch (opcion) {
                case 1 -> ejecutar(() -> {
                    exigirGerente(empleado, "agregar empleados");
                    long dni = leerLong("DNI: ");
                    String nombre = leerTexto("Nombre: ");
                    int edad = leerEntero("Edad: ");
                    long telefono = leerLong("Teléfono (10 dígitos): ");
                    String email = leerTexto("Email (@gmail.com): ");
                    Puesto puesto = leerPuesto();
                    String contraseña = leerTexto("Contraseña: ");

                    if (agencia.agregarEmpleado(dni, nombre, edad, telefono, email, puesto, contraseña)) {
                        System.out.println("Empleado agregado correctamente.");
                    } else {
                        System.out.println("No se pudo guardar el empleado en la base de datos.");
                    }
                });
                case 2 -> ejecutar(() -> {
                    exigirGerente(empleado, "ver empleados");
                    System.out.println(agencia.listarEmpleados());
                });
                case 3 -> ejecutar(() -> {
                    exigirGerente(empleado, "ver empleados");
                    System.out.println(agencia.verEmpleadoId(leerEntero("DNI del empleado: ")));
                });
                case 4 -> ejecutar(() -> {
                    exigirGerente(empleado, "modificar empleados");
                    long dni = leerLong("DNI del empleado a modificar: ");
                    try {
                        agencia.existeDNI(dni);
                        System.out.println("(deje vacío lo que no quiera modificar)");
                        String nombre = leerTextoOpcional("Nombre nuevo: ");
                        Long telefono = leerLongOpcionalONulo("Teléfono nuevo: ");
                        String email = leerTextoOpcional("Email nuevo: ");
                        Puesto puesto = leerPuestoOpcional();
                        double salario = leerDoubleOpcional("Salario nuevo: ");

                        System.out.println(agencia.modificarDatosEmpleado(dni, nombre, telefono, email, puesto, salario));
                    }catch (ObjetoNoEncontradoEx ex)
                    {
                        System.out.println(ex.getMessage());
                    }
                });
                case 5 -> ejecutar(() -> {
                    exigirGerente(empleado, "eliminar empleados");
                    long dni = leerLong("DNI del empleado a eliminar: ");

                    if (empleado.getID() == dni) {
                        throw new AccionImposibleEx("No puede eliminar su propio usuario mientras tiene la sesión iniciada.");
                    }
                    if (!leerSiNo("¿Confirma que desea eliminar al empleado " + dni + "?")) {
                        System.out.println("Operación cancelada.");
                        return;
                    }
                    if (agencia.eliminarEmpleado(dni)) {
                        System.out.println("Empleado eliminado correctamente.");
                    } else {
                        System.out.println("No se pudo eliminar el empleado.");
                    }
                });
                case 0 -> { }
                default -> System.out.println("Opción inválida.");
            }
        } while (opcion != 0);
    }

    //// CLIENTES ////

    private static void menuClientes() {
        int opcion;

        do {
            System.out.println("\n--- CLIENTES ---");
            System.out.println("1. Agregar cliente");
            System.out.println("2. Ver todos los clientes");
            System.out.println("3. Ver cliente por DNI");
            System.out.println("4. Modificar cliente");
            System.out.println("5. Historial de reservas del cliente");
            System.out.println("6. Historial de alquileres del cliente");
            System.out.println("7. Historial de compras del cliente");
            System.out.println("0. Volver");

            opcion = leerOpcion();

            switch (opcion) {
                case 1 -> ejecutar(() -> {
                    long dni = leerLong("DNI: ");
                    String nombre = leerTexto("Nombre: ");
                    int edad = leerEntero("Edad: ");
                    long telefono = leerLong("Teléfono (10 dígitos): ");
                    String email = leerTexto("Email (@gmail.com): ");
                    int licencia = leerEntero("Número de licencia (0 si no tiene, o entre 5 y 9): ");

                    if (agencia.agregarCliente(dni, nombre, edad, telefono, email, licencia)) {
                        System.out.println("Cliente agregado correctamente.");
                    }
                });
                case 2 -> ejecutar(() -> System.out.println(agencia.listarClientes()));
                case 3 -> ejecutar(() -> System.out.println(agencia.verClienteDni(leerLong("DNI del cliente: "))));
                case 4 -> ejecutar(() -> {
                    long dni = leerLong("DNI del cliente a modificar: ");
                    try {
                        agencia.existeDNI(dni);
                        System.out.println("(deje vacío lo que no quiera modificar)");
                        String nombre = leerTextoOpcional("Nombre nuevo: ");
                        int edad = leerEnteroOpcional("Edad nueva: ");
                        Long telefono = leerLongOpcionalONulo("Teléfono nuevo: ");
                        String email = leerTextoOpcional("Email nuevo: ");
                        int licencia = leerEnteroOpcional("Número de licencia nuevo (5 a 9): ");

                        System.out.println(agencia.modificarDatosCliente(dni, nombre, edad, telefono, email, licencia));
                    }
                    catch (ObjetoNoEncontradoEx ex)
                    {
                        System.out.println(ex.getMessage());
                    }
                });
                case 5 -> ejecutar(() -> {
                    for (Reserva r : agencia.getReservasCliente(leerLong("DNI del cliente: "))) {
                        System.out.println(r);
                    }
                });
                case 6 -> ejecutar(() -> {
                    for (Alquiler a : agencia.getAlquileresCliente(leerLong("DNI del cliente: "))) {
                        System.out.println(a);
                    }
                });
                case 7 -> ejecutar(() -> {
                    for (Venta v : agencia.getVentasCliente(leerLong("DNI del cliente: "))) {
                        System.out.println(v);
                    }
                });
                case 0 -> { }
                default -> System.out.println("Opción inválida.");
            }
        } while (opcion != 0);
    }

    //// EMBARCACIONES ////

    private static void menuEmbarcaciones(Empleado empleado) {
        int opcion;

        do {
            System.out.println("\n--- EMBARCACIONES ---");
            System.out.println("1. Ver todas las embarcaciones");
            System.out.println("2. Ver embarcación por matrícula");
            if (esGerente(empleado)) {
                System.out.println("3. Agregar velero");
                System.out.println("4. Agregar moto de agua");
                System.out.println("5. Agregar lancha");
            }
            System.out.println("0. Volver");

            opcion = leerOpcion();

            switch (opcion) {
                case 1 -> ejecutar(() -> System.out.println(agencia.listarEmbarcaciones()));
                case 2 -> ejecutar(() -> System.out.println(agencia.verEmbarcacionMat(leerMatricula())));
                case 3 -> ejecutar(() -> {
                    exigirGerente(empleado, "agregar embarcaciones");
                    String nombre = leerTexto("Nombre: ");
                    String matricula = leerMatricula();
                    double precio = leerDouble("Precio diario (MINIMO 100.0): ");
                    int velas = leerEntero("Cantidad de velas (MAYOR A 5): ");
                    double longitud = leerDouble("Longitud (metros)(MINIMO 5): ");
                    int capacidad = leerEntero("Capacidad de personas (2 a 12): ");
                    int camarotes = leerEntero("Cantidad de camarotes (MIN 1): ");
                    boolean capitan = leerSiNo("¿Requiere capitán?");

                    if (agencia.registrarVelero(nombre, matricula, precio, true, velas, longitud, capacidad, camarotes, capitan)) {
                        System.out.println("Velero registrado correctamente.");
                    } else {
                        System.out.println("No se pudo guardar el velero en la base de datos.");
                    }
                });
                case 4 -> ejecutar(() -> {
                    exigirGerente(empleado, "agregar embarcaciones");
                    String nombre = leerTexto("Nombre: ");
                    String matricula = leerMatricula();
                    double precio = leerDouble("Precio diario (MINIMO 100.0): ");
                    int capacidad = leerEntero("Capacidad de personas (1 a 3): ");
                    int cilindrada = leerEntero("Cilindrada (cc MINIMO 1000): ");
                    double velocidad = leerDouble("Límite de velocidad (km/h MINIMO 60): ");
                    boolean licencia = leerSiNo("¿Requiere licencia?");

                    if (agencia.registrarMotoDeAgua(nombre, matricula, precio, true, capacidad, cilindrada, velocidad, licencia)) {
                        System.out.println("Moto de agua registrada correctamente.");
                    } else {
                        System.out.println("No se pudo guardar la moto de agua en la base de datos.");
                    }
                });
                case 5 -> ejecutar(() -> {
                    exigirGerente(empleado, "agregar embarcaciones");
                    String nombre = leerTexto("Nombre: ");
                    String matricula = leerMatricula();
                    double precio = leerDouble("Precio diario (MINIMO 100.0): ");
                    int potencia = leerEntero("Potencia del motor (Minimo 15): ");
                    double longitud = leerDouble("Longitud (Metros MAYOR O IGUAL A 3 ): ");
                    int capacidad = leerEntero("Capacidad de personas (2 a 12): ");
                    TipoCombustible combustible = leerCombustible();

                    if (agencia.registrarLancha(nombre, matricula, precio, true, potencia, longitud, capacidad, combustible)) {
                        System.out.println("Lancha registrada correctamente.");
                    } else {
                        System.out.println("No se pudo guardar la lancha en la base de datos.");
                    }
                });
                case 0 -> { }
                default -> System.out.println("Opción inválida.");
            }
        } while (opcion != 0);
    }

    //// RESERVAS ////

    private static void menuReservas() {
        int opcion;

        do {
            System.out.println("\n--- RESERVAS ---");
            System.out.println("1. Crear reserva");
            System.out.println("2. Cancelar reserva");
            System.out.println("3. Cambiar estado de una reserva");
            System.out.println("4. Ver reserva por ID");
            System.out.println("5. Ver todas las reservas");
            System.out.println("6. Eliminar reserva (solo si está CANCELADA)");
            System.out.println("0. Volver");

            opcion = leerOpcion();

            switch (opcion) {
                case 1 -> ejecutar(() -> {
                    int dni = leerEntero("DNI del cliente: ");
                    String matricula = leerMatricula();
                    int personas = leerEntero("Cantidad de personas: ");
                    LocalDate inicio = leerFecha("Fecha de inicio (dd/MM/yyyy): ");
                    LocalDate fin = leerFecha("Fecha de fin (dd/MM/yyyy): ");

                    if (agencia.agregarUnaReserva(dni, personas, matricula,
                            inicio.getYear(), inicio.getMonthValue(), inicio.getDayOfMonth(),
                            fin.getYear(), fin.getMonthValue(), fin.getDayOfMonth())) {
                        System.out.println("Reserva creada correctamente.");
                    } else {
                        System.out.println("No se pudo guardar la reserva en la base de datos.");
                    }
                });
                case 2 -> ejecutar(() -> System.out.println(agencia.cancelarReserva(leerEntero("ID de la reserva: "))));
                case 3 -> ejecutar(() -> {
                    int id = leerEntero("ID de la reserva: ");
                    ESTADO_RESERVA estado = leerEstadoReserva();
                    agencia.actualizarEstadoReserva(id, estado);
                    System.out.println("Estado actualizado correctamente.");
                });
                case 4 -> ejecutar(() -> System.out.println(agencia.listarReservaPorID(leerEntero("ID de la reserva: "))));
                case 5 -> ejecutar(() -> System.out.println(agencia.listarTodasLasReservas()));
                case 6 -> ejecutar(() -> {
                    int id = leerEntero("ID de la reserva a eliminar: ");

                    if (!leerSiNo("¿Confirma que desea eliminar la reserva " + id + "?")) {
                        System.out.println("Operación cancelada.");
                        return;
                    }
                    if (agencia.eliminarReserva(id)) {
                        System.out.println("Reserva eliminada correctamente.");
                    } else {
                        System.out.println("No se pudo eliminar la reserva.");
                    }
                });
                case 0 -> { }
                default -> System.out.println("Opción inválida.");
            }
        } while (opcion != 0);
    }

    ////ALQUILERES /////
    private static void menuAlquileres() {
        int opcion;

        do {
            System.out.println("\n--- ALQUILERES ---");
            System.out.println("1. Iniciar alquiler (desde una reserva confirmada)");
            System.out.println("2. Ver todos los alquileres");
            System.out.println("3. Ver alquiler por ID");
            System.out.println("0. Volver");

            opcion = leerOpcion();

            switch (opcion) {
                case 1 -> ejecutar(() -> {
                    if (agencia.iniciarAlquiler(leerEntero("ID de la reserva: "))) {
                        System.out.println("Alquiler iniciado correctamente.");
                    } else {
                        System.out.println("No se pudo iniciar el alquiler.");
                    }
                });
                case 2 -> ejecutar(() -> System.out.println(agencia.listarAlquileres()));
                case 3 -> ejecutar(() -> System.out.println(agencia.verAlquiler(leerEntero("ID del alquiler: "))));
                case 0 -> { }
                default -> System.out.println("Opción inválida.");
            }
        } while (opcion != 0);
    }

    //// VENTAS ////
    private static void menuVentas() {
        int opcion;

        do {
            System.out.println("\n--- VENTAS ---");
            System.out.println("1. Registrar venta de una embarcación");
            System.out.println("2. Ver todas las ventas");
            System.out.println("3. Ver venta por ID");
            System.out.println("0. Volver");

            opcion = leerOpcion();

            switch (opcion) {
                case 1 -> ejecutar(() -> {
                    long dni = leerLong("DNI del cliente comprador: ");
                    String matricula = leerMatricula();

                    if (agencia.registrarVenta(dni, matricula)) {
                        System.out.println("Venta registrada correctamente.");
                    } else {
                        System.out.println("No se pudo registrar la venta.");
                    }
                });
                case 2 -> ejecutar(() -> System.out.println(agencia.listarVentas()));
                case 3 -> ejecutar(() -> System.out.println(agencia.verVentaId(leerEntero("ID de la venta: "))));
                case 0 -> { }
                default -> System.out.println("Opción inválida.");
            }
        } while (opcion != 0);
    }

    //// LECTURAS OPCIONALES EXTRA ////

    /// Vacio = 0 (Agencia lo interpreta como "no cambiar").
    private static int leerEnteroOpcional(String mensaje) throws DatoErroneoEx {
        String s = leerTexto(mensaje);
        if (s.isEmpty()) {
            return 0;
        }
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            throw new DatoErroneoEx("'" + s + "' no es un número entero válido.");
        }
    }

    /// Vacio = 0 (Agencia lo interpreta como "no cambiar").
    private static double leerDoubleOpcional(String mensaje) throws DatoErroneoEx {
        String s = leerTexto(mensaje).replace(',', '.');
        if (s.isEmpty()) {
            return 0;
        }
        try {
            return Double.parseDouble(s);
        } catch (NumberFormatException e) {
            throw new DatoErroneoEx("'" + s + "' no es un número válido.");
        }
    }

    /// Vacio = null (no cambiar).
    private static Puesto leerPuestoOpcional() throws DatoErroneoEx {
        String s = leerTexto("Puesto nuevo (1 = Gerente, 2 = Recepcionista): ");
        if (s.isEmpty()) {
            return null;
        }
        return switch (s) {
            case "1" -> Puesto.GERENTE;
            case "2" -> Puesto.RECEPCIONISTA;
            default -> throw new DatoErroneoEx("Puesto inválido, debe ser 1 o 2.");
        };
    }



    //// LECTURA DE DATOS (todas las entradas invalidas terminan en DatoErroneoEx)
    ////

    private static String leerTexto(String mensaje) {
        System.out.print(mensaje);
        if (!sc.hasNextLine()) {
            throw new EntradaCerradaException();
        }
        return sc.nextLine().trim();
    }

    private static String leerTextoOpcional(String mensaje) {
        String s = leerTexto(mensaje);
        return s.isEmpty() ? null : s;
    }

    private static int leerEntero(String mensaje) throws DatoErroneoEx {
        String s = leerTexto(mensaje);
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            throw new DatoErroneoEx("'" + s + "' no es un número entero válido.");
        }
    }

    private static long leerLong(String mensaje) throws DatoErroneoEx {
        String s = leerTexto(mensaje);
        try {
            return Long.parseLong(s);
        } catch (NumberFormatException e) {
            throw new DatoErroneoEx("'" + s + "' no es un número válido.");
        }
    }

    /// Vacio = 0 (que Agencia.modificarDatosCliente interpreta como "no cambiar").
    private static long leerLongOpcional(String mensaje) throws DatoErroneoEx {
        Long valor = leerLongOpcionalONulo(mensaje);
        return valor == null ? 0 : valor;
    }

    /// Vacio = null.
    private static Long leerLongOpcionalONulo(String mensaje) throws DatoErroneoEx {
        String s = leerTexto(mensaje);
        if (s.isEmpty()) {
            return null;
        }
        try {
            return Long.parseLong(s);
        } catch (NumberFormatException e) {
            throw new DatoErroneoEx("'" + s + "' no es un número válido.");
        }
    }

    private static double leerDouble(String mensaje) throws DatoErroneoEx {
        String s = leerTexto(mensaje).replace(',', '.');
        try {
            return Double.parseDouble(s);
        } catch (NumberFormatException e) {
            throw new DatoErroneoEx("'" + s + "' no es un número válido.");
        }
    }

    private static boolean leerSiNo(String mensaje) throws DatoErroneoEx {
        String s = leerTexto(mensaje + " (s/n): ");
        if (s.equalsIgnoreCase("s")) {
            return true;
        }
        if (s.equalsIgnoreCase("n")) {
            return false;
        }
        throw new DatoErroneoEx("Debe responder s o n.");
    }

    private static String leerMatricula() {
        return leerTexto("Matrícula (formato ABC123): ").toUpperCase();
    }

    private static LocalDate leerFecha(String mensaje) throws DatoErroneoEx {
        String s = leerTexto(mensaje);
        try {
            return LocalDate.parse(s, FORMATO_FECHA);
        } catch (DateTimeParseException e) {
            throw new DatoErroneoEx("'" + s + "' no es una fecha válida (use dd/MM/yyyy).");
        }
    }

    private static int leerOpcion() {
        try {
            return leerEntero("Opción: ");
        } catch (DatoErroneoEx e) {
            System.out.println("Error: " + e.getMessage());
            return -1;
        }
    }

    private static Puesto leerPuesto() throws DatoErroneoEx {
        String s = leerTexto("Puesto (1 = Gerente, 2 = Recepcionista): ");
        return switch (s) {
            case "1" -> Puesto.GERENTE;
            case "2" -> Puesto.RECEPCIONISTA;
            default -> throw new DatoErroneoEx("Puesto inválido, debe ser 1 o 2.");
        };
    }

    private static TipoCombustible leerCombustible() throws DatoErroneoEx {
        TipoCombustible[] valores = TipoCombustible.values();
        StringBuilder sb = new StringBuilder("Combustible (");
        for (int i = 0; i < valores.length; i++) {
            sb.append(i + 1).append(" = ").append(valores[i]).append(i < valores.length - 1 ? ", " : "): ");
        }

        int n = leerEntero(sb.toString());
        if (n < 1 || n > valores.length) {
            throw new DatoErroneoEx("Combustible inválido.");
        }
        return valores[n - 1];
    }

    private static ESTADO_RESERVA leerEstadoReserva() throws DatoErroneoEx {
        ESTADO_RESERVA[] valores = ESTADO_RESERVA.values();
        StringBuilder sb = new StringBuilder("Estado (");
        for (int i = 0; i < valores.length; i++) {
            sb.append(i + 1).append(" = ").append(valores[i]).append(i < valores.length - 1 ? ", " : "): ");
        }

        int n = leerEntero(sb.toString());
        if (n < 1 || n > valores.length) {
            throw new DatoErroneoEx("Estado inválido.");
        }
        return valores[n - 1];
    }


}