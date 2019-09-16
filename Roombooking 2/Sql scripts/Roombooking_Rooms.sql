create table Rooms
(
  roomFloor varchar(255) null,
  roomID    varchar(255) null
);

INSERT INTO Roombooking.Rooms (roomFloor, roomID) VALUES ('Floor 1', 'E102');
INSERT INTO Roombooking.Rooms (roomFloor, roomID) VALUES ('Floor 2', 'E202');
INSERT INTO Roombooking.Rooms (roomFloor, roomID) VALUES ('Floor 3', 'E303');
INSERT INTO Roombooking.Rooms (roomFloor, roomID) VALUES ('E4', 'E4046');
INSERT INTO Roombooking.Rooms (roomFloor, roomID) VALUES ('E4', 'E4046');
INSERT INTO Roombooking.Rooms (roomFloor, roomID) VALUES ('E5', 'E5045');
INSERT INTO Roombooking.Rooms (roomFloor, roomID) VALUES ('Rom80', 'E808');
INSERT INTO Roombooking.Rooms (roomFloor, roomID) VALUES ('Rom50', 'E320');
INSERT INTO Roombooking.Rooms (roomFloor, roomID) VALUES ('Ãstsia', 'E3203');
create table User
(
  User_ID        int auto_increment
    primary key,
  User_firstName varchar(20)  not null,
  User_lastName  varchar(35)  not null,
  User_email     varchar(40)  not null,
  User_dob       varchar(40)  not null,
  User_password  varchar(255) null,
  User_salt      varchar(100) null
);

