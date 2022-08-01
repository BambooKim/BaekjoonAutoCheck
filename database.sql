-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema baekjoon
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema baekjoon
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `baekjoon` DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci ;
USE `baekjoon` ;

-- -----------------------------------------------------
-- Table `baekjoon`.`user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `baekjoon`.`user` (
  `user_id` BIGINT(1) NOT NULL AUTO_INCREMENT,
  `kor_name` VARCHAR(45) CHARACTER SET 'utf8' COLLATE 'utf8_unicode_ci' NOT NULL,
  `boj_id` VARCHAR(64) CHARACTER SET 'utf8' COLLATE 'utf8_unicode_ci' NOT NULL,
  `enter_year` INT NOT NULL,
  `user_tier` INT NULL,
  `username` VARCHAR(45) CHARACTER SET 'utf8' COLLATE 'utf8_unicode_ci' NOT NULL,
  `password` VARCHAR(100) CHARACTER SET 'utf8' COLLATE 'utf8_unicode_ci' NOT NULL,
  `role` VARCHAR(45) CHARACTER SET 'utf8' COLLATE 'utf8_unicode_ci' NULL,
  `user_status` VARCHAR(45) CHARACTER SET 'utf8' COLLATE 'utf8_unicode_ci' NOT NULL,
  `joined_at` DATETIME NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE INDEX `id_UNIQUE` (`user_id` ASC) VISIBLE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_unicode_ci;


-- -----------------------------------------------------
-- Table `baekjoon`.`user_tier_history`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `baekjoon`.`user_tier_history` (
  `tier_history_id` BIGINT(1) NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT(1) NOT NULL,
  `before_tier` INT NOT NULL,
  `after_tier` INT NOT NULL,
  `created_date` DATETIME NULL,
  `last_modified_date` DATETIME NULL,
  PRIMARY KEY (`tier_history_id`),
  UNIQUE INDEX `tier_history_id_UNIQUE` (`tier_history_id` ASC) VISIBLE,
  INDEX `tier_history_user_idx` (`user_id` ASC) VISIBLE,
  CONSTRAINT `tier_history_user`
    FOREIGN KEY (`user_id`)
    REFERENCES `baekjoon`.`user` (`user_id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_unicode_ci;


-- -----------------------------------------------------
-- Table `baekjoon`.`season`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `baekjoon`.`season` (
  `season_id` BIGINT(1) NOT NULL AUTO_INCREMENT,
  `start_at` DATETIME NOT NULL,
  `end_at` DATETIME NOT NULL,
  `name` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`season_id`),
  UNIQUE INDEX `season_id_UNIQUE` (`season_id` ASC) VISIBLE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_unicode_ci;


-- -----------------------------------------------------
-- Table `baekjoon`.`term`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `baekjoon`.`term` (
  `term_id` BIGINT(1) NOT NULL AUTO_INCREMENT,
  `start_at` DATETIME NOT NULL,
  `end_at` DATETIME NOT NULL,
  `season_id` BIGINT(1) NULL,
  `created_date` DATETIME NULL,
  `last_modified_date` DATETIME NULL,
  PRIMARY KEY (`term_id`),
  UNIQUE INDEX `term_id_UNIQUE` (`term_id` ASC) VISIBLE,
  INDEX `term_season_idx` (`season_id` ASC) VISIBLE,
  CONSTRAINT `term_season`
    FOREIGN KEY (`season_id`)
    REFERENCES `baekjoon`.`season` (`season_id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_unicode_ci;


-- -----------------------------------------------------
-- Table `baekjoon`.`checks`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `baekjoon`.`checks` (
  `check_id` BIGINT(1) NOT NULL AUTO_INCREMENT,
  `term_id` BIGINT(1) NOT NULL,
  `user_id` BIGINT(1) NOT NULL,
  `status` VARCHAR(45) NOT NULL,
  `success` TINYINT(1) NOT NULL,
  `reason` VARCHAR(45) NULL,
  `run_at` DATETIME NULL,
  `rank_applied` TINYINT(1) NOT NULL,
  `created_date` DATETIME NULL,
  `last_modified_date` DATETIME NULL,
  PRIMARY KEY (`check_id`),
  UNIQUE INDEX `check_id_UNIQUE` (`check_id` ASC) VISIBLE,
  INDEX `checks_term_idx` (`term_id` ASC) VISIBLE,
  INDEX `checks_user_idx` (`user_id` ASC) VISIBLE,
  CONSTRAINT `checks_term`
    FOREIGN KEY (`term_id`)
    REFERENCES `baekjoon`.`term` (`term_id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE,
  CONSTRAINT `checks_user`
    FOREIGN KEY (`user_id`)
    REFERENCES `baekjoon`.`user` (`user_id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_unicode_ci;


-- -----------------------------------------------------
-- Table `baekjoon`.`check_history`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `baekjoon`.`check_history` (
  `history_id` BIGINT(1) NOT NULL AUTO_INCREMENT,
  `check_id` BIGINT(1) NOT NULL,
  `prob_no` INT NOT NULL,
  `prob_tier` INT NOT NULL,
  `solved_at` DATETIME NOT NULL,
  `created_date` DATETIME NULL,
  `last_modified_date` DATETIME NULL,
  PRIMARY KEY (`history_id`),
  UNIQUE INDEX `history_id_UNIQUE` (`history_id` ASC) VISIBLE,
  INDEX `check_history_check_idx` (`check_id` ASC) VISIBLE,
  CONSTRAINT `check_history_check`
    FOREIGN KEY (`check_id`)
    REFERENCES `baekjoon`.`checks` (`check_id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_unicode_ci;


-- -----------------------------------------------------
-- Table `baekjoon`.`accum_rank`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `baekjoon`.`accum_rank` (
  `accum_rank_id` BIGINT(1) NOT NULL AUTO_INCREMENT,
  `season_id` BIGINT(1) NOT NULL,
  `user_id` BIGINT(1) NOT NULL,
  `score_challenge` INT NOT NULL,
  `score_fail` INT NOT NULL,
  `score_total` INT NOT NULL,
  `created_date` DATETIME NULL,
  `last_modified_date` DATETIME NULL,
  PRIMARY KEY (`accum_rank_id`),
  UNIQUE INDEX `accum_rank_id_UNIQUE` (`accum_rank_id` ASC) VISIBLE,
  INDEX `accum_rank_season_idx` (`season_id` ASC) VISIBLE,
  INDEX `accum_rank_user_idx` (`user_id` ASC) VISIBLE,
  CONSTRAINT `accum_rank_season`
    FOREIGN KEY (`season_id`)
    REFERENCES `baekjoon`.`season` (`season_id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE,
  CONSTRAINT `accum_rank_user`
    FOREIGN KEY (`user_id`)
    REFERENCES `baekjoon`.`user` (`user_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_unicode_ci;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
