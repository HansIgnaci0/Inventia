package Vistas;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Robot;
import java.awt.Shape;
import java.awt.Window;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.border.LineBorder;

/**
 * Avisos, confirmaciones y formularios sobre un panel de vidrio esmerilado que sube desde
 * la parte baja de la pantalla hasta el centro de la ventana.
 *
 * Reemplaza a JOptionPane con firmas equivalentes para que el código que lo usa no cambie:
 *   Aviso.mostrar(padre, mensaje)                      ~ JOptionPane.showMessageDialog
 *   Aviso.confirmar(padre, mensaje, titulo, opc, tipo) ~ JOptionPane.showConfirmDialog
 *   Aviso.pedirTexto(padre, mensaje)                   ~ JOptionPane.showInputDialog
 *
 * Si el mensaje de confirmación es un componente (por ejemplo un panel con campos), se
 * muestra como formulario y devuelve OK_OPTION o CANCEL_OPTION.
 * Solo aspecto: no toca datos ni consultas.
 */
public final class Aviso {

    public enum Tipo { ERROR, ADVERTENCIA, INFO, PREGUNTA, EXITO }

    /** Solo para herramientas de desarrollo (capturas sin pantalla): no muestra nada. */
    public static volatile boolean modoPrueba = false;

    private static final int RADIO = 28;
    private static final int ANCHO_TEXTO = 380;
    private static final double DURACION_ENTRADA_MS = 460;
    private static final double DURACION_SALIDA_MS = 170;

    private Aviso() {
    }

    // ---------------------------------------------------------------- API

    /** Mensaje con un solo botón. El tipo (error, advertencia o éxito) se deduce del texto. */
    public static void mostrar(Component padre, Object mensaje) {
        String texto = String.valueOf(mensaje);
        mostrar(padre, tipoSegunTexto(texto), texto);
    }

    public static void mostrar(Component padre, Tipo tipo, String mensaje) {
        if (modoPrueba) {
            return;
        }
        abrir(padre, tipo, titulo(tipo), etiquetaMensaje(limpiar(mensaje)), "Aceptar", null, false, null);
    }

    public static int confirmar(Component padre, Object mensaje) {
        return confirmar(padre, mensaje, "Confirmar", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
    }

    public static int confirmar(Component padre, Object mensaje, String titulo, int tipoOpcion) {
        return confirmar(padre, mensaje, titulo, tipoOpcion, JOptionPane.QUESTION_MESSAGE);
    }

    /** Confirmación (devuelve YES_OPTION o NO_OPTION) o formulario (devuelve OK_OPTION o CANCEL_OPTION). */
    public static int confirmar(Component padre, Object mensaje, String titulo, int tipoOpcion, int tipoMensaje) {
        if (mensaje instanceof JComponent) {
            if (modoPrueba) {
                return JOptionPane.CANCEL_OPTION;
            }
            JComponent formulario = (JComponent) mensaje;
            estilizarFormulario(formulario);
            int r = abrir(padre, null, titulo, formulario, "Guardar", "Cancelar", false, null);
            return r == 0 ? JOptionPane.OK_OPTION : JOptionPane.CANCEL_OPTION;
        }
        if (modoPrueba) {
            return JOptionPane.NO_OPTION;
        }
        String texto = limpiar(String.valueOf(mensaje));
        boolean peligro = texto.toLowerCase().contains("elimin");
        Tipo tipo = peligro ? Tipo.ADVERTENCIA : Tipo.PREGUNTA;
        int r = abrir(padre, tipo, titulo == null ? "Confirmar" : titulo, etiquetaMensaje(texto),
                peligro ? "Eliminar" : "Confirmar", "Cancelar", peligro, null);
        return r == 0 ? JOptionPane.YES_OPTION : JOptionPane.NO_OPTION;
    }

    /** Pide un texto. Devuelve null si se cancela. */
    public static String pedirTexto(Component padre, Object mensaje) {
        if (modoPrueba) {
            return null;
        }
        JTextField campo = new JTextField();
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        campo.setPreferredSize(new Dimension(ANCHO_TEXTO, 42));
        JPanel cuerpo = new JPanel(new java.awt.BorderLayout(0, 12));
        cuerpo.setOpaque(false);
        cuerpo.add(etiquetaMensaje(limpiar(String.valueOf(mensaje))), java.awt.BorderLayout.NORTH);
        cuerpo.add(campo, java.awt.BorderLayout.CENTER);
        int r = abrir(padre, Tipo.PREGUNTA, "Ingresa un dato", cuerpo, "Aceptar", "Cancelar", false, campo);
        return r == 0 ? campo.getText() : null;
    }

    // ------------------------------------------------------- textos y tipos

    private static String titulo(Tipo tipo) {
        switch (tipo) {
            case ERROR:
                return "Ocurrió un problema";
            case EXITO:
                return "Listo";
            case INFO:
                return "Aviso";
            case PREGUNTA:
                return "Confirmar";
            default:
                return "Revisa este dato";
        }
    }

    /** Deduce el tipo desde el texto, porque los mensajes se arman en muchos lugares. */
    static Tipo tipoSegunTexto(String texto) {
        String t = texto.toLowerCase();
        if (t.startsWith("error") || t.contains("error al") || t.contains("error:")
                || t.contains("no se pudo") || t.contains("no se encontró el driver")) {
            return Tipo.ERROR;
        }
        if (t.contains("correctamente") || t.contains("✔") || t.contains("actualizado")
                || t.contains("agregado")) {
            return Tipo.EXITO;
        }
        return Tipo.ADVERTENCIA;
    }

    /** Quita emojis y símbolos que la fuente no dibuja, y recorta mensajes larguísimos. */
    static String limpiar(String texto) {
        StringBuilder sb = new StringBuilder();
        texto.codePoints().forEach(cp -> {
            boolean simbolo = (cp >= 0x2600 && cp <= 0x27BF) || (cp >= 0x1F300 && cp <= 0x1FAFF);
            if (!simbolo) {
                sb.appendCodePoint(cp);
            }
        });
        String limpio = sb.toString().trim();
        return limpio.length() > 700 ? limpio.substring(0, 700) + "…" : limpio;
    }

    private static JLabel etiquetaMensaje(String texto) {
        String html = texto.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\n", "<br>");
        JLabel l = new JLabel("<html><body style='width:" + ANCHO_TEXTO + "px'>" + html + "</body></html>");
        l.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        l.setForeground(new Color(0x374151));
        return l;
    }

    private static void estilizarFormulario(Container c) {
        if (c instanceof JComponent) {
            ((JComponent) c).setOpaque(false);
        }
        for (Component hijo : c.getComponents()) {
            if (hijo instanceof JLabel) {
                hijo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                hijo.setForeground(Tema.TEXTO_SUAVE);
            } else if (hijo instanceof JTextField) {
                hijo.setFont(new Font("Segoe UI", Font.PLAIN, 16));
                hijo.setPreferredSize(new Dimension(Math.max(hijo.getPreferredSize().width, 340), 40));
            }
            if (hijo instanceof JComponent && hijo instanceof Container) {
                ((JComponent) hijo).setOpaque(false);
                estilizarFormulario((Container) hijo);
            }
        }
        if (c instanceof JPanel && ((JPanel) c).getLayout() instanceof GridLayout) {
            GridLayout g = (GridLayout) ((JPanel) c).getLayout();
            ((JPanel) c).setLayout(new GridLayout(g.getRows(), g.getColumns(), 0, 6));
        }
    }

    private static JTextField primerCampo(Container c) {
        for (Component hijo : c.getComponents()) {
            if (hijo instanceof JTextField) {
                return (JTextField) hijo;
            }
            if (hijo instanceof Container) {
                JTextField r = primerCampo((Container) hijo);
                if (r != null) {
                    return r;
                }
            }
        }
        return null;
    }

    // ------------------------------------------------------------- ventana

    /** Muestra el panel y espera. Devuelve 0 si se aceptó y 1 si se canceló. */
    private static int abrir(Component padre, Tipo tipo, String titulo, JComponent cuerpo,
            String textoAceptar, String textoCancelar, boolean peligro, JComponent foco) {
        Window dueno = ventanaDe(padre);
        Rectangle pantalla = limitesPantalla(dueno);
        BufferedImage fondo = capturarDesenfocado(pantalla);

        JDialog d = new JDialog(dueno, Dialog.ModalityType.APPLICATION_MODAL);
        d.setName("aviso");
        d.setUndecorated(true);
        if (dueno == null) {
            d.setAlwaysOnTop(true);
        }

        final int[] resultado = {1};
        final boolean[] cerrando = {false};
        final Cristal cristal = new Cristal(fondo, pantalla);

        // contenido
        GridBagConstraints g = new GridBagConstraints();
        g.gridx = 0;
        g.weightx = 1;
        g.fill = GridBagConstraints.HORIZONTAL;
        g.anchor = GridBagConstraints.NORTHWEST;

        JPanel cabecera = new JPanel(new GridBagLayout());
        cabecera.setOpaque(false);
        GridBagConstraints h = new GridBagConstraints();
        h.anchor = GridBagConstraints.WEST;
        if (tipo != null) {
            h.insets = new Insets(0, 0, 0, 16);
            cabecera.add(new Insignia(tipo), h);
        }
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 21));
        lblTitulo.setForeground(Tema.TEXTO);
        h.insets = new Insets(0, 0, 0, 0);
        h.weightx = 1;
        h.fill = GridBagConstraints.HORIZONTAL;
        cabecera.add(lblTitulo, h);

        g.gridy = 0;
        cristal.add(cabecera, g);
        g.gridy = 1;
        g.insets = new Insets(18, 0, 0, 0);
        cristal.add(cuerpo, g);

        JButton aceptar = boton(textoAceptar, true, peligro);
        JPanel botones = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 10, 0));
        botones.setOpaque(false);
        if (textoCancelar != null) {
            JButton cancelar = boton(textoCancelar, false, false);
            cancelar.addActionListener(e -> {
                resultado[0] = 1;
                cerrar(d, cristal, cerrando);
            });
            botones.add(cancelar);
        }
        botones.add(aceptar);
        aceptar.addActionListener(e -> {
            resultado[0] = 0;
            cerrar(d, cristal, cerrando);
        });
        g.gridy = 2;
        g.insets = new Insets(24, 0, 0, 0);
        cristal.add(botones, g);

        d.setContentPane(cristal);
        d.getRootPane().setDefaultButton(aceptar);
        d.getRootPane().registerKeyboardAction(e -> {
            resultado[0] = 1;
            cerrar(d, cristal, cerrando);
        }, KeyStroke.getKeyStroke("ESCAPE"), JComponent.WHEN_IN_FOCUSED_WINDOW);
        d.pack();
        // Ventana opaca con esquinas redondeadas. Se evita la transparencia por píxel: en algunos
        // equipos dejaba el panel sin pintar después de moverlo.
        try {
            d.setShape(new RoundRectangle2D.Float(0, 0, d.getWidth(), d.getHeight(), RADIO, RADIO));
        } catch (UnsupportedOperationException ignorado) {
            // sin ventanas con forma: queda rectangular
        }

        // animación de entrada: sube desde abajo de la pantalla hasta el centro de la ventana
        Point destino = destinoCentrado(dueno, pantalla, d.getSize());
        int origenY = pantalla.y + pantalla.height;
        d.setLocation(destino.x, origenY);

        final long[] inicio = {0};
        Timer entrada = new Timer(14, null);
        entrada.addActionListener(e -> {
            if (inicio[0] == 0) {
                inicio[0] = System.nanoTime();
            }
            double ms = (System.nanoTime() - inicio[0]) / 1e6;
            double t = Math.min(1.0, ms / DURACION_ENTRADA_MS);
            double k = 1 - Math.pow(1 - t, 3);
            d.setLocation(destino.x, (int) Math.round(origenY + (destino.y - origenY) * k));
            pintarYa(d);
            if (t >= 1.0) {
                ((Timer) e.getSource()).stop();
                reforzarPintado(d);
            }
        });

        final JComponent enfoque = foco != null ? foco : primerCampo(cuerpo);
        d.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowOpened(java.awt.event.WindowEvent e) {
                SwingUtilities.invokeLater(() -> {
                    if (enfoque != null) {
                        enfoque.requestFocusInWindow();
                    } else {
                        aceptar.requestFocusInWindow();
                    }
                });
            }
        });

        entrada.start();
        d.setVisible(true); // bloquea hasta que se cierra
        entrada.stop();
        return resultado[0];
    }

    private static void cerrar(JDialog d, Cristal cristal, boolean[] cerrando) {
        if (cerrando[0]) {
            return;
        }
        cerrando[0] = true;
        final long inicio = System.nanoTime();
        final int y0 = d.getY();
        final int hasta = limitesPantalla(d).y + limitesPantalla(d).height;
        Timer salida = new Timer(14, null);
        salida.addActionListener(e -> {
            double p = Math.min(1.0, (System.nanoTime() - inicio) / 1e6 / DURACION_SALIDA_MS);
            d.setLocation(d.getX(), (int) Math.round(y0 + (hasta - y0) * p * p));
            if (p >= 1.0) {
                ((Timer) e.getSource()).stop();
                d.dispose();
            }
        });
        salida.start();
    }

    /** Pide repintar todo el contenido de la ventana. */
    private static void pintarYa(JDialog d) {
        d.getContentPane().repaint();
    }

    /** Repinta varias veces al llegar al centro, por si algún cuadro se perdió durante el movimiento. */
    private static void reforzarPintado(JDialog d) {
        final int[] veces = {0};
        Timer refuerzo = new Timer(90, null);
        refuerzo.addActionListener(e -> {
            if (!d.isShowing() || ++veces[0] > 4) {
                ((Timer) e.getSource()).stop();
                return;
            }
            d.getRootPane().revalidate();
            pintarYa(d);
        });
        refuerzo.start();
    }

    private static Window ventanaDe(Component padre) {
        Window w = null;
        if (padre instanceof Window) {
            w = (Window) padre;
        } else if (padre != null) {
            w = SwingUtilities.getWindowAncestor(padre);
        }
        if (w == null || !w.isShowing()) {
            w = KeyboardFocusManager.getCurrentKeyboardFocusManager().getActiveWindow();
        }
        return (w != null && w.isShowing()) ? w : null;
    }

    private static Rectangle limitesPantalla(Window dueno) {
        GraphicsConfiguration gc = dueno != null
                ? dueno.getGraphicsConfiguration()
                : GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
        return gc.getBounds();
    }

    private static Point destinoCentrado(Window dueno, Rectangle pantalla, Dimension tam) {
        Rectangle ref = dueno != null ? dueno.getBounds() : pantalla;
        int x = ref.x + (ref.width - tam.width) / 2;
        int y = ref.y + (ref.height - tam.height) / 2;
        x = Math.max(pantalla.x, Math.min(x, pantalla.x + pantalla.width - tam.width));
        y = Math.max(pantalla.y, Math.min(y, pantalla.y + pantalla.height - tam.height));
        return new Point(x, y);
    }

    // -------------------------------------------------------- fondo de vidrio

    /** Foto de la pantalla, desenfocada. Devuelve null si el sistema no permite capturar. */
    private static BufferedImage capturarDesenfocado(Rectangle pantalla) {
        try {
            BufferedImage foto = new Robot().createScreenCapture(pantalla);
            return desenfocar(foto);
        } catch (Exception | Error e) {
            return null;
        }
    }

    private static BufferedImage desenfocar(BufferedImage origen) {
        int w = origen.getWidth();
        int h = origen.getHeight();
        int pw = Math.max(1, w / 8);
        int ph = Math.max(1, h / 8);
        BufferedImage pequena = new BufferedImage(pw, ph, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = pequena.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(origen, 0, 0, pw, ph, null);
        g.dispose();

        float[] kernel = new float[25];
        java.util.Arrays.fill(kernel, 1f / 25f);
        ConvolveOp caja = new ConvolveOp(new Kernel(5, 5, kernel), ConvolveOp.EDGE_NO_OP, null);
        pequena = caja.filter(pequena, null);
        pequena = caja.filter(pequena, null);

        BufferedImage grande = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        g = grande.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(pequena, 0, 0, w, h, null);
        g.dispose();
        return grande;
    }

    // ------------------------------------------------------------ componentes

    /** El vidrio: fondo desenfocado de la pantalla, velo claro, brillo superior y borde fino. */
    private static final class Cristal extends JPanel {

        private final BufferedImage fondo;
        private final Rectangle pantalla;

        Cristal(BufferedImage fondo, Rectangle pantalla) {
            super(new GridBagLayout());
            this.fondo = fondo;
            this.pantalla = pantalla;
            setOpaque(true);
            setBorder(BorderFactory.createEmptyBorder(30, 32, 28, 32));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth();
            int h = getHeight();
            Shape forma = new RoundRectangle2D.Float(0.5f, 0.5f, w - 1f, h - 1f, RADIO, RADIO);

            g2.setColor(Color.WHITE);
            g2.fillRect(0, 0, w, h);
            g2.setClip(forma);
            if (fondo != null && isShowing()) {
                Point p = getLocationOnScreen();
                g2.drawImage(fondo, pantalla.x - p.x, pantalla.y - p.y, null);
            } else {
                g2.setPaint(new GradientPaint(0, 0, new Color(0xDCE6F2), 0, h, new Color(0xC9D6E6)));
                g2.fillRect(0, 0, w, h);
            }
            // velo claro para que el texto se lea sobre cualquier fondo
            g2.setPaint(new GradientPaint(0, 0, new Color(255, 255, 255, 178), 0, h, new Color(246, 249, 253, 150)));
            g2.fillRect(0, 0, w, h);
            // brillo de la parte superior
            g2.setPaint(new GradientPaint(0, 0, new Color(255, 255, 255, 120), 0, h * 0.5f, new Color(255, 255, 255, 0)));
            g2.fillRect(0, 0, w, (int) (h * 0.55f));
            g2.setClip(null);

            // borde: luz exterior y línea interior tenue
            g2.setStroke(new BasicStroke(1.4f));
            g2.setColor(new Color(255, 255, 255, 235));
            g2.draw(forma);
            g2.setStroke(new BasicStroke(1f));
            g2.setColor(new Color(29, 59, 83, 36));
            g2.draw(new RoundRectangle2D.Float(1.5f, 1.5f, w - 3f, h - 3f, RADIO - 2, RADIO - 2));
            g2.dispose();
        }
    }

    /** Círculo de color con el símbolo del tipo de aviso. */
    private static final class Insignia extends JComponent {

        private final Tipo tipo;

        Insignia(Tipo tipo) {
            this.tipo = tipo;
            setPreferredSize(new Dimension(48, 48));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
            Color fuerte;
            Color suave;
            switch (tipo) {
                case ERROR:
                    fuerte = new Color(0xD14343);
                    suave = new Color(0xFCE9E9);
                    break;
                case EXITO:
                    fuerte = new Color(0x2E8B57);
                    suave = new Color(0xE3F3EA);
                    break;
                case ADVERTENCIA:
                    fuerte = new Color(0xC77700);
                    suave = new Color(0xFFF1D6);
                    break;
                default:
                    fuerte = new Color(0x2F6DB5);
                    suave = new Color(0xE3EDF8);
                    break;
            }
            g2.setColor(suave);
            g2.fill(new Ellipse2D.Float(0, 0, 47, 47));
            g2.setColor(fuerte);
            g2.setStroke(new BasicStroke(3.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            switch (tipo) {
                case ERROR:
                    g2.drawLine(17, 17, 30, 30);
                    g2.drawLine(30, 17, 17, 30);
                    break;
                case EXITO:
                    Path2D visto = new Path2D.Float();
                    visto.moveTo(15, 24.5);
                    visto.lineTo(21, 30.5);
                    visto.lineTo(32, 17.5);
                    g2.draw(visto);
                    break;
                case ADVERTENCIA:
                    g2.drawLine(23, 14, 23, 26);
                    g2.fill(new Ellipse2D.Float(21.2f, 30, 3.6f, 3.6f));
                    break;
                case INFO:
                    g2.fill(new Ellipse2D.Float(21.2f, 13.5f, 3.6f, 3.6f));
                    g2.drawLine(23, 21, 23, 33);
                    break;
                default:
                    g2.setFont(new Font("Segoe UI", Font.BOLD, 28));
                    FontMetrics fm = g2.getFontMetrics();
                    g2.drawString("?", (47 - fm.stringWidth("?")) / 2f, (47 - fm.getHeight()) / 2f + fm.getAscent() - 1);
                    break;
            }
            g2.dispose();
        }
    }

    private static JButton boton(String texto, boolean principal, boolean peligro) {
        JButton b = new JButton(texto);
        b.setFont(new Font("Segoe UI", Font.BOLD, 15));
        b.setFocusPainted(false);
        b.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));
        b.setPreferredSize(new Dimension(Math.max(112, b.getPreferredSize().width + 28), 42));
        if (principal) {
            b.setBackground(peligro ? Estilo.ROJO : Estilo.AZUL);
            b.setForeground(Color.WHITE);
            b.setBorderPainted(false);
        } else {
            b.setBackground(new Color(255, 255, 255, 220));
            b.setForeground(Tema.TEXTO);
            b.setBorder(new LineBorder(new Color(0xCBD2D9), 1, true));
        }
        return b;
    }

}
