CREATE TABLE Roombooking.`Order`
(
    orderID varchar(255) PRIMARY KEY NOT NULL,
    userID int(11),
    roomID varchar(255),
    CONSTRAINT O_userID_FK FOREIGN KEY (userID) REFERENCES User,
    CONSTRAINT O_roomID_FK FOREIGN KEY (roomID) REFERENCES Rooms
);
CREATE INDEX O_userID_FK ON Roombooking.`Order` (userID);
CREATE INDEX O_roomID_FK ON Roombooking.`Order` (roomID);
CREATE TABLE Roombooking.Rooms
(
    roomID varchar(255) PRIMARY KEY NOT NULL,
    roomFloor varchar(255),
    maxCapacity int(11)
);
CREATE UNIQUE INDEX roomID ON Roombooking.Rooms (roomID);
INSERT INTO Roombooking.Rooms (roomID, roomFloor, maxCapacity) VALUES ('E102', 'Floor 1', 10);
INSERT INTO Roombooking.Rooms (roomID, roomFloor, maxCapacity) VALUES ('E202', 'Floor 2', 10);
INSERT INTO Roombooking.Rooms (roomID, roomFloor, maxCapacity) VALUES ('E303', 'Floor 3', 10);
INSERT INTO Roombooking.Rooms (roomID, roomFloor, maxCapacity) VALUES ('E320', 'Rom 50', 10);
INSERT INTO Roombooking.Rooms (roomID, roomFloor, maxCapacity) VALUES ('E3203', 'Østsia', 10);
INSERT INTO Roombooking.Rooms (roomID, roomFloor, maxCapacity) VALUES ('E4046', 'E4', 10);
INSERT INTO Roombooking.Rooms (roomID, roomFloor, maxCapacity) VALUES ('E5045', 'E5', 10);
INSERT INTO Roombooking.Rooms (roomID, roomFloor, maxCapacity) VALUES ('E808', 'Rom 80', 10);
CREATE TABLE Roombooking.User
(
    User_ID int(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
    User_firstName varchar(20) NOT NULL,
    User_lastName varchar(35) NOT NULL,
    User_email varchar(40) NOT NULL,
    User_dob varchar(40) NOT NULL,
    User_password varchar(255),
    User_salt varchar(100)
);
CREATE UNIQUE INDEX User_email ON Roombooking.User (User_email);