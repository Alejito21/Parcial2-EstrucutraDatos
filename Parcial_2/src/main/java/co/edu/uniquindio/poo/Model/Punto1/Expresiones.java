package co.edu.uniquindio.poo.Model.Punto1;

import co.edu.uniquindio.poo.Model.Estructuras.Pila;

public class Expresiones {

    private static boolean esOperador(String t) {
        return t.equals("+") || t.equals("-") || t.equals("*") || t.equals("/");
    }

    private static int precedencia(String op) {
        return (op.equals("*") || op.equals("/")) ? 2 : 1;
    }

    private static String vista(String op) {
        return op.equals("*") ? "×" : op.equals("/") ? "÷" : op;
    }

    /** Convierte una expresión infija (ya separada en símbolos) a posfija. */
    public static String[] infijaAPosfija(String[] simbolos, boolean traza) {
        Pila<String> operadores = new Pila<>();
        String[] salida = new String[simbolos.length];
        int n = 0;
        if (traza) System.out.printf("%-10s %-34s %-18s %s%n", "Símbolo", "Acción", "Pila operadores", "Salida");
        for (String s : simbolos) {
            String accion;
            if (s.equals("(")) {
                operadores.apilar(s);
                accion = "se apila (";
            } else if (s.equals(")")) {
                while (!operadores.tope().equals("(")) salida[n++] = operadores.desapilar();
                operadores.desapilar();
                accion = "salen operadores hasta (";
            } else if (esOperador(s)) {
                while (!operadores.estaVacia() && !operadores.tope().equals("(")
                        && precedencia(operadores.tope()) >= precedencia(s)) {
                    salida[n++] = operadores.desapilar();
                }
                operadores.apilar(s);
                accion = "operador: se apila " + vista(s);
            } else {
                salida[n++] = s;
                accion = "operando a la salida";
            }
            if (traza) System.out.printf("%-10s %-34s %-18s %s%n", vista(s), accion, operadores, unir(salida, n));
        }
        while (!operadores.estaVacia()) salida[n++] = operadores.desapilar();
        if (traza) System.out.printf("%-10s %-34s %-18s %s%n", "fin", "se vacía la pila", "[vacía]", unir(salida, n));
        String[] resultado = new String[n];
        for (int i = 0; i < n; i++) resultado[i] = salida[i];
        return resultado;
    }

    /** Evalúa una expresión posfija cuyos operandos ya son números. */
    public static double evaluarPosfija(String[] posfija, boolean traza) {
        Pila<Double> pila = new Pila<>();
        if (traza) System.out.printf("%-10s %-44s %s%n", "Elemento", "Operación", "Pila (tope → fondo)");
        for (String s : posfija) {
            String operacion;
            if (esOperador(s)) {
                double b = pila.desapilar();
                double a = pila.desapilar();
                double r = s.equals("+") ? a + b : s.equals("-") ? a - b : s.equals("*") ? a * b : a / b;
                pila.apilar(r);
                operacion = "a=" + num(a) + " " + vista(s) + " b=" + num(b) + " = " + num(r);
            } else {
                pila.apilar(Double.parseDouble(s));
                operacion = "se apila " + s;
            }
            if (traza) System.out.printf("%-10s %-44s %s%n", vista(s), operacion, pilaNum(pila));
        }
        return pila.desapilar();
    }

    private static String unir(String[] v, int n) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) sb.append(vista(v[i])).append(' ');
        return sb.toString().trim();
    }

    private static String pilaNum(Pila<Double> p) {
        // Muestra tope → fondo sin destruir la pila
        Pila<Double> aux = new Pila<>();
        StringBuilder sb = new StringBuilder();
        while (!p.estaVacia()) {
            double d = p.desapilar();
            aux.apilar(d);
            sb.append(num(d));
            if (!p.estaVacia()) sb.append(" → ");
        }
        while (!aux.estaVacia()) p.apilar(aux.desapilar());
        return sb.toString();
    }

    public static String num(double d) {
        return d == Math.rint(d) ? String.valueOf((long) d) : String.valueOf(d);
    }
}
