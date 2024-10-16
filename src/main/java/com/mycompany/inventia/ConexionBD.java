/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.inventia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import javax.swing.JOptionPane;

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

}
