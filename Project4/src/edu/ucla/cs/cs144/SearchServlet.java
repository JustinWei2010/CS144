package edu.ucla.cs.cs144;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class SearchServlet extends HttpServlet implements Servlet {
   private static final int DEFAULT_SKIP = 0;
   private static final int DEFAULT_RETURN = 30;

   public SearchServlet() {}

   protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
   {
      final String query = request.getParameter("q");
      final String numResultsToSkip = request.getParameter("numResultsToSkip");
      final String numResultsToReturn = request.getParameter("numResultsToReturn");
      int skip_count = DEFAULT_SKIP;
      int result_count = DEFAULT_RETURN;
      if (numResultsToSkip != null) {skip_count = Integer.parseInt(numResultsToSkip);}
      if (numResultsToReturn != null) {result_count = Integer.parseInt(numResultsToReturn);}
      final SearchResult[] search_result = AuctionSearchClient.basicSearch(query, skip_count, result_count);

      request.setAttribute("search_result", search_result);
      request.setAttribute("query", query);
      request.setAttribute("numResultsToSkip", skip_count);
      request.setAttribute("numResultsToReturn", result_count);
      request.setAttribute("show_prev", skip_count-result_count >= 0 && result_count != 0);
      request.setAttribute("show_next", search_result.length == result_count);
      request.getRequestDispatcher("/search.jsp").forward(request, response);
   }
}
