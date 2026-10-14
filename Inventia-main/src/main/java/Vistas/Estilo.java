package Vistas;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

/**
 * Piezas visuales compartidas por las vistas rediseñadas (Ventas, EntradaProductos):
 * cabecera, tablas claras, formato de pesos, botones de acción y pastillas.
 * Solo aspecto: no toca datos ni consultas.
 */
public final class Estilo {

    public static final Color AZUL = new Color(0x2F6DB5);
    public static final Color LINEA = new Color(0xE1E5EB);
    public static final Color ROJO = new Color(0xC62828);
    public static final Color ROJO_BORDE = new Color(0xF0C4C4);

    private static final NumberFormat FORMATO_MILES =
            NumberFormat.getIntegerInstance(Locale.forLanguageTag("es-CL"));
    private static final DecimalFormat FORMATO_CANTIDAD = new DecimalFormat("0.###",
            DecimalFormatSymbols.getInstance(Locale.forLanguageTag("es-CL")));

    public static final int PRIMARIO = 0;
    public static final int SECUNDARIO = 1;
    public static final int PELIGRO = 2;

    private Estilo() {
    }

    // ---------------------------------------------------------------- formato

    /** Número con separador de miles: 1.248. */
    public static String miles(int valor) {
        return FORMATO_MILES.format(valor);
    }

    /** Valor en pesos con separador de miles: $24.520. */
    public static String pesos(int valor) {
        return "$" + FORMATO_MILES.format(valor);
    }

    /** Igual que pesos(int), aceptando un número o un texto numérico; otro texto se deja igual. */
    public static String pesos(Object valor) {
        if (valor instanceof Number) {
            return pesos(((Number) valor).intValue());
        }
        if (valor != null) {
            try {
                return pesos(Integer.parseInt(valor.toString().trim()));
            } catch (NumberFormatException e) {
                return valor.toString();
            }
        }
        return "";
    }

    // ------------------------------------------------------------- estructura

    /** Cabecera azul oscura: botón Atrás (opcional, puede ser null), título y fecha de hoy. */
    public static JPanel cabecera(JButton atras, JLabel titulo, String textoTitulo) {
        if (atras != null) {
            atras.setText("Atrás");
            atras.setIcon(Iconos.flechaIzquierda(Color.WHITE, 16));
            atras.setIconTextGap(8);
            atras.setFont(new Font("Segoe UI", Font.BOLD, 15));
            atras.setForeground(Color.WHITE);
            atras.setBackground(new Color(0x2A4A66));
            atras.setFocusPainted(false);
            atras.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(new Color(0x4A6785), 1, true),
                    BorderFactory.createEmptyBorder(7, 14, 7, 16)));
            atras.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }

        titulo.setText(textoTitulo);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titulo.setForeground(Color.WHITE);

        JPanel izquierda = new JPanel(new FlowLayout(FlowLayout.LEFT, 18, 0));
        izquierda.setOpaque(false);
        if (atras != null) {
            izquierda.add(atras);
        }
        izquierda.add(titulo);

        JLabel hoy = new JLabel(Tema.fechaHoy());
        hoy.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        hoy.setForeground(Tema.TINTA_SUAVE);

        JPanel cabecera = new JPanel(new BorderLayout());
        cabecera.setBackground(Tema.TINTA);
        cabecera.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 24));
        cabecera.add(izquierda, BorderLayout.WEST);
        cabecera.add(hoy, BorderLayout.EAST);
        return cabecera;
    }

    public static JPanel tituloConPastilla(String texto, Pastilla pastilla) {
        JLabel titulo = new JLabel(texto);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titulo.setForeground(Tema.TEXTO);
        JPanel fila = new JPanel(new GridBagLayout());
        fila.setOpaque(false);
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.WEST;
        fila.add(titulo, c);
        c.insets = new Insets(0, 10, 0, 0);
        fila.add(pastilla, c);
        return fila;
    }

    public static JLabel textoPie(String texto) {
        JLabel l = new JLabel(texto);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        l.setForeground(Tema.TEXTO_SUAVE);
        return l;
    }

    /** Cuadro gris claro con una etiqueta pequeña arriba y un valor destacado debajo. */
    public static JPanel ficha(String etiqueta, JLabel valor) {
        JPanel p = new JPanel(new BorderLayout(0, 2)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0xF8F9FB));
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
                g2.setColor(new Color(0xE9ECF0));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
                g2.dispose();
            }
        };
        p.setOpaque(false);
        p.setBorder(BorderFactory.createEmptyBorder(10, 14, 12, 14));
        JLabel titulo = new JLabel(etiqueta);
        titulo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        titulo.setForeground(Tema.TEXTO_SUAVE);
        valor.setFont(new Font("Segoe UI", Font.BOLD, 20));
        valor.setForeground(Tema.TEXTO);
        p.add(titulo, BorderLayout.NORTH);
        p.add(valor, BorderLayout.CENTER);
        return p;
    }

    // ----------------------------------------------------------------- tablas

    /** Tabla clara con filas altas y líneas finas, dentro de su marco. */
    public static void tabla(JTable t, JScrollPane marco) {
        t.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        t.setRowHeight(40);
        t.setShowHorizontalLines(true);
        t.setShowVerticalLines(false);
        t.setIntercellSpacing(new Dimension(0, 1));
        t.setGridColor(new Color(0xEEF0F3));
        t.setBackground(Color.WHITE);
        t.setForeground(Tema.TEXTO);
        t.setSelectionBackground(new Color(0xD9E6F5));
        t.setSelectionForeground(Tema.TEXTO);
        t.setFillsViewportHeight(true);
        t.getTableHeader().setReorderingAllowed(false);
        t.getTableHeader().setPreferredSize(new Dimension(10, 42));
        marco.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, LINEA));
        marco.getViewport().setBackground(Color.WHITE);
    }

    /** Textos de encabezado y anchos; un texto null deja la columna oculta. */
    public static void columnas(JTable t, String[] titulos, int[] anchos) {
        int total = t.getColumnModel().getColumnCount();
        for (int i = 0; i < titulos.length && i < total; i++) {
            TableColumn col = t.getColumnModel().getColumn(i);
            if (titulos[i] == null) {
                col.setMinWidth(0);
                col.setMaxWidth(0);
                col.setWidth(0);
                continue;
            }
            col.setHeaderValue(titulos[i]);
            col.setPreferredWidth(anchos[i]);
            int alineacion = SwingConstants.LEFT;
            if (i == total - 1 || titulos[i].startsWith("Precio")) {
                alineacion = SwingConstants.RIGHT;
            } else if (titulos[i].equals("Cantidad")) {
                alineacion = SwingConstants.CENTER;
            }
            col.setHeaderRenderer(new Encabezado(alineacion));
        }
    }

    // ---------------------------------------------------------------- vistas

    /** Vista simple de un proveedor: cabecera con su nombre y una tarjeta con la tabla de pedidos. */
    public static void vistaProveedor(javax.swing.JFrame f, JLabel titulo, JTable tabla, JScrollPane marco) {
        String nombre = titulo.getText();
        f.setTitle("Pedido – " + nombre);
        JPanel cabecera = cabecera(null, titulo, nombre);

        Pastilla contador = new Pastilla();
        tabla(tabla, marco);
        marco.setPreferredSize(new Dimension(420, 380));
        columnas(tabla, new String[]{"Producto", "Cantidad"}, new int[]{290, 130});
        tabla.getColumnModel().getColumn(0).setCellRenderer(new Celda(Celda.TEXTO, SwingConstants.LEFT, false, Tema.TEXTO));
        tabla.getColumnModel().getColumn(1).setCellRenderer(new Celda(Celda.TEXTO, SwingConstants.RIGHT, true, Tema.TEXTO));

        JPanel encabezado = new JPanel(new BorderLayout());
        encabezado.setOpaque(false);
        encabezado.setBorder(BorderFactory.createEmptyBorder(16, 14, 16, 14));
        encabezado.add(tituloConPastilla("Productos pedidos", contador), BorderLayout.WEST);

        Superficie tarjeta = new Superficie(new BorderLayout());
        tarjeta.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        tarjeta.add(encabezado, BorderLayout.NORTH);
        tarjeta.add(marco, BorderLayout.CENTER);

        JPanel cuerpo = new JPanel(new BorderLayout());
        cuerpo.setBackground(Tema.FONDO);
        cuerpo.setBorder(BorderFactory.createEmptyBorder(22, 22, 22, 22));
        cuerpo.add(tarjeta, BorderLayout.CENTER);

        JPanel raiz = new JPanel(new BorderLayout());
        raiz.add(cabecera, BorderLayout.NORTH);
        raiz.add(cuerpo, BorderLayout.CENTER);
        f.setContentPane(raiz);

        Runnable actualizar = () -> {
            int n = tabla.getModel().getRowCount();
            contador.setText(n == 1 ? "1 producto" : n + " productos");
        };
        tabla.getModel().addTableModelListener(e -> javax.swing.SwingUtilities.invokeLater(actualizar));
        actualizar.run();

        f.pack();
        f.setMinimumSize(f.getPreferredSize());
    }

    // ---------------------------------------------------------------- botones

    /** Botón ancho con icono a la izquierda: primario (azul), secundario (blanco) o de peligro (rojo). */
    public static void botonAccion(JButton b, Icon icono, String texto, int variante) {
        Color fondo = Color.WHITE;
        Color letra = Tema.TEXTO;
        Color borde = LINEA;
        if (variante == PRIMARIO) {
            fondo = AZUL;
            letra = Color.WHITE;
        } else if (variante == PELIGRO) {
            letra = ROJO;
            borde = ROJO_BORDE;
        }
        b.setText(texto);
        b.setIcon(icono);
        b.setIconTextGap(14);
        b.setHorizontalAlignment(SwingConstants.LEFT);
        b.setFont(new Font("Segoe UI", Font.BOLD, 16));
        b.setForeground(letra);
        b.setBackground(fondo);
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setPreferredSize(new Dimension(10, 56));
        if (variante == PRIMARIO) {
            b.setBorderPainted(false);
            b.setMargin(new Insets(0, 20, 0, 20));
        } else {
            b.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(borde, 1, true),
                    BorderFactory.createEmptyBorder(0, 20, 0, 20)));
        }
    }

    // ------------------------------------------------------------- componentes

    /** Etiqueta redondeada con un número o conteo, junto al título de una tarjeta. */
    public static final class Pastilla extends JLabel {

        public Pastilla() {
            setOpaque(false);
            setFont(new Font("Segoe UI", Font.PLAIN, 13));
            setForeground(Tema.TEXTO_SUAVE);
            setBorder(BorderFactory.createEmptyBorder(3, 10, 3, 10));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(0xEFF1F5));
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), getHeight(), getHeight());
            g2.dispose();
            super.paintComponent(g);
        }
    }

    /** Encabezado de tabla claro, con el texto alineado igual que su columna. */
    public static final class Encabezado extends DefaultTableCellRenderer {

        public Encabezado(int alineacion) {
            setHorizontalAlignment(alineacion);
            setFont(new Font("Segoe UI", Font.PLAIN, 14));
            setForeground(Tema.TEXTO_SUAVE);
            setBackground(new Color(0xF8F9FB));
            setOpaque(true);
            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 1, 0, LINEA),
                    BorderFactory.createEmptyBorder(0, 14, 0, 14)));
        }

        @Override
        public Component getTableCellRendererComponent(JTable tabla, Object valor,
                boolean seleccionada, boolean foco, int fila, int columna) {
            setText(valor == null ? "" : valor.toString());
            return this;
        }
    }

    /** Celda con formato: texto, pesos ($1.990) o cantidad (3 / 1,35). */
    public static final class Celda extends DefaultTableCellRenderer {

        public static final int TEXTO = 0;
        public static final int PESOS = 1;
        public static final int CANTIDAD = 2;

        private final int tipo;
        private final boolean negrita;
        private final Color color;

        public Celda(int tipo, int alineacion, boolean negrita, Color color) {
            this.tipo = tipo;
            this.negrita = negrita;
            this.color = color;
            setHorizontalAlignment(alineacion);
        }

        @Override
        public Component getTableCellRendererComponent(JTable tabla, Object valor,
                boolean seleccionada, boolean foco, int fila, int columna) {
            super.getTableCellRendererComponent(tabla, valor, seleccionada, false, fila, columna);
            if (tipo == PESOS) {
                setText(pesos(valor));
            } else if (tipo == TEXTO && valor instanceof java.sql.Time) {
                setText(valor.toString().substring(0, 5)); // HH:mm
            } else if (tipo == CANTIDAD && valor instanceof Number) {
                setText(FORMATO_CANTIDAD.format(((Number) valor).doubleValue()));
            }
            setForeground(color);
            setFont(new Font("Segoe UI", negrita ? Font.BOLD : Font.PLAIN, 15));
            setBorder(BorderFactory.createEmptyBorder(0, 14, 0, 14));
            return this;
        }
    }
}
