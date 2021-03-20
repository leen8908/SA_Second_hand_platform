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
-- Table structure for table `report`
--

CREATE TABLE `report` (
  `id` int(11) NOT NULL,
  `report_member_id` int(11) NOT NULL,
  `report_member_name` varchar(30) NOT NULL,
  `product_id` int(11) NOT NULL,
  `product_name` varchar(30) NOT NULL,
  `reported_member_id` int(11) NOT NULL,
  `manager_id` int(11) DEFAULT NULL,
  `status` varchar(250) NOT NULL DEFAULT '等待審核中',
  `content` varchar(250) NOT NULL,
  `image` varchar(250) DEFAULT NULL,
  `start_date` date NOT NULL,
  `finish_date` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `report`
--

INSERT INTO `report` (`id`, `report_member_id`, `report_member_name`, `product_id`, `product_name`, `reported_member_id`, `manager_id`, `status`, `content`, `image`, `start_date`, `finish_date`) VALUES
(10, 11, 'test1', 18, '大猩猩', 1, NULL, '等待審核中', '我要狒狒', NULL, '2021-01-06', NULL),
(11, 13, 'suesue', 22, '空氣清淨機', 12, 1, '審核不通過', '我開心', NULL, '2021-01-06', '2021-01-06'),
(12, 13, 'suesue', 21, 'qqㄋㄟㄋㄟ好喝到咩噗茶', 12, 1, '審核通過', '覺得難喝', NULL, '2021-01-06', '2021-01-06');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `report`
--
ALTER TABLE `report`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `report`
--
ALTER TABLE `report`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
