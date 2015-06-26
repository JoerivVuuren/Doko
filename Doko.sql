-- phpMyAdmin SQL Dump
-- version 4.0.9
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Jun 26, 2015 at 07:28 AM
-- Server version: 5.5.34
-- PHP Version: 5.5.25

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `kite_MoneyMoneyMoney`
--
CREATE DATABASE IF NOT EXISTS `kite_MoneyMoneyMoney` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `kite_MoneyMoneyMoney`;

-- --------------------------------------------------------

--
-- Table structure for table `credit_requests`
--

CREATE TABLE IF NOT EXISTS `credit_requests` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `sender` text NOT NULL,
  `receiver` text NOT NULL,
  `amount` decimal(10,2) NOT NULL,
  `group_id` int(11) NOT NULL,
  `origin` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=46 ;

-- --------------------------------------------------------

--
-- Table structure for table `debit_requests`
--

CREATE TABLE IF NOT EXISTS `debit_requests` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `sender` text NOT NULL,
  `receiver` text NOT NULL,
  `amount` decimal(10,2) NOT NULL,
  `group_id` int(11) NOT NULL,
  `origin` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=62 ;

-- --------------------------------------------------------

--
-- Table structure for table `debts`
--

CREATE TABLE IF NOT EXISTS `debts` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `group_id` int(11) NOT NULL,
  `creditor` varchar(30) NOT NULL,
  `debitor` varchar(30) NOT NULL,
  `debt` decimal(10,2) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=23 ;

-- --------------------------------------------------------

--
-- Table structure for table `debt_history`
--

CREATE TABLE IF NOT EXISTS `debt_history` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `group_id` int(11) NOT NULL,
  `creditor` varchar(30) NOT NULL,
  `debitor` varchar(30) NOT NULL,
  `debt` decimal(10,2) NOT NULL,
  `datetime` datetime NOT NULL,
  `origin` varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=74 ;

-- --------------------------------------------------------

--
-- Table structure for table `friends`
--

CREATE TABLE IF NOT EXISTS `friends` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `friend1` varchar(30) NOT NULL,
  `friend2` varchar(30) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `id` (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 COMMENT='http://stackoverflow.com/questions/4674005/setting-up-a-friend-list-in-mysql' AUTO_INCREMENT=40 ;

-- --------------------------------------------------------

--
-- Table structure for table `friend_requests`
--

CREATE TABLE IF NOT EXISTS `friend_requests` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `sender` varchar(30) NOT NULL,
  `receiver` varchar(30) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=54 ;

-- --------------------------------------------------------

--
-- Table structure for table `game_requests`
--

CREATE TABLE IF NOT EXISTS `game_requests` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `sender` text NOT NULL,
  `receiver` text NOT NULL,
  `amount` decimal(10,2) NOT NULL,
  `group_id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=87 ;

-- --------------------------------------------------------

--
-- Table structure for table `groups`
--

CREATE TABLE IF NOT EXISTS `groups` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `admin_name` varchar(30) NOT NULL,
  `name` varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=72 ;

-- --------------------------------------------------------

--
-- Table structure for table `groups_users`
--

CREATE TABLE IF NOT EXISTS `groups_users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `group_id` int(11) NOT NULL,
  `username` varchar(30) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=123 ;

-- --------------------------------------------------------

--
-- Table structure for table `group_requests`
--

CREATE TABLE IF NOT EXISTS `group_requests` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `group_id` int(11) NOT NULL,
  `receiver` varchar(30) NOT NULL,
  `group_name` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=53 ;

-- --------------------------------------------------------

--
-- Table structure for table `state`
--

CREATE TABLE IF NOT EXISTS `state` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `game` int(10) NOT NULL,
  `b` int(10) NOT NULL,
  `user` int(10) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `tictacstate`
--

CREATE TABLE IF NOT EXISTS `tictacstate` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `veld0` int(11) NOT NULL DEFAULT '0',
  `veld1` int(11) NOT NULL DEFAULT '0',
  `veld2` int(11) NOT NULL DEFAULT '0',
  `veld3` int(11) NOT NULL DEFAULT '0',
  `veld4` int(11) NOT NULL DEFAULT '0',
  `veld5` int(11) NOT NULL DEFAULT '0',
  `veld6` int(11) NOT NULL DEFAULT '0',
  `veld7` int(11) NOT NULL DEFAULT '0',
  `veld8` int(11) NOT NULL DEFAULT '0',
  `player1` varchar(30) NOT NULL,
  `player2` varchar(30) NOT NULL,
  `amount` decimal(10,2) NOT NULL,
  `turn` varchar(30) NOT NULL,
  `group_id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=43 ;

-- --------------------------------------------------------

--
-- Table structure for table `tictactoe`
--

CREATE TABLE IF NOT EXISTS `tictactoe` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `o` int(11) NOT NULL,
  `x` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE IF NOT EXISTS `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` text NOT NULL,
  `password` text NOT NULL,
  `profile_picture` varchar(1024) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_2` (`id`),
  KEY `id` (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=46 ;

-- --------------------------------------------------------

--
-- Table structure for table `wall`
--

CREATE TABLE IF NOT EXISTS `wall` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `group_id` int(11) NOT NULL,
  `player1` varchar(30) NOT NULL,
  `player2` varchar(30) NOT NULL,
  `amount` decimal(10,2) NOT NULL,
  `datetime` datetime NOT NULL,
  `type` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=39 ;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
