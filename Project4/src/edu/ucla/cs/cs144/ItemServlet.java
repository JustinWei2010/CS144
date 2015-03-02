package edu.ucla.cs.cs144;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class ItemServlet extends HttpServlet implements Servlet {
   private static final String API_KEY = "AIzaSyDve_kvrQSTQt-Exg3b4Sbiji0LlfwABWk";

   public ItemServlet() {}

   protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
   {
      try {
         final String xmlData = AuctionSearchClient.getXMLDataForItemId(request.getParameter("id"));
         if (xmlData != "" && xmlData != null) {
            final Item item = XMLParser.convertXMLToItem(xmlData);
            request.setAttribute("item", item);
            request.setAttribute("xmlData", xmlData);
            request.setAttribute("API_KEY", API_KEY);
            request.getRequestDispatcher("/item.jsp").forward(request, response);
         } else {
            request.getRequestDispatcher("/404.html").forward(request, response);
         }
      } catch (final Exception ex) {
         ex.printStackTrace();
         request.getRequestDispatcher("/404.html").forward(request, response);
      }
   }
}