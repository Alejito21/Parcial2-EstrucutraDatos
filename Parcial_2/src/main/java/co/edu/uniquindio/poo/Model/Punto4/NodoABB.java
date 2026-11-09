package co.edu.uniquindio.poo.Model.Punto4;

// DOCUMENTACIÓN
// Nodo del árbol binario de búsqueda (Punto 4). La clave es el código del equipo.
// Los campos son de paquete para que ArbolABB los manipule directamente;
// desde fuera solo se pueden leer con los getters.
public class NodoABB {
    int clave;
    NodoABB izquierdo;   // claves menores
    NodoABB derecho;     // claves mayores

    NodoABB(int clave) {
        this.clave = clave;
    }

    public int getClave() { return clave; }
    public NodoABB getIzquierdo() { return izquierdo; }
    public NodoABB getDerecho() { return derecho; }
}
