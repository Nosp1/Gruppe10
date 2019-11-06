DROP schema roombooking;
CREATE DATABASE if not exists roombooking;
USE roombooking;

CREATE TABLE if not exists roombooking.rooms
(
    Room_ID          int(11) UNIQUE AUTO_INCREMENT,
    Room_name        VARCHAR(255),
    Room_building    VARCHAR(255),
    Room_maxCapacity int(11),
    Room_Type        enum('CLASSROOM', 'GROUPROOM', 'AUDITORIUM'),
    Tavle            VARCHAR(3),
    Projektor       VARCHAR(3),
    CONSTRAINT R_Room_ID_PK PRIMARY KEY (Room_ID)
);




create table if not exists user_type(
    User_type_ID int auto_increment,
    User_type_Name VARCHAR(10),
    CONSTRAINT UT_PK_User_type_ID PRIMARY KEY (User_type_ID)
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
    User_type_ID   int,
    CONSTRAINT U_USER_ID_PK PRIMARY KEY (User_ID),
    CONSTRAINT U_User_type_ID_FK FOREIGN KEY (User_type_ID) REFERENCES user_type (User_type_ID)
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
create table if not exists email
(
    Email_name     varchar(55)  null,
    Email_Password varchar(255) null,
    Email_Salt     varchar(255) null,
    constraint Email_Email_name_uindex
        unique (Email_name)
);

CREATE TABLE if not exists user_report(
    Report_ID int auto_increment,
    Report_Response char (30),
    User_ID int,
    Room_ID int,

    CONSTRAINT PK_Report PRIMARY KEY (Report_ID),
    CONSTRAINT FK_ReportUser FOREIGN KEY(User_ID) REFERENCES User (User_ID),
    CONSTRAINT FK_ReportRoom FOREIGN KEY(Room_ID) REFERENCES Rooms (Room_ID)
);

create table if not exists user_type_registry(
    Registry_Number int auto_increment,
    User_ID int,
    User_type_ID int,
    CONSTRAINT UTR_PK_Registry_Number PRIMARY KEY (Registry_Number),
    CONSTRAINT UTR_FK_User_ID FOREIGN KEY (User_ID) REFERENCES `user`(User_ID),
    CONSTRAINT UTR_FK_User_type_ID FOREIGN KEY (User_type_ID) REFERENCES user_type(User_type_ID)
);

INSERT INTO user_type (User_type_Name) VALUES ('STUDENT');
INSERT INTO user_type (User_type_Name) VALUES ('TEACHER');
INSERT INTO user_type (User_type_Name) VALUES ('ADMIN');

