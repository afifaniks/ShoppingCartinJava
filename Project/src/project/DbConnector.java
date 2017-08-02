/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project;

import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.MessagingException;
import javax.swing.JFrame;

//import javax.swing.JPanel;

/**
 *
 * @author Void
 */
public class DbConnector {

    public static int insertToUser(String username, String name, String email, String password, String card) {
        try {
            Connection con = DriverManager.getConnection("jdbc:sqlite:DBs/bbs.db");
//            String tester = "SELECT * FROM userinfo WHERE username = '"+username+"'";
//            Statement stmt = con.createStatement();
//            
//            ResultSet rs = stmt.executeQuery(tester);
//            
//            if(!rs.next()){
//                JOptionPane.showMessageDialog(null,"Username already exist!");
//            }
            String sql = "INSERT INTO userinfo"
                                  +"(username, name, email, password, card)"
                                  +"VALUES(?,?,?,?,?)";
            
            PreparedStatement ps = con.prepareStatement(sql);
            
            ps.setString(1, username);
            ps.setString(2, name);
            ps.setString(3, email);
            ps.setString(4, password);
            ps.setString(5, card);
            ps.executeUpdate();
            
            //JOptionPane.showMessageDialog(null, "Signing up successful!");
            JFrame notification = new NotificationForm();
            
            
        } catch (SQLException e) {
            if(e.getErrorCode()==19) //Duplicate username
                return 19;
            
            JFrame errorOc = new ErrorOccured();
        }
        
        return 0;

    }
    
    public static boolean signer(String username, String pass){
        try {
            Connection con = DriverManager.getConnection("jdbc:sqlite:DBs/bbs.db");
            String check = "SELECT username, password FROM userinfo WHERE username=? AND password=?";
            PreparedStatement ps = con.prepareStatement(check);
            
            ps.setString(1, username);
            ps.setString(2, pass);
            ResultSet rs = ps.executeQuery();
            
            return rs.next();
            
        } catch (SQLException ex) {
            Logger.getLogger(DbConnector.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
    }
    
    public static void passRecovery(String username) throws MessagingException{
        String[] query= new String[2];
        
        try {
            Connection con = DriverManager.getConnection("jdbc:sqlite:DBs/bbs.db");
            String check = "SELECT username, email, password FROM userinfo WHERE username=?";
            PreparedStatement ps = con.prepareStatement(check);
            
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            
           if(rs.next()){
                query[0]=rs.getString("email");
                query[1]=rs.getString("password");
                
                ForgotPassword.Send("blackbengalshopping","projectinjava",query[0],"Password Recovery","Your password: "+query[1]);
                
                NotificationForm nf = new NotificationForm();
                nf.jLabel7.setText("An email has been sent to "+query[0]);
               
           }
           else{
               ErrorOccured foo = new ErrorOccured();
               foo.errorMessage.setText("Username not found");
           }
            
        } catch (SQLException ex) {
            Logger.getLogger(DbConnector.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
   
}
