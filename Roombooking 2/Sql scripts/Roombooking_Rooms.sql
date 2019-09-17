Create DATABASE IF NOT EXISTS Roombooking;
USE Roombooking;

CREATE TABLE IF NOT EXISTS Rooms
(
  roomID    VARCHAR(255) UNIQUE,
  roomFloor VARCHAR(255) NULL,
  CONSTRAINT R_roomID_PK PRIMARY KEY (roomID)
);

INSERT INTO Roombooking.Rooms (roomID, roomFloor) VALUES ('E102', 'Floor 1');
INSERT INTO Roombooking.Rooms (roomID, roomFloor) VALUES ('E202', 'Floor 2');
INSERT INTO Roombooking.Rooms (roomID, roomFloor) VALUES ('E303', 'Floor 3');
INSERT INTO Roombooking.Rooms (roomID, roomFloor) VALUES ('E4046', 'E4');
INSERT INTO Roombooking.Rooms (roomID, roomFloor) VALUES ('E5045', 'E5');
INSERT INTO Roombooking.Rooms (roomID, roomFloor) VALUES ('E808', 'Rom 80');
INSERT INTO Roombooking.Rooms (roomID, roomFloor) VALUES ('E320', 'Rom 50');
INSERT INTO Roombooking.Rooms (roomID, roomFloor) VALUES ('E3203', 'Østsia');

CREATE TABLE IF NOT EXISTS User
(
  User_ID        INT AUTO_INCREMENT
    PRIMARY KEY,
  User_firstName VARCHAR(20)  NOT NULL,
  User_lastName  VARCHAR(35)  NOT NULL,
  User_email     VARCHAR(40)  NOT NULL,
  User_dob       VARCHAR(40)  NOT NULL,
  User_password  VARCHAR(255) NULL,
  User_salt      VARCHAR(100) NULL
);