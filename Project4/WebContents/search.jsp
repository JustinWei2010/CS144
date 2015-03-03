<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!-- Define buttons urls -->
<c:url value="search" var="prevURL">
    <c:param name="q" value="${query}" />
    <c:param name="numResultsToSkip" value="${numResultsToSkip-numResultsToReturn}" />
    <c:param name="numResultsToReturn" value="${numResultsToReturn}" />
</c:url>
<c:url value="search" var="nextURL">
    <c:param name="q" value="${query}" />
    <c:param name="numResultsToSkip" value="${numResultsToSkip+numResultsToReturn}" />
    <c:param name="numResultsToReturn" value="${numResultsToReturn}" />
</c:url>

<html>
    <head>
        <title>Search Results</title>
        <link href="bootstrap.min.css" rel="stylesheet">
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
        <script src="bootstrap.min.js"></script>
    
        
        <script type="text/javascript" src="AutoSuggestControl.js"></script>
        <style>
            div.suggestions {
                -moz-box-sizing: border-box;
                box-sizing: border-box;
                border: 1px solid black;
                position: absolute;   
                background-color: white;
            }

            div.suggestions div {
                cursor: default;
                padding: 0px 3px;
                white-space: nowrap;
   		overflow: hidden;
                text-overflow: ellipsis;
	    }

	    div.suggestions div.current {
                background-color: #3366cc;
           	color: white;
	    }
        </style>
    </head>
    <body>
        <jsp:include page="header.html"/>
        <div class="content-wrapper col-xs-8 col-xs-offset-2">
            <div class="row">
                <div class="col-xs-8 col-xs-offset-2">
                    <form action="search" method="GET" id="searchForm">
                        <div class="input-group">
                            <input type="text" class="form-control" autocomplete="off" placeholder="Looking for..." name="q" id="searchTextBox">
                            <input type="hidden" name="numResultsToSkip" value="${DEFAULT_SKIP}">
                            <input type="hidden" name="numResultsToReturn" value="${DEFAULT_RETURN}">
                            <span class="input-group-btn">
                                <button class="btn btn-success" type="submit">Search!</button>
                            </span>
                        </div><!-- /input-group -->
                    </form>
                </div><!-- /.col-xs-8 -->
            </div><!-- /.row -->
            <hr>
            <div class="search-results-wrapper col-xs-12">
                <c:choose>
                    <c:when test="${not empty search_result}">
                        <c:forEach var="result" items="${search_result}" begin="0" end="${numResultsToReturn-1}">
                            <c:url value="item" var="itemURL">
                                <c:param name="id" value="${result.itemId}" />
                            </c:url>
                            <span class="col-xs-10"><p>${result.name}</p></span>
                            <a href="${itemURL}">${result.itemId}</a>
                            <hr>
                        </c:forEach>
                        <div class="btn-group col-xs-offset-5" role="group" aria-label="...">
                            <c:if test="${show_prev}">
                                <a class="btn btn-lg btn-primary" href="${prevURL}">Prev</a>
                            </c:if>
                            <c:if test="${show_next}">
                                <a class="btn btn-lg btn-primary" href="${nextURL}">Next</a>
                            </c:if>
                            <hr>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <h3>No Results Found...</h3>
                    </c:otherwise>
                </c:choose>
            </div><!-- /.search-results-wrapper -->
        </div><!-- /.content-wrapper -->
    </body>
</html>
