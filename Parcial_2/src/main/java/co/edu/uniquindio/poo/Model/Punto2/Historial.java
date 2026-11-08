package co.edu.uniquindio.poo.Model.Punto2;

import co.edu.uniquindio.poo.Model.Estructuras.Pila;

public class Historial {
    public static <T> Pila<T> obtenerSegmento(Pila<T> original, int inicio, int fin) {
        Pila<T> resultado = new Pila<>();
        if (inicio < 1 || inicio > fin || fin > original.tamanio()) return resultado;


        Pila<T> aux = new Pila<>();
        for (int i = 0; i < fin; i++) aux.apilar(original.desapilar());

        int posicion = fin;
        while (!aux.estaVacia()) {
            T elemento = aux.desapilar();
            original.apilar(elemento);
            if (posicion >= inicio) resultado.apilar(elemento);
            posicion--;
        }
        return resultado;
    }
}
