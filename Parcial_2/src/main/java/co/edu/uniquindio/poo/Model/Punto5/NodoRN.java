package co.edu.uniquindio.poo.Model.Punto5;

// DOCUMENTACIÓN
// Nodo del árbol Rojo-Negro (Punto 5c). Todo nodo nuevo nace ROJO.
// Guarda referencia al padre porque el balanceo necesita subir por el árbol
// (consultar padre, abuelo y tío).
public class NodoRN {
    String clave;
    boolean rojo = true;
    NodoRN izquierdo, derecho, padre;

    NodoRN(String clave) {
        this.clave = clave;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public boolean isRojo() {
        return rojo;
    }

    public void setRojo(boolean rojo) {
        this.rojo = rojo;
    }

    public NodoRN getIzquierdo() {
        return izquierdo;
    }

    public void setIzquierdo(NodoRN izquierdo) {
        this.izquierdo = izquierdo;
    }

    public NodoRN getDerecho() {
        return derecho;
    }

    public void setDerecho(NodoRN derecho) {
        this.derecho = derecho;
    }

    public NodoRN getPadre() {
        return padre;
    }

    public void setPadre(NodoRN padre) {
        this.padre = padre;
    }
}
