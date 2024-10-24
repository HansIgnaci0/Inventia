/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.inventia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author hansi
 */
public class ConexionBD {

    static Connection getConnection() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public static void listarProductos(JTable jTable1, ConexionBD conexion) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public static void eliminarProducto(int id, ConexionBD conexion) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public static void modificarProducto(int id, String nombre, double precio, int cantidad, ConexionBD conexion) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public static void agregarProducto(int id, String nombre, double precio, int cantidad, ConexionBD conexion) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    Connection conectar=null;

    String bd="dbinventia";
    String ip="localhost";
    String puerto="3306";
    String cadena="jdbc:mysql://"+ip+":"+puerto+"/"+bd;
    
    public Connection establecerConexion(String usuario,String contrasenia){
        try{
            Class.forName("com.mysql.jdbc.Driver");
            conectar=DriverManager.getConnection(cadena, usuario,contrasenia);
            
        }catch(Exception e){
            JOptionPane.showMessageDialog(null,"No se conectó a la base de datos, error: "+e.toString());
        }
        return conectar;
    }
    
public boolean bConexion(String usuario, String contrasenia) {
    boolean booleanConexion;  
    
    // Comparamos las cadenas correctamente usando equals()
    if (usuario.equals("root") && contrasenia.equals("ADMIN12341")) {
        booleanConexion = true;  
    } else {
        booleanConexion = false;  
    }
    
    return booleanConexion;  
}

    Statement createStatement() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
public void listarProductos(JTable tabla, Connection conexion) {
    DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();
    modelo.setRowCount(0); 

    
    if (modelo.getColumnCount() == 0) {
        modelo.addColumn("ID Producto");
        modelo.addColumn("Nombre");
        modelo.addColumn("Precio");
    }

    String query = "SELECT NOMBRE, PRECIO FROM producto"; 
    
    try {
        Statement stmt = conexion.createStatement();
        ResultSet rs = stmt.executeQuery(query);

        
        while (rs.next()) {
            String[] fila = new String[2];
            fila[0] = rs.getString("NOMBRE");
            fila[1] = String.valueOf(rs.getDouble("PRECIO"));

            modelo.addRow(fila); 
        }

      

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Error al listar los productos: " + e.getMessage());
    }
}


public void listarDetalleDiario(JTable tabla, Connection conexion) {
        DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();
        modelo.setRowCount(0); // Limpiar la tabla

        // Definir las columnas del detalle diario
        if (modelo.getColumnCount() == 0) {
            modelo.addColumn("Producto");
            modelo.addColumn("Cantidad");
            modelo.addColumn("Precio");
            modelo.addColumn("Total");
        }

        // Consulta SQL para obtener las ventas del día
        String query = "SELECT id_producto,cantidad, precio_unitario, (cantidad * precio_unitario) AS total FROM detalle_venta";

        try {
            Statement stmt = conexion.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                String[] fila = new String[4];
                fila[0] = rs.getString("id_producto");
                fila[1] = String.valueOf(rs.getInt("cantidad"));
                fila[2] = String.valueOf(rs.getDouble("precio_unitario"));
                fila[3] = String.valueOf(rs.getDouble("total"));

                modelo.addRow(fila); // Añadir la fila a la tabla
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al listar el detalle diario: " + e.getMessage());
        }
    }
    public void listarTodosLosProductos(JTable tabla, Connection conexion) {
        DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();
        modelo.setRowCount(0);

        String query = "SELECT ID, NOMBRE, PRECIO, CANTIDAD FROM producto"; 

        try {
            Statement stmt = conexion.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                String[] fila = new String[4];
                fila[0] = rs.getString("ID");
                fila[1] = rs.getString("NOMBRE");
                fila[2] = String.valueOf(rs.getDouble("PRECIO"));
                fila[3] = String.valueOf(rs.getInt("CANTIDAD"));
                modelo.addRow(fila);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al listar los productos: " + e.getMessage());
        }
    }
    
    public void agregarVenta(int id_producto, int cantidad, double precio){
    String query = "INSERT INTO detalle_venta (ID_PRODUCTO, CANTIDAD, PRECIO_UNITARIO) VALUES (?, ?, ?)";
    
    try {
        PreparedStatement ps = conectar.prepareStatement(query);
        ps.setInt(1, id_producto);
        ps.setInt(2, cantidad);
        ps.setDouble(3, precio);
        ps.executeUpdate();
        ps.close();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}


    public void agregarProducto(String nombre, double precio, int cantidad, Connection conexion) {
        String query = "INSERT INTO producto (NOMBRE, PRECIO, CANTIDAD) VALUES ('" + nombre + "', " + precio + ", " + cantidad + ")";

        try {
            Statement stmt = conexion.createStatement();
            stmt.executeUpdate(query);
            JOptionPane.showMessageDialog(null, "Producto agregado exitosamente.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al agregar el producto: " + e.getMessage());
        }
    }

    public void modificarProducto(int id, String nombre, double precio, int cantidad, Connection conexion) {
        String query = "UPDATE producto SET NOMBRE = '" + nombre + "', PRECIO = " + precio + ", CANTIDAD = " + cantidad + " WHERE ID = " + id;

        try {
            Statement stmt = conexion.createStatement();
            stmt.executeUpdate(query);
            JOptionPane.showMessageDialog(null, "Producto modificado exitosamente.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al modificar el producto: " + e.getMessage());
        }
    }

    public void eliminarProducto(int id, Connection conexion) {
        String query = "DELETE FROM producto WHERE ID = " + id;

        try {
            Statement stmt = conexion.createStatement();
            stmt.executeUpdate(query);
            JOptionPane.showMessageDialog(null, "Producto eliminado exitosamente.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar el producto: " + e.getMessage());
        }
    }
}

