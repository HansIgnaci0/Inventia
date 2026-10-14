# AGENTS.md

Instrucciones para cualquier agente de IA (y persona) que trabaje en este repositorio.

## Contexto

Inventia es una aplicación Java Swing (inventario, ventas y pedidos) que usa MySQL.
El sistema **está en producción** y la base de datos productiva ya contiene datos reales.
En este equipo de desarrollo **no existe acceso a la base de datos**. Solo se puede comprobar
la parte visual y la lógica que no consulta la BD. Todo lo demás se prueba en el entorno productivo.

El trabajo actual es corregir errores y mejorar la interfaz. No se rediseña el modelo de datos.

## Reglas inmutables sobre la base de datos

1. **La base de datos no se modifica nunca.** No se crean, renombran ni eliminan tablas, columnas,
   relaciones, índices, restricciones, vistas ni procedimientos. No se generan scripts de migración,
   `ALTER`, `DROP` ni `CREATE` para la base productiva.
2. **Los nombres de tablas, columnas y relaciones son inmutables.** Esto incluye cada nombre que
   aparece dentro de un texto SQL o de un `rs.getXxx("columna")` en el código. Nombres actuales
   que se usan, entre otros:
   - Tablas: `producto`, `detalleventa`, `pedidos`, `ingresos_dia`.
   - Columnas: `codigoProducto`, `nombreProducto`, `precioUnitarioProducto`, `stockProducto`, `tipo`,
     `idProducto`, `idProveedor`, `idTrabajador`, `cantidadSolicitada`, `fechaSolicitud`,
     `totalVenta`, `fecha`, `hora`, `totalDia`.
   - Valores de datos que el código compara: `'GRANEL'`, `'NORMAL'`, y el formato de código `GR-0001`.
3. **No "corregir" nombres que parecen mal escritos o inconsistentes.** Si una columna se llama
   distinto en dos métodos de `ConexionBD.java` (por ejemplo `ID_PRODUCTO` y `idProducto`), no se
   unifica. No se sabe cuál nombre existe en producción. Se avisa al usuario y se deja como está.
4. **No se cambia el significado de una consulta SQL**: ni las columnas que lee o escribe, ni los
   filtros, ni los `JOIN`, ni el orden de los parámetros `?`. Se puede cambiar el código Java que
   rodea la consulta (por ejemplo cerrar recursos) solo si el SQL queda idéntico y el usuario lo
   aprueba.
5. **Los datos de conexión** (`dbcamila`, `localhost`, `3306`, usuario y contraseña) no se cambian
   sin que el usuario lo pida de forma explícita.
6. Si una tarea parece exigir un cambio en la BD o en un nombre de tabla o columna, **se detiene el
   trabajo y se pregunta al usuario**. Nunca se hace por cuenta propia.

## Qué sí se puede cambiar

- Apariencia de las vistas en `src/main/java/Vistas/`: colores, fuentes, bordes, tamaños, espaciado,
  textos visibles, iconos y tema (FlatLaf).
- Corrección de errores de lógica en Java que no toquen nombres ni sentido del SQL.
- Archivos de apoyo para el desarrollo local (`ejecutar.ps1`, este archivo, `.gitignore`).

## Cuidados con el código de las vistas

- Las vistas fueron creadas con NetBeans. Cada vista tiene un `.java` y un `.form`.
  El bloque `initComponents()` está marcado como generado. NetBeans puede volver a escribirlo.
  Si se edita a mano el `.java`, hay que mantener el `.form` coherente o avisar al usuario.
- La apariencia común vive en `Vistas/Tema.java` (FlatLaf, colores, fuentes, tablas, botones).
  Cada vista llama `Tema.instalar()` antes de `initComponents()` y `Tema.aplicar(this)` después.
  Para cambiar el estilo de todas las vistas, se edita `Tema.java`, no cada vista.
- `Ventas` y `EntradaProductos` reordenan sus componentes en `disenarVenta()` y `disenarEntrada()`
  (tarjetas, formato de pesos, contadores, panel de producto seleccionado). Esas vistas no usan
  `Tema.aplicar`. Sus botones conservan los `ActionListener` originales. Las piezas visuales
  compartidas (cabecera, tablas claras, botones de acción) están en `Estilo.java`.
- Los avisos, confirmaciones, formularios y diálogos de texto usan `Aviso.mostrar(...)`,
  `Aviso.confirmar(...)` y `Aviso.pedirTexto(...)` (panel de vidrio que sube desde abajo). Tienen
  las mismas firmas que `JOptionPane`. No se vuelve a usar `JOptionPane.show...`. `Aviso.mostrar`
  deduce el tipo (error, advertencia o éxito) del texto del mensaje.
  `ConexionBD` también muestra sus errores con `Aviso`; el texto de cada mensaje no se cambió.
  `Aviso.modoPrueba` solo lo usan las herramientas de `dev/`.
  `Aviso` usa una ventana opaca con esquinas redondeadas (`setShape`). No volver a usar transparencia
  por píxel (`setBackground` con alfa) ni `setOpacity`: en este equipo dejaba el panel sin pintar.
- La tabla de `EntradaProductos` solo tiene Producto, Precio y Stock, porque `listarEntradaProducto`
  solo consulta esas columnas. Mostrar código o tipo (unidad/granel) exige cambiar ese SQL: no se hace.
- `Mainn`, `Login` y `Ventas` reordenan sus componentes en `disenarMenu()`, `disenarLogin()` y `disenarVenta()`, fuera del
  bloque generado. Si se abren y se guardan en NetBeans, el `.form` conserva el diseño antiguo.
- No se renombran las variables de los componentes ni los métodos de eventos
  (`jButton1ActionPerformed`, etc.) sin actualizar todas sus referencias.
- Las vistas `Login` y `Mainn` y el resto se enlazan entre sí con `new Vista().setVisible(true)`
  y `dispose()`. No se rompe esa navegación.

## Cómo probar sin base de datos

- `ejecutar.ps1 <Vista>` compila y abre una vista. Sin BD, los botones que consultan datos
  muestran un error de conexión. Eso es esperado y no es un fallo de la interfaz.
- `ejecutar.ps1 -Captura` abre cada vista, cierra los avisos de error y guarda un PNG por vista en
  `capturas/`. Las filas de ejemplo de esas imágenes solo existen en `dev/Captura.java`.
  Un agente puede abrir los PNG para comprobar un cambio visual.
- `ejecutar.ps1 -Avisos` muestra cada tipo de aviso en pantalla real y guarda un PNG de cada uno
  en `capturas/` (se verán ventanas moverse unos segundos). También imprime la trayectoria de la
  animación de subida.
- `empaquetar.ps1` compila y genera `dist/Inventia_1.2.0.jar`, un único `.jar` ejecutable con MySQL, FlatLaf
  y jcalendar dentro (clase principal `Vistas.Mainn`). No incluye las herramientas de `dev/`.
  Se ejecuta con `java -jar dist\Inventia.jar` y requiere Java 21 o superior.
- No se agregan datos falsos, bases de datos locales ni imitaciones de la BD al código de producción.

## Seguridad

- El código contiene credenciales de la BD escritas en `ConexionBD.java`. No se copian a otros
  archivos, a mensajes de commit ni a documentación.
- El repositorio de GitHub debe mantenerse **privado** mientras esas credenciales sigan en el código.
