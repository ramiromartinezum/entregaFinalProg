package main.trivia;

public class Pregunta {
    private String enunciado;
    private String[] opciones;
    private String respuestaCorrecta;  // Cambiamos a String

    public Pregunta(String enunciado, String[] opciones, String respuestaCorrecta) {
        this.enunciado = enunciado;
        this.opciones = opciones;
        this.respuestaCorrecta = respuestaCorrecta;
    }

    public String getEnunciado() {
        return enunciado;
    }

    public String[] getOpciones() {
        return opciones;
    }

    public boolean esRespuestaCorrecta(String respuestaUsuario) {
        return respuestaUsuario.equalsIgnoreCase(respuestaCorrecta);
    }
}
