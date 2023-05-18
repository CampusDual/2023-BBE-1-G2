
DROP TABLE IF EXISTS HOTEL;
CREATE TABLE IF NOT EXISTS HOTEL(
	ID SERIAL PRIMARY KEY,
	NAME VARCHAR(100) NOT NULL,
	STARS INTEGER NOT NULL,
	ADDRESS VARCHAR (200) NOT NULL
);

DROP TABLE IF EXISTS ROOM;
CREATE TABLE IF NOT EXISTS ROOM(
	ID SERIAL PRIMARY KEY,
	NUMBER INTEGER NOT NULL,
	CAPACITY INTEGER NOT NULL,
	DESCRIPTION TEXT,
	HOTEL INTEGER NOT NULL,
	FOREIGN KEY(HOTEL) REFERENCES HOTEL(ID) on delete cascade on update cascade
);
