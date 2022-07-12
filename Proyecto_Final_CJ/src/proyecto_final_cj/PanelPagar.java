/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package proyecto_final_cj;

import java.awt.HeadlessException;
import java.sql.*;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;

/**
 *
 * @author hugob
 */
public class PanelPagar extends javax.swing.JPanel {
    
//Desde aquí podemos cambiar el precio por minuto
    private final double TARIFA_MINUTOS = 0.15; 
//Usamos un getter para poder acceder al precio/minuto desde otras clases
    public double getTARIFA_MINUTOS() {
        return TARIFA_MINUTOS;
    }
    
    /**
     * Creates new form PanelPagar
     */
    public PanelPagar() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        labelRetirar = new javax.swing.JLabel();
        btnPagar = new javax.swing.JButton();
        txtMatriculaRetirar = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(204, 204, 204));
        setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        setPreferredSize(new java.awt.Dimension(630, 420));

        labelRetirar.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        labelRetirar.setText("RETIRAR VEHÍCULO");

        btnPagar.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnPagar.setText("PAGAR");
        btnPagar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPagarActionPerformed(evt);
            }
        });

        txtMatriculaRetirar.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txtMatriculaRetirar.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel1.setText("Sistema de gestión de aparcamiento Aparka-t, SLU  ");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(184, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(txtMatriculaRetirar, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(201, 201, 201))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(labelRetirar)
                        .addGap(210, 210, 210))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(btnPagar)
                        .addGap(263, 263, 263))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(156, 156, 156))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 86, Short.MAX_VALUE)
                .addComponent(labelRetirar)
                .addGap(47, 47, 47)
                .addComponent(txtMatriculaRetirar, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40)
                .addComponent(btnPagar)
                .addGap(127, 127, 127))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnPagarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPagarActionPerformed
        
        try{
           //Creamos la conexión con la base de datos
           Class.forName("com.mysql.jdbc.Driver"); 
           Connection cnx = DriverManager.getConnection("jdbc:mysql://localhost/bdparking","root","");
           Statement sentencia=cnx.createStatement();
           
           //Damos formato a la fecha e instanciamos los objetos necesarios para obtenerla
           DateFormat dateFormat = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
           Calendar cal = Calendar.getInstance();
           Date date = cal.getTime();
           String fechaHoraActual = dateFormat.format(date);
           
           String matricula = this.txtMatriculaRetirar.getText().toUpperCase().trim();
           String patronAntiguo = "^[A-Z]{1,2}[0-9]{4}[BCDFGHJKLMNPRSTVWXYZ]{2}";//Este es el formato antiguo de las matrículas españolas
           String patronNuevo = "^[0-9]{4}[BCDFGHJKLMNPRSTVWXYZ]{3}";//Este es el formato actual de la matrícula española 
           
           if(matricula.matches(patronAntiguo) || matricula.matches(patronNuevo)){
               
               String sql = "SELECT horaEntrada, estaDentro, id FROM vehiculos WHERE matricula='"+matricula+"' AND estaDentro=1";
               ResultSet rs = sentencia.executeQuery(sql);
               
               if (rs.first()){
                rs.first();
                    if(rs.getBoolean("estaDentro")!=false){
                        String horaEntrada = rs.getString("horaEntrada");
                        Date horaentrada = dateFormat.parse(horaEntrada);
                        int minutosEstancia = (int)(date.getTime()-horaentrada.getTime())/60000;
                        int id = rs.getInt("id");
               
                        double precioTotal = minutosEstancia * TARIFA_MINUTOS;
                        DecimalFormat formatoEuros = new DecimalFormat("0.00");
                        JOptionPane.showMessageDialog(this, "Total a pagar "+formatoEuros.format(precioTotal)+" €", "Confirmación del pago", 1);
                        sentencia.executeUpdate("UPDATE vehiculos SET horaSalida='"+fechaHoraActual+"', totalPagado="+precioTotal+", estaDentro= false WHERE id="+id);
                        JOptionPane.showMessageDialog(this, "Que tenga un buen viaje, esperamos verle pronto de nuevo", "Buen viaje", 1);
                        this.txtMatriculaRetirar.setText("");
                       
                    }
                    else JOptionPane.showMessageDialog(this,"El vehículo no se encuentra en nuestras instalaciones o ya ha pagado", "El vehículo no se encuentra en las instalaciones", JOptionPane.INFORMATION_MESSAGE);
               }
               else {
                   JOptionPane.showMessageDialog(this, "El vehículo no se encuentra en la base de datos\nPor favor, compruebe la matrícula", "Vehículo no encontrado", JOptionPane.INFORMATION_MESSAGE);
               }
               cnx.close();
           }
           else {
               JOptionPane.showMessageDialog(this,"Debe introducir una matrícula válida sin espacios ni guiones\n Ejemplo: 1234BCD o C2779BH","Alerta - Formato no valido", JOptionPane.ERROR_MESSAGE);
           }
        }
        catch(HeadlessException | ClassNotFoundException | SQLException | ParseException e){
            System.out.println("Ha ocurrido un error "+e.getLocalizedMessage());
        }
    }//GEN-LAST:event_btnPagarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnPagar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel labelRetirar;
    private javax.swing.JTextField txtMatriculaRetirar;
    // End of variables declaration//GEN-END:variables
}
