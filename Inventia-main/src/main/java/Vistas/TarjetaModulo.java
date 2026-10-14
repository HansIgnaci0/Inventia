package Vistas;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

/**
 * Tarjeta de módulo del menú principal: franja de color arriba, icono, título,
 * descripción, enlace "Abrir módulo" y atajo de teclado.
 * Toda la tarjeta es clicable; el clic acciona el botón recibido, así que el
 * ActionListener original del botón sigue siendo quien abre la vista.
 */
public final class TarjetaModulo extends JPanel {

    private static final Color BORDE_TARJETA = new Color(0xE1E5EB);
    private static final Color SEPARADOR = new Color(0xE9ECF0);

    private final Color acento;
    private boolean sobre = false;

    public TarjetaModulo(Color acento, Color tinte, Icon icono, String titulo,
            String descripcion, String atajo, JButton abrir) {
        this.acento = acento;
        setOpaque(false);
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(26, 28, 20, 28));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        putClientProperty(Tema.OMITIR, Boolean.TRUE);

        GridBagConstraints g = new GridBagConstraints();
        g.gridx = 0;
        g.weightx = 1;
        g.fill = GridBagConstraints.HORIZONTAL;
        g.anchor = GridBagConstraints.NORTHWEST;

        g.gridy = 0;
        g.fill = GridBagConstraints.NONE;
        add(new Ficha(tinte, icono), g);

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.putClientProperty(Tema.OMITIR, Boolean.TRUE);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(Tema.TEXTO);
        g.gridy = 1;
        g.fill = GridBagConstraints.HORIZONTAL;
        g.insets = new Insets(24, 0, 8, 0);
        add(lblTitulo, g);

        JLabel lblDescripcion = new JLabel("<html><body style='width:200px'>" + descripcion + "</body></html>");
        lblDescripcion.putClientProperty(Tema.OMITIR, Boolean.TRUE);
        lblDescripcion.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        lblDescripcion.setForeground(Tema.TEXTO_SUAVE);
        lblDescripcion.setVerticalAlignment(SwingConstants.TOP);
        g.gridy = 2;
        g.weighty = 1;
        g.fill = GridBagConstraints.BOTH;
        g.insets = new Insets(0, 0, 20, 0);
        add(lblDescripcion, g);

        JPanel linea = new JPanel();
        linea.putClientProperty(Tema.OMITIR, Boolean.TRUE);
        linea.setBackground(SEPARADOR);
        linea.setPreferredSize(new Dimension(1, 1));
        linea.setMinimumSize(new Dimension(1, 1));
        linea.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        g.gridy = 3;
        g.weighty = 0;
        g.fill = GridBagConstraints.HORIZONTAL;
        g.insets = new Insets(0, 0, 14, 0);
        add(linea, g);

        abrir.putClientProperty(Tema.OMITIR, Boolean.TRUE);
        abrir.setText("Abrir módulo  →");
        abrir.setFont(new Font("Segoe UI", Font.BOLD, 15));
        abrir.setForeground(acento);
        abrir.setHorizontalAlignment(SwingConstants.LEFT);
        abrir.setContentAreaFilled(false);
        abrir.setBorderPainted(false);
        abrir.setFocusPainted(false);
        abrir.setMargin(new Insets(0, 0, 0, 0));
        abrir.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JLabel lblAtajo = new JLabel(atajo, SwingConstants.CENTER);
        lblAtajo.putClientProperty(Tema.OMITIR, Boolean.TRUE);
        lblAtajo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblAtajo.setForeground(Tema.TEXTO_SUAVE);
        lblAtajo.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDE_TARJETA, 1, true),
                BorderFactory.createEmptyBorder(3, 9, 3, 9)));

        JPanel pie = new JPanel(new java.awt.BorderLayout());
        pie.putClientProperty(Tema.OMITIR, Boolean.TRUE);
        pie.setOpaque(false);
        pie.add(abrir, java.awt.BorderLayout.WEST);
        pie.add(lblAtajo, java.awt.BorderLayout.EAST);
        g.gridy = 4;
        g.insets = new Insets(0, 0, 0, 0);
        add(pie, g);

        MouseAdapter raton = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                abrir.doClick();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                marcarSobre(true);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                marcarSobre(getMousePosition(true) != null);
            }
        };
        // Los hijos sin listeners propios reenvían el ratón a la tarjeta.
        addMouseListener(raton);
    }

    private void marcarSobre(boolean valor) {
        if (sobre != valor) {
            sobre = valor;
            repaint();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int w = getWidth();
        int h = getHeight();
        RoundRectangle2D forma = new RoundRectangle2D.Double(0.5, 0.5, w - 1, h - 1, 16, 16);

        g2.setColor(sobre ? new Color(0xFAFBFD) : Color.WHITE);
        g2.fill(forma);

        g2.setClip(forma);
        g2.setColor(acento);
        g2.fillRect(0, 0, w, 4);
        g2.setClip(null);

        g2.setColor(sobre ? acento : BORDE_TARJETA);
        g2.draw(forma);
        g2.dispose();
    }

    /** Cuadro de color suave que contiene el icono. */
    private static final class Ficha extends JPanel {

        private final Color tinte;
        private final Icon icono;

        Ficha(Color tinte, Icon icono) {
            this.tinte = tinte;
            this.icono = icono;
            setOpaque(false);
            setPreferredSize(new Dimension(56, 56));
            setMinimumSize(new Dimension(56, 56));
            putClientProperty(Tema.OMITIR, Boolean.TRUE);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(tinte);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
            icono.paintIcon(this, g2,
                    (getWidth() - icono.getIconWidth()) / 2,
                    (getHeight() - icono.getIconHeight()) / 2);
            g2.dispose();
        }
    }
}
