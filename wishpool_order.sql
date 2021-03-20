-- phpMyAdmin SQL Dump
-- version 5.0.2
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jan 06, 2021 at 06:33 PM
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
-- Table structure for table `wishpool_order`
--

CREATE TABLE `wishpool_order` (
  `id` int(11) NOT NULL,
  `token` double NOT NULL,
  `wisher_id` int(11) NOT NULL,
  `seller_id` int(11) NOT NULL,
  `expectedProduct_id` int(11) NOT NULL,
  `help_id` int(11) NOT NULL,
  `wish_id` int(11) DEFAULT NULL,
  `report_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `wishpool_order`
--

INSERT INTO `wishpool_order` (`id`, `token`, `wisher_id`, `seller_id`, `expectedProduct_id`, `help_id`, `wish_id`, `report_id`) VALUES
(34, 10, 1, 12, 2, 35, NULL, NULL),
(35, 100, 1, 13, 6, 36, NULL, NULL);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `wishpool_order`
--
ALTER TABLE `wishpool_order`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `wishpool_order`
--
ALTER TABLE `wishpool_order`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=36;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
