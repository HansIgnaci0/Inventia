import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Window;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/**
 * Herramienta SOLO para desarrollo. No forma parte de la aplicacion.
 * Abre el menu principal en pantalla real, muestra cada tipo de aviso de vidrio y guarda
 * un PNG de cada uno (con la pantalla real porque el vidrio desenfoca lo que hay detras).
 * Tambien guarda un fotograma a mitad de la animacion de subida.
 * Durante unos segundos veras ventanas moverse: es normal.
 */
public class CapturaAviso {

    private static JFrame marco;

    public static void main(String[] args) throws Exception {
        System.setErr(new java.io.PrintStream(java.io.OutputStream.nullOutputStream()));
        File carpeta = new File(args.length > 0 ? args[0] : "capturas");
        carpeta.mkdirs();

        Vistas.Aviso.modoPrueba = true; // mientras se crea la ventana base
        SwingUtilities.invokeAndWait(() -> {
            marco = new Vistas.Mainn();
            marco.setLocation(120, 70);
            marco.setVisible(true);
        });
        Thread.sleep(700);
        Vistas.Aviso.modoPrueba = false;

        caso(carpeta, "Aviso_advertencia", 1000, 0, () ->
                Vistas.Aviso.mostrar(marco, "Seleccione un producto de la tabla"));
        caso(carpeta, "Aviso_error", 1000, 0, () ->
                Vistas.Aviso.mostrar(marco, "Error al guardar la venta: Communications link failure"));
        caso(carpeta, "Aviso_exito", 1000, 0, () ->
                Vistas.Aviso.mostrar(marco, "Venta registrada correctamente"));
        caso(carpeta, "Aviso_confirmar", 1000, 0, () ->
                Vistas.Aviso.confirmar(marco, "¿Eliminar este producto?", "Confirmar",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE));
        caso(carpeta, "Aviso_texto", 1000, 0, () ->
                Vistas.Aviso.pedirTexto(marco, "¿Cuántos kilos de Papas?"));
        caso(carpeta, "Aviso_formulario", 1000, 0, () -> {
            JPanel panel = new JPanel(new GridLayout(0, 1));
            panel.add(new JLabel("Nombre del producto a granel:"));
            panel.add(new JTextField());
            panel.add(new JLabel("Precio por kilo:"));
            panel.add(new JTextField());
            Vistas.Aviso.confirmar(marco, panel, "Producto a granel",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        });
        // Trayectoria de la animacion: posicion Y del panel mientras sube desde abajo.
        Thread sonda = new Thread(() -> {
            try {
                StringBuilder sb = new StringBuilder();
                int ultimo = Integer.MIN_VALUE;
                long fin = System.currentTimeMillis() + 1800;
                while (System.currentTimeMillis() < fin) {
                    for (Window w : Window.getWindows()) {
                        if ("aviso".equals(w.getName()) && w.isVisible() && w.getY() != ultimo) {
                            ultimo = w.getY();
                            sb.append(ultimo).append(" ");
                        }
                    }
                    Thread.sleep(15);
                }
                System.out.println("Trayectoria Y del panel (pantalla " + java.awt.Toolkit.getDefaultToolkit().getScreenSize().height
                        + " px): " + sb);
                SwingUtilities.invokeLater(() -> {
                    for (Window w : Window.getWindows()) {
                        if ("aviso".equals(w.getName()) && w.isVisible()) {
                            w.dispose();
                        }
                    }
                });
            } catch (Exception e) {
                System.out.println("ERR animacion: " + e);
            }
        });
        sonda.start();
        SwingUtilities.invokeAndWait(() -> Vistas.Aviso.mostrar(marco, "Stock insuficiente"));
        sonda.join();

        SwingUtilities.invokeAndWait(() -> marco.dispose());
        System.out.println("Listo: capturas guardadas en " + carpeta.getAbsolutePath());
        System.exit(0);
    }

    /** Abre un aviso (bloquea), saca la foto pasados "espera" ms y cierra el aviso. */
    private static void caso(File carpeta, String nombre, int espera, int extra, Runnable abrir) throws Exception {
        Thread foto = new Thread(() -> {
            try {
                Thread.sleep(espera);
                Rectangle zona = marco.getBounds();
                zona.grow(30, 30);
                BufferedImage img = new Robot().createScreenCapture(zona);
                ImageIO.write(img, "png", new File(carpeta, nombre + ".png"));
                Thread.sleep(250);
                SwingUtilities.invokeLater(() -> {
                    for (Window w : Window.getWindows()) {
                        if ("aviso".equals(w.getName()) && w.isVisible()) {
                            w.dispose();
                        }
                    }
                });
            } catch (Exception e) {
                System.out.println("ERR " + nombre + ": " + e);
            }
        });
        foto.start();
        SwingUtilities.invokeAndWait(abrir);
        foto.join();
        System.out.println("OK  " + nombre);
        Thread.sleep(300);
    }
}
