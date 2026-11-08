package co.edu.uniquindio.poo.Model.Punto3;

import co.edu.uniquindio.poo.Model.Estructuras.Cola;

public class Ruta {
    public static boolean traza = false;

    public static Cola<Paquete> seleccionarPaquetes(Cola<Paquete> pendientes, int capacidad) {
        Cola<Paquete> seleccionados = new Cola<>();
        if (capacidad <= 0) return seleccionados;
        // Recorrido 1: urgentes. Recorrido 2: normales, con la capacidad que sobró.
        int restante = procesar(pendientes, seleccionados, Paquete.Tipo.URGENTE, pendientes.tamanio(), capacidad);
        procesar(pendientes, seleccionados, Paquete.Tipo.NORMAL, pendientes.tamanio(), restante);
        return seleccionados;
    }


    private static int procesar(Cola<Paquete> pendientes, Cola<Paquete> seleccionados,
                                Paquete.Tipo tipo, int n, int capacidad) {
        if (n == 0) return capacidad;                       // caso base
        Paquete p = pendientes.desencolar();
        if (p.getTipo() == tipo && p.getPeso() <= capacidad) {
            seleccionados.encolar(p);
            capacidad -= p.getPeso();
            if (traza) System.out.println("  " + p + " -> SELECCIONADO, capacidad = " + capacidad);
        } else {
            pendientes.encolar(p);
            if (traza) {
                String motivo = p.getTipo() != tipo ? "no es " + tipo : "no cabe (" + p.getPeso() + " > " + capacidad + ")";
                System.out.println("  " + p + " -> pendiente (" + motivo + ")");
            }
        }
        return procesar(pendientes, seleccionados, tipo, n - 1, capacidad);   // llamada recursiva
    }


    public static int pesoTotal(Cola<Paquete> cola) {
        return sumar(cola, cola.tamanio());
    }

    private static int sumar(Cola<Paquete> cola, int n) {
        if (n == 0) return 0;
        Paquete p = cola.desencolar();
        cola.encolar(p);
        return p.getPeso() + sumar(cola, n - 1);
    }
}
