package co.edu.uniquindio.poo.Model.Estructuras;

public class Pila<T> {
    private static class Nodo<T> {
        T dato;
        Nodo<T> siguiente;
        Nodo(T dato, Nodo<T> siguiente) {
            this.dato = dato;
            this.siguiente = siguiente;
        }
    }

    private Nodo<T> cima;
    private int tamanio;

    public void apilar(T dato) {
        cima = new Nodo<>(dato, cima);
        tamanio++;
    }

    public T desapilar() {
        if (cima == null) throw new IllegalStateException("La pila está vacía");
        T dato = cima.dato;
        cima = cima.siguiente;
        tamanio--;
        return dato;
    }

    public T tope() {
        if (cima == null) throw new IllegalStateException("La pila está vacía");
        return cima.dato;
    }

    public boolean estaVacia() {
        return cima == null;
    }

    public int tamanio() {
        return tamanio;
    }

    /** Muestra el contenido desde el tope hasta el fondo. */
    @Override
    public String toString() {
        if (cima == null) return "[vacía]";
        StringBuilder sb = new StringBuilder();
        Nodo<T> actual = cima;
        while (actual != null) {
            sb.append(actual.dato);
            if (actual.siguiente != null) sb.append(" → ");
            actual = actual.siguiente;
        }
        return sb.toString();
    }
}
