package lia.analysis;

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

import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.search.Query;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.util.Version;

import java.io.IOException;

// From chapter 4
public class UsingAnalyzersExample {
    /**
     * This method doesn't do anything, except compile correctly.
     * This is used to show snippets of how Analyzers are used.
     */
    public void someMethod() throws IOException, ParseException {
        RAMDirectory directory = new RAMDirectory();

        Analyzer analyzer = new StandardAnalyzer();
        IndexWriter writer = new IndexWriter(directory, new IndexWriterConfig( analyzer));

        Document doc = new Document();
        doc.add(new TextField("title", "This is the title", Field.Store.YES));
        doc.add(new TextField("contents", "...document contents...", Field.Store.NO));
        writer.addDocument(doc);

  
        String expression = "some query";

        Query query = new QueryParser("contents", analyzer)
                .parse(expression);

        QueryParser parser = new QueryParser("contents", analyzer);
        query = parser.parse(expression);
    }
}
