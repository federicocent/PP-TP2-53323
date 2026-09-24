package hilos;

import modelo.EventoUniversitario;
import modelo.Inscripcion;
import modelo.actividades.Actividad;

public class EnvioTicketsThread extends Thread {
    private EventoUniversitario evento;

    public EnvioTicketsThread(EventoUniversitario evento) {
        super("EnvioTicketsThread");
        this.evento = evento;
    }

    @Override
    public void run() {
        System.out.println("[" + Thread.currentThread().getName() + "] Iniciando envío de tickets...");
        for (Actividad actividad : evento.getActividades()) {
            for (Inscripcion inscripcion : actividad.getInscripciones()) {
                if (inscripcion.getTicket() != null) {
                    inscripcion.getTicket().enviarTicket();
                }
            }
        }
        System.out.println("[" + Thread.currentThread().getName() + "] Envío de tickets finalizado.");
    }
}
