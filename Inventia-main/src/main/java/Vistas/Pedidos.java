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
import java.time.LocalDate;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
/**
 *
 * @author lttlk
 */
public class Pedidos extends javax.swing.JFrame {
    
    DefaultTableModel model;
    DefaultTableModel model2;
    ConexionBD con = new ConexionBD();
    TableRowSorter<DefaultTableModel> sorter;
    /**
     * Creates new form Pedidos
     */
    public Pedidos() {
        Tema.instalar();
        initComponents();
        this.setLocationRelativeTo(null);
        
        model = new DefaultTableModel();
        model.addColumn("Producto");
        model.addColumn("Cantidad");
        
        jTable1.setModel(model);
        
        model = new DefaultTableModel();
        model.addColumn("Producto");
        model.addColumn("Cantidad Solicitada");
        
        jTable2.setModel(model);
        
        Connection conexion = con.establecerConexion("root", "ADMIN12341");
        con.listarPedidos(jTable1,conexion);
        disenarPedidos();
    }

    // ------------------------------------------------------------------
    // Diseño visual. Reordena los mismos componentes de initComponents()
    // en dos tarjetas (stock / pedido). No toca la lógica: cada botón
    // conserva su ActionListener original.
    // ------------------------------------------------------------------

    private void disenarPedidos() {
        setTitle("Inventia");
        javax.swing.JPanel cabecera = Estilo.cabecera(BotonAtras, jLabel1, "Pedidos");

        // ---- Tarjeta de stock
        Estilo.Pastilla contadorStock = new Estilo.Pastilla();
        txtBuscar.putClientProperty("JTextField.placeholderText", "Buscar producto");
        txtBuscar.putClientProperty("JTextField.leadingIcon", Iconos.lupa(Tema.TEXTO_SUAVE, 16));
        txtBuscar.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 15));
        txtBuscar.setPreferredSize(new java.awt.Dimension(240, 38));

        // El filtro por nombre (columna 0) se arma aquí para que el buscador sirva de algo.
        DefaultTableModel modeloStock = (DefaultTableModel) jTable1.getModel();
        sorter = new TableRowSorter<>(modeloStock);
        jTable1.setRowSorter(sorter);
        txtBuscar.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String texto = txtBuscar.getText().trim();
                sorter.setRowFilter(texto.isEmpty() ? null
                        : RowFilter.regexFilter("(?i)" + java.util.regex.Pattern.quote(texto), 0));
            }
        });

        javax.swing.JPanel encabezadoStock = new javax.swing.JPanel(new java.awt.BorderLayout(12, 0));
        encabezadoStock.setOpaque(false);
        encabezadoStock.setBorder(javax.swing.BorderFactory.createEmptyBorder(16, 14, 16, 14));
        encabezadoStock.add(Estilo.tituloConPastilla("Stock de productos", contadorStock), java.awt.BorderLayout.WEST);
        encabezadoStock.add(txtBuscar, java.awt.BorderLayout.EAST);

        Estilo.tabla(jTable1, jScrollPane1);
        jScrollPane1.setPreferredSize(new java.awt.Dimension(460, 380));
        Estilo.columnas(jTable1, new String[]{"Producto", "Stock"}, new int[]{340, 120});
        jTable1.getColumnModel().getColumn(0).setCellRenderer(new Estilo.Celda(Estilo.Celda.TEXTO, javax.swing.SwingConstants.LEFT, false, Tema.TEXTO));
        jTable1.getColumnModel().getColumn(1).setCellRenderer(new Estilo.Celda(Estilo.Celda.TEXTO, javax.swing.SwingConstants.RIGHT, true, Tema.TEXTO));

        javax.swing.JPanel pieStock = new javax.swing.JPanel(new java.awt.BorderLayout());
        pieStock.setOpaque(false);
        pieStock.setBorder(javax.swing.BorderFactory.createEmptyBorder(12, 14, 12, 14));
        pieStock.add(Estilo.textoPie("Selecciona un producto y pulsa  →"), java.awt.BorderLayout.WEST);

        Superficie tarjetaStock = new Superficie(new java.awt.BorderLayout());
        tarjetaStock.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        tarjetaStock.add(encabezadoStock, java.awt.BorderLayout.NORTH);
        tarjetaStock.add(jScrollPane1, java.awt.BorderLayout.CENTER);
        tarjetaStock.add(pieStock, java.awt.BorderLayout.SOUTH);

        // ---- Botón del centro: agregar al pedido
        jButton2.setText("");
        jButton2.setIcon(Iconos.flechaDerecha(java.awt.Color.WHITE, 22));
        jButton2.setToolTipText("Agregar al pedido");
        jButton2.setBackground(Estilo.AZUL);
        jButton2.setBorderPainted(false);
        jButton2.setFocusPainted(false);
        jButton2.setPreferredSize(new java.awt.Dimension(58, 58));
        jButton2.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));

        // ---- Tarjeta del pedido
        Estilo.Pastilla contadorPedido = new Estilo.Pastilla();
        javax.swing.JPanel encabezadoPedido = new javax.swing.JPanel(new java.awt.BorderLayout());
        encabezadoPedido.setOpaque(false);
        encabezadoPedido.setBorder(javax.swing.BorderFactory.createEmptyBorder(16, 14, 16, 14));
        encabezadoPedido.add(Estilo.tituloConPastilla("Pedido", contadorPedido), java.awt.BorderLayout.WEST);
        encabezadoPedido.setPreferredSize(new java.awt.Dimension(10, encabezadoStock.getPreferredSize().height));

        Estilo.tabla(jTable2, jScrollPane2);
        jScrollPane2.setPreferredSize(new java.awt.Dimension(420, 380));
        Estilo.columnas(jTable2, new String[]{"Producto", "Cantidad solicitada"}, new int[]{260, 160});
        jTable2.getColumnModel().getColumn(0).setCellRenderer(new Estilo.Celda(Estilo.Celda.TEXTO, javax.swing.SwingConstants.LEFT, false, Tema.TEXTO));
        jTable2.getColumnModel().getColumn(1).setCellRenderer(new Estilo.Celda(Estilo.Celda.TEXTO, javax.swing.SwingConstants.RIGHT, true, Tema.TEXTO));

        jButton1.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 17));
        jButton1.setForeground(java.awt.Color.WHITE);
        jButton1.setBackground(Estilo.AZUL);
        jButton1.setBorderPainted(false);
        jButton1.setFocusPainted(false);
        jButton1.setPreferredSize(new java.awt.Dimension(180, 50));
        jButton1.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));

        javax.swing.JPanel piePedido = new javax.swing.JPanel(new java.awt.BorderLayout(12, 0));
        piePedido.setOpaque(false);
        piePedido.setBorder(javax.swing.BorderFactory.createEmptyBorder(12, 14, 14, 14));
        piePedido.add(Estilo.textoPie("Al imprimir se abren las hojas de cada proveedor."), java.awt.BorderLayout.CENTER);
        piePedido.add(jButton1, java.awt.BorderLayout.EAST);

        Superficie tarjetaPedido = new Superficie(new java.awt.BorderLayout());
        tarjetaPedido.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        tarjetaPedido.add(encabezadoPedido, java.awt.BorderLayout.NORTH);
        tarjetaPedido.add(jScrollPane2, java.awt.BorderLayout.CENTER);
        tarjetaPedido.add(piePedido, java.awt.BorderLayout.SOUTH);

        // ---- Cuerpo
        javax.swing.JPanel cuerpo = new javax.swing.JPanel(new java.awt.GridBagLayout());
        cuerpo.setBackground(Tema.FONDO);
        cuerpo.setBorder(javax.swing.BorderFactory.createEmptyBorder(22, 22, 22, 22));
        java.awt.GridBagConstraints g = new java.awt.GridBagConstraints();
        g.gridy = 0;
        g.weighty = 1;
        g.fill = java.awt.GridBagConstraints.BOTH;
        g.gridx = 0;
        g.weightx = 0.52;
        cuerpo.add(tarjetaStock, g);
        g.gridx = 1;
        g.weightx = 0;
        g.fill = java.awt.GridBagConstraints.NONE;
        g.insets = new java.awt.Insets(0, 14, 0, 14);
        cuerpo.add(jButton2, g);
        g.gridx = 2;
        g.weightx = 0.48;
        g.fill = java.awt.GridBagConstraints.BOTH;
        g.insets = new java.awt.Insets(0, 0, 0, 0);
        cuerpo.add(tarjetaPedido, g);

        javax.swing.JPanel raiz = new javax.swing.JPanel(new java.awt.BorderLayout());
        raiz.add(cabecera, java.awt.BorderLayout.NORTH);
        raiz.add(cuerpo, java.awt.BorderLayout.CENTER);
        setContentPane(raiz);

        // ---- Contadores que se actualizan solos
        Runnable actualizar = () -> {
            contadorStock.setText(Estilo.miles(jTable1.getModel().getRowCount()));
            int n = jTable2.getModel().getRowCount();
            contadorPedido.setText(n == 1 ? "1 producto" : n + " productos");
        };
        jTable1.getModel().addTableModelListener(e -> javax.swing.SwingUtilities.invokeLater(actualizar));
        jTable2.getModel().addTableModelListener(e -> javax.swing.SwingUtilities.invokeLater(actualizar));
        actualizar.run();

        pack();
        setMinimumSize(getPreferredSize());
        setLocationRelativeTo(null);
    }


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPopupMenu1 = new javax.swing.JPopupMenu();
        jPopupMenu2 = new javax.swing.JPopupMenu();
        jPopupMenu3 = new javax.swing.JPopupMenu();
        jLabel1 = new javax.swing.JLabel();
        BotonAtras = new javax.swing.JButton();
        txtBuscar = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Constantia", 0, 18)); // NOI18N
        jLabel1.setText("Pedidos");

        BotonAtras.setText("Atras");
        BotonAtras.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BotonAtrasActionPerformed(evt);
            }
        });

        txtBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBuscarActionPerformed(evt);
            }
        });

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Proveedor", "Producto", "Cantidad"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

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
        ));
        jScrollPane2.setViewportView(jTable2);

        jButton1.setText("Imprimir");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("->");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(BotonAtras)
                        .addGap(181, 181, 181)
                        .addComponent(jLabel1))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 259, Short.MAX_VALUE)
                        .addGap(44, 44, 44)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 307, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
            .addGroup(layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addGap(284, 284, 284))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addComponent(BotonAtras)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(170, 170, 170)
                        .addComponent(jButton2))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(60, 60, 60)
                        .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 296, Short.MAX_VALUE)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addGap(92, 92, 92))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BotonAtrasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BotonAtrasActionPerformed
        // TODO add your handling code here:

        this.dispose();
        Mainn main=new Mainn();
        main.setVisible(true);
    }//GEN-LAST:event_BotonAtrasActionPerformed

    private void txtBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBuscarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBuscarActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        LocalDate fechaActual = LocalDate.now();
        Date fechaSQL = Date.valueOf(fechaActual);
        for (int i = 0; i < jTable2.getRowCount(); i++) {
            
            String nombre= (String) jTable2.getValueAt(i, 0);
            int cantidadPedido = (Integer) jTable2.getValueAt(i, 1);
            int proveedor=con.obtenerProveedor(nombre);
            int IdProducto=con.obtenerID(nombre);
            
            con.agregarPedido(IdProducto, 2, cantidadPedido, fechaSQL);
        }
        
        
        
        
        
        
        
        Proveedor1 proveedor1=new Proveedor1();
        proveedor1.setLocation(100, 100);
        proveedor1.setVisible(true);
        
        Proveedor2 proveedor2=new Proveedor2();
        proveedor2.setLocation(600, 100);
        proveedor2.setVisible(true);
        
        Proveedor3 proveedor3=new Proveedor3();
        proveedor3.setLocation(1100, 100);
        proveedor3.setVisible(true);
        
        
        
        
        
        
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        int filaSeleccionada = jTable1.getSelectedRow(); 
    if (filaSeleccionada != -1) {
        String nombreProducto = (String) jTable1.getValueAt(filaSeleccionada, 0); 
        double stockProducto = Integer.parseInt(jTable1.getValueAt(filaSeleccionada, 1).toString()); 

        String cantidadStr = Aviso.pedirTexto(null, "Ingrese la cantidad para " + nombreProducto);
        if (cantidadStr != null && !cantidadStr.trim().isEmpty()) {
            try {
                int cantidadPedir = Integer.parseInt(cantidadStr);

                model.addRow(new Object[]{nombreProducto, cantidadPedir});
            } catch (NumberFormatException e) {
                Aviso.mostrar(null, "Ingrese una cantidad válida");
            }
        }
    } else {
        Aviso.mostrar(null, "Seleccione un producto de la tabla");
    }
    }//GEN-LAST:event_jButton2ActionPerformed

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
            java.util.logging.Logger.getLogger(Pedidos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Pedidos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Pedidos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Pedidos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Pedidos().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BotonAtras;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JPopupMenu jPopupMenu2;
    private javax.swing.JPopupMenu jPopupMenu3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTextField txtBuscar;
    // End of variables declaration//GEN-END:variables
}
