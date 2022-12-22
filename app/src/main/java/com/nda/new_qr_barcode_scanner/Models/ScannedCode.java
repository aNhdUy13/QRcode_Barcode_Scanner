package com.nda.new_qr_barcode_scanner.Models;

public class ScannedCode {
    private int id;
    private String scannedContent, scannedTime;


    public ScannedCode(int id, String scannedContent, String scannedTime) {
        this.id = id;
        this.scannedContent = scannedContent;
        this.scannedTime = scannedTime;
    }

    public int getId() {
        return id;
    }

    public String getScannedContent() {
        return scannedContent;
    }

    public void setScannedContent(String scannedContent) {
        this.scannedContent = scannedContent;
    }

    public String getScannedTime() {
        return scannedTime;
    }

    public void setScannedTime(String scannedTime) {
        this.scannedTime = scannedTime;
    }
}
