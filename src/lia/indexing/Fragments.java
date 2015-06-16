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

import java.util.Date;
import java.util.Calendar;
import java.io.IOException;

import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.DoubleField;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.util.Version;

// From chapter 2

/** Just to test the code compiles. */
class Fragments {

  public static void indexNumbersMethod() {
    // START
    new StringField("size", "4096", Field.Store.YES);
    new StringField("price", "10.99", Field.Store.YES);
    new TextField("author", "Arthur C. Clark", Field.Store.YES);
    // END
  }

  public static final String COMPANY_DOMAIN = "example.com";
  public static final String BAD_DOMAIN = "yucky-domain.com";

  private String getSenderEmail() {
    return "bob@smith.com";
  }

  private String getSenderName() {
    return "Bob Smith";
  }

  private String getSenderDomain() {
    return COMPANY_DOMAIN;
  }

  private String getSubject() {
    return "Hi there Lisa";
  }

  private String getBody() {
    return "I don't have much to say";
  }

  private boolean isImportant(String lowerDomain) {
    return lowerDomain.endsWith(COMPANY_DOMAIN);
  }

  private boolean isUnimportant(String lowerDomain) {
    return lowerDomain.endsWith(BAD_DOMAIN);
  }

  public void ramDirExample() throws Exception {
    Analyzer analyzer = new WhitespaceAnalyzer();
    // START
    Directory ramDir = new RAMDirectory();
    IndexWriter writer = new IndexWriter(ramDir,new IndexWriterConfig( analyzer));
    // END
  }

  public void dirCopy() throws Exception {
    Directory otherDir = null;

    // START
    Directory ramDir = new RAMDirectory();
    // END
  }

  public void addIndexes() throws Exception {
    Directory otherDir = null;
    Directory ramDir = null;
    Analyzer analyzer = null;

    // START
    IndexWriter writer = new IndexWriter(otherDir, new IndexWriterConfig(analyzer));
    writer.addIndexes(new Directory[] {ramDir});
    // END
  }

  public void docBoostMethod() throws IOException {

    Directory dir = new RAMDirectory();
    IndexWriter writer = new IndexWriter(dir, new IndexWriterConfig( new StandardAnalyzer()));

    // START
    Document doc = new Document();
    String senderEmail = getSenderEmail();
    String senderName = getSenderName();
    String subject = getSubject();
    String body = getBody();
    doc.add(new StringField("senderEmail", senderEmail,
                      Field.Store.YES));
    doc.add(new StringField("senderName", senderName,
                      Field.Store.YES));
    doc.add(new TextField("subject", subject,
                      Field.Store.YES));
    doc.add(new TextField("body", body,
                      Field.Store.NO));
    String lowerDomain = getSenderDomain().toLowerCase();
    if (isImportant(lowerDomain)) {
      //doc.setBoost(1.5F); 
      //1
    } else if (isUnimportant(lowerDomain)) {
      //doc.setBoost(0.1F);    //2 
    }
    writer.addDocument(doc);
    // END
    writer.close();

    /*
      #1 Good domain boost factor: 1.5
      #2 Bad domain boost factor: 0.1
    */
  }

  public void fieldBoostMethod() throws IOException {

    String senderName = getSenderName();
    String subject = getSubject();

    // START
    Field subjectField = new TextField("subject", subject,
                                   Field.Store.YES);
    subjectField.setBoost(1.2F);
    // END
  }

  public void numberField() {
    Document doc = new Document();
    // START
    doc.add(new DoubleField("price", 19.99, Field.Store.YES));
    // END
  }

  public void numberTimestamp() {
    Document doc = new Document();
    // START
    doc.add(new LongField("timestamp",new Date().getTime(),Field.Store.YES));
    // END

    // START
    doc.add(new IntField("day", (int) (new Date().getTime()/24/3600), Field.Store.YES));
    // END

    Date date = new Date();
    // START
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    doc.add(new IntField("dayOfMonth",cal.get(Calendar.DAY_OF_MONTH), Field.Store.YES ));
            
    // END
  }

  public void setInfoStream() throws Exception {
    Directory dir = null;
    Analyzer analyzer = null;
    // START
    IndexWriter writer = new IndexWriter(dir,new IndexWriterConfig( analyzer).setInfoStream(System.out));
    
    // END
  }

  public void dateMethod() {
    Document doc = new Document();
    doc.add(new StringField("indexDate",
                      DateTools.dateToString(new Date(), DateTools.Resolution.DAY),
                      Field.Store.YES
                      ));
  }

  public void numericField() throws Exception {
    Document doc = new Document();
    DoubleField price = new DoubleField("price",19.99, Field.Store.YES);
    
    doc.add(price);

    LongField timestamp = new LongField("timestamp",new Date().getTime(),Field.Store.YES);
    
    doc.add(timestamp);

    Date b = new Date();
   
    String v = DateTools.dateToString(b, DateTools.Resolution.DAY);
    IntField birthday = new IntField("birthday", Integer.parseInt(v),Field.Store.YES);
    birthday.setIntValue(Integer.parseInt(v));
    doc.add(birthday);
  }

  public void indexAuthors() throws Exception {
    String[] authors = new String[] {"lisa", "tom"};
    // START
    Document doc = new Document();
    for (String author: authors) {
      doc.add(new StringField("author", author,
                        Field.Store.YES));
    }
    // END
  }
}

