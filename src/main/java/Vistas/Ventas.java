/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Vistas;
import com.mycompany.inventia.ConexionBD;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
/**
 *
 * @author lttlk
 */
public class Ventas extends javax.swing.JFrame {

    DefaultTableModel modeloProductos = new DefaultTableModel();
    DefaultTableModel modeloBoleta = new DefaultTableModel();
    TableRowSorter<DefaultTableModel> sorter;
    ConexionBD cn = new ConexionBD();
    /**
     * Creates new form NewJFrame
     */
    public Ventas() {
        initComponents();
        this.setLocationRelativeTo(null);
        String[] titulo = new String[] {"Producto", "Precio"};
    modeloProductos.setColumnIdentifiers(titulo);
    jTable2.setModel(modeloProductos); 
    
    String[] titulo2=new String[]{"Producto","Cantidad","Precio Unitario","Total"};
    modeloBoleta.setColumnIdentifiers(titulo2);
    jTable3.setModel(modeloBoleta);
    
    
    sorter = new TableRowSorter<>(modeloProductos);
    jTable2.setRowSorter(sorter);
        
        txtBuscar.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String texto = txtBuscar.getText();
                filtrar(texto);
            }
        });
    }
    private void filtrar(String texto) {
        if (texto.trim().length() == 0) {
            sorter.setRowFilter(null); 
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + texto, 0)); 
        }
    }

    private void sumatotal(){
        double totalVenta = 0;
    for (int i = 0; i < jTable3.getRowCount(); i++) {
        totalVenta += (double) jTable3.getValueAt(i, 3); 
    }
    totalLabel.setText("Total: $" + totalVenta);
    }
    
    private void restarTotal() {
    int total = 0; // Iniciamos el total en 0

    DefaultTableModel modelo = (DefaultTableModel) jTable3.getModel();
    int numFilas = modelo.getRowCount(); // Número de filas en la tabla

    // Recorremos todas las filas para recalcular el total
    for (int i = 0; i < numFilas; i++) {
        // Obtenemos el valor de la columna del "Total" (suponiendo que esté en la columna 3)
        double subtotal = (double) modelo.getValueAt(i, 3);
        total += subtotal;
    }

    // Actualizamos el label con el nuevo total
    totalLabel.setText("Total: $" + total);
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
        jButton1 = new javax.swing.JButton();
        txtBuscar = new javax.swing.JTextField();
        Sumar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        ImprimirButon = new javax.swing.JButton();
        Restar = new javax.swing.JButton();
        totalLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel2.setFont(new java.awt.Font("Constantia", 0, 52)); // NOI18N
        jLabel2.setText("Inventia");

        BotonAtras.setText("Atras");
        BotonAtras.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BotonAtrasActionPerformed(evt);
            }
        });

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

        jButton1.setText("Listar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        txtBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBuscarActionPerformed(evt);
            }
        });

        Sumar.setText("->");
        Sumar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SumarActionPerformed(evt);
            }
        });

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

        ImprimirButon.setText("Imprimir");
        ImprimirButon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ImprimirButonActionPerformed(evt);
            }
        });

        Restar.setText("<-");
        Restar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RestarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(BotonAtras)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(42, 42, 42)
                        .addComponent(jButton1))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 246, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(238, 238, 238)
                                .addComponent(ImprimirButon)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(Sumar)
                                    .addComponent(Restar))
                                .addGap(19, 19, 19)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(118, 118, 118)
                                        .addComponent(totalLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 360, Short.MAX_VALUE))))))
                .addGap(37, 37, 37)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(BotonAtras))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addComponent(jButton1)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(14, 14, 14)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 285, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(90, 90, 90)
                                .addComponent(Sumar)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(Restar)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(totalLabel)
                        .addGap(23, 23, 23)
                        .addComponent(ImprimirButon, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 399, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(58, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BotonAtrasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BotonAtrasActionPerformed

        this.dispose(); 
        Mainn main=new Mainn();
        main.setVisible(true);
    }//GEN-LAST:event_BotonAtrasActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        Connection conexion = cn.establecerConexion("root", "ADMIN12341");
        cn.listarProductos(jTable2, conexion);
        
    }//GEN-LAST:event_jButton1ActionPerformed

    private void txtBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBuscarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBuscarActionPerformed

    private void SumarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SumarActionPerformed
        // TODO add your handling code here:
        // Botón para agregar a la boleta
        int filaSeleccionada = jTable2.getSelectedRow(); 
    if (filaSeleccionada != -1) {
        String nombreProducto = (String) jTable2.getValueAt(filaSeleccionada, 0); 
        double precioProducto = Double.parseDouble(jTable2.getValueAt(filaSeleccionada, 1).toString()); 

        String cantidadStr = JOptionPane.showInputDialog(null, "Ingrese la cantidad para " + nombreProducto);
        if (cantidadStr != null && !cantidadStr.trim().isEmpty()) {
            try {
                int cantidad = Integer.parseInt(cantidadStr);
                double total = cantidad * precioProducto;

                DefaultTableModel modeloBoleta = (DefaultTableModel) jTable3.getModel();
                modeloBoleta.addRow(new Object[]{nombreProducto, cantidad, precioProducto, total});

                sumatotal(); // Recalcula el total
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Ingrese una cantidad válida");
            }
        }
    } else {
        JOptionPane.showMessageDialog(null, "Seleccione un producto de la tabla");
    }
    

    }//GEN-LAST:event_SumarActionPerformed

    private void ImprimirButonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ImprimirButonActionPerformed
        // TODO add your handling code here:
        int opcion = JOptionPane.showConfirmDialog(null, "¿Hacer la venta?");

    // Si el usuario elige YES_OPTION
    if (opcion == JOptionPane.YES_OPTION) {
        // Recorre cada fila de jTable3
        for (int i = 0; i < jTable3.getRowCount(); i++) {
            // Obtiene la cantidad, precio y total de cada fila
            int numero=1;
            int cantidad = (Integer) jTable3.getValueAt(i, 1);
            double precio = (Double) jTable3.getValueAt(i, 2);
            double total = (Double) jTable3.getValueAt(i, 3);

            // Imprime los valores de cantidad, precio y total
            System.out.println("Cantidad: " + cantidad + ", Precio: " + precio + ", Total: " + total);
            cn.agregarVenta(numero, cantidad, precio);
        }
    } else if (opcion == JOptionPane.NO_OPTION) {
        System.out.println("No se realizó la venta.");
    }
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
    JOptionPane.showMessageDialog(null, "Seleccione una fila para eliminar.");
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
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    private javax.swing.JLabel totalLabel;
    private javax.swing.JTextField txtBuscar;
    // End of variables declaration//GEN-END:variables


}