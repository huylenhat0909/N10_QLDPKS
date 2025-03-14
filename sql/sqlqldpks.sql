CREATE DATABASE QLDPKS;
-- Chọn database để làm việc
USE QLDPKS;

-- Bảng Khách Hàng
CREATE TABLE KhachHang (
    maKH VARCHAR(20) PRIMARY KEY,
    tenKH VARCHAR(100) NOT NULL,
    CCCD VARCHAR(20) UNIQUE NOT NULL,
    sdt VARCHAR(15) NOT NULL,
    email VARCHAR(100)
);
ALTER TABLE KhachHang ALTER COLUMN tenKH NVARCHAR(50);
-- Bảng Nhân Viên
CREATE TABLE NhanVien (
    maNhanVien VARCHAR(20) PRIMARY KEY,
    tenNhanVien VARCHAR(100) NOT NULL,
    soCCCD VARCHAR(20) UNIQUE NOT NULL,
    ngaySinh DATE,
    gioiTinh bit,
    sdt VARCHAR(15) NOT NULL,
    email VARCHAR(100),
    chucVu VARCHAR(50) NOT NULL
);
ALTER TABLE NhanVien ALTER COLUMN tenNhanVien NVARCHAR(50);
ALTER TABLE NhanVien ALTER COLUMN chucVu NVARCHAR(50);
-- Bảng Tài Khoản
CREATE TABLE TaiKhoan (
    nhanVien VARCHAR(20) UNIQUE NOT NULL,
    taiKhoan VARCHAR(50)PRIMARY KEY,
    FOREIGN KEY (nhanVien) REFERENCES NhanVien(maNhanVien)
);

-- Bảng Loại Phòng
CREATE TABLE LoaiPhong (
    maLoaiPhong VARCHAR(20) PRIMARY KEY,
    tenLoaiPhong VARCHAR(100) NOT NULL,
    soTang INT NOT NULL,
    moTa VARCHAR(100),
    giaPhongTheoGio DECIMAL(15,2) NOT NULL,
    giaPhongTheoNgay DECIMAL(15,2) NOT NULL
);
ALTER TABLE LoaiPhong ALTER COLUMN tenLoaiPhong NVARCHAR(50);
ALTER TABLE LoaiPhong ALTER COLUMN moTa NVARCHAR(100);
-- Bảng Phòng
CREATE TABLE Phong (
    maPhong VARCHAR(20) PRIMARY KEY,
    tenPhong VARCHAR(100) NOT NULL,
    loaiPhong VARCHAR(20) NOT NULL,
    soGiuong INT NOT NULL,
    giaPhongTheoGiuong DECIMAL(15,2),
	kieThue bit,
    trangThai VARCHAR(50) NOT NULL,
    FOREIGN KEY (loaiPhong) REFERENCES LoaiPhong(maLoaiPhong)
);
ALTER TABLE Phong ALTER COLUMN tenPhong NVARCHAR(20);
-- Bảng Khuyến Mãi
CREATE TABLE KhuyenMai (
    maKM VARCHAR(20) PRIMARY KEY,
    moTa varchar(50),
    ngayApDung DATE NOT NULL,
    ngayHetHan DATE NOT NULL,
    tienApDungKM DECIMAL(15,2),
    phanTramKM FLOAT NOT NULL
);
ALTER TABLE KhuyenMai ALTER COLUMN moTa NVARCHAR(50);
-- Bảng Hóa Đơn
CREATE TABLE HoaDon (
    maHoaDon VARCHAR(20) PRIMARY KEY,
    nhanVien VARCHAR(20) NOT NULL,
    khachHang VARCHAR(20) NOT NULL,
    khuyenMai VARCHAR(20),
	chitiethoadon varchar(20),
    FOREIGN KEY (nhanVien) REFERENCES NhanVien(maNhanVien),
    FOREIGN KEY (khachHang) REFERENCES KhachHang(maKH),
    FOREIGN KEY (khuyenMai) REFERENCES KhuyenMai(maKM),
	FOREIGN KEY (chitiethoadon) REFERENCES ChiTietHoaDon(maCTHD)
);

-- Bảng Thông Tin Đặt Phòng
CREATE TABLE ThongTinDatPhong (
    maTTP VARCHAR(20) PRIMARY KEY,
    khachHang VARCHAR(20) NOT NULL,
    nhanVien VARCHAR(20) NOT NULL,
    gioDatPhong DATETIME NOT NULL,
    gioTraPhong DATETIME NOT NULL,
    phong VARCHAR(20) NOT NULL,
    FOREIGN KEY (khachHang) REFERENCES KhachHang(maKH),
    FOREIGN KEY (nhanVien) REFERENCES NhanVien(maNhanVien),
    FOREIGN KEY (phong) REFERENCES Phong(maPhong)
);

-- Bảng Loại Dịch Vụ
CREATE TABLE LoaiDichVu (
    maLoaiDV VARCHAR(20) PRIMARY KEY,
    tenDV VARCHAR(100) NOT NULL
);
ALTER TABLE LoaiDichVu ALTER COLUMN tenDV NVARCHAR(50);
-- Bảng Dịch Vụ
CREATE TABLE DichVu (
    maDichVu VARCHAR(20) PRIMARY KEY,
    tenDichVu VARCHAR(100) NOT NULL,
    giaTien Decimal(15,2) NOT NULL,
    trangThai int NOT NULL,
    moTa TEXT,
    loaiDV VARCHAR(20) NOT NULL,
    FOREIGN KEY (loaiDV) REFERENCES LoaiDichVu(maLoaiDV)
);
ALTER TABLE DichVu ALTER COLUMN tenDichVu NVARCHAR(50);
-- Bảng Chi Tiết Dịch Vụ
CREATE TABLE ChiTietDichVu (
    maCTDV VARCHAR(20) PRIMARY KEY,
    dichVu VARCHAR(20) NOT NULL,
    soLuong INT NOT NULL,
    FOREIGN KEY (dichVu) REFERENCES DichVu(maDichVu)
);

-- Bảng Chi Tiết Hóa Đơn
CREATE TABLE ChiTietHoaDon (
    maCTHD VARCHAR(20) PRIMARY KEY,
    cTDichVu VARCHAR(20) NOT NULL,
    phong VARCHAR(20) NOT NULL,
    ngayLapHoaDon DATETIME DEFAULT CURRENT_TIMESTAMP,
    trangThai bit NOT NULL,
    phuongThucTT VARCHAR(50) NOT NULL,
    FOREIGN KEY (cTDichVu) REFERENCES ChiTietDichVu(maCTDV),
    FOREIGN KEY (phong) REFERENCES Phong(maPhong)
);
ALTER TABLE ChiTietHoaDon ALTER COLUMN trangThai NVARCHAR(50);
--Them thuoc tinh mat khau cho bang tai khoan
ALTER TABLE TaiKhoan
ADD matkhau VARCHAR(15);

