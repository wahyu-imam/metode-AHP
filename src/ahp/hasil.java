/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ahp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import database.database;
import java.awt.HeadlessException;
import java.sql.SQLException;
import javax.swing.JOptionPane;
/**
 *
 * @author imam
 */
public class hasil {
    database db;
    Connection con;
    Statement stm;
    PreparedStatement pst;
    ResultSet rs;
    
    public hasil(){
        db = new database();
    }
    
    public String autoKode(){
        String sql = "SELECT MAX(kode_hasil) FROM `hasil`";
        String data = null;
        try {
            con = db.getKoneksi();
            stm = con.createStatement();
            rs = stm.executeQuery(sql);
            if(rs.next()){
                if(rs.getString(1) == null){
                    data = "H01";
                }else{
                    String kode = rs.getString(1);
                    String[] arrayKode = kode.split("H");
                    int nomor = Integer.parseInt(arrayKode[1]) + 1;
                    if(nomor < 10){
                        kode = String.valueOf(nomor);
                        data = "H0"+kode;
                    }else if(nomor > 9 && nomor < 100){
                        kode = String.valueOf(nomor);
                        data = "H"+kode;
                    }else{
                        JOptionPane.showMessageDialog(null, "Data melebihi kapasitas kode otomatis");
                    }
                }
                con.close(); stm.close(); rs.close();
            }
        } catch (HeadlessException | NumberFormatException | SQLException e) {
            System.out.println("error : "+e);
        }
        return data;
    }
    
    private void tambah(String[][] hasil){
        for (String[] h : hasil) {
            String sql = "INSERT INTO `hasil`(`kode_hasil`, `hasil`, `kode_alternatif`) VALUES (?,?,?)";
            try {
                con = db.getKoneksi();
                pst = con.prepareStatement(sql);
                pst.setString(1, autoKode());
                pst.setString(2, h[h.length - 2]);
                pst.setString(3, h[h.length - 1]);
                pst.execute(); 
                con.close(); pst.close();
            }catch (SQLException e) {
                System.out.println("error : "+e);
            }
        }
    }
    
    public void save(String[][] hasil){
        if(autoKode() != null){
            hapus();
            tambah(hasil);
        }else{
            tambah(hasil);
        }
    }
    
    public void hapus(){
        String sql = "DELETE FROM `hasil`";
        try {
            con = db.getKoneksi();
            pst = con.prepareStatement(sql);
            pst.execute();
            con.close(); pst.close();
        } catch (SQLException e) {
            System.out.println("error : "+e);
        }
    }
    
//    public static void main(String[] args) {
//        hasil h = new hasil();
//        System.out.println(h.autoKode());
//    }
}
