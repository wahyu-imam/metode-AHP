/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package master;

import database.database;
import java.awt.HeadlessException;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JOptionPane;
/**
 *
 * @author imam
 */
public class alternatif {
    database db;
    Connection con;
    Statement stm;
    PreparedStatement pst;
    ResultSet rs;
    
    public alternatif(){
        db = new database();
    }
    
    public String[][] get_all(){
        ArrayList<m_alternatif> tampung = new ArrayList<>();
        String sql = "select * from alternatif";
        try {
            con = db.getKoneksi();
            stm = con.createStatement();
            rs = stm.executeQuery(sql);
            while (rs.next()) {                
                tampung.add(new m_alternatif(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), 
                        rs.getString(5), rs.getString(6), rs.getString(7)));
            }
            con.close(); stm.close(); rs.close();
        } catch (SQLException e) {
            System.out.println("error : "+e);
        }
        String[][] data = null;
        if(!tampung.isEmpty()){
            data = new String[tampung.size()][7];
            int baris = 0;
            for (m_alternatif a : tampung) {
                data[baris][0] = a.getKode();
                data[baris][1] = a.getNama();
                data[baris][2] = a.getN1();
                data[baris][3] = a.getN2();
                data[baris][4] = a.getN3();
                data[baris][5] = a.getN4();
                data[baris][6] = a.getN5();
                baris++;
            }
        }
        return data;
    }
    
    public String[][] getById(String kode){
       ArrayList<m_alternatif> tampung = new ArrayList<>();
       String sql = "SELECT * FROM `alternatif` WHERE kode_alternatif = '"+kode+"'";
        try {
            con = db.getKoneksi();
            stm = con.createStatement();
            rs = stm.executeQuery(sql);
            if (rs.next()) {                
                tampung.add(new m_alternatif(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), 
                        rs.getString(5), rs.getString(6), rs.getString(7)));
            }
            con.close(); stm.close(); rs.close();
        } catch (SQLException e) {
            System.out.println("error : "+e);
        }
        String[][] data = null;
        if(!tampung.isEmpty()){
            data = new String[tampung.size()][7];
            int baris = 0;
            for (m_alternatif a : tampung) {
                data[baris][0] = a.getKode();
                data[baris][1] = a.getNama();
                data[baris][2] = a.getN1();
                data[baris][3] = a.getN2();
                data[baris][4] = a.getN3();
                data[baris][5] = a.getN4();
                data[baris][6] = a.getN5();
                baris++;
            }
        }
        return data;
    }
    
    private String autoKode(){
        String sql = "SELECT MAX(kode_alternatif) as kode FROM `alternatif`";
        String data = null;
        try {
            con = db.getKoneksi();
            stm = con.createStatement();
            rs = stm.executeQuery(sql);
            if(rs.next()){
                if(rs.getString(1) == null){
                    data = "A01";
                }else{
                    String kode = rs.getString(1);
                    String[] arrayKode = kode.split("A");
                    int nomor = Integer.parseInt(arrayKode[1]) + 1;
                    if(nomor < 10){
                        kode = String.valueOf(nomor);
                        data = "A0"+kode;
                    }else if(nomor > 9 && nomor < 100){
                        kode = String.valueOf(nomor);
                        data = "A"+kode;
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
    
    public void save(String nama, String n1, String n2, String n3, String n4, String n5){
        String sql = "INSERT INTO `alternatif`(`kode_alternatif`, `nama_alternatif`, `n1`, `n2`, `n3`, `n4`, `n5`) VALUES "
                + "(?,?,?,?,?,?,?)";
        try {
            con = db.getKoneksi();
            pst = con.prepareStatement(sql);
            pst.setString(1, autoKode());
            pst.setString(2, nama);
            pst.setString(3, n1);
            pst.setString(4, n2);
            pst.setString(5, n3);
            pst.setString(6, n4);
            pst.setString(7, n5);
            pst.execute();
            con.close(); pst.close();
            JOptionPane.showMessageDialog(null, "Berhasil simpan data");
        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(null, "error : "+e);
        }
    }
    
    public void edit(String nama, String n1, String n2, String n3, String n4, String n5, String kode){
        String sql = "UPDATE `alternatif` SET `nama_alternatif`=?,`n1`=?,`n2`=?,`n3`=?,`n4`=?,`n5`=? WHERE kode_alternatif =?";
        try {
            con = db.getKoneksi();
            pst = con.prepareStatement(sql);
            pst.setString(1, nama);
            pst.setString(2, n1);
            pst.setString(3, n2);
            pst.setString(4, n3);
            pst.setString(5, n4);
            pst.setString(6, n5);
            pst.setString(7, kode);
            pst.execute();
            con.close(); pst.close();
            JOptionPane.showMessageDialog(null, "Berhasil simpan data");
        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(null, "error : "+e);
        }
    }
    
    public void hapus(String kode){
        String sql = "DELETE FROM `alternatif` WHERE kode_alternatif =?";
        try {
            con = db.getKoneksi();
            pst = con.prepareStatement(sql);
            pst.setString(1, kode);
            pst.execute();
            con.close(); pst.close();
            JOptionPane.showMessageDialog(null, "Berhasil hapus data");
        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(null, "error : "+e);
        }
    }
    
    private void show2D(String[][] data){
        for (String[] d1 : data) {
            for (String d2 : d1) {
                System.out.print(d2 + "; ");
            }
            System.out.println("");
        }
    }
    
    public static void main(String[] args) {
        alternatif a = new alternatif();
//        String[][] data_alternatif_tunggal = a.getById("A01");
//        a.save("test", "0", "0", "0", "0", "0");
//        a.edit("test 2", "1", "1", "1", "1", "1", "A04");
//        a.hapus("A04");
        String[][] data_alternatif = a.get_all();
        a.show2D(data_alternatif);
    }
}
