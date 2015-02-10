package edu.ucla.cs.cs144.search;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;
import java.util.Date;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.lucene.document.Document;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.ScoreDoc;

public class AuctionSearch implements IAuctionSearch {

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
      final SearchResult[] results = new SearchResult[numResultsToReturn];
      final int total = numResultsToReturn;
      int start = 0;
      if (numResultsToSkip > 0) {
         start = numResultsToSkip-1;
      }
      try {
         final SearchEngine se = new SearchEngine();
         final TopDocs topDocs = se.performSearch(query, total);
         final ScoreDoc[] hits = topDocs.scoreDocs;
         
         //Check that the total requested is less than total returned
         if (hits.length < total) {
            System.err.println("Number of requested results is greater than total results returned!");
            System.exit(2);
         }
         
         //Add each search result to the array
         for (int i = start; i < total; i++) {
            Document doc = se.getDocument(hits[i].doc);
            results[i] = new SearchResult(doc.get("item_id"), doc.get("name"));
         }
      } catch (IOException | ParseException ex) {
         ex.printStackTrace();
      }
      return results;
   }

   public SearchResult[] spatialSearch(String query, SearchRegion region,
         int numResultsToSkip, int numResultsToReturn) {
      // TODO: Your code here!
      return new SearchResult[0];
   }

   public String getXMLDataForItemId(String itemId) {
      // TODO: Your code here!
      return "";
   }

   public String echo(String message) {
      return message;
   }

}
