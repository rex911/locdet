/**
 * 
 */
package gazetteer;

/**
 * @author rex
 *
 * the class that stores information of a certain location
 */
public class Location implements Comparable<Location>{
	public String name, type, lat, lon, population;
	public String[] higher;
	public Location(String args) {
		String parts[] = args.split("\t", 6);
		this.name = parts[0];
		this.type = parts[1];
		this.higher = parts[2].split("_");
	    this.population = parts[3];
	    this.lat = parts[4];
	    this.lon = parts[5];
	}
	@Override
	public int compareTo(Location that) {
		// TODO Auto-generated method stub
		return this.name.compareTo(that.name);
	}
}
