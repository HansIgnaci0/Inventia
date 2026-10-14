/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Vistas;
import com.mycompany.inventia.ConexionBD;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
/**
 *
 * @author lttlk
 */
public class Ventas extends javax.swing.JFrame {
    
    private void mantenerFocoScanner() {
    txtBuscar.setFocusable(true);
    txtBuscar.requestFocusInWindow();

    // Cada vez que pierde foco, lo recupera
    txtBuscar.addFocusListener(new java.awt.event.FocusAdapter() {
        @Override
        public void focusLost(java.awt.event.FocusEvent e) {
            SwingUtilities.invokeLater(() -> txtBuscar.requestFocusInWindow());
        }
    });
}

    DefaultTableModel modeloProductos = new DefaultTableModel() {
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex == 1) return Integer.class; // Precio
        return String.class; // Producto
    }
};

DefaultTableModel modeloBoleta = new DefaultTableModel() {
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 2: return Double.class;   // Cantidad (kilos o unidades)
            case 3: return Integer.class;  // Precio unitario
            case 4: return Integer.class;  // Total
            default: return String.class; // Producto
        }
    }
};

    TableRowSorter<DefaultTableModel> sorter;
    ConexionBD cn = new ConexionBD();
    /**
     * Creates new form NewJFrame
     */
    public Ventas() {
        Tema.instalar();
        initComponents();

        mantenerFocoScanner();

        this.setLocationRelativeTo(null);

        String[] titulo = {"Codigo", "Producto", "Precio"};
    modeloProductos.setColumnIdentifiers(titulo);
    jTable2.setModel(modeloProductos);

    
   String[] titulo2 = {"Codigo", "Producto", "Cantidad", "Precio Unitario", "Total"};



    modeloBoleta.setColumnIdentifiers(titulo2);
    jTable3.setModel(modeloBoleta);
    jTable3.getColumnModel().getColumn(0).setMinWidth(0);
jTable3.getColumnModel().getColumn(0).setMaxWidth(0);
jTable3.getColumnModel().getColumn(0).setWidth(0);
    Connection conexion = cn.establecerConexion("root", "ADMIN12341");
        cn.listarProductos(jTable2, conexion);
    
    sorter = new TableRowSorter<>(modeloProductos);
    jTable2.setRowSorter(sorter);
        
        txtBuscar.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String texto = txtBuscar.getText();
                filtrar(texto);
            }
        });
    SwingUtilities.invokeLater(() -> {
    txtBuscar.requestFocusInWindow();
});

        disenarVenta();
    }
    private void agregarProductoDesdeScanner(int filaModelo) {

       String codigo = (String) modeloProductos.getValueAt(filaModelo, 0);
    String nombre = (String) modeloProductos.getValueAt(filaModelo, 1);
    int precio = (Integer) modeloProductos.getValueAt(filaModelo, 2);

    String tipo = cn.obtenerTipo(codigo);   // NORMAL o GRANEL

    DefaultTableModel modelo = (DefaultTableModel) jTable3.getModel();

    // ------------------ PRODUCTO GRANEL --------------------
    if (tipo.equals("GRANEL")) {

        String kilosStr = Aviso.pedirTexto(
                this,
                "¿Cuántos kilos de " + nombre + "?"
        );

        if (kilosStr == null || kilosStr.isEmpty()) return;

        try {
            double kilos = Double.parseDouble(kilosStr.replace(",", "."));

            double total = kilos * precio;

            modelo.addRow(new Object[]{
                codigo,
                nombre,
                kilos,        // cantidad decimal
                precio,
                (int) total
            });

            sumatotal();
            return;

        } catch (Exception e) {
            Aviso.mostrar(this, "Ingrese un número válido (ej: 1.3)");
            return;
        }
    }

    // ------------------ PRODUCTO NORMAL --------------------

    // Ver si ya está en la boleta
    for (int i = 0; i < modelo.getRowCount(); i++) {
        String codigoBoleta = (String) modelo.getValueAt(i, 0);

        if (codigoBoleta.equals(codigo)) {
            double cantidadActual = (Double) modelo.getValueAt(i, 2);
            double nuevaCantidad = cantidadActual + 1.0;

            Integer stock = cn.obtenerStock(nombre);

if (stock != null && nuevaCantidad > stock) {
    Aviso.mostrar(this, "Stock insuficiente");
    return;
}


            modelo.setValueAt(nuevaCantidad, i, 2);
            modelo.setValueAt((int)(nuevaCantidad * precio), i, 4);

            sumatotal();
            return;
        }
    }

    // No estaba en la boleta
        Integer stock = cn.obtenerStock(nombre);
if (stock != null && stock < 1) {
    Aviso.mostrar(this, "Sin stock");
    return;
}

    modelo.addRow(new Object[]{
        codigo,
        nombre,
        1.0,
        precio,
        precio
    });

    sumatotal();
    }


    private void buscarPorCodigoYAgregar(String codigoTexto) {

    for (int filaVista = 0; filaVista < jTable2.getRowCount(); filaVista++) {

        int filaModelo = jTable2.convertRowIndexToModel(filaVista);

        String codigoTabla = (String) modeloProductos.getValueAt(filaModelo, 0);

        if (codigoTabla.equals(codigoTexto)) {


            jTable2.setRowSelectionInterval(filaVista, filaVista);

            agregarProductoDesdeScanner(filaModelo); // 🔥 usamos índice REAL
            return;
        }
    }

    Aviso.mostrar(this, "Producto no encontrado");
}

    private void filtrar(String texto) {
    if (texto.trim().isEmpty()) {
        sorter.setRowFilter(null);
        return;
    }

    sorter.setRowFilter(RowFilter.regexFilter("(?i)" + texto, 0, 1));


    }

    private void sumatotal(){
        int totalVenta = 0;
    for (int i = 0; i < jTable3.getRowCount(); i++) {
    Object valor = jTable3.getValueAt(i, 4);
totalVenta += ((Number) valor).intValue();

}

    totalLabel1.setText(pesos(totalVenta));
    }
    
    private void restarTotal() {
    int total = 0; // Iniciamos el total en 0

    DefaultTableModel modelo = (DefaultTableModel) jTable3.getModel();
    int numFilas = modelo.getRowCount(); // Número de filas en la tabla

    // Recorremos todas las filas para recalcular el total
    for (int i = 0; i < numFilas; i++) {
        // Obtenemos el valor de la columna del "Total" (suponiendo que esté en la columna 3)
        int subtotal = (Integer) modelo.getValueAt(i, 4);

        total += subtotal;
    }

    // Actualizamos el label con el nuevo total
    totalLabel1.setText(pesos(total));
}


    // ------------------------------------------------------------------
    // Diseño visual. Reordena los mismos componentes de initComponents()
    // en dos tarjetas (productos / detalle de la venta). No toca la lógica:
    // cada botón conserva su ActionListener original.
    // ------------------------------------------------------------------

    private static String pesos(int valor) {
        return Estilo.pesos(valor);
    }

    private Estilo.Pastilla contadorProductos;
    private Estilo.Pastilla contadorBoleta;
    private javax.swing.JLabel lblMostrando;

    private void disenarVenta() {
        setTitle("Inventia");
        javax.swing.JPanel cabecera = Estilo.cabecera(BotonAtras, jLabel2, "Venta");

        // ---- Tarjeta de productos
        contadorProductos = new Estilo.Pastilla();
        txtBuscar.putClientProperty("JTextField.placeholderText", "Buscar por código o nombre");
        txtBuscar.putClientProperty("JTextField.leadingIcon", Iconos.lupa(Tema.TEXTO_SUAVE, 16));
        txtBuscar.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 15));
        txtBuscar.setPreferredSize(new java.awt.Dimension(280, 38));

        javax.swing.JPanel encabezadoProductos = new javax.swing.JPanel(new java.awt.BorderLayout(12, 0));
        encabezadoProductos.setOpaque(false);
        encabezadoProductos.setBorder(javax.swing.BorderFactory.createEmptyBorder(16, 14, 16, 14));
        encabezadoProductos.add(Estilo.tituloConPastilla("Productos", contadorProductos), java.awt.BorderLayout.WEST);
        encabezadoProductos.add(txtBuscar, java.awt.BorderLayout.EAST);

        Estilo.tabla(jTable2, jScrollPane2);
        jScrollPane2.setPreferredSize(new java.awt.Dimension(470, 380));
        Estilo.columnas(jTable2, new String[]{"Código", "Producto", "Precio"}, new int[]{110, 250, 110});
        jTable2.getColumnModel().getColumn(0).setCellRenderer(new Estilo.Celda(Estilo.Celda.TEXTO, javax.swing.SwingConstants.LEFT, false, Tema.TEXTO_SUAVE));
        jTable2.getColumnModel().getColumn(1).setCellRenderer(new Estilo.Celda(Estilo.Celda.TEXTO, javax.swing.SwingConstants.LEFT, false, Tema.TEXTO));
        jTable2.getColumnModel().getColumn(2).setCellRenderer(new Estilo.Celda(Estilo.Celda.PESOS, javax.swing.SwingConstants.RIGHT, true, Tema.TEXTO));

        lblMostrando = Estilo.textoPie("");
        javax.swing.JPanel pieProductos = new javax.swing.JPanel(new java.awt.BorderLayout());
        pieProductos.setOpaque(false);
        pieProductos.setBorder(javax.swing.BorderFactory.createEmptyBorder(12, 14, 12, 14));
        pieProductos.add(lblMostrando, java.awt.BorderLayout.WEST);
        pieProductos.add(Estilo.textoPie("Selecciona un producto y pulsa  →"), java.awt.BorderLayout.EAST);

        Superficie tarjetaProductos = new Superficie(new java.awt.BorderLayout());
        tarjetaProductos.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        tarjetaProductos.add(encabezadoProductos, java.awt.BorderLayout.NORTH);
        tarjetaProductos.add(jScrollPane2, java.awt.BorderLayout.CENTER);
        tarjetaProductos.add(pieProductos, java.awt.BorderLayout.SOUTH);

        // ---- Botones del centro: agregar (->) y quitar (<-)
        Sumar.setText("");
        Sumar.setIcon(Iconos.flechaDerecha(java.awt.Color.WHITE, 22));
        Sumar.setToolTipText("Agregar a la venta");
        Sumar.setBackground(Estilo.AZUL);
        Sumar.setBorderPainted(false);
        Sumar.setFocusPainted(false);
        Sumar.setPreferredSize(new java.awt.Dimension(58, 58));
        Sumar.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));

        Restar.setText("");
        Restar.setIcon(Iconos.flechaIzquierda(Tema.TEXTO, 22));
        Restar.setToolTipText("Quitar de la venta");
        Restar.setBackground(java.awt.Color.WHITE);
        Restar.setBorder(new javax.swing.border.LineBorder(Estilo.LINEA, 1, true));
        Restar.setFocusPainted(false);
        Restar.setPreferredSize(new java.awt.Dimension(58, 58));
        Restar.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));

        javax.swing.JPanel centro = new javax.swing.JPanel(new java.awt.GridBagLayout());
        centro.setOpaque(false);
        java.awt.GridBagConstraints c = new java.awt.GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new java.awt.Insets(0, 0, 14, 0);
        centro.add(Sumar, c);
        c.gridy = 1;
        c.insets = new java.awt.Insets(0, 0, 0, 0);
        centro.add(Restar, c);

        // ---- Tarjeta del detalle de la venta
        contadorBoleta = new Estilo.Pastilla();
        javax.swing.JPanel encabezadoBoleta = new javax.swing.JPanel(new java.awt.BorderLayout());
        encabezadoBoleta.setOpaque(false);
        encabezadoBoleta.setBorder(javax.swing.BorderFactory.createEmptyBorder(16, 14, 16, 14));
        encabezadoBoleta.add(Estilo.tituloConPastilla("Detalle de la venta", contadorBoleta), java.awt.BorderLayout.WEST);
        // misma altura que el encabezado de la izquierda, para alinear las tablas
        encabezadoBoleta.setPreferredSize(new java.awt.Dimension(10, encabezadoProductos.getPreferredSize().height));

        Estilo.tabla(jTable3, jScrollPane1);
        jScrollPane1.setPreferredSize(new java.awt.Dimension(640, 380));
        Estilo.columnas(jTable3, new String[]{null, "Producto", "Cantidad", "Precio unitario", "Subtotal"},
                new int[]{0, 260, 110, 130, 130});
        jTable3.getColumnModel().getColumn(1).setCellRenderer(new Estilo.Celda(Estilo.Celda.TEXTO, javax.swing.SwingConstants.LEFT, false, Tema.TEXTO));
        jTable3.getColumnModel().getColumn(2).setCellRenderer(new Estilo.Celda(Estilo.Celda.CANTIDAD, javax.swing.SwingConstants.CENTER, false, Tema.TEXTO));
        jTable3.getColumnModel().getColumn(3).setCellRenderer(new Estilo.Celda(Estilo.Celda.PESOS, javax.swing.SwingConstants.RIGHT, false, Tema.TEXTO));
        jTable3.getColumnModel().getColumn(4).setCellRenderer(new Estilo.Celda(Estilo.Celda.PESOS, javax.swing.SwingConstants.RIGHT, true, Tema.TEXTO));

        javax.swing.JLabel etiquetaTotal = new javax.swing.JLabel("Total");
        etiquetaTotal.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));
        etiquetaTotal.setForeground(Tema.TEXTO_SUAVE);
        totalLabel1.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 40));
        totalLabel1.setForeground(Tema.TEXTO);
        javax.swing.JPanel bloqueTotal = new javax.swing.JPanel(new java.awt.BorderLayout());
        bloqueTotal.setOpaque(false);
        bloqueTotal.add(etiquetaTotal, java.awt.BorderLayout.NORTH);
        bloqueTotal.add(totalLabel1, java.awt.BorderLayout.CENTER);

        ImprimirButon.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 19));
        ImprimirButon.setForeground(java.awt.Color.WHITE);
        ImprimirButon.setBackground(Estilo.AZUL);
        ImprimirButon.setBorderPainted(false);
        ImprimirButon.setFocusPainted(false);
        ImprimirButon.setPreferredSize(new java.awt.Dimension(200, 54));
        ImprimirButon.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));

        javax.swing.JPanel pieBoleta = new javax.swing.JPanel(new java.awt.BorderLayout());
        pieBoleta.setOpaque(false);
        pieBoleta.setBorder(javax.swing.BorderFactory.createEmptyBorder(14, 14, 16, 16));
        pieBoleta.add(bloqueTotal, java.awt.BorderLayout.WEST);
        pieBoleta.add(ImprimirButon, java.awt.BorderLayout.EAST);

        Superficie tarjetaBoleta = new Superficie(new java.awt.BorderLayout());
        tarjetaBoleta.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        tarjetaBoleta.add(encabezadoBoleta, java.awt.BorderLayout.NORTH);
        tarjetaBoleta.add(jScrollPane1, java.awt.BorderLayout.CENTER);
        tarjetaBoleta.add(pieBoleta, java.awt.BorderLayout.SOUTH);

        // ---- Cuerpo
        javax.swing.JPanel cuerpo = new javax.swing.JPanel(new java.awt.GridBagLayout());
        cuerpo.setBackground(Tema.FONDO);
        cuerpo.setBorder(javax.swing.BorderFactory.createEmptyBorder(22, 22, 22, 22));
        java.awt.GridBagConstraints g = new java.awt.GridBagConstraints();
        g.gridy = 0;
        g.weighty = 1;
        g.fill = java.awt.GridBagConstraints.BOTH;
        g.gridx = 0;
        g.weightx = 0.42;
        cuerpo.add(tarjetaProductos, g);
        g.gridx = 1;
        g.weightx = 0;
        g.fill = java.awt.GridBagConstraints.NONE;
        g.insets = new java.awt.Insets(0, 14, 0, 14);
        cuerpo.add(centro, g);
        g.gridx = 2;
        g.weightx = 0.58;
        g.fill = java.awt.GridBagConstraints.BOTH;
        g.insets = new java.awt.Insets(0, 0, 0, 0);
        cuerpo.add(tarjetaBoleta, g);

        javax.swing.JPanel raiz = new javax.swing.JPanel(new java.awt.BorderLayout());
        raiz.add(cabecera, java.awt.BorderLayout.NORTH);
        raiz.add(cuerpo, java.awt.BorderLayout.CENTER);
        setContentPane(raiz);

        // ---- Contadores que se actualizan solos
        javax.swing.event.TableModelListener alCambiar = e -> SwingUtilities.invokeLater(this::actualizarContadores);
        modeloProductos.addTableModelListener(alCambiar);
        modeloBoleta.addTableModelListener(alCambiar);
        jTable2.getRowSorter().addRowSorterListener(e -> SwingUtilities.invokeLater(this::actualizarContadores));
        sumatotal();
        actualizarContadores();

        pack();
        setMinimumSize(getPreferredSize());
        setLocationRelativeTo(null);
        SwingUtilities.invokeLater(() -> txtBuscar.requestFocusInWindow());
    }

    private void actualizarContadores() {
        int total = modeloProductos.getRowCount();
        contadorProductos.setText(Estilo.miles(total));
        int n = modeloBoleta.getRowCount();
        contadorBoleta.setText(n == 1 ? "1 producto" : n + " productos");
        lblMostrando.setText("Mostrando " + Estilo.miles(jTable2.getRowCount())
                + " de " + Estilo.miles(total) + " productos");
    }

    Ventas(Mainn aThis) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        BotonAtras = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        txtBuscar = new javax.swing.JTextField();
        Sumar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        ImprimirButon = new javax.swing.JButton();
        Restar = new javax.swing.JButton();
        totalLabel = new javax.swing.JLabel();
        totalLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel2.setFont(new java.awt.Font("Constantia", 0, 52)); // NOI18N
        jLabel2.setText("Venta");

        BotonAtras.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        BotonAtras.setText("Atras");
        BotonAtras.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BotonAtrasActionPerformed(evt);
            }
        });

        jTable2.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                true, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(jTable2);

        txtBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBuscarActionPerformed(evt);
            }
        });

        Sumar.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        Sumar.setText("->");
        Sumar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SumarActionPerformed(evt);
            }
        });

        jTable3.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable3);

        ImprimirButon.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        ImprimirButon.setText("Imprimir");
        ImprimirButon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ImprimirButonActionPerformed(evt);
            }
        });

        Restar.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        Restar.setText("<-");
        Restar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RestarActionPerformed(evt);
            }
        });

        totalLabel1.setFont(new java.awt.Font("Segoe UI", 0, 36)); // NOI18N
        totalLabel1.setText("Total: $");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 339, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(totalLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 393, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(353, 353, 353))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(36, 36, 36)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(Restar)
                                    .addComponent(Sumar))
                                .addGap(18, 18, 18)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 936, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 45, Short.MAX_VALUE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(BotonAtras)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(449, 449, 449)
                                        .addComponent(totalLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(18, 18, 18)
                                        .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(377, 377, 377)
                                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(794, 794, 794)
                                .addComponent(ImprimirButon)))
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(BotonAtras)
                            .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(30, 30, 30)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 464, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(109, 109, 109)
                                .addComponent(Sumar, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(36, 36, 36)
                                .addComponent(Restar, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 379, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(totalLabel)
                        .addGap(18, 18, 18)
                        .addComponent(totalLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(ImprimirButon, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(44, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BotonAtrasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BotonAtrasActionPerformed

        this.dispose(); 
        Mainn main=new Mainn();
        main.setVisible(true);
    }//GEN-LAST:event_BotonAtrasActionPerformed

    private void txtBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBuscarActionPerformed
        // TODO add your handling code here:
        String codigo = txtBuscar.getText().trim();

    if (codigo.isEmpty()) return;

    buscarPorCodigoYAgregar(codigo);
   

    txtBuscar.setText(""); // limpiar para siguiente scan
    txtBuscar.requestFocusInWindow();
    }//GEN-LAST:event_txtBuscarActionPerformed

    private void SumarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SumarActionPerformed
        // TODO add your handling code here:
        // Botón para agregar a la boleta
        int filaSeleccionada = jTable2.getSelectedRow(); 

    if (filaSeleccionada == -1) {
        Aviso.mostrar(null, "Seleccione un producto de la tabla");
        return;
    }

    // Obtener datos del producto seleccionado
    String codigo = (String) jTable2.getValueAt(filaSeleccionada, 0); // oculto
String nombreProducto = (String) jTable2.getValueAt(filaSeleccionada, 1);
int precioProducto = (Integer) jTable2.getValueAt(filaSeleccionada, 2);


    String cantidadStr = Aviso.pedirTexto(
            null, 
            "Ingrese la cantidad para " + nombreProducto
    );

    if (cantidadStr == null || cantidadStr.trim().isEmpty()) return;

    try {
        double cantidad = Double.parseDouble(cantidadStr.replace(",", "."));

        Integer stock = cn.obtenerStock(nombreProducto);

if (stock != null && cantidad > stock) {
    Aviso.mostrar(null, "Stock insuficiente");
    return;
}


        int total = (int) (cantidad * precioProducto);

        DefaultTableModel modeloBoleta = (DefaultTableModel) jTable3.getModel();
        
        modeloBoleta.addRow(new Object[]{
    codigo,
    nombreProducto,
    cantidad,
    precioProducto,
    (int)(cantidad * precioProducto)
});


        sumatotal(); // recalcula el total general

    } catch (NumberFormatException e) {
        Aviso.mostrar(null, "Ingrese una cantidad válida");
    }
    

    }//GEN-LAST:event_SumarActionPerformed

    private void ImprimirButonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ImprimirButonActionPerformed
           
    if (jTable3.getRowCount() == 0) {
        Aviso.mostrar(this, "No hay productos en la boleta");
        return;
    }

    int opcion = Aviso.confirmar(this, "¿Confirmar venta?");

    if (opcion != JOptionPane.YES_OPTION) return;

    // 📅 Fecha y hora actuales
    LocalDate fechaActual = LocalDate.now();
    LocalTime horaActual = LocalTime.now();

    Date fechaSQL = Date.valueOf(fechaActual);
    Time horaSQL = Time.valueOf(horaActual);

    // 💰 Total de la venta (ya lo tienes en el label)
    int totalVenta = 0;
    for (int i = 0; i < jTable3.getRowCount(); i++) {
        Object v = jTable3.getValueAt(i, 4);
        totalVenta += ((Number) v).intValue();
    }

    // 🧾 Guardamos la boleta (venta)
    int idVenta = cn.agregarVenta(totalVenta, fechaSQL, horaSQL);

    if (idVenta == -1) {
        Aviso.mostrar(this, "Error al guardar la venta");
        return;
    }

    // 📦 Guardamos los productos vendidos
    for (int i = 0; i < jTable3.getRowCount(); i++) {

        String codigo = (String) jTable3.getValueAt(i, 0);
        String nombre = (String) jTable3.getValueAt(i, 1);
        double cantidad = (Double) jTable3.getValueAt(i, 2);
        int subtotal = (Integer) jTable3.getValueAt(i, 4);

        // Insertamos el detalle

        // Actualizamos stock
        String tipo = cn.obtenerTipo(codigo);

Integer stockInicial = cn.obtenerStock(nombre);
if (stockInicial != null) {
    int stockFinal = stockInicial - (int) cantidad;
    cn.modificarProducto(nombre, stockFinal);
}


    }

    // 🧹 Limpiar boleta
    ((DefaultTableModel) jTable3.getModel()).setRowCount(0);
    totalLabel1.setText(pesos(0));

    Aviso.mostrar(this, "Venta registrada correctamente 🧾");
    }//GEN-LAST:event_ImprimirButonActionPerformed

    private void RestarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RestarActionPerformed
        // TODO add your handling code here:
    int filaSeleccionada = jTable3.getSelectedRow();
if (filaSeleccionada != -1) {
    DefaultTableModel modelo = (DefaultTableModel) jTable3.getModel();
    modelo.removeRow(filaSeleccionada); // Eliminar la fila seleccionada

    // Llamar al método para recalcular y actualizar el total
    restarTotal();
} else {
    Aviso.mostrar(null, "Seleccione una fila para eliminar.");
}

    }//GEN-LAST:event_RestarActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Ventas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Ventas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Ventas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Ventas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Ventas().setVisible(true);
            }
        });
    }
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BotonAtras;
    private javax.swing.JButton ImprimirButon;
    private javax.swing.JButton Restar;
    private javax.swing.JButton Sumar;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    private javax.swing.JLabel totalLabel;
    private javax.swing.JLabel totalLabel1;
    private javax.swing.JTextField txtBuscar;
    // End of variables declaration//GEN-END:variables


}