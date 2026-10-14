/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.inventia;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.time.LocalDate;
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
    
    private Connection conectar = null;

    private final String bd = "dbcamila";
    private final String ip = "localhost";
    private final String puerto = "3306";
    private final String cadena = "jdbc:mysql://" + ip + ":" + puerto + "/" + bd + 
                                   "?useTimezone=true&serverTimezone=UTC";

    public Connection establecerConexion(String usuario, String contrasenia) {
        try {
            // Cambia a la clase moderna del driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Establece la conexión
            conectar = DriverManager.getConnection(cadena, usuario, contrasenia);
            
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "No se encontró el Driver: " + e.getMessage());
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al conectar a la base de datos: " + e.getMessage());
            e.printStackTrace(); // Esto imprime el detalle del error en la consola
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

    String query = "SELECT codigoProducto, nombreProducto, precioUnitarioProducto FROM producto";


    try {
        Statement stmt = conexion.createStatement();
        ResultSet rs = stmt.executeQuery(query);

        while (rs.next()) {
    Object[] fila = new Object[]{
        rs.getString("codigoProducto"),
        rs.getString("nombreProducto"),
        rs.getInt("precioUnitarioProducto")
    };
    modelo.addRow(fila);
}

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Error al listar los productos: " + e.getMessage());
    }
}



public void listarVentasDelDia(JTable tabla, Connection conexion) {

    DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();
    modelo.setRowCount(0);

    // Definir columnas si no existen
    if (modelo.getColumnCount() == 0) {
        modelo.addColumn("Hora");
        modelo.addColumn("Total");
    }

    String sql = """
        SELECT hora, totalVenta
        FROM detalleventa
        WHERE fecha = CURDATE()
        ORDER BY hora
    """;

    try (Statement st = conexion.createStatement();
         ResultSet rs = st.executeQuery(sql)) {

        while (rs.next()) {
            modelo.addRow(new Object[]{
                rs.getTime("hora"),
                rs.getInt("totalVenta")
            });
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Error al listar caja del día: " + e.getMessage());
    }
}

public int obtenerTotalDelDia() {

    String sql = "SELECT SUM(totalVenta) FROM detalleventa WHERE fecha = CURDATE()";

    try (PreparedStatement ps = conectar.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        if (rs.next()) {
            return rs.getInt(1);
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Error total del día: " + e.getMessage());
    }

    return 0;
}

    
   public int agregarVenta(int totalVenta, Date fecha, Time hora) {

    String query = "INSERT INTO detalleventa (totalVenta, fecha, hora) VALUES (?, ?, ?)";

    try {
        PreparedStatement ps = conectar.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

        ps.setInt(1, totalVenta);
        ps.setDate(2, fecha);
        ps.setTime(3, hora);

        ps.executeUpdate();

        // 🔥 Obtenemos el ID de la venta recién creada
        ResultSet rs = ps.getGeneratedKeys();
        if (rs.next()) {
            return rs.getInt(1); // idVenta
        }

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error al guardar venta: " + e.getMessage());
    }

    return -1; // error
}





    public void agregarProducto(String nombre, double precio, int cantidad, int id_producto) {
        String query = "INSERT INTO producto (ID_PRODUCTO, NOMBRE, STOCK, PRECIO) VALUES (?, ?, ?, ?)"; 
            try {
        PreparedStatement ps = conectar.prepareStatement(query);
        ps.setInt(1, id_producto);
        ps.setString(2, nombre);
        ps.setInt(3, cantidad);
        ps.setDouble(4, precio);
        
        ps.executeUpdate();
        ps.close();
    } catch (SQLException e) {
        e.printStackTrace();
    
        }
    }

    public void modificarProducto(String nombre,int cantidad) {
        String query = "UPDATE producto SET stockProducto = ? WHERE nombreProducto = ?";
        try {
            PreparedStatement ps = conectar.prepareStatement(query);
            ps.setInt(1, cantidad); // Primer marcador: stockProducto
            ps.setString(2, nombre);
            

            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al modificar el producto: " + e.getMessage());
        }
    }

public void eliminarProducto(String nombreProducto) {
    String sql = "DELETE FROM producto WHERE nombreProducto = ?";

    try {
        PreparedStatement ps = conectar.prepareStatement(sql);
        ps.setString(1, nombreProducto);
        ps.executeUpdate();
        ps.close();

        JOptionPane.showMessageDialog(null, "Producto eliminado correctamente");

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Error al eliminar: " + e.getMessage());
    }
}

public void insertarProductoGranel(String codigo, String nombre, double precio) {
    String sql = "INSERT INTO producto (codigoProducto, nombreProducto, precioUnitarioProducto, stockProducto, tipo) VALUES (?, ?, ?, NULL, 'GRANEL')";

    try (Connection con = establecerConexion("root","ADMIN12341");
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setString(1, codigo);
        ps.setString(2, nombre);
        ps.setDouble(3, precio);

        ps.executeUpdate();

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Error al crear granel: " + e.getMessage());
    }
}
public String generarCodigoGranel() {
    String codigo = "GR-0001";
    try {
        Connection cn = establecerConexion("root", "ADMIN12341");
        String sql = "SELECT MAX(codigoProducto) FROM producto WHERE tipo='GRANEL'";
        java.sql.PreparedStatement ps = cn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        if (rs.next() && rs.getString(1) != null) {
            String ultimo = rs.getString(1); // ej: GR-0007
            int num = Integer.parseInt(ultimo.substring(3));
            num++;
            codigo = String.format("GR-%04d", num);
        }

    } catch (Exception e) {
        e.printStackTrace();
    }
    return codigo;
}

    
    public void listarEntradaProducto(JTable tabla, Connection conexion) {
    DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();
    modelo.setRowCount(0);

    // Definir las columnas de la tabla
    if (modelo.getColumnCount() == 0) {
        modelo.addColumn("nombreProducto");
        modelo.addColumn("precioUnitarioProducto");
        modelo.addColumn("stockProducto"); // Incluyendo cantidad si es necesario
    }
    
    String query = "SELECT nombreProducto, precioUnitarioProducto, stockProducto  FROM producto";

    try {
        Statement stmt = conexion.createStatement();
        ResultSet rs = stmt.executeQuery(query);

        // Llenar las filas de la tabla
        while (rs.next()) {
            String[] fila = new String[3];
            fila[0] = String.valueOf(rs.getString("nombreProducto"));
            fila[1] = String.valueOf(rs.getInt("precioUnitarioProducto"));
            fila[2] = fila[2] = String.valueOf(rs.getInt("stockProducto"));

            modelo.addRow(fila);
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Error al listar los productos: " + e.getMessage());
    }
}
    public void listarpedidos(JTable tabla, Connection conexion,int idproveedor) {
    DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();
    modelo.setRowCount(0);

    // Definir las columnas de la tabla
    if (modelo.getColumnCount() == 0) {
        modelo.addColumn("Producto");
        modelo.addColumn("Cantidad Solicitada"); // Incluyendo cantidad si es necesario
    }

    String query = "SELECT p.nombreProducto, ped.cantidadSolicitada " +
               "FROM pedidos ped " +
               "INNER JOIN producto p ON ped.idProducto = p.idProducto " +
               "WHERE p.idProveedor = "+idproveedor;


    try {
        Statement stmt = conexion.createStatement();
        ResultSet rs = stmt.executeQuery(query);

        // Llenar las filas de la tabla
        while (rs.next()) {
            String[] fila = new String[2];
            fila[0] = String.valueOf(rs.getString("nombreProducto"));
            fila[1] = String.valueOf(rs.getInt("cantidadSolicitada"));

            modelo.addRow(fila);
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Error al listar los productos: " + e.getMessage());
    }
}
    public int obtenerProveedor(String nombre) {
    String query = "SELECT idProveedor FROM producto WHERE nombreProducto = ?";
    int proveedor = -1; // Valor inicial para manejar posibles errores o resultados vacíos

    try {
        // Preparar la consulta
        PreparedStatement ps = conectar.prepareStatement(query);
        ps.setString(1, nombre);

        // Ejecutar la consulta
        ResultSet rs = ps.executeQuery();

        // Leer el resultado
        if (rs.next()) {
            proveedor = rs.getInt("idProveedor");
        }

        // Cerrar recursos
        rs.close();
        ps.close();
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error al obtener el stock: " + e.getMessage());
    }

    
    return proveedor;
}
    public int obtenerID(String nombre) {
    String query = "SELECT idProducto FROM producto WHERE nombreProducto = ?";
    int ID = -1; // Valor inicial para manejar posibles errores o resultados vacíos

    try {
        // Preparar la consulta
        PreparedStatement ps = conectar.prepareStatement(query);
        ps.setString(1, nombre);

        // Ejecutar la consulta
        ResultSet rs = ps.executeQuery();

        // Leer el resultado
        if (rs.next()) {
            ID = rs.getInt("idProducto");
        }

        // Cerrar recursos
        rs.close();
        ps.close();
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error al obtener el stock: " + e.getMessage());
    }

    
    return ID;
}

public Integer obtenerStock(String nombre) {

    String query = "SELECT stockProducto FROM producto WHERE nombreProducto = ?";

    try {
        PreparedStatement ps = conectar.prepareStatement(query);
        ps.setString(1, nombre);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            int stock = rs.getInt("stockProducto");

            if (rs.wasNull()) {
                return null;   // 👉 GRANEL
            }

            return stock;      // 👉 NORMAL
        }

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error al obtener stock: " + e.getMessage());
    }

    return null;
}
    
    public void listarPedidos(JTable tabla, Connection conexion) {
    DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();
    modelo.setRowCount(0);

    // Definir las columnas de la tabla
    if (modelo.getColumnCount() == 0) {
        modelo.addColumn("nombreProducto");
        modelo.addColumn("stockProducto"); // Incluyendo cantidad si es necesario
    }

    String query = "SELECT nombreProducto, stockProducto  FROM producto ";

    try {
        Statement stmt = conexion.createStatement();
        ResultSet rs = stmt.executeQuery(query);

        // Llenar las filas de la tabla
        while (rs.next()) {
            String[] fila = new String[3];
            fila[0] = String.valueOf(rs.getString("nombreProducto"));
            fila[1] = String.valueOf(rs.getInt("stockProducto"));

            modelo.addRow(fila);
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Error al listar los productos: " + e.getMessage());
    }
}
    public void agregarPedido(int id_producto, int idtrabajor, int cantidad, java.sql.Date fecha) {
    // La secuencia idDetalleVenta_SEQ.NEXTVAL se maneja directamente en la consulta
        
    String query = "INSERT INTO pedidos(idProducto, idTrabajador, cantidadSolicitada, fechaSolicitud) "
        + "VALUES (?, ?, ?, ?)";
        
    try {
        // Preparar la consulta con los parámetros
        PreparedStatement ps = conectar.prepareStatement(query);
        
        // Asignación de parámetros
        ps.setInt(1, id_producto);   // Establece el id_producto
        ps.setInt(2, idtrabajor);   // Establece el id del trabajador
        ps.setInt(3, cantidad);     // Establece la cantidad solicitada
        ps.setDate(4, fecha);       // Establece la fecha de solicitud

        // Ejecutar la consulta
        int filasAfectadas = ps.executeUpdate();

        // Verificar si se insertó correctamente
        

        // Cerrar el PreparedStatement
        ps.close();
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error al imprimir los datos: " + e.getMessage());
    }
}
    public void agregarTotalDia(int total,java.sql.Date fecha) {
    
        String query = "INSERT INTO ingresos_dia(fecha, totalDia) VALUES (?, ?)";

    try {
        // Preparar la consulta con los parámetros
        PreparedStatement ps = conectar.prepareStatement(query);

        // Asignación de parámetros
        ps.setDate(1, fecha);   // Primer parámetro: fechaVenta
        ps.setInt(2, total);    // Segundo parámetro: ingresosTotal

        // Ejecutar la consulta
        int filasAfectadas = ps.executeUpdate();

        // Verificar si se insertó correctamente
        if (filasAfectadas > 0) {
            JOptionPane.showMessageDialog(null, "Datos insertados correctamente.");
        } else {
            JOptionPane.showMessageDialog(null, "No se pudo insertar los datos.");
        }

        // Cerrar el PreparedStatement
        ps.close();
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error al insertar los datos: " + e.getMessage());
    }
        
    
}
    public ResultSet buscarProductoPorCodigo(String codigo) {
    String sql = """
        SELECT codigoProducto, nombreProducto, stockProducto, precioUnitarioProducto 
        FROM producto 
        WHERE codigoProducto = ?
    """;

    try {
        PreparedStatement ps = conectar.prepareStatement(sql);
        ps.setString(1, codigo);
        return ps.executeQuery();
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Error buscar producto: " + e.getMessage());
    }
    return null;
}

    public void actualizarProducto(String codigo, int nuevoStock, int nuevoPrecio) {
    String sql = """
        UPDATE producto 
        SET stockProducto = ?, precioUnitarioProducto = ? 
        WHERE codigoProducto = ?
    """;

    try {
        PreparedStatement ps = conectar.prepareStatement(sql);
        ps.setInt(1, nuevoStock);
        ps.setInt(2, nuevoPrecio);
        ps.setString(3, codigo);
        ps.executeUpdate();
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Error al actualizar producto: " + e.getMessage());
    }
}

    public void insertarProductoNuevo(String codigo, String nombre, int precio, int stock) {
    String sql = """
        INSERT INTO producto 
        (codigoProducto, nombreProducto, precioUnitarioProducto, stockProducto)
        VALUES (?, ?, ?, ?)
    """;

    try (Connection con = establecerConexion("root", "ADMIN12341");
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setString(1, codigo);
        ps.setString(2, nombre);
        ps.setInt(3, precio);
        ps.setInt(4, stock);

        ps.executeUpdate();

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Error al crear producto: " + e.getMessage());
    }
}

    public String obtenerTipo(String codigo) {

    String sql = "SELECT tipo FROM producto WHERE codigoProducto = ?";

    try {
        PreparedStatement ps = conectar.prepareStatement(sql);
        ps.setString(1, codigo);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return rs.getString("tipo");  // NORMAL o GRANEL
        }

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error al obtener tipo: " + e.getMessage());
    }

    return "NORMAL"; // por defecto (por seguridad)
}
public void actualizarProductoPorNombre(String nombre, int stock, double precio) {
    try {
        Connection con = establecerConexion("root", "ADMIN12341");
        String sql = "UPDATE producto SET stockProducto=?, precioUnitarioProducto=? WHERE nombreProducto=?";
        java.sql.PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, stock);
        ps.setDouble(2, precio);
        ps.setString(3, nombre);
        ps.executeUpdate();
    } catch (Exception e) {
        e.printStackTrace();
    }
}
public void actualizarPrecioPorNombre(String nombre, double precio) {
    try {
        Connection con = establecerConexion("root", "ADMIN12341");
        String sql = "UPDATE producto SET precioUnitarioProducto=? WHERE nombreProducto=?";
        java.sql.PreparedStatement ps = con.prepareStatement(sql);
        ps.setDouble(1, precio);
        ps.setString(2, nombre);
        ps.executeUpdate();
    } catch (Exception e) {
        e.printStackTrace();
    }
}
public String buscarCodigoPorNombre(String nombre) {
    try {
        Connection con = establecerConexion("root","ADMIN12341");
        String sql = "SELECT codigoProducto FROM producto WHERE nombreProducto=?";
        java.sql.PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, nombre);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getString("codigoProducto");
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return "";
}
public void actualizarGranel(String nombreViejo, String nombreNuevo, double precio) {
    try {
        Connection con = establecerConexion("root", "ADMIN12341");
        String sql = "UPDATE producto SET nombreProducto=?, precioUnitarioProducto=? WHERE nombreProducto=?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, nombreNuevo);
        ps.setDouble(2, precio);
        ps.setString(3, nombreViejo);
        ps.executeUpdate();
    } catch (Exception e) {
        e.printStackTrace();
    }
}

public void actualizarNormal(String nombreViejo, String nombreNuevo, int stock, double precio) {
    try {
        Connection con = establecerConexion("root", "ADMIN12341");
        String sql = "UPDATE producto SET nombreProducto=?, precioUnitarioProducto=?, stockProducto=? WHERE nombreProducto=?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, nombreNuevo);
        ps.setDouble(2, precio);
        ps.setInt(3, stock);
        ps.setString(4, nombreViejo);
        ps.executeUpdate();
    } catch (Exception e) {
        e.printStackTrace();
    }
}






}

