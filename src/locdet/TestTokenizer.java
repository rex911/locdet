/**
 * 
 */
package locdet;

import disambiguate.GeoGeoDisambiguator;
import edu.cmu.minorthird.text.*;
import gazetteer.Gazetteer;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Iterator;
import java.util.Set;

/**
 * @author rex
 *
 */
public class TestTokenizer {

	/**
	 * @param args
	 * @throws ParseException 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException, ParseException {
		// TODO Auto-generated method stub
		String s = "121549818 375221347464384514\nWed Sep 04 11:38:35 +0000 2013\nAustin,Texas\nno_location\nI've collected 4,536 gold coins! http://t.co/zthFmtXrZG #android, #androidgames, #gameinsight";
		Document document = new Document("1", s);
		Corpus corpus = new Corpus("small", "small.labels", new TwitterTokenizer());
		Set<String> set = corpus.getTextLabels().getTokenProperties();
		System.out.println(set.size());
		Iterator<Span> i = corpus.getTextLabels().instanceIterator("city");
		Gazetteer gaz = new Gazetteer("hyer.txt");
		while (i.hasNext()){
			System.out.println(new GeoGeoDisambiguator().disambiguate(gaz, corpus.getTextLabels(), i.next()));
		}
	}
}

	
