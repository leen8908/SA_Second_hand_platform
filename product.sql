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
-- Table structure for table `product`
--

CREATE TABLE `product` (
  `id` int(11) NOT NULL,
  `name` varchar(30) NOT NULL,
  `category` varchar(30) NOT NULL,
  `describtion` text NOT NULL,
  `token` double NOT NULL,
  `area` text NOT NULL,
  `expectedProductOrNot` tinyint(1) NOT NULL,
  `add_time` date NOT NULL,
  `corr_time` date DEFAULT NULL,
  `del_time` date DEFAULT NULL,
  `member_id` int(11) DEFAULT NULL,
  `status` tinyint(1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `product`
--

INSERT INTO `product` (`id`, `name`, `category`, `describtion`, `token`, `area`, `expectedProductOrNot`, `add_time`, `corr_time`, `del_time`, `member_id`, `status`) VALUES
(1, '大猩猩', '交通工具', '我想要騎大猩猩上課', 10, '大概在喜馬拉雅山', 1, '2021-01-02', NULL, NULL, 11, 0),
(2, '水蜜桃', '生活用品', '不用太大,跟籃球一樣大就好', 10, '拉拉山的山洞', 1, '2021-01-02', NULL, NULL, 1, 1),
(6, '粉底刷', '美妝用品', '不用太小,跟哈利波特的掃把一樣小就好', 100, '妙麗的奇幻世界', 1, '2021-01-03', NULL, NULL, 1, 1),
(7, '野狼12345', '交通工具', '我不是野狼125', 12121, '薩哈拉沙漠', 0, '2021-01-03', NULL, NULL, 11, 0),
(9, '山豬', '交通工具', '體重200斤\n黑色的\n要有獠牙', 10, '高雄市', 1, '2021-01-05', NULL, NULL, 1, 0),
(10, '發霉的粉餅', '美妝用品', '很多黴', 10, '太平洋', 1, '2021-01-05', NULL, NULL, 1, 0),
(12, 'applepen', '其他', '我的是applepen不是applepencil', 100, '太平洋', 0, '2021-01-05', NULL, NULL, 11, 0),
(13, '台灣黑熊的坐騎', '交通工具', '你來交換就知道了', 123, '我家', 0, '2021-01-05', NULL, '2021-01-06', 1, 1),
(14, '馬桶刷', '生活用品', '很NEW', 3, '我鄰居家', 0, '2021-01-05', NULL, NULL, 11, 0),
(18, '大猩猩', '交通工具', '我要騎去上課', 100, '喜馬拉雅山', 0, '2021-01-06', NULL, '2021-01-06', 1, 1),
(19, '大猩猩', '生活用品', 'NICE', 123, '美國', 0, '2021-01-06', '2021-01-06', '2021-01-06', 12, 1),
(20, '大猩猩', '交通工具', '我想要騎去上課', 10, '合歡山', 1, '2021-01-06', '2021-01-06', '2021-01-06', 12, 1),
(21, 'qqㄋㄟㄋㄟ好喝到咩噗茶', '生活用品', '好好喝 不要加糖 (❁´◡`❁)', 20, '東區東區 ', 0, '2021-01-06', NULL, '2021-01-06', 12, 1),
(22, '空氣清淨機', '生活用品', '最好是附帶ps5功能ㄉ', 10000, '日本', 0, '2021-01-06', NULL, NULL, 12, 0);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `product`
--
ALTER TABLE `product`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `product`
--
ALTER TABLE `product`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=23;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
