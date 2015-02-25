<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
    <head>
        <title>${item.id}</title>
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
        
        <h3>location: ${item.location.name}</h3>
        <c:if test="${not empty item.location.latitude}">
            <h4>latitude: ${item.location.latitude}</h4>
        </c:if>
        <c:if test="${not empty item.location.longitude}">
            <h4>longitude: ${item.location.longitude}</h4>
        </c:if>
        <h3>country: ${item.country}</h3>
        
        <h3>seller: ${item.seller.name}</h3>
        <h4>rating: ${item.seller.rating}</h4>
        <h3>description: ${item.description}</h3>
    </body>
</html>