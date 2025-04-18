USE [master]
GO
/****** Object:  Database [QuanLyKhachSan]    Script Date: 4/3/2025 10:51:20 AM ******/
CREATE DATABASE [QuanLyKhachSan] ON  PRIMARY 
( NAME = N'QuanLyKhachSan', FILENAME = N'c:\Program Files\Microsoft SQL Server\MSSQL10_50.SQLEXPRESS\MSSQL\DATA\QuanLyKhachSan.mdf' , SIZE = 2304KB , MAXSIZE = UNLIMITED, FILEGROWTH = 1024KB )
 LOG ON 
( NAME = N'QuanLyKhachSan_log', FILENAME = N'c:\Program Files\Microsoft SQL Server\MSSQL10_50.SQLEXPRESS\MSSQL\DATA\QuanLyKhachSan_log.LDF' , SIZE = 832KB , MAXSIZE = 2048GB , FILEGROWTH = 10%)
GO
ALTER DATABASE [QuanLyKhachSan] SET COMPATIBILITY_LEVEL = 100
GO
IF (1 = FULLTEXTSERVICEPROPERTY('IsFullTextInstalled'))
begin
EXEC [QuanLyKhachSan].[dbo].[sp_fulltext_database] @action = 'enable'
end
GO
ALTER DATABASE [QuanLyKhachSan] SET ANSI_NULL_DEFAULT OFF 
GO
ALTER DATABASE [QuanLyKhachSan] SET ANSI_NULLS OFF 
GO
ALTER DATABASE [QuanLyKhachSan] SET ANSI_PADDING OFF 
GO
ALTER DATABASE [QuanLyKhachSan] SET ANSI_WARNINGS OFF 
GO
ALTER DATABASE [QuanLyKhachSan] SET ARITHABORT OFF 
GO
ALTER DATABASE [QuanLyKhachSan] SET AUTO_CLOSE ON 
GO
ALTER DATABASE [QuanLyKhachSan] SET AUTO_SHRINK OFF 
GO
ALTER DATABASE [QuanLyKhachSan] SET AUTO_UPDATE_STATISTICS ON 
GO
ALTER DATABASE [QuanLyKhachSan] SET CURSOR_CLOSE_ON_COMMIT OFF 
GO
ALTER DATABASE [QuanLyKhachSan] SET CURSOR_DEFAULT  GLOBAL 
GO
ALTER DATABASE [QuanLyKhachSan] SET CONCAT_NULL_YIELDS_NULL OFF 
GO
ALTER DATABASE [QuanLyKhachSan] SET NUMERIC_ROUNDABORT OFF 
GO
ALTER DATABASE [QuanLyKhachSan] SET QUOTED_IDENTIFIER OFF 
GO
ALTER DATABASE [QuanLyKhachSan] SET RECURSIVE_TRIGGERS OFF 
GO
ALTER DATABASE [QuanLyKhachSan] SET  ENABLE_BROKER 
GO
ALTER DATABASE [QuanLyKhachSan] SET AUTO_UPDATE_STATISTICS_ASYNC OFF 
GO
ALTER DATABASE [QuanLyKhachSan] SET DATE_CORRELATION_OPTIMIZATION OFF 
GO
ALTER DATABASE [QuanLyKhachSan] SET TRUSTWORTHY OFF 
GO
ALTER DATABASE [QuanLyKhachSan] SET ALLOW_SNAPSHOT_ISOLATION OFF 
GO
ALTER DATABASE [QuanLyKhachSan] SET PARAMETERIZATION SIMPLE 
GO
ALTER DATABASE [QuanLyKhachSan] SET READ_COMMITTED_SNAPSHOT OFF 
GO
ALTER DATABASE [QuanLyKhachSan] SET HONOR_BROKER_PRIORITY OFF 
GO
ALTER DATABASE [QuanLyKhachSan] SET RECOVERY SIMPLE 
GO
ALTER DATABASE [QuanLyKhachSan] SET  MULTI_USER 
GO
ALTER DATABASE [QuanLyKhachSan] SET PAGE_VERIFY CHECKSUM  
GO
ALTER DATABASE [QuanLyKhachSan] SET DB_CHAINING OFF 
GO
USE [QuanLyKhachSan]
GO
/****** Object:  Table [dbo].[CTPhieuDatPhong]    Script Date: 4/3/2025 10:51:20 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[CTPhieuDatPhong](
	[maPhieuDatPhong] [nvarchar](50) NOT NULL,
	[maPhong] [nvarchar](50) NOT NULL,
	[gioDatPhong] [datetime] NULL,
	[gioTraPhong] [datetime] NULL,
	[kieuThue] [bit] NOT NULL,
	[giaPhongtheoKieuThue] [decimal](14, 2) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[maPhieuDatPhong] ASC,
	[maPhong] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[ChiTietHoaDon]    Script Date: 4/3/2025 10:51:21 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[ChiTietHoaDon](
	[maHoaDon] [nvarchar](50) NOT NULL,
	[maDichVu] [nvarchar](50) NOT NULL,
	[maPhong] [nvarchar](50) NOT NULL,
	[ngayLapHoaDon] [datetime] NULL,
	[trangThai] [bit] NULL,
	[phuongThucTT] [nvarchar](50) NULL,
	[soLuongDV] [int] NULL,
PRIMARY KEY CLUSTERED 
(
	[maHoaDon] ASC,
	[maDichVu] ASC,
	[maPhong] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[DichVu]    Script Date: 4/3/2025 10:51:21 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[DichVu](
	[maDichVu] [nvarchar](50) NOT NULL,
	[tenDichVu] [nvarchar](100) NULL,
	[giaTien] [float] NULL,
	[trangThai] [bit] NULL,
	[moTa] [nvarchar](255) NULL,
	[maLoaiDV] [nvarchar](50) NULL,
PRIMARY KEY CLUSTERED 
(
	[maDichVu] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[HoaDon]    Script Date: 4/3/2025 10:51:21 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[HoaDon](
	[maHoaDon] [nvarchar](50) NOT NULL,
	[maNhanVien] [nvarchar](50) NULL,
	[maKhachHang] [nvarchar](50) NULL,
	[maKM] [nvarchar](50) NULL,
PRIMARY KEY CLUSTERED 
(
	[maHoaDon] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[KhachHang]    Script Date: 4/3/2025 10:51:21 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[KhachHang](
	[maKH] [nvarchar](50) NOT NULL,
	[tenKH] [nvarchar](100) NULL,
	[CCCD] [nvarchar](20) NULL,
	[sdt] [nvarchar](15) NULL,
	[email] [nvarchar](100) NULL,
PRIMARY KEY CLUSTERED 
(
	[maKH] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[KhuyenMai]    Script Date: 4/3/2025 10:51:21 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[KhuyenMai](
	[maKM] [nvarchar](50) NOT NULL,
	[moTa] [nvarchar](255) NULL,
	[ngayApDung] [datetime] NULL,
	[ngayHetHan] [datetime] NULL,
	[tienApDungKM] [float] NULL,
	[phanTramKM] [float] NULL,
PRIMARY KEY CLUSTERED 
(
	[maKM] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[LoaiDichVu]    Script Date: 4/3/2025 10:51:21 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[LoaiDichVu](
	[maLoaiDV] [nvarchar](50) NOT NULL,
	[tenLoaiDV] [nvarchar](100) NULL,
PRIMARY KEY CLUSTERED 
(
	[maLoaiDV] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[LoaiPhong]    Script Date: 4/3/2025 10:51:21 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[LoaiPhong](
	[maLoaiPhong] [nvarchar](50) NOT NULL,
	[tenLoaiPhong] [nvarchar](100) NULL,
	[moTa] [nvarchar](100) NULL,
	[giaPhonggio] [decimal](14, 2) NULL,
	[giaPhongngay] [decimal](14, 2) NULL,
PRIMARY KEY CLUSTERED 
(
	[maLoaiPhong] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[NhanVien]    Script Date: 4/3/2025 10:51:21 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[NhanVien](
	[maNhanVien] [nvarchar](50) NOT NULL,
	[tenNhanVien] [nvarchar](100) NULL,
	[soCCCD] [nvarchar](20) NULL,
	[ngaySinh] [date] NULL,
	[gioiTinh] [bit] NULL,
	[sdt] [nvarchar](15) NULL,
	[email] [nvarchar](100) NULL,
	[chucVu] [nvarchar](50) NULL,
PRIMARY KEY CLUSTERED 
(
	[maNhanVien] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[PhieuDatPhong]    Script Date: 4/3/2025 10:51:21 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[PhieuDatPhong](
	[maPhieuDatPhong] [nvarchar](50) NOT NULL,
	[maNhanVien] [nvarchar](50) NULL,
	[maKhachHang] [nvarchar](50) NULL,
PRIMARY KEY CLUSTERED 
(
	[maPhieuDatPhong] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Phong]    Script Date: 4/3/2025 10:51:21 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Phong](
	[maPhong] [nvarchar](50) NOT NULL,
	[tenPhong] [nvarchar](100) NULL,
	[maLoaiPhong] [nvarchar](50) NULL,
	[soGiuong] [int] NULL,
	[giaPhongTheoGiuong] [float] NULL,
	[trangThai] [nvarchar](50) NULL,
	[tang] [int] NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[maPhong] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[TaiKhoan]    Script Date: 4/3/2025 10:51:21 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[TaiKhoan](
	[taiKhoan] [nvarchar](50) NOT NULL,
	[matKhau] [nvarchar](255) NULL,
	[maNhanVien] [nvarchar](50) NULL,
PRIMARY KEY CLUSTERED 
(
	[taiKhoan] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
UNIQUE NONCLUSTERED 
(
	[maNhanVien] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
ALTER TABLE [dbo].[CTPhieuDatPhong]  WITH CHECK ADD FOREIGN KEY([maPhieuDatPhong])
REFERENCES [dbo].[PhieuDatPhong] ([maPhieuDatPhong])
GO
ALTER TABLE [dbo].[CTPhieuDatPhong]  WITH CHECK ADD FOREIGN KEY([maPhong])
REFERENCES [dbo].[Phong] ([maPhong])
GO
ALTER TABLE [dbo].[ChiTietHoaDon]  WITH CHECK ADD FOREIGN KEY([maDichVu])
REFERENCES [dbo].[DichVu] ([maDichVu])
GO
ALTER TABLE [dbo].[ChiTietHoaDon]  WITH CHECK ADD FOREIGN KEY([maHoaDon])
REFERENCES [dbo].[HoaDon] ([maHoaDon])
GO
ALTER TABLE [dbo].[ChiTietHoaDon]  WITH CHECK ADD FOREIGN KEY([maPhong])
REFERENCES [dbo].[Phong] ([maPhong])
GO
ALTER TABLE [dbo].[DichVu]  WITH CHECK ADD FOREIGN KEY([maLoaiDV])
REFERENCES [dbo].[LoaiDichVu] ([maLoaiDV])
GO
ALTER TABLE [dbo].[HoaDon]  WITH CHECK ADD FOREIGN KEY([maKM])
REFERENCES [dbo].[KhuyenMai] ([maKM])
GO
ALTER TABLE [dbo].[HoaDon]  WITH CHECK ADD FOREIGN KEY([maKhachHang])
REFERENCES [dbo].[KhachHang] ([maKH])
GO
ALTER TABLE [dbo].[HoaDon]  WITH CHECK ADD FOREIGN KEY([maNhanVien])
REFERENCES [dbo].[NhanVien] ([maNhanVien])
GO
ALTER TABLE [dbo].[PhieuDatPhong]  WITH CHECK ADD FOREIGN KEY([maKhachHang])
REFERENCES [dbo].[KhachHang] ([maKH])
GO
ALTER TABLE [dbo].[PhieuDatPhong]  WITH CHECK ADD FOREIGN KEY([maNhanVien])
REFERENCES [dbo].[NhanVien] ([maNhanVien])
GO
ALTER TABLE [dbo].[Phong]  WITH CHECK ADD FOREIGN KEY([maLoaiPhong])
REFERENCES [dbo].[LoaiPhong] ([maLoaiPhong])
GO
ALTER TABLE [dbo].[TaiKhoan]  WITH CHECK ADD FOREIGN KEY([maNhanVien])
REFERENCES [dbo].[NhanVien] ([maNhanVien])
GO
USE [master]
GO
ALTER DATABASE [QuanLyKhachSan] SET  READ_WRITE 
GO
INSERT INTO Phong (maPhong, tenPhong, maLoaiPhong, soGiuong, giaPhongTheoGiuong, trangThai,tang)
VALUES ('MP012', N'Phòng 205', N'MLP001', 2, 1.3, N'Trống',2);
INSERT INTO Phong (maPhong, tenPhong, maLoaiPhong, soGiuong, giaPhongTheoGiuong, trangThai,tang)
VALUES ('MP013', N'Phòng 206', N'MLP001', 2, 1.3, N'Trống',2);
INSERT INTO Phong (maPhong, tenPhong, maLoaiPhong, soGiuong, giaPhongTheoGiuong, trangThai,tang)
VALUES ('MP014', N'Phòng 207', N'MLP001', 3, 1.8, N'Trống',2);

INSERT INTO Phong (maPhong, tenPhong, maLoaiPhong, soGiuong, giaPhongTheoGiuong, trangThai,tang)
VALUES ('MP015', N'Phòng 301', N'MLP002', 1, 1, N'Trống',3);
INSERT INTO Phong (maPhong, tenPhong, maLoaiPhong, soGiuong, giaPhongTheoGiuong, trangThai,tang)
VALUES ('MP016', N'Phòng 302', N'MLP002', 1, 1, N'Trống',3);
INSERT INTO Phong (maPhong, tenPhong, maLoaiPhong, soGiuong, giaPhongTheoGiuong, trangThai,tang)
VALUES ('MP017', N'Phòng 303', N'MLP002', 1, 1, N'Trống',3);
INSERT INTO Phong (maPhong, tenPhong, maLoaiPhong, soGiuong, giaPhongTheoGiuong, trangThai,tang)
VALUES ('MP018', N'Phòng 304', N'MLP002', 1, 1, N'Trống',3);
INSERT INTO Phong (maPhong, tenPhong, maLoaiPhong, soGiuong, giaPhongTheoGiuong, trangThai,tang)
VALUES ('MP019', N'Phòng 305', N'MLP002', 2, 1.3, N'Trống',3);
INSERT INTO Phong (maPhong, tenPhong, maLoaiPhong, soGiuong, giaPhongTheoGiuong, trangThai,tang)
VALUES ('MP020', N'Phòng 306', N'MLP002', 2, 1.3, N'Trống',3);
INSERT INTO Phong (maPhong, tenPhong, maLoaiPhong, soGiuong, giaPhongTheoGiuong, trangThai,tang)
VALUES ('MP021', N'Phòng 307', N'MLP002', 3, 1.8, N'Trống',3);

INSERT INTO Phong (maPhong, tenPhong, maLoaiPhong, soGiuong, giaPhongTheoGiuong, trangThai,tang)
VALUES ('MP022', N'Phòng 401', N'MLP002', 1, 1, N'Trống',4);
INSERT INTO Phong (maPhong, tenPhong, maLoaiPhong, soGiuong, giaPhongTheoGiuong, trangThai,tang)
VALUES ('MP023', N'Phòng 402', N'MLP002', 1, 1, N'Trống',4);
INSERT INTO Phong (maPhong, tenPhong, maLoaiPhong, soGiuong, giaPhongTheoGiuong, trangThai,tang)
VALUES ('MP024', N'Phòng 403', N'MLP002', 1, 1, N'Trống',4);
INSERT INTO Phong (maPhong, tenPhong, maLoaiPhong, soGiuong, giaPhongTheoGiuong, trangThai,tang)
VALUES ('MP025', N'Phòng 404', N'MLP002', 1, 1, N'Trống',4);
INSERT INTO Phong (maPhong, tenPhong, maLoaiPhong, soGiuong, giaPhongTheoGiuong, trangThai,tang)
VALUES ('MP026', N'Phòng 405', N'MLP002', 2, 1.3, N'Trống',4);
INSERT INTO Phong (maPhong, tenPhong, maLoaiPhong, soGiuong, giaPhongTheoGiuong, trangThai,tang)
VALUES ('MP027', N'Phòng 406', N'MLP002', 2, 1.3, N'Trống',4);
INSERT INTO Phong (maPhong, tenPhong, maLoaiPhong, soGiuong, giaPhongTheoGiuong, trangThai,tang)
VALUES ('MP028', N'Phòng 407', N'MLP002', 3, 1.8, N'Trống',4);


INSERT INTO Phong (maPhong, tenPhong, maLoaiPhong, soGiuong, giaPhongTheoGiuong, trangThai,tang)
VALUES ('MP029', N'Phòng 501', N'MLP003', 1, 1, N'Trống',5);
INSERT INTO Phong (maPhong, tenPhong, maLoaiPhong, soGiuong, giaPhongTheoGiuong, trangThai,tang)
VALUES ('MP030', N'Phòng 502', N'MLP003', 1, 1, N'Trống',5);
INSERT INTO Phong (maPhong, tenPhong, maLoaiPhong, soGiuong, giaPhongTheoGiuong, trangThai,tang)
VALUES ('MP032', N'Phòng 503', N'MLP003', 1, 1, N'Trống',5);
INSERT INTO Phong (maPhong, tenPhong, maLoaiPhong, soGiuong, giaPhongTheoGiuong, trangThai,tang)
VALUES ('MP031', N'Phòng 504', N'MLP003', 1, 1, N'Trống',5);
INSERT INTO Phong (maPhong, tenPhong, maLoaiPhong, soGiuong, giaPhongTheoGiuong, trangThai,tang)
VALUES ('MP033', N'Phòng 505', N'MLP003', 2, 1.3, N'Trống',5);
INSERT INTO Phong (maPhong, tenPhong, maLoaiPhong, soGiuong, giaPhongTheoGiuong, trangThai,tang)
VALUES ('MP034', N'Phòng 506', N'MLP003', 2, 1.3, N'Trống',5);
INSERT INTO Phong (maPhong, tenPhong, maLoaiPhong, soGiuong, giaPhongTheoGiuong, trangThai,tang)
VALUES ('MP035', N'Phòng 507', N'MLP003', 3, 1.8, N'Trống',5);