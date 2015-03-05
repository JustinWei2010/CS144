package edu.ucla.cs.cs144;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@SuppressWarnings("serial")
public class BuyServlet extends HttpServlet implements Servlet {
   final static String SECURE_PORT = "8443";
   public BuyServlet() {}

   protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
   {
      final HttpSession session = request.getSession(true);
      if (session != null) {
         final Item item = (Item) session.getAttribute("buy_item");
         final String confirmURL = "https:"+request.getServerName()+":"+SECURE_PORT+request.getContextPath()+"/confirmOrder";

         //Return 404 page if no buy item is found in session
         if (item != null) {
            request.setAttribute("id", item.getId());
            request.setAttribute("name", item.getName());
            request.setAttribute("price", item.getBuyPrice());
            request.setAttribute("confirmURL", confirmURL);
            request.getRequestDispatcher("/buy.jsp").forward(request, response);
         } else {
            request.getRequestDispatcher("/404.jsp").forward(request, response);
         }
      } else {
         request.getRequestDispatcher("/404.jsp").forward(request, response);
      }
   }
}