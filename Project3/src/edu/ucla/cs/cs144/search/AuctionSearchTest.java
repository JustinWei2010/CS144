package edu.ucla.cs.cs144.search;

import edu.ucla.cs.cs144.index.Indexer;

public class AuctionSearchTest {
   public static void main(String[] args1)
   {
      Indexer idx = new Indexer();
      idx.rebuildIndexes();
      
      AuctionSearch as = new AuctionSearch();

      String message = "Test message";
      String reply = as.echo(message);
      System.out.println("Reply: " + reply);

      String query = "superman";
      SearchResult[] basicResults = as.basicSearch(query, 0, 20);
      System.out.println("Basic Seacrh Query: " + query);
      System.out.println("Received " + basicResults.length + " results");
      for(SearchResult result : basicResults) {
         System.out.println(result.getItemId() + ": " + result.getName());
      }

      SearchRegion region =
            new SearchRegion(33.774, -118.63, 34.201, -117.38); 
      SearchResult[] spatialResults = as.spatialSearch("camera", region, 0, 20);
      System.out.println("Spatial Search");
      System.out.println("Received " + spatialResults.length + " results");
      for(SearchResult result : spatialResults) {
         System.out.println(result.getItemId() + ": " + result.getName());
      }

      String itemId = "1497595357";
      String item = as.getXMLDataForItemId(itemId);
      System.out.println("XML data for ItemId: " + itemId);
      System.out.println(item);

      // Add your own test here
   }
}
