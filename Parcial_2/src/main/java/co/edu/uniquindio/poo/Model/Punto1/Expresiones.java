package co.edu.uniquindio.poo.Model.Punto1;

import co.edu.uniquindio.poo.Model.Estructuras.Pila;

// DOCUMENTACIÓN
// Punto 1 (segunda parte): conversión de una expresión infija a posfija
// (algoritmo de la pila de operadores) y evaluación de la posfija con una pila.
// Las expresiones se reciben como arreglos de símbolos: "(", ")", "+", "-", "*", "/",
// variables (A, B, ...) o números.
// Si 'traza' es true se imprime la tabla paso a paso (elemento, operación, pila).
public class Expresiones {

    private static boolean esOperador(String t) {
        return t.equals("+") || t.equals("-") || t.equals("*") || t.equals("/");
    }

    // Prioridad: * y / (2) mayor que + y - (1).
    private static int precedencia(String op) {
        return (op.equals("*") || op.equals("/")) ? 2 : 1;
    }

    // Símbolo "bonito" para mostrar en las tablas (* -> ×, / -> ÷).
    private static String vista(String op) {
        return op.equals("*") ? "×" : op.equals("/") ? "÷" : op;
    }

    // Convierte infija -> posfija.
    // Reglas:
    //  - Operando: pasa directo a la salida.
    //  - "(": se apila.
    //  - ")": se desapilan operadores a la salida hasta encontrar "(" (que se descarta).
    //  - Operador: salen a la salida los operadores de la pila con prioridad mayor o
    //    igual (asociatividad izquierda) y luego se apila el operador actual.
    //  - Al final se vacía la pila de operadores en la salida.
    public static String[] infijaAPosfija(String[] simbolos, boolean traza) {
        Pila<String> operadores = new Pila<>();
        String[] salida = new String[simbolos.length];
        int n = 0;
        if (traza) System.out.printf("%-8s %-36s %-18s %s%n", "Símbolo", "Acción", "Pila operadores", "Salida");
        for (String s : simbolos) {
            String accion;
            if (s.equals("(")) {
                operadores.apilar(s);
                accion = "se apila (";
            } else if (s.equals(")")) {
                StringBuilder salen = new StringBuilder();
                while (!operadores.tope().equals("(")) {
                    String op = operadores.desapilar();
                    salida[n++] = op;
                    salen.append(vista(op)).append(' ');
                }
                operadores.desapilar();   // se descarta el "("
                accion = "sale " + salen.toString().trim() + " hasta ( ";
            } else if (esOperador(s)) {
                StringBuilder salen = new StringBuilder();
                while (!operadores.estaVacia() && !operadores.tope().equals("(")
                        && precedencia(operadores.tope()) >= precedencia(s)) {
                    String op = operadores.desapilar();
                    salida[n++] = op;
                    salen.append(vista(op)).append(' ');
                }
                operadores.apilar(s);
                accion = (salen.length() > 0 ? "sale " + salen.toString().trim() + "; " : "")
                        + "se apila " + vista(s);
            } else {
                salida[n++] = s;
                accion = "operando a la salida";
            }
            if (traza) System.out.printf("%-8s %-36s %-18s %s%n", vista(s), accion, pilaTexto(operadores), unir(salida, n));
        }
        // Se vacían los operadores que quedaron
        while (!operadores.estaVacia()) salida[n++] = operadores.desapilar();
        if (traza) System.out.printf("%-8s %-36s %-18s %s%n", "fin", "se vacía la pila", "[vacía]", unir(salida, n));

        // Se copia a un arreglo del tamaño exacto (los paréntesis no van a la salida)
        String[] resultado = new String[n];
        for (int i = 0; i < n; i++) resultado[i] = salida[i];
        return resultado;
    }

    // Evalúa una expresión posfija con valores numéricos.
    //  - Número: se apila.
    //  - Operador: se desapila b (tope) y luego a; se apila a <op> b.
    // Al final, el único elemento en la pila es el resultado.
    public static double evaluarPosfija(String[] posfija, boolean traza) {
        Pila<Double> pila = new Pila<>();
        if (traza) System.out.printf("%-8s %-36s %s%n", "Elemento", "Operación", "Pila (tope → fondo)");
        for (String s : posfija) {
            String operacion;
            if (esOperador(s)) {
                double b = pila.desapilar();   // segundo operando (estaba en el tope)
                double a = pila.desapilar();   // primer operando
                double r = s.equals("+") ? a + b : s.equals("-") ? a - b : s.equals("*") ? a * b : a / b;
                pila.apilar(r);
                operacion = num(a) + " " + vista(s) + " " + num(b) + " = " + num(r) + " (se apila)";
            } else {
                pila.apilar(Double.parseDouble(s));
                operacion = "se apila " + s;
            }
            if (traza) System.out.printf("%-8s %-36s %s%n", vista(s), operacion, pilaTexto(pila));
        }
        return pila.desapilar();
    }

    // Une los primeros n símbolos de la salida para mostrarlos en la tabla.
    private static String unir(String[] v, int n) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) sb.append(vista(v[i])).append(' ');
        return sb.toString().trim();
    }

    // Muestra una pila (tope -> fondo) SIN destruirla: se pasa a una auxiliar y se devuelve.
    private static <T> String pilaTexto(Pila<T> p) {
        if (p.estaVacia()) return "[vacía]";
        Pila<T> aux = new Pila<>();
        StringBuilder sb = new StringBuilder();
        while (!p.estaVacia()) {
            T d = p.desapilar();
            aux.apilar(d);
            sb.append(d instanceof Double ? num((Double) d) : vista(d.toString()));
            if (!p.estaVacia()) sb.append(" → ");
        }
        while (!aux.estaVacia()) p.apilar(aux.desapilar());
        return sb.toString();
    }

    // Formatea un número: sin ".0" si es entero (6.0 -> "6", 4.5 -> "4.5").
    public static String num(double d) {
        return d == Math.rint(d) ? String.valueOf((long) d) : String.valueOf(d);
    }
}
