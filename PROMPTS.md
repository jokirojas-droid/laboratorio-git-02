# Bitácora de Interacción con IA - Agrotech Llano

**Estudiante:**Johan Kirley Rojas Bobadilla
**Asignatura:** Ingeniería de Software I
**Laboratorio:**  C++ a Java

---

## 1. Configuración del System Prompt

Se configuró la IA con el siguiente System Prompt (copiado textualmente de la guía):

> *"Actúa estrictamente como un Arquitecto de Software Senior y Code Reviewer Exigente de la Universidad de los Llanos. Mi objetivo es refactorizar y dominar la Programación Orientada a Objetos en Java para Sistemas de Escala Agrícola e Industrial."*

**Reglas de Interacción:**  
- PROHIBIDO generar código solución.  
- Método Socrático (responder solo con preguntas guía, diagramas o pistas).  
- Code Review identificando violaciones de encapsulamiento, malas prácticas, acoplamiento.  
- Enfoque industrial: robustez, producción y mantenibilidad.

**Confirmación de la IA:**  
> *"Arquitecto Senior listo. Presenta tu análisis de requerimientos."*

---

## 2. Preguntas Socráticas Realizadas a la IA

### Pregunta 1 (Hito 1 - Encapsulamiento y Concurrencia)

**Mi pregunta:**  
> *"¿Mi modelo de abstracción y encapsulamiento en Java previene mutaciones no deseadas del estado del Sensor en un entorno concurrente?"*

**Respuesta de la IA (resumen del diálogo socrático):**  
La IA no dio una respuesta directa, sino que formuló una serie de preguntas guía para que yo reflexionara:

- ¿Quién puede modificar actualmente el estado interno del Sensor?
- ¿El estado se expone directamente mediante getters que devuelven referencias a objetos mutables?
- Si un getter devuelve un objeto mutable, ¿qué ocurre si el consumidor lo modifica sin pasar por el Sensor?
- ¿Tus atributos del Sensor son private? Si lo son, ¿eso por sí solo garantiza que el estado no pueda mutarse indirectamente?
- ¿Qué campos del Sensor representan estado mutable y cuáles deberían ser inmutables después de la construcción?
- En concurrencia, ¿qué ocurre si dos hilos ejecutan simultáneamente una operación de lectura y otra de modificación?
- ¿Podría un hilo observar un estado parcialmente actualizado?
- ¿Qué mecanismo garantiza la visibilidad de los cambios entre hilos? ¿Y la atomicidad?

**Conclusión del análisis:**  
Mi diseño usa atributos `private` y solo getters (sin setters), lo que previene mutaciones externas **desde el punto de vista de acceso**. Sin embargo, la IA me hizo notar que el encapsulamiento por sí solo **no garantiza seguridad en concurrencia**; se necesitarían mecanismos adicionales como `synchronized`, `volatile` o inmutabilidad total si el sistema fuera multihilo real. Para este laboratorio (entorno secuencial), el diseño es adecuado.

---

### Pregunta 2 (Hito 2 - Polimorfismo vs C++ y Memoria)

**Mi pregunta:**  
> *"¿Cómo gestiona Java el despacho dinámico de métodos (Polimorfismo) internamente a diferencia de las tablas virtuales (vtable) de C++ y qué riesgos de memoria desaparecen con el Garbage Collector?"*

**Respuesta de la IA (resumen del diálogo socrático):**  
La IA me guió a través de un análisis comparativo con preguntas como:

- En Java, si tienes una referencia de tipo `Sensor` que apunta a un objeto de una subclase, ¿qué determina en tiempo de ejecución qué implementación ejecutar?
- En C++, ¿recuerdas qué papel cumple una vtable y dónde suele existir el puntero asociado?
- ¿Java tiene exactamente una estructura equivalente a una vtable? (La IA me hizo notar que la JVM usa estructuras internas, pero el concepto es análogo).
- ¿Qué optimizaciones puede hacer la JVM (ej. inlining) que C++ no puede en tiempo de ejecución?
- En C++, ¿quién es responsable de liberar memoria con `delete`? ¿Qué pasa si olvidas liberar o liberas dos veces?
- En Java, ¿puedes hacer `delete` manual? ¿El Garbage Collector elimina todos los problemas de memoria?

**Conclusión del análisis:**  
- **Polimorfismo:** Java usa tablas virtuales internas (similares a las vtables de C++), pero la JVM puede optimizar con JIT (compilación just-in-time) y realizar inlining de métodos cuando detecta que casi siempre se llama al mismo tipo.  
- **Memoria:** C++ requiere `new`/`delete` manual; Java usa Garbage Collector que libera objetos inalcanzables automáticamente, eliminando:  
  - Memory leaks por `delete` olvidado.  
  - Double-free.  
  - Dangling pointers (punteros a memoria liberada).  
- **Riesgo remanente en Java:** Aún existen "fugas lógicas" cuando se mantienen referencias innecesarias (ej. colecciones estáticas que crecen sin límite).

---

### Pregunta 3 (Hito 3 - Principio de Abierto/Cerrado - OCP)

**Mi pregunta (inferida de la conversación):**  
> *"¿Mi implementación cumple con el Principio de Abierto/Cerrado (OCP) de SOLID si mañana la empresa decide integrar Sensores de Calidad del Aire?"*

**Respuesta de la IA (resumen):**  
La IA me hizo reflexionar sobre si `EstacionMonitoreo` depende de abstracciones o de implementaciones concretas. Mi clase usa `List<Sensor>`, que es una abstracción, y procesa sensores mediante el método polimórfico `tomarLectura()`. Esto significa que **sí cumple OCP**:  
- **Abierto para extensión:** Puedo crear `SensorCalidadAire extends Sensor` sin modificar `EstacionMonitoreo`.  
- **Cerrado para modificación:** `EstacionMonitoreo` no necesita cambios para soportar el nuevo sensor.

La IA también me preguntó si el filtrado (`filtrarCriticos`) podría verse afectado; concluimos que si el umbral es genérico (`double`), el filtro sigue funcionando sin cambios.

---

## 3. Errores de Compilación y Resolución (Registro de Debug)

Durante el desarrollo, enfrenté los siguientes errores típicos de la transición C++ → Java:

| **Error** | **Causa** | **Solución** |
| :--- | :--- | :--- |
| `string cannot be resolved to a type` | En Java la clase es `String` con mayúscula. | Refactorizar todas las variables y parámetros a `String`. |
| `Syntax error on token(s), misplaced construct(s)` | Usé `package lab AI and java;` con espacios. | Eliminé la línea de package (no necesaria para este proyecto). |
| `The public type Sensor must be defined in its own file` | El archivo se llamaba `sensor.java` (minúscula) pero la clase es `Sensor`. | Renombré el archivo a `Sensor.java`. |
| `Sensor cannot be resolved to a type` | El compilador no encontraba la clase padre porque no estaba compilada. | Guardé todos los archivos y reinicié VS Code (limpié el workspace). |
| `Return type for the method is missing` / `This method must return a result of type double` | Faltaba `return` o estaba vacío. | Añadí `return Math.random() * 100;` en los hijos. |
| `Syntax error, insert "}" to complete ClassBody` | Faltaba una llave de cierre en `SensorTemperatura`. | Agregué la llave `}` al final de la clase. |
| `for Sensor sensor : sensors` (sin paréntesis) | En Java el for-each siempre lleva paréntesis. | Corregí a `for (Sensor sensor : sensores)`. |
| `sensor.tomarLectura;` (sin paréntesis) | En Java los métodos siempre se invocan con `()`. | Corregí a `sensor.tomarLectura()`. |

---

## 4. Reflexión Final (Cierre del Laboratorio)

El enfoque socrático de la IA me obligó a **razonar cada línea de código** y a **comparar mentalmente** las diferencias entre C++ y Java, en lugar de recibir soluciones prefabricadas. Esto reforzó mi comprensión de:

- La importancia del encapsulamiento más allá de los modificadores de acceso.
- El funcionamiento interno del polimorfismo y la gestión de memoria en ambos lenguajes.
- El principio OCP y cómo el diseño basado en abstracciones facilita la evolución del software.

La IA actuó estrictamente como un Code Reviewer, identificando oportunidades de mejora sin escribir código por mí. Este proceso simuló fielmente un escenario de trabajo real en la industria.

---

**Fecha de entrega:** [Fecha actual]  
**Repositorio:** [URL de tu repositorio en GitHub]
