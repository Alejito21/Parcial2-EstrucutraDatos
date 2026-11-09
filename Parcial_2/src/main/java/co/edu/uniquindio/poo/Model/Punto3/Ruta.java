package co.edu.uniquindio.poo.Model.Punto3;

import co.edu.uniquindio.poo.Model.Estructuras.Cola;

// DOCUMENTACIÓN
// Punto 3: selección de paquetes para una ruta con capacidad limitada.
// Todo el procesamiento es RECURSIVO (no se usan for / while / do-while).
public class Ruta {

    // Si es true, se imprime cada paquete evaluado y si fue seleccionado o quedó pendiente.
    public static boolean traza = false;

    // Selecciona paquetes de 'pendientes' sin pasarse de 'capacidad':
    //  1. Primero se recorren los URGENTES en orden de registro.
    //  2. Después los NORMALES en orden de registro, con la capacidad que sobró.
    // Un paquete se selecciona si su peso <= capacidad restante; si no, queda pendiente.
    // Retorna la cola de seleccionados (urgentes primero, luego normales) y los retira
    // de 'pendientes'. Los que quedan conservan su orden original.
    // Si la capacidad es <= 0, retorna una cola vacía y no toca 'pendientes'.
    public static Cola<Paquete> seleccionarPaquetes(Cola<Paquete> pendientes, int capacidad) {
        Cola<Paquete> seleccionados = new Cola<>();
        if (capacidad <= 0) return seleccionados;
        // Recorrido 1: urgentes. Recorrido 2: normales, con la capacidad que sobró.
        // En cada recorrido se procesan exactamente 'tamanio()' elementos (una vuelta completa).
        int restante = procesar(pendientes, seleccionados, Paquete.Tipo.URGENTE, pendientes.tamanio(), capacidad);
        procesar(pendientes, seleccionados, Paquete.Tipo.NORMAL, pendientes.tamanio(), restante);
        return seleccionados;
    }

    // Recorrido recursivo de una vuelta sobre la cola:
    // se desencola el frente; si es del tipo buscado y cabe, se pasa a 'seleccionados';
    // si no, se vuelve a encolar al final (así el orden relativo de los pendientes se conserva).
    // 'n' = cuántos elementos faltan por revisar en esta vuelta. Retorna la capacidad restante.
    private static int procesar(Cola<Paquete> pendientes, Cola<Paquete> seleccionados,
                                Paquete.Tipo tipo, int n, int capacidad) {
        if (n == 0) return capacidad;                       // caso base: terminó la vuelta
        Paquete p = pendientes.desencolar();
        if (p.getTipo() == tipo && p.getPeso() <= capacidad) {
            seleccionados.encolar(p);
            capacidad -= p.getPeso();
            if (traza) System.out.println("  " + p + " -> SELECCIONADO, capacidad restante = " + capacidad);
        } else {
            pendientes.encolar(p);                          // se rota al final
            if (traza && p.getTipo() == tipo) {
                System.out.println("  " + p + " -> PENDIENTE (no cabe: " + p.getPeso() + " > " + capacidad + ")");
            }
        }
        return procesar(pendientes, seleccionados, tipo, n - 1, capacidad);   // llamada recursiva
    }

    // Suma (recursivamente) el peso de todos los paquetes de una cola sin modificarla:
    // cada paquete se desencola y se vuelve a encolar, dando una vuelta completa.
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
