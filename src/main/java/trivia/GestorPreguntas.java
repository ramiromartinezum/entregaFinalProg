package main.java.trivia;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
public class GestorPreguntas {
    private List<Pregunta> preguntas;

    public GestorPreguntas() {
        preguntas = new ArrayList<>();
        cargarPreguntas("trivia_questions.csv");
    }

    private void cargarPreguntas(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isFirstLine = true;
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                String[] parts = line.split(",");
                if (parts.length >= 5) {
                    String enunciado = parts[0].trim();
                    String[] opciones = { parts[1].trim(), parts[2].trim(), parts[3].trim() };
                    String respuestaCorrecta = parts[4].trim().replace("\"", "");  // Elimina comillas

                    Pregunta pregunta = new Pregunta(enunciado, opciones, respuestaCorrecta);
                    preguntas.add(pregunta);
                }
            }
        } catch (IOException e) {
            System.err.println("Error al cargar preguntas: " + e.getMessage());
        }
    }


    public List<Pregunta> obtenerPreguntas() {
        return preguntas;
    }
}
