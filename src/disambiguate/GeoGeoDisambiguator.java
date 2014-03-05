/**
 * 
 */
package disambiguate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import edu.cmu.minorthird.text.BasicTextLabels;
import edu.cmu.minorthird.text.MutableTextLabels;
import edu.cmu.minorthird.text.Span;
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
		
		List<Location> candi = gaz.locations.get(name.asString().toLowerCase());
		if (candi == null){
		    return null;
		}
		else if (candi.size() == 1){
			return candi.get(0);
		}
		else {
			return infer(candi, labels, name);
		}
	}
	// from a list of locations, select the most plausible one given the context
	public static Location infer(List<Location> candi, 
			MutableTextLabels labels, Span name) {
		List<Location> tempCandi = new ArrayList<Location>();
		for (Location loc : candi) {
			if (labels.hasType(name, loc.type)) {
				tempCandi.add(loc);
			}
		}
		if (tempCandi.size() == 1) return tempCandi.get(0);
		if (tempCandi.size() == 0) return null;
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
						if (loc.higher.contains(j)){
							tempCandi.add(loc);
							break;
						}
					}
				}
				break;
			}
		}
		if (tempCandi.size() == 1) return tempCandi.get(0);
		return null;
	}
}
