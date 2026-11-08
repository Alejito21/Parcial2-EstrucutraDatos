package co.edu.uniquindio.poo.Main;

import co.edu.uniquindio.poo.Model.Estructuras.Pila;
import co.edu.uniquindio.poo.Model.Punto1.Caja;
import co.edu.uniquindio.poo.Model.Punto1.Expresiones;
import co.edu.uniquindio.poo.Model.Punto1.GestionarCajas;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

public class Main {
    private static int fallos = 0;

    private static void verificar(String descripcion, Object obtenido, Object esperado) {
        boolean ok = obtenido.equals(esperado);
        if (!ok) fallos++;
        System.out.println((ok ? "  [OK]   " : "  [FALLA] ") + descripcion + (ok ? "" : " -> obtenido: " + obtenido + " | esperado: " + esperado));
    }

    private static void titulo(String t) {
        System.out.println("\n==================================================================");
        System.out.println(t);
        System.out.println("==================================================================");
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        System.setOut(new PrintStream(System.out, true, "UTF-8"));   // para ver →, × y tildes en la consola
        punto1();
        System.out.println("\n" + (fallos == 0 ? "TODAS LAS VERIFICACIONES PASARON" : "VERIFICACIONES FALLIDAS: " + fallos));
    }

    // PUNTO 1
    private static void punto1() {
        titulo("PUNTO 1 - Retiro de cajas y evaluación de expresiones");
        Pila<Caja> pila = new Pila<>();
        GestionarCajas gc = new GestionarCajas();
        pila.apilar(new Caja("C1", 2));
        pila.apilar(new Caja("C2", 4));
        pila.apilar(new Caja("C3", 6));
        pila.apilar(new Caja("C4", 8));
        pila.apilar(new Caja("C5", 3));
        pila.apilar(new Caja("C6", 9));
        pila.apilar(new Caja("C7", 5));
        System.out.println("Pila inicial (tope → fondo): " + pila);
        verificar("pila inicial", pila.toString(), "C7:5 → C6:9 → C5:3 → C4:8 → C3:6 → C2:4 → C1:2");

        String[] retiros = {"C3", "C4", "C7"};
        String[] esperadas = {
                "C7:5 → C5:3 → C2:4 → C6:9 → C4:8 → C1:2",
                "C7:5 → C2:4 → C1:2 → C5:3 → C6:9",
                "C1:2 → C6:9 → C2:4 → C5:3"};
        for (int i = 0; i < retiros.length; i++) {

            boolean r = gc.retirarCaja(pila, retiros[i]);
            System.out.println("retirarCaja(" + retiros[i] + ") = " + r + "  →  " + pila);
            verificar("llamada " + (i + 1), r + " | " + pila, "true | " + esperadas[i]);
        }

        // Casos límite
        Pila<Caja> copia = new Pila<>();
        verificar("pila vacía retorna false", gc.retirarCaja(copia, "C1"), false);
        String antes = pila.toString();
        verificar("código inexistente retorna false", gc.retirarCaja(pila, "C99"), false);
        verificar("código inexistente deja la pila igual", pila.toString(), antes);

        System.out.println("\nPila final (cima → fondo): " + pila);

        // A..D con los valores de las cuatro cajas finales (cima → fondo)
        int v1 = pila.desapilar().getValor();
        int v2 = pila.desapilar().getValor();
        int v3 = pila.desapilar().getValor();
        int v4 = pila.desapilar().getValor();
        double A = v1;
        double B = v2 / A;
        double C = v3 + A;
        double D = (v4 + C) * A;
        System.out.println("A = " + Expresiones.num(A) + ", B = " + Expresiones.num(B)
                + ", C = " + Expresiones.num(C) + ", D = " + Expresiones.num(D));

        System.out.println("\nConversión de (A + B) × C − (A − B) × D a posfija:");
        String[] infija = {"(", "A", "+", "B", ")", "*", "C", "-", "(", "A", "-", "B", ")", "*", "D"};
        String[] posfija = Expresiones.infijaAPosfija(infija, true);
        StringBuilder sb = new StringBuilder();
        for (String s : posfija) sb.append(s).append(' ');
        System.out.println("Posfija: " + sb.toString().trim());
        verificar("posfija", sb.toString().trim(), "A B + C * A B - D * -");

        // Reemplazo de variables por valores
        String[] valores = new String[posfija.length];
        for (int i = 0; i < posfija.length; i++) {
            switch (posfija[i]) {
                case "A": valores[i] = Expresiones.num(A); break;
                case "B": valores[i] = Expresiones.num(B); break;
                case "C": valores[i] = Expresiones.num(C); break;
                case "D": valores[i] = Expresiones.num(D); break;
                default: valores[i] = posfija[i];
            }
        }
        System.out.println("\nEvaluación de la posfija con valores:");
        double resultado = Expresiones.evaluarPosfija(valores, true);
        System.out.println("Resultado final = " + Expresiones.num(resultado));
        verificar("resultado de la expresión", resultado, 84.0);
    }
}