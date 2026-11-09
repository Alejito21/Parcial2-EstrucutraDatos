package co.edu.uniquindio.poo.Vista;

import co.edu.uniquindio.poo.Model.Estructuras.Pila;
import co.edu.uniquindio.poo.Model.Punto1.Caja;
import co.edu.uniquindio.poo.Model.Punto1.Expresiones;
import co.edu.uniquindio.poo.Model.Punto1.GestionarCajas;

import javax.swing.*;
import java.awt.*;

// DOCUMENTACIÓN
// Pestaña del Punto 1: pila de cajas, retiro con reorganización y evaluación
// de la expresión (A + B) × C − (A − B) × D con las cajas que quedan en la pila.
@SuppressWarnings("serial")   // las ventanas no se serializan
public final class PanelPunto1 extends JPanel {

    private Pila<Caja> pila = new Pila<>();
    private final DefaultListModel<String> modeloPila = new DefaultListModel<>();
    private final JTextArea salida = UtilVista.areaTexto();

    private final JTextField txtCodigo = new JTextField("C8", 5);
    private final JTextField txtValor = new JTextField("1", 4);
    private final JTextField txtRetirar = new JTextField("C3", 5);

    public PanelPunto1() {
        super(new BorderLayout());

        JButton btnApilar = new JButton("Apilar caja");
        JButton btnPrueba = new JButton("Cargar datos de prueba");
        JButton btnVaciar = new JButton("Vaciar pila");
        JButton btnRetirar = new JButton("retirarCaja");
        JButton btnSecuencia = new JButton("Ejecutar prueba completa (C3, C4, C7)");
        JButton btnExpresion = new JButton("Evaluar expresión con la pila actual");
        JButton btnLimpiar = new JButton("Limpiar resultados");

        JPanel controles = new JPanel(new GridLayout(3, 1));
        controles.add(UtilVista.fila(new JLabel("Código:"), txtCodigo, new JLabel("Valor:"), txtValor,
                btnApilar, btnPrueba, btnVaciar));
        controles.add(UtilVista.fila(new JLabel("Código a retirar:"), txtRetirar, btnRetirar, btnSecuencia));
        controles.add(UtilVista.fila(btnExpresion, btnLimpiar));
        add(controles, BorderLayout.NORTH);

        JScrollPane vistaPila = UtilVista.conTitulo("Pila (tope → fondo)", UtilVista.lista(modeloPila));
        vistaPila.setPreferredSize(new Dimension(230, 0));
        vistaPila.setMinimumSize(new Dimension(180, 0));
        JSplitPane centro = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, vistaPila,
                UtilVista.conTitulo("Resultados", salida));
        centro.setDividerLocation(230);
        add(centro, BorderLayout.CENTER);

        btnApilar.addActionListener(e -> apilar());
        btnPrueba.addActionListener(e -> cargarPrueba());
        btnVaciar.addActionListener(e -> { pila = new Pila<>(); refrescar(); });
        btnRetirar.addActionListener(e -> retirar(txtRetirar.getText().trim()));
        btnSecuencia.addActionListener(e -> {
            UtilVista.agregar(salida, "\n=== Prueba completa del enunciado ===");
            cargarPrueba();
            retirar("C3");
            retirar("C4");
            retirar("C7");
            UtilVista.agregar(salida, "Pila final (cima → fondo): " + pila + "\n");
        });
        btnExpresion.addActionListener(e -> evaluarExpresion());
        btnLimpiar.addActionListener(e -> salida.setText(""));

        cargarPrueba();
    }

    private void apilar() {
        try {
            String codigo = txtCodigo.getText().trim();
            if (codigo.isEmpty()) throw new IllegalArgumentException("El código no puede estar vacío.");
            for (Caja c : pila) {
                if (c.getCodigo().equals(codigo)) throw new IllegalArgumentException("Ya existe una caja con código " + codigo + ".");
            }
            int valor = UtilVista.leerEntero(txtValor, "El valor");
            pila.apilar(new Caja(codigo, valor));
            UtilVista.agregar(salida, "apilar(" + codigo + ":" + valor + ")  →  " + pila);
            refrescar();
        } catch (IllegalArgumentException ex) {
            UtilVista.error(this, ex.getMessage());
        }
    }

    // Datos del enunciado: C1:2, C2:4, C3:6, C4:8, C5:3, C6:9, C7:5 (C1 al fondo).
    private void cargarPrueba() {
        pila = new Pila<>();
        int[] valores = {2, 4, 6, 8, 3, 9, 5};
        for (int i = 0; i < valores.length; i++) pila.apilar(new Caja("C" + (i + 1), valores[i]));
        UtilVista.agregar(salida, "Pila inicial (tope → fondo): " + pila);
        refrescar();
    }

    private void retirar(String codigo) {
        boolean r = GestionarCajas.retirarCaja(pila, codigo);
        UtilVista.agregar(salida, "retirarCaja(" + codigo + ") = " + r + "  →  " + pila);
        refrescar();
    }

    private void evaluarExpresion() {
        if (pila.tamanio() < 4) {
            UtilVista.error(this, "La pila debe tener al menos 4 cajas para asignar A, B, C y D.");
            return;
        }
        String texto = UtilVista.capturar(() -> {
            double[] abcd = Expresiones.calcularVariables(pila);
            System.out.println("\n=== Evaluación de (A + B) × C − (A − B) × D ===");
            System.out.println("Pila usada (cima → fondo): " + pila);
            System.out.println("1. A = " + Expresiones.num(abcd[0]) + ", B = " + Expresiones.num(abcd[1])
                    + ", C = " + Expresiones.num(abcd[2]) + ", D = " + Expresiones.num(abcd[3]));
            System.out.println("\n2. Conversión a posfija:");
            String[] posfija = Expresiones.infijaAPosfija(Expresiones.EXPRESION_INFIJA, true);
            System.out.println("Posfija: " + Expresiones.unir(posfija));
            String[] valores = Expresiones.reemplazarVariables(posfija, abcd);
            System.out.println("\n3. Con valores: " + Expresiones.unir(valores));
            System.out.println("\n4. Evaluación con la pila:");
            double resultado = Expresiones.evaluarPosfija(valores, true);
            System.out.println("\n5. Resultado final = " + Expresiones.num(resultado));
        });
        UtilVista.agregar(salida, texto);
    }

    // Actualiza la lista visual de la pila numerando las posiciones desde la cima.
    private void refrescar() {
        modeloPila.clear();
        int pos = 1;
        for (Caja c : pila) {
            modeloPila.addElement(pos + ". " + c + (pos == 1 ? "   ← tope" : ""));
            pos++;
        }
        if (pila.estaVacia()) modeloPila.addElement("[vacía]");
    }
}
