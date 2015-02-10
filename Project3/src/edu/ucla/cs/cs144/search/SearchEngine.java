package edu.ucla.cs.cs144.search;

import java.io.IOException;
import java.io.File;

import org.apache.lucene.document.Document;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.FSDirectory;

public class SearchEngine {
   private static final String LUCENE_DIR = "/var/lib/lucene";
   private static final String INDEX_DIR = "/index1";
   private IndexSearcher searcher = null;
   private QueryParser parser = null;

   /** Creates a new instance of SearchEngine */
   public SearchEngine() throws IOException {
      searcher = new IndexSearcher(DirectoryReader.open(FSDirectory.open(new File(LUCENE_DIR+INDEX_DIR))));
      parser = new QueryParser("content", new StandardAnalyzer());
   }

   public TopDocs performSearch(String queryString, int n)
         throws IOException, ParseException {
      Query query = parser.parse(queryString);
      return searcher.search(query, n);
   }

   public Document getDocument(int docId)
         throws IOException {
      return searcher.doc(docId);
   }
}