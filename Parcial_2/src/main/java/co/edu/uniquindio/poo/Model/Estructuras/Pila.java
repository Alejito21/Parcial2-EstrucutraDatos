package co.edu.uniquindio.poo.Model.Estructuras;

// DOCUMENTACIÓN
// Pila genérica propia (LIFO: el último en entrar es el primero en salir),
// implementada con nodos enlazados. La "cima" apunta al último elemento apilado.
// Todas las operaciones básicas son O(1).
public class Pila<T> {

    // Nodo interno: guarda el dato y la referencia al nodo que está debajo.
    private static class Nodo<T> {
        T dato;
        Nodo<T> siguiente;
        Nodo(T dato, Nodo<T> siguiente) {
            this.dato = dato;
            this.siguiente = siguiente;
        }
    }

    private Nodo<T> cima;   // elemento del tope
    private int tamanio;    // cantidad de elementos

    // Coloca un elemento en la cima.
    public void apilar(T dato) {
        cima = new Nodo<>(dato, cima);
        tamanio++;
    }

    // Quita y retorna el elemento de la cima. Lanza excepción si la pila está vacía.
    public T desapilar() {
        if (cima == null) throw new IllegalStateException("La pila está vacía");
        T dato = cima.dato;
        cima = cima.siguiente;
        tamanio--;
        return dato;
    }

    // Retorna el elemento de la cima sin quitarlo.
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

    // Muestra el contenido desde el tope hasta el fondo, separado por " → ".
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
