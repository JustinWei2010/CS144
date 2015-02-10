package edu.ucla.cs.cs144.index;

import java.io.IOException;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import edu.ucla.cs.cs144.DbManager;

public class Indexer {
   private static final String LUCENE_DIR = "/var/lib/lucene";
   private static final String INDEX_DIR = "/index1";
   private IndexWriter indexWriter = null;

   /** Creates a new instance of Indexer */
   public Indexer() {
   }

   //Create and initialize indexWriter in class private variable
   private IndexWriter getIndexWriter() throws IOException {
      if (indexWriter == null) {
         Directory indexDir = FSDirectory.open(new File(LUCENE_DIR + INDEX_DIR));
         IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_4_10_2, new StandardAnalyzer());
         indexWriter = new IndexWriter(indexDir, config);
      }
      return indexWriter;
   }

   private void closeIndexWriter() throws IOException {
      if (indexWriter != null) {
         indexWriter.close();
      }
   }

   public void rebuildIndexes() {
      Connection conn = null;

      // create a connection to the database to retrieve Items from MySQL
      try {
         conn = DbManager.getConnection(true);
      } catch (final SQLException ex) {
         System.out.println(ex);
      }

      /*
       * Add your code here to retrieve Items using the connection
       * and add corresponding entries to your Lucene inverted indexes.
       *
       * You will have to use JDBC API to retrieve MySQL data from Java.
       * Read our tutorial on JDBC if you do not know how to use JDBC.
       *
       * You will also have to use Lucene IndexWriter and Document
       * classes to create an index and populate it with Items data.
       * Read our tutorial on Lucene as well if you don't know how.
       *
       * As part of this development, you may want to add 
       * new methods and create additional Java classes. 
       * If you create new classes, make sure that
       * the classes become part of "edu.ucla.cs.cs144" package
       * and place your class source files at src/edu/ucla/cs/cs144/.
       * 
       */

      //Create index writer
      try {
         getIndexWriter();
      } catch (final IOException ex) {
         System.out.println(ex);
      }

      //Check for creating errors
      if (indexWriter == null) {
         System.out.println("There was an error while creating the indexWriter");
         System.exit(2);
      }

      //Create Lucene index for all items in table
      try {
         final Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery("SELECT item_id, name, description FROM item");
         PreparedStatement ps = conn.prepareStatement("SELECT category FROM item_category WHERE item_id = ?");
         while (rs.next()) {
            indexItem(rs, ps);
         }
      } catch (final SQLException | IOException ex) {
         ex.printStackTrace();
         System.out.println(ex);
      }

      // close the indexWriter and database connection
      try {
         closeIndexWriter();
         conn.close();
      } catch (final SQLException | IOException ex) {
         System.out.println(ex);
      }
   }    

   /*
    * Helper function which returns a string of category names pertaining to an item delimited by spaces.
    */
   private String getItemCategories(final Document doc, final PreparedStatement ps, String item_id) 
         throws SQLException {
      String categories = "";
      ps.setString(1, item_id);
      final ResultSet rs = ps.executeQuery();
      //Loop through each category and append name
      while (rs.next()) {
         categories += rs.getString("category") + " ";
      }
      return categories;
   }

   /*
    * Helper function which indexes item depending on the information retrieved from database.
    */
   private void indexItem(final ResultSet rs, final PreparedStatement ps) throws IOException, SQLException {
      final String item_id = rs.getString("item_id");
      final String name = rs.getString("name");
      final String description = rs.getString("description");
      final Document doc = new Document();

      //Add fields to document
      doc.add(new StringField("item_id", item_id, Field.Store.YES));
      doc.add(new StringField("name", name, Field.Store.YES));
      String fullSearchableText = name + " " + getItemCategories(doc, ps, item_id) + description;
      doc.add(new TextField("content", fullSearchableText, Field.Store.NO));

      //Write document to index
      indexWriter.addDocument(doc);
   }
}
