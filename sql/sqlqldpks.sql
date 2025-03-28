CREATE DATABASE QuanLyKhachSan;
GO
USE QuanLyKhachSan;
GO

-- Bảng Khách Hàng
CREATE TABLE KhachHang (
    maKH NVARCHAR(50) PRIMARY KEY,
    tenKH NVARCHAR(100),
    CCCD NVARCHAR(20),
    sdt NVARCHAR(15),
    email NVARCHAR(100)
);

-- Bảng Nhân Viên
CREATE TABLE NhanVien (
    maNhanVien NVARCHAR(50) PRIMARY KEY,
    tenNhanVien NVARCHAR(100),
    soCCCD NVARCHAR(20),
    ngaySinh DATE,
    gioiTinh BIT,
    sdt NVARCHAR(15),
    email NVARCHAR(100),
    chucVu NVARCHAR(50)
);

-- Bảng Tài Khoản
CREATE TABLE TaiKhoan (
    taiKhoan NVARCHAR(50) PRIMARY KEY,
    matKhau NVARCHAR(255),
    maNhanVien NVARCHAR(50) UNIQUE,
    FOREIGN KEY (maNhanVien) REFERENCES NhanVien(maNhanVien)
);

-- Bảng Phiếu Đặt Phòng
CREATE TABLE PhieuDatPhong (
    maPhieuDatPhong NVARCHAR(50) PRIMARY KEY,
    maNhanVien NVARCHAR(50),
    maKhachHang NVARCHAR(50),
    FOREIGN KEY (maNhanVien) REFERENCES NhanVien(maNhanVien),
    FOREIGN KEY (maKhachHang) REFERENCES KhachHang(maKH)
);

-- Bảng Chi Tiết Phiếu Đặt Phòng
CREATE TABLE CTPhieuDatPhong (
    maPhieuDatPhong NVARCHAR(50),
    maPhong NVARCHAR(50),
    gioDatPhong DATETIME,
    gioTraPhong DATETIME,
    PRIMARY KEY (maPhieuDatPhong, maPhong),
    FOREIGN KEY (maPhieuDatPhong) REFERENCES PhieuDatPhong(maPhieuDatPhong),
    FOREIGN KEY (maPhong) REFERENCES Phong(maPhong)
);

-- Bảng Loại Phòng
CREATE TABLE LoaiPhong (
    maLoaiPhong NVARCHAR(50) PRIMARY KEY,
    tenLoaiPhong NVARCHAR(100)
);

-- Bảng Phòng
CREATE TABLE Phong (
    maPhong NVARCHAR(50) PRIMARY KEY,
    tenPhong NVARCHAR(100),
    maLoaiPhong NVARCHAR(50),
    soGiuong INT,
    giaPhongTheoGiuong FLOAT,
    trangThai NVARCHAR(50),
    kieuThue BIT,
    FOREIGN KEY (maLoaiPhong) REFERENCES LoaiPhong(maLoaiPhong)
);

-- Bảng Khuyến Mãi
CREATE TABLE KhuyenMai (
    maKM NVARCHAR(50) PRIMARY KEY,
    moTa NVARCHAR(255),
    ngayApDung DATETIME,
    ngayHetHan DATETIME,
    tienApDungKM FLOAT,
    phanTramKM FLOAT
);

-- Bảng Hóa Đơn
CREATE TABLE HoaDon (
    maHoaDon NVARCHAR(50) PRIMARY KEY,
    maNhanVien NVARCHAR(50),
    maKhachHang NVARCHAR(50),
    maKM NVARCHAR(50) NULL,
    FOREIGN KEY (maNhanVien) REFERENCES NhanVien(maNhanVien),
    FOREIGN KEY (maKhachHang) REFERENCES KhachHang(maKH),
    FOREIGN KEY (maKM) REFERENCES KhuyenMai(maKM)
);

-- Bảng Loại Dịch Vụ
CREATE TABLE LoaiDichVu (
    maLoaiDV NVARCHAR(50) PRIMARY KEY,
    tenLoaiDV NVARCHAR(100)
);

-- Bảng Dịch Vụ
CREATE TABLE DichVu (
    maDichVu NVARCHAR(50) PRIMARY KEY,
    tenDichVu NVARCHAR(100),
    giaTien FLOAT,
    trangThai BIT,
    moTa NVARCHAR(255),
    maLoaiDV NVARCHAR(50),
    FOREIGN KEY (maLoaiDV) REFERENCES LoaiDichVu(maLoaiDV)
);

-- Bảng Chi Tiết Hóa Đơn
CREATE TABLE ChiTietHoaDon (
    maHoaDon NVARCHAR(50),
    maDichVu NVARCHAR(50),
    maPhong NVARCHAR(50),
    ngayLapHoaDon DATETIME,
    trangThai BIT,
    phuongThucTT NVARCHAR(50),
	soLuongDV INT,
    PRIMARY KEY (maHoaDon, maDichVu, maPhong),
    FOREIGN KEY (maHoaDon) REFERENCES HoaDon(maHoaDon),
    FOREIGN KEY (maDichVu) REFERENCES DichVu(maDichVu),
    FOREIGN KEY (maPhong) REFERENCES Phong(maPhong)
);
