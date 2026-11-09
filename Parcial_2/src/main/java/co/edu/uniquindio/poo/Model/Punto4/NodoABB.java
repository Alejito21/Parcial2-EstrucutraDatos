package co.edu.uniquindio.poo.Model.Punto4;

public class NodoABB {
    int clave;
    NodoABB izquierdo;
    NodoABB derecho;

    NodoABB(int clave) {
        this.clave = clave;
    }

    public int getClave() { return clave; }
    public NodoABB getIzquierdo() { return izquierdo; }
    public NodoABB getDerecho() { return derecho; }
}
