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
    </head>
    <body>
        <form action="search" method="GET" id="searchForm">
            Search:
            <input type="text" name="q">
            <input type="hidden" name="numResultsToSkip" value="${DEFAULT_SKIP}">
            <input type="hidden" name="numResultsToReturn" value="${DEFAULT_RETURN}">
            <input type="submit" value="Submit">
        </form>

        <h1>Search Results</h1>
        <c:choose>
            <c:when test="${not empty search_result}">
                <table>
                    <c:forEach var="result" items="${search_result}" begin="0" end="${numResultsToReturn-1}">
                        <c:url value="item" var="itemURL">
                            <c:param name="id" value="${result.itemId}" />
                        </c:url>
                        <tr>
                            <td><a href="${itemURL}">${result.itemId}</a></td>
                            <td>${result.name}</td>
                        </tr>
                    </c:forEach>
                </table>
                <c:if test="${show_prev}">
                    <a href="${prevURL}">Prev</a>
                </c:if>
                <c:if test="${show_next}">
                    <a href="${nextURL}">Next</a>
                </c:if>
            </c:when>
            <c:otherwise>
                <h3>No Results Found...</h3>
            </c:otherwise>
        </c:choose>

    </body>
</html>