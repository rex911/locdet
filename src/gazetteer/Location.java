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
	public String id, name, type, lat, lon, population, line;
	public List<String> higher, alternate;
	public Location(String args) {
		this.line = args;
		String parts[] = args.split("\t", 8);
		this.id = parts[0];
		this.name = parts[1];
		this.type = parts[2];
		this.higher = Arrays.asList(parts[3].split("_"));
	    this.population = parts[4];
	    this.lat = parts[5];
	    this.lon = parts[6];
	    this.alternate = Arrays.asList(parts[7].split(","));
	}
	@Override
	public int compareTo(Location that) {
		// TODO Auto-generated method stub
		return this.name.compareTo(that.name);
	}
}
