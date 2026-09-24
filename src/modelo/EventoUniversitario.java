package modelo;

import modelo.actividades.Actividad;
import modelo.actividades.Charla;
import modelo.actividades.Curso;
import modelo.actividades.Taller;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class EventoUniversitario implements Serializable {
    private final String Id;
    private String titulo;
    private double costoBase;
    private boolean gratuito;
    private Sala sala;
    private List<Actividad> actividades;

    private static int cantidadEventos = 0;

    public EventoUniversitario(String Id, String titulo, double costoBase, boolean gratuito){
        this.Id = Id;
        this.titulo = titulo;
        this.costoBase = costoBase;
        this.gratuito = gratuito;
        cantidadEventos++;
        this.actividades = new ArrayList<>();
    }

    public EventoUniversitario(EventoUniversitario copiaEvento) {
        this(copiaEvento.Id + "-COPIA", copiaEvento.titulo, copiaEvento.costoBase, copiaEvento.gratuito);
        cantidadEventos--; // una copia no es un evento nuevo, no debe sumar al contador
        this.actividades = new ArrayList<>(copiaEvento.actividades);
        this.sala = copiaEvento.sala;
    }

    public static int getCantidadEventos() {
        return cantidadEventos;
    }

    public double calcularCostoEstimado() {
        if (gratuito) return 0;
        double totalActividades = 0;
        for (Actividad a : actividades) {
            totalActividades += a.calcularCostoMateriales();
        }
        return (costoBase + totalActividades) * 1.21;
    }

    public void asignarSala(Sala sala) {
        this.sala = sala;
    }

    public void crearActividad(String titulo, int cupo, String tipo, String disertanteOrNull, boolean requiereNotebook) {
        crearActividad(titulo, cupo, tipo, disertanteOrNull, requiereNotebook, 0);
    }

    public void crearActividad(String titulo, int cupo, String tipo, String disertanteOrNull, boolean requiereNotebook, int nivel) {
        Actividad actividad;
        if (tipo.equalsIgnoreCase("Charla")) {
            actividad = new Charla(titulo, cupo, disertanteOrNull);
        } else if (tipo.equalsIgnoreCase("Taller")) {
            actividad = new Taller(titulo, cupo, requiereNotebook);
        } else if (tipo.equalsIgnoreCase("Curso")) {
            actividad = new Curso(titulo, cupo, nivel);
        } else {
            throw new IllegalArgumentException("Tipo de actividad desconocido: " + tipo);
        }
        actividades.add(actividad);
    }

    public <T extends Actividad> List<T> filtrarActividadesPorTipo(Class<T> tipo) {
        List<T> resultado = new ArrayList<>();
        for (Actividad actividad : actividades) {
            if (tipo.isInstance(actividad)) {
                resultado.add(tipo.cast(actividad));
            }
        }
        return resultado;
    }

    public double calcularCostoMateriales(List<? extends Actividad> actividades) {
        double total = 0;
        for (Actividad actividad : actividades) {
            total += actividad.calcularCostoMateriales();
        }
        return total;
    }

    public void mostrarActividades() {
        for (Actividad actividad: actividades) {
            actividad.mostrarIdentificacion();
        }
    }

    public void mostrarDatos() {
        System.out.println(this);
        System.out.println("Costo estimado: $" + calcularCostoEstimado());
        if (!actividades.isEmpty()) {
            System.out.println("Actividades:");
            mostrarActividades();
        }
    }

    // Usado solamente para poblar
    public List<Actividad> getActividades() {
        return actividades;
    }

    public String getEventoId() {
        return this.Id;
    }

    public boolean persistirEvento() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(Id+".dat"))) {
            oos.writeObject(this);
            return true;
        } catch (FileNotFoundException e) {
            System.out.println("No se pudo crear: " + e.getMessage());
            return false;
        } catch (IOException e) {
            System.out.println("No se pudo guardar: " + e.getMessage());
            return false;
        }
    }

    public EventoUniversitario recuperarEvento(String id) {
        try (ObjectInput ois = new ObjectInputStream(new FileInputStream(id+".dat"))) {
            return (EventoUniversitario) ois.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("No se encontró el archivo: " + e.getMessage());
            return null;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("No se pudo leer: " + e.getMessage());
            return null;
        }
    }

    @Override
    public String toString() {
        return "Evento [ID: " + Id +
                ", Título: " + titulo +
                ", Costo: " + costoBase +
                ", Gratuito: " + (gratuito ? "Si" : "No") +
                ", Sala: " + (sala != null ? sala.getNombre() : "Sin asignar") + "]";
    }

}


