package co.edu.uniquindio.poo.Model.Punto2;

import co.edu.uniquindio.poo.Model.Estructuras.Pila;

// DOCUMENTACIÓN
// Punto 2: obtener un segmento del historial de acciones de un editor.
public class Historial {

    // Retorna una NUEVA pila con las acciones entre las posiciones 'inicio' y 'fin'
    // (ambas incluidas, contadas desde la cima empezando en 1). La acción de la
    // posición 'inicio' queda en la cima del resultado.
    // La pila original queda con los mismos elementos y en el mismo orden.
    // Si inicio < 1, inicio > fin o fin > tamaño, retorna una pila vacía.
    public static <T> Pila<T> obtenerSegmento(Pila<T> original, int inicio, int fin) {
        Pila<T> resultado = new Pila<>();
        if (inicio < 1 || inicio > fin || fin > original.tamanio()) return resultado;

        // 1. Se sacan las 'fin' primeras acciones a una auxiliar.
        //    En 'aux' quedan invertidas: la de la posición 'fin' queda en su cima.
        Pila<T> aux = new Pila<>();
        for (int i = 0; i < fin; i++) aux.apilar(original.desapilar());

        // 2. Se devuelven a la original (recupera su orden) y, de paso, las que están
        //    dentro del rango se apilan en el resultado. Se recorre desde 'fin' hacia 1,
        //    así la de la posición 'inicio' es la última en apilarse y queda en la cima.
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
