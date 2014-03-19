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

	/**
	 * Call this method to do disambiguation.
	 * 
	 * @param gaz the loaded Gazetteer
	 * @param labels the MutableTextLabels used for disambiguation
	 * @param name the Span which corresponds to the location name requiring disambiguation
	 * @param active a boolean array, decides what modules shall be used for disambiguation
	 * @return an instance of Class Location or null, if disambiguation is not successful
	 */
	public static Location disambiguate(Gazetteer gaz, MutableTextLabels labels, Span name,
			boolean[] active) {
		
		List<Location> candi = gaz.get(name.asString());
		if (candi == null) return null;

		if (active[0]) candi = checkType(candi, labels, name);
		if (candi.size() == 0) return null;
		
		if (active[1]) {
			List<Location> tempCandi = checkAdjacent(gaz, candi, labels, name);
		if (tempCandi.size() == 1) 
			return tempCandi.get(0);
		if (tempCandi.size() > 1) candi = tempCandi;
		}
		if (active[2]) {
			Location tempLoc = checkContext(gaz, candi, labels, name);
			if (tempLoc != null) 
				return tempLoc;
		}
		return checkPopulation(candi);

	}
	
	/**
	 * Module one: checks if candidates' type is the same as the type being annotated previously.
	 * 
	 * @param candi
	 * @param labels
	 * @param name
	 * @return
	 */
	private static List<Location> checkType(List<Location> candi, MutableTextLabels labels, Span name) {
		List<Location> tempCandi = new ArrayList<Location>();
		for (Location loc : candi) {
			if (labels.hasType(name, loc.type)) {
				tempCandi.add(loc);
			}
		}
		return tempCandi;
	}
	
	/**
	 * <p>Module two: checks the adjacent context of the target location name; more specifically, checks if 
	 * succeeding token(s) is the higher political district containing the target location name.</p>
	 * <p>For example, if "Ottawa" is followed by "Ontario", only candidates with a higher political district named
	 * "Ontario" will be kept.</p>
	 * 
	 * <p>After this module, if there remains only 1 candidate, terminate disambiguation and return this candidate;
	 * otherwise, go to the next activated module</p>
	 * 
	 * @param gaz
	 * @param candi
	 * @param labels
	 * @param name
	 * @return
	 */
	private static List<Location> checkAdjacent(Gazetteer gaz, List<Location> candi,
			MutableTextLabels labels, Span name) {
		Span tweet = name.documentSpan();
		Iterator<Span> i = labels.instanceIterator("Location", tweet.getDocumentId());
		List<Location> tempCandi = new ArrayList<Location>(); //temporary candidates
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
		return tempCandi;
	}
	
	/**
	 * Module three: check the other locations (if any) mentioned in the same Tweet.
	 * Return the candidate with minimal distance total to those locations.
	 * 
	 * @param gaz
	 * @param candi
	 * @param labels
	 * @param name
	 * @return
	 */
	private static Location checkContext(Gazetteer gaz, List<Location> candi,
			MutableTextLabels labels, Span name) {
		Span tweet = name.documentSpan();
		Iterator<Span> i = labels.instanceIterator("city", tweet.getDocumentId());
		List<Location> contextLoc = new ArrayList<Location>();
		boolean[] active = {true, true, false, true};
		while (i.hasNext()) {
			Span context = i.next();
			// Exclude the location name itself
			if (context == name) continue;
			contextLoc.add(GeoGeoDisambiguator.disambiguate(gaz, labels, context, active));
		}
		if (contextLoc.size() == 0) return null;
		double minDist = Double.MAX_VALUE;
		double dist;
		Location ret = candi.get(0);
		for (Location loc : candi) {
			dist = 0;
			for (Location cLoc : contextLoc) {
				dist += loc.distanceTo(cLoc);
			}
			if (dist < minDist) {
				minDist = dist;
				ret = loc;
			}
		}
		return ret;
	}
	
	/**
	 * Module five: "last resort". If all previous modules failed, return the candidate with largest population.
	 * 
	 * @param candi
	 * @return
	 */
	private static Location checkPopulation(List<Location> candi) {
		Collections.sort(candi);
		Collections.reverse(candi);
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
