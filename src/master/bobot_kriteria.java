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
public class bobot_kriteria {
    database db;
    Connection con;
    Statement stm;
    PreparedStatement pst;
    ResultSet rs;
    kriteria kriteria;
    
    public bobot_kriteria(){
        kriteria = new kriteria();
        db = new database();
    }
    
    public String[][] get_all(){
        ArrayList<m_bbt_kriteria> tampung = new ArrayList<>();
        String sql = "SELECT * FROM `bbt_kriteria`";
        try {
            con = db.getKoneksi();
            stm = con.createStatement();
            rs = stm.executeQuery(sql);
            while(rs.next()){
                tampung.add(new m_bbt_kriteria(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4)));
            }
            con.close(); stm.close(); rs.close();
        } catch (SQLException e) {
            System.out.println("error : "+e);
        }
        String[][] data = null;
        if(!tampung.isEmpty()){
            data = new String[tampung.size()][4];
            int baris = 0;
            for (m_bbt_kriteria bbt : tampung) {
                data[baris][0] = bbt.getKode();
                data[baris][1] = bbt.getDari();
                data[baris][2] = bbt.getKe();
                data[baris][3] = bbt.getBobot();
                baris++;
            }
        }
        return data;
    }
    
    public String[][] getById(String kode){
        ArrayList<m_bbt_kriteria> tampung = new ArrayList<>();
        String sql = "SELECT * FROM `bbt_kriteria` WHERE kode_bbt = '"+kode+"'";
        try {
            con = db.getKoneksi();
            stm = con.createStatement();
            rs = stm.executeQuery(sql);
            if(rs.next()){
                tampung.add(new m_bbt_kriteria(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4)));
            }
            con.close(); stm.close(); rs.close();
        } catch (SQLException e) {
            System.out.println("error : "+e);
        }
        String[][] data = null;
        if(!tampung.isEmpty()){
            data = new String[tampung.size()][4];
            int baris = 0;
            for (m_bbt_kriteria k : tampung) {
                data[baris][0] = k.getKode();
                data[baris][1] = k.getDari();
                data[baris][2] = k.getKe();
                data[baris][3] = k.getBobot();
                baris++;
            }
       }
       return data;
    }
    
    private String autoKode(){
        String sql = "SELECT MAX(kode_bbt) AS kode FROM `bbt_kriteria`";
        String data = null;
        try {
            con = db.getKoneksi();
            stm = con.createStatement();
            rs = stm.executeQuery(sql);
            if(rs.next()){
                if(rs.getString(1) == null){
                    data = "BC01";
                }else{
                    String kode = rs.getString(1);
                    String[] arrayKode = kode.split("BC");
                    int nomor = Integer.parseInt(arrayKode[1]) + 1;
                    if(nomor < 10){
                        kode = String.valueOf(nomor);
                        data = "BC0"+kode;
                    }else if(nomor > 9 && nomor < 100){
                        kode = String.valueOf(nomor);
                        data = "BC"+kode;
                    }else{
                        JOptionPane.showMessageDialog(null, "Data melebihi kapasitas kode otomatis");
                    }
                }
                con.close(); stm.close(); rs.close();
            }
        } catch (Exception e) {
            System.out.println("error : "+e);
        }
        return data;
    }
    
    public void save(String dari, String ke, String bobot){
        String sql = "INSERT INTO `bbt_kriteria`(`kode_bbt`, `dari`, `ke`, `bobot`) VALUES (?,?,?,?)";
        try {
            con = db.getKoneksi();
            pst = con.prepareStatement(sql);
            pst.setString(1, autoKode());
            pst.setString(2, dari);
            pst.setString(3, ke);
            pst.setString(4, bobot);
            pst.execute();
            con.close(); pst.close();
            JOptionPane.showMessageDialog(null, "Berhasil simpan data");
        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(null, "error : "+e);
        }
    }
    
    public void edit(String dari, String ke, String bobot, String kode){
        String sql = "UPDATE `bbt_kriteria` SET `dari`=?,`ke`=?,`bobot`=? WHERE kode_bbt=?";
        try {
            con = db.getKoneksi();
            pst = con.prepareStatement(sql);
            pst.setString(1, dari);
            pst.setString(2, ke);
            pst.setString(3, bobot);
            pst.setString(4, kode);
            pst.execute();
            con.close(); pst.close();
            JOptionPane.showMessageDialog(null, "Berhasil simpan data");
        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(null, "error : "+e);
        }
    }
    
    public void hapus(String kode){
        String sql = "DELETE FROM `bbt_kriteria` WHERE kode_bbt=?";
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
    
    // mengambil nilai perbandingan antar kriteria
    public double[] getBbtKriteria(){
        String[][] data_kriteria = get_all();
        double[] data = new double[data_kriteria.length];
        for (int i = 0; i < data_kriteria.length; i++) {
            data[i] = Double.parseDouble(data_kriteria[i][data_kriteria[i].length - 1]);
        }
        return data;
    }
    
    //perbandingan antar kriteria
    public String[][] pak(){ 
        String[][] data_kriteria = kriteria.get_all();
        double[] input = getBbtKriteria();
        double[][] pak = new double[data_kriteria.length][data_kriteria.length];
        int index = 0;
        for (int i = 0; i < data_kriteria.length; i++) {
            for (int j = i; j < data_kriteria.length; j++) {
                if(i == j){
                    pak[i][j] = 1;
                }else{
                    pak[i][j] = input[index];
                    pak[j][i] = 1 / input[index];
                    index++;
                }
            }
        }
        String[][] data = new String[pak.length][pak[0].length];
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                data[i][j] = String.valueOf(pak[i][j]);
            }
        }
        return data;
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
        bobot_kriteria bk = new bobot_kriteria();
        
//        bk.save("C01", "C02", "10");
//        bk.edit("C01", "C03", "5", "BC11");
//        bk.hapus("BC11");
//        String[][] data_bbt_kriteria_tunggal = bk.getById("BC01");
        String[][] pak = bk.pak();
        bk.show2D(pak);
    }
}
