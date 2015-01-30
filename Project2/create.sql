CREATE TABLE user
(
    user_id CHAR(100) PRIMARY KEY,
    location VARCHAR(100),
    country VARCHAR(100)
);

CREATE TABLE item
(
    item_id INT UNSIGNED PRIMARY KEY,
    seller_id CHAR(100),
    name VARCHAR(1000),
    buy_price DECIMAL(8,2),
    currently DECIMAL(8,2),
    first_bid DECIMAL(8,2),
    number_of_bids int,
    started TIMESTAMP,
    ends TIMESTAMP,
    description VARCHAR(4000),
    location VARCHAR(100),
    country VARCHAR(100),
    latitude VARCHAR(100),
    longitude VARCHAR(100),
    FOREIGN KEY (seller_id) REFERENCES user(user_id)
);

CREATE TABLE item_category
(
    item_id INT UNSIGNED PRIMARY KEY,
    category VARCHAR(100),
    FOREIGN KEY (item_id) REFERENCES item(item_id)
);

CREATE TABLE item_bid
(
    item_id INT UNSIGNED,
    bidder_id CHAR(100),
    time TIMESTAMP,
    amount DECIMAL(8,2),
    FOREIGN KEY (item_id) REFERENCES item(item_id),
    FOREIGN KEY (bidder_id) REFERENCES user(user_id),
    PRIMARY KEY(item_id, bidder_id, time)
);

CREATE TABLE bidder
(
    user_id CHAR(100) PRIMARY KEY,
    rating INT,
    FOREIGN KEY (user_id) REFERENCES user(user_id)
);

CREATE TABLE seller
(
    user_id CHAR(100) PRIMARY KEY,
    rating INT,
    FOREIGN KEY (user_id) REFERENCES user(user_id)
);
