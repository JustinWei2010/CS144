/* CS144
*
* Parser skeleton for processing item-???.xml files. Must be compiled in
* JDK 1.5 or above.
*
* Instructions:
*
* This program processes all files passed on the command line (to parse
* an entire diectory, type "java MyParser myFiles/*.xml" at the shell).
*
* At the point noted below, an individual XML file has been parsed into a
* DOM Document node. You should fill in code to process the node. Java's
* interface for the Document Object Model (DOM) is in package
* org.w3c.dom. The documentation is available online at
*
* http://java.sun.com/j2se/1.5.0/docs/api/index.html
*
* A tutorial of Java's XML Parsing can be found at:
*
* http://java.sun.com/webservices/jaxp/
*
* Some auxiliary methods have been written for you. You may find them
* useful.
*/

package edu.ucla.cs.cs144;

import java.io.*;
import java.text.*;
import java.util.*;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ErrorHandler;


class MyParser {
   static final String[] typeName = {
  "none",
  "Element",
  "Attr",
  "Text",
  "CDATA",
  "EntityRef",
  "Entity",
  "ProcInstr",
  "Comment",
  "Document",
  "DocType",
  "DocFragment",
  "Notation",
   };
   
   //Global variables
   public static final String DELIM = "|*|";
   public static DocumentBuilder builder;
   public static final int SELLER = 0;
   public static final int BIDDER = 1;
   public static final String SQL_NULL = "\\N";
   public static final int MAX_DESC_LENGTH = 4000;
   public static final HashMap<String, String> users = new HashMap<String, String>();
   
   //Output files
   public static PrintWriter item_file;
   public static PrintWriter item_category_file;
   public static PrintWriter item_bid_file;
   public static PrintWriter user_file;
   public static PrintWriter bidder_file;
   public static PrintWriter seller_file;
   
   static class MyErrorHandler implements ErrorHandler {
       
       public void warning(SAXParseException exception)
       throws SAXException {
           fatalError(exception);
       }
       
       public void error(SAXParseException exception)
       throws SAXException {
           fatalError(exception);
       }
       
       public void fatalError(SAXParseException exception)
       throws SAXException {
           exception.printStackTrace();
           System.out.println("There should be no errors " +
                              "in the supplied XML files.");
           System.exit(3);
       }
       
   }
   
   /* Non-recursive (NR) version of Node.getElementsByTagName(...)
    */
   static Element[] getElementsByTagNameNR(Element e, String tagName) {
       Vector< Element > elements = new Vector< Element >();
       Node child = e.getFirstChild();
       while (child != null) {
           if (child instanceof Element && child.getNodeName().equals(tagName))
           {
               elements.add( (Element)child );
           }
           child = child.getNextSibling();
       }
       Element[] result = new Element[elements.size()];
       elements.copyInto(result);
       return result;
   }
   
   /* Returns the first subelement of e matching the given tagName, or
    * null if one does not exist. NR means Non-Recursive.
    */
   static Element getElementByTagNameNR(Element e, String tagName) {
       Node child = e.getFirstChild();
       while (child != null) {
           if (child instanceof Element && child.getNodeName().equals(tagName))
               return (Element) child;
           child = child.getNextSibling();
       }
       return null;
   }
   
   /* Returns the text associated with the given element (which must have
    * type #PCDATA) as child, or "" if it contains no text.
    */
   static String getElementText(Element e) {
       if (e.getChildNodes().getLength() == 1) {
           Text elementText = (Text) e.getFirstChild();
           return elementText.getNodeValue();
       }
       else
           return "";
   }
   
   /* Returns the text (#PCDATA) associated with the first subelement X
    * of e with the given tagName. If no such X exists or X contains no
    * text, "" is returned. NR means Non-Recursive.
    */
   static String getElementTextByTagNameNR(Element e, String tagName) {
       Element elem = getElementByTagNameNR(e, tagName);
       if (elem != null)
           return getElementText(elem);
       else
           return "";
   }
   
   /* Returns the amount (in XXXXX.xx format) denoted by a money-string
    * like $3,453.23. Returns the input if the input is an empty string.
    */
   static String strip(String money) {
       if (money.equals(""))
           return money;
       else {
           double am = 0.0;
           NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.US);
           try { am = nf.parse(money).doubleValue(); }
           catch (ParseException e) {
               System.out.println("This method should work for all " +
                                  "money values you find in our data.");
               System.exit(20);
           }
           nf.setGroupingUsed(false);
           return nf.format(am).substring(1);
       }
   }
   
   /*
    * Convert xml date string format to SQL datetime load format
    */
   static String xmlToSQLDate(String date) throws ParseException {
      DateFormat xml_format = new SimpleDateFormat("MMM-dd-yy kk:mm:ss");
      DateFormat sql_format = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
      Date d = xml_format.parse(date);
      return sql_format.format(d);
   }
   
   /* Process one items-???.xml file.
    */
   static void processFile(File xmlFile) {
       Document doc = null;
       try {
           doc = builder.parse(xmlFile);
       }
       catch (IOException e) {
           e.printStackTrace();
           System.exit(3);
       }
       catch (SAXException e) {
           System.out.println("Parsing error on file " + xmlFile);
           System.out.println("  (not supposed to happen with supplied XML files)");
           e.printStackTrace();
           System.exit(3);
       }
       
       /* At this point 'doc' contains a DOM representation of an 'Items' XML
        * file. Use doc.getDocumentElement() to get the root Element. */
       System.out.println("Successfully parsed - " + xmlFile);
       
       /* Fill in code here (you will probably need to write auxiliary
           methods). */
       translateDomTree(doc.getDocumentElement());
       
       /**************************************************************/
       System.out.println("Successfully translated - " + xmlFile);
       //recursiveDescent(doc.getDocumentElement(), 0);
   }
   
   /*
    * Output the DOM tree as tuples to SQL load files
    */
   public static void translateDomTree(final Element tree) {
      //Translate each item
      Element[] elements = getElementsByTagNameNR((Element) tree, "Item");
      for (int i=0; i<elements.length; i++) {
         translateItem(elements[i]);
      }
      
      //Output user tuple data according to hashmap
      for (Entry<String, String> user : users.entrySet()) {
         String user_id = user.getKey();
         String location_info = user.getValue();
         
         if (location_info == null) {
            user_file.println(user_id + DELIM + SQL_NULL + DELIM + SQL_NULL);
         } else {
            user_file.println(user_id + DELIM + location_info);
         }
      }
   }
   
   public static void translateItem(final Element item) {
      String item_id = item.getAttribute("ItemID");
      String name = SQL_NULL;
      String currently = SQL_NULL;
      String buy_price = SQL_NULL;
      String first_bid = SQL_NULL;
      String number_of_bids = SQL_NULL;
      String location = SQL_NULL;
      String latitude = SQL_NULL;
      String longitude = SQL_NULL;
      String country = SQL_NULL;
      String started = SQL_NULL;
      String ends = SQL_NULL;
      String seller_id = SQL_NULL;
      String description = SQL_NULL;
      
      //Translate each of item's elements
      org.w3c.dom.NodeList nlist = item.getChildNodes();
      for (int i=0; i<nlist.getLength(); i++) {
         Node n = nlist.item(i);
         switch (n.getNodeName()) {
            case "Name":
               name = getElementText((Element) n);
               break;
            case "Category":
               translateCategory((Element) n, item_id);
               break;
            case "Currently":
               currently = strip(getElementText((Element) n));
               break;
            case "Buy_Price":
               buy_price = strip(getElementText((Element) n));
               break;
            case "First_Bid":
               first_bid = strip(getElementText((Element) n));
               break;
            case "Number_of_Bids":
               number_of_bids = getElementText((Element) n);
               break;
            case "Bids":
               Element[] bids = getElementsByTagNameNR((Element) n, "Bid");
               for (int j=0; j<bids.length; j++) {
                  translateBid(bids[j], item_id);
               }
               break;
            case "Location":
               location = getElementText((Element) n);
               latitude = ((Element) n).getAttribute("Latitude");
               longitude = ((Element) n).getAttribute("Longitude");
               break;
            case "Country":
               country = getElementText((Element) n);
               break;
            case "Started":
               try {
                  started = xmlToSQLDate(getElementText((Element) n));
               } catch (ParseException e) {
                  System.out.println("Xml date parsing error");
                  System.exit(2);
               }
               break;
            case "Ends":
               try {
                  ends = xmlToSQLDate(getElementText((Element) n));
               } catch (ParseException e) {
                  System.out.println("Xml date parsing error");
                  System.exit(2);
               }
               break;
            case "Seller":
               seller_id = translateUserInfo((Element) n, item_id, SELLER);
               break;
            case "Description":
               description = getElementText((Element) n);
               if (description.length() > 4000) {
                  description = description.substring(0, MAX_DESC_LENGTH);
               }
               break;
         }
      }
      
      //Check for null coordinate attribute values
      if (latitude == "") {latitude = SQL_NULL;}
      if (longitude == "") {longitude = SQL_NULL;}
      
      item_file.format("%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s\n",
            item_id, DELIM, seller_id, DELIM, name, DELIM, buy_price, DELIM, currently, 
            first_bid, DELIM, number_of_bids, DELIM, started, DELIM, ends, DELIM, location, 
            DELIM, country, DELIM, latitude, DELIM, longitude, DELIM, description);
   }
   
   public static void translateCategory(final Element category, final String item_id) {
      item_category_file.println(item_id + DELIM + getElementText(category));
   }
   
   public static void translateBid(final Element bid, final String item_id) {
      try {
         final String date = xmlToSQLDate(getElementTextByTagNameNR(bid, "Time"));
         final String amount = strip(getElementTextByTagNameNR(bid, "Amount"));
         final String user_id = translateUserInfo(getElementByTagNameNR(bid, "Bidder"), item_id, BIDDER);
         item_bid_file.println(item_id + DELIM + user_id + DELIM + date + DELIM + amount);
      } catch (ParseException e) {
         System.out.println("Xml date parsing error");
         System.exit(2);
      }
   }
   
   public static String translateUserInfo(final Element user, final String item_id, final int type) {
      final String user_id = user.getAttribute("UserID");
      final String rating = user.getAttribute("Rating");
      if (type == SELLER) {
         //Check if entry exists, put dummy value
         if (users.get(user_id) == null) {
            users.put(user_id, null);
         }
         
         //Update seller table
         seller_file.println(user_id + DELIM + rating);
      } else if(type == BIDDER) {
         //Check if entry exists, put location value
         if (users.get(user_id) == null) {
            String location = getElementTextByTagNameNR(user, "Location");
            String country = getElementTextByTagNameNR(user, "Country");
            if (location == null) {location = SQL_NULL;}
            if (country == null) {country = SQL_NULL;}
            users.put(user_id, location + DELIM + country);
         }
         
         //Update bidder table
         bidder_file.println(user_id + DELIM + rating);
      } else {
         System.out.println("Invalid user type in translateUserInfo()!");
         System.exit(2);
      }
      return user_id;
   }
   
   public static void recursiveDescent(Node n, int level) {
       // adjust indentation according to level
       for(int i=0; i<4*level; i++)
           System.out.print(" ");
       
       // dump out node name, type, and value  
       String ntype = typeName[n.getNodeType()];
       String nname = n.getNodeName();
       String nvalue = n.getNodeValue();
       
       System.out.println("Type = " + ntype + ", Name = " + nname + ", Value = " + nvalue);
       // dump out attributes if any
       org.w3c.dom.NamedNodeMap nattrib = n.getAttributes();
       if(nattrib != null && nattrib.getLength() > 0)
           for(int i=0; i<nattrib.getLength(); i++)
               recursiveDescent(nattrib.item(i),  level+1);
       
       // now walk through its children list
       org.w3c.dom.NodeList nlist = n.getChildNodes();
       for(int i=0; i<nlist.getLength(); i++)
           recursiveDescent(nlist.item(i), level+1);
   }  
   
   public static void main (String[] args) {
       if (args.length == 0) {
           System.out.println("Usage: java MyParser [file] [file] ...");
           System.exit(1);
       }
       
       /* Initialize parser. */
       try {
           DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
           factory.setValidating(false);
           factory.setIgnoringElementContentWhitespace(true);      
           builder = factory.newDocumentBuilder();
           builder.setErrorHandler(new MyErrorHandler());
       }
       catch (FactoryConfigurationError e) {
           System.out.println("unable to get a document builder factory");
           System.exit(2);
       } 
       catch (ParserConfigurationException e) {
           System.out.println("parser was unable to be configured");
           System.exit(2);
       }
       
       /* Open all output csv files */
       try {
         item_file = new PrintWriter(new FileWriter("item.csv"));
         item_category_file = new PrintWriter(new FileWriter("item_category.csv"));
         item_bid_file = new PrintWriter(new FileWriter("item_bid.csv"));
         user_file = new PrintWriter(new FileWriter("user.csv"));
         bidder_file = new PrintWriter(new FileWriter("bidder.csv"));
         seller_file = new PrintWriter(new FileWriter("seller.csv"));
       } catch (IOException e) {
          System.out.println("Unable to create csv files!");
          System.exit(2);
       }
       
       /* Process all files listed on command line. */
       for (int i = 0; i < args.length; i++) {
           File currentFile = new File(args[i]);
           processFile(currentFile);
       }
       
       /* Close all output csv files */
       item_file.close();
       item_category_file.close();
       item_bid_file.close();
       user_file.close();
       bidder_file.close();
       seller_file.close();
   }
}