CREATE DATABASE 'Roombooking';

CREATE TABLE Roombooking.Rooms
(
    Room_ID          int(11) AUTO_INCREMENT,
    Room_name        VARCHAR(255),
    Room_building    VARCHAR(255),
    Room_maxCapacity int(11),
    CONSTRAINT R_Room_ID_PK PRIMARY KEY (Room_ID)
);

CREATE TABLE if not exists Roombooking.`Order`
(
    Order_ID int(11) AUTO_INCREMENT,
    User_ID  int(11),
    Room_ID  int(255),
    timestamp_start datetime,
    timestamp_end datetime,
    CONSTRAINT O_Order_ID_PK PRIMARY KEY (Order_ID),
    CONSTRAINT O_User_ID_FK FOREIGN KEY (User_ID) REFERENCES User (User_ID),
    CONSTRAINT O_Room_ID_FK FOREIGN KEY (Room_ID) REFERENCES Rooms (Room_ID)
);

CREATE TABLE  if not exists  Roombooking.User
(


    User_ID        int auto_increment unique,
    User_firstName varchar(20)  not null,
    User_lastName  varchar(35)  not null,
    User_email     varchar(40)  not null,
    User_dob       varchar(40)  not null,
    User_password  varchar(255) null,
    User_salt      varchar(100) null,
    constraint User_UserID_PK PRIMARY KEY (User_ID),
    constraint User_User_email_uindex
        unique (User_email)

);

DELETE from Rooms;