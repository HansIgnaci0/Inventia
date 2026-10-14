import java.awt.Component;
import java.awt.Container;
import java.awt.Window;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

/**
 * Herramienta SOLO para desarrollo. No forma parte de la aplicacion.
 * Abre cada vista, cierra los avisos de error de conexion (no hay base de datos
 * en este equipo), rellena las tablas con filas de ejemplo y guarda un PNG.
 * Las filas de ejemplo existen unicamente en la captura; nunca se escriben en la BD.
 */
public class Captura {

    private static final String[] VISTAS = {
        "Login", "Mainn", "Ventas", "Detalle", "EntradaProductos",
        "Pedidos", "PedidoImpreso", "Proveedor1", "Proveedor2", "Proveedor3"
    };

    public static void main(String[] args) throws Exception {
        // Sin pantalla de avisos: los errores de conexion esperados (sin BD) no deben bloquear.
        Vistas.Aviso.modoPrueba = true;
        // Sin BD, cada vista imprime errores de conexion esperados: se descartan.
        System.setErr(new java.io.PrintStream(java.io.OutputStream.nullOutputStream()));
        File carpeta = new File(args.length > 0 ? args[0] : "capturas");
        carpeta.mkdirs();

        Thread cerrador = new Thread(() -> {
            while (true) {
                for (Window w : Window.getWindows()) {
                    if (w instanceof javax.swing.JDialog && w.isVisible()) {
                        cerrarAviso((javax.swing.JDialog) w);
                    }
                }
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    return;
                }
            }
        });
        cerrador.setDaemon(true);
        cerrador.start();

        for (String nombre : VISTAS) {
            try {
                final JFrame[] caja = new JFrame[1];
                SwingUtilities.invokeAndWait(() -> {
                    try {
                        caja[0] = (JFrame) Class.forName("Vistas." + nombre)
                                .getDeclaredConstructor().newInstance();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
                JFrame f = caja[0];
                SwingUtilities.invokeAndWait(() -> rellenarTablas(f));
                SwingUtilities.invokeAndWait(() -> { }); // deja correr los avisos de cambio de las tablas
                SwingUtilities.invokeAndWait(() -> {
                    f.setSize(f.getPreferredSize().width, Math.max(f.getPreferredSize().height, f.getHeight()));
                    f.addNotify();
                    f.validate();
                    BufferedImage img = new BufferedImage(
                            f.getContentPane().getWidth(), f.getContentPane().getHeight(),
                            BufferedImage.TYPE_INT_RGB);
                    f.getContentPane().paint(img.getGraphics()); // paint (no printAll): printAll oculta la selección de las tablas
                    try {
                        ImageIO.write(img, "png", new File(carpeta, nombre + ".png"));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    f.dispose();
                });
                System.out.println("OK  " + nombre);
            } catch (Exception e) {
                System.out.println("ERR " + nombre + ": " + e);
            }
        }
        System.exit(0);
    }

    /** Cierra el aviso como si el usuario pulsara Aceptar; si no es un JOptionPane, lo descarta. */
    private static void cerrarAviso(javax.swing.JDialog d) {
        javax.swing.JOptionPane panel = buscarOptionPane(d);
        if (panel != null) {
            panel.setValue(javax.swing.JOptionPane.OK_OPTION);
        } else {
            d.dispose();
        }
    }

    private static javax.swing.JOptionPane buscarOptionPane(Container c) {
        for (Component hijo : c.getComponents()) {
            if (hijo instanceof javax.swing.JOptionPane) {
                return (javax.swing.JOptionPane) hijo;
            }
            if (hijo instanceof Container) {
                javax.swing.JOptionPane r = buscarOptionPane((Container) hijo);
                if (r != null) {
                    return r;
                }
            }
        }
        return null;
    }

    private static void rellenarTablas(Container c) {
        for (Component hijo : c.getComponents()) {
            if (hijo instanceof JTable) {
                JTable t = (JTable) hijo;
                if (t.getModel() instanceof DefaultTableModel && t.getModel().getRowCount() == 0
                        && t.getColumnCount() > 0) {
                    DefaultTableModel m = (DefaultTableModel) t.getModel();
                    for (int i = 1; i <= 8; i++) {
                        Object[] fila = new Object[m.getColumnCount()];
                        for (int j = 0; j < fila.length; j++) {
                            String col = String.valueOf(m.getColumnName(j)).toLowerCase();
                            if (col.contains("cod")) {
                                fila[j] = "78010" + (i < 10 ? "0" : "") + i;
                            } else if (col.contains("cantidad")) {
                                fila[j] = i % 3 == 0 ? 1.5 : (double) (i % 4 + 1);
                            } else if (col.contains("precio") || col.contains("total")
                                    || col.contains("subtotal") || col.contains("stock")) {
                                fila[j] = i * 350 + 640;
                            } else if (col.contains("hora")) {
                                fila[j] = "1" + i + ":4" + i;
                            } else {
                                fila[j] = "Producto de ejemplo " + i;
                            }
                        }
                        m.addRow(fila);
                    }
                    t.setRowSelectionInterval(2, 2); // muestra una fila seleccionada
                }
            }
            if (hijo instanceof Container) {
                rellenarTablas((Container) hijo);
            }
        }
    }
}
