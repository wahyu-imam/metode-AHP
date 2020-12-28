-- phpMyAdmin SQL Dump
-- version 5.0.3
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Dec 27, 2020 at 07:06 PM
-- Server version: 10.4.14-MariaDB
-- PHP Version: 7.4.11

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `ahp`
--

-- --------------------------------------------------------

--
-- Table structure for table `alternatif`
--

CREATE TABLE `alternatif` (
  `kode_alternatif` varchar(10) NOT NULL,
  `nama_alternatif` varchar(100) NOT NULL,
  `n1` int(11) NOT NULL,
  `n2` int(11) NOT NULL,
  `n3` int(11) NOT NULL,
  `n4` int(11) NOT NULL,
  `n5` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `alternatif`
--

INSERT INTO `alternatif` (`kode_alternatif`, `nama_alternatif`, `n1`, `n2`, `n3`, `n4`, `n5`) VALUES
('A01', 'Diana', 70, 75, 70, 65, 75),
('A02', 'Febri', 80, 75, 75, 60, 70),
('A03', 'Selly', 75, 70, 80, 70, 75);

-- --------------------------------------------------------

--
-- Table structure for table `bbt_kriteria`
--

CREATE TABLE `bbt_kriteria` (
  `kode_bbt` varchar(10) NOT NULL,
  `dari` varchar(10) NOT NULL,
  `ke` varchar(10) NOT NULL,
  `bobot` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `bbt_kriteria`
--

INSERT INTO `bbt_kriteria` (`kode_bbt`, `dari`, `ke`, `bobot`) VALUES
('BC01', 'C01', 'C02', 1),
('BC02', 'C01', 'C03', 3),
('BC03', 'C01', 'C04', 1),
('BC04', 'C01', 'C05', 3),
('BC05', 'C02', 'C03', 2),
('BC06', 'C02', 'C04', 1),
('BC07', 'C02', 'C05', 1),
('BC08', 'C03', 'C04', 1),
('BC09', 'C03', 'C05', 2),
('BC10', 'C04', 'C05', 3);

-- --------------------------------------------------------

--
-- Table structure for table `hasil`
--

CREATE TABLE `hasil` (
  `kode_hasil` varchar(10) NOT NULL,
  `hasil` double NOT NULL,
  `kode_alternatif` varchar(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `hasil`
--

INSERT INTO `hasil` (`kode_hasil`, `hasil`, `kode_alternatif`) VALUES
('H01', 0.3271728093116395, 'A01'),
('H02', 0.3441771436873622, 'A02'),
('H03', 0.3286500470009984, 'A03');

-- --------------------------------------------------------

--
-- Table structure for table `kriteria`
--

CREATE TABLE `kriteria` (
  `kode_kriteria` varchar(10) NOT NULL,
  `nama_kriteria` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `kriteria`
--

INSERT INTO `kriteria` (`kode_kriteria`, `nama_kriteria`) VALUES
('C01', 'Penyampaian Materi'),
('C02', 'test'),
('C03', 'Pemahaman Siswa'),
('C04', 'Cara Belajar'),
('C05', 'Kenyamanan');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `alternatif`
--
ALTER TABLE `alternatif`
  ADD PRIMARY KEY (`kode_alternatif`);

--
-- Indexes for table `bbt_kriteria`
--
ALTER TABLE `bbt_kriteria`
  ADD PRIMARY KEY (`kode_bbt`);

--
-- Indexes for table `hasil`
--
ALTER TABLE `hasil`
  ADD PRIMARY KEY (`kode_hasil`);

--
-- Indexes for table `kriteria`
--
ALTER TABLE `kriteria`
  ADD PRIMARY KEY (`kode_kriteria`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
