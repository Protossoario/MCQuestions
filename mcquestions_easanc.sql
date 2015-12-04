# ************************************************************
# Sequel Pro SQL dump
# Version 4499
#
# http://www.sequelpro.com/
# https://github.com/sequelpro/sequelpro
#
# Host: 127.0.0.1 (MySQL 5.6.27)
# Database: mcquestions
# Generation Time: 2015-12-04 16:29:53 +0000
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Dump of table answer
# ------------------------------------------------------------

DROP TABLE IF EXISTS `answer`;

CREATE TABLE `answer` (
  `id_answer` int(11) NOT NULL AUTO_INCREMENT,
  `question_id_question` int(11) NOT NULL,
  `text` varchar(255) NOT NULL,
  `correct` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id_answer`,`question_id_question`),
  KEY `fk_answer_question_idx` (`question_id_question`),
  CONSTRAINT `fk_answer_question` FOREIGN KEY (`question_id_question`) REFERENCES `question` (`id_question`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `answer` WRITE;
/*!40000 ALTER TABLE `answer` DISABLE KEYS */;

INSERT INTO `answer` (`id_answer`, `question_id_question`, `text`, `correct`)
VALUES
	(5,3,'Versailles',0),
	(6,3,'Paris',1),
	(7,3,'Marseille',0),
	(8,3,'Algiers',0),
	(9,4,'X = 1',1),
	(10,4,'X = 2',0),
	(11,4,'X = 3',0),
	(12,4,'X = 4',0),
	(13,4,'None of the above',0),
	(14,1,'1',0),
	(15,1,'2',0),
	(16,1,'3',0),
	(17,1,'4',1),
	(18,1,'5',0),
	(19,5,'Jean-Jacques Rousseau',0),
	(20,5,'John Maynard Keynes',0),
	(21,5,'Adam Smith',1),
	(22,5,'Immanuel Kant',0),
	(23,6,'Both A and B are correct.',0),
	(24,6,'Only A is correct.',0),
	(25,6,'Only B is correct.',1),
	(26,6,'Both A and B are incorrect.',0),
	(27,7,'The dot product of two vectors will be non-zero if they are perpendicular to each other.',1),
	(28,7,'The dot product of two vectors will be zero if they are perpendicular to each other.',0),
	(29,7,'The cross product of two vectors is a vector which is perpendicular to both of them.',0),
	(30,7,'The cross product of two vectors is a plane which is normal to both of them.',1);

/*!40000 ALTER TABLE `answer` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table question
# ------------------------------------------------------------

DROP TABLE IF EXISTS `question`;

CREATE TABLE `question` (
  `id_question` int(11) NOT NULL AUTO_INCREMENT,
  `text` varchar(255) NOT NULL,
  PRIMARY KEY (`id_question`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `question` WRITE;
/*!40000 ALTER TABLE `question` DISABLE KEYS */;

INSERT INTO `question` (`id_question`, `text`)
VALUES
	(1,'2+2=?'),
	(3,'What is the capital of France?'),
	(4,'Solve the following for X: 2x^2 + 4x + 4 = 2'),
	(5,'Name the philosopher who invented the economic concept of <strong>the Invisible Hand</strong>.'),
	(6,'Which statement is correct?<br>A) SQL is a programming language<br>B) Transactions help maintain consistency across multiple operations, at the cost of efficiency.'),
	(7,'Identify which of the following statements are FALSE.');

/*!40000 ALTER TABLE `question` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table question_category
# ------------------------------------------------------------

DROP TABLE IF EXISTS `question_category`;

CREATE TABLE `question_category` (
  `name` varchar(255) NOT NULL,
  `question_id_question` int(11) NOT NULL,
  PRIMARY KEY (`name`,`question_id_question`),
  KEY `fk_question_category_question1_idx` (`question_id_question`),
  CONSTRAINT `fk_question_category_question1` FOREIGN KEY (`question_id_question`) REFERENCES `question` (`id_question`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `question_category` WRITE;
/*!40000 ALTER TABLE `question_category` DISABLE KEYS */;

INSERT INTO `question_category` (`name`, `question_id_question`)
VALUES
	('math',1),
	('Geography',3),
	('algebra',4),
	('equations',4),
	('math',4),
	('capitalism',5),
	('economy',5),
	('history',5),
	('database',6),
	('programming',6),
	('SQL',6),
	('calculus',7),
	('geometry',7),
	('math',7),
	('physics',7),
	('vector',7);

/*!40000 ALTER TABLE `question_category` ENABLE KEYS */;
UNLOCK TABLES;



/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

