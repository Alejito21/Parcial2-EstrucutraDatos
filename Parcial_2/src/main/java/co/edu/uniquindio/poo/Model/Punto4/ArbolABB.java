package co.edu.uniquindio.poo.Model.Punto4;

import co.edu.uniquindio.poo.Model.Estructuras.Cola;

public class ArbolABB {

    private NodoABB raiz;

    public NodoABB getRaiz() { return raiz; }


    public boolean insertar(int clave) {
        if (raiz == null) {
            raiz = new NodoABB(clave);
            return true;
        }
        NodoABB actual = raiz;
        while (true) {
            if (clave == actual.clave) return false;
            if (clave < actual.clave) {
                if (actual.izquierdo == null) { actual.izquierdo = new NodoABB(clave); return true; }
                actual = actual.izquierdo;
            } else {
                if (actual.derecho == null) { actual.derecho = new NodoABB(clave); return true; }
                actual = actual.derecho;
            }
        }
    }

    // ---------- a) consulta por rango ----------


    public static int contarEquiposEnRango(NodoABB nodo, int inferior, int superior) {
        if (nodo == null || inferior > superior) return 0;
        if (nodo.clave < inferior)
            return contarEquiposEnRango(nodo.derecho, inferior, superior);
        if (nodo.clave > superior)
            return contarEquiposEnRango(nodo.izquierdo, inferior, superior);
        return 1 + contarEquiposEnRango(nodo.izquierdo, inferior, superior)
                + contarEquiposEnRango(nodo.derecho, inferior, superior);
    }

    // ---------- b) eliminación ----------


    public boolean eliminar(int clave) {
        boolean[] eliminado = new boolean[1];
        raiz = eliminar(raiz, clave, eliminado);
        return eliminado[0];
    }

    private NodoABB eliminar(NodoABB nodo, int clave, boolean[] eliminado) {
        if (nodo == null) return null;
        if (clave < nodo.clave) {
            nodo.izquierdo = eliminar(nodo.izquierdo, clave, eliminado);
        } else if (clave > nodo.clave) {
            nodo.derecho = eliminar(nodo.derecho, clave, eliminado);
        } else {
            eliminado[0] = true;
            if (nodo.izquierdo == null) return nodo.derecho;
            if (nodo.derecho == null) return nodo.izquierdo;
            NodoABB predecesor = nodo.izquierdo;
            while (predecesor.derecho != null) predecesor = predecesor.derecho;
            nodo.clave = predecesor.clave;
            nodo.izquierdo = eliminar(nodo.izquierdo, predecesor.clave, new boolean[1]);
        }
        return nodo;
    }

    //

    public String inorden() { StringBuilder sb = new StringBuilder(); inorden(raiz, sb); return sb.toString().trim(); }
    public String preorden() { StringBuilder sb = new StringBuilder(); preorden(raiz, sb); return sb.toString().trim(); }
    public String postorden() { StringBuilder sb = new StringBuilder(); postorden(raiz, sb); return sb.toString().trim(); }

    private void inorden(NodoABB n, StringBuilder sb) {
        if (n == null) return;
        inorden(n.izquierdo, sb); sb.append(n.clave).append(' '); inorden(n.derecho, sb);
    }

    private void preorden(NodoABB n, StringBuilder sb) {
        if (n == null) return;
        sb.append(n.clave).append(' '); preorden(n.izquierdo, sb); preorden(n.derecho, sb);
    }

    private void postorden(NodoABB n, StringBuilder sb) {
        if (n == null) return;
        postorden(n.izquierdo, sb); postorden(n.derecho, sb); sb.append(n.clave).append(' ');
    }


    public String amplitud() {
        StringBuilder sb = new StringBuilder();
        if (raiz == null) return "";
        Cola<NodoABB> cola = new Cola<>();
        cola.encolar(raiz);
        while (!cola.estaVacia()) {
            NodoABB n = cola.desencolar();
            sb.append(n.clave).append(' ');
            if (n.izquierdo != null) cola.encolar(n.izquierdo);
            if (n.derecho != null) cola.encolar(n.derecho);
        }
        return sb.toString().trim();
    }


    public void imprimir() {
        imprimir(raiz, 0);
    }

    private void imprimir(NodoABB n, int nivel) {
        if (n == null) return;
        imprimir(n.derecho, nivel + 1);
        System.out.println("    ".repeat(nivel) + n.clave);
        imprimir(n.izquierdo, nivel + 1);
    }
}
