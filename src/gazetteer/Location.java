/**
 * 
 */
package gazetteer;

import java.util.Arrays;
import java.util.List;

/**
 * the class that stores information of a certain location
 * 
 * @author rex
 *
 * 
 */
public class Location implements Comparable<Location>{
	/**
	 * a unique identifier of this location
	 */
	public final String id;
	/**
	 * the official name of this location
	 */
	public final String name;
	/**
	 * the type of this location; can be either "city", "SP", "country" or "continent"
	 */
	public final String type;
	/**
	 * latitude of this location; only available for cities.
	 */
	public final String lat;
	/**
	 * longitude of this location; only available for cities.
	 */
	public final String lon;
	/**
	 * population of this location; only available for cities.
	 */
	public final int population;
	/**
	 * higher political districts of this location.
	 */
	public final List<String> higher;
	public Location(String id, String name, String type, String higher, String population,
			String lat, String lon) {
		this.id = id;
		this.name = name;
		this.type = type;
		this.higher = Arrays.asList(higher.split("_"));
		int temp;
	    try {
			temp = Integer.parseInt(population);
		} catch (NumberFormatException e) {
			temp = 0;
		}
	    this.population = temp;
	    this.lat = lat;
	    this.lon = lon;
	    //this.alternate = Arrays.asList(parts[7].split(","));
	}
	@Override
	public int compareTo(Location that) {
		return this.population - that.population;
	}
	/**
	 * Calculate distance between two locations, based on their coordinates.
	 * 
	 * @param that the other location 
	 * @return distance measured by KM; Double.MAX_VALUE if unable to calculate
	 */
	public double distanceTo(Location that) {
		try {
			double a = Math.sin(Math.toRadians((Double.parseDouble(this.lat)-Double.parseDouble(that.lat))/2))
					*Math.sin(Math.toRadians((Double.parseDouble(this.lat)-Double.parseDouble(that.lat))/2))
					+ Math.cos(Math.toRadians(Double.parseDouble(this.lat)))*Math.cos(Math.toRadians(Double.parseDouble(that.lat)))
					*Math.sin(Math.toRadians((Double.parseDouble(this.lon)-Double.parseDouble(that.lon))/2))
					*Math.sin(Math.toRadians((Double.parseDouble(this.lon)-Double.parseDouble(that.lon))/2));
			return 6371 * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
		} catch (NumberFormatException e) {
			return Double.MAX_VALUE;
		}
	}
}
