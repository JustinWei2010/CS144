******************************************************************************
TEAM: Amazon Meets Facebook
******************************************************************************

1) SSL is encryped at (4)->(5) and (5)->(6). These are the only points in time
where the credit card information is being passed. 

2) The item data is stored within Servlet sessions with the key buy_item. The
entire item is stored to ensure that the data that is loaded is correct and
consistent. Each time an item with buy_price field is loaded the item stored
within the session is changed. No data except the credit card information
is passed within get parameters. This is also only done when the server
is encrypted by SSL. 