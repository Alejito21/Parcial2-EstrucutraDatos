package co.edu.uniquindio.poo.Model.Estructuras;

// DOCUMENTACIÓN
// Pila genérica propia (LIFO: el último en entrar es el primero en salir),
// implementada con nodos enlazados. La "cima" apunta al último elemento apilado.
// Todas las operaciones básicas son O(1).
// Implementa Iterable para poder recorrerla (tope -> fondo) sin desapilar;
// se usa en la interfaz gráfica para mostrar su contenido.
public class Pila<T> implements Iterable<T> {

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

    // Recorre los elementos desde el tope hasta el fondo, sin sacarlos de la pila.
    @Override
    public java.util.Iterator<T> iterator() {
        return new java.util.Iterator<T>() {
            private Nodo<T> actual = cima;

            @Override
            public boolean hasNext() {
                return actual != null;
            }

            @Override
            public T next() {
                if (actual == null) throw new java.util.NoSuchElementException();
                T dato = actual.dato;
                actual = actual.siguiente;
                return dato;
            }
        };
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
