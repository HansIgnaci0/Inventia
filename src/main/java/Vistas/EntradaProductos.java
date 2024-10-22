package Vistas;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import com.mycompany.inventia.ConexionBD;

public class EntradaProductos extends javax.swing.JFrame {

    private DefaultTableModel tableModel;
    private ConexionBD conexionBD;
    private Connection conexion;
    private NumberFormat currencyFormat;

    public EntradaProductos() {
        initComponents();
        this.setLocationRelativeTo(null);
        initTableModel();
        conexionBD = new ConexionBD();
        currencyFormat = NumberFormat.getCurrencyInstance();
        conexion = conexionBD.establecerConexion("root", "ADMIN12341");
        if (conexion != null) {
            cargarProductos();
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo conectar a la base de datos.", "Error de conexión", JOptionPane.ERROR_MESSAGE);
        }
        setLocationRelativeTo(null); // Centrar el JFrame en la pantalla
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        BotonAtras = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jTextField4 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButtonListar = new javax.swing.JButton(); // Botón Listar
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel2.setFont(new java.awt.Font("Constantia", 0, 36)); // NOI18N
        jLabel2.setText("Inventia");

        BotonAtras.setText("Atras");
        BotonAtras.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BotonAtrasActionPerformed(evt);
            }
        });

        jLabel1.setText("ID");

        jLabel3.setText("Nombre");

        jLabel4.setText("Precio");

        jLabel5.setText("Cantidad");

        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldActionPerformed(evt);
            }
        });

        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldActionPerformed(evt);
            }
        });

        jTextField3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldActionPerformed(evt);
            }
        });

        jTextField4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldActionPerformed(evt);
            }
        });

        jButton1.setText("Agregar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Modificar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Eliminar");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButtonListar.setText("Listar");
        jButtonListar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonListarActionPerformed(evt);
            }
        });

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {},
            new String [] {
                "ID", "Nombre", "Precio", "Cantidad"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap(166, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(71, 71, 71))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jTextField1)
                                .addGap(18, 18, 18)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addGap(38, 38, 38))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jTextField2)
                                .addGap(18, 18, 18)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addGap(49, 49, 49))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jTextField3)
                                .addGap(18, 18, 18))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addComponent(BotonAtras)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(114, 114, 114)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton3)))
                .addGap(20, 20, 20))
            .addGroup(layout.createSequentialGroup()
                .addGap(166, 166, 166)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 310, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jButtonListar)
                        .addGap(12, 12, 12)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(BotonAtras)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(jLabel4)
                            .addComponent(jLabel1)
                            .addComponent(jLabel3))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1)
                    .addComponent(jButton2)
                    .addComponent(jButton3))
                .addGap(18, 18, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 415, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonListar)
                .addContainerGap())
        );

        pack();
    }

    private void initTableModel() {
        tableModel = (DefaultTableModel) jTable1.getModel();
    }

    private void cargarProductos() {
        try (PreparedStatement stmt = conexion.prepareStatement("SELECT * FROM productos");
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String id = rs.getString("id");
                String nombre = rs.getString("nombre");
                double precio = rs.getDouble("precio");
                int cantidad = rs.getInt("cantidad");
                tableModel.addRow(new Object[]{id, nombre, currencyFormat.format(precio), cantidad});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void BotonAtrasActionPerformed(java.awt.event.ActionEvent evt) {
        
        this.dispose();
        Mainn main = new Mainn();
        main.setVisible(true);
    }

    private void jTextFieldActionPerformed(java.awt.event.ActionEvent evt) {
        JTextField source = (JTextField) evt.getSource();
        System.out.println("TextField content: " + source.getText());
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        if (conexion != null) {
            String id = jTextField1.getText();
            String nombre = jTextField2.getText();
            double precio = Double.parseDouble(jTextField3.getText());
            int cantidad = Integer.parseInt(jTextField4.getText());
            agregarProducto(id, nombre, precio, cantidad);
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo conectar a la base de datos.", "Error de conexión", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {
        if (conexion != null) {
            String id = jTextField1.getText();
            String nombre = jTextField2.getText();
            double precio = Double.parseDouble(jTextField3.getText());
            int cantidad = Integer.parseInt(jTextField4.getText());
            modificarProducto(id, nombre, precio, cantidad);
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo conectar a la base de datos.", "Error de conexión", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {
        if (conexion != null) {
            String id = jTextField1.getText();
            eliminarProducto(id);
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo conectar a la base de datos.", "Error de conexión", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void jButtonListarActionPerformed(java.awt.event.ActionEvent evt) {
        if (conexion != null) {
            cargarProductos();
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo conectar a la base de datos.", "Error de conexión", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Funciones CRUD
    private void agregarProducto(String id, String nombre, double precio, int cantidad) {
        try (PreparedStatement stmt = conexion.prepareStatement("INSERT INTO productos (id, nombre, precio, cantidad) VALUES (?, ?, ?, ?)")) {
            stmt.setString(1, id);
            stmt.setString(2, nombre);
            stmt.setDouble(3, precio);
            stmt.setInt(4, cantidad);
            stmt.executeUpdate();
            tableModel.addRow(new Object[]{id, nombre, currencyFormat.format(precio), cantidad});
            System.out.println("Producto agregado: ID=" + id + ", Nombre=" + nombre + ", Precio=" + currencyFormat.format(precio) + ", Cantidad=" + cantidad);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void modificarProducto(String id, String nombre, double precio, int cantidad) {
        try (PreparedStatement stmt = conexion.prepareStatement("UPDATE productos SET nombre = ?, precio = ?, cantidad = ? WHERE id = ?")) {
            stmt.setString(1, nombre);
            stmt.setDouble(2, precio);
            stmt.setInt(3, cantidad);
            stmt.setString(4, id);
            stmt.executeUpdate();

            int rowCount = tableModel.getRowCount();
            for (int i = 0; i < rowCount; i++) {
                if (tableModel.getValueAt(i, 0).equals(id)) {
                    tableModel.setValueAt(nombre, i, 1);
                    tableModel.setValueAt(currencyFormat.format(precio), i, 2);
                    tableModel.setValueAt(cantidad, i, 3);
                    System.out.println("Producto modificado: ID=" + id + ", Nombre=" + nombre + ", Precio=" + currencyFormat.format(precio) + ", Cantidad=" + cantidad);
                    return;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void eliminarProducto(String id) {
        try (PreparedStatement stmt = conexion.prepareStatement("DELETE FROM productos WHERE id = ?")) {
            stmt.setString(1, id);
            stmt.executeUpdate();

            int rowCount = tableModel.getRowCount();
            for (int i = 0; i < rowCount; i++) {
                if (tableModel.getValueAt(i, 0).equals(id)) {
                    tableModel.removeRow(i);
                    System.out.println("Producto eliminado: ID=" + id);
                    return;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new EntradaProductos().setVisible(true);
            }
        });
    }

    // Variables declaration
    private javax.swing.JButton BotonAtras;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButtonListar; // Declaración del botón Listar
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
}