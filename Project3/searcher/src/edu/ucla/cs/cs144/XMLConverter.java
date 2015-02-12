package edu.ucla.cs.cs144;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class XMLConverter {
   private static final String LESS_SIGN = "&lt;";
   private static final String GREATER_SIGN = "&gt;";
   private static final String AMP_SIGN = "&amp;";
   private static final String QUOTE_SIGN = "&quot;";
   private static final String APOS_SIGN = "&apos;";
   private static final int SELLER = 0;
   private static final int BIDDER = 1;

   private Connection conn;
   private String converted_xml;
   private String item_id;

   public XMLConverter(final Connection conn, final String item_id) {
      this.conn = conn;
      this.item_id = item_id;
      this.converted_xml = "";
      try {
         this.buildXML();
      } catch (SQLException ex) {
         ex.printStackTrace();
      }
   }

   private void buildXML() throws SQLException{
      final String query = String.format("SELECT * FROM item WHERE item_id = \"%s\"", this.item_id);
      final Statement stmt = conn.createStatement();
      final ResultSet rs = stmt.executeQuery(query);
      if (rs.next()) {
         this.converted_xml += String.format("<Item ItemID=\"%s\">\n", this.item_id);
         this.converted_xml += String.format("<Name>%s</Name>\n", escapeTextChars(rs.getString("name")));
         this.converted_xml += getCategoriesXML();

         this.converted_xml += String.format("<Currently>$%s</Currently>\n", rs.getString("currently"));
         final String buy_price = rs.getString("buy_price");
         if (buy_price != null) {
            this.converted_xml += String.format("<Buy_Price>$%s</Buy_Price>\n", buy_price);
         }
         this.converted_xml += String.format("<First_Bid>$%s</First_Bid>\n", rs.getString("first_bid"));

         final int number_bids = rs.getInt("number_of_bids");
         this.converted_xml += String.format("<Number_of_Bids>%d</Number_of_Bids>\n", number_bids);
         this.converted_xml += getBidsXML(number_bids);

         final String latitude = rs.getString("latitude");
         final String longitude = rs.getString("Longitude");
         if (latitude != null && longitude != null) {
            this.converted_xml += String.format("<Location Latitude=\"%s\" Longitude=\"%s\">%s</Location>\n",
                  latitude, longitude, escapeTextChars(rs.getString("location")));
         } else {
            this.converted_xml += String.format("<Location>%s</Location>\n", escapeTextChars(rs.getString("location")));
         }

         this.converted_xml += String.format("<Country>%s</Country>\n", escapeTextChars(rs.getString("country")));
         this.converted_xml += String.format("<Started>%s</Started>\n", sqlToXMLDate(rs.getString("started")));
         this.converted_xml += String.format("<Ends>%s</Ends>\n", sqlToXMLDate(rs.getString("ends")));
         this.converted_xml += getUserXML(rs.getString("seller_id"), SELLER, null);
         this.converted_xml += String.format("<Description>%s</Description>\n", escapeTextChars(rs.getString("description")));
         this.converted_xml += String.format("</Item>");
      }
   }

   /*
    * Helper function that transforms XML escape characters in a text to their counterparts
    */
   private String escapeTextChars(final String text) {
      String result = "";
      for (int i = 0; i < text.length(); i++) {
         char c = text.charAt(i);
         switch (c) {
         case '<':
            result += LESS_SIGN;
            break;
         case '>':
            result += GREATER_SIGN;
            break;
         case '&':
            result += AMP_SIGN;
            break;
         case '\"':
            result += QUOTE_SIGN;
            break;
         case '\'':
            result += APOS_SIGN;
            break;
         default:
            result += c;
            break;
         }
      }
      return result;
   }

   /*
    * Helper function that converts SQL date string format to XML date format
    */
   private String sqlToXMLDate(final String date) {
      DateFormat sql_format = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
      DateFormat xml_format = new SimpleDateFormat("MMM-dd-yy kk:mm:ss");
      try {
         Date d = sql_format.parse(date);
         return xml_format.format(d);
      } catch (final ParseException ex) {
         ex.printStackTrace();
         System.out.format("Error parsing date \"%s\"!\n", date);
         return "";
      }
   }

   private String getCategoriesXML() throws SQLException {
      final String query = String.format("SELECT category FROM item_category WHERE item_id = \"%s\"", this.item_id);
      final Statement stmt = conn.createStatement();
      final ResultSet rs = stmt.executeQuery(query);
      String categories_xml = "";
      while (rs.next()) {
         categories_xml += String.format("<Category>%s</Category>\n", escapeTextChars(rs.getString("category")));
      }
      return categories_xml;
   }

   private String getBidsXML(final int bid_count) throws SQLException {
      final String query = String.format("SELECT bidder_id, time, amount FROM item_bid WHERE item_id = \"%s\"", this.item_id);
      final Statement stmt = conn.createStatement();
      final ResultSet rs = stmt.executeQuery(query);
      String bids_xml = "";
      if (bid_count > 0) {
         bids_xml += "<Bids>\n";
         final PreparedStatement pstmt = conn.prepareStatement("SELECT rating FROM bidder WHERE user_id = ?");
         while(rs.next()) {
            bids_xml += "<Bid>\n";
            final String user_id = rs.getString("bidder_id");
            pstmt.setString(1, user_id);
            bids_xml += getUserXML(user_id, BIDDER, pstmt);
            bids_xml += String.format("<Time>%s</Time>\n", sqlToXMLDate(rs.getString("time")));
            bids_xml += String.format("<Amount>$%s</Amount>\n", rs.getString("amount"));
            bids_xml += "</Bid>\n";
         }
         bids_xml += "</Bids>\n";
      } else {
         bids_xml += "<Bids />\n";
      }
      return bids_xml;
   }

   private String getUserXML(final String user_id, final int type, PreparedStatement pstmt) throws SQLException {
      String user_xml = "";
      if (type == SELLER) {
         final String query = String.format("SELECT rating FROM seller WHERE user_id = \"%s\"", user_id);
         final Statement stmt = conn.createStatement();
         final ResultSet rs = stmt.executeQuery(query);
         if (rs.next()) {
            user_xml += String.format("<Seller Rating=\"%s\" UserID=\"%s\" />\n", rs.getString("rating"), user_id);
         } else {
            System.err.format("No rating found for seller \"%s\"\n", user_id);
         }
      } else if (type == BIDDER) {
         pstmt.setString(1, user_id);
         final ResultSet bidder_set = pstmt.executeQuery();
         if (bidder_set.next()) {
            final String query = String.format("SELECT location, country FROM user WHERE user_id = \"%s\"", user_id);
            final Statement stmt = conn.createStatement();
            final ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
               user_xml += String.format("<Bidder Rating=\"%s\" UserID=\"%s\">\n", bidder_set.getString("rating"), user_id);
               final String location = escapeTextChars(rs.getString("location"));
               final String country = escapeTextChars(rs.getString("country"));
               if (location != null) {
                  user_xml += String.format("<Location>%s</Location>\n", location);
               }
               if (country != null) {
                  user_xml += String.format("<Country>%s</Country>\n", country);
               }
               user_xml += "</Bidder>\n";
            } else {
               System.err.format("No user found for id \"%s\"\n", user_id);
            }
         } else {
            System.err.format("No rating found for seller \"%s\"\n", user_id);
         }
      }
      return user_xml;
   }

   public String getXML() {
      return this.converted_xml;
   }

}
