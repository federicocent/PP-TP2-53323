package modelo;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

public class Inscripcion implements Serializable {
    private Estudiante estudiante;
    private LocalDate fecha;
    private String estado;
    private TicketDeAcceso ticket;

    public Inscripcion(Estudiante estudiante, LocalDate fecha, String estado) {
        this.estudiante = estudiante;
        this.fecha = fecha;
        this.estado = estado;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public String getEstado() {
        return estado;
    }

    public boolean estaConfirmada() {
        return "Confirmada".equals(estado);
    }

    public void confirmar() {
        this.estado = "Confirmada";
    }

    public TicketDeAcceso getTicket() {
        return ticket;
    }

    public TicketDeAcceso emitirTicket() {
        if (!estaConfirmada()) {
            return null;
        }
        this.ticket = new TicketDeAcceso();
        return this.ticket;
    }

    @Override
    public String toString() {
        return "  * " + estudiante.getNombre() + " (Legajo: " + estudiante.getLegajo() +
                ") - Fecha: " + fecha + " - Estado: " + estado;
    }

    // Clase anidada miembro: un ticket solo tiene sentido dentro del contexto de una inscripción concreta.
    public class TicketDeAcceso implements Serializable {
        private final String idTicket;
        private final LocalDate fechaEmision;

        private TicketDeAcceso() {
            this.idTicket = "TCK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            this.fechaEmision = LocalDate.now();
        }

        public void enviarTicket() {
            System.out.println("  Enviando ticket " + idTicket + " a " + estudiante.getNombre() + "...");
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("  Ticket " + idTicket + " enviado a " + estudiante.getNombre() + " (emitido " + fechaEmision + ").");
        }

        @Override
        public String toString() {
            return "Ticket #" + idTicket + " (emitido " + fechaEmision + ")";
        }
    }
}
