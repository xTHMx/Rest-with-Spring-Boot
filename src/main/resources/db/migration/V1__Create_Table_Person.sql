-- --------------------------------------------------------
-- Servidor:                     127.0.0.1
-- Versão do servidor:           8.0.42 - MySQL Community Server - GPL
-- OS do Servidor:               Win64
-- HeidiSQL Versão:              12.10.0.7000
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- Copiando estrutura do banco de dados para projeto_spring
CREATE DATABASE IF NOT EXISTS `projeto_spring` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `projeto_spring`;

-- Copiando estrutura para tabela projeto_spring.person
CREATE TABLE IF NOT EXISTS `person` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `first_name` varchar(80) NOT NULL,
  `last_name` varchar(80) NOT NULL,
  `address` varchar(100) NOT NULL,
  `gender` varchar(10) NOT NULL,

  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Copiando dados para a tabela projeto_spring.person: ~5 rows (aproximadamente)
INSERT INTO `person` (`id`, `address`, `first_name`, `gender`, `last_name`) VALUES
	(1, 'Rua Benedito, 14', 'John', 'Masculino', 'Doe'),
	(2, 'Rua Benedito, 14', 'John', 'Macho', 'Doe'),
	(4, 'Alexandria, bairro nobre, 223', 'Albert', 'Macho', 'Eintein'),
	(5, 'Alexandria, bairro nobre, 223', 'Albert', 'Macho', 'Eintein'),
	(6, 'Alexandria, bairro nobre, 223', 'Albert', 'Macho', 'Eintein'),
	(7, 'Catete, Rio de Janeiro', 'Chico', 'Masculino', 'Buarque');
