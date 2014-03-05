/**
 * 
 */
package gazetteer;

import java.util.List;
import java.util.TreeMap;

/**
 * Alternate names of all locations are stored here
 * 
 * @author rex
 *
 */
public class AlternateNames {
	public TreeMap<String, List<String>> map;
	public AlternateNames(Gazetteer gaz) {
		for (List<Location> locList: gaz.locations.values()) {
			for (Location loc: locList) {
				if (loc.alternate.size() > 0) {
					map.put(loc.name, loc.alternate);
				}
			}
		}
	}
}
