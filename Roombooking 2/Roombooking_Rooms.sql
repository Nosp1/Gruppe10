Create DATABASE if not exists Roombooking;
use Roombooking;
create table if not exists Rooms
(
    Room_ID          int auto_increment unique,
    Room_name        varchar(255),
    Room_building    varchar(255) not null,
    Room_maxCapacity int          null,
    CONSTRAINT Room_RoomID_PK PRIMARY KEY (Room_ID)
);

create table if not exists User
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

