package edu.ucla.cs.cs144;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class ItemServlet extends HttpServlet implements Servlet {

   public ItemServlet() {}

   protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
   {
      final String xmlData = AuctionSearchClient.getXMLDataForItemId(request.getParameter("id"));
      //Only parse xmlData if item with id exists in database
      if (xmlData != "" && xmlData != null) {
         final Item item = XMLParser.convertXMLToItem(xmlData);
         request.setAttribute("item", item);
         request.setAttribute("xmlData", xmlData);
         request.getRequestDispatcher("/item.jsp").forward(request, response);
      } else {
         //TODO: Include this notFound page
         request.getRequestDispatcher("/notFound.html").forward(request, response);
      }
   }
}
