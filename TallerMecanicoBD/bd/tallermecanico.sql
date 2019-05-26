/*
 Navicat Premium Data Transfer

 Source Server         : MySQL
 Source Server Type    : MySQL
 Source Server Version : 50725
 Source Host           : localhost:3306
 Source Schema         : tallermecanico

 Target Server Type    : MySQL
 Target Server Version : 50725
 File Encoding         : 65001

 Date: 26/05/2019 13:26:17
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for Administrador
-- ----------------------------
DROP TABLE IF EXISTS `Administrador`;
CREATE TABLE `Administrador` (
  `id` int(11) NOT NULL,
  `correo` varchar(255) NOT NULL,
  `nombre` varchar(255) NOT NULL,
  `contrasena` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of Administrador
-- ----------------------------
BEGIN;
INSERT INTO `Administrador` VALUES (1, 'daniela@gmail.com', 'Daniela Hernandez', '12345');
INSERT INTO `Administrador` VALUES (2, 'luis@gmail.com', 'Luis Parada', '12345');
INSERT INTO `Administrador` VALUES (3, 'diana@gmail.com', 'Diana Pajonares', '12345');
COMMIT;

-- ----------------------------
-- Table structure for Automovil
-- ----------------------------
DROP TABLE IF EXISTS `Automovil`;
CREATE TABLE `Automovil` (
  `matricula` varchar(11) NOT NULL,
  `marca` varchar(255) NOT NULL,
  `modelo` int(4) NOT NULL,
  `linea` varchar(255) NOT NULL,
  `color` varchar(255) NOT NULL,
  `idCliente` int(255) NOT NULL,
  PRIMARY KEY (`matricula`) USING BTREE,
  KEY `idCliente` (`idCliente`),
  CONSTRAINT `idCliente` FOREIGN KEY (`idCliente`) REFERENCES `Cliente` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of Automovil
-- ----------------------------
BEGIN;
INSERT INTO `Automovil` VALUES ('1', 'Ford', 2011, 'Escape', 'Blanca', 1);
INSERT INTO `Automovil` VALUES ('2', 'Audi', 2018, 'A3', 'Negro', 2);
INSERT INTO `Automovil` VALUES ('3', 'Mazda', 2011, 'CX7', 'Blanca', 1);
COMMIT;

-- ----------------------------
-- Table structure for Cliente
-- ----------------------------
DROP TABLE IF EXISTS `Cliente`;
CREATE TABLE `Cliente` (
  `id` int(11) NOT NULL,
  `nombre` varchar(255) NOT NULL,
  `telefono` varchar(10) NOT NULL,
  `direccion` varchar(255) NOT NULL,
  `correo` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of Cliente
-- ----------------------------
BEGIN;
INSERT INTO `Cliente` VALUES (1, 'Ruben Hernandez', '2282199050', 'Xalapa, Veracruz', 'ruben@gmail.com');
INSERT INTO `Cliente` VALUES (2, 'Francisco Valenzuela', '2717117751', 'Murillo Vidal #43 Xalapa, Veracruz', 'francisco@gmail.com');
INSERT INTO `Cliente` VALUES (3, 'Daniela Hernandez', '2323232323', 'Cordoba', 'danihv@gmail.com');
COMMIT;

-- ----------------------------
-- Table structure for Reparacion
-- ----------------------------
DROP TABLE IF EXISTS `Reparacion`;
CREATE TABLE `Reparacion` (
  `id` int(11) NOT NULL,
  `tipo` varchar(255) NOT NULL,
  `kilometraje` varchar(255) NOT NULL,
  `fecha` varchar(255) NOT NULL,
  `hora` varchar(255) NOT NULL,
  `descripcionFalla` varchar(255) NOT NULL,
  `descripcionMantenimiento` varchar(255) NOT NULL,
  `idAutomovil` int(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idAutomovil` (`idAutomovil`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

SET FOREIGN_KEY_CHECKS = 1;
