package com.nda.new_qr_barcode_scanner.Models;

public class SavedCode {
    private  int id_own;
    private String title_own;
    private byte[] img_own;

    public SavedCode(int id_own, String title_own, byte[] img_own){
        this.id_own = id_own;
        this.title_own = title_own;
        this.img_own = img_own;
    }

    public int getId_own() {
        return id_own;
    }

    public void setId_own(int id_own) {
        this.id_own = id_own;
    }

    public String getTitle_own() {
        return title_own;
    }

    public void setTitle_own(String title_own) {
        this.title_own = title_own;
    }

    public byte[] getImg_own() {
        return img_own;
    }

    public void setImg_own(byte[] img_own) {
        this.img_own = img_own;
    }
}
