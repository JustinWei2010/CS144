<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:url value="search" var="prevURL">
    <c:param name="q" value="${query}" />
    <c:param name="numResultsToSkip" value="${numResultsToSkip-numResultsToReturn}" />
    <c:param name="numResultsToReturn" value="${numResultsToReturn}" />
</c:url>
<html>
    <head>
        <title>Pay Now</title>
        <link href="bootstrap.min.css" rel="stylesheet">
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
        <script src="bootstrap.min.js"></script>
        <style type="text/css"> 
            #pay-form .form-control { 
                text-align:right; 
            } 
            #pay-form .btn {
                width: 100%;
            }
        </style> 
    </head>
    <body>
        <jsp:include page="header.html"/>
        <div class="content-wrapper col-xs-8 col-xs-offset-2">
            <h1>Buy Item: ${id}</h1>
            <hr>
            <div class="row">
                <div class="pay-info-wrapper col-xs-8 col-xs-offset-2">
                    <div class="panel panel-info">
                        <div class="panel-heading">Checkout Information</div>
                            <div class="panel-body">
                                <form class="form-horizontal" id="pay-form" method="POST" action="${confirmURL}">
                                    <div class="form-group">
                                        <label for="inputItemID" class="col-xs-2 control-label">ID</label>
                                        <div class="col-xs-10">
                                            <input type="text" class="form-control" id="inputItemID" value="${id}" readonly>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label for="inputItemName" class="col-xs-2 control-label">Name</label>
                                        <div class="col-xs-10">
                                            <input type="text" class="form-control" id="inputItemName" value="${name}" readonly>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label for="inputBuyPrice" class="col-xs-2 control-label">BuyPrice</label>
                                        <div class="col-xs-10">
                                            <input type="text" class="form-control" id="inputBuyPrice" value="${price}" readonly>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label for="inputCreditCard" class="col-xs-2 control-label">CreditCard</label>
                                        <div class="col-xs-10">
                                            <input type="text" class="form-control" id="inputCreditCard" name="card_number" placeholder="Card Number...">
                                        </div>
                                    </div>
                                    <button class="btn btn-lg btn-primary" type="submit">Confirm</button>
                                </form>
                            </div><!-- /.panel -->
                        </div><!-- /.panel-heading -->
                    </div><!-- /.panel-body -->
                </div><!-- /.pay-info-wrapper -->
            </div>
        </div><!-- /.content-wrapper -->
    </body>
</html>