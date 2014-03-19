/**
 * 
 */
package disambiguate;

import edu.cmu.minorthird.text.*;
import gazetteer.Gazetteer;
import gazetteer.Location;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Iterator;
import java.util.Set;

import crf.Corpus;
import crf.TwitterTokenizer;

/**
 * Evaluates the performance of disambiguator.
 * 
 * @author rex
 *
 */
public class TestDisambiguator {

	/**
	 * @param args
	 * @throws ParseException 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException, ParseException {
		Gazetteer gaz = new Gazetteer("src/resources/hyer.txt");
		Corpus corpus = new Corpus("src/resources/disam", "src/resources/disam.labels", new TwitterTokenizer());
		Iterator<Span> i = corpus.getTextLabels().instanceIterator("city");	
		int count = 0, count2 = 0;
		boolean[] active = {true, true, true, true};
		while (i.hasNext()){
			Span name = i.next();
			Location temp = GeoGeoDisambiguator.disambiguate(gaz, corpus.getTextLabels(), name, active);
			if (temp!=null  && corpus.getTextLabels().hasType(name, temp.id)) {
				count ++;
			} else {
				System.out.println(name.getDocumentContents());
				System.out.println(name.asString());
				if (temp!=null) System.out.println(temp.id);
			}
			count2 ++;
		}
		System.out.println("Accuracy: " + Float.toString((float) count / count2));
	}

}

	
