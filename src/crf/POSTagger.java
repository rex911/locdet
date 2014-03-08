/**
 * 
 */
package crf;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cmu.arktweetnlp.Tagger;
import cmu.arktweetnlp.Tagger.TaggedToken;
import edu.cmu.minorthird.text.BasicTextBase;
import edu.cmu.minorthird.text.MutableTextBase;
import edu.cmu.minorthird.text.MutableTextLabels;
import edu.cmu.minorthird.text.Span;
import edu.cmu.minorthird.text.TextBase;

/**
 * POS tag tweets using CMU's POS tagger (http://www.ark.cs.cmu.edu/TweetNLP/)
 * 
 * @author rex
 *
 */
public class POSTagger {
	
	public static void tag(MutableTextLabels labels) throws IOException {
		TextBase base = labels.getTextBase();
		Tagger tagger = new Tagger();
		tagger.loadModel("model.ritter_ptb_alldata_fixed.20130723.txt");
		for (Iterator<Span> i = base.documentSpanIterator();i.hasNext();){
			Span doc = i.next();
			List<String> tokens = new ArrayList<String>();
			for (int j = 0;j < doc.size(); j++) {
				tokens.add(doc.getTextToken(j).getValue());
			}
			
			List<TaggedToken> taggedTokens = tagger.justTag(tokens);
			for (int j = 0;j < doc.size(); j++) {
				labels.setProperty(doc.getToken(j), "POS", taggedTokens.get(j).tag);
			}
		}
	}
	
	public static void main(String args[]) throws IOException, ParseException {
		Corpus corpus = new Corpus("disam", "disam.labels", new TwitterTokenizer());
		MutableTextLabels labels = corpus.getTextLabels();
		tag(labels);
		/*TextBase base = labels.getTextBase();
		for (Iterator<Span> i = base.documentSpanIterator();i.hasNext();){
			Span doc = i.next();
			for (int j = 0;j < doc.size(); j++) {
				System.out.println(doc.getToken(j).getValue()+" "+labels.getProperty(doc.getToken(j), "POS"));
			}
			
			
		}*/
		System.out.println(labels.getTokenProperties());
	}

}
