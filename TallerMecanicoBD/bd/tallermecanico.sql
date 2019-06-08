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

 Date: 08/06/2019 12:54:44
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
INSERT INTO `Administrador` VALUES (1, 'daniela@gmail.com', 'Daniela Hernandez', '827ccb0eea8a706c4c34a16891f84e7b');
INSERT INTO `Administrador` VALUES (2, 'luis@gmail.com', 'Luis Parada', '827ccb0eea8a706c4c34a16891f84e7b');
INSERT INTO `Administrador` VALUES (3, 'diana@gmail.com', 'Diana Pajonares', '827ccb0eea8a706c4c34a16891f84e7b');
INSERT INTO `Administrador` VALUES (4, 'prueba@gmail.com', 'Prueba', '827ccb0eea8a706c4c34a16891f84e7b');
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
INSERT INTO `Automovil` VALUES ('TXV-901', 'Ford', 2011, 'Escape', 'Blanca', 1);
INSERT INTO `Automovil` VALUES ('WOW-901', 'Mazda', 2011, 'CX7', 'Blanca', 1);
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
INSERT INTO `Cliente` VALUES (5, 'Luis Parada Cruz', '2282212278', 'Murillo Vidal 145, Xalapa, Veracruz', 'luisparada@gmail.com');
COMMIT;

-- ----------------------------
-- Table structure for Reparacion
-- ----------------------------
DROP TABLE IF EXISTS `Reparacion`;
CREATE TABLE `Reparacion` (
  `id` int(11) NOT NULL,
  `tipo` varchar(255) NOT NULL,
  `kilometraje` varchar(255) NOT NULL,
  `fecha` date NOT NULL,
  `hora` date NOT NULL,
  `descripcionFalla` varchar(255) NOT NULL,
  `descripcionMantenimiento` varchar(255) DEFAULT NULL,
  `idAutomovil` varchar(255) NOT NULL,
  `costo` int(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `matricula` (`idAutomovil`),
  CONSTRAINT `matricula` FOREIGN KEY (`idAutomovil`) REFERENCES `Automovil` (`matricula`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of Reparacion
-- ----------------------------
BEGIN;
INSERT INTO `Reparacion` VALUES (1, 'Mecanico', '123123', '2019-06-03', '0002-12-31', '123213', '12312', 'WOW-901', 1231);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
