package co.edu.uniquindio.poo.Model.Estructuras;

// DOCUMENTACIÓN
// Cola genérica propia (FIFO: el primero en entrar es el primero en salir),
// implementada con nodos enlazados. Se guarda referencia al frente (por donde
// se sale) y al fin (por donde se entra) para que encolar y desencolar sean O(1).
public class Cola<T> {

    // Nodo interno: guarda el dato y la referencia al siguiente en la fila.
    private static class Nodo<T> {
        T dato;
        Nodo<T> siguiente;
        Nodo(T dato) {
            this.dato = dato;
        }
    }

    private Nodo<T> frente;  // primer elemento (el próximo en salir)
    private Nodo<T> fin;     // último elemento (el último en llegar)
    private int tamanio;     // cantidad de elementos

    // Agrega un elemento al final de la cola.
    public void encolar(T dato) {
        Nodo<T> nuevo = new Nodo<>(dato);
        if (fin == null) {
            frente = nuevo;          // cola vacía: el nuevo es frente y fin
        } else {
            fin.siguiente = nuevo;
        }
        fin = nuevo;
        tamanio++;
    }

    // Quita y retorna el elemento del frente. Lanza excepción si la cola está vacía.
    public T desencolar() {
        if (frente == null) throw new IllegalStateException("La cola está vacía");
        T dato = frente.dato;
        frente = frente.siguiente;
        if (frente == null) fin = null;   // quedó vacía
        tamanio--;
        return dato;
    }

    // Retorna el elemento del frente sin quitarlo.
    public T frente() {
        if (frente == null) throw new IllegalStateException("La cola está vacía");
        return frente.dato;
    }

    public boolean estaVacia() {
        return frente == null;
    }

    public int tamanio() {
        return tamanio;
    }

    // Muestra el contenido desde el frente hasta el final, separado por comas.
    @Override
    public String toString() {
        if (frente == null) return "[vacía]";
        StringBuilder sb = new StringBuilder();
        Nodo<T> actual = frente;
        while (actual != null) {
            sb.append(actual.dato);
            if (actual.siguiente != null) sb.append(", ");
            actual = actual.siguiente;
        }
        return sb.toString();
    }
}
