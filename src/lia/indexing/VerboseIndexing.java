package lia.indexing;

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

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;

import java.io.IOException;

// From chapter 2
public class VerboseIndexing {

  private void index() throws IOException {

    Directory dir = new RAMDirectory();
    IndexWriterConfig wconfig = new IndexWriterConfig(new WhitespaceAnalyzer());
    wconfig.setInfoStream(System.out);
   
    IndexWriter writer = new IndexWriter(dir, wconfig);

   

    for (int i = 0; i < 100; i++) {
      Document doc = new Document();
      doc.add(new StringField("keyword", "goober", Field.Store.YES));
      writer.addDocument(doc);
    }
    writer.close();
  }

  public static void main(String[] args) throws IOException {
    VerboseIndexing vi = new VerboseIndexing();
    vi.index();
  }
}
