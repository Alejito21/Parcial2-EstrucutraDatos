package co.edu.uniquindio.poo.Main;

import co.edu.uniquindio.poo.Model.Estructuras.Cola;
import co.edu.uniquindio.poo.Model.Estructuras.Pila;
import co.edu.uniquindio.poo.Model.Punto1.Caja;
import co.edu.uniquindio.poo.Model.Punto1.Expresiones;
import co.edu.uniquindio.poo.Model.Punto1.GestionarCajas;
import co.edu.uniquindio.poo.Model.Punto2.Historial;
import co.edu.uniquindio.poo.Model.Punto3.Paquete;
import co.edu.uniquindio.poo.Model.Punto3.Ruta;
import co.edu.uniquindio.poo.Model.Punto4.ArbolABB;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

// DOCUMENTACIÓN
// Programa de consola: ejecuta los datos de prueba de cada punto del parcial,
// imprime los resultados pedidos y verifica automáticamente los valores esperados
// ([OK] / [FALLA]). Al final indica si todas las verificaciones pasaron.
public class Main {
    private static int fallos = 0;   // cantidad de verificaciones fallidas

    // Compara un valor obtenido con el esperado e imprime [OK] o [FALLA].
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
        punto2();
        punto3();
        punto4();
        System.out.println("\n" + (fallos == 0 ? "TODAS LAS VERIFICACIONES PASARON" : "VERIFICACIONES FALLIDAS: " + fallos));
    }

    // ------------------------------------------------------------------
    // PUNTO 1: retiro de cajas + evaluación de la expresión
    // ------------------------------------------------------------------
    private static void punto1() {
        titulo("PUNTO 1 - Retiro de cajas y evaluación de expresiones");
        Pila<Caja> pila = new Pila<>();
        pila.apilar(new Caja("C1", 2));
        pila.apilar(new Caja("C2", 4));
        pila.apilar(new Caja("C3", 6));
        pila.apilar(new Caja("C4", 8));
        pila.apilar(new Caja("C5", 3));
        pila.apilar(new Caja("C6", 9));
        pila.apilar(new Caja("C7", 5));
        System.out.println("Pila inicial (tope → fondo): " + pila);
        verificar("pila inicial", pila.toString(), "C7:5 → C6:9 → C5:3 → C4:8 → C3:6 → C2:4 → C1:2");

        // Tres retiros consecutivos sobre la misma pila
        String[] retiros = {"C3", "C4", "C7"};
        String[] esperadas = {
                "C7:5 → C5:3 → C2:4 → C6:9 → C4:8 → C1:2",
                "C7:5 → C2:4 → C1:2 → C5:3 → C6:9",
                "C1:2 → C6:9 → C2:4 → C5:3"};
        for (int i = 0; i < retiros.length; i++) {
            boolean r = GestionarCajas.retirarCaja(pila, retiros[i]);
            System.out.println("retirarCaja(" + retiros[i] + ") = " + r + "  →  " + pila);
            verificar("llamada " + (i + 1), r + " | " + pila, "true | " + esperadas[i]);
        }

        // Casos límite
        verificar("pila vacía retorna false", GestionarCajas.retirarCaja(new Pila<>(), "C1"), false);
        String antes = pila.toString();
        verificar("código inexistente retorna false", GestionarCajas.retirarCaja(pila, "C99"), false);
        verificar("código inexistente deja la pila igual", pila.toString(), antes);

        System.out.println("\nPila final (cima → fondo): " + pila);

        // Se leen los valores de las 4 cajas finales (cima → fondo) sin destruir la pila:
        // se pasan a una auxiliar y luego se devuelven.
        int[] v = new int[4];
        Pila<Caja> aux = new Pila<>();
        for (int i = 0; i < 4; i++) {
            Caja c = pila.desapilar();
            v[i] = c.getValor();
            aux.apilar(c);
        }
        while (!aux.estaVacia()) pila.apilar(aux.desapilar());

        double A = v[0];               // valor de la primera caja
        double B = v[1] / A;           // segunda caja / A (con decimales)
        double C = v[2] + A;           // tercera caja + A
        double D = (v[3] + C) * A;     // (cuarta caja + C) * A
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
        StringBuilder sv = new StringBuilder();
        for (String s : valores) sv.append(s).append(' ');
        System.out.println("Posfija con valores: " + sv.toString().trim());

        System.out.println("\nEvaluación de la posfija con valores:");
        double resultado = Expresiones.evaluarPosfija(valores, true);
        System.out.println("Resultado final = " + Expresiones.num(resultado));
        verificar("resultado de la expresión", resultado, 84.0);
    }

    // ------------------------------------------------------------------
    // PUNTO 2: segmento del historial
    // ------------------------------------------------------------------
    private static void punto2() {
        titulo("PUNTO 2 - Obtener un segmento del historial");
        Pila<String> historial = new Pila<>();
        for (int i = 1; i <= 6; i++) historial.apilar("A" + i);
        System.out.println("Original (tope → fondo): " + historial);

        Pila<String> seg = Historial.obtenerSegmento(historial, 2, 4);
        System.out.println("obtenerSegmento(2, 4) → " + seg);
        verificar("segmento 2..4", seg.toString(), "A5 → A4 → A3");
        verificar("la original no cambia", historial.toString(), "A6 → A5 → A4 → A3 → A2 → A1");

        verificar("inicio = fin = 3", Historial.obtenerSegmento(historial, 3, 3).toString(), "A4");
        verificar("segmento completo 1..6", Historial.obtenerSegmento(historial, 1, 6).toString(), historial.toString());
        verificar("inicio < 1 → vacía", Historial.obtenerSegmento(historial, 0, 3).estaVacia(), true);
        verificar("inicio > fin → vacía", Historial.obtenerSegmento(historial, 4, 2).estaVacia(), true);
        verificar("fin > tamaño → vacía", Historial.obtenerSegmento(historial, 2, 7).estaVacia(), true);
        verificar("la original sigue igual", historial.toString(), "A6 → A5 → A4 → A3 → A2 → A1");
    }

    // ------------------------------------------------------------------
    // PUNTO 3: selección de paquetes (recursiva)
    // ------------------------------------------------------------------
    private static Cola<Paquete> colaDePrueba() {
        Cola<Paquete> cola = new Cola<>();
        cola.encolar(new Paquete("P1", Paquete.Tipo.NORMAL, 4));
        cola.encolar(new Paquete("P2", Paquete.Tipo.URGENTE, 7));
        cola.encolar(new Paquete("P3", Paquete.Tipo.NORMAL, 3));
        cola.encolar(new Paquete("P4", Paquete.Tipo.URGENTE, 5));
        cola.encolar(new Paquete("P5", Paquete.Tipo.NORMAL, 6));
        cola.encolar(new Paquete("P6", Paquete.Tipo.URGENTE, 4));
        cola.encolar(new Paquete("P7", Paquete.Tipo.NORMAL, 2));
        return cola;
    }

    private static void punto3() {
        titulo("PUNTO 3 - Selección de solicitudes para una jornada");
        Cola<Paquete> pendientes = colaDePrueba();
        int capacidad = 14;
        System.out.println("Cola inicial (frente → final): " + pendientes);
        System.out.println("Capacidad disponible: " + capacidad + " kg");

        System.out.println("\nPaquetes evaluados (primero urgentes, luego normales):");
        Ruta.traza = true;
        Cola<Paquete> seleccionados = Ruta.seleccionarPaquetes(pendientes, capacidad);
        Ruta.traza = false;
        int restante = capacidad - Ruta.pesoTotal(seleccionados);

        System.out.println("\nCola retornada (seleccionados): " + seleccionados);
        System.out.println("Pendientes después de la selección: " + pendientes);
        System.out.println("Capacidad que quedó disponible: " + restante + " kg");
        verificar("seleccionados", seleccionados.toString(), "P2(URG,7kg), P4(URG,5kg), P7(NOR,2kg)");
        verificar("pendientes", pendientes.toString(), "P1(NOR,4kg), P3(NOR,3kg), P5(NOR,6kg), P6(URG,4kg)");
        verificar("capacidad restante", restante, 0);

        Cola<Paquete> otra = colaDePrueba();
        String antes = otra.toString();
        verificar("capacidad 0 → cola vacía", Ruta.seleccionarPaquetes(otra, 0).estaVacia(), true);
        verificar("capacidad 0 → pendientes sin cambios", otra.toString(), antes);
    }

    // ------------------------------------------------------------------
    // PUNTO 4: ABB, consulta por rango y eliminación
    // ------------------------------------------------------------------
    private static void punto4() {
        titulo("PUNTO 4 - Árbol binario de búsqueda");
        ArbolABB abb = new ArbolABB();
        int[] claves = {41, 20, 52, 16, 6, 13, 12, 60, 22, 47, 50, 60, 43, 44};
        for (int c : claves) {
            if (!abb.insertar(c)) System.out.println("(" + c + " repetido: no se inserta)");
        }
        System.out.println("Árbol (acostado, la raíz a la izquierda):");
        abb.imprimir();
        System.out.println("Inorden:   " + abb.inorden());
        System.out.println("Preorden:  " + abb.preorden());
        System.out.println("Postorden: " + abb.postorden());
        System.out.println("Amplitud:  " + abb.amplitud());
        verificar("inorden", abb.inorden(), "6 12 13 16 20 22 41 43 44 47 50 52 60");
        verificar("preorden", abb.preorden(), "41 20 16 6 13 12 22 52 47 43 44 50 60");
        verificar("postorden", abb.postorden(), "12 13 6 16 22 20 44 43 50 47 60 52 41");
        verificar("amplitud", abb.amplitud(), "41 20 52 16 22 47 60 6 43 50 13 44 12");

        // a) consulta por rango
        System.out.println("\ncontarEquiposEnRango(15, 50) = " + ArbolABB.contarEquiposEnRango(abb.getRaiz(), 15, 50));
        verificar("rango [15, 50]", ArbolABB.contarEquiposEnRango(abb.getRaiz(), 15, 50), 8);
        verificar("rango [1, 100]", ArbolABB.contarEquiposEnRango(abb.getRaiz(), 1, 100), 13);
        verificar("rango [41, 41]", ArbolABB.contarEquiposEnRango(abb.getRaiz(), 41, 41), 1);
        verificar("inferior > superior → 0", ArbolABB.contarEquiposEnRango(abb.getRaiz(), 50, 15), 0);
        verificar("árbol vacío → 0", ArbolABB.contarEquiposEnRango(null, 1, 100), 0);

        // b) eliminación de 41 (opción: predecesor inorden = mayor del subárbol izquierdo = 22)
        System.out.println("\nEliminar 41 (opción: predecesor inorden, mayor del subárbol izquierdo):");
        abb.eliminar(41);
        abb.imprimir();
        System.out.println("Inorden:   " + abb.inorden());
        System.out.println("Preorden:  " + abb.preorden());
        verificar("nueva raíz", abb.getRaiz().getClave(), 22);
        verificar("inorden tras eliminar", abb.inorden(), "6 12 13 16 20 22 43 44 47 50 52 60");
        verificar("preorden tras eliminar", abb.preorden(), "22 20 16 6 13 12 52 47 43 44 50 60");
    }
}
