/**
 * 
 */
package gazetteer;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.TreeMap;



/**
 * A gazetteer that stores information about a list of locations
 * 
 * @author rex
 * 
 */
public class Gazetteer {
	public final Hashtable<String, List<Location>> locations = new Hashtable<String, List<Location>>();
	public final Hashtable<String, Location> ids = new Hashtable<String, Location>();
	
	/**
	 * @param fileName the directory to gazetteer file(i.e. hyer.txt)
	 * @throws IOException
	 */
	public Gazetteer(String fileName) throws IOException {
		InputStream fis = new FileInputStream(fileName);
		BufferedReader br = new BufferedReader(new InputStreamReader(fis, Charset.forName("UTF-8")));
		String line;
		while ((line = br.readLine()) != null) {
		    // Deal with the line
			String[] parts = line.replace("\n", "").split("\t", 8);
			Location temp = new Location(parts[0], parts[1], parts[2], parts[3],
					parts[4], parts[5], parts[6]);
			ids.put(temp.id, temp);
			if (!locations.containsKey(temp.name.toLowerCase())) {
			    locations.put(temp.name.toLowerCase(), new ArrayList<Location>(Arrays.asList(temp)));
			}
			else if (!locations.get(temp.name.toLowerCase()).contains(temp)){
				locations.get(temp.name.toLowerCase()).add(temp);
			}
			// add hashtable for alternate names
			for (String name: parts[7].split(",")){
				if (!locations.containsKey(name.toLowerCase())) {
				    locations.put(name.toLowerCase(), new ArrayList<Location>(Arrays.asList(temp)));
				}
				else if (!locations.get(name.toLowerCase()).contains(temp)){
					locations.get(name.toLowerCase()).add(temp);
				}
			}
		}
		// Done with the file
		br.close();
		br = null;
		fis = null;
	}
	/**
	 * Return a List of Locations matching a given name
	 * 
	 * @param name
	 * @return a List of Locations
	 */
	public List<Location> get(String name){
		return locations.get(name.toLowerCase());
	}
	/**
	 * Return a Location matching a given id
	 * 
	 * @param id
	 * @return a Location
	 */
	public Location getByID(String id) {
		return ids.get(id);
	}
	
}
