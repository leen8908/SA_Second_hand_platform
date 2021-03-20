-- phpMyAdmin SQL Dump
-- version 5.0.2
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jan 06, 2021 at 06:34 PM
-- Server version: 10.4.14-MariaDB
-- PHP Version: 7.4.10

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `missa`
--

-- --------------------------------------------------------

--
-- Table structure for table `wishpool_status`
--

CREATE TABLE `wishpool_status` (
  `id` int(11) NOT NULL,
  `start_time` datetime NOT NULL,
  `finish_time` datetime DEFAULT NULL,
  `status` varchar(30) NOT NULL,
  `wishOrNot` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `wishpool_status`
--

INSERT INTO `wishpool_status` (`id`, `start_time`, `finish_time`, `status`, `wishOrNot`) VALUES
(35, '2021-01-06 16:20:15', '2021-01-06 16:21:24', '取物成功，賣家新增代幣10枚', 1),
(36, '2021-01-06 16:41:47', '2021-01-06 16:48:51', '取物成功，賣家新增代幣10枚', 1);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `wishpool_status`
--
ALTER TABLE `wishpool_status`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `wishpool_status`
--
ALTER TABLE `wishpool_status`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=37;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
