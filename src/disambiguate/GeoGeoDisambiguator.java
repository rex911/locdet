/**
 * 
 */
package disambiguate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import edu.cmu.minorthird.text.BasicTextLabels;
import edu.cmu.minorthird.text.MutableTextLabels;
import edu.cmu.minorthird.text.Span;
import edu.cmu.minorthird.text.TextToken;
import edu.cmu.minorthird.text.Token;
import gazetteer.Gazetteer;
import gazetteer.Location;

/**
 * Disambiguate a given location name, i.e. decide the true locations mentioned in 
 * a document of tweet
 * 
 * @author rex
 *
 */
public class GeoGeoDisambiguator {

	public static Location disambiguate(Gazetteer gaz, MutableTextLabels labels, Span name) {
		
		List<Location> candi = gaz.get(name.asString());
		if (candi == null){
		    return null;
		}
		else if (candi.size() == 1){
			return candi.get(0);
		}
		else {
			return infer(gaz, candi, labels, name);
		}
	}
	// from a list of locations, select the most plausible one given the context
	private static Location infer(Gazetteer gaz, List<Location> candi, 
			MutableTextLabels labels, Span name) {
		// phase 1: type matching
		List<Location> tempCandi = new ArrayList<Location>();
		for (Location loc : candi) {
			if (labels.hasType(name, loc.type)) {
				tempCandi.add(loc);
			}
		}
		if (tempCandi.size() == 1) return tempCandi.get(0);
		if (tempCandi.size() == 0) return null;
		
		//phase 2: adjacent context
		candi = tempCandi;
		Span tweet = name.documentSpan();
		Iterator<Span> i = labels.instanceIterator("Location", tweet.getDocumentId());
		tempCandi = new ArrayList<Location>(); //temporary candidates
		while (i.hasNext()){
			Span adjacent = i.next();
			// try to disambiguate using adjacent context
			if (adjacent.contains(name) && name.getHiChar()+1 < adjacent.getHiChar()) {
				Span higher = tweet.charIndexProperSubSpan(name.getHiChar()+1, adjacent.getHiChar());
				for (Location loc : candi){
					for (int j = 0; j<higher.size(); j++) {
						if (contains(gaz, loc.higher, higher.getTextToken(j))){
							
							tempCandi.add(loc);
							break;
						}
					}
				}
			}
			break;
		}
		if (tempCandi.size() == 1) return tempCandi.get(0);
		
		// phase 3: whole context of the tweet
		i = labels.instanceIterator("Location", tweet.getDocumentId());
		tempCandi = new ArrayList<Location>();
		while (i.hasNext()) {
			Span context = i.next();
		}
		
		// phase 4: 
		
		// phase 5: choose the most populated location
		Collections.sort(candi);
		return candi.get(0);
	}
	private static boolean contains(Gazetteer gaz, List<String> higher, TextToken name){
		List<Location> list = gaz.get(name.getValue());
		if (list == null) return false;
		for (Location loc : list) {
			if (higher.contains(loc.name))
				return true;
		}
		return false;
	}
}
