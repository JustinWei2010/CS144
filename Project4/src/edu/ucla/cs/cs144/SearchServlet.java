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
      int numResultsToSkip = DEFAULT_SKIP;
      int numResultsToReturn = DEFAULT_RETURN;
      SearchResult[] search_result = null;
      try {
         numResultsToSkip = Integer.parseInt(request.getParameter("numResultsToSkip"));
         numResultsToReturn = Integer.parseInt(request.getParameter("numResultsToReturn"));
         if (query != null && query != "") {
            //Grab extra result for checking whether or not to display next button
            search_result = AuctionSearchClient.basicSearch(query, numResultsToSkip, numResultsToReturn+1);
         }
      } catch (final Exception ex) {
         ex.printStackTrace();
      }

      request.setAttribute("search_result", search_result);
      request.setAttribute("query", query);
      request.setAttribute("numResultsToSkip", numResultsToSkip);
      request.setAttribute("numResultsToReturn", numResultsToReturn);
      request.setAttribute("show_prev", search_result != null && numResultsToSkip-numResultsToReturn >= 0 && numResultsToReturn != 0);
      request.setAttribute("show_next", search_result != null && search_result.length == numResultsToReturn+1 && numResultsToReturn != 0);
      request.setAttribute("DEFAULT_SKIP", DEFAULT_SKIP);
      request.setAttribute("DEFAULT_RETURN", DEFAULT_RETURN);
      request.getRequestDispatcher("/search.jsp").forward(request, response);
   }
}