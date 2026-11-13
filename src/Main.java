//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
import Clases.*;
import Enums.*;
import Excepciones.*;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    private static final Scanner sc = new Scanner(System.in);
    private static final Agencia agencia = new Agencia();

    // ===================== MAIN =====================
    public static void main(String[] args) {
        boolean salir = false;
        do {
            Empleado empleado = iniciarSesion();
            if (empleado == null) {
                salir = true;
            } else if (empleado.getPuesto() == Puesto.GERENTE) {
                menuGerente();
            } else if (empleado.getPuesto() == Puesto.RECEPCIONISTA) {
                menuRecepcionista();
            }
            agencia.cerrarSesion();
        } while (!salir);
        System.out.println("Programa finalizado.");
    }

    // ===================== INICIO DE SESIÓN =====================
    private static Empleado iniciarSesion() {
        while (true) {
            System.out.println("\n===== INICIO DE SESIÓN =====");
            int dni = leerInt("DNI del empleado (0 para salir): ");
            if (dni == 0) return null;
            System.out.print("Contraseña: ");
            String pass = sc.nextLine();

            try {
                // Se envía el HASH de lo ingresado: Agencia lo compara contra el hash guardado
                Empleado emp = agencia.iniciarSesion(dni, hashear(pass));
                System.out.println("Bienvenido/a " + emp.getNombre());
                return emp;
            } catch (Exception e) {
                mostrarError(e);
            }
        }
    }

    // ASUMIDO: SHA-256 en hexadecimal. Si el proyecto usa otro, cambiar solo este método.
    private static String hashear(String texto) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(texto.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            return "";
        }
    }

    // ===================== MENÚS PRINCIPALES =====================
    private static void menuGerente() {
        menuPrincipal(Puesto.GERENTE);
    }

    private static void menuRecepcionista() {
        menuPrincipal(Puesto.RECEPCIONISTA);
    }

    private static void menuPrincipal(Puesto puesto) {
        int op;
        do {
            System.out.println("\n===== MENÚ " + puesto + " =====");
            System.out.println("1. Gestión de clientes");
            System.out.println("2. Gestión de empleados");
            System.out.println("3. Gestión de embarcaciones");
            System.out.println("4. Gestión de reservas");
            System.out.println("5. Gestión de alquileres");
            System.out.println("6. Gestión de ventas");
            System.out.println("7. Gestión de reportes");
            System.out.println("0. Cerrar sesión");
            op = leerInt("Opción: ");
            switch (op) {
                case 1: menuClientes(puesto); break;
                case 2: menuEmpleados(puesto); break;
                case 3: menuEmbarcaciones(puesto); break;
                case 4: menuReservas(puesto); break;
                case 5: menuAlquileres(puesto); break;
                case 6: menuVentas(puesto); break;
                case 7: menuReportes(puesto); break;
                case 0: System.out.println("Sesión cerrada."); break;
                default: System.out.println("Opción inválida.");
            }
        } while (op != 0);
    }

    // ===================== CLIENTES (gerente y recepcionista: todo) =====================
    private static void menuClientes(Puesto puesto) {
        int op;
        do {
            System.out.println("\n--- CLIENTES ---");
            System.out.println("1. Registrar\n2. Listar todos\n3. Buscar por DNI");
            System.out.println("4. Ver reservas de un cliente\n5. Ver alquileres de un cliente\n6. Ver compras de un cliente");
            System.out.println("7. Modificar\n8. Eliminar\n0. Volver");
            op = leerInt("Opción: ");
            try {
                switch (op) {
                    case 1:
                        Long dni = leerLong("DNI: ");
                        String nombre = leerTexto("Nombre: ");
                        int edad = leerInt("Edad: ");
                        Long tel = leerLong("Teléfono: ");
                        String email = leerTexto("Email: ");
                        int licencia = leerInt("Número de licencia: ");
                        agencia.agregarCliente(dni, nombre, edad, tel, email, licencia);
                        System.out.println("Cliente registrado.");
                        break;
                    case 2:
                        System.out.println(agencia.listarClientes());
                        break;
                    case 3:
                        System.out.println(agencia.verClienteDni(leerLong("DNI: ")));
                        break;
                    case 4:
                        for (Reserva r : agencia.getReservasCliente(leerLong("DNI: "))) System.out.println(r);
                        break;
                    case 5:
                        for (Alquiler a : agencia.getAlquileresCliente(leerLong("DNI: "))) System.out.println(a);
                        break;
                    case 6:
                        for (Venta v : agencia.getVentasCliente(leerLong("DNI: "))) System.out.println(v);
                        break;
                    case 7:
                        modificarCliente();
                        break;
                    case 8:
                        agencia.eliminarCliente(leerLong("DNI del cliente a eliminar: "));
                        System.out.println("Cliente eliminado.");
                        break;
                    case 0: break;
                    default: System.out.println("Opción inválida.");
                }
            } catch (Exception e) {
                mostrarError(e);
            }
        } while (op != 0);
    }

    private static void modificarCliente() throws Exception {
        long dni = leerLong("DNI del cliente a modificar: ");
        System.out.println(agencia.verClienteDni(dni)); // lanza ObjetoNoEncontradoEx si no existe
        System.out.println("(Enter para no modificar un campo)");
        String nombre = leerOpcionalTexto("Nuevo nombre: ");
        int edad = leerOpcionalInt("Nueva edad: ");
        Long tel = leerOpcionalLong("Nuevo teléfono: ");
        String email = leerOpcionalTexto("Nuevo email: ");
        int licencia = leerOpcionalInt("Nueva licencia: ");
        System.out.println(agencia.modificarDatosCliente(dni, nombre, edad, tel, email, licencia));
    }

    // ===================== EMPLEADOS =====================
    private static void menuEmpleados(Puesto puesto) {
        boolean gerente = (puesto == Puesto.GERENTE);
        int op;
        do {
            System.out.println("\n--- EMPLEADOS ---");
            if (gerente) System.out.println("1. Registrar");
            System.out.println("2. Listar todos\n3. Buscar por DNI");
            if (gerente) System.out.println("4. Modificar\n5. Eliminar");
            System.out.println("0. Volver");
            op = leerInt("Opción: ");
            if (!gerente && (op == 1 || op == 4 || op == 5)) {
                System.out.println("No tiene permisos para esta acción.");
                continue;
            }
            try {
                switch (op) {
                    case 1:
                        Long dni = leerLong("DNI: ");
                        String nombre = leerTexto("Nombre: ");
                        int edad = leerInt("Edad: ");
                        Long tel = leerLong("Teléfono: ");
                        String email = leerTexto("Email: ");
                        Puesto p = leerEnum(Puesto.class, "Puesto");
                        System.out.print("Contraseña: ");
                        String pass = sc.nextLine();
                        agencia.agregarEmpleado(dni, nombre, edad, tel, email, p, hashear(pass));
                        System.out.println("Empleado registrado.");
                        break;
                    case 2:
                        System.out.println(agencia.listarEmpleados());
                        break;
                    case 3:
                        System.out.println(agencia.verEmpleadoId(leerInt("DNI: ")));
                        break;
                    case 4:
                        // FALTA en Agencia: modificarEmpleado (ver lista de faltantes)
                        System.out.println("Función no disponible: falta modificarEmpleado en Agencia.");
                        break;
                    case 5:
                        agencia.eliminarEmpleado(leerInt("DNI del empleado a eliminar: "));
                        System.out.println("Empleado eliminado.");
                        break;
                    case 0: break;
                    default: System.out.println("Opción inválida.");
                }
            } catch (Exception e) {
                mostrarError(e);
            }
        } while (op != 0);
    }

    // ===================== EMBARCACIONES =====================
    // Nadie puede modificar embarcaciones. Solo el gerente registra y elimina.
    private static void menuEmbarcaciones(Puesto puesto) {
        boolean gerente = (puesto == Puesto.GERENTE);
        int op;
        do {
            System.out.println("\n--- EMBARCACIONES ---");
            if (gerente) System.out.println("1. Registrar");
            System.out.println("2. Listar todas\n3. Buscar por matrícula");
            if (gerente) System.out.println("4. Eliminar");
            System.out.println("0. Volver");
            op = leerInt("Opción: ");
            if (!gerente && (op == 1 || op == 4)) {
                System.out.println("No tiene permisos para esta acción.");
                continue;
            }
            try {
                switch (op) {
                    case 1: registrarEmbarcacion(); break;
                    case 2: System.out.println(agencia.listarEmbarcaciones()); break;
                    case 3: System.out.println(agencia.verEmbarcacionMat(leerTexto("Matrícula: "))); break;
                    case 4:
                        agencia.eliminarEmbarcacion(leerTexto("Matrícula a eliminar: "));
                        System.out.println("Embarcación eliminada.");
                        break;
                    case 0: break;
                    default: System.out.println("Opción inválida.");
                }
            } catch (Exception e) {
                mostrarError(e);
            }
        } while (op != 0);
    }

    private static void registrarEmbarcacion() throws Exception {
        System.out.println("Tipo: 1. Lancha  2. Velero  3. Moto de agua");
        int tipo = leerInt("Tipo: ");
        if (tipo < 1 || tipo > 3) {
            System.out.println("Tipo inválido.");
            return;
        }
        String nombre = leerTexto("Nombre: ");
        String matricula = leerTexto("Matrícula: ");
        double precio = leerDouble("Precio diario: ");
        int capacidad = leerInt("Capacidad de personas: ");

        switch (tipo) {
            case 1:
                int potencia = leerInt("Potencia del motor: ");
                double longL = leerDouble("Longitud (metros): ");
                TipoCombustible comb = leerEnum(TipoCombustible.class, "Combustible");
                agencia.registrarLancha(nombre, matricula, precio, true, potencia, longL, capacidad, comb);
                break;
            case 2:
                int velas = leerInt("Cantidad de velas: ");
                double longV = leerDouble("Longitud (metros): ");
                int camarotes = leerInt("Cantidad de camarotes: ");
                boolean capitan = leerBoolean("¿Requiere capitán?");
                agencia.registrarVelero(nombre, matricula, precio, true, velas, longV, capacidad, camarotes, capitan);
                break;
            case 3:
                int cilindrada = leerInt("Cilindrada: ");
                double velMax = leerDouble("Límite de velocidad: ");
                boolean licencia = leerBoolean("¿Requiere licencia?");
                agencia.registrarMotoDeAgua(nombre, matricula, precio, true, capacidad, cilindrada, velMax, licencia);
                break;
        }
        System.out.println("Embarcación registrada.");
    }

    // ===================== RESERVAS =====================
    // Ambos puestos: registrar, consultar y modificar SOLO el estado
    private static void menuReservas(Puesto puesto) {
        int op;
        do {
            System.out.println("\n--- RESERVAS ---");
            System.out.println("1. Registrar\n2. Listar todas\n3. Buscar por ID\n4. Modificar estado\n0. Volver");
            op = leerInt("Opción: ");
            try {
                switch (op) {
                    case 1: registrarReserva(); break;
                    case 2: System.out.println(agencia.listarTodasLasReservas()); break;
                    case 3: System.out.println(agencia.listarReservaPorID(leerInt("ID de la reserva: "))); break;
                    case 4: modificarEstadoReserva(); break;
                    case 0: break;
                    default: System.out.println("Opción inválida.");
                }
            } catch (Exception e) {
                mostrarError(e);
            }
        } while (op != 0);
    }

    private static void registrarReserva() throws Exception {
        int id = leerInt("ID de la reserva: ");
        Cliente cliente = agencia.getCliente(leerLong("DNI del cliente: "));                   // A AGREGAR en Agencia
        Embarcacion emb = agencia.getEmbarcacion(leerTexto("Matrícula de la embarcación: ")); // A AGREGAR en Agencia
        int personas = leerInt("Cantidad de personas: ");
        System.out.println("Fecha de inicio:");
        int aI = leerInt("  Año: "), mI = leerInt("  Mes: "), dI = leerInt("  Día: ");
        System.out.println("Fecha de fin:");
        int aF = leerInt("  Año: "), mF = leerInt("  Mes: "), dF = leerInt("  Día: ");
        // Si cliente o embarcación son null, Agencia lanza AccionImposibleEx
        agencia.agregarUnaReserva(id, cliente, personas, emb, aI, mI, dI, aF, mF, dF, ESTADO_RESERVA.PENDIENTE);
        System.out.println("Reserva registrada (estado PENDIENTE).");
    }

    private static void modificarEstadoReserva() throws Exception {
        int id = leerInt("ID de la reserva: ");
        System.out.println(agencia.listarReservaPorID(id)); // lanza ObjetoNoEncontradoEx si no existe
        ESTADO_RESERVA nuevo = leerEnum(ESTADO_RESERVA.class, "Nuevo estado");
        if (nuevo == ESTADO_RESERVA.CANCELADA) {
            System.out.println(agencia.cancelarReserva(id)); // aplica las reglas de cancelación
        } else {
            agencia.actualizarEstadoReserva(id, nuevo);
            System.out.println("Estado actualizado.");
        }
    }

    // ===================== ALQUILERES =====================
    private static void menuAlquileres(Puesto puesto) {
        int op;
        do {
            System.out.println("\n--- ALQUILERES ---");
            System.out.println("1. Listar todos\n2. Buscar por ID\n3. Iniciar alquiler\n0. Volver");
            op = leerInt("Opción: ");
            try {
                switch (op) {
                    case 1: System.out.println(agencia.listarAlquileres()); break;
                    case 2: System.out.println(agencia.verAlquiler(leerInt("ID del alquiler: "))); break;
                    case 3:
                        int id = leerInt("ID para el nuevo alquiler: ");
                        int idReserva = leerInt("ID de la reserva (debe estar CONFIRMADA): ");
                        if (agencia.IniciarAlquiler(id, idReserva)) System.out.println("Alquiler iniciado.");
                        else System.out.println("No se pudo iniciar el alquiler.");
                        break;
                    case 0: break;
                    default: System.out.println("Opción inválida.");
                }
            } catch (Exception e) {
                mostrarError(e);
            }
        } while (op != 0);
    }

    // ===================== VENTAS =====================
    private static void menuVentas(Puesto puesto) {
        int op;
        do {
            System.out.println("\n--- VENTAS ---");
            System.out.println("1. Registrar pago (iniciar venta)\n2. Listar ventas\n3. Buscar venta por ID\n0. Volver");
            op = leerInt("Opción: ");
            try {
                switch (op) {
                    case 1:
                        if (agencia.iniciarVenta(leerInt("ID del alquiler a pagar: "))) System.out.println("Pago registrado.");
                        else System.out.println("No se pudo registrar el pago.");
                        break;
                    case 2: System.out.println(agencia.listarVentas()); break;
                    case 3: System.out.println(agencia.verVentaId(leerInt("ID de la venta: "))); break;
                    case 0: break;
                    default: System.out.println("Opción inválida.");
                }
            } catch (Exception e) {
                mostrarError(e);
            }
        } while (op != 0);
    }

    // ===================== REPORTES =====================
    private static void menuReportes(Puesto puesto) {
        int op;
        do {
            System.out.println("\n--- REPORTES ---");
            System.out.println("1. Consultar\n2. Registrar\n0. Volver");
            op = leerInt("Opción: ");
            try {
                switch (op) {
                    case 1:
                        // FALTA en Agencia: listarReportes()
                        System.out.println("Función no disponible: falta listarReportes en Agencia.");
                        // System.out.println(agencia.listarReportes());
                        break;
                    case 2:
                        // FALTA en Agencia: registrarReporte(...)
                        System.out.println("Función no disponible: falta registrarReporte en Agencia.");
                        // int idVenta = leerInt("ID de la venta: ");
                        // String desc = leerTexto("Descripción: ");
                        // double coste = leerDouble("Coste: ");
                        // boolean cargo = leerBoolean("¿Corre por cuenta de la agencia?");
                        // agencia.registrarReporte(idVenta, desc, coste, cargo);
                        break;
                    case 0: break;
                    default: System.out.println("Opción inválida.");
                }
            } catch (Exception e) {
                mostrarError(e);
            }
        } while (op != 0);
    }

    // ===================== UTILIDADES =====================
    private static void mostrarError(Exception e) {
        if (e instanceof DatoErroneoEx) System.out.println("Dato erróneo: " + e.getMessage());
        else if (e instanceof AccionImposibleEx) System.out.println("Acción imposible: " + e.getMessage());
        else if (e instanceof ElementoDuplicadoEx) System.out.println("Elemento duplicado: " + e.getMessage());
        else if (e instanceof ObjetoNoEncontradoEx) System.out.println("No encontrado: " + e.getMessage());
        else System.out.println("Error: " + e.getMessage());
    }

    private static <T extends Enum<T>> T leerEnum(Class<T> clase, String titulo) {
        T[] valores = clase.getEnumConstants();
        while (true) {
            System.out.println(titulo + ":");
            for (int i = 0; i < valores.length; i++) System.out.println("  " + (i + 1) + ". " + valores[i]);
            int op = leerInt("Opción: ");
            if (op >= 1 && op <= valores.length) return valores[op - 1];
            System.out.println("Opción inválida.");
        }
    }

    private static String leerTexto(String msg) {
        String s;
        do {
            System.out.print(msg);
            s = sc.nextLine().trim();
            if (s.isEmpty()) System.out.println("No puede estar vacío.");
        } while (s.isEmpty());
        return s;
    }

    private static String leerOpcionalTexto(String msg) {
        System.out.print(msg);
        String s = sc.nextLine().trim();
        return s.isEmpty() ? null : s;
    }

    private static int leerOpcionalInt(String msg) {
        while (true) {
            System.out.print(msg);
            String s = sc.nextLine().trim();
            if (s.isEmpty()) return 0;
            try { return Integer.parseInt(s); }
            catch (NumberFormatException e) { System.out.println("Número inválido."); }
        }
    }

    private static Long leerOpcionalLong(String msg) {
        while (true) {
            System.out.print(msg);
            String s = sc.nextLine().trim();
            if (s.isEmpty()) return null;
            try { return Long.parseLong(s); }
            catch (NumberFormatException e) { System.out.println("Número inválido."); }
        }
    }

    private static int leerInt(String msg) {
        while (true) {
            System.out.print(msg);
            try { return Integer.parseInt(sc.nextLine().trim()); }
            catch (NumberFormatException e) { System.out.println("Ingrese un número entero válido."); }
        }
    }

    private static long leerLong(String msg) {
        while (true) {
            System.out.print(msg);
            try { return Long.parseLong(sc.nextLine().trim()); }
            catch (NumberFormatException e) { System.out.println("Ingrese un número válido."); }
        }
    }

    private static double leerDouble(String msg) {
        while (true) {
            System.out.print(msg);
            try { return Double.parseDouble(sc.nextLine().trim().replace(',', '.')); }
            catch (NumberFormatException e) { System.out.println("Ingrese un número decimal válido."); }
        }
    }

    private static boolean leerBoolean(String msg) {
        while (true) {
            System.out.print(msg + " (s/n): ");
            String s = sc.nextLine().trim().toLowerCase();
            if (s.equals("s")) return true;
            if (s.equals("n")) return false;
            System.out.println("Responda s o n.");
        }
    }
}
