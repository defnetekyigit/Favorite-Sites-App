-- Defne Tekyiğit 22070006021
create database FavoriteSites;
use FavoriteSites;

create table userinfo(
    UserID int auto_increment,
    username varchar(255) not null unique,
    password varchar(255) not null,
    primary key (UserID)
    
);

create table visits(
    VisitID int auto_increment,
    username varchar(255),
    countryName varchar(255) not null,
    cityName varchar(255) not null,
    yearVisited int not null,
    seasonVisited varchar(255) not null,
    bestFeature varchar(255) not null,
    comment varchar(255) not null,
    rating int not null,
    primary key (VisitID),
    foreign key (username) references userinfo(username)
);

CREATE INDEX idx_username ON userinfo (Username);
CREATE INDEX idx_visitID ON visits (visitID);

create table sharedVisits(
    Username varchar(255),
    VisitID int,
    FriendUsername varchar(255),
    foreign key (Username) references userinfo(Username),
    foreign key (visitID) references visits(visitID),
    foreign key (FriendUsername) references userinfo(Username)
);

-- inserting test data
insert into userinfo (username, password) values ("defne_tekyigit","12345");
insert into userinfo (username, password) values ("lara_ozduman","12345");
insert into visits (username, countryName, cityName, yearVisited, seasonVisited, bestFeature, comment, rating)
values
("defne_tekyigit", "turkey", "izmir", 2012, "spring", "food", "izmir is such a beautiful city", 5),
("defne_tekyigit", "england", "london", 2017, "summer", "sightseeing places", "it was a long two weeks trip but it was amazing", 4),
("defne_tekyigit", "thailand", "bangkok", 2024, "spring", "food", "tried so many new dishes and they were so delicious.", 4),
("defne_tekyigit", "serbia", "belgrade", 2024, "spring", "sightseeing places", "it was a family short trip", 4),
("defne_tekyigit", "turkey", "istanbul", 2023, "winter", "historic places", "istanbul is a big and amazing city", 5),
("defne_tekyigit", "france", "paris", 2022, "fall", "food", "all the pastrie were delicious.", 5),
("lara_ozduman", "japan", "osaka", 2023, "spring", "food", "it was sakura season so everything was so pretty",5),
("lara_ozduman", "usa", "new york", 2022, "winter", "sightseeing places", "the trip was so fun with my friends",4);

insert into sharedVisits(username, visitID, friendUsername) values
("lara_ozduman",7,"defne_tekyigit"),
("lara_ozduman",8,"defne_tekyigit");



