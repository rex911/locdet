/**
 * 
 */
package locdet;

import disambiguate.GeoGeoDisambiguator;
import edu.cmu.minorthird.text.*;
import gazetteer.Gazetteer;
import gazetteer.Location;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Iterator;
import java.util.Set;

/**
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
		Corpus corpus = new Corpus("small", "small.labels", new TwitterTokenizer());
		Iterator<Span> i = corpus.getTextLabels().instanceIterator("city");
		Gazetteer gaz = new Gazetteer("hyer.txt");
		int count = 0, count2 = 0;
		while (i.hasNext()){
			Span name = i.next();
			Location temp = new GeoGeoDisambiguator().disambiguate(gaz, corpus.getTextLabels(), name);
			if (temp!=null) {
				count ++;
			} else System.out.println(name.asString());
			/*if (gaz.get(i.next().asString()) != null) count++;
			count2 ++;*/
		}
		System.out.println(count);
	}
}

	
