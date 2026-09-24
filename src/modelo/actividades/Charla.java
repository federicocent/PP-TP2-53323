package modelo.actividades;

import java.io.Serializable;

public class Charla extends Actividad implements Serializable {
    private String disertante;

    public Charla(String titulo, int cupoMaximo, String disertante) {
        super(titulo, cupoMaximo);
        this.disertante = disertante;
    }

    @Override
    public double calcularCostoMateriales() { return 0; } // las charlas son gratis

    @Override
    public String getTipo() { return "Charla"; }
}