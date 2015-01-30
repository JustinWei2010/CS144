SELECT COUNT(user_id) FROM user;

SELECT COUNT(item_id) FROM item WHERE BINARY location="New York";

SELECT COUNT(*) FROM (SELECT * FROM item_category GROUP BY item_id HAVING count(item_id)=4) AS Agg;

SELECT item_id FROM (SELECT item_id, MAX(amount) FROM item_bid WHERE item_id IN (SELECT item_id FROM item WHERE ends>'2001-12-20 12:00:01')) AS Agg;

SELECT COUNT(user_id) FROM seller WHERE rating>1000;

SELECT COUNT(user_id) FROM user WHERE user_id in (SELECT user_id FROM seller) AND user_id in (SELECT user_id FROM bidder);

SELECT COUNT(*) FROM (SELECT category FROM item_category WHERE item_category.item_id IN  (SELECT DISTINCT item_id FROM item_bid WHERE item_bid.amount>100) GROUP BY category) AS Agg;
