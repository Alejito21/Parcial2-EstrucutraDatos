package co.edu.uniquindio.poo.Model.Punto5;

import co.edu.uniquindio.poo.Model.Estructuras.Cola;

// DOCUMENTACIÓN
// Punto 5 c): árbol Rojo-Negro de palabras (orden alfabético).
//
// Propiedades que se deben cumplir:
//   1. Todo nodo es rojo o negro.
//   2. La raíz es negra.
//   3. Las hojas nulas (null) se consideran negras.
//   4. Un nodo rojo no puede tener hijos rojos (no hay dos rojos seguidos).
//   5. Todo camino desde un nodo hasta sus hojas nulas tiene la misma cantidad de nodos negros.
//
// Inserción: se inserta como en un ABB con el nodo nuevo en ROJO. Si su padre también
// es rojo se viola la propiedad 4 y se corrige mirando al TÍO (hermano del padre):
//   Caso 1: tío ROJO  -> recoloreo: padre y tío a negro, abuelo a rojo; se sigue revisando desde el abuelo.
//   Caso 2: tío NEGRO y el nodo forma un "triángulo" (es hijo derecho de un padre izquierdo
//           o viceversa) -> rotación sobre el padre para convertirlo en el caso 3.
//   Caso 3: tío NEGRO y el nodo forma una "línea" -> rotación sobre el abuelo en sentido
//           contrario, el padre pasa a negro y el abuelo a rojo.
// Al final la raíz siempre se pinta de negro.
//
// Cada paso queda en la cola 'pasos' (descripción + copia del árbol).
public class ArbolRN {

    private NodoRN raiz;
    private Cola<Paso<NodoRN>> pasos = new Cola<>();

    public NodoRN getRaiz() { return raiz; }

    public Cola<Paso<NodoRN>> getPasos() { return pasos; }

    public void limpiarPasos() { pasos = new Cola<>(); }

    private void registrar(String descripcion) {
        pasos.encolar(new Paso<>(descripcion, copiar(raiz, null)));
    }

    private static String color(NodoRN n) {
        return n == null || !n.rojo ? "negro" : "rojo";
    }

    // ===================== INSERCIÓN =====================

    // Inserta una palabra. Retorna false si ya existía.
    public boolean insertar(String palabra) {
        String clave = palabra.trim().toLowerCase();
        if (clave.isEmpty()) return false;

        // Búsqueda del lugar como en un ABB
        NodoRN padre = null;
        NodoRN actual = raiz;
        while (actual != null) {
            int cmp = clave.compareTo(actual.clave);
            if (cmp == 0) {
                registrar("Insertar \"" + clave + "\": ya existe, no se inserta.");
                return false;
            }
            padre = actual;
            actual = cmp < 0 ? actual.izquierdo : actual.derecho;
        }

        NodoRN nuevo = new NodoRN(clave);   // nace rojo
        nuevo.padre = padre;
        if (padre == null) {
            raiz = nuevo;
            raiz.rojo = false;
            registrar("Insertar \"" + clave + "\": el árbol estaba vacío, es la RAÍZ y se pinta de NEGRO.");
            return true;
        }
        boolean esIzq = clave.compareTo(padre.clave) < 0;
        if (esIzq) padre.izquierdo = nuevo; else padre.derecho = nuevo;

        String ubicacion = "Insertar \"" + clave + "\" en ROJO como hijo " + (esIzq ? "izquierdo" : "derecho")
                + " de \"" + padre.clave + "\" (" + color(padre) + ").";
        if (!padre.rojo) {
            registrar(ubicacion + " El padre es negro: no se viola ninguna propiedad.");
            return true;
        }
        registrar(ubicacion + " Padre rojo con hijo rojo: se viola la propiedad 4, hay que corregir.");
        corregirInsercion(nuevo);
        return true;
    }

    // Corrige la propiedad 4 subiendo por el árbol (ver casos arriba).
    private void corregirInsercion(NodoRN z) {
        while (z.padre != null && z.padre.rojo) {
            NodoRN p = z.padre;
            NodoRN a = p.padre;          // existe: un padre rojo nunca es la raíz
            boolean padreIzq = (p == a.izquierdo);
            NodoRN tio = padreIzq ? a.derecho : a.izquierdo;

            if (tio != null && tio.rojo) {
                // Caso 1: recoloreo
                p.rojo = false;
                tio.rojo = false;
                a.rojo = true;
                registrar("Caso 1 (tío \"" + tio.clave + "\" ROJO): RECOLOREO. Padre \"" + p.clave
                        + "\" y tío \"" + tio.clave + "\" pasan a negro; abuelo \"" + a.clave
                        + "\" pasa a rojo. Se sigue revisando desde \"" + a.clave + "\".");
                z = a;
                continue;
            }

            String nombreTio = tio == null ? "nulo (negro)" : "\"" + tio.clave + "\" negro";
            boolean zIzq = (z == p.izquierdo);
            if (padreIzq != zIzq) {
                // Caso 2: triángulo -> se rota el padre para formar una línea
                if (padreIzq) rotarIzquierda(p); else rotarDerecha(p);
                registrar("Caso 2 (tío " + nombreTio + ", \"" + z.clave + "\" forma un triángulo con \""
                        + p.clave + "\" y \"" + a.clave + "\"): ROTACIÓN SIMPLE A LA "
                        + (padreIzq ? "IZQUIERDA" : "DERECHA") + " sobre \"" + p.clave
                        + "\". Ahora \"" + p.clave + "\" queda en línea debajo de \"" + z.clave + "\".");
                z = p;               // el antiguo padre es ahora el nodo de abajo de la línea
                p = z.padre;
            }
            // Caso 3: línea -> rotación sobre el abuelo + recoloreo
            p.rojo = false;
            a.rojo = true;
            if (padreIzq) rotarDerecha(a); else rotarIzquierda(a);
            registrar("Caso 3 (tío " + nombreTio + ", línea \"" + a.clave + "\"-\"" + p.clave + "\"-\""
                    + z.clave + "\"): ROTACIÓN SIMPLE A LA " + (padreIzq ? "DERECHA" : "IZQUIERDA")
                    + " sobre el abuelo \"" + a.clave + "\". \"" + p.clave + "\" sube y pasa a negro; \""
                    + a.clave + "\" pasa a rojo.");
        }
        if (raiz.rojo) {
            raiz.rojo = false;
            registrar("La raíz \"" + raiz.clave + "\" quedó roja: se pinta de NEGRO (propiedad 2).");
        }
    }

    //        x                 y
    //       / \               / \
    //      A   y     ===>    x   C
    //         / \           / \
    //        B   C         A   B
    private void rotarIzquierda(NodoRN x) {
        NodoRN y = x.derecho;
        x.derecho = y.izquierdo;
        if (y.izquierdo != null) y.izquierdo.padre = x;
        reemplazarEnPadre(x, y);
        y.izquierdo = x;
        x.padre = y;
    }

    //          x             y
    //         / \           / \
    //        y   C   ===>  A   x
    //       / \               / \
    //      A   B             B   C
    private void rotarDerecha(NodoRN x) {
        NodoRN y = x.izquierdo;
        x.izquierdo = y.derecho;
        if (y.derecho != null) y.derecho.padre = x;
        reemplazarEnPadre(x, y);
        y.derecho = x;
        x.padre = y;
    }

    // Pone a 'y' en el lugar que ocupaba 'x' dentro de su padre (o como raíz).
    private void reemplazarEnPadre(NodoRN x, NodoRN y) {
        y.padre = x.padre;
        if (x.padre == null) raiz = y;
        else if (x == x.padre.izquierdo) x.padre.izquierdo = y;
        else x.padre.derecho = y;
    }

    // Copia profunda del árbol (con colores) para guardar los pasos.
    private static NodoRN copiar(NodoRN n, NodoRN padre) {
        if (n == null) return null;
        NodoRN c = new NodoRN(n.clave);
        c.rojo = n.rojo;
        c.padre = padre;
        c.izquierdo = copiar(n.izquierdo, c);
        c.derecho = copiar(n.derecho, c);
        return c;
    }

    // ===================== VERIFICACIÓN =====================

    // Verifica las propiedades: raíz negra, sin rojos seguidos, misma altura negra y orden ABB.
    public boolean esRojoNegro() {
        if (raiz == null) return true;
        return !raiz.rojo && alturaNegra(raiz, null, null) > 0;
    }

    // Retorna la altura negra o -1 si alguna propiedad falla.
    private int alturaNegra(NodoRN n, String min, String max) {
        if (n == null) return 1;   // hoja nula = negra
        if ((min != null && n.clave.compareTo(min) <= 0) || (max != null && n.clave.compareTo(max) >= 0)) return -1;
        if (n.rojo && ((n.izquierdo != null && n.izquierdo.rojo) || (n.derecho != null && n.derecho.rojo))) return -1;
        int hi = alturaNegra(n.izquierdo, min, n.clave);
        int hd = alturaNegra(n.derecho, n.clave, max);
        if (hi < 0 || hd < 0 || hi != hd) return -1;
        return hi + (n.rojo ? 0 : 1);
    }

    // Altura negra del árbol (nodos negros desde la raíz hasta una hoja nula, sin contar la nula).
    public int getAlturaNegra() {
        return raiz == null ? 0 : alturaNegra(raiz, null, null) - 1;
    }

    // ===================== RECORRIDOS =====================

    public Cola<String> inorden() { Cola<String> c = new Cola<>(); inorden(raiz, c); return c; }
    public Cola<String> preorden() { Cola<String> c = new Cola<>(); preorden(raiz, c); return c; }

    private void inorden(NodoRN n, Cola<String> c) {
        if (n == null) return;
        inorden(n.izquierdo, c); c.encolar(n.getEtiqueta()); inorden(n.derecho, c);
    }

    private void preorden(NodoRN n, Cola<String> c) {
        if (n == null) return;
        c.encolar(n.getEtiqueta()); preorden(n.izquierdo, c); preorden(n.derecho, c);
    }
}
