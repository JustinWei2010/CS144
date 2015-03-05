package edu.ucla.cs.cs144;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@SuppressWarnings("serial")
public class ItemServlet extends HttpServlet implements Servlet {
   private static final String API_KEY = "AIzaSyDve_kvrQSTQt-Exg3b4Sbiji0LlfwABWk";

   public ItemServlet() {}

   protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
   {
      final HttpSession session = request.getSession(true);
      Item item = null;
      try {
         final String xmlData = AuctionSearchClient.getXMLDataForItemId(request.getParameter("id"));
         if (xmlData != "" && xmlData != null) {
            item = XMLParser.convertXMLToItem(xmlData);
            if (item.getBuyPrice() != null && item.getBuyPrice() != "" && session != null) {
               session.setAttribute("buy_item",item);
               request.setAttribute("server_name", request.getServerName());
               request.setAttribute("context_path", request.getContextPath());
            }
         }
      } catch (final Exception ex) {
         ex.printStackTrace();
      }
      request.setAttribute("item", item);
      request.setAttribute("API_KEY", API_KEY);
      request.getRequestDispatcher("/item.jsp").forward(request, response);
   }
}