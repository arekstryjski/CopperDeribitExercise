-- Users schema

-- !Ups

CREATE TABLE SimpleBalances (
    currency varchar(5) NOT NULL,
    current decimal(10, 8) NOT NULL,
    reserved decimal(10, 8) NOT NULL,
    PRIMARY KEY (currency)
);

-- !Downs

DROP TABLE SimpleBalances;