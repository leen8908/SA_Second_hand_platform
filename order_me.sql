-- phpMyAdmin SQL Dump
-- version 5.0.2
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jan 06, 2021 at 06:32 PM
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
-- Table structure for table `order_me`
--

CREATE TABLE `order_me` (
  `id` int(11) NOT NULL,
  `means_of_transaction` varchar(30) NOT NULL,
  `owner_id` int(11) NOT NULL,
  `buyer_id` int(11) NOT NULL,
  `product_id` int(11) NOT NULL,
  `exchangedProduct_id` int(11) DEFAULT NULL,
  `token` double DEFAULT NULL,
  `application_id` int(11) NOT NULL,
  `match_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `order_me`
--

INSERT INTO `order_me` (`id`, `means_of_transaction`, `owner_id`, `buyer_id`, `product_id`, `exchangedProduct_id`, `token`, `application_id`, `match_id`) VALUES
(39, '以物易物', 1, 12, 13, 19, 123, 47, 48),
(40, '代幣', 1, 12, 18, NULL, 100, 49, 50);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `order_me`
--
ALTER TABLE `order_me`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `order_me`
--
ALTER TABLE `order_me`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=41;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
