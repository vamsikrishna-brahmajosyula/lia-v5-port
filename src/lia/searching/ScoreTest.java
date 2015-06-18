package lia.searching;

/**
 * Copyright Manning Publications Co.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific lan      
*/

import junit.framework.TestCase;

import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.FieldInvertState;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.CollectionStatistics;
import org.apache.lucene.search.Explanation;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.search.similarities.TFIDFSimilarity;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TermStatistics;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

import java.io.IOException;
import java.util.Vector;

// From chapter 3
public class ScoreTest extends TestCase {
  private Directory directory;

  public void setUp() throws Exception {
    directory = new RAMDirectory();
  }

  public void tearDown() throws Exception {
    directory.close();
  }

  public void testSimple() throws Exception {
    indexSingleFieldDocs(new Field[] {new StringField("contents", "x", Field.Store.YES)});
    IndexSearcher searcher = new IndexSearcher(DirectoryReader.open(directory));
    searcher.setSimilarity(new SimpleSimilarity());

    Query query = new TermQuery(new Term("contents", "x"));
    Explanation explanation = searcher.explain(query, 0);
    System.out.println(explanation);

    TopDocs matches = searcher.search(query, 10);
    assertEquals(1, matches.totalHits);

    assertEquals(1F, matches.scoreDocs[0].score, 0.0);

    
  }

  private void indexSingleFieldDocs(Field[] fields) throws Exception {
    IndexWriter writer = new IndexWriter(directory,
        new IndexWriterConfig(new WhitespaceAnalyzer()));
    for (Field f : fields) {
      Document doc = new Document();
      doc.add(f);
      writer.addDocument(doc);
    }
    
    writer.close();
  }

  public void testWildcard() throws Exception {
    indexSingleFieldDocs(new Field[]
      { new StringField("contents", "wild", Field.Store.YES),
        new StringField("contents", "child", Field.Store.YES),
        new StringField("contents", "mild", Field.Store.YES),
        new StringField("contents", "mildew", Field.Store.YES) });

    IndexSearcher searcher = new IndexSearcher(DirectoryReader.open(directory));
    Query query = new WildcardQuery(new Term("contents", "?ild*"));  //#A
    TopDocs matches = searcher.search(query, 10);
    assertEquals("child no match", 3, matches.totalHits);

    assertEquals("score the same", matches.scoreDocs[0].score,
                                   matches.scoreDocs[1].score, 0.0);
    assertEquals("score the same", matches.scoreDocs[1].score,
                                   matches.scoreDocs[2].score, 0.0);
    
  }
  /*
    #A Construct WildcardQuery using Term
  */

  public void testFuzzy() throws Exception {
    indexSingleFieldDocs(new Field[] { new TextField("contents",
                                                 "fuzzy",
                                                 Field.Store.YES),
                                       new TextField("contents",
                                                 "wuzzy",
                                                 Field.Store.YES)
                                     });

    IndexSearcher searcher = new IndexSearcher(DirectoryReader.open(directory));
    Query query = new FuzzyQuery(new Term("contents", "wuzza"));
    TopDocs matches = searcher.search(query, 10);
    assertEquals("both close enough", 2, matches.totalHits);

    assertTrue("wuzzy closer than fuzzy",
               matches.scoreDocs[0].score != matches.scoreDocs[1].score);

    Document doc = searcher.doc(matches.scoreDocs[0].doc);
    assertEquals("wuzza bear", "wuzzy", doc.get("contents"));
    
  }

  public static class SimpleSimilarity extends Similarity {
    public float lengthNorm(String field, int numTerms) {
      return 1.0f;
    }

    public float queryNorm(float sumOfSquaredWeights) {
      return 1.0f;
    }

    public float tf(float freq) {
      return freq;
    }

    public float sloppyFreq(int distance) {
      return 2.0f;
    }

//    public float idf(Vector terms, Searcher searcher) {
//      return 1.0f;
//    }

    public float idf(int docFreq, int numDocs) {
      return 1.0f;
    }

    public float coord(int overlap, int maxOverlap) {
      return 1.0f;
    }

	@Override
	public long computeNorm(FieldInvertState arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public SimWeight computeWeight(float arg0, CollectionStatistics arg1, TermStatistics... arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SimScorer simScorer(SimWeight arg0, LeafReaderContext arg1) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}
  }

}
