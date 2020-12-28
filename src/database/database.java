/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import java.sql.Connection;
import java.sql.DriverManager;
/**
 *
 * @author imam
 */
public class database {
    public Connection getKoneksi(){
        Connection con = null;
        String userName = "root";
        String pass = "";
        String url = "jdbc:mysql://localhost/ahp";
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(url, userName, pass);
//            System.out.println("Koneksi Berhasil");
        } catch (Exception e) {
            System.out.println("koneksi gagal : "+e);
        }
        return con;
    }
    
//    public static void main(String[] args) {
//        database koneksi = new database();
//        koneksi.getKoneksi();
//    }
}
