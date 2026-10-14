/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Vistas;

import com.mycompany.inventia.ConexionBD;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author lttlk
 */
public class Detalle extends javax.swing.JFrame {
    
    DefaultTableModel dtm = new DefaultTableModel();
    ConexionBD cn = new ConexionBD(); 


    /**
     * Creates new form Detalle
     */
    public Detalle() {
        Tema.instalar();
        initComponents();
        this.setLocationRelativeTo(null);
        
        
        String[] titulo = new String[] {"Hora", "Total"};
        dtm.setColumnIdentifiers(titulo);
        jTableDetalle.setModel(dtm);
        listarDetalleDiario(new java.sql.Date(System.currentTimeMillis()));

    // Muestra la fecha de hoy en el selector (antes del listener, para no repetir la consulta)
    DateChooser.setDate(new java.util.Date());

    // ---------- AÑADIMOS EL LISTENER ----------
    DateChooser.getDateEditor().addPropertyChangeListener(evt -> {
        if ("date".equals(evt.getPropertyName())) {
            java.util.Date fechaUtil = DateChooser.getDate();
            if (fechaUtil != null) {
                java.sql.Date fechaSQL = new java.sql.Date(fechaUtil.getTime());
                listarDetalleDiario(fechaSQL); // Llama al método que llena la tabla
            
            }
        }
    }); 
        disenarDetalle();
    }

    // ------------------------------------------------------------------
    // Diseño visual. Reordena los mismos componentes de initComponents()
    // en una tarjeta con el detalle del día. No toca la lógica.
    // ------------------------------------------------------------------

    private void disenarDetalle() {
        setTitle("Inventia");
        javax.swing.JPanel cabecera = Estilo.cabecera(BotonAtras, jLabel1, "Detalle de ventas");

        Estilo.Pastilla contador = new Estilo.Pastilla();
        DateChooser.setPreferredSize(new java.awt.Dimension(190, 38));
        DateChooser.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 15));

        javax.swing.JPanel encabezado = new javax.swing.JPanel(new java.awt.BorderLayout(12, 0));
        encabezado.setOpaque(false);
        encabezado.setBorder(javax.swing.BorderFactory.createEmptyBorder(16, 14, 16, 14));
        encabezado.add(Estilo.tituloConPastilla("Ventas del día", contador), java.awt.BorderLayout.WEST);
        encabezado.add(DateChooser, java.awt.BorderLayout.EAST);

        Estilo.tabla(jTableDetalle, jScrollPane1);
        jScrollPane1.setPreferredSize(new java.awt.Dimension(620, 340));
        Estilo.columnas(jTableDetalle, new String[]{"Hora", "Total"}, new int[]{300, 300});
        jTableDetalle.getColumnModel().getColumn(0).setCellRenderer(new Estilo.Celda(Estilo.Celda.TEXTO, javax.swing.SwingConstants.LEFT, false, Tema.TEXTO));
        jTableDetalle.getColumnModel().getColumn(1).setCellRenderer(new Estilo.Celda(Estilo.Celda.PESOS, javax.swing.SwingConstants.RIGHT, true, Tema.TEXTO));

        javax.swing.JLabel etiquetaTotal = new javax.swing.JLabel("Total del día");
        etiquetaTotal.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));
        etiquetaTotal.setForeground(Tema.TEXTO_SUAVE);
        totalLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 40));
        totalLabel.setForeground(Tema.TEXTO);
        javax.swing.JPanel pie = new javax.swing.JPanel(new java.awt.BorderLayout());
        pie.setOpaque(false);
        pie.setBorder(javax.swing.BorderFactory.createEmptyBorder(14, 14, 16, 14));
        pie.add(etiquetaTotal, java.awt.BorderLayout.NORTH);
        pie.add(totalLabel, java.awt.BorderLayout.CENTER);

        Superficie tarjeta = new Superficie(new java.awt.BorderLayout());
        tarjeta.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        tarjeta.add(encabezado, java.awt.BorderLayout.NORTH);
        tarjeta.add(jScrollPane1, java.awt.BorderLayout.CENTER);
        tarjeta.add(pie, java.awt.BorderLayout.SOUTH);

        javax.swing.JPanel cuerpo = new javax.swing.JPanel(new java.awt.BorderLayout());
        cuerpo.setBackground(Tema.FONDO);
        cuerpo.setBorder(javax.swing.BorderFactory.createEmptyBorder(22, 22, 22, 22));
        cuerpo.add(tarjeta, java.awt.BorderLayout.CENTER);

        javax.swing.JPanel raiz = new javax.swing.JPanel(new java.awt.BorderLayout());
        raiz.add(cabecera, java.awt.BorderLayout.NORTH);
        raiz.add(cuerpo, java.awt.BorderLayout.CENTER);
        setContentPane(raiz);

        Runnable actualizar = () -> {
            int n = dtm.getRowCount();
            contador.setText(n == 1 ? "1 venta" : n + " ventas");
        };
        dtm.addTableModelListener(e -> javax.swing.SwingUtilities.invokeLater(actualizar));
        actualizar.run();

        pack();
        setMinimumSize(getPreferredSize());
        setLocationRelativeTo(null);
    }
    
       private void listarDetalleDiario(java.sql.Date fechaSQL) {
    Connection conexion = cn.establecerConexion("root", "ADMIN12341");
    DefaultTableModel modelo = (DefaultTableModel) jTableDetalle.getModel();
    modelo.setRowCount(0);

    String sql = """
        SELECT hora, totalVenta
        FROM detalleventa
        WHERE fecha = ?
        ORDER BY hora
    """;

    int totalDia = 0;

    try (PreparedStatement ps = conexion.prepareStatement(sql)) {
        ps.setDate(1, fechaSQL);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            modelo.addRow(new Object[]{
                rs.getTime("hora"),
                rs.getInt("totalVenta")
            });
            totalDia += rs.getInt("totalVenta");
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    totalLabel.setText(Estilo.pesos(totalDia));
}


    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        BotonAtras = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableDetalle = new javax.swing.JTable();
        totalLabel = new javax.swing.JLabel();
        DateChooser = new com.toedter.calendar.JDateChooser();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Constantia", 0, 40)); // NOI18N
        jLabel1.setText("Detalle diario");

        BotonAtras.setText("Atras");
        BotonAtras.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BotonAtrasActionPerformed(evt);
            }
        });

        jTableDetalle.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jTableDetalle.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Producto", "Cantidad", "Precio", "Total"
            }
        ));
        jScrollPane1.setViewportView(jTableDetalle);

        totalLabel.setFont(new java.awt.Font("Segoe UI", 0, 36)); // NOI18N
        totalLabel.setText("Total dia:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(113, 113, 113)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 426, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(BotonAtras)
                        .addGap(113, 113, 113)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(DateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(157, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(totalLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 304, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(168, 168, 168))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(BotonAtras)
                    .addComponent(DateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(19, 19, 19)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 359, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37)
                .addComponent(totalLabel)
                .addContainerGap(38, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BotonAtrasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BotonAtrasActionPerformed
        // TODO add your handling code here:

        this.dispose();
        Mainn main=new Mainn();
        main.setVisible(true);
    }//GEN-LAST:event_BotonAtrasActionPerformed

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
            java.util.logging.Logger.getLogger(Detalle.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Detalle.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Detalle.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Detalle.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Detalle().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BotonAtras;
    private com.toedter.calendar.JDateChooser DateChooser;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTableDetalle;
    private javax.swing.JLabel totalLabel;
    // End of variables declaration//GEN-END:variables
}
