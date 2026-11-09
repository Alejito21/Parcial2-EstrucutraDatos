package co.edu.uniquindio.poo.Model.Punto4;

import co.edu.uniquindio.poo.Model.Estructuras.NodoArbol;

// DOCUMENTACIÓN
// Nodo del árbol binario de búsqueda (Punto 4). La clave es el código del equipo.
// Los campos son de paquete para que ArbolABB los manipule directamente;
// desde fuera solo se pueden leer con los getters.
// Implementa NodoArbol para poder dibujarse en la interfaz gráfica.
public class NodoABB implements NodoArbol {
    int clave;
    NodoABB izquierdo;   // claves menores
    NodoABB derecho;     // claves mayores

    NodoABB(int clave) {
        this.clave = clave;
    }

    public int getClave() { return clave; }

    @Override
    public NodoABB getIzquierdo() { return izquierdo; }

    @Override
    public NodoABB getDerecho() { return derecho; }

    @Override
    public String getEtiqueta() { return String.valueOf(clave); }
}
