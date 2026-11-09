package co.edu.uniquindio.poo.Model.Estructuras;

// DOCUMENTACIÓN
// Dibuja un árbol binario en texto, "acostado" (girado 90° a la izquierda):
// la raíz queda a la izquierda, el subárbol DERECHO hacia arriba y el IZQUIERDO hacia abajo.
// Ejemplo:
//         ┌── eco (FE=0)
//     dedo (FE=0)
//         └── cine (FE=0)
public class DibujoArbol {

    public static String texto(NodoArbol raiz) {
        if (raiz == null) return "(árbol vacío)\n";
        StringBuilder sb = new StringBuilder();
        dibujar(raiz, "", 0, sb);
        return sb.toString();
    }

    // lado: 0 = raíz, 1 = hijo derecho (dibujado arriba), -1 = hijo izquierdo (dibujado abajo).
    // 'prefijo' acumula las líneas verticales de los niveles superiores.
    private static void dibujar(NodoArbol n, String prefijo, int lado, StringBuilder sb) {
        if (n.getDerecho() != null) {
            dibujar(n.getDerecho(), prefijo + (lado == -1 ? "│   " : "    "), 1, sb);
        }
        String conector = lado == 0 ? "" : lado == 1 ? "┌── " : "└── ";
        sb.append(prefijo).append(conector).append(n.getEtiqueta()).append('\n');
        if (n.getIzquierdo() != null) {
            dibujar(n.getIzquierdo(), prefijo + (lado == 1 ? "│   " : "    "), -1, sb);
        }
    }
}
