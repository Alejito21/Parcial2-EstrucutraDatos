package co.edu.uniquindio.poo.Model.Punto5;

import co.edu.uniquindio.poo.Model.Estructuras.NodoArbol;

// DOCUMENTACIÓN
// Nodo del árbol AVL (Punto 5). La clave es una palabra (orden alfabético).
// 'altura' = número de niveles del subárbol que empieza en este nodo (una hoja tiene 1).
// Factor de equilibrio: FE = altura(subárbol derecho) - altura(subárbol izquierdo).
//   FE = -1, 0 o +1 -> equilibrado.  FE = -2 o +2 -> desequilibrado (hay que rotar).
public class NodoAVL implements NodoArbol {
    String clave;
    NodoAVL izquierdo;
    NodoAVL derecho;
    int altura = 1;

    NodoAVL(String clave) {
        this.clave = clave;
    }

    // Altura de un subárbol; el subárbol vacío tiene altura 0.
    static int altura(NodoAVL n) {
        return n == null ? 0 : n.altura;
    }

    // FE = altura derecha - altura izquierda.
    public int getFE() {
        return altura(derecho) - altura(izquierdo);
    }

    // FE con signo: "+1", "0", "-2", ...
    public String getFETexto() {
        int fe = getFE();
        return fe > 0 ? "+" + fe : String.valueOf(fe);
    }

    @Override
    public String getEtiqueta() {
        return clave + " (FE=" + getFETexto() + ")";
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    @Override
    public NodoAVL getIzquierdo() {
        return izquierdo;
    }

    public void setIzquierdo(NodoAVL izquierdo) {
        this.izquierdo = izquierdo;
    }

    @Override
    public NodoAVL getDerecho() {
        return derecho;
    }

    public void setDerecho(NodoAVL derecho) {
        this.derecho = derecho;
    }

    public int getAltura() {
        return altura;
    }

    public void setAltura(int altura) {
        this.altura = altura;
    }
}
