package com.derteuffel.infoenspm.entities;

public class PieceJointe {
    private  int id,size;
    private  String fileDownloadUri, fileName, fileType;

    public PieceJointe() {
    }

    public PieceJointe(int size, String fileDownloadUri, String fileName, String fileType) {
        this.size = size;
        this.fileDownloadUri = fileDownloadUri;
        this.fileName = fileName;
        this.fileType = fileType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getFileDownloadUri() {
        return fileDownloadUri;
    }

    public void setFileDownloadUri(String fileDownloadUri) {
        this.fileDownloadUri = fileDownloadUri;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
}
