-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Máy chủ: 127.0.0.1
-- Thời gian đã tạo: Th2 02, 2023 lúc 05:58 PM
-- Phiên bản máy phục vụ: 10.4.27-MariaDB
-- Phiên bản PHP: 7.4.33

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Cơ sở dữ liệu: `spring_app`
--
CREATE DATABASE IF NOT EXISTS `spring_app` DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci;
USE `spring_app`;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `account`
--

CREATE TABLE `account` (
  `idAccount` int(11) NOT NULL,
  `idUser` int(11) NOT NULL,
  `username` varchar(10) NOT NULL,
  `password` varchar(128) NOT NULL,
  `oldpass` varchar(128) NOT NULL,
  `role` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_vietnamese_ci;

--
-- Đang đổ dữ liệu cho bảng `account`
--

INSERT INTO `account` (`idAccount`, `idUser`, `username`, `password`, `oldpass`, `role`) VALUES
(1, 1, 'admin', '$2a$10$lxEtaIRX0wKWpGWfX25w9.dg6hICqtvPI8GttmBreWC59f2EXkFnG', '12345678', 'ROLE_ADMIN'),
(12, 9, '0987654321', '$2a$10$0xzH/XD7l5x88du9L1Y..Oo0oPCzkk1/T9dkuBHzSftwOSyk.xtxq', '01122022', 'ROLE_EMPLOYEE'),
(13, 10, '0123456788', '$2a$10$5RzHS7mZhvyxCQqb5i.EluctWCHQMFcqCbrizqXm5wZy4gmluW272', '01042022', 'ROLE_EMPLOYEE'),
(15, 15, '0123456789', '$2a$10$CCqRu/0NL35NkYO3gwTlPOOC33MMmXuvtZdo6N9cDx2Z2sZ9rz66y', '12122022', 'ROLE_USER'),
(16, 16, '0123456777', '$2a$10$A1eyWs6H6cDnveeyuZxVYe9lWxg71jdUazi4JuYVejlfj2FsAQE4m', '15122022', 'ROLE_USER'),
(18, 18, '0988888888', '$2a$10$Ge9kDIcupXBpTILTeeF2Eef2HlWPaqrPyh3ub6rHiZ68vpBatdht6', '26041987', 'ROLE_EMPLOYEE'),
(19, 19, '0989999999', '$2a$10$GzIgRdG9sXDN6H0PBqeIiOo.Po7GOTSVs6xeSHM.OJAwTksDN.aEm', '25042002', 'ROLE_USER'),
(20, 20, '1900629268', '$2a$10$0LzKX67kMV3q5GrDsL32y.0kFeXuzeVxeQGv9h28kcFALBuhzfnhS', '10052002', 'ROLE_USER'),
(21, 21, '0222222222', '$2a$10$0qp3006aUu5OV.jxwcLT8.3BEuqYfXtc7RQWJmRGYDzklU1s76g4.', '11042002', 'ROLE_EMPLOYEE'),
(22, 22, '0333333333', '$2a$10$Q5PZGpbwVx3PvkT/fh.V4e03V.Bpv1DKSPKE3cDiEF3eluI6mk5lW', '12042002', 'ROLE_USER');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `banan`
--

CREATE TABLE `banan` (
  `idBanAn` int(11) NOT NULL,
  `loaiBanAn` int(11) NOT NULL,
  `gioBD` varchar(10) NOT NULL,
  `gioKT` varchar(10) NOT NULL,
  `soLuongBanAn` int(11) NOT NULL,
  `isActive` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_vietnamese_ci;

--
-- Đang đổ dữ liệu cho bảng `banan`
--

INSERT INTO `banan` (`idBanAn`, `loaiBanAn`, `gioBD`, `gioKT`, `soLuongBanAn`, `isActive`) VALUES
(1, 2, '09:00', '14:00', 10, 1),
(2, 4, '09:00', '14:00', 10, 1),
(3, 2, '18:00', '23:00', 5, 1),
(5, 2, '07:00', '10:00', 5, 0),
(6, 4, '18:00', '23:00', 10, 1),
(7, 10, '09:00', '14:00', 10, 1),
(8, 6, '18:00', '23:00', 10, 1),
(9, 6, '09:00', '14:00', 10, 1),
(10, 6, '18:00', '23:00', 10, 0),
(16, 1, '09:17', '11:17', 1, 0),
(17, 10, '18:00', '23:00', 10, 1);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `chonmon`
--

CREATE TABLE `chonmon` (
  `idDatBan` int(11) NOT NULL,
  `idMonAn` int(11) NOT NULL,
  `soLuong` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_vietnamese_ci;

--
-- Đang đổ dữ liệu cho bảng `chonmon`
--

INSERT INTO `chonmon` (`idDatBan`, `idMonAn`, `soLuong`) VALUES
(7, 3, 1),
(7, 7, 1),
(7, 2, 2),
(7, 9, 1),
(7, 6, 2),
(11, 7, 1),
(11, 2, 1),
(11, 12, 1),
(11, 6, 1),
(12, 14, 2),
(12, 16, 1),
(12, 18, 2),
(12, 21, 1),
(14, 14, 1),
(14, 16, 1),
(14, 21, 1),
(13, 14, 2),
(13, 16, 1),
(13, 18, 1),
(13, 21, 1),
(15, 14, 2),
(15, 16, 1),
(15, 18, 1),
(15, 21, 1),
(16, 14, 1),
(16, 16, 1);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `datban`
--

CREATE TABLE `datban` (
  `idDatBan` int(11) NOT NULL,
  `idUser` int(11) NOT NULL,
  `idBanAn` int(11) NOT NULL,
  `soLuong` int(11) NOT NULL,
  `ngayDatBan` date NOT NULL,
  `gioDatBan` varchar(10) NOT NULL,
  `ghiChu` text NOT NULL,
  `isCheckin` tinyint(1) NOT NULL,
  `isActive` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_vietnamese_ci;

--
-- Đang đổ dữ liệu cho bảng `datban`
--

INSERT INTO `datban` (`idDatBan`, `idUser`, `idBanAn`, `soLuong`, `ngayDatBan`, `gioDatBan`, `ghiChu`, `isCheckin`, `isActive`) VALUES
(2, 15, 2, 2, '2022-12-16', '10:30', '', 0, 0),
(5, 15, 1, 1, '2022-12-13', '11:35', 'Thêm bánh sinh nhật', 1, 0),
(7, 15, 6, 2, '2022-12-08', '22:20', 'Cho mình một bàn gần cửa sổ view biển nhé', 1, 0),
(11, 15, 3, 2, '2022-12-14', '20:00', '', 0, 1),
(12, 16, 3, 2, '2022-12-15', '18:50', '', 1, 0),
(13, 19, 1, 4, '2023-01-31', '10:30', '', 0, 1),
(14, 20, 2, 1, '2023-01-02', '09:30', '', 1, 1),
(15, 22, 9, 2, '2023-02-01', '10:10', '', 0, 1),
(16, 15, 3, 2, '2023-02-02', '18:50', '', 1, 1);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `hoadon`
--

CREATE TABLE `hoadon` (
  `idHoaDon` int(11) NOT NULL,
  `idDatBan` int(11) NOT NULL,
  `ngayHoaDon` date NOT NULL,
  `gioHoaDon` varchar(20) NOT NULL,
  `tongTien` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `hoadon`
--

INSERT INTO `hoadon` (`idHoaDon`, `idDatBan`, `ngayHoaDon`, `gioHoaDon`, `tongTien`) VALUES
(5, 7, '2022-12-12', '20:50', 3020000),
(6, 12, '2022-12-15', '16:00', 1455000);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `khachhang`
--

CREATE TABLE `khachhang` (
  `idKhachHang` int(11) NOT NULL,
  `idUser` int(11) NOT NULL,
  `soLanDB` int(11) NOT NULL,
  `giamGia` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_vietnamese_ci;

--
-- Đang đổ dữ liệu cho bảng `khachhang`
--

INSERT INTO `khachhang` (`idKhachHang`, `idUser`, `soLanDB`, `giamGia`) VALUES
(5, 15, 5, 0),
(6, 16, 2, 0),
(8, 19, 0, 0),
(9, 20, 1, 0),
(10, 22, 0, 0);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `monan`
--

CREATE TABLE `monan` (
  `idMonAn` int(11) NOT NULL,
  `tenMonAn` text NOT NULL,
  `loaiMonAn` varchar(30) NOT NULL,
  `giaTien` int(11) NOT NULL,
  `soLuongMonAn` int(11) NOT NULL,
  `img` text NOT NULL,
  `isActive` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_vietnamese_ci;

--
-- Đang đổ dữ liệu cho bảng `monan`
--

INSERT INTO `monan` (`idMonAn`, `tenMonAn`, `loaiMonAn`, `giaTien`, `soLuongMonAn`, `img`, `isActive`) VALUES
(1, 'Bún đậu mắm tôm ngon vờ cờ lờ', 'Món chính', 50000, 10, 'https://kenh14cdn.com/2016/10-ab266-1473685069919.jpg', 0),
(2, 'Cua hoàng đế Alaska nguyên con nhưng chỉ ăn được chân', 'Món chính', 1250000, 2, 'https://product.hstatic.net/1000182631/product/haisant6_01821_6868edc95b2d46d3959d6bd494e2b67e.jpg', 0),
(3, 'Gỏi tôm', 'Khai vị', 30000, 1, 'https://cdn.tgdd.vn/Files/2022/04/04/1423782/goi-y-8-mon-nguoi-khai-vi-cho-nhung-buoi-tiec-hoi-hop-voi-gia-dinh-202204040912057499.jpg', 0),
(5, 'Bánh flan xoài', 'Tráng miệng', 25000, 15, 'https://blog.beemart.vn/wp-content/uploads/2015/05/ca-nha-ngon-ngat-ngay-voi-2-mon-trang-mieng-don-gian-3.png', 0),
(6, 'Bánh Panna cotta dâu', 'Tráng miệng', 45000, 3, 'https://danviet.mediacdn.vn/upload/1-2016/images/2016-01-19/1453188592-dau-10.jpg', 0),
(7, 'Salad cá hồi', 'Khai vị', 50000, 2, 'https://afamilycdn.com/zoom/700_438/150157425591193600/2021/4/20/base64-1618910967516899249294-37-0-623-1119-crop-1618910997875914285934.png', 0),
(9, 'Ghẹ rang muối kiểu Âu', 'Món chính', 350000, 1, 'https://cdn.netspace.edu.vn/images/2021/07/18/cach-lam-ghe-rang-muoi-252654-800.jpg', 0),
(10, 'Bò sốt cay Malaysia', 'Món chính', 190000, 0, 'https://monngon3mien.files.wordpress.com/2012/11/cari-2-copy.jpg?w=640', 0),
(11, 'Bánh tiramisu', 'Tráng miệng', 50000, 0, 'https://cdn.tgdd.vn/Files/2017/03/21/963390/cach-lam-banh-tiramisu-bang-banh-quy-5_730x411.jpg', 0),
(12, 'Gan ngỗng Pháp', 'Món chính', 350000, 1, 'https://vinmec-prod.s3.amazonaws.com/images/20201209_040248_418477_gan-ngong-dac-san-t.max-1800x1800.jpg', 0),
(13, 'Bún đậu mắm tôm ', 'Món chính', 50000, 0, 'https://kenh14cdn.com/2016/10-ab266-1473685069919.jpg', 0),
(14, 'Gỏi tôm', 'Khai vị', 30000, 7, 'https://cdn.tgdd.vn/Files/2022/04/04/1423782/goi-y-8-mon-nguoi-khai-vi-cho-nhung-buoi-tiec-hoi-hop-voi-gia-dinh-202204040912057499.jpg', 1),
(15, 'Salad cá hồi', 'Khai vị', 50000, 0, 'https://afamilycdn.com/zoom/700_438/150157425591193600/2021/4/20/base64-1618910967516899249294-37-0-623-1119-crop-1618910997875914285934.png', 1),
(16, 'Cua hoàng đế Alaska nguyên con nhưng chỉ ăn được chân', 'Món chính', 1250000, 4, 'https://product.hstatic.net/1000182631/product/haisant6_01821_6868edc95b2d46d3959d6bd494e2b67e.jpg', 1),
(17, 'Ghẹ rang muối kiểu Âu', 'Món chính', 350000, 0, 'https://cdn.netspace.edu.vn/images/2021/07/18/cach-lam-ghe-rang-muoi-252654-800.jpg', 1),
(18, 'Bún đậu mắm tôm ', 'Món chính', 50000, 4, 'https://kenh14cdn.com/2016/10-ab266-1473685069919.jpg', 1),
(19, 'Gan ngỗng Pháp', 'Món chính', 350000, 0, 'https://vinmec-prod.s3.amazonaws.com/images/20201209_040248_418477_gan-ngong-dac-san-t.max-1800x1800.jpg', 1),
(20, 'Bò sốt cay Malaysia', 'Món chính', 190000, 0, 'https://monngon3mien.files.wordpress.com/2012/11/cari-2-copy.jpg?w=640', 1),
(21, 'Bánh Panna cotta dâu', 'Tráng miệng', 45000, 4, 'https://danviet.mediacdn.vn/upload/1-2016/images/2016-01-19/1453188592-dau-10.jpg', 1),
(22, 'Bánh tiramisu', 'Tráng miệng', 50000, 0, 'https://cdn.tgdd.vn/Files/2017/03/21/963390/cach-lam-banh-tiramisu-bang-banh-quy-5_730x411.jpg', 1);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `nhanvien`
--

CREATE TABLE `nhanvien` (
  `idNhanVien` int(11) NOT NULL,
  `idUser` int(11) NOT NULL,
  `ngayBDLamViec` date NOT NULL,
  `gioBDLamViec` varchar(10) NOT NULL,
  `gioKTLamViec` varchar(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_vietnamese_ci;

--
-- Đang đổ dữ liệu cho bảng `nhanvien`
--

INSERT INTO `nhanvien` (`idNhanVien`, `idUser`, `ngayBDLamViec`, `gioBDLamViec`, `gioKTLamViec`) VALUES
(1, 9, '2022-12-05', '08:30', '14:30'),
(2, 10, '2022-12-05', '17:30', '23:30'),
(4, 18, '2022-01-31', '08:30', '14:50'),
(5, 21, '2023-02-01', '08:30', '14:30');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `quanly`
--

CREATE TABLE `quanly` (
  `idQuanLy` int(11) NOT NULL,
  `idUser` int(11) NOT NULL,
  `gioBDSang` varchar(10) NOT NULL,
  `gioKTSang` varchar(10) NOT NULL,
  `gioBDToi` varchar(10) NOT NULL,
  `gioKTToi` varchar(10) NOT NULL,
  `doanhThu` bigint(20) NOT NULL,
  `monAnDuocYeuThich` text CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_vietnamese_ci;

--
-- Đang đổ dữ liệu cho bảng `quanly`
--

INSERT INTO `quanly` (`idQuanLy`, `idUser`, `gioBDSang`, `gioKTSang`, `gioBDToi`, `gioKTToi`, `doanhThu`, `monAnDuocYeuThich`) VALUES
(1, 1, '09:00', '14:00', '18:00', '23:00', 4475000, 'Gỏi tôm');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `user`
--

CREATE TABLE `user` (
  `idUser` int(11) NOT NULL,
  `hoTen` varchar(50) NOT NULL,
  `ngaySinh` date NOT NULL,
  `sdt` varchar(10) NOT NULL,
  `email` varchar(50) NOT NULL,
  `chucVu` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_vietnamese_ci;

--
-- Đang đổ dữ liệu cho bảng `user`
--

INSERT INTO `user` (`idUser`, `hoTen`, `ngaySinh`, `sdt`, `email`, `chucVu`) VALUES
(1, 'Nguyễn Thành Luân', '2002-04-22', '0349904429', 'ntluangoc@gmail.com', 'Quản lý'),
(9, 'Dương Văn Tuyến', '2022-12-01', '0987654321', 'tuyengu@gmail.com', 'Nhân viên'),
(10, 'Tạ Minh Tú', '2022-04-01', '0123456788', 'tungu@gmail.com', 'Nhân viên'),
(15, 'Trần Công Đoàn nghiện', '2022-12-12', '0123456789', 'tcdoan@gmail.com', 'Khách hàng'),
(16, 'Phạm Xuân Thịnh', '2022-12-15', '0123456777', 'thinhnd123@gmail.com', 'Khách hàng'),
(18, 'Lionel Messi', '1987-04-26', '0988888888', 'silun@gmail.com', 'Nhân viên'),
(19, 'Cris Ronaldo', '2002-04-25', '0989999999', 'cr7@gmail.com', 'Khách hàng'),
(20, 'Vũ Duy Việt', '2002-05-10', '1900629268', 'vietvu@gmail.com', 'Khách hàng'),
(21, 'Zata', '2002-04-11', '0222222222', 'zata@gmail.com', 'Nhân viên'),
(22, 'Batman', '2002-04-12', '0333333333', 'bat@gmail.com', 'Khách hàng');

--
-- Chỉ mục cho các bảng đã đổ
--

--
-- Chỉ mục cho bảng `account`
--
ALTER TABLE `account`
  ADD PRIMARY KEY (`idAccount`),
  ADD UNIQUE KEY `username` (`username`),
  ADD KEY `fk_user` (`idUser`);

--
-- Chỉ mục cho bảng `banan`
--
ALTER TABLE `banan`
  ADD PRIMARY KEY (`idBanAn`);

--
-- Chỉ mục cho bảng `chonmon`
--
ALTER TABLE `chonmon`
  ADD KEY `fk_chonmon_datban` (`idDatBan`),
  ADD KEY `fk_chonmon_monan` (`idMonAn`);

--
-- Chỉ mục cho bảng `datban`
--
ALTER TABLE `datban`
  ADD PRIMARY KEY (`idDatBan`),
  ADD KEY `fk_datban_user` (`idUser`),
  ADD KEY `fk_datban_banan` (`idBanAn`);

--
-- Chỉ mục cho bảng `hoadon`
--
ALTER TABLE `hoadon`
  ADD PRIMARY KEY (`idHoaDon`),
  ADD KEY `fk_hoadon_datban` (`idDatBan`);

--
-- Chỉ mục cho bảng `khachhang`
--
ALTER TABLE `khachhang`
  ADD PRIMARY KEY (`idKhachHang`),
  ADD KEY `fk_khachhang` (`idUser`);

--
-- Chỉ mục cho bảng `monan`
--
ALTER TABLE `monan`
  ADD PRIMARY KEY (`idMonAn`);

--
-- Chỉ mục cho bảng `nhanvien`
--
ALTER TABLE `nhanvien`
  ADD PRIMARY KEY (`idNhanVien`),
  ADD KEY `fk_nhanvien` (`idUser`);

--
-- Chỉ mục cho bảng `quanly`
--
ALTER TABLE `quanly`
  ADD PRIMARY KEY (`idQuanLy`),
  ADD KEY `fk_quanly` (`idUser`);

--
-- Chỉ mục cho bảng `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`idUser`),
  ADD UNIQUE KEY `sdt` (`sdt`);

--
-- AUTO_INCREMENT cho các bảng đã đổ
--

--
-- AUTO_INCREMENT cho bảng `account`
--
ALTER TABLE `account`
  MODIFY `idAccount` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=23;

--
-- AUTO_INCREMENT cho bảng `banan`
--
ALTER TABLE `banan`
  MODIFY `idBanAn` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=18;

--
-- AUTO_INCREMENT cho bảng `datban`
--
ALTER TABLE `datban`
  MODIFY `idDatBan` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=17;

--
-- AUTO_INCREMENT cho bảng `hoadon`
--
ALTER TABLE `hoadon`
  MODIFY `idHoaDon` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT cho bảng `khachhang`
--
ALTER TABLE `khachhang`
  MODIFY `idKhachHang` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT cho bảng `monan`
--
ALTER TABLE `monan`
  MODIFY `idMonAn` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=23;

--
-- AUTO_INCREMENT cho bảng `nhanvien`
--
ALTER TABLE `nhanvien`
  MODIFY `idNhanVien` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT cho bảng `quanly`
--
ALTER TABLE `quanly`
  MODIFY `idQuanLy` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT cho bảng `user`
--
ALTER TABLE `user`
  MODIFY `idUser` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=23;

--
-- Các ràng buộc cho các bảng đã đổ
--

--
-- Các ràng buộc cho bảng `account`
--
ALTER TABLE `account`
  ADD CONSTRAINT `fk_user` FOREIGN KEY (`idUser`) REFERENCES `user` (`idUser`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Các ràng buộc cho bảng `chonmon`
--
ALTER TABLE `chonmon`
  ADD CONSTRAINT `fk_chonmon_datban` FOREIGN KEY (`idDatBan`) REFERENCES `datban` (`idDatBan`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_chonmon_monan` FOREIGN KEY (`idMonAn`) REFERENCES `monan` (`idMonAn`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Các ràng buộc cho bảng `datban`
--
ALTER TABLE `datban`
  ADD CONSTRAINT `fk_datban_banan` FOREIGN KEY (`idBanAn`) REFERENCES `banan` (`idBanAn`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_datban_user` FOREIGN KEY (`idUser`) REFERENCES `user` (`idUser`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Các ràng buộc cho bảng `hoadon`
--
ALTER TABLE `hoadon`
  ADD CONSTRAINT `fk_hoadon_datban` FOREIGN KEY (`idDatBan`) REFERENCES `datban` (`idDatBan`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Các ràng buộc cho bảng `khachhang`
--
ALTER TABLE `khachhang`
  ADD CONSTRAINT `fk_khachhang` FOREIGN KEY (`idUser`) REFERENCES `user` (`idUser`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Các ràng buộc cho bảng `nhanvien`
--
ALTER TABLE `nhanvien`
  ADD CONSTRAINT `fk_nhanvien` FOREIGN KEY (`idUser`) REFERENCES `user` (`idUser`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Các ràng buộc cho bảng `quanly`
--
ALTER TABLE `quanly`
  ADD CONSTRAINT `fk_quanly` FOREIGN KEY (`idUser`) REFERENCES `user` (`idUser`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
