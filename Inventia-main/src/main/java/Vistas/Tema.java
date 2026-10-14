package Vistas;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.table.JTableHeader;

/**
 * Apariencia común de todas las vistas. Solo cambia aspecto visual:
 * no toca datos, consultas ni nombres de la base de datos.
 *
 * Uso en cada vista, dentro del constructor:
 *   Tema.instalar();      // antes de initComponents()
 *   initComponents();
 *   Tema.aplicar(this);   // justo después de initComponents()
 *
 * Un componente que lleve la propiedad cliente "tema.omitir" conserva el estilo
 * que la vista le dio (sus hijos sí se estilizan).
 * Una ventana con la propiedad "tema.sinCabecera" en su rootPane no recibe la franja de marca.
 */
public final class Tema {

    public static final String OMITIR = "tema.omitir";
    public static final String SIN_CABECERA = "tema.sinCabecera";

    public static final Color TINTA = new Color(0x1D3B53);
    public static final Color TINTA_SUAVE = new Color(0xB9C6D3);
    public static final Color FONDO = new Color(0xEEF1F4);
    public static final Color SUPERFICIE = Color.WHITE;
    public static final Color BORDE = new Color(0xCBD2D9);
    public static final Color TEXTO = new Color(0x17212B);
    public static final Color TEXTO_SUAVE = new Color(0x5B6773);
    public static final Color PELIGRO = new Color(0xA32A2A);
    public static final Color SELECCION = new Color(0xD6E2EE);
    public static final Color ZEBRA = new Color(0xF6F8FA);

    private static final String FUENTE = "Segoe UI";
    private static boolean instalado = false;

    private Tema() {
    }

    /** Activa FlatLaf y los valores por defecto. Es seguro llamarlo varias veces. */
    public static void instalar() {
        if (instalado) {
            return;
        }
        instalado = true;
        try {
            com.formdev.flatlaf.FlatLaf.setGlobalExtraDefaults(
                    Collections.singletonMap("@accentColor", "#1D3B53"));
            UIManager.put("Button.arc", 6);
            UIManager.put("Component.arc", 6);
            UIManager.put("TextComponent.arc", 6);
            UIManager.put("ScrollBar.thumbArc", 999);
            UIManager.put("ScrollBar.thumbInsets", new Insets(2, 2, 2, 2));
            UIManager.put("Table.showHorizontalLines", true);
            UIManager.put("Table.showVerticalLines", false);
            UIManager.put("Table.intercellSpacing", new Dimension(0, 1));
            UIManager.put("Table.gridColor", new Color(0xE3E7EB));
            UIManager.put("Table.alternateRowColor", ZEBRA);
            UIManager.put("Table.selectionBackground", SELECCION);
            // El buscador conserva el foco, así que la tabla casi siempre está "inactiva":
            // sin estos valores la fila elegida casi no se distingue.
            UIManager.put("Table.selectionInactiveBackground", SELECCION);
            UIManager.put("Table.selectionInactiveForeground", TEXTO);
            UIManager.put("Table.selectionForeground", TEXTO);
            UIManager.put("defaultFont", new Font(FUENTE, Font.PLAIN, 14));
            UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatLightLaf());
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(Tema.class.getName())
                    .log(java.util.logging.Level.WARNING, "No se pudo activar FlatLaf", ex);
        }
    }

    /** Aplica colores, fuentes y espaciado a una ventana y a todos sus componentes. */
    public static void aplicar(JFrame ventana) {
        Container contenido = ventana.getContentPane();
        contenido.setBackground(FONDO);
        recorrer(contenido);

        if (ventana.getRootPane().getClientProperty(SIN_CABECERA) == null) {
            JPanel raiz = new JPanel(new BorderLayout());
            raiz.add(crearCabecera(), BorderLayout.NORTH);
            raiz.add(contenido, BorderLayout.CENTER);
            ventana.setContentPane(raiz);
        }
        if (ventana.getTitle() == null || ventana.getTitle().isEmpty()) {
            ventana.setTitle("Inventia");
        }
        ventana.pack();
        ventana.setMinimumSize(ventana.getPreferredSize());
    }

    /** Fecha de hoy en español, por ejemplo "Miércoles 14 de octubre de 2026". */
    public static String fechaHoy() {
        String fecha = LocalDate.now().format(
                DateTimeFormatter.ofPattern("EEEE d 'de' MMMM 'de' yyyy", Locale.forLanguageTag("es")));
        return Character.toUpperCase(fecha.charAt(0)) + fecha.substring(1);
    }

    /** Franja de marca: nombre de la aplicación y fecha de hoy. */
    private static JPanel crearCabecera() {
        JPanel franja = new JPanel(new BorderLayout());
        franja.setBackground(TINTA);
        franja.setBorder(BorderFactory.createEmptyBorder(11, 20, 11, 20));

        JLabel marca = new JLabel("Inventia");
        marca.setFont(new Font(FUENTE, Font.BOLD, 17));
        marca.setForeground(Color.WHITE);

        JLabel hoy = new JLabel(fechaHoy());
        hoy.setFont(new Font(FUENTE, Font.PLAIN, 13));
        hoy.setForeground(TINTA_SUAVE);

        franja.add(marca, BorderLayout.WEST);
        franja.add(hoy, BorderLayout.EAST);
        return franja;
    }

    private static void recorrer(Container contenedor) {
        for (Component c : contenedor.getComponents()) {
            if (!(c instanceof javax.swing.JComponent)
                    || ((javax.swing.JComponent) c).getClientProperty(OMITIR) == null) {
                estilizar(c);
            }
            if (c instanceof Container) {
                recorrer((Container) c);
            }
        }
    }

    private static void estilizar(Component c) {
        if (c instanceof JButton) {
            estilizarBoton((JButton) c);
        } else if (c instanceof JLabel) {
            estilizarEtiqueta((JLabel) c);
        } else if (c instanceof JTable) {
            estilizarTabla((JTable) c);
        } else if (c instanceof JScrollPane) {
            JScrollPane sp = (JScrollPane) c;
            sp.setBorder(BorderFactory.createLineBorder(BORDE));
            sp.getViewport().setBackground(SUPERFICIE);
        } else if (c instanceof JTextField) {
            c.setFont(conFuente(c.getFont(), Font.PLAIN, 15));
        } else if (c instanceof JPanel) {
            ((JPanel) c).setOpaque(true);
            c.setBackground(FONDO);
        }
    }

    private static void estilizarBoton(JButton b) {
        String texto = b.getText() == null ? "" : b.getText().toLowerCase();
        Color fondo = TINTA;
        Color letra = Color.WHITE;
        boolean contorno = false;
        if (texto.contains("elimin")) {
            fondo = PELIGRO;
        } else if (texto.startsWith("atr")) {
            fondo = SUPERFICIE;
            letra = TINTA;
            contorno = true;
        }
        b.setFont(conFuente(b.getFont(), Font.BOLD, 15));
        b.setBackground(fondo);
        b.setForeground(letra);
        b.setFocusPainted(false);
        b.setBorderPainted(contorno);
        b.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));
        // Botones de símbolo ("->", "<-") son angostos: sin margen lateral para que no se corte el texto.
        b.setMargin(texto.length() <= 3 ? new Insets(6, 2, 6, 2) : new Insets(6, 16, 6, 16));
    }

    private static void estilizarEtiqueta(JLabel l) {
        Font f = l.getFont();
        boolean titulo = f != null && f.getSize() >= 18;
        l.setFont(conFuente(f, titulo ? Font.BOLD : Font.PLAIN, titulo ? 26 : 14));
        l.setForeground(titulo ? TINTA : TEXTO);
    }

    private static void estilizarTabla(JTable t) {
        t.setFont(conFuente(t.getFont(), Font.PLAIN, 15));
        t.setRowHeight(30);
        t.setFillsViewportHeight(true);
        t.setBackground(SUPERFICIE);
        t.setForeground(TEXTO);
        JTableHeader h = t.getTableHeader();
        if (h != null) {
            h.setFont(conFuente(t.getFont(), Font.BOLD, 14));
            h.setBackground(TINTA);
            h.setForeground(Color.WHITE);
            h.setPreferredSize(new Dimension(h.getPreferredSize().width, 36));
            h.setReorderingAllowed(false);
        }
    }

    /** Familia común; la talla original se conserva, con un tope para que nada desborde. */
    private static Font conFuente(Font original, int estilo, int tope) {
        int talla = original == null ? 14 : Math.min(original.getSize(), tope);
        return new Font(FUENTE, estilo, talla);
    }
}
