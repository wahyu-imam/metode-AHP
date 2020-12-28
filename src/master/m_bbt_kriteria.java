/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package master;

/**
 *
 * @author imam
 */
public class m_bbt_kriteria {
    String kode;
    String dari;
    String ke;
    String bobot;
    
    public m_bbt_kriteria(String kode, String dari, String ke, String bobot){
        this.kode = kode;
        this.dari = dari;
        this.ke = ke;
        this.bobot = bobot;
    }

    public String getKode() {
        return kode;
    }

    public String getDari() {
        return dari;
    }

    public String getKe() {
        return ke;
    }

    public String getBobot() {
        return bobot;
    }
}
