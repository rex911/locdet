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
import java.util.List;
import java.util.TreeMap;



/**
 * @author rex
 * 
 * A gazetteer that stores information about a list of locations
 */
public class Gazetteer {
	public TreeMap<String, List<Location>> locations = new TreeMap();
	
	public Gazetteer(String fileName) throws IOException {
		InputStream fis = new FileInputStream(fileName);
		BufferedReader br = new BufferedReader(new InputStreamReader(fis, Charset.forName("UTF-8")));
		String line;
		while ((line = br.readLine()) != null) {
		    // Deal with the line
			Location temp = new Location(line.replace("\n", ""));
			if (!locations.containsKey(temp.name)) {
			    locations.put(temp.name, new ArrayList<Location>(Arrays.asList(temp)));
			}
			else {
				locations.get(temp.name).add(temp);
			}
		}
		// Done with the file
		br.close();
		br = null;
		fis = null;
	}
}
