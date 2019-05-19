/*
 Navicat Premium Data Transfer

 Source Server         : local instance 3306
 Source Server Type    : MySQL
 Source Server Version : 50725
 Source Host           : localhost:3306
 Source Schema         : tallermecanico

 Target Server Type    : MySQL
 Target Server Version : 50725
 File Encoding         : 65001

 Date: 18/05/2019 14:32:25
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for administrador
-- ----------------------------
DROP TABLE IF EXISTS `administrador`;
CREATE TABLE `administrador` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `correo` varchar(255) NOT NULL,
  `nombre` varchar(255) NOT NULL,
  `contrasena` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of administrador
-- ----------------------------
BEGIN;
INSERT INTO `administrador` VALUES (1, 'dannyhvalenz@gmail.com', 'Daniela Hernandez', '827ccb0eea8a706c4c34a16891f84e7b');
COMMIT;

-- ----------------------------
-- Table structure for automovil
-- ----------------------------
DROP TABLE IF EXISTS `automovil`;
CREATE TABLE `automovil` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `marca` varchar(255) NOT NULL,
  `modelo` varchar(255) NOT NULL,
  `linea` varchar(255) NOT NULL,
  `color` varchar(255) NOT NULL,
  `idCliente` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `id` (`id`),
  KEY `idCliente` (`idCliente`),
  CONSTRAINT `idCliente` FOREIGN KEY (`idCliente`) REFERENCES `cliente` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for cliente
-- ----------------------------
DROP TABLE IF EXISTS `cliente`;
CREATE TABLE `cliente` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(255) NOT NULL,
  `telefono` varchar(10) NOT NULL,
  `direccion` varchar(255) NOT NULL,
  `correo` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `id` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of cliente
-- ----------------------------
BEGIN;
INSERT INTO `cliente` VALUES (1, 'Daniela', '2717117751', 'Xalapa', 'daniela@gmail.com');
INSERT INTO `cliente` VALUES (2, 'Luis', '1234567890', 'Xalapa', 'luis@gmail.com');
COMMIT;

-- ----------------------------
-- Table structure for reparacion
-- ----------------------------
DROP TABLE IF EXISTS `reparacion`;
CREATE TABLE `reparacion` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `tipo` varchar(255) NOT NULL,
  `kilometraje` varchar(255) NOT NULL,
  `fecha` date NOT NULL,
  `hora` time(6) NOT NULL,
  `descripcionFalla` varchar(255) NOT NULL,
  `descripcionMantenimiento` varchar(255) DEFAULT NULL,
  `idAutomovil` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idAutomovil` (`idAutomovil`),
  CONSTRAINT `idAutomovil` FOREIGN KEY (`idAutomovil`) REFERENCES `automovil` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

SET FOREIGN_KEY_CHECKS = 1;
