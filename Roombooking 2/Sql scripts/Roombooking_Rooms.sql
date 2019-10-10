CREATE DATABASE if not exists roombooking;
USE roombooking;



CREATE TABLE if not exists roombooking.rooms
(
    Room_ID          int(11) UNIQUE AUTO_INCREMENT,
    Room_name        VARCHAR(255),
    Room_building    VARCHAR(255),
    Room_maxCapacity int(11),
    CONSTRAINT R_Room_ID_PK PRIMARY KEY (Room_ID)
);

CREATE TABLE if not exists roombooking.user
(
    User_ID        int UNIQUE AUTO_INCREMENT,
    User_firstName varchar(20) NOT NULL,
    User_lastName  varchar(35) NOT NULL,
    User_email     varchar(40) UNIQUE,
    User_dob       varchar(40) NOT NULL,
    User_password  varchar(255),
    User_salt      varchar(100),
    CONSTRAINT U_USER_ID_PK primary key (User_ID)
);

CREATE TABLE if not exists roombooking.`order`
(
    Order_ID        int(11) AUTO_INCREMENT,
    User_ID         int(11),
    Room_ID         int(255),
    Timestamp_start DATETIME,
    Timestamp_end   DATETIME,
    CONSTRAINT O_Order_ID_PK PRIMARY KEY (Order_ID),
    CONSTRAINT O_User_ID_FK FOREIGN KEY (User_ID) REFERENCES user (User_ID),
    CONSTRAINT O_Room_ID_FK FOREIGN KEY (Room_ID) REFERENCES rooms (Room_ID)
);
