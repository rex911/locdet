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
 * @author rex
 * 
 * A gazetteer that stores information about a list of locations
 */
public class Gazetteer {
	public Hashtable<String, List<Location>> locations = new Hashtable<String, List<Location>>();
	
	public Gazetteer(String fileName) throws IOException {
		InputStream fis = new FileInputStream(fileName);
		BufferedReader br = new BufferedReader(new InputStreamReader(fis, Charset.forName("UTF-8")));
		String line;
		while ((line = br.readLine()) != null) {
		    // Deal with the line
			String[] parts = line.replace("\n", "").split("\t", 8);
			Location temp = new Location(parts[0], parts[1], parts[2], parts[3],
					parts[4], parts[5], parts[6]);
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
	public List<Location> get(String name){
		return locations.get(name.toLowerCase());
	}
}
