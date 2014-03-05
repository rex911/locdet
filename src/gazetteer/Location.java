/**
 * 
 */
package gazetteer;

import java.util.Arrays;
import java.util.List;

/**
 * @author rex
 *
 * the class that stores information of a certain location
 */
public class Location implements Comparable<Location>{
	public final String id, name, type, lat, lon, population;
	public final List<String> higher;
	public Location(String id, String name, String type, String higher, String population,
			String lat, String lon) {
		this.id = id;
		this.name = name;
		this.type = type;
		this.higher = Arrays.asList(higher.split("_"));
	    this.population = population;
	    this.lat = lat;
	    this.lon = lon;
	    //this.alternate = Arrays.asList(parts[7].split(","));
	}
	@Override
	public int compareTo(Location that) {
		return this.name.compareTo(that.name);
	}
}
