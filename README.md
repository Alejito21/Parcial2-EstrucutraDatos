# Parcial 2 - Estructuras de Datos

Universidad del Quindío · Ingeniería de Sistemas y Computación.

Proyecto Java (Maven) con las soluciones del parcial. Todas las estructuras son propias
(`Pila` y `Cola` con nodos enlazados); no se usan colecciones de Java en los ejercicios.

## Estructura

```
Parcial_2/src/main/java/co/edu/uniquindio/poo/
├── Main/Main.java                 Ejecuta los datos de prueba y verifica resultados
└── Model/
    ├── Estructuras/  Pila, Cola   Estructuras propias
    ├── Punto1/       Caja, GestionarCajas (retirarCaja), Expresiones (infija→posfija, evaluación)
    ├── Punto2/       Historial (obtenerSegmento)
    ├── Punto3/       Paquete, Ruta (seleccionarPaquetes, recursivo sin ciclos)
    ├── Punto4/       NodoABB, ArbolABB (contarEquiposEnRango, eliminar, recorridos)
    └── Punto5/       NodoAVL, NodoRN, ArbolAVL
```

## Ejecución

Desde IntelliJ: ejecutar `co.edu.uniquindio.poo.Main.Main`.

Desde la terminal (requiere JDK 24 o superior):

```bash
cd Parcial_2
javac -encoding UTF-8 -d out $(find src -name '*.java')
java -cp out co.edu.uniquindio.poo.Main.Main
```

Cada resultado se compara con el valor esperado y se marca `[OK]` o `[FALLA]`.
