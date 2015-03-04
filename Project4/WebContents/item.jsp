<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
    <head>
        <title>Item Result</title>
        <link href="bootstrap.min.css" rel="stylesheet">
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
        <script src="bootstrap.min.js"></script>

        <script type="text/javascript" 
            src="http://maps.google.com/maps/api/js?key=${API_KEY}">
        </script> 
        <c:if test="${not empty item}">
            <!-- Define variables to be passed to googleMaps.js -->
            <script type="text/javascript">
                var latitude = ${item.location.latitude};
                var longitude = ${item.location.longitude};
                var address = '${item.address}';
            </script>
            <script type="text/javascript" src="googleMaps.js"></script>
        </c:if>
        
    </head>
    <body>
        <jsp:include page="header.html"/>
        <div class="content-wrapper col-xs-8 col-xs-offset-2">
            <div class="row">
                <div class="col-xs-8 col-xs-offset-2">
                    <form action="item" method="GET" id="itemForm">
                        <div class="input-group">
                            <input type="text" class="form-control" name="id" placeholder="Input ItemID...">
                            <span class="input-group-btn">
                                <button class="btn btn-success" type="submit">Search!</button>
                            </span>
                        </div><!-- /input-group -->
                    </form>
                </div><!-- /.col-xs-8 -->
            </div><!-- /.row -->
            <hr>
            <div class="item-wrapper col-xs-12">
                <c:choose>
                    <c:when test="${not empty item}">
                        <div class="panel panel-info">
                            <div class="panel-heading">${item.name}</div>
                            <div class="panel-body">
                                <div class="row">
                                    <span class="col-xs-7">
                                        <div id="map-canvas" style="width: 100%; height: 400px;"></div>
                                    </span>
                                    <div class="col-xs-5">
                                        Seller: ${item.seller.name} <span class="badge">${item.seller.rating}</span>
                                        <hr>
                                        Started: ${item.started}
                                        <br>
                                        Ends: ${item.ends}
                                        <br>
                                        Current: ${item.currently}
                                        <br>
                                        <div class="btn-group">
                                            <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-expanded="false">
                                            ${item.numberOfBids} Bidders <span class="caret"></span></button>
                                            <c:if test="${item.numberOfBids > 0}">
                                                <ul class="dropdown-menu" role="menu">
                                                    <div class="list-group">
                                                        <c:forEach var="bid" items="${item.bids}">
                                                            <a href="#" class="list-group-item">
                                                                <h4 class="list-group-item-heading">${bid.bidder.name} <span class="badge">${bid.bidder.rating}</span></h4>
                                                                <p class="list-group-item-text">${bid.amount}</p>
                                                                <p>${bid.time}</p>
                                                            </a>
                                                        </c:forEach>
                                                    </div><!-- /.list-group -->
                                                </ul>
                                            </c:if>
                                        </div><!-- /.btn-group -->
                                        <hr>
                                        <div id="item-categories-list">
                                            <h3>Categories</h3>
                                            <c:forEach var="category" items="${item.categories}">
                                                <tr>
                                                    <td>#${category}</td>
                                                </tr>
                                            </c:forEach>
                                        </div><!-- /item-categories-list -->
                                    </div><!-- /.col-xs-5 -->
                                </div><!-- /.row -->
                                <hr>
                                <div id="item-description">
                                    <p>${item.description}</p>
                                </div>
                            </div><!-- /.panel-body -->
                       </div><!-- /.panel panel-info -->
                   </c:when>
                   <c:otherwise>
                       <h3>No Item Found...</h3>
                   </c:otherwise>
               </c:choose>
           </div><!-- /item-wrapper -->
       </div><!-- /content-wrapper -->
    </body>
</html>