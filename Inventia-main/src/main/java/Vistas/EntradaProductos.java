/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Vistas;

import com.mycompany.inventia.ConexionBD;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.RowFilter;
/**
 *
 * @author wizfa
 */
public class EntradaProductos extends javax.swing.JFrame {
    private ConexionBD conexion;
    private Connection conn;
            ConexionBD con = new ConexionBD();
    DefaultTableModel modeloProductos = new DefaultTableModel();
    TableRowSorter<DefaultTableModel> sorter;


    ConexionBD cn = new ConexionBD();
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

    private void activarBuscador() {
    txtBuscar.addKeyListener(new KeyAdapter() {
        @Override
        public void keyReleased(KeyEvent e) {
            String texto = txtBuscar.getText().trim();

            // Si está vacío, mostrar todo
            if (texto.isEmpty()) {
                sorter.setRowFilter(null);
                return;
            }

            // Si son letras → filtrar por nombre (columna 0)
            if (!texto.matches("\\d+")) {
                sorter.setRowFilter(RowFilter.regexFilter("(?i)" + texto, 0));
            } 
            else {
                sorter.setRowFilter(null); // números = scanner
            }
        }
    });
}

private void agregarProductoGranel() {
    try {
        JTextField txtNombre = new JTextField();
        JTextField txtPrecio = new JTextField();

        JPanel panel = new JPanel();
        panel.setLayout(new java.awt.GridLayout(0,1));
        panel.add(new JLabel("Nombre del producto a granel:"));
        panel.add(txtNombre);
        panel.add(new JLabel("Precio por kilo:"));
        panel.add(txtPrecio);

        int opcion = Aviso.confirmar(
                this,
                panel,
                "Producto a granel",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (opcion != JOptionPane.OK_OPTION) return;

        String nombre = txtNombre.getText().trim();
        double precio = Double.parseDouble(txtPrecio.getText().trim());

        if (nombre.isEmpty() || precio <= 0) {
            Aviso.mostrar(this,"Datos inválidos");
            return;
        }

        // Generar código único
        String codigo = cn.generarCodigoGranel();


        cn.insertarProductoGranel(codigo, nombre, precio);

        Connection con = cn.establecerConexion("root","ADMIN12341");
        cn.listarEntradaProducto(jTable1, con);

        Aviso.mostrar(this,"Producto granel agregado ✔");

    } catch (Exception e) {
        Aviso.mostrar(this,"Error: " + e.getMessage());
    }
}



    


    /**
     * 
     * Creates new form EntradaProductos
     */
    public EntradaProductos() {
        Tema.instalar();
        initComponents();
        mantenerFocoScanner();
        this.setLocationRelativeTo(null);
        String[] titulo = new String[] {"Producto", "Precio","Stock"};
    modeloProductos.setColumnIdentifiers(titulo);
    jTable1.setModel(modeloProductos);
    sorter = new TableRowSorter<>(modeloProductos);
    jTable1.setRowSorter(sorter);
    Connection conexion = con.establecerConexion("root", "ADMIN12341");
    con.listarEntradaProducto(jTable1, conexion);
    SwingUtilities.invokeLater(() -> {
    txtBuscar.requestFocusInWindow();
    });
    activarBuscador();
        disenarEntrada();
    }

    // ------------------------------------------------------------------
    // Diseño visual. Reordena los mismos componentes de initComponents()
    // en un catálogo (izquierda) y un panel de producto seleccionado con
    // acciones (derecha). No toca la lógica: cada botón conserva su
    // ActionListener original.
    // ------------------------------------------------------------------

    private Estilo.Pastilla contadorProductos;
    private JLabel lblMostrando;
    private javax.swing.JTextArea selNombre;
    private JLabel selPrecio;
    private JLabel selStock;

    private void disenarEntrada() {
        setTitle("Inventia");
        JPanel cabecera = Estilo.cabecera(BotonAtras, new JLabel(), "Entrada de productos");

        // ---- Catálogo
        contadorProductos = new Estilo.Pastilla();
        txtBuscar.putClientProperty("JTextField.placeholderText", "Buscar por nombre o escanear código");
        txtBuscar.putClientProperty("JTextField.leadingIcon", Iconos.lupa(Tema.TEXTO_SUAVE, 16));
        txtBuscar.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 15));
        txtBuscar.setPreferredSize(new java.awt.Dimension(340, 38));

        JPanel encabezado = new JPanel(new java.awt.BorderLayout(12, 0));
        encabezado.setOpaque(false);
        encabezado.setBorder(javax.swing.BorderFactory.createEmptyBorder(16, 14, 16, 14));
        encabezado.add(Estilo.tituloConPastilla("Catálogo de productos", contadorProductos), java.awt.BorderLayout.WEST);
        encabezado.add(txtBuscar, java.awt.BorderLayout.EAST);

        Estilo.tabla(jTable1, jScrollPane1);
        jScrollPane1.setPreferredSize(new java.awt.Dimension(700, 440));
        Estilo.columnas(jTable1, new String[]{"Producto", "Precio", "Stock"}, new int[]{380, 130, 110});
        jTable1.getColumnModel().getColumn(0).setCellRenderer(new Estilo.Celda(Estilo.Celda.TEXTO, javax.swing.SwingConstants.LEFT, false, Tema.TEXTO));
        jTable1.getColumnModel().getColumn(1).setCellRenderer(new Estilo.Celda(Estilo.Celda.PESOS, javax.swing.SwingConstants.RIGHT, true, Tema.TEXTO));
        jTable1.getColumnModel().getColumn(2).setCellRenderer(new Estilo.Celda(Estilo.Celda.TEXTO, javax.swing.SwingConstants.RIGHT, false, Tema.TEXTO));
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2 && jTable1.getSelectedRow() != -1) {
                    modificarBtn.doClick();
                }
            }
        });

        lblMostrando = Estilo.textoPie("");
        JPanel pie = new JPanel(new java.awt.BorderLayout());
        pie.setOpaque(false);
        pie.setBorder(javax.swing.BorderFactory.createEmptyBorder(12, 14, 12, 14));
        pie.add(lblMostrando, java.awt.BorderLayout.WEST);
        pie.add(Estilo.textoPie("Doble clic para modificar"), java.awt.BorderLayout.EAST);

        Superficie catalogo = new Superficie(new java.awt.BorderLayout());
        catalogo.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        catalogo.add(encabezado, java.awt.BorderLayout.NORTH);
        catalogo.add(jScrollPane1, java.awt.BorderLayout.CENTER);
        catalogo.add(pie, java.awt.BorderLayout.SOUTH);

        // ---- Producto seleccionado
        JLabel etiqueta = Estilo.textoPie("Producto seleccionado");
        // Área de texto de 2 líneas: ajusta nombres largos sin cortarlos.
        selNombre = new javax.swing.JTextArea(2, 1);
        selNombre.setLineWrap(true);
        selNombre.setWrapStyleWord(true);
        selNombre.setEditable(false);
        selNombre.setFocusable(false);
        selNombre.setOpaque(false);
        selNombre.setBorder(null);
        selNombre.setForeground(Tema.TEXTO);
        selPrecio = new JLabel();
        selStock = new JLabel();

        JPanel fichas = new JPanel(new java.awt.GridLayout(1, 2, 12, 0));
        fichas.setOpaque(false);
        fichas.add(Estilo.ficha("Precio", selPrecio));
        fichas.add(Estilo.ficha("Stock", selStock));

        Superficie seleccionado = new Superficie(new java.awt.GridBagLayout());
        seleccionado.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20));
        java.awt.GridBagConstraints s = new java.awt.GridBagConstraints();
        s.gridx = 0;
        s.weightx = 1;
        s.fill = java.awt.GridBagConstraints.HORIZONTAL;
        s.anchor = java.awt.GridBagConstraints.NORTHWEST;
        s.gridy = 0;
        seleccionado.add(etiqueta, s);
        s.gridy = 1;
        s.insets = new java.awt.Insets(8, 0, 18, 0);
        seleccionado.add(selNombre, s);
        s.gridy = 2;
        s.insets = new java.awt.Insets(0, 0, 0, 0);
        seleccionado.add(fichas, s);

        // ---- Acciones (los tres botones originales)
        Estilo.botonAccion(granelBTn, Iconos.mas(java.awt.Color.WHITE, 22), "Agregar P. Granel", Estilo.PRIMARIO);
        Estilo.botonAccion(modificarBtn, Iconos.lapiz(Tema.TEXTO, 22), "Modificar producto", Estilo.SECUNDARIO);
        Estilo.botonAccion(BotonEliminar, Iconos.papelera(Estilo.ROJO, 22), "Eliminar producto", Estilo.PELIGRO);

        Superficie acciones = new Superficie(new java.awt.GridLayout(3, 1, 0, 12));
        acciones.setBorder(javax.swing.BorderFactory.createEmptyBorder(16, 16, 16, 16));
        acciones.add(granelBTn);
        acciones.add(modificarBtn);
        acciones.add(BotonEliminar);

        JLabel nota = new JLabel("<html><body style='width:290px'>Eliminar pide confirmación antes de borrar el producto.</body></html>");
        nota.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));
        nota.setForeground(Tema.TEXTO_SUAVE);

        JPanel derecha = new JPanel(new java.awt.GridBagLayout());
        derecha.setOpaque(false);
        java.awt.GridBagConstraints d = new java.awt.GridBagConstraints();
        d.gridx = 0;
        d.weightx = 1;
        d.fill = java.awt.GridBagConstraints.HORIZONTAL;
        d.anchor = java.awt.GridBagConstraints.NORTH;
        d.gridy = 0;
        derecha.add(seleccionado, d);
        d.gridy = 1;
        d.insets = new java.awt.Insets(18, 0, 0, 0);
        derecha.add(acciones, d);
        d.gridy = 2;
        d.insets = new java.awt.Insets(14, 6, 0, 6);
        derecha.add(nota, d);
        d.gridy = 3;
        d.weighty = 1;
        d.insets = new java.awt.Insets(0, 0, 0, 0);
        derecha.add(javax.swing.Box.createGlue(), d);
        d.gridy = 4;
        d.weighty = 0;
        derecha.add(javax.swing.Box.createHorizontalStrut(350), d); // ancho fijo de la columna

        // ---- Cuerpo
        JPanel cuerpo = new JPanel(new java.awt.GridBagLayout());
        cuerpo.setBackground(Tema.FONDO);
        cuerpo.setBorder(javax.swing.BorderFactory.createEmptyBorder(22, 22, 22, 22));
        java.awt.GridBagConstraints g = new java.awt.GridBagConstraints();
        g.gridy = 0;
        g.weighty = 1;
        g.fill = java.awt.GridBagConstraints.BOTH;
        g.gridx = 0;
        g.weightx = 1;
        cuerpo.add(catalogo, g);
        g.gridx = 1;
        g.weightx = 0;
        g.insets = new java.awt.Insets(0, 22, 0, 0);
        cuerpo.add(derecha, g);

        JPanel raiz = new JPanel(new java.awt.BorderLayout());
        raiz.add(cabecera, java.awt.BorderLayout.NORTH);
        raiz.add(cuerpo, java.awt.BorderLayout.CENTER);
        setContentPane(raiz);

        // ---- Se actualizan solos
        javax.swing.event.TableModelListener alCambiar = e -> SwingUtilities.invokeLater(this::actualizarContadores);
        modeloProductos.addTableModelListener(alCambiar);
        jTable1.getRowSorter().addRowSorterListener(e -> SwingUtilities.invokeLater(this::actualizarContadores));
        jTable1.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                actualizarSeleccion();
            }
        });
        actualizarContadores();
        actualizarSeleccion();

        pack();
        setMinimumSize(getPreferredSize());
        setLocationRelativeTo(null);
        SwingUtilities.invokeLater(() -> txtBuscar.requestFocusInWindow());
    }

    private void actualizarContadores() {
        int total = modeloProductos.getRowCount();
        contadorProductos.setText(Estilo.miles(total));
        lblMostrando.setText("Mostrando " + Estilo.miles(jTable1.getRowCount())
                + " de " + Estilo.miles(total) + " productos");
    }

    private void actualizarSeleccion() {
        int fila = jTable1.getSelectedRow();
        if (fila == -1 || fila >= jTable1.getRowCount()) {
            selNombre.setText("Elige un producto de la lista para ver sus datos.");
            selNombre.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 16));
            selNombre.setForeground(Tema.TEXTO_SUAVE);
            selPrecio.setText("—");
            selStock.setText("—");
            return;
        }
        int m = jTable1.convertRowIndexToModel(fila);
        String nombre = String.valueOf(modeloProductos.getValueAt(m, 0));
        selNombre.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 21));
        selNombre.setForeground(Tema.TEXTO);
        selNombre.setText(nombre);
        selPrecio.setText(Estilo.pesos(modeloProductos.getValueAt(m, 1)));
        selStock.setText(String.valueOf(modeloProductos.getValueAt(m, 2)));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        BotonEliminar = new javax.swing.JButton();
        BotonAtras = new javax.swing.JButton();
        txtBuscar = new javax.swing.JTextField();
        granelBTn = new javax.swing.JButton();
        modificarBtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTable1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "ID", "Nombre", "Precio", "Cantidad"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        BotonEliminar.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        BotonEliminar.setText("Eliminar Producto");
        BotonEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BotonEliminarActionPerformed(evt);
            }
        });

        BotonAtras.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        BotonAtras.setText("Atras");
        BotonAtras.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BotonAtrasActionPerformed(evt);
            }
        });

        txtBuscar.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        txtBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBuscarActionPerformed(evt);
            }
        });

        granelBTn.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        granelBTn.setText("Agregar P.Granel");
        granelBTn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                granelBTnActionPerformed(evt);
            }
        });

        modificarBtn.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        modificarBtn.setText("Modificar Producto");
        modificarBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                modificarBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(BotonAtras)
                .addGap(125, 125, 125)
                .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 273, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(438, 438, 438))
            .addGroup(layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 583, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(38, 38, 38)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(BotonEliminar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(granelBTn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(modificarBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(BotonAtras)
                .addGap(92, 92, 92)
                .addComponent(granelBTn)
                .addGap(33, 33, 33)
                .addComponent(BotonEliminar)
                .addGap(33, 33, 33)
                .addComponent(modificarBtn)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(txtBuscar, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 494, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BotonAtrasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BotonAtrasActionPerformed
        // TODO add your handling code here:

        this.dispose();
        Mainn main=new Mainn();
        main.setVisible(true);
    }//GEN-LAST:event_BotonAtrasActionPerformed

    private void BotonEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BotonEliminarActionPerformed
        
    int fila = jTable1.getSelectedRow();

    // Se valida antes de convertir: con -1 la conversión lanzaba una excepción y el aviso nunca salía.
    if (fila == -1) {
        Aviso.mostrar(this, "Seleccione un producto");
        return;
    }
    fila = jTable1.convertRowIndexToModel(fila);

    // El nombre se lee del modelo con el índice del modelo. Antes se leía de la tabla con ese
    // índice, y con el buscador activo podía tomar el producto de otra fila.
    String nombre = modeloProductos.getValueAt(fila, 0).toString();

    int confirm = Aviso.confirmar(
        this,
        "¿Eliminar este producto?",
        "Confirmar",
        JOptionPane.YES_NO_OPTION
    );

    if (confirm == JOptionPane.YES_OPTION) {
        con.eliminarProducto(nombre);
        Connection conexion = con.establecerConexion("root", "ADMIN12341");
        cn.listarEntradaProducto(jTable1, conexion);
    }

    }//GEN-LAST:event_BotonEliminarActionPerformed

    private void txtBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBuscarActionPerformed
       try {
        String texto = txtBuscar.getText().trim();

// Si contiene letras → es búsqueda, no scanner
if (!texto.matches("\\d+")) {
    return; // Ya se filtró arriba
}

String codigo = texto;
        codigo = codigo.trim();
if (!codigo.matches("\\d{8,14}")) {
    Aviso.mostrar(this,
        "⚠ Código inválido\n" +
        "Vuelva a escanear el producto.\n\n" +
        "El código debe tener solo números (8 a 14 dígitos)."
    );
    txtBuscar.setText("");
    return;
}

        Connection con = cn.establecerConexion("root", "ADMIN12341");
        ResultSet rs = cn.buscarProductoPorCodigo(codigo);

        if (!rs.next()) {
            JTextField txtNombre = new JTextField();
    JTextField txtPrecio = new JTextField();
    JTextField txtStock = new JTextField();

    JPanel panelNuevo = new JPanel();
    panelNuevo.setLayout(new java.awt.GridLayout(0, 1));

    panelNuevo.add(new JLabel("Código: " + codigo));
    panelNuevo.add(new JLabel("Nombre del producto:"));
    panelNuevo.add(txtNombre);
    panelNuevo.add(new JLabel("Precio unitario:"));
    panelNuevo.add(txtPrecio);
    panelNuevo.add(new JLabel("Stock inicial:"));
    panelNuevo.add(txtStock);

    int opcion = Aviso.confirmar(
            this,
            panelNuevo,
            "Nuevo producto",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE
    );

    if (opcion != JOptionPane.OK_OPTION) {
        txtBuscar.setText("");
        return;
    }

    String nombre = txtNombre.getText().trim();
    int precio = Integer.parseInt(txtPrecio.getText().trim());
    int stock = Integer.parseInt(txtStock.getText().trim());

    cn.insertarProductoNuevo(codigo, nombre, precio, stock);

    Connection conexionTabla = cn.establecerConexion("root", "ADMIN12341");
    cn.listarEntradaProducto(jTable1, conexionTabla);

    Aviso.mostrar(this,
            "Producto creado correctamente\n" +
            nombre +
            "\nStock: " + stock +
            "\nPrecio: $" + precio
    );

    txtBuscar.setText("");
    return;
        }

        String nombre = rs.getString("nombreProducto");
        int stockActual = rs.getInt("stockProducto");
        int precioActual = rs.getInt("precioUnitarioProducto");

        // ===== PANEL PERSONALIZADO =====
        JTextField txtCantidad = new JTextField("0");
        JTextField txtPrecio = new JTextField(String.valueOf(precioActual));

        JPanel panel = new JPanel();
        panel.setLayout(new java.awt.GridLayout(0, 1));

        panel.add(new JLabel("Producto: " + nombre));
        panel.add(new JLabel("Stock actual: " + stockActual));
        panel.add(new JLabel("Cantidad a ingresar:"));
        panel.add(txtCantidad);
        panel.add(new JLabel("Precio unitario:"));
        panel.add(txtPrecio);

        int opcion = Aviso.confirmar(
                this,
                panel,
                "Ingreso de producto",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (opcion != JOptionPane.OK_OPTION) {
            txtBuscar.setText("");
            return;
        }

        int cantidadAgregar = Integer.parseInt(txtCantidad.getText().trim());
        int nuevoPrecio = Integer.parseInt(txtPrecio.getText().trim());

        int nuevoStock = stockActual + cantidadAgregar;

        cn.actualizarProducto(codigo, nuevoStock, nuevoPrecio);
        Connection conexionTabla = cn.establecerConexion("root", "ADMIN12341");
cn.listarEntradaProducto(jTable1, conexionTabla);

Aviso.mostrar(this,
        "Producto actualizado:\n" +
        nombre +
        "\nNuevo stock: " + nuevoStock +
        "\nPrecio: $" + nuevoPrecio
);
        txtBuscar.setText("");

    } catch (Exception e) {
        Aviso.mostrar(this, "Error: " + e.getMessage());
    }
    }//GEN-LAST:event_txtBuscarActionPerformed

    private void granelBTnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_granelBTnActionPerformed
        // TODO add your handling code here:
        agregarProductoGranel();
    }//GEN-LAST:event_granelBTnActionPerformed

    private void modificarBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_modificarBtnActionPerformed
        // TODO add your handling code here:
        try {
    int fila = jTable1.getSelectedRow();

    if (fila == -1) {
        Aviso.mostrar(this, "Seleccione un producto");
        return;
    }

    // Convertir fila visual a real (por el sorter)
    fila = jTable1.convertRowIndexToModel(fila);

    String nombre = modeloProductos.getValueAt(fila, 0).toString();
int precioActual = Integer.parseInt(modeloProductos.getValueAt(fila, 1).toString());
Object stockObj = modeloProductos.getValueAt(fila, 2); // <-- ESTA LÍNEA FALTABA


    String codigo = cn.buscarCodigoPorNombre(nombre);
boolean esGranel = !codigo.matches("\\d+"); // si NO es numérico → granel

    JTextField txtNombre = new JTextField(nombre); 
    JTextField txtPrecio = new JTextField(String.valueOf(precioActual));
    JTextField txtStock = new JTextField();

    JPanel panel = new JPanel();
    panel.setLayout(new java.awt.GridLayout(0,1));
    panel.add(new JLabel("Nombre:"));
    panel.add(txtNombre);
    panel.add(new JLabel("Producto: " + nombre));
    panel.add(new JLabel("Precio:"));
    panel.add(txtPrecio);

    if (!esGranel) {
        txtStock.setText(stockObj.toString());
        panel.add(new JLabel("Stock:"));
        panel.add(txtStock);
    } else {
        panel.add(new JLabel("⚖ Producto a granel"));
    }

    int opcion = Aviso.confirmar(
            this,
            panel,
            "Modificar producto",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE
    );

    if (opcion != JOptionPane.OK_OPTION) return;
    String nuevoNombre = txtNombre.getText().trim();

if (nuevoNombre.isEmpty()) {
    Aviso.mostrar(this, "El nombre no puede estar vacío");
    return;
}

    int nuevoPrecio = Integer.parseInt(txtPrecio.getText().trim());


    if (nuevoPrecio <= 0) {
        Aviso.mostrar(this, "Precio inválido");
        return;
    }

   if (esGranel) {

    cn.actualizarGranel(nombre, nuevoNombre, nuevoPrecio);

} else {

    int nuevoStock = Integer.parseInt(txtStock.getText().trim());

    if (nuevoStock < 0) {
        Aviso.mostrar(this, "Stock inválido");
        return;
    }

    cn.actualizarNormal(nombre, nuevoNombre, nuevoStock, nuevoPrecio);
}
 
    Connection con = cn.establecerConexion("root", "ADMIN12341");
    cn.listarEntradaProducto(jTable1, con);

    Aviso.mostrar(this, "Producto actualizado ✔");

} catch (Exception e) {
    Aviso.mostrar(this, "Error: " + e.getMessage());
}

    }//GEN-LAST:event_modificarBtnActionPerformed

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
            java.util.logging.Logger.getLogger(EntradaProductos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(EntradaProductos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(EntradaProductos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(EntradaProductos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new EntradaProductos().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BotonAtras;
    private javax.swing.JButton BotonEliminar;
    private javax.swing.JButton granelBTn;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JButton modificarBtn;
    private javax.swing.JTextField txtBuscar;
    // End of variables declaration//GEN-END:variables
}
