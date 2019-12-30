DROP SCHEMA IF EXISTS ModSimProject;
CREATE SCHEMA ModSimProject;
USE ModSimProject;

# ======================================

DROP TABLE IF EXISTS FOOD;
CREATE TABLE FOOD
(
    X_POS  INT,
    Y_POS  INT,
    ENERGY INT NOT NULL DEFAULT 1,
    PRIMARY KEY (X_POS, Y_POS)
);

