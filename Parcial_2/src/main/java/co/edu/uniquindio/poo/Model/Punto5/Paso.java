package co.edu.uniquindio.poo.Model.Punto5;

import co.edu.uniquindio.poo.Model.Estructuras.DibujoArbol;
import co.edu.uniquindio.poo.Model.Estructuras.NodoArbol;

// DOCUMENTACIÓN
// Un paso del proceso de construcción/modificación de un árbol (AVL o Rojo-Negro):
// una descripción de lo que se hizo y una COPIA del árbol en ese momento
// (copia para que los cambios posteriores no la alteren).
public class Paso<N extends NodoArbol> {
    private final String descripcion;
    private final N arbol;

    public Paso(String descripcion, N arbol) {
        this.descripcion = descripcion;
        this.arbol = arbol;
    }

    public String getDescripcion() { return descripcion; }
    public N getArbol() { return arbol; }

    @Override
    public String toString() {
        return descripcion + "\n" + DibujoArbol.texto(arbol);
    }
}
