/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.inventia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
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
        String query = "SELECT producto, cantidad, precio, (cantidad * precio) AS total FROM ventas WHERE fecha = CURDATE()";

        try {
            Statement stmt = conexion.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                String[] fila = new String[4];
                fila[0] = rs.getString("producto");
                fila[1] = String.valueOf(rs.getInt("cantidad"));
                fila[2] = String.valueOf(rs.getDouble("precio"));
                fila[3] = String.valueOf(rs.getDouble("total"));

                modelo.addRow(fila); // Añadir la fila a la tabla
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al listar el detalle diario: " + e.getMessage());
        }
    }
}

