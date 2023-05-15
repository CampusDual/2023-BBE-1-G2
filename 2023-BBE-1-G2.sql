DROP TYPE IF EXISTS STARS_ENUM;
CREATE TYPE STARS_ENUM AS ENUM('ONE','TWO','THREE','FOUR','FIVE');

DROP TABLE IF EXISTS HOTEL;
CREATE TABLE IF NOT EXISTS HOTEL(
	ID SERIAL PRIMARY KEY,
	NAME VARCHAR(100) NOT NULL,
	STARS STARS_ENUM NOT NULL,
	ADDRESS VARCHAR (200) NOT NULL
);

