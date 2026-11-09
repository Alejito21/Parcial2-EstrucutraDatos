package co.edu.uniquindio.poo.Vista;

import co.edu.uniquindio.poo.Model.Punto4.ArbolABB;
import co.edu.uniquindio.poo.Model.Punto4.NodoABB;

import javax.swing.*;
import java.awt.*;

// DOCUMENTACIÓN
// Pestaña del Punto 4: árbol binario de búsqueda dibujado, consulta de equipos
// por rango, eliminación (opción: predecesor inorden) y los cuatro recorridos.
@SuppressWarnings("serial")   // las ventanas no se serializan
public final class PanelPunto4 extends JPanel {

    private static final int[] DATOS_PRUEBA = {41, 20, 52, 16, 6, 13, 12, 60, 22, 47, 50, 60, 43, 44};

    private ArbolABB arbol = new ArbolABB();
    private final PanelArbol dibujo = new PanelArbol();
    private final JTextArea recorridos = UtilVista.areaTexto();
    private final JTextArea salida = UtilVista.areaTexto();

    private final JTextField txtClave = new JTextField("41", 4);
    private final JTextField txtInferior = new JTextField("15", 4);
    private final JTextField txtSuperior = new JTextField("50", 4);

    public PanelPunto4() {
        super(new BorderLayout());
        salida.setLineWrap(true);
        salida.setWrapStyleWord(true);

        JButton btnPrueba = new JButton("Cargar datos de prueba");
        JButton btnInsertar = new JButton("Insertar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnVaciar = new JButton("Vaciar");
        JButton btnRango = new JButton("contarEquiposEnRango");

        JPanel controles = new JPanel(new GridLayout(2, 1));
        controles.add(UtilVista.fila(new JLabel("Código:"), txtClave, btnInsertar, btnEliminar, btnPrueba, btnVaciar,
                new JLabel("  Eliminación con dos hijos: predecesor inorden (mayor del subárbol izquierdo)")));
        controles.add(UtilVista.fila(new JLabel("Límite inferior:"), txtInferior,
                new JLabel("Límite superior:"), txtSuperior, btnRango));
        add(controles, BorderLayout.NORTH);

        JScrollPane abajoRecorridos = UtilVista.conTitulo("Recorridos", recorridos);
        JScrollPane abajoSalida = UtilVista.conTitulo("Resultados", salida);
        JPanel abajo = new JPanel(new GridLayout(1, 2, 8, 0));
        abajo.add(abajoRecorridos);
        abajo.add(abajoSalida);
        abajo.setPreferredSize(new Dimension(0, 170));

        add(UtilVista.conTitulo("Árbol", dibujo), BorderLayout.CENTER);
        add(abajo, BorderLayout.SOUTH);

        btnPrueba.addActionListener(e -> cargarPrueba());
        btnInsertar.addActionListener(e -> insertar());
        btnEliminar.addActionListener(e -> eliminar());
        btnVaciar.addActionListener(e -> { arbol = new ArbolABB(); refrescar(); });
        btnRango.addActionListener(e -> contarRango());

        cargarPrueba();
    }

    private void cargarPrueba() {
        arbol = new ArbolABB();
        StringBuilder sb = new StringBuilder("Se insertan: ");
        for (int c : DATOS_PRUEBA) {
            sb.append(c).append(arbol.insertar(c) ? " " : " (repetido, se ignora) ");
        }
        UtilVista.agregar(salida, sb.toString().trim());
        refrescar();
    }

    private void insertar() {
        try {
            int c = UtilVista.leerEntero(txtClave, "El código");
            UtilVista.agregar(salida, arbol.insertar(c) ? "Se insertó " + c : c + " ya existe, no se inserta");
            refrescar();
        } catch (IllegalArgumentException ex) {
            UtilVista.error(this, ex.getMessage());
        }
    }

    private void eliminar() {
        try {
            int c = UtilVista.leerEntero(txtClave, "El código");
            String explicacion = explicarEliminacion(c);
            if (arbol.eliminar(c)) {
                UtilVista.agregar(salida, "Eliminar " + c + ": " + explicacion);
            } else {
                UtilVista.agregar(salida, c + " no existe en el árbol");
            }
            refrescar();
        } catch (IllegalArgumentException ex) {
            UtilVista.error(this, ex.getMessage());
        }
    }

    // Describe qué caso de eliminación aplica ANTES de eliminar.
    private String explicarEliminacion(int clave) {
        NodoABB n = arbol.getRaiz();
        while (n != null && n.getClave() != clave) n = clave < n.getClave() ? n.getIzquierdo() : n.getDerecho();
        if (n == null) return "";
        if (n.getIzquierdo() == null && n.getDerecho() == null) return "es hoja, se quita directamente.";
        if (n.getIzquierdo() == null || n.getDerecho() == null) return "tiene un solo hijo, que ocupa su lugar.";
        NodoABB pred = n.getIzquierdo();
        while (pred.getDerecho() != null) pred = pred.getDerecho();
        return "tiene dos hijos. Opción: PREDECESOR INORDEN (mayor del subárbol izquierdo) = "
                + pred.getClave() + ", que toma su lugar.";
    }

    private void contarRango() {
        try {
            int inf = UtilVista.leerEntero(txtInferior, "El límite inferior");
            int sup = UtilVista.leerEntero(txtSuperior, "El límite superior");
            int total = ArbolABB.contarEquiposEnRango(arbol.getRaiz(), inf, sup);
            UtilVista.agregar(salida, "contarEquiposEnRango(" + inf + ", " + sup + ") = " + total);
        } catch (IllegalArgumentException ex) {
            UtilVista.error(this, ex.getMessage());
        }
    }

    private void refrescar() {
        dibujo.setRaiz(arbol.getRaiz());
        recorridos.setText("Inorden:   " + arbol.inorden()
                + "\nPreorden:  " + arbol.preorden()
                + "\nPostorden: " + arbol.postorden()
                + "\nAmplitud:  " + arbol.amplitud());
    }
}
