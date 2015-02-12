package edu.ucla.cs.cs144;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.ScoreDoc;

import edu.ucla.cs.cs144.DbManager;
import edu.ucla.cs.cs144.XMLConverter;

public class AuctionSearch implements IAuctionSearch {

   private Connection conn;

   public AuctionSearch () {
      try {
         this.conn = DbManager.getConnection(true);
      } catch (SQLException ex) {
         ex.printStackTrace();
      }
   }
   
   /* 
    * You will probably have to use JDBC to access MySQL data
    * Lucene IndexSearcher class to lookup Lucene index.
    * Read the corresponding tutorial to learn about how to use these.
    *
    * You may create helper functions or classes to simplify writing these
    * methods. Make sure that your helper functions are not public,
    * so that they are not exposed to outside of this class.
    *
    * Any new classes that you create should be part of
    * edu.ucla.cs.cs144 package and their source files should be
    * placed at src/edu/ucla/cs/cs144.
    *
    */

   public SearchResult[] basicSearch(final String query, final int numResultsToSkip, 
         final int numResultsToReturn) {
      final LinkedList<SearchResult> results = new LinkedList<SearchResult>();
      try {
         final SearchEngine se = new SearchEngine();
         final TopDocs topDocs = se.performSearch(query);
         final ScoreDoc[] hits = topDocs.scoreDocs;
         final int total = numResultsToReturn+numResultsToSkip;

         //Check if requested skip amount is greater than number of hits
         if (hits.length < numResultsToSkip) {
            System.err.println("Number of requested to skip is greater than total results returned!");
            System.exit(2);
         }

         //Add each search result to the array
         for (int i=numResultsToSkip; i < total; i++) {
            Document doc = se.getDocument(hits[i].doc);
            results.add(new SearchResult(doc.get("item_id"), doc.get("name")));
         }
         return results.toArray(new SearchResult[results.size()]);
      } catch (IOException | ParseException ex) {
         ex.printStackTrace();
         System.exit(2);
         return null;
      }
   }

   /*
    * Helper function to setup database connection and retrieve spatial query records
    */
   private ResultSet getSpatialQueryResults(final SearchRegion region) throws SQLException {
      final Statement stmt = this.conn.createStatement();
      final double lx = region.getLx(), ly = region.getLy(), rx = region.getRx(), ry = region.getRy();

      //Careful about the precision of the float from string formatter
      final String spatial_query = String.format("SELECT * FROM item_location WHERE "
            + "MBRContains(GeomFromText('Polygon((%f %f, %f %f, %f %f, %f %f, %f %f))'), coord)", 
            lx, ly, lx, ry, rx, ry, rx, ly, lx, ly);
      return stmt.executeQuery(spatial_query);
   }

   public SearchResult[] spatialSearch(final String query, final SearchRegion region,
         final int numResultsToSkip, final int numResultsToReturn) {
      //Get spatial index search results and add them to hashset
      final HashSet <String> basic_results = new HashSet <String>();
      try {
         ResultSet rs = getSpatialQueryResults(region);
         while (rs.next()) {
            basic_results.add(rs.getString("item_id"));
         }
      } catch (SQLException ex) {
         ex.printStackTrace();
         System.exit(2);
      }

      //Check keyword search results
      final LinkedList<SearchResult> results = new LinkedList<SearchResult>();
      try {
         final SearchEngine se = new SearchEngine();
         final TopDocs topDocs = se.performSearch(query);
         final ScoreDoc[] hits = topDocs.scoreDocs;

         //Check if requested skip amount is greater than number of hits
         if (hits.length < numResultsToSkip) {
            System.err.println("Number of requested to skip is greater than total results returned!");
            System.exit(2);
         }

         //Add each search result to the array
         int skip_count = 0;
         int total_results = 0;
         for (int i=0; i < hits.length; i++) {
            Document doc = se.getDocument(hits[i].doc);
            //Only take intersection of the two results
            if (basic_results.contains(doc.get("item_id"))) {
               if (skip_count < numResultsToSkip) {
                  skip_count++;
               } else if (total_results < numResultsToReturn){
                  results.add(new SearchResult(doc.get("item_id"), doc.get("name")));
                  total_results++;
               } else {
                  break;
               }
            }
         }
         return results.toArray(new SearchResult[results.size()]);
      } catch (IOException | ParseException ex) {
         ex.printStackTrace();
         System.exit(2);
         return null;
      }
   }

   public String getXMLDataForItemId(final String item_id) {
      XMLConverter converter = new XMLConverter(this.conn, item_id);
      return converter.getXML();
   }

   public String echo(final String message) {
      return message;
   }

   public void closeDBConnection() {
      try {
         this.conn.close();
      } catch (SQLException ex) {
         ex.printStackTrace();
      }
   }
   
}
