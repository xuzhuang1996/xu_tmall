
create database if not exists EXAM DEFAULT CHARSET utf8 COLLATE utf8_general_ci;

use EXAM;

DROP TABLE IF EXISTS `exam_questions`;
CREATE TABLE `exam_questions` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(200) NOT NULL,
  `optionA` varchar(20) NOT NULL, 
  `optionB` varchar(20) NOT NULL,
  `optionC` varchar(20) NOT NULL,
  `optionD` varchar(20) NOT NULL,
  `answer`  varchar(1) NOT NULL,
   PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

