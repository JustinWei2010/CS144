package edu.ucla.cs.cs144;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@SuppressWarnings("serial")
public class ConfirmOrderServlet extends HttpServlet implements Servlet {
   public ConfirmOrderServlet() {}

   protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
   {
      final HttpSession session = request.getSession(true);
      if (session != null && request.isSecure()) {
         final Item item = (Item) session.getAttribute("buy_item");
         final String card_number = request.getParameter("card_number");
         final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM-dd-yy kk:mm:ss");
         final Date date = new Date();

         //Return 404 page if no buy item is found in session or no card info provided
         if (item != null && card_number != null) {
            request.setAttribute("id", item.getId());
            request.setAttribute("name", item.getName());
            request.setAttribute("price", item.getBuyPrice());
            request.setAttribute("card_number", card_number);
            request.setAttribute("time", dateFormat.format(date));
            request.getRequestDispatcher("/confirmOrder.jsp").forward(request, response);
         } else {
            request.getRequestDispatcher("/404.jsp").forward(request, response);
         }
      } else {
         request.getRequestDispatcher("/404.jsp").forward(request, response);
      }
   }
}