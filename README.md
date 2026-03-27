#  To-Do App — Java Swing

Aplicación de escritorio desarrollada en **Java Swing** para gestionar una lista de tareas. Los datos se mantienen en memoria durante la sesión (no requiere base de datos).

---

## Cómo ejecutar el proyecto

### Requisitos previos

| Herramienta | Versión mínima |
|-------------|---------------|
| Java JDK    | 11            |
| Maven       | 3.6+          |

### Pasos
Opción 1 — Maven (recomendado)
```bash
# 1. Clonar o descomprimir el proyecto
git clone <https://github.com/marceldgr4/TestJavaSwing.git>
cd testJavaSwing

# 2. Compilar el proyecto
mvn clean compile

# 3. Ejecutar la aplicación
mvn exec:java -Dexec.mainClass="com.app.App"
```

> **Alternativa con IDE:** Importar como proyecto Maven en IntelliJ IDEA, Eclipse o NetBeans y ejecutar la clase `com.app.App`.

---
## Opción 2 —  Ejecutable .jar y correrlo
```
Buscar selecionar el archivo testJavaSwing.jar.
se encutra en la raiz de proyecto
testJavaSwing/
├── testJavaSwing.jar 
```
---
##  Estructura del proyecto

```
testJavaSwing/
├── testJavaSwing.jar                    #ejecutable java.
├── pom.xml                              # Configuración Maven + plugins
├── README.md
├── src/
│   ├── main/
│   │   ├── java/com/app/
│   │   │   ├── App.java                 # Punto de entrada (main)
│   │   │   ├── Model/
│   │   │   │   ├── Task.java            # Entidad de tarea
│   │   │   │   └── TaskStatus.java      # Enum de estados
│   │   │   ├── Service/
│   │   │   │   └── TaskService.java     # Lógica de negocio
│   │   │   └── UI/
│   │   │       ├── MainFrame.java       # Ventana principal
│   │   │       ├── TaskFormPanel.java   # Formulario de nueva tarea
│   │   │       └── TaskTablePanel.java  # Tabla + filtro + acciones
│   │   └── resources/
│   │       └── META-INF/
│   │           └── MANIFEST.MF          # Define la clase principal del JAR
│   └── test/
│       └── java/com/app/
│           └── AppTest.java
├── .idea/
│   └── artifacts/
│       └── testJavaSwing_jar.xml        # Config. artifact IntelliJ
└── target/                              # Generado por Maven (no editar)
    └── testJavaSwing-1.0-SNAPSHOT.jar   # JAR ejecutable
```

---

##  Funcionalidades implementadas

### Requerimientos obligatorios

- [x] **Registro de tareas** con campos: Título (`JTextField`), Descripción (`JTextArea`) y Estado (`JComboBox`)
- [x] **Validación de título obligatorio** — muestra error con `JOptionPane` si está vacío
- [x] **Visualización en tabla** (`JTable`) con columnas: ID, Título, Descripción y Estado
- [x] **Eliminar tarea seleccionada** con validación de selección
- [x] **Cambiar estado** de una tarea seleccionada mediante diálogo de selección

### Requerimientos extra (opcionales)

- [x] **Filtro por estado** — combo box con opciones: Todas, Pendiente, En Proceso, Completado
- [x] **Confirmación antes de eliminar** — diálogo `YES/NO` antes de proceder con la eliminación
- [x] **Código comentado y limpio** — separación en capas Model / Service / UI

---

##  Decisiones técnicas

### Arquitectura en capas
Se adoptó una separación en tres capas para mantener el código organizado y con responsabilidades claras:

- **Model** — representa los datos (`Task`, `TaskStatus`)
- **Service** — contiene la lógica de negocio (`TaskService`) independiente de la interfaz gráfica
- **UI** — vistas Swing divididas en componentes reutilizables (`TaskFormPanel`, `TaskTablePanel`)

### Comunicación entre paneles
`TaskFormPanel` recibe un `Consumer<Void>` como callback, de modo que al agregar una tarea notifica a `TaskTablePanel` para que refresque la tabla sin crear acoplamiento directo entre ambos paneles.

### Gestión de IDs
Se utiliza un contador estático en `Task` para asignar IDs únicos de forma incremental durante la sesión.

### Look and Feel del sistema
Se aplica `UIManager.getSystemLookAndFeelClassName()` para que la aplicación adopte la apariencia nativa del sistema operativo del usuario.

### Sin persistencia
Por requerimiento del test, los datos existen únicamente en memoria (`ArrayList` dentro de `TaskService`). Al cerrar la aplicación los datos se pierden.

---

##  Funcionalidades en detalle

| Acción | Comportamiento |
|--------|---------------|
| Agregar tarea sin título | Muestra error con `JOptionPane` |
| Eliminar sin seleccionar fila | Muestra advertencia |
| Cambiar estado sin seleccionar fila | Muestra advertencia |
| Confirmar eliminación | Diálogo de confirmación Sí/No |
| Filtrar por estado | Actualiza la tabla en tiempo real |
| Cambiar estado | Diálogo con los tres estados disponibles |
