package modelo.actividades;
import excepciones.CupoExcedidoException;
import modelo.Inscripcion;
import modelo.Estudiante;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public abstract class Actividad implements Serializable {
    public static final int CUPO_MINIMO = 1;

    private static int contadorId = 1;

    private int id;
    private String titulo;
    private int cupoMaximo;
    private List<Inscripcion> inscripciones;

    public Actividad(String titulo, int cupoMaximo) {
        this.id = contadorId++;
        this.titulo = titulo;
        this.cupoMaximo = cupoMaximo;
        this.inscripciones = new ArrayList<>();
    }

    public Inscripcion inscribir(Estudiante estudiante) throws CupoExcedidoException {
        int inscripcionesLength = inscripciones.size();

        if (inscripcionesLength >= cupoMaximo) {
            throw new CupoExcedidoException("Cupo lleno (" + cupoMaximo + "/" +cupoMaximo + ")" + ". No fue posible realizar la inscripción a " + titulo + ".");
        }

        Inscripcion inscripcion = new Inscripcion(estudiante, LocalDate.now(), "Pendiente");
        inscripciones.add(inscripcion);
        return inscripcion;
    }

    public void mostrarInscripciones() {
        for (Inscripcion inscripcion: inscripciones) {
            System.out.println(inscripcion);
        }
    }

    public String getTitulo() {
        return titulo;
    }

    public List<Inscripcion> getInscripciones() {
        return inscripciones;
    }

    public final void mostrarIdentificacion() {
        System.out.println("  - #" + id + " [" + getTipo() + "] " + titulo +
                " (cupo máximo: " + cupoMaximo + ", inscriptos: " + inscripciones.size() + ")");
    }

    public abstract double calcularCostoMateriales();
    public abstract String getTipo();

    @Override
    public String toString() {
        return "  - #" + id + ": " + titulo +
                " (cupo máximo: " + cupoMaximo + ", inscriptos: " + inscripciones.size() + ")";
    }
}
