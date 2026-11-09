package co.edu.uniquindio.poo.Model.Estructuras;

// DOCUMENTACIÓN
// Interfaz común para los nodos de árboles binarios (AVL, Rojo-Negro, ...).
// Permite que un mismo código (DibujoArbol, la interfaz gráfica) dibuje
// cualquier árbol sin conocer su tipo concreto.
public interface NodoArbol {

    // Texto que se muestra para el nodo al dibujarlo (ej: "cine (FE=+1)" o "cine [N]").
    String getEtiqueta();

    NodoArbol getIzquierdo();

    NodoArbol getDerecho();
}
