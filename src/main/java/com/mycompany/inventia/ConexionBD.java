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
            JOptionPane.showMessageDialog(null,"Se conectó a la base de datos");
        }catch(Exception e){
            JOptionPane.showMessageDialog(null,"No se conectó a la base de datos, error: "+e.toString());
        }
        return conectar;
    }
    
public boolean bConexion(String usuario, String contrasenia) {
    boolean booleanConexion;  // Declaramos la variable para guardar el estado de la conexión
    
    // Comparamos las cadenas correctamente usando equals()
    if (usuario.equals("root") && contrasenia.equals("ADMIN12341")) {
        booleanConexion = true;  // Si las credenciales son correctas
    } else {
        booleanConexion = false;  // Si las credenciales son incorrectas
    }
    
    return booleanConexion;  // Devolvemos el resultado
}

    Statement createStatement() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
public void listarProductos(JTable tabla, Connection conexion) {
        // Crear el modelo de la tabla con dos columnas
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("ID Producto");
        modelo.addColumn("Nombre");

        String query = "SELECT ID_PRODUCTO, NOMBRE FROM producto"; // Cambia 'producto' por el nombre de tu tabla
        
        try {
            Statement stmt = conexion.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            // Recorrer los resultados de la consulta
            while (rs.next()) {
                String[] fila = new String[2];
                fila[0] = rs.getString("id"); // Cambia 'id' por el nombre de la columna
                fila[1] = rs.getString("nombre"); // Cambia 'nombre' por el nombre de la columna

                modelo.addRow(fila); // Añadir la fila al modelo
            }

            // Asignar el modelo a la tabla
            tabla.setModel(modelo);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al listar los productos: " + e.toString());
        }
    }
}    


