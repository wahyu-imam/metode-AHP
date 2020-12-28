/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ahp;

import java.text.DecimalFormat;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import database.database;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author imam
 */
public class ahp {
    DecimalFormat df;
    Connection con;
    Statement stm;
    ResultSet rs;
    database db;
    
    public ahp(){
        db = new database();
        df = new DecimalFormat("#.####");
    }
    
    public String[][] data_kriteria(){
//        String[][] data = {{"C01","Jarak ke pondok mahasiswa"},
//                           {"C02","Jarak ke sarana pendidikan"},
//                           {"C03","Jarak dengan BTS"},
//                           {"C04","Pesaing"},
//                           {"C05","Luas bangunan"}};
        String[][] data = new String[5][2];
        String sql = "SELECT * FROM `kriteria`";
        try {
            con = db.getKoneksi();
            stm = con.createStatement();
            rs = stm.executeQuery(sql);
            int i = 0;
            while(rs.next()){
                data[i][0] = rs.getString(1);
                data[i][1] = rs.getString(2);
                i++;
            }
            con.close(); stm.close(); rs.close();
        } catch (SQLException e) {
            System.out.println("error : "+e);
        }
        return data;
    }
    
    public String[][] data_alternatif(){
//        String[][] data = {{"A01","Lokasi 1"},
//                           {"A02","Lokasi 2"},
//                           {"A03","Lokasi 3"}};
        ArrayList<m_alternatif> tampung = new ArrayList<>();
        String sql = "SELECT * FROM `alternatif`";
        try {
            con = db.getKoneksi();
            stm = con.createStatement();
            rs = stm.executeQuery(sql);
            while(rs.next()){
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
            int no = 0;
            for (m_alternatif a : tampung) {
                data[no][0] = a.getKode();
                data[no][1] = a.getNama();
                data[no][2] = a.getN1();
                data[no][3] = a.getN2();
                data[no][4] = a.getN3();
                data[no][5] = a.getN4();
                data[no][6] = a.getN5();
                no++;
            }
        }
        return data;
    }
    
    public String[][] nilai_perbandingan(){
        String[][] data = {{"1","Sama penting dengan"},
                           {"2","Mendekati sedikit lebih penting dari"},
                           {"3","Sedikit lebih penting dari"},
                           {"4","Mendekati lebih penting dari"},
                           {"5","Lebih penting dari"},
                           {"6","Mendekati sangat penting dari"},
                           {"7","Sangat penting dari"},
                           {"8","Mendekati mutlak dari"},
                           {"9","Mutlak sangat penting dari"}};
        return data;
    }
    
    // mengambil nilai perbandingan antar kriteria
    public double[] getBbtKriteria(){
        ArrayList<Double> tampung = new ArrayList<>();
        String sql = "select bobot from bbt_kriteria";
        try {
            con = db.getKoneksi();
            stm = con.createStatement();
            rs = stm.executeQuery(sql);
            while(rs.next()){
                tampung.add(Double.parseDouble(rs.getString(1)));
            }
            con.close(); stm.close(); rs.close();
        } catch (NumberFormatException | SQLException e) {
            System.out.println("error : "+e);
        }
        double[] data = null;
        if(!tampung.isEmpty()){
            data = new double[tampung.size()];
            for (int i = 0; i < data.length; i++) {
                data[i] = tampung.get(i);
            }
        }
        return data;
    }
    
    //perbandingan antar kriteria
    public String[][] pak(){ 
        String[][] data_kriteria = data_kriteria();
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
    
    //mengambil nilai perbandingan antar alternatif berdasarkan kriteria
    public String[][] getBbtAlternatif(){
        ArrayList<String> tampung = new ArrayList<>();
        String[][] data_alternatif = data_alternatif();
        for (int i = 0; i < data_alternatif.length; i++) {
            for (int j = 2; j < data_alternatif[i].length; j++) {
                tampung.add(data_alternatif[i][j]);
            }
        }
        String[][] data = new String[tampung.size() / data_alternatif.length][data_alternatif.length];
        int index = 0;
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                data[i][j] = tampung.get(index);
                index++;
            }
        }
        return data;
    }
    
    //perbandingan antar alternatif berdasarkan kriteria
    private String[][] paa(double[] input){ 
        String[][] data_alternatif = data_alternatif();
//        int index = 0;
        double[][] paa = new double[data_alternatif.length][data_alternatif.length];
        for (int i = 0; i < data_alternatif.length; i++) {
            for (int j = i; j < data_alternatif.length; j++) {
                if(i == j){
                    double bbt = input[i] / input[j];
                    paa[i][j] = bbt;
                }else{
                    double pembilang = input[i];
                    double penyebut = input[j];
                    paa[i][j] = pembilang / penyebut;
                    paa[j][i] = penyebut / pembilang;
//                    index++;
                }
            }
        }
        String[][] data = new String[paa.length][paa[0].length];
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                data[i][j] = String.valueOf(paa[i][j]);
            }
        }
        return data;
    }
    
    public String[][] paa_all_alternatif(){
        ArrayList<String> tampung = new ArrayList<>();
        String[][] dataBbtAlternatif = getBbtAlternatif();
        for (String[] ba : dataBbtAlternatif) {
            double[] input = new double[ba.length];
            for (int j = 0; j < ba.length; j++) {
                input[j] = Double.parseDouble(ba[j]);
            }
            String[][] ppa = paa(input);
            for (String[] ppa1 : ppa) {
                tampung.addAll(Arrays.asList(ppa1));
            }
        }
        int baris = dataBbtAlternatif.length * dataBbtAlternatif[0].length;
        int kolom = dataBbtAlternatif[0].length;
        String[][] data = new String[baris][kolom];
        int index = 0;
        for (String[] d : data) {
            for (int j = 0; j < d.length; j++) {
                d[j] = tampung.get(index);
                index++;
            }
        }
        return data;
    }
    
    //menghitung total bobot per kriteria
    public String[][] getTotalKolomKriteria(){
        String[][] data_pak = pak();
        double[] total = new double[data_pak.length];
        double tot;
        for (int i = 0; i < data_pak.length; i++) {
            tot = 0;
            for (int j = 0; j < data_pak[i].length; j++) {
                tot += Double.parseDouble(data_pak[j][i]);
            }
            total[i] = tot;
        }
        String[][] data = new String[data_pak.length + 1][data_pak[0].length];
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                if(i == data.length - 1){
                    data[i][j] = String.valueOf(total[j]);
                }else{
                    data[i][j] = data_pak[i][j];
                }
            }
        }
        return data;
    }
    
    //Menormalisasikan matriks kriteria & menghitung bobot prioritas
    public String[][] normalisasiKriteria(){ 
        String[][] data_tot_baris_kriteria = getTotalKolomKriteria();
        double[][] normalisasi = new double[data_tot_baris_kriteria.length - 1][data_tot_baris_kriteria[0].length];
        for (int i = 0; i < normalisasi.length; i++) {
            for (int j = 0; j < normalisasi[i].length; j++) {
                double temp = Double.parseDouble(data_tot_baris_kriteria[i][j]) / 
                        Double.parseDouble(data_tot_baris_kriteria[data_tot_baris_kriteria.length - 1][j]);
                normalisasi[i][j] = temp;
            }
        }
        double[] bbt_prioritas = new double[normalisasi.length];
        for (int i = 0; i < normalisasi.length; i++) {
            double jml = 0;
            for (int j = 0; j < normalisasi[i].length + 1; j++) {
                if(j == normalisasi[i].length){
                    bbt_prioritas[i] = jml / normalisasi[0].length;
                }else{
                    jml += normalisasi[i][j];
                }
            }
        }
        String[][] data = new String[normalisasi.length][normalisasi[0].length + 1];
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                if(j == data[i].length - 1){
                    data[i][j] = String.valueOf(bbt_prioritas[i]);
                }else{
                    data[i][j] = String.valueOf(normalisasi[i][j]);
                }
            }
        }
        return data;
    }
    
    //menghitung konsistensi matrik
    public String[][] konsistensi_matrik(){
        String[][] data_pak = pak();
        String[][] data_nor_kriteria = normalisasiKriteria();
        double[][] cm = new double[data_nor_kriteria.length][data_nor_kriteria[0].length];
        for (int i = 0; i < cm.length; i++) {
            double jml = 0;
            for (int j = 0; j < cm[i].length - 1; j++) {
                jml += Double.parseDouble(data_pak[i][j]) *
                        Double.parseDouble(data_nor_kriteria[j][data_nor_kriteria[i].length - 1]);
                cm[i][j] = Double.parseDouble(data_nor_kriteria[i][j]);
            }
            cm[i][cm[i].length - 1] = jml / Double.parseDouble(data_nor_kriteria[i][data_nor_kriteria[i].length - 1]);
        }
        String[][] data = new String[cm.length][cm[0].length];
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                data[i][j] = String.valueOf(cm[i][j]);
            }
        }
        return data;
    }
    
    //menghitung Consistency Index
    public double ci(){
        String[][] data_cm = konsistensi_matrik();
        double jml = 0;
        for (String[] cm : data_cm) {
            jml += Double.parseDouble(cm[cm.length - 1]);
        }
        double lamdaMax = jml / data_cm.length;
        return (lamdaMax - data_cm.length) / (data_cm.length - 1);
    }
    
    //menentukan Ratio Index
    public double ri(){
        double[] ri = {0, 0, 0.58, 0.9, 1.12, 1.24, 1.32, 1.41, 1.46, 1.49};
        String[][] data_kriteria = data_kriteria();
        double data = 0;
        for (int i = 0; i < ri.length; i++) {
            if(i == data_kriteria.length - 1){
                data = ri[i];
            }
        }
        return data;
    }
    
    //menghitung Consistency Ratio
    public double cr(){
        double ci = ci();
        double ri = ri();
        return ci / ri;
    }
    
    //menghitung total bobot per alternatif berdasarkan kriteria
    private String[][] getTotalKolomAlternatif(String[][] paa){
//        String[][] paa = pak();
        double[] total = new double[paa.length];
        double tot;
        for (int i = 0; i < paa.length; i++) {
            tot = 0;
            for (int j = 0; j < paa[i].length; j++) {
                tot += Double.parseDouble(paa[j][i]);
            }
            total[i] = tot;
        }
        String[][] data = new String[paa.length + 1][paa[0].length];
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                if(i == data.length - 1){
                    data[i][j] = String.valueOf(total[j]);
                }else{
                    data[i][j] = paa[i][j];
                }
            }
        }
        return data;
    }
    
    public String[][] getAllTotalKolomAlternatif(){
        ArrayList<String> tampung = new ArrayList<>();
        ArrayList<String> tampungAll = new ArrayList<>();
        String[][] data_ppa = paa_all_alternatif();
        int no = data_ppa[0].length;
        for (int i = 0; i < data_ppa.length; i++) {
            for (int j = 0; j < data_ppa[i].length; j++) {
                tampung.add(data_ppa[i][j]);
            }
            if(i == no - 1){
                String[][] ppa = new String[tampung.size() / data_ppa[0].length][data_ppa[0].length];
                int index = 0;
                for (int j = 0; j < ppa.length; j++) {
                    for (int k = 0; k < ppa[j].length; k++) {
                        ppa[j][k] = tampung.get(index);
                        index++;
                    }
                }
                String[][] tot_kolom_alternatif = getTotalKolomAlternatif(ppa);
                for (int j = 0; j < tot_kolom_alternatif.length; j++) {
                    for (int k = 0; k < tot_kolom_alternatif[j].length; k++) {
                        tampungAll.add(tot_kolom_alternatif[j][k]);
                    }
                }
                no += data_ppa[0].length;
                tampung.clear();
            }
        }
        String[][] data = new String[tampungAll.size() / data_ppa[0].length][data_ppa[0].length];
        int index = 0;
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                data[i][j] = tampungAll.get(index);
                index++;
            }
        }
        return data;
    }
    //Menormalisasikan matriks alternatif & menghitung bobot prioritas
    private String[][] normalisasiAlternatif(String[][] total_kolom_alternatif){ 
//        String[][] total_kolom_alternatif = getTotalKolomKriteria();
        double[][] normalisasi = new double[total_kolom_alternatif.length - 1][total_kolom_alternatif[0].length];
        for (int i = 0; i < normalisasi.length; i++) {
            for (int j = 0; j < normalisasi[i].length; j++) {
                double temp = Double.parseDouble(total_kolom_alternatif[i][j]) / 
                        Double.parseDouble(total_kolom_alternatif[total_kolom_alternatif.length - 1][j]);
                normalisasi[i][j] = temp;
            }
        }
        double[] bbt_prioritas = new double[normalisasi.length];
        for (int i = 0; i < normalisasi.length; i++) {
            double jml = 0;
            for (int j = 0; j < normalisasi[i].length + 1; j++) {
                if(j == normalisasi[i].length){
                    bbt_prioritas[i] = jml / normalisasi[0].length;
                }else{
                    jml += normalisasi[i][j];
                }
            }
        }
        String[][] data = new String[normalisasi.length][normalisasi[0].length + 1];
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                if(j == data[i].length - 1){
                    data[i][j] = String.valueOf(bbt_prioritas[i]);
                }else{
                    data[i][j] = String.valueOf(normalisasi[i][j]);
                }
            }
        }
        return data;
    }
    
    public String[][] normalisasiAllAlternatif(){
        ArrayList<String> tampung = new ArrayList<>();
        ArrayList<String> tampungAll = new ArrayList<>();
        String[][] AllTotalKolomAlternatif = getAllTotalKolomAlternatif();
        int no = AllTotalKolomAlternatif[0].length + 1;
        for (int i = 0; i < AllTotalKolomAlternatif.length; i++) {
            for (int j = 0; j < AllTotalKolomAlternatif[i].length; j++) {
                tampung.add(AllTotalKolomAlternatif[i][j]);
            }
            if(i == no - 1){
                int baris = tampung.size() / AllTotalKolomAlternatif[0].length;
                int kolom = AllTotalKolomAlternatif[0].length;
                String[][] totalKolomAlternatif = new String[baris][kolom];
                int index = 0;
                for (int j = 0; j < totalKolomAlternatif.length; j++) {
                    for (int k = 0; k < totalKolomAlternatif[j].length; k++) {
                        totalKolomAlternatif[j][k] = tampung.get(index);
                        index++;
                    }
                }
                String[][] norAlternatif = normalisasiAlternatif(totalKolomAlternatif);
                for (int j = 0; j < norAlternatif.length; j++) {
                    for (int k = 0; k < norAlternatif[j].length; k++) {
                        tampungAll.add(norAlternatif[j][k]);
                    }
                }
                no += AllTotalKolomAlternatif[0].length + 1;
                tampung.clear();
            }
        }
        int baris = tampungAll.size() / (AllTotalKolomAlternatif[0].length + 1);
        int kolom = AllTotalKolomAlternatif[0].length + 1;
        String[][] data = new String[baris][kolom];
        int index = 0;
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                data[i][j] = tampungAll.get(index);
                index++;
            }
        }
        return data;
    }
    
    //menghitung ranking
    public String[][] perangkingan(){
        ArrayList<String> tampung = new ArrayList<>();
        ArrayList<String> tampungAll = new ArrayList<>();
        String[][] data_alternatif = data_alternatif();
        String[][] data_nor_kriteria = normalisasiKriteria();
        String[][] bpa = normalisasiAllAlternatif();
        int no = data_alternatif.length;
        for (int i = 0; i < bpa.length; i++) {
            for (int j = 0; j < bpa[i].length; j++) {
                tampung.add(bpa[i][j]);
            }
            if(i == no -1){
                for (int j = data_alternatif.length; j < tampung.size(); j += data_alternatif.length + 1) {
                    tampungAll.add(tampung.get(j));
                }
                no += data_alternatif.length;
                tampung.clear();
            }
        }
        String[][] nor_alternatif = new String[data_alternatif.length][data_nor_kriteria.length];
        int kolom = 0;
        tampung.clear();
        for (int i = 0; i < tampungAll.size(); i++) {
            tampung.add(tampungAll.get(i));
            if(tampung.size() == data_alternatif.length){
                for (int j = 0; j < tampung.size(); j++) {
                    nor_alternatif[j][kolom] = tampung.get(j);
                }
                kolom++;
                tampung.clear();
            }
        }
        double[] rank = new double[data_alternatif.length];
        for (int i = 0; i < nor_alternatif.length; i++) {
            double jml = 0;
            for (int j = 0; j < nor_alternatif[i].length; j++) {
                double bbtKriteria = Double.parseDouble(data_nor_kriteria[j][data_nor_kriteria[0].length - 1]);
                double bbt_alternatif_k = Double.parseDouble(nor_alternatif[i][j]);
                jml += bbtKriteria * bbt_alternatif_k;
            }
            rank[i] = jml;
        }
        String[][] data = new String[data_alternatif.length][data_nor_kriteria.length + 2];
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length - 2; j++) {
                data[i][j] = nor_alternatif[i][j];
            }
            data[i][data[0].length - 2] = String.valueOf(rank[i]);
            data[i][data[0].length - 1] = data_alternatif[i][0];
        }
        return data;
    }
    
    public String[][] hitung_ahp(){
        boolean laporan = false;
        
        String[][] pak = pak();
        String[][] paa = paa_all_alternatif();
        String[][] total_kolom_kriteria = getTotalKolomKriteria();
        String[][] normalisasiKriteria = normalisasiKriteria();
        String[][] cm = konsistensi_matrik();
        double ci = ci();
        double ri = ri();
        double cr = cr();
        String temp;
        if(cr >= 0 && cr <= 1){
            temp = "Konsisten";
        }else temp = "Tidak Konsisten";
        String[][] nor_alternatif = normalisasiAllAlternatif();
        String[][] rank = perangkingan();
        
        if(laporan){
            System.out.println("Perbandingan antar kriteria");
            show2D(pak);
            System.out.println("\nPerbandingan antar alternatif");
            show2D(paa);
            System.out.println("\nPerhitungan metode AHP");
            System.out.println("a. Perhitungan bobot prioritas kriteria");
            System.out.println("  1. Mencari Kolom total");
            show2D(total_kolom_kriteria);
            System.out.println("\n  2. Menormalisasikan matriks & bobot prioritas");
            show2D(normalisasiKriteria);
            System.out.println("\n  3. Mencari Konsistensi Matriks");
            show2D(cm);
            System.out.println("\nConsistency Index : "+df.format(ci));
            System.out.println("\nRatio Index : "+ri);
            System.out.println("\nConsistency Ratio : "+df.format(cr)+" ("+temp+")");
            System.out.println("\nb. Perhitungan Bobot Prioritas Alternatif");
            show2D(nor_alternatif);
            System.out.println("\nPerankingan");
            for (String[] r : rank) {
                for (int j = 0; j < r.length - 1; j++) {
                    double tmp = Double.parseDouble(r[j]);
                    System.out.print(df.format(tmp)+"; ");
                }
                System.out.println(r[r.length - 1]);
            }
        }
        return rank;
    }
    
    public void show2D(String[][] data){
        for (String[] d1 : data) {
            for (String d2 : d1) {
                double temp = Double.parseDouble(d2);
                System.out.print(df.format(temp)+"; ");
            }
            System.out.println("");
        }
    }
    
    public static void main(String[] args) {
        DecimalFormat df = new DecimalFormat("#.###");
        ahp ahp = new ahp();
        
//        ahp.hitung_ahp();
        String[][] data_alternatif = ahp.data_alternatif();
        for (String[] d1 : data_alternatif) {
            for (String d2 : d1) {
                System.out.print(d2+"; ");
            }
            System.out.println("");
        }
        String[][] pak = ahp.pak();
        System.out.println("Perbandingan antar kriteria");
        ahp.show2D(pak);
        System.out.println("\nPerbandingan antar alternatif");
        String[][] paa = ahp.paa_all_alternatif();
        ahp.show2D(paa);
        System.out.println("\nPerhitungan metode AHP");
        System.out.println("a. Perhitungan bobot prioritas kriteria");
        System.out.println("  1. Mencari Kolom total");
        String[][] total_kolom_kriteria = ahp.getTotalKolomKriteria();
        ahp.show2D(total_kolom_kriteria);
        System.out.println("\n2. Menormalisasikan matriks & bobot prioritas");
        String[][] normalisasiKriteria = ahp.normalisasiKriteria();
        ahp.show2D(normalisasiKriteria);
        System.out.println("\n3. Mencari Konsistensi Matriks");
        String[][] cm = ahp.konsistensi_matrik();
        ahp.show2D(cm);
        double ci = ahp.ci();
        System.out.println("\nConsistency Index : "+df.format(ci));
        double ri = ahp.ri();
        System.out.println("\nRatio Index : "+ri);
        double cr = ahp.cr();
        String temp;
        if(cr >= 0 && cr <= 1){
            temp = "Konsisten";
        }else temp = "Tidak Konsisten";
        System.out.println("\nConsistency Ratio : "+df.format(cr)+" ("+temp+")");
        System.out.println("\nb. Perhitungan Bobot Prioritas Alternatif");
        String[][] nor_alternatif = ahp.normalisasiAllAlternatif();
        ahp.show2D(nor_alternatif);
        System.out.println("\nPerankingan");
        String[][] rank = ahp.perangkingan();
        for (String[] r : rank) {
            for (int j = 0; j < r.length - 1; j++) {
                double tmp = Double.parseDouble(r[j]);
                System.out.print(df.format(tmp)+"; ");
            }
            System.out.println(r[r.length - 1]);
        }
    }
}
