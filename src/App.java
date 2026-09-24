import certificacion.Certificable;
import excepciones.CupoExcedidoException;
import hilos.EnvioTicketsThread;
import modelo.Estudiante;
import modelo.EventoUniversitario;
import modelo.Inscripcion;
import modelo.Sala;
import modelo.actividades.Actividad;
import modelo.actividades.Charla;
import modelo.actividades.Curso;
import modelo.actividades.Taller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class App {

    public static void main(String[] args) {
        ejercicio1();
        ejercicio2();
        ejercicio3();
        ejercicio4();
    }

    private static void ejercicio1() {
        System.out.println( "=== TP2 - EJERCICIO 1 ===" );

        System.out.println( "\n--- Creación de estudiantes, sala y evento ---" );
        Estudiante ana = new Estudiante("1001", "Ana Pérez");
        Estudiante juan = new Estudiante("1002", "Juan López");
        Estudiante maria = new Estudiante("1003", "María Gómez");

        Sala sala = new Sala(1, "Auditorio Principal");

        EventoUniversitario evento = new EventoUniversitario("EVT-001", "Semana de la Ingeniería", 1000.0, false);
        evento.asignarSala(sala);
        evento.crearActividad("Introducción a IA", 2, "Charla", "Lic. Gómez", false);
        evento.crearActividad("Taller de Robótica", 15, "Taller", null, true);
        evento.mostrarDatos();

        Actividad charla = evento.getActividades().get(0);

        System.out.println( "\n--- Caso exitoso: inscripciones dentro del cupo ---" );
        try {
            charla.inscribir(ana);
            charla.inscribir(juan);
            System.out.println( "Ana y Juan inscriptos correctamente." );
            boolean guardadoOk = evento.persistirEvento();
            System.out.println("Evento guardado: " + guardadoOk);
            EventoUniversitario recuperado = evento.recuperarEvento(evento.getEventoId());
            System.out.println("Evento recuperado: " + recuperado);
        } catch (CupoExcedidoException e) {
            System.out.println( e.getMessage() );
        } finally {
            System.out.println("=================================");
            System.out.println( "Realizando limpieza..." );
            File eventoArchivo = new File(evento.getEventoId() + ".dat");
            eventoArchivo.delete();
            System.out.println( "Limpieza realizada con éxito" );
        }

        System.out.println( "\n--- Caso fallido: cupo excedido ---" );
        try {
            charla.inscribir(maria);
            System.out.println( "María inscripta correctamente." );
        } catch (CupoExcedidoException e) {
            System.out.println( e.getMessage() );
        }

        System.out.println( "\n--- Estado final del evento ---" );
        evento.mostrarDatos();
        System.out.println("Cantidad de eventos: " + EventoUniversitario.getCantidadEventos());
    }

    private static void ejercicio2() {
        System.out.println( "\n\n=== TP2 - EJERCICIO 2 ===" );

        System.out.println( "\n--- Creación de estudiantes, sala, evento y actividades ---" );
        Estudiante ana = new Estudiante("1501", "Ana Beltrán");
        Estudiante luis = new Estudiante("1502", "Luis Cano");

        Sala sala = new Sala(2, "Aula Magna");

        EventoUniversitario evento = new EventoUniversitario("EVT-002", "Semana Tech", 800.0, false);
        evento.asignarSala(sala);
        evento.crearActividad("Historia de la IA", 40, "Charla", "Dr. Salas", false);
        evento.crearActividad("Taller de Docker", 12, "Taller", null, true);
        evento.crearActividad("Curso de Java Avanzado", 20, "Curso", null, false, 3);

        try {
            System.out.println( "\n--- Inscripción de alumnos en cada actividad ---" );
            for (Actividad actividad : evento.getActividades()) {
                actividad.inscribir(ana);
                actividad.inscribir(luis);
            }
            System.out.println( "Ana y Luis inscriptos en las 3 actividades." );

            System.out.println( "\n--- Emisión de certificados (talleres y cursos, no charlas) ---" );
            List<String> certificados = new ArrayList<>();
            for (Actividad actividad : evento.getActividades()) {
                if (actividad instanceof Certificable certificable) {
                    for (Inscripcion inscripcion : actividad.getInscripciones()) {
                        certificados.add(certificable.generarCertificado(inscripcion.getEstudiante()));
                    }
                }
            }
            for (String certificado : certificados) {
                System.out.println( certificado );
            }
        } catch (CupoExcedidoException e) {
            System.out.println( e.getMessage() );
        }

        System.out.println( "\n--- Datos del evento ---" );
        evento.mostrarDatos();
    }

    private static void ejercicio3() {
        System.out.println( "\n\n=== TP2 - EJERCICIO 3 ===" );

        System.out.println( "\n--- Creación de estudiantes, sala, evento y actividades ---" );
        Estudiante ana = new Estudiante("1601", "Ana Farías");
        Estudiante bruno = new Estudiante("1602", "Bruno Ibarra");

        Sala sala = new Sala(3, "Laboratorio 2");

        EventoUniversitario evento = new EventoUniversitario("EVT-003", "Congreso de Sistemas", 1200.0, false);
        evento.asignarSala(sala);
        evento.crearActividad("Charla de apertura", 100, "Charla", "Ing. Roca", false);
        evento.crearActividad("Taller de Kubernetes", 15, "Taller", null, true);
        evento.crearActividad("Curso de Bases de Datos", 25, "Curso", null, false, 2);

        try {
            System.out.println( "\n--- Inscripción de alumnos ---" );
            for (Actividad actividad : evento.getActividades()) {
                actividad.inscribir(ana);
            }
            evento.getActividades().get(1).inscribir(bruno);
            System.out.println( "Inscripciones realizadas." );

            List<Charla> charlas = evento.filtrarActividadesPorTipo(Charla.class);
            List<Taller> talleres = evento.filtrarActividadesPorTipo(Taller.class);
            List<Curso> cursos = evento.filtrarActividadesPorTipo(Curso.class);

            System.out.println( "\n--- Cantidad de actividades por tipo ---" );
            System.out.println("Charlas: " + charlas.size());
            System.out.println("Talleres: " + talleres.size());
            System.out.println("Cursos: " + cursos.size());

            System.out.println( "\n--- Filtrado tipado (List<Charla>, List<Taller>, List<Curso>) ---" );
            System.out.println("Charlas:");
            for (Charla c : charlas) c.mostrarIdentificacion();
            System.out.println("Talleres:");
            for (Taller t : talleres) t.mostrarIdentificacion();
            System.out.println("Cursos:");
            for (Curso c : cursos) c.mostrarIdentificacion();

            System.out.println( "\n--- Costo de materiales por tipo (List<? extends Actividad>) ---" );
            System.out.println("Costo materiales charlas: $" + evento.calcularCostoMateriales(charlas));
            System.out.println("Costo materiales talleres: $" + evento.calcularCostoMateriales(talleres));
            System.out.println("Costo materiales cursos: $" + evento.calcularCostoMateriales(cursos));
        } catch (CupoExcedidoException e) {
            System.out.println( e.getMessage() );
        }
    }

    private static void ejercicio4() {
        System.out.println( "\n\n=== TP2 - EJERCICIO 4 ===" );

        System.out.println( "\n--- Creación de estudiantes, sala, evento y actividades ---" );
        Estudiante ana = new Estudiante("2001", "Ana Ríos");
        Estudiante juan = new Estudiante("2002", "Juan Medina");
        Estudiante sofia = new Estudiante("2003", "Sofía Torres");

        Sala sala = new Sala(4, "Sala de Conferencias");

        EventoUniversitario evento = new EventoUniversitario("EVT-004", "Jornada de Cierre", 0, true);
        evento.asignarSala(sala);
        evento.crearActividad("Charla de cierre", 30, "Charla", "Dra. Funes", false);
        evento.crearActividad("Taller de Cloud", 10, "Taller", null, true);

        try {
            Actividad charla = evento.getActividades().get(0);
            Actividad taller = evento.getActividades().get(1);

            System.out.println( "\n--- Inscripciones ---" );
            Inscripcion insc1 = charla.inscribir(ana);
            Inscripcion insc2 = taller.inscribir(juan);
            Inscripcion insc3 = taller.inscribir(sofia);
            System.out.println( "3 inscripciones creadas, todas en estado Pendiente." );

            System.out.println( "\n--- Confirmación de algunas inscripciones ---" );
            insc1.confirmar();
            insc2.confirmar();
            // insc3 queda a propósito sin confirmar
            System.out.println( "Se confirmaron las inscripciones de Ana y Juan. La de Sofía queda pendiente." );

            System.out.println( "\n--- Emisión de tickets (solo inscripciones confirmadas) ---" );
            insc1.emitirTicket();
            insc2.emitirTicket();
            Inscripcion.TicketDeAcceso ticketSofia = insc3.emitirTicket();
            if (ticketSofia == null) {
                System.out.println( "No se emite ticket para Sofía: su inscripción no está confirmada." );
            }

            System.out.println( "\n--- Inicio del envío concurrente de tickets ---" );
            EnvioTicketsThread hiloEnvio = new EnvioTicketsThread(evento);
            hiloEnvio.start();

            System.out.println("[" + Thread.currentThread().getName() + "] Mientras se envían los tickets en paralelo, sigo mostrando datos del evento:");
            evento.mostrarDatos();
            System.out.println("[" + Thread.currentThread().getName() + "] Estudiantes inscriptos:");
            for (Actividad actividad : evento.getActividades()) {
                actividad.mostrarInscripciones();
            }

            hiloEnvio.join();
            System.out.println( "\nEnvío de tickets finalizado, ambos flujos terminaron." );
        } catch (CupoExcedidoException e) {
            System.out.println( e.getMessage() );
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println( "El hilo principal fue interrumpido esperando el envío de tickets." );
        }
    }
}
