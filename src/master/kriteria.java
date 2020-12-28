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
public class kriteria {
    database db;
    Connection con;
    Statement stm;
    PreparedStatement pst;
    ResultSet rs;
    
    public kriteria(){
        db = new database();
    }
    
    public String[][] get_all(){
        ArrayList<m_kriteria> tampung = new ArrayList<>();
        String sql = "SELECT * FROM `kriteria`";
        try {
            con = db.getKoneksi();
            stm = con.createStatement();
            rs = stm.executeQuery(sql);
            while (rs.next()) {                
                tampung.add(new m_kriteria(rs.getString(1), rs.getString(2)));
            }
            con.close(); stm.close(); rs.close();
        } catch (SQLException e) {
            System.out.println("error : "+e);
        }
        String[][] data = null;
        if(!tampung.isEmpty()){
            data = new String[tampung.size()][2];
            int baris = 0;
            for (m_kriteria k : tampung) {
                data[baris][0] = k.getKode();
                data[baris][1] = k.getNama();
                baris++;
            }
        }
        return data;
    }
    
    public String[][] getById(String kode){
        ArrayList<m_kriteria> tampung = new ArrayList<>();
        String sql = "SELECT * FROM `kriteria` WHERE kode_kriteria = '"+kode+"'";
        try {
            con = db.getKoneksi();
            stm = con.createStatement();
            rs = stm.executeQuery(sql);
            if(rs.next()){
                tampung.add(new m_kriteria(rs.getString(1), rs.getString(2)));
            }
            con.close(); stm.close(); rs.close();
        } catch (SQLException e) {
            System.out.println("error : "+e);
        }
        String[][] data = null;
        if(!tampung.isEmpty()){
            data = new String[tampung.size()][2];
            int baris = 0;
            for (m_kriteria k : tampung) {
                data[baris][0] = k.getKode();
                data[baris][1] = k.getNama();
                baris++;
            }
        }
        return data;
    }
    
    private String autoKode(){
        String sql = "SELECT MAX(kode_kriteria) as kode FROM `kriteria`";
        String data = null;
        try {
            con = db.getKoneksi();
            stm = con.createStatement();
            rs = stm.executeQuery(sql);
            if(rs.next()){
                if(rs.getString(1) == null){
                    data = "C01";
                }else{
                    String kode = rs.getString(1);
                    String[] arrayKode = kode.split("C");
                    int nomor = Integer.parseInt(arrayKode[1]) + 1;
                    if(nomor < 10){
                        kode = String.valueOf(nomor);
                        data = "C0"+kode;
                    }else if(nomor > 9 && nomor < 100){
                        kode = String.valueOf(nomor);
                        data = "C"+kode;
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
    
    public void save(String nama){
        String sql = "INSERT INTO `kriteria`(`kode_kriteria`, `nama_kriteria`) VALUES (?,?)";
        try {
            con = db.getKoneksi();
            pst = con.prepareStatement(sql);
            pst.setString(1, autoKode());
            pst.setString(2, nama);
            pst.execute();
            con.close(); pst.close();
            JOptionPane.showMessageDialog(null, "Berhasil simpan data");
        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(null, "error : "+e);
        }
    }
    
    public void edit(String nama, String kode){
        String sql = "UPDATE `kriteria` SET `nama_kriteria`=? WHERE kode_kriteria =?";
        try {
            con = db.getKoneksi();
            pst = con.prepareStatement(sql);
            pst.setString(1, nama);
            pst.setString(2, kode);
            pst.execute();
            con.close(); pst.close();
            JOptionPane.showMessageDialog(null, "Berhasil simpan data");
        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(null, "error : "+e);
        }
    }
    
    public void hapus(String kode){
        String sql = "DELETE FROM `kriteria` WHERE kode_kriteria=?";
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
                System.out.print(d2+"; ");
            }
            System.out.println("");
        }
    }
    
    public static void main(String[] args) {
        kriteria k = new kriteria();
        
//        k.save("test");
//        k.edit("test 2", "C06");
//        k.hapus("C06");
//        String[][] data_kriteria_tunggal = k.getById("C01");
        String[][] data_kriteria = k.get_all();
        k.show2D(data_kriteria);
    }
}
