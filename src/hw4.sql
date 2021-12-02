CREATE DATABASE real_estate;

USE real_estate;

CREATE TABLE client (
    cid INT,
    fname VARCHAR(20),
    lname VARCHAR(20),
    PRIMARY KEY(cid)
);

INSERT INTO client VALUES
    (1, "John", "Doe"),
    (2, "Rachel", "Evans"),
    (3, "Thomas", "Johnson"),
    (4, "Bruce", "Miller"),
    (5, "Shane", "Hampton"),
    (6, "Owen", "Keller"),
    (7, "Ronnie", "Jennings"),
    (8, "Santiago", "Aguilar"),
    (9, "Tina", "Massey"),
    (10, "Lee", "Jimenez"),
    (11, "Tiffany", "West"),
    (12, "Katrina", "Smith"),
    (13, "Chris", "Burgess"),
    (14, "Taylor", "Hoffman"),
    (15, "Willie", "Jenkins"),
    (16, "Diane", "Brock"),
    (17, "Kimberly", "Torres"),
    (18, "Mike", "Morgan"),
    (19, "Angel", "Alexander"),
    (20, "Sue", "Austin");

CREATE TABLE land_buyer (
    cid INT, 
    min_acres FLOAT, 
    max_acres FLOAT,
    PRIMARY KEY(cid),
    FOREIGN KEY(cid) REFERENCES client(cid)
);

INSERT INTO land_buyer VALUES 
    (1, .5, 1),
    (2, 1, 1.5),
    (3, .3, 1.2),
    (4, .5, .8),
    (5, .6, 1.5);

CREATE TABLE house_buyer (
    cid INT, 
    style VARCHAR(25),
    bed INT,
    bath INT,
    PRIMARY KEY(cid),
    FOREIGN KEY(cid) REFERENCES client(cid)
);

INSERT INTO house_buyer VALUES 
    (6, "1-story", 2, 2),
    (7, "1-story", 2, 2),
    (8, "2-story", 4, 3),
    (9, "2-story", 3, 2),
    (10, "2-story", 3, 3);

CREATE TABLE seller (
    cid INT,
    PRIMARY KEY(cid),
    FOREIGN KEY(cid) REFERENCES client(cid)
);

INSERT INTO seller VALUES 
    (11),
    (12),
    (13),
    (14),
    (15),
    (16),
    (17),
    (18),
    (19),
    (20);

CREATE TABLE realtor (
    rid INT,
    fname VARCHAR(20),
    lname VARCHAR(20),
    PRIMARY KEY(rid)
);

INSERT INTO realtor VALUES 
    (1, "Josh", "Thompson"),
    (2, "Lynne", "Henry"),
    (3, "Josh", "Simpson"),
    (4, "Jerry", "Vargas"),
    (5, "Marta", "Massey"),
    (6, "Judy", "Williams"),
    (7, "Essie", "Summers"),
    (8, "Bill", "Ortiz"),
    (9, "Annie", "Mcguire"),
    (10, "Carole", "Newton");

CREATE TABLE listing (
    pid INT,
    street VARCHAR(20),
    city VARCHAR(20),
    state VARCHAR(20),
    zip INT,
    listprice INT,
    type VARCHAR(20), 
    PRIMARY KEY(pid)
);

INSERT INTO listing VALUES 
    (1, "Harvey Lane", "Birmingham", "Alabama", 41240, 100000, "house"),
    (2, "Sunbream Lane", "Lemont", "IL", 57230, 40000, "land"),
    (3, "Lafayette St", "New Castle", "PA", 50202, 300000, "house"),
    (4, "Hartford St", "Millington", "TN", 50120, 250000, "house"),
    (5, "Bowman Ave", "Saint Petersburg", "FL", 60542, 180000, "land"),
    (6, "Michigan Ave", "Chigaco", "IL", 60450, 350000, "house"),
    (7, "Wall Street", "New York", "NY", 42810, 260000, "land"),
    (8, "Fairway Lane", "Mansfiled", "MA", 03412, 280000, "land"),
    (9, "Sheffield Road", "Jersey City", "NJ", 07302, 340000, "land"),
    (10, "Boston Rd", "Kingston", "NY", 50332, 160000, "house");

CREATE TABLE house (
    pid INT,
    style VARCHAR(20),
    bed INT, 
    bath INT,
    PRIMARY KEY(pid),
    FOREIGN KEY(pid) REFERENCES listing(pid)
);

INSERT INTO house VALUES 
    (1, "2-story", 3, 2),
    (2, "1-story", 2, 2),
    (3, "2-story", 4, 3),
    (4, "1-story", 2, 1),
    (5, "2-story", 4, 3);

CREATE TABLE land (
    pid INT, 
    acreage FLOAT,
    PRIMARY KEY(pid),
    FOREIGN KEY(pid) REFERENCES listing(pid)
);

INSERT INTO land VALUES
    (6, .5),
    (7, 1.5),
    (8, 1),
    (9, 1.2),
    (10, .7);
    
CREATE TABLE house_listing (
    pid INT,
    cid INT,
    rid INT,
    PRIMARY KEY(pid),
    FOREIGN KEY(pid) REFERENCES house(pid),
    FOREIGN KEY(cid) REFERENCES client(cid),
    FOREIGN KEY(rid) REFERENCES realtor(rid)
);

INSERT INTO house_listing VALUES 
    (1, 11, 3),
    (2, 12, 3),
    (3, 13, 2),
    (4, 14, 1),
    (5, 15, 5);

CREATE TABLE land_listing (
    pid INT,
    cid INT,
    rid INT,
    PRIMARY KEY(pid),
    FOREIGN KEY(pid) REFERENCES land(pid),
    FOREIGN KEY(cid) REFERENCES client(cid),
    FOREIGN KEY(rid) REFERENCES realtor(rid)
);

INSERT INTO land_listing VALUES 
    (6, 16, 6),
    (7, 17, 7),
    (8, 18, 8),
    (9, 19, 9),
    (10, 20, 10);

CREATE TABLE house_trans (
    pid INT, -- prpoerty if of house
    buy_cid INT,
    sell_cid INT,
    buy_rid INT, 
    sell_rid INT,
    sellprice INT, 
    PRIMARY KEY(pid),
    FOREIGN KEY(pid) REFERENCES house(pid),
    FOREIGN KEY(buy_cid) REFERENCES client(cid),
    FOREIGN KEY(sell_cid) REFERENCES client(cid),
    FOREIGN KEY(buy_rid) REFERENCES realtor(rid),
    FOREIGN KEY(sell_rid) REFERENCES realtor(rid)
);

INSERT INTO house_trans VALUES 
    (1, 6, 11, 4, 3, 175000),
    (2, 7, 12, 4, 3, 150000),
    (3, 8, 13, 3, 2, 200000),
    (4, 9, 14, 8, 1, 300000),
    (5, 10, 15, 7, 5, 380000);

CREATE TABLE land_trans (
    pid INT, -- property id of land
    buy_cid INT,
    sell_cid INT,
    buy_rid INT, 
    sell_rid INT,
    sellprice INT, 
    PRIMARY KEY(pid),
    FOREIGN KEY(pid) REFERENCES house(pid),
    FOREIGN KEY(buy_cid) REFERENCES client(cid),
    FOREIGN KEY(sell_cid) REFERENCES client(cid),
    FOREIGN KEY(buy_rid) REFERENCES realtor(rid),
    FOREIGN KEY(sell_rid) REFERENCES realtor(rid)
);

INSERT INTO land_trans VALUES 
    (6, 1, 16, 5, 6, 200000),
    (7, 2, 17, 7, 7, 250000),
    (8, 3, 18, 3, 8, 400000),
    (9, 4, 19, 9, 9, 300000),
    (10, 5, 20, 10, 10, 380000);

-- query 1
CREATE VIEW land_view AS
    SELECT la.pid, listprice, street, city, state, zip, acreage
        FROM land la join listing li
        WHERE la.pid = li.pid;

SELECT lb.cid, listprice, street, city, state, zip, acreage
    FROM land_buyer lb, land_view lv
    WHERE lv.acreage >= lb.min_acres
        and lv.acreage <= lb.max_acres

-- query 2
CREATE VIEW house_view AS 
    SELECT h.pid, listprice, street, city, state, zip, bed, bath, style
        FROM house h join listing l
            ON h.pid = l.pid;

SELECT cid, pid, listprice, street, city, state, zip, hb.bed, hb.bath, hb.style 
    FROM house_buyer hb, house_view hv 
    WHERE hb.style = hv.style 
    AND hb.bed = hv.bed 
    AND hb.bath = hv.bath;

    -- test