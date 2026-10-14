package Vistas;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JPanel;

/** Panel blanco de esquinas redondeadas con borde fino, para agrupar contenido. */
public final class Superficie extends JPanel {

    private static final Color BORDE = new Color(0xE1E5EB);
    private static final int RADIO = 14;

    public Superficie(LayoutManager layout) {
        super(layout);
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        RoundRectangle2D forma = new RoundRectangle2D.Double(0.5, 0.5, getWidth() - 1, getHeight() - 1, RADIO, RADIO);
        g2.setColor(Color.WHITE);
        g2.fill(forma);
        g2.setColor(BORDE);
        g2.draw(forma);
        g2.dispose();
    }
}
