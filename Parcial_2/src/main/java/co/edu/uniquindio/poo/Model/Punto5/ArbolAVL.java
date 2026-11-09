package co.edu.uniquindio.poo.Model.Punto5;

import co.edu.uniquindio.poo.Model.Estructuras.Cola;

// DOCUMENTACIÓN
// Punto 5 a) y b): árbol AVL de palabras ordenadas alfabéticamente.
//
// Factor de equilibrio: FE = altura(derecho) - altura(izquierdo).
//
// Cada operación se hace en DOS FASES para poder mostrar el árbol antes de rotar:
//   1. Se inserta / elimina como en un ABB normal (actualizando alturas) y se registra
//      el árbol con todos sus FE (aquí se ve si algún nodo quedó en +2 o -2).
//   2. Se rebalancea recorriendo el árbol de abajo hacia arriba (postorden): el primer
//      nodo desequilibrado que se encuentra es el más profundo, igual que en el
//      algoritmo clásico. Cada rotación se registra con el árbol resultante.
//
// Casos de rotación:
//   FE = -2 y FE(hijo izq) <= 0  -> caso II: rotación simple a la DERECHA.
//   FE = -2 y FE(hijo izq) = +1  -> caso ID: rotación doble (izquierda sobre el hijo, derecha sobre el nodo).
//   FE = +2 y FE(hijo der) >= 0  -> caso DD: rotación simple a la IZQUIERDA.
//   FE = +2 y FE(hijo der) = -1  -> caso DI: rotación doble (derecha sobre el hijo, izquierda sobre el nodo).
//
// Todos los pasos quedan en la cola 'pasos' (descripción + copia del árbol).
public class ArbolAVL {

    private NodoAVL raiz;
    private Cola<Paso<NodoAVL>> pasos = new Cola<>();

    // Descripción de una rotación cuyo subárbol resultante todavía no ha sido
    // enlazado a su padre; se registra cuando el padre lo enlaza (ver rebalancear).
    private String rotacionPendiente;

    public NodoAVL getRaiz() { return raiz; }

    public Cola<Paso<NodoAVL>> getPasos() { return pasos; }

    // Borra el historial de pasos (el árbol no cambia).
    public void limpiarPasos() { pasos = new Cola<>(); }

    // Las palabras se comparan en minúscula y sin espacios alrededor.
    private static String normalizar(String palabra) {
        return palabra.trim().toLowerCase();
    }

    private void registrar(String descripcion) {
        pasos.encolar(new Paso<>(descripcion, copiar(raiz)));
    }

    // ===================== INSERCIÓN =====================

    // Inserta una palabra y la balancea. Retorna false si ya existía.
    public boolean insertar(String palabra) {
        String clave = normalizar(palabra);
        if (clave.isEmpty()) return false;
        if (buscar(clave)) {
            registrar("Insertar \"" + clave + "\": ya existe, no se inserta.");
            return false;
        }
        // Fase 1: inserción como ABB
        raiz = insertarABB(raiz, clave);
        NodoAVL desbalanceado = buscarDesbalanceado(raiz);
        if (desbalanceado == null) {
            registrar("Insertar \"" + clave + "\": se ubica por orden alfabético. "
                    + "Todos los FE están entre -1 y +1, el árbol queda equilibrado.");
            return true;
        }
        registrar("Insertar \"" + clave + "\": se ubica por orden alfabético. "
                + describirDesequilibrio(desbalanceado));
        // Fase 2: rotaciones
        balancearArbol();
        return true;
    }

    private NodoAVL insertarABB(NodoAVL n, String clave) {
        if (n == null) return new NodoAVL(clave);
        if (clave.compareTo(n.clave) < 0) n.izquierdo = insertarABB(n.izquierdo, clave);
        else n.derecho = insertarABB(n.derecho, clave);
        actualizarAltura(n);
        return n;
    }

    // ===================== ELIMINACIÓN =====================

    // Elimina una palabra. Si el nodo tiene dos hijos se reemplaza por el MAYOR
    // de su subárbol izquierdo (predecesor inorden). Retorna false si no existe.
    public boolean eliminar(String palabra) {
        String clave = normalizar(palabra);
        NodoAVL nodo = buscarNodo(raiz, clave);
        if (nodo == null) {
            registrar("Eliminar \"" + clave + "\": no existe en el árbol.");
            return false;
        }
        // Explicación de cómo se elimina según el número de hijos
        String como;
        if (nodo.izquierdo == null && nodo.derecho == null) {
            como = "es una hoja, se quita directamente.";
        } else if (nodo.izquierdo == null || nodo.derecho == null) {
            String hijo = (nodo.izquierdo != null ? nodo.izquierdo : nodo.derecho).clave;
            como = "tiene un solo hijo (\"" + hijo + "\"), que ocupa su lugar.";
        } else {
            NodoAVL pred = nodo.izquierdo;
            while (pred.derecho != null) pred = pred.derecho;
            como = "tiene dos hijos. Se busca el MAYOR de su subárbol izquierdo (bajando por "
                    + "\"" + nodo.izquierdo.clave + "\" y luego todo a la derecha): \"" + pred.clave
                    + "\". \"" + pred.clave + "\" toma el lugar de \"" + clave
                    + "\" y se elimina de su posición original"
                    + (pred.izquierdo != null ? " (su hijo izquierdo \"" + pred.izquierdo.clave + "\" sube a ocupar su lugar)." : " (era una hoja).");
        }
        // Fase 1: eliminación como ABB
        raiz = eliminarABB(raiz, clave);
        NodoAVL desbalanceado = buscarDesbalanceado(raiz);
        registrar("Eliminar \"" + clave + "\": " + como + " "
                + (desbalanceado == null
                    ? "Se recalculan los FE: todos están entre -1 y +1, no se necesita rotación."
                    : "Se recalculan los FE: " + describirDesequilibrio(desbalanceado)));
        // Fase 2: rotaciones (en una eliminación puede haber más de una)
        if (desbalanceado != null) balancearArbol();
        return true;
    }

    private NodoAVL eliminarABB(NodoAVL n, String clave) {
        if (n == null) return null;
        int cmp = clave.compareTo(n.clave);
        if (cmp < 0) {
            n.izquierdo = eliminarABB(n.izquierdo, clave);
        } else if (cmp > 0) {
            n.derecho = eliminarABB(n.derecho, clave);
        } else {
            if (n.izquierdo == null) return n.derecho;
            if (n.derecho == null) return n.izquierdo;
            NodoAVL pred = n.izquierdo;                       // mayor del subárbol izquierdo
            while (pred.derecho != null) pred = pred.derecho;
            n.clave = pred.clave;
            n.izquierdo = eliminarABB(n.izquierdo, pred.clave);
        }
        actualizarAltura(n);
        return n;
    }

    // ===================== BALANCEO =====================

    // Fase 2: rebalancea todo el árbol y registra la última rotación.
    private void balancearArbol() {
        raiz = rebalancear(raiz);
        registrarRotacionPendiente();
        NodoAVL otro = buscarDesbalanceado(raiz);
        if (otro != null) throw new IllegalStateException("El árbol quedó desequilibrado en " + otro.clave);
    }

    // Recorrido postorden: primero se balancean los hijos y luego el nodo.
    // Cuando un hijo fue rotado, su nueva raíz se enlaza aquí y en ese momento
    // el árbol completo es consistente, así que se registra el paso.
    private NodoAVL rebalancear(NodoAVL n) {
        if (n == null) return null;
        n.izquierdo = rebalancear(n.izquierdo);
        registrarRotacionPendiente();
        n.derecho = rebalancear(n.derecho);
        registrarRotacionPendiente();
        actualizarAltura(n);
        return balancear(n);
    }

    private void registrarRotacionPendiente() {
        if (rotacionPendiente != null) {
            registrar(rotacionPendiente);
            rotacionPendiente = null;
        }
    }

    // Aplica la rotación que corresponda si el nodo tiene FE = ±2.
    private NodoAVL balancear(NodoAVL n) {
        int fe = n.getFE();
        if (fe == -2) {
            if (n.izquierdo.getFE() <= 0) {
                // Caso II
                rotacionPendiente = "Caso II en \"" + n.clave + "\" (FE=-2, hijo izquierdo \"" + n.izquierdo.clave
                        + "\" con FE=" + n.izquierdo.getFETexto() + "): ROTACIÓN SIMPLE A LA DERECHA sobre \""
                        + n.clave + "\". \"" + n.izquierdo.clave + "\" sube y \"" + n.clave + "\" pasa a ser su hijo derecho.";
                return rotarDerecha(n);
            }
            // Caso ID: primero se rota el hijo a la izquierda (queda como caso II) y luego el nodo a la derecha
            String hijo = n.izquierdo.clave;
            String nieto = n.izquierdo.derecho.clave;
            n.izquierdo = rotarIzquierda(n.izquierdo);
            registrar("Caso ID en \"" + n.clave + "\" (FE=-2, hijo izquierdo \"" + hijo + "\" con FE=+1): ROTACIÓN DOBLE. "
                    + "Paso 1: rotación simple a la IZQUIERDA sobre \"" + hijo + "\" (sube \"" + nieto + "\").");
            rotacionPendiente = "Caso ID en \"" + n.clave + "\": Paso 2: rotación simple a la DERECHA sobre \""
                    + n.clave + "\" (sube \"" + nieto + "\" y queda como raíz del subárbol).";
            return rotarDerecha(n);
        }
        if (fe == 2) {
            if (n.derecho.getFE() >= 0) {
                // Caso DD
                rotacionPendiente = "Caso DD en \"" + n.clave + "\" (FE=+2, hijo derecho \"" + n.derecho.clave
                        + "\" con FE=" + n.derecho.getFETexto() + "): ROTACIÓN SIMPLE A LA IZQUIERDA sobre \""
                        + n.clave + "\". \"" + n.derecho.clave + "\" sube y \"" + n.clave + "\" pasa a ser su hijo izquierdo.";
                return rotarIzquierda(n);
            }
            // Caso DI: primero se rota el hijo a la derecha (queda como caso DD) y luego el nodo a la izquierda
            String hijo = n.derecho.clave;
            String nieto = n.derecho.izquierdo.clave;
            n.derecho = rotarDerecha(n.derecho);
            registrar("Caso DI en \"" + n.clave + "\" (FE=+2, hijo derecho \"" + hijo + "\" con FE=-1): ROTACIÓN DOBLE. "
                    + "Paso 1: rotación simple a la DERECHA sobre \"" + hijo + "\" (sube \"" + nieto + "\").");
            rotacionPendiente = "Caso DI en \"" + n.clave + "\": Paso 2: rotación simple a la IZQUIERDA sobre \""
                    + n.clave + "\" (sube \"" + nieto + "\" y queda como raíz del subárbol).";
            return rotarIzquierda(n);
        }
        return n;
    }

    //        n                 h
    //       / \               / \
    //      h   C    ===>     A   n
    //     / \                   / \
    //    A   B                 B   C
    private NodoAVL rotarDerecha(NodoAVL n) {
        NodoAVL h = n.izquierdo;
        n.izquierdo = h.derecho;
        h.derecho = n;
        actualizarAltura(n);
        actualizarAltura(h);
        return h;
    }

    //      n                     h
    //     / \                   / \
    //    A   h      ===>       n   C
    //       / \               / \
    //      B   C             A   B
    private NodoAVL rotarIzquierda(NodoAVL n) {
        NodoAVL h = n.derecho;
        n.derecho = h.izquierdo;
        h.izquierdo = n;
        actualizarAltura(n);
        actualizarAltura(h);
        return h;
    }

    private static void actualizarAltura(NodoAVL n) {
        n.altura = 1 + Math.max(NodoAVL.altura(n.izquierdo), NodoAVL.altura(n.derecho));
    }

    // Retorna el nodo desequilibrado más profundo (el primero en postorden), o null.
    private NodoAVL buscarDesbalanceado(NodoAVL n) {
        if (n == null) return null;
        NodoAVL r = buscarDesbalanceado(n.izquierdo);
        if (r != null) return r;
        r = buscarDesbalanceado(n.derecho);
        if (r != null) return r;
        return Math.abs(n.getFE()) > 1 ? n : null;
    }

    private String describirDesequilibrio(NodoAVL d) {
        return "El nodo \"" + d.clave + "\" queda con FE=" + d.getFETexto()
                + " -> DESEQUILIBRIO, hay que rotar.";
    }

    // ===================== CONSULTAS =====================

    public boolean buscar(String palabra) {
        return buscarNodo(raiz, normalizar(palabra)) != null;
    }

    private NodoAVL buscarNodo(NodoAVL n, String clave) {
        if (n == null) return null;
        int cmp = clave.compareTo(n.clave);
        if (cmp == 0) return n;
        return cmp < 0 ? buscarNodo(n.izquierdo, clave) : buscarNodo(n.derecho, clave);
    }

    // Copia profunda del árbol. Las alturas se recalculan desde los hijos para que
    // los FE de la copia sean correctos aunque se copie en medio de un rebalanceo.
    static NodoAVL copiar(NodoAVL n) {
        if (n == null) return null;
        NodoAVL c = new NodoAVL(n.clave);
        c.izquierdo = copiar(n.izquierdo);
        c.derecho = copiar(n.derecho);
        actualizarAltura(c);
        return c;
    }

    // Verifica que el árbol cumpla las propiedades del AVL:
    // orden alfabético (ABB), alturas correctas y |FE| <= 1 en todos los nodos.
    public boolean esAVL() {
        return verificar(raiz, null, null) >= 0;
    }

    // Retorna la altura real o -1 si alguna propiedad falla.
    private int verificar(NodoAVL n, String min, String max) {
        if (n == null) return 0;
        if ((min != null && n.clave.compareTo(min) <= 0) || (max != null && n.clave.compareTo(max) >= 0)) return -1;
        int hi = verificar(n.izquierdo, min, n.clave);
        int hd = verificar(n.derecho, n.clave, max);
        if (hi < 0 || hd < 0 || Math.abs(hd - hi) > 1) return -1;
        int h = 1 + Math.max(hi, hd);
        return h == n.altura ? h : -1;
    }

    // ===================== RECORRIDOS =====================

    public Cola<String> inorden() { Cola<String> c = new Cola<>(); inorden(raiz, c); return c; }
    public Cola<String> preorden() { Cola<String> c = new Cola<>(); preorden(raiz, c); return c; }
    public Cola<String> postorden() { Cola<String> c = new Cola<>(); postorden(raiz, c); return c; }

    private void inorden(NodoAVL n, Cola<String> c) {
        if (n == null) return;
        inorden(n.izquierdo, c); c.encolar(n.clave); inorden(n.derecho, c);
    }

    private void preorden(NodoAVL n, Cola<String> c) {
        if (n == null) return;
        c.encolar(n.clave); preorden(n.izquierdo, c); preorden(n.derecho, c);
    }

    private void postorden(NodoAVL n, Cola<String> c) {
        if (n == null) return;
        postorden(n.izquierdo, c); postorden(n.derecho, c); c.encolar(n.clave);
    }

    // Por niveles, usando la cola propia.
    public Cola<String> amplitud() {
        Cola<String> resultado = new Cola<>();
        if (raiz == null) return resultado;
        Cola<NodoAVL> cola = new Cola<>();
        cola.encolar(raiz);
        while (!cola.estaVacia()) {
            NodoAVL n = cola.desencolar();
            resultado.encolar(n.clave);
            if (n.izquierdo != null) cola.encolar(n.izquierdo);
            if (n.derecho != null) cola.encolar(n.derecho);
        }
        return resultado;
    }
}
