package Vistas;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.RoundRectangle2D;
import javax.swing.Icon;

/** Iconos de línea dibujados con Java2D, sobre una cuadrícula de 24x24. Sin archivos de imagen. */
public final class Iconos {

    private enum Tipo { CARRITO, RECIBO, CAJA, FLECHA_DERECHA, FLECHA_IZQUIERDA, LUPA, MAS, LAPIZ, PAPELERA }

    public static Icon flechaDerecha(Color color, int tamano) {
        return new Linea(Tipo.FLECHA_DERECHA, color, tamano);
    }

    public static Icon flechaIzquierda(Color color, int tamano) {
        return new Linea(Tipo.FLECHA_IZQUIERDA, color, tamano);
    }

    public static Icon mas(Color color, int tamano) {
        return new Linea(Tipo.MAS, color, tamano);
    }

    public static Icon lapiz(Color color, int tamano) {
        return new Linea(Tipo.LAPIZ, color, tamano);
    }

    public static Icon papelera(Color color, int tamano) {
        return new Linea(Tipo.PAPELERA, color, tamano);
    }

    public static Icon lupa(Color color, int tamano) {
        return new Linea(Tipo.LUPA, color, tamano);
    }

    private Iconos() {
    }

    public static Icon carrito(Color color, int tamano) {
        return new Linea(Tipo.CARRITO, color, tamano);
    }

    public static Icon recibo(Color color, int tamano) {
        return new Linea(Tipo.RECIBO, color, tamano);
    }

    public static Icon caja(Color color, int tamano) {
        return new Linea(Tipo.CAJA, color, tamano);
    }

    private static final class Linea implements Icon {

        private final Tipo tipo;
        private final Color color;
        private final int tamano;

        Linea(Tipo tipo, Color color, int tamano) {
            this.tipo = tipo;
            this.color = color;
            this.tamano = tamano;
        }

        @Override
        public int getIconWidth() {
            return tamano;
        }

        @Override
        public int getIconHeight() {
            return tamano;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
            g2.translate(x, y);
            double s = tamano / 24.0;
            g2.scale(s, s);
            g2.setColor(color);
            g2.setStroke(new BasicStroke(1.9f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            switch (tipo) {
                case CARRITO:
                    dibujarCarrito(g2);
                    break;
                case RECIBO:
                    dibujarRecibo(g2);
                    break;
                case FLECHA_DERECHA:
                    dibujarFlecha(g2, false);
                    break;
                case FLECHA_IZQUIERDA:
                    dibujarFlecha(g2, true);
                    break;
                case LUPA:
                    dibujarLupa(g2);
                    break;
                case MAS:
                    dibujarMas(g2);
                    break;
                case LAPIZ:
                    dibujarLapiz(g2);
                    break;
                case PAPELERA:
                    dibujarPapelera(g2);
                    break;
                default:
                    dibujarCaja(g2);
                    break;
            }
            g2.dispose();
        }

        private void dibujarFlecha(Graphics2D g2, boolean haciaIzquierda) {
            if (haciaIzquierda) {
                g2.translate(24, 0);
                g2.scale(-1, 1);
            }
            Path2D p = new Path2D.Double();
            p.moveTo(4, 12);
            p.lineTo(20, 12);
            p.moveTo(13, 5);
            p.lineTo(20, 12);
            p.lineTo(13, 19);
            g2.draw(p);
        }

        private void dibujarMas(Graphics2D g2) {
            g2.draw(new java.awt.geom.Line2D.Double(12, 5, 12, 19));
            g2.draw(new java.awt.geom.Line2D.Double(5, 12, 19, 12));
        }

        private void dibujarLapiz(Graphics2D g2) {
            Path2D p = new Path2D.Double();
            p.moveTo(4, 20);
            p.lineTo(5, 15);
            p.lineTo(16, 4);
            p.lineTo(20, 8);
            p.lineTo(9, 19);
            p.closePath();
            g2.draw(p);
            g2.draw(new java.awt.geom.Line2D.Double(13.5, 6.5, 17.5, 10.5));
        }

        private void dibujarPapelera(Graphics2D g2) {
            g2.draw(new java.awt.geom.Line2D.Double(4, 7, 20, 7));
            Path2D asa = new Path2D.Double();
            asa.moveTo(9, 7);
            asa.lineTo(9, 4);
            asa.lineTo(15, 4);
            asa.lineTo(15, 7);
            g2.draw(asa);
            Path2D cuerpo = new Path2D.Double();
            cuerpo.moveTo(6, 7);
            cuerpo.lineTo(7, 20);
            cuerpo.lineTo(17, 20);
            cuerpo.lineTo(18, 7);
            g2.draw(cuerpo);
            g2.draw(new java.awt.geom.Line2D.Double(10, 11, 10, 16));
            g2.draw(new java.awt.geom.Line2D.Double(14, 11, 14, 16));
        }

        private void dibujarLupa(Graphics2D g2) {
            g2.draw(new Ellipse2D.Double(3.5, 3.5, 13, 13));
            g2.draw(new java.awt.geom.Line2D.Double(15, 15, 20.5, 20.5));
        }

        private void dibujarCarrito(Graphics2D g2) {
            Path2D p = new Path2D.Double();
            p.moveTo(2.5, 3.5);
            p.lineTo(5.2, 3.5);
            p.lineTo(7.4, 15);
            p.lineTo(18.6, 15);
            p.lineTo(20.5, 6.5);
            p.lineTo(6, 6.5);
            g2.draw(p);
            g2.draw(new Ellipse2D.Double(7.6, 17.8, 3, 3));
            g2.draw(new Ellipse2D.Double(15, 17.8, 3, 3));
        }

        private void dibujarRecibo(Graphics2D g2) {
            Path2D p = new Path2D.Double();
            p.moveTo(5.5, 2.5);
            p.lineTo(18.5, 2.5);
            p.lineTo(18.5, 21.5);
            p.lineTo(16, 19.8);
            p.lineTo(14, 21.5);
            p.lineTo(12, 19.8);
            p.lineTo(10, 21.5);
            p.lineTo(8, 19.8);
            p.lineTo(5.5, 21.5);
            p.closePath();
            g2.draw(p);
            g2.draw(new java.awt.geom.Line2D.Double(8.5, 8, 15.5, 8));
            g2.draw(new java.awt.geom.Line2D.Double(8.5, 12, 15.5, 12));
            g2.draw(new java.awt.geom.Line2D.Double(8.5, 16, 12.5, 16));
        }

        private void dibujarCaja(Graphics2D g2) {
            Path2D p = new Path2D.Double();
            p.moveTo(12, 2.5);
            p.lineTo(20.5, 7.2);
            p.lineTo(20.5, 16.8);
            p.lineTo(12, 21.5);
            p.lineTo(3.5, 16.8);
            p.lineTo(3.5, 7.2);
            p.closePath();
            g2.draw(p);
            Path2D q = new Path2D.Double();
            q.moveTo(3.5, 7.2);
            q.lineTo(12, 12);
            q.lineTo(20.5, 7.2);
            q.moveTo(12, 12);
            q.lineTo(12, 21.5);
            g2.draw(q);
        }
    }
}
