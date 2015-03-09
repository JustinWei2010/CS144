<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
    <head>
        <title>Confirmation</title>
        <link href="bootstrap.min.css" rel="stylesheet">
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
        <script src="bootstrap.min.js"></script>
        <style type="text/css"> 
            #confirm-form .form-control { 
                text-align:right; 
            } 
        </style> 
    </head>
    <body>
        <jsp:include page="headerBasic.html"/>
        <div class="content-wrapper col-xs-8 col-xs-offset-2">
            <h1>Confirm Your Order</h1>
            <hr>
            <div class="row">
                <div class="confirm-info-wrapper col-xs-8 col-xs-offset-2">
                    <div class="panel panel-info">
                        <div class="panel-heading">Order Confirmation Details</div>
                            <div class="panel-body">
                                <form class="form-horizontal" id="confirm-form">
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
                                            <input type="text" class="form-control" id="inputCreditCard" value="${card_number}" readonly>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label for="inputTime" class="col-xs-2 control-label">Time</label>
                                        <div class="col-xs-10">
                                            <input type="text" class="form-control" id="inputTime" value="${time}" readonly>
                                        </div>
                                    </div>
                                </form>
                            </div><!-- /.panel -->
                        </div><!-- /.panel-heading -->
                    </div><!-- /.panel-body -->
                </div><!-- /.confirm-info-wrapper -->
            </div>
        </div><!-- /.content-wrapper -->
    </body>
</html>