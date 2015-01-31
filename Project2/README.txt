**********************************************************
TEAM: Amazon Meets Facebook
**********************************************************

We worked together for the most part for all parts of the 
project, but we were in charge of different final parts.

Justin Wei: Java transformation program, making sure it 
works. 
Samping Chuang: Relational schema and queries, making sure
the right numbers were being outputted.

----------------------------------------------------------
PART B Solutions:

1)
user
    -user_id 
    -location
    -country
    -PRIMARY (user_id)

item 
    -item_id 
    -seller_id (foreign)
    -name
    -buy_price
    -currently
    -first_bid
    -number_of_bids
    -started
    -ends
    -location
    -country
    -latitude
    -longitude
    -description
    -PRIMARY (item_id)

item_category
    -item_id (foreign)
    -category
    -PRIMARY (item_id, category)

item_bid
    -item_id (foreign)
    -bidder_id (foreign)
    -time
    -amount
    -PRIMARY (item_id, bidder_id, time)

bidder
    -user_id (foreign)
    -rating
    -PRIMARY (user_id)

seller
    -user_id (foreign)
    -rating
    -PRIMARY (user_id)

2)
user 
    user_id -> seller_rating, bidder_rating, location, country
    
item
    item_id -> seller_id, name, buy_price, currently, first_bid, 
number_of_bids, started, ends, description, location, country, 
latitude, longitude

item_category
    item_id -> category

item_bid
    item_id, bidder_id, time -> amount

bidder
    user_id -> rating

seller
    user_id -> rating

3) Yes the schema is both in BCNF and 4NF forms.
