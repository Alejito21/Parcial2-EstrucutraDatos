package co.edu.uniquindio.poo.Vista;

import co.edu.uniquindio.poo.Model.Estructuras.NodoArbol;
import co.edu.uniquindio.poo.Model.Punto5.NodoAVL;
import co.edu.uniquindio.poo.Model.Punto5.NodoRN;

import javax.swing.*;
import java.awt.*;
import java.util.IdentityHashMap;
import java.util.Map;

// DOCUMENTACIÓN
// Panel que dibuja gráficamente cualquier árbol binario (NodoArbol).
// Distribución:
//   - Coordenada X: según el orden INORDEN (cada nodo ocupa una "columna"), así el
//     dibujo respeta el orden del árbol y los nodos nunca se superponen.
//   - Coordenada Y: según la profundidad (nivel) del nodo.
// Estilo según el tipo de nodo:
//   - AVL: muestra la clave y su FE; si |FE| >= 2 el nodo se pinta en rojo claro.
//   - Rojo-Negro: nodo relleno de rojo o negro.
//   - Otros (ABB): nodo verde claro.
@SuppressWarnings("serial")   // las ventanas no se serializan
public final class PanelArbol extends JPanel {

    private static final int ALTO_NIVEL = 75;   // separación vertical entre niveles
    private static final int SEPARACION = 14;   // espacio horizontal entre columnas
    private static final int MARGEN = 25;

    private static final Color AVL_FONDO = new Color(0xE3F2FD), AVL_BORDE = new Color(0x1565C0);
    private static final Color MAL_FONDO = new Color(0xFFCDD2), MAL_BORDE = new Color(0xC62828);
    private static final Color ABB_FONDO = new Color(0xE8F5E9), ABB_BORDE = new Color(0x2E7D32);
    private static final Color RN_ROJO = new Color(0xD32F2F), RN_NEGRO = new Color(0x263238);

    private final Font fuenteClave = new Font(Font.SANS_SERIF, Font.BOLD, 13);
    private final Font fuenteFE = new Font(Font.SANS_SERIF, Font.PLAIN, 11);

    private NodoArbol raiz;
    // Posición (centro) y ancho calculados para cada nodo
    private final Map<NodoArbol, Rectangle> posiciones = new IdentityHashMap<>();
    private int siguienteX;

    public PanelArbol() {
        setBackground(Color.WHITE);
    }

    public void setRaiz(NodoArbol raiz) {
        this.raiz = raiz;
        calcularPosiciones();
        revalidate();
        repaint();
    }

    // ---------- cálculo de posiciones ----------

    private void calcularPosiciones() {
        posiciones.clear();
        siguienteX = MARGEN;
        int[] maxNivel = {0};
        ubicar(raiz, 0, maxNivel);
        int ancho = siguienteX + MARGEN;
        int alto = MARGEN * 2 + maxNivel[0] * ALTO_NIVEL + altoNodo();
        setPreferredSize(new Dimension(ancho, alto));
    }

    // Recorrido inorden: izquierdo, se asigna la columna al nodo, derecho.
    private void ubicar(NodoArbol n, int nivel, int[] maxNivel) {
        if (n == null) return;
        ubicar(n.getIzquierdo(), nivel + 1, maxNivel);
        int ancho = anchoNodo(n);
        int x = siguienteX + ancho / 2;
        int y = MARGEN + altoNodo() / 2 + nivel * ALTO_NIVEL;
        posiciones.put(n, new Rectangle(x, y, ancho, altoNodo()));
        siguienteX += ancho + SEPARACION;
        maxNivel[0] = Math.max(maxNivel[0], nivel);
        ubicar(n.getDerecho(), nivel + 1, maxNivel);
    }

    private int altoNodo() {
        return raiz instanceof NodoAVL ? 42 : 32;
    }

    private int anchoNodo(NodoArbol n) {
        FontMetrics fm = getFontMetrics(fuenteClave);
        return Math.max(40, fm.stringWidth(texto(n)) + 22);
    }

    // Texto principal del nodo (sin FE ni color, que se dibujan aparte).
    private static String texto(NodoArbol n) {
        if (n instanceof NodoAVL) return ((NodoAVL) n).getClave();
        if (n instanceof NodoRN) return ((NodoRN) n).getClave();
        return n.getEtiqueta();
    }

    // ---------- dibujo ----------

    @Override
    protected void paintComponent(Graphics g0) {
        super.paintComponent(g0);
        Graphics2D g = (Graphics2D) g0;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        if (raiz == null) {
            g.setColor(Color.GRAY);
            g.drawString("(árbol vacío)", MARGEN, MARGEN + 10);
            return;
        }
        g.setStroke(new BasicStroke(1.6f));
        dibujarAristas(g, raiz);
        dibujarNodos(g, raiz);
    }

    private void dibujarAristas(Graphics2D g, NodoArbol n) {
        if (n == null) return;
        Rectangle p = posiciones.get(n);
        g.setColor(Color.GRAY);
        for (NodoArbol hijo : new NodoArbol[]{n.getIzquierdo(), n.getDerecho()}) {
            if (hijo != null) {
                Rectangle h = posiciones.get(hijo);
                g.drawLine(p.x, p.y + p.height / 2, h.x, h.y - h.height / 2);
            }
        }
        dibujarAristas(g, n.getIzquierdo());
        dibujarAristas(g, n.getDerecho());
    }

    private void dibujarNodos(Graphics2D g, NodoArbol n) {
        if (n == null) return;
        Rectangle r = posiciones.get(n);
        int x = r.x - r.width / 2, y = r.y - r.height / 2;

        Color fondo, borde, colorTexto = Color.BLACK;
        if (n instanceof NodoRN) {
            fondo = ((NodoRN) n).isRojo() ? RN_ROJO : RN_NEGRO;
            borde = fondo.darker();
            colorTexto = Color.WHITE;
        } else if (n instanceof NodoAVL) {
            boolean mal = Math.abs(((NodoAVL) n).getFE()) > 1;
            fondo = mal ? MAL_FONDO : AVL_FONDO;
            borde = mal ? MAL_BORDE : AVL_BORDE;
        } else {
            fondo = ABB_FONDO;
            borde = ABB_BORDE;
        }
        g.setColor(fondo);
        g.fillRoundRect(x, y, r.width, r.height, 18, 18);
        g.setColor(borde);
        g.drawRoundRect(x, y, r.width, r.height, 18, 18);

        // Clave (y FE debajo en el caso AVL)
        g.setColor(colorTexto);
        g.setFont(fuenteClave);
        FontMetrics fm = g.getFontMetrics();
        String t = texto(n);
        if (n instanceof NodoAVL) {
            g.drawString(t, r.x - fm.stringWidth(t) / 2, y + 17);
            String fe = "FE=" + ((NodoAVL) n).getFETexto();
            g.setFont(fuenteFE);
            FontMetrics fm2 = g.getFontMetrics();
            g.setColor(borde);
            g.drawString(fe, r.x - fm2.stringWidth(fe) / 2, y + 33);
        } else {
            g.drawString(t, r.x - fm.stringWidth(t) / 2, r.y + fm.getAscent() / 2 - 2);
        }

        dibujarNodos(g, n.getIzquierdo());
        dibujarNodos(g, n.getDerecho());
    }
}
