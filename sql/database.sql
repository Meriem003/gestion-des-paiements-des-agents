-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: Sep 29, 2025 at 01:09 PM
-- Server version: 8.0.30
-- PHP Version: 8.4.8

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `gestion_paiements`
--

-- --------------------------------------------------------

--
-- Table structure for table `agent`
--

CREATE TABLE `agent` (
  `id` int NOT NULL,
  `nom` varchar(100) NOT NULL,
  `prenom` varchar(100) NOT NULL,
  `email` varchar(150) NOT NULL,
  `mot_de_passe` varchar(255) NOT NULL,
  `type_agent` enum('OUVRIER','RESPONSABLE_DEPARTEMENT','DIRECTEUR','STAGIAIRE') NOT NULL,
  `departement_id` int DEFAULT NULL,
  `est_responsable_departement` tinyint(1) DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `agent`
--

INSERT INTO `agent` (`id`, `nom`, `prenom`, `email`, `mot_de_passe`, `type_agent`, `departement_id`, `est_responsable_departement`) VALUES
(1, 'salhi', 'meryam', 'salhi.meryam@youcode.com', 'admin123', 'DIRECTEUR', 1, 1),
(2, 'TESTEUR', 'Jean', 'jean.testeur@test.com', 'tesjea200', 'RESPONSABLE_DEPARTEMENT', 1, 1),
(3, 'TESTEUR', 'Alice', 'alice.test@entreprise.com', 'test123', 'OUVRIER', 1, 0);

-- --------------------------------------------------------

--
-- Table structure for table `departement`
--

CREATE TABLE `departement` (
  `id` int NOT NULL,
  `nom` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `departement`
--

INSERT INTO `departement` (`id`, `nom`) VALUES
(1, 'Administration'),
(3, 'Marketing'),
(5, 'Informatique'),
(6, 'Ressources Humaines'),
(7, 'Comptabilité'),
(8, 'Logistique');

-- --------------------------------------------------------

--
-- Table structure for table `paiement`
--

CREATE TABLE `paiement` (
  `id` int NOT NULL,
  `type_paiement` enum('SALAIRE','PRIME','BONUS','INDEMNITE') NOT NULL,
  `montant` decimal(10,2) NOT NULL,
  `date_paiement` date NOT NULL,
  `motif` varchar(255) DEFAULT NULL,
  `condition_validee` tinyint(1) DEFAULT '0',
  `agent_id` int NOT NULL
) ;

--
-- Dumping data for table `paiement`
--

INSERT INTO `paiement` (`id`, `type_paiement`, `montant`, `date_paiement`, `motif`, `condition_validee`, `agent_id`) VALUES
(1, 'SALAIRE', '2500.00', '2025-09-29', 'Salaire - SEPTEMBER 2025', 1, 3),
(2, 'PRIME', '500.00', '2025-09-29', 'Prime accordée par le responsable de département', 1, 3),
(3, 'SALAIRE', '3000.00', '2025-09-29', 'Salaire - SEPTEMBER 2025', 1, 3),
(4, 'PRIME', '800.00', '2025-09-29', 'Prime accordée par le responsable de département', 1, 3);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `agent`
--
ALTER TABLE `agent`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `email` (`email`),
  ADD KEY `departement_id` (`departement_id`);

--
-- Indexes for table `departement`
--
ALTER TABLE `departement`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `paiement`
--
ALTER TABLE `paiement`
  ADD PRIMARY KEY (`id`),
  ADD KEY `agent_id` (`agent_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `agent`
--
ALTER TABLE `agent`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- AUTO_INCREMENT for table `departement`
--
ALTER TABLE `departement`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT for table `paiement`
--
ALTER TABLE `paiement`
  MODIFY `id` int NOT NULL AUTO_INCREMENT;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `agent`
--
ALTER TABLE `agent`
  ADD CONSTRAINT `agent_ibfk_1` FOREIGN KEY (`departement_id`) REFERENCES `departement` (`id`) ON DELETE SET NULL;

--
-- Constraints for table `paiement`
--
ALTER TABLE `paiement`
  ADD CONSTRAINT `paiement_ibfk_1` FOREIGN KEY (`agent_id`) REFERENCES `agent` (`id`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
