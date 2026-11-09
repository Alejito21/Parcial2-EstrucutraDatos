package co.edu.uniquindio.poo.Vista;

import co.edu.uniquindio.poo.Main.Main;
import co.edu.uniquindio.poo.Model.Estructuras.Cola;
import co.edu.uniquindio.poo.Model.Estructuras.NodoArbol;
import co.edu.uniquindio.poo.Model.Punto5.ArbolAVL;
import co.edu.uniquindio.poo.Model.Punto5.ArbolRN;
import co.edu.uniquindio.poo.Model.Punto5.Paso;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

// DOCUMENTACIÓN
// Pestaña del Punto 5: AVL y Rojo-Negro paso a paso.
// Cada operación agrega sus pasos a la lista de la izquierda; al seleccionar un paso
// se ve su descripción y el árbol en ese momento (con FE en el AVL y colores en el RN).
// Los botones Anterior / Siguiente permiten recorrer los pasos uno a uno.
@SuppressWarnings("serial")   // las ventanas no se serializan
public final class PanelPunto5 extends JPanel {

    private ArbolAVL avl = new ArbolAVL();
    private ArbolRN rn;

    // Pasos acumulados (la lista de la GUI usa ArrayList solo para la navegación visual)
    private final List<Paso<? extends NodoArbol>> pasos = new ArrayList<>();
    private final DefaultListModel<String> modeloPasos = new DefaultListModel<>();
    private final JList<String> listaPasos = new JList<>(modeloPasos);

    private final PanelArbol dibujo = new PanelArbol();
    private final JTextArea descripcion = new JTextArea(3, 40);
    private final JTextArea recorridos = UtilVista.areaTexto();
    private final JLabel lblPaso = new JLabel("Paso 0 de 0");
    private final JTextField txtPalabra = new JTextField("cine", 8);

    public PanelPunto5() {
        super(new BorderLayout());

        JButton btnConstruir = new JButton("a) Construir AVL con la lista");
        JButton btnInsertar = new JButton("Insertar en AVL");
        JButton btnEliminar = new JButton("b) Eliminar del AVL");
        JButton btnRN = new JButton("c) Construir Rojo-Negro con el postorden del AVL");
        JButton btnReiniciar = new JButton("Reiniciar");
        JButton btnAnterior = new JButton("◀ Anterior");
        JButton btnSiguiente = new JButton("Siguiente ▶");
        JButton btnUltimo = new JButton("Último ▶▶");

        JPanel controles = new JPanel(new GridLayout(2, 1));
        controles.add(UtilVista.fila(btnConstruir, new JLabel("Palabra:"), txtPalabra, btnInsertar, btnEliminar,
                btnRN, btnReiniciar));
        controles.add(UtilVista.fila(btnAnterior, btnSiguiente, btnUltimo, lblPaso,
                new JLabel("   FE = altura(derecho) − altura(izquierdo).  Al eliminar con dos hijos: mayor del subárbol izquierdo.")));
        add(controles, BorderLayout.NORTH);

        // Lista de pasos (izquierda)
        listaPasos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaPasos.addListSelectionListener(e -> { if (!e.getValueIsAdjusting()) mostrarPaso(); });
        JScrollPane izquierda = UtilVista.conTitulo("Pasos", listaPasos);
        izquierda.setPreferredSize(new Dimension(330, 0));

        // Descripción + dibujo + recorridos (derecha)
        descripcion.setEditable(false);
        descripcion.setLineWrap(true);
        descripcion.setWrapStyleWord(true);
        descripcion.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        descripcion.setMargin(new Insets(6, 8, 6, 8));
        JScrollPane recorridosScroll = UtilVista.conTitulo("Recorridos", recorridos);
        recorridosScroll.setPreferredSize(new Dimension(0, 130));

        JPanel derecha = new JPanel(new BorderLayout());
        derecha.add(UtilVista.conTitulo("Descripción del paso", descripcion), BorderLayout.NORTH);
        derecha.add(UtilVista.conTitulo("Árbol", dibujo), BorderLayout.CENTER);
        derecha.add(recorridosScroll, BorderLayout.SOUTH);

        // Tamaños mínimos para que un árbol ancho no aplaste la lista de pasos
        izquierda.setMinimumSize(new Dimension(250, 0));
        derecha.setMinimumSize(new Dimension(300, 0));
        JSplitPane division = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, izquierda, derecha);
        division.setDividerLocation(330);
        add(division, BorderLayout.CENTER);

        btnConstruir.addActionListener(e -> construirAVL());
        btnInsertar.addActionListener(e -> {
            String p = txtPalabra.getText().trim();
            if (p.isEmpty()) { UtilVista.error(this, "Escriba una palabra."); return; }
            avl.insertar(p);
            agregarPasos("AVL", avl.getPasos());
            avl.limpiarPasos();
        });
        btnEliminar.addActionListener(e -> {
            String p = txtPalabra.getText().trim();
            if (p.isEmpty()) { UtilVista.error(this, "Escriba una palabra."); return; }
            avl.eliminar(p);
            agregarPasos("AVL", avl.getPasos());
            avl.limpiarPasos();
        });
        btnRN.addActionListener(e -> construirRN());
        btnReiniciar.addActionListener(e -> {
            avl = new ArbolAVL();
            rn = null;
            pasos.clear();
            modeloPasos.clear();
            mostrarPaso();
        });
        btnAnterior.addActionListener(e -> mover(-1));
        btnSiguiente.addActionListener(e -> mover(1));
        btnUltimo.addActionListener(e -> seleccionar(pasos.size() - 1));

        construirAVL();
    }

    // a) Inserta en un AVL nuevo las 15 palabras del enunciado.
    private void construirAVL() {
        avl = new ArbolAVL();
        rn = null;
        pasos.clear();
        modeloPasos.clear();
        for (String p : Main.PALABRAS_AVL) avl.insertar(p);
        agregarPasos("AVL", avl.getPasos());
        avl.limpiarPasos();
        seleccionar(0);
    }

    // c) Construye un Rojo-Negro nuevo insertando el postorden del AVL actual.
    private void construirRN() {
        Cola<String> postorden = avl.postorden();
        if (postorden.estaVacia()) { UtilVista.error(this, "El AVL está vacío."); return; }
        rn = new ArbolRN();
        for (String p : postorden) rn.insertar(p);
        int primero = pasos.size();
        agregarPasos("RN", rn.getPasos());
        rn.limpiarPasos();
        seleccionar(primero);
    }

    // Agrega los pasos de una operación a la lista y selecciona el último.
    private void agregarPasos(String prefijo, Cola<? extends Paso<? extends NodoArbol>> nuevos) {
        for (Paso<? extends NodoArbol> p : nuevos) {
            pasos.add(p);
            String d = p.getDescripcion();
            modeloPasos.addElement(pasos.size() + ". [" + prefijo + "] " + (d.length() > 70 ? d.substring(0, 70) + "…" : d));
        }
        seleccionar(pasos.size() - 1);
        actualizarRecorridos();
    }

    private void seleccionar(int i) {
        if (pasos.isEmpty()) { mostrarPaso(); return; }
        i = Math.max(0, Math.min(i, pasos.size() - 1));
        listaPasos.setSelectedIndex(i);
        listaPasos.ensureIndexIsVisible(i);
        mostrarPaso();
    }

    private void mover(int delta) {
        seleccionar(listaPasos.getSelectedIndex() + delta);
    }

    private void mostrarPaso() {
        int i = listaPasos.getSelectedIndex();
        if (i < 0 || i >= pasos.size()) {
            dibujo.setRaiz(null);
            descripcion.setText("");
            lblPaso.setText("Paso 0 de " + pasos.size());
            actualizarRecorridos();
            return;
        }
        Paso<? extends NodoArbol> p = pasos.get(i);
        descripcion.setText(p.getDescripcion());
        dibujo.setRaiz(p.getArbol());
        lblPaso.setText("Paso " + (i + 1) + " de " + pasos.size());
    }

    private void actualizarRecorridos() {
        StringBuilder sb = new StringBuilder();
        sb.append("AVL actual\n");
        sb.append("  Inorden:   ").append(avl.inorden()).append('\n');
        sb.append("  Preorden:  ").append(avl.preorden()).append('\n');
        sb.append("  Postorden: ").append(avl.postorden()).append('\n');
        sb.append("  Amplitud:  ").append(avl.amplitud()).append('\n');
        if (rn != null) {
            sb.append("Rojo-Negro (").append(rn.esRojoNegro() ? "válido" : "INVÁLIDO")
                    .append(", altura negra ").append(rn.getAlturaNegra()).append(")\n");
            sb.append("  Preorden:  ").append(rn.preorden()).append('\n');
        }
        recorridos.setText(sb.toString());
        recorridos.setCaretPosition(0);
    }
}
