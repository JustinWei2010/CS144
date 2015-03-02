<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
    <head>
        <script type="text/javascript" 
            src="http://maps.google.com/maps/api/js?key=${API_KEY}">
        </script> 
        <!-- Define variables to be passed to googleMaps.js -->
        <script type="text/javascript">
            var latitude = ${item.location.latitude};
            var longitude = ${item.location.longitude};
            var address = '${item.address}';
        </script>
        <script type="text/javascript" src="googleMaps.js"></script>
        <title>Item: ${item.id}</title>
    </head>
    <body>
        <form action="item" method="GET" id="searchForm">
            ItemID:
            <input type="text" name="id">
            <input type="submit" value="Submit">
        </form>
        
        <h1>${item.name}</h1>
        <table>
            <c:forEach var="category" items="${item.categories}">
                <tr>
                    <td>category: ${category}</td>
                </tr>
            </c:forEach>
        </table>
        
        <h3>currently: ${item.currently}</h3>
        <c:if test="${not empty item.buyPrice}">
            <h3>buy_price: ${item.buyPrice}</h3>
        </c:if>
        
        <h3>first_bid: ${item.firstBid}</h3>
        <h3>number_of_bids: ${item.numberOfBids}</h3>
        <c:if test="${item.numberOfBids > 0}">
           <table>
                <c:forEach var="bid" items="${item.bids}">
                    <tr>
                        <td>bidder: ${bid.bidder.name}</td>
                        <td>rating: ${bid.bidder.rating}</td>
                        <td>time: ${bid.time}</td>
                        <td>amount: ${bid.amount}</td>
                    </tr>
                </c:forEach>
            </table>
        </c:if>

        <h3>started: ${item.started}</h3>
        <h3>ends: ${item.ends}</h3>

        <h3>seller: ${item.seller.name}</h3>
        <h4>rating: ${item.seller.rating}</h4>
        <h3>description: ${item.description}</h3>

        <div id="map_canvas" style="width: 500px; height: 550px;"></div>
    </body>
</html>