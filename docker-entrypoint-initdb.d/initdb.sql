USE `mrdb`;

DROP TABLE IF EXISTS `market_rate`;
CREATE TABLE `market_rate` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `market_code` varchar(15) NOT NULL,
  `date_mr` DATETIME NOT NULL,
  `day_mr` DATE NOT NULL,
  `hour_mr` int(2) NOT NULL,
  `open_mr` DECIMAL(18,5) NOT NULL,
  `close_mr` DECIMAL(18,5) NOT NULL,
  `highest` DECIMAL(18,5) NOT NULL,
  `lowest` DECIMAL(18,5) NOT NULL,
  `average` DECIMAL(18,5) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`)
) ENGINE=InnoDB;

CREATE OR REPLACE UNIQUE INDEX `market_rate__marketCode_date_ix` ON market_rate(marketCode, date_mr);
CREATE OR REPLACE UNIQUE INDEX `market_rate__marketCode_day_hour_ix` ON market_rate(marketCode, day_mr, hour_mr);