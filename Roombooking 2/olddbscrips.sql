create table Rooms
(
    Room_building    varchar(255) not null,
    Room_ID          int auto_increment
        primary key,
    Room_maxCapacity int          null
);


create table User
(
    User_ID        int auto_increment
        primary key,
    User_firstName varchar(20)  not null,
    User_lastName  varchar(35)  not null,
    User_email     varchar(40)  not null,
    User_dob       varchar(40)  not null,
    User_password  varchar(255) null,
    User_salt      varchar(100) null,
    constraint User_User_email_uindex
        unique (User_email)
);

INSERT INTO Roombooking.User (User_ID, User_firstName, User_lastName, User_email, User_dob, User_password, User_salt) VALUES (67, 'Trym', 'Staurheim', 'Trymerlend@hotmail.no', '1993-09-25', '7e60e9f391f9be31c5f0c7e92f1c38b3:d2e0c9c3595f82036bfbc75d6ed656002a3a94c724c47f09dc864ac4b7f5a9891336bbf89b670f796f431a32cb0ec7679bee52fa18453288a8f0508710e40353', '7e60e9f391f9be31c5f0c7e92f1c38b3');
INSERT INTO Roombooking.User (User_ID, User_firstName, User_lastName, User_email, User_dob, User_password, User_salt) VALUES (68, 'Ola', 'Nordmann', 'OlaN@gmail.com', '1993-09-25', '726684da0f342b36d7347c3c29a901c7:ce3146ff9a684f63a473f5493918fc245b7edbefec2478e3d13594f3bcfa8e5d05602d78ebdd237b15c0b09ea1c71a46ec9e23b06a742adad8bc7ad530db877f', '726684da0f342b36d7347c3c29a901c7');
INSERT INTO Roombooking.User (User_ID, User_firstName, User_lastName, User_email, User_dob, User_password, User_salt) VALUES (69, 'Mari', 'Nordmann', 'marinordmann@gmail.com', '1993-01-25', '526f9e4a0e52cfe1641171257daf8945:6f961cc404465db7cb402734de15ff056ef849077a8012ffe9c6afa8bc3c1ae3c75df92b1e7d2435bba8e3b1b64f01c289e2c85f92b2d3e90cb0558665ce3ce5', '526f9e4a0e52cfe1641171257daf8945');
INSERT INTO Roombooking.User (User_ID, User_firstName, User_lastName, User_email, User_dob, User_password, User_salt) VALUES (70, 'Hans', 'land', 'hansland@gmail.com', '1993-09-25', '70d2d1ae6e44edabfef672ec2e9bda2a:45e3596221c2c39d7808f113283d15d8376d9fa2a3fb1ccaa5872603e15c4fccb9098f3462122e805c224d1bdb33483e6326768c3ff8c4b6c5a851f51a382882', '70d2d1ae6e44edabfef672ec2e9bda2a');
INSERT INTO Roombooking.User (User_ID, User_firstName, User_lastName, User_email, User_dob, User_password, User_salt) VALUES (71, 'john', 'smith', 'johnsmith@gmail.com', '1993-09-25', '97dab60396cbfd6bda1d0bb6db63cb2a:0ab77ea9309091bf7f663dbc74eaea8733273cd0780deb03d97ffb1f1cca8b969fafccaf810ebb2a529ed57b8b8d39090fed72e0a436640bf427d0a7e846d08a', '97dab60396cbfd6bda1d0bb6db63cb2a');
INSERT INTO Roombooking.User (User_ID, User_firstName, User_lastName, User_email, User_dob, User_password, User_salt) VALUES (72, 'Hallgeir', 'Nilsen', 'hallgeirNilsen@gmail.com', '1993-09-25', '794c1c71214ca210a2cceec805b764af:5534ecf6d71d497397cc24779f73edb9f48e98013a31e261c9c15a6f47dda4b2272736e3f6d17dd643c49e198cdbe1c9ec4cb6b2393bfb30f835fd13a283b395', '794c1c71214ca210a2cceec805b764af');
INSERT INTO Roombooking.User (User_ID, User_firstName, User_lastName, User_email, User_dob, User_password, User_salt) VALUES (73, 'Lisa', 'Austad', 'lisa.austad@gmail.com', '1992-03-23', '71a98510ef94b7d358163d86fd002854:c30d8c6bbad5e546a26dea69aee1b9781bec9bab27bdbc5a7bd587f4758f8f2128c543c31ffaaaf833c6be0c96fd92a39805ce60d65c616bd1e6aea61dc0a10c', '71a98510ef94b7d358163d86fd002854');