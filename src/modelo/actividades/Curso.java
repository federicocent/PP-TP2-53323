package modelo.actividades;

import certificacion.Certificable;
import modelo.Estudiante;

import java.io.Serializable;

public class Curso extends Actividad implements Serializable, Certificable {
    private int nivel;

    public Curso(String titulo, int cupoMaximo, int nivel) {
        super(titulo, cupoMaximo);
        this.nivel = nivel;
    }

    @Override
    public double calcularCostoMateriales() {
        return 1500 * nivel;
    }

    @Override
    public String getTipo() { return "Curso"; }

    @Override
    public String generarCertificado(Estudiante estudiante) {
        return "Certificado de asistencia - " + ENTIDAD_EMISORA + " - Curso: " + getTitulo() +
                " (Nivel " + nivel + ") - Alumno: " + estudiante.getNombre();
    }
}
