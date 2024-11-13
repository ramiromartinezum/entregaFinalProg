package main.java.trivia;
import java.util.List;
import java.util.Scanner;
public class TriviaJuego {
    private GestorPreguntas gestorPreguntas;
    private int puntaje;

    public TriviaJuego() {
        gestorPreguntas = new GestorPreguntas();
        puntaje = 0;
    }

    public void iniciar() {
        Scanner scanner = new Scanner(System.in);
        List<Pregunta> preguntas = gestorPreguntas.obtenerPreguntas();

        for (Pregunta pregunta : preguntas) {
            System.out.println(pregunta.getEnunciado());
            String[] opciones = pregunta.getOpciones();
            for (int i = 0; i < opciones.length; i++) {
                System.out.println((i + 1) + ". " + opciones[i]);
            }
            System.out.print("Seleccione el número de la respuesta: ");
            int respuestaIndice = scanner.nextInt() - 1;

            // Verifica si el índice es válido y luego obtiene el texto de la opción seleccionada
            if (respuestaIndice >= 0 && respuestaIndice < opciones.length) {
                String respuestaUsuario = opciones[respuestaIndice];
                if (pregunta.esRespuestaCorrecta(respuestaUsuario)) {
                    puntaje++;
                    System.out.println("¡Correcto!");
                } else {
                    System.out.println("Incorrecto.");
                }
            } else {
                System.out.println("Respuesta no válida.");
            }
        }

        System.out.println("Tu puntaje final es: " + puntaje);
        scanner.close();
    }
}
