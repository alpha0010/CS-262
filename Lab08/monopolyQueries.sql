--
-- This SQL script builds a monopoly database, deleting any pre-existing version.
--
-- @author kvlinden
-- @version Summer, 2015
--

-- Drop previous versions of the tables if they they exist, in reverse order of foreign keys.
DROP TABLE IF EXISTS PlayerGame;
DROP TABLE IF EXISTS Game;
DROP TABLE IF EXISTS Player;

-- Create the schema.
CREATE TABLE Game (
	ID integer PRIMARY KEY, 
	time timestamp
	);

CREATE TABLE Player (
	ID integer PRIMARY KEY, 
	emailAddress varchar(50) NOT NULL,
	name varchar(50)
	);

CREATE TABLE PlayerGame (
	gameID integer REFERENCES Game(ID), 
	playerID integer REFERENCES Player(ID),
	score integer
	);

-- Allow users to select data from the tables.
GRANT SELECT ON Game TO PUBLIC;
GRANT SELECT ON Player TO PUBLIC;
GRANT SELECT ON PlayerGame TO PUBLIC;

-- Add sample records.
INSERT INTO Game VALUES (1, '2006-06-27 08:00:00');
INSERT INTO Game VALUES (2, '2006-06-28 13:20:00');
INSERT INTO Game VALUES (3, '2006-06-29 18:41:00');

INSERT INTO Player(ID, emailAddress) VALUES (1, 'me@calvin.edu');
INSERT INTO Player VALUES (2, 'king@gmail.com', 'The King');
INSERT INTO Player VALUES (3, 'dog@gmail.com', 'Dogbreath');

INSERT INTO PlayerGame VALUES (1, 1, 0.00);
INSERT INTO PlayerGame VALUES (1, 2, 0.00);
INSERT INTO PlayerGame VALUES (1, 3, 2350.00);
INSERT INTO PlayerGame VALUES (2, 1, 1000.00);
INSERT INTO PlayerGame VALUES (2, 2, 0.00);
INSERT INTO PlayerGame VALUES (2, 3, 500.00);
INSERT INTO PlayerGame VALUES (3, 2, 0.00);
INSERT INTO PlayerGame VALUES (3, 3, 5500.00);

-- 8.1
-- a.
--SELECT * FROM Game ORDER BY time DESC;

-- b.
--SELECT * FROM Game WHERE time >= (current_timestamp - interval '7 days');

-- c.
--SELECT * FROM Player WHERE NOT name IS NULL;

-- d.
--SELECT DISTINCT Player.* FROM Player
--    JOIN PlayerGame ON Player.ID = PlayerGame.playerID
--    WHERE PlayerGame.score > 2000;

-- e.
--SELECT * FROM Player WHERE emailAddress LIKE '%@gmail.com';

-- 8.2
-- 1.
--SELECT PlayerGame.score FROM Player
--    JOIN PlayerGame ON Player.ID = PlayerGame.playerID
--    WHERE Player.name = 'The King'
--    ORDER BY PlayerGame.score DESC;

-- 2.
--SELECT Player.name FROM Game
--    JOIN PlayerGame ON Game.ID = PlayerGame.gameID
--    JOIN Player ON PlayerGame.playerID = Player.ID
--    WHERE time = '2006-06-28 13:20:00'
--    ORDER BY score DESC
--    LIMIT 1;

-- 3. This restricts a Player from being detected as a duplicate of itself,
--    and prevents a Player-Player duplicate pair from being selected twice
--    by only counting it when the first of the pair has a lower ID.

-- 4. There is Drupal's node table... but the restriction 'want' probably
--    excludes this.
--    If a 'Person' table has a 'guardian' field, each 'guardian' is also a
--    'Person', so finding a person and their legal guardian can be done in a
--    single query via self join.
