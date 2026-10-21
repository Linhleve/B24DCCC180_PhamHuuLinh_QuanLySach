package com.example.quanlysach.model;

import java.io.Serializable;

/**
 * Model lớp Book - đại diện cho một cuốn sách trong thư viện cá nhân.
 * Implements Serializable để truyền qua Intent dễ dàng.
 */
public class Book implements Serializable {

    // Các thuộc tính của sách
    private String tenSach;   // Tên sách
    private String tacGia;    // Tác giả
    private String theLoai;   // Thể loại
    private String tinhTrang; // Tình trạng: "Chưa đọc" / "Đang đọc" / "Đã đọc"
    private int danhGia;      // Đánh giá số sao (0-5)
    private String ghiChu;    // Ghi chú (có thể rỗng)

    // Constructor đầy đủ tham số
    public Book(String tenSach, String tacGia, String theLoai,
                String tinhTrang, int danhGia, String ghiChu) {
        this.tenSach = tenSach;
        this.tacGia = tacGia;
        this.theLoai = theLoai;
        this.tinhTrang = tinhTrang;
        this.danhGia = danhGia;
        // Tránh null cho ghi chú
        this.ghiChu = (ghiChu != null) ? ghiChu : "";
    }

    // --- Getters ---
    public String getTenSach()   { return tenSach; }
    public String getTacGia()    { return tacGia; }
    public String getTheLoai()   { return theLoai; }
    public String getTinhTrang() { return tinhTrang; }
    public int getDanhGia()      { return danhGia; }
    public String getGhiChu()    { return ghiChu; }

    // --- Setters ---
    public void setTenSach(String tenSach)     { this.tenSach = tenSach; }
    public void setTacGia(String tacGia)       { this.tacGia = tacGia; }
    public void setTheLoai(String theLoai)     { this.theLoai = theLoai; }
    public void setTinhTrang(String tinhTrang) { this.tinhTrang = tinhTrang; }
    public void setDanhGia(int danhGia)        { this.danhGia = danhGia; }
    public void setGhiChu(String ghiChu) {
        this.ghiChu = (ghiChu != null) ? ghiChu : "";
    }
}
