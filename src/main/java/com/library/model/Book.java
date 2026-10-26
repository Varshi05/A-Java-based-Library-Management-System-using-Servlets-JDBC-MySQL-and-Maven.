package com.library.model;

public class Book {

    private int id;
    private String bname;
    private String author;
    private String btype;
    private float price;

    public Book() { }

    public Book(int id, String bname, String author, String btype, float price) {
        this.id = id;
        this.bname = bname;
        this.author = author;
        this.btype = btype;
        this.price = price;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getBname() { return bname; }
    public void setBname(String bname) { this.bname = bname; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getBtype() { return btype; }
    public void setBtype(String btype) { this.btype = btype; }

    public float getPrice() { return price; }
    public void setPrice(float price) { this.price = price; }
}