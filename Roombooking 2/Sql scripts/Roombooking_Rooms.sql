CREATE DATABASE if not exists roombooking;
USE roombooking;



CREATE TABLE if not exists roombooking.rooms
(
    Room_ID          int(11) UNIQUE AUTO_INCREMENT,
    Room_name        VARCHAR(255),
    Room_building    VARCHAR(255),
    Room_maxCapacity int(11),
    Tavle            VARCHAR(3),
    Prosjektor       VARCHAR(3),
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
/*
 might be redundant table.
 */
create table Email
(
    Email_name     varchar(55)  null,
    Email_Password varchar(255) null,
    Email_Salt     varchar(255) null,
    constraint Email_Email_name_uindex
        unique (Email_name)
);

CREATE TABLE UserReport(
    Report_ID int,
    Report_Response char (30),
    User_ID int,
    Room_ID int,

    CONSTRAINT PK_Report PRIMARY KEY (Report_ID),
    CONSTRAINT FK_ReportUser FOREIGN KEY(User_ID) REFERENCES User (User_ID),
    CONSTRAINT FK_ReportRoom FOREIGN KEY(Room_ID) REFERENCES Rooms (Room_ID)
    );

