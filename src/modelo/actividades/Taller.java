package modelo.actividades;

import certificacion.Certificable;
import modelo.Estudiante;

import java.io.Serializable;

public class Taller extends Actividad implements Serializable, Certificable {
    private boolean requiereNotebook;

    public Taller(String titulo, int cupoMaximo, boolean requiereNotebook) {
        super(titulo, cupoMaximo);
        this.requiereNotebook = requiereNotebook;
    }

    @Override
    public double calcularCostoMateriales() {
        return requiereNotebook ? 5000 : 2000;
    }

    @Override
    public String getTipo() { return "Taller"; }

    @Override
    public String generarCertificado(Estudiante estudiante) {
        return "Certificado de asistencia - " + ENTIDAD_EMISORA + " - Taller: " + getTitulo() +
                " - Alumno: " + estudiante.getNombre();
    }
}
