/**
 * 
 */
package gazetteer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.List;
import java.util.TreeSet;

/**
 * @author rex
 *
 */
public class SimpleGaz {
	public final TreeSet<String> locations = new TreeSet<String>();
	
	public SimpleGaz(Gazetteer gaz, String type) throws IOException {
		Enumeration<String> enumKey = gaz.locations.keys();
		while(enumKey.hasMoreElements()) {
		    String key = enumKey.nextElement();
		    List<Location> val = gaz.locations.get(key);
		    for (Location loc : val) {
		    	if (loc.type.equals(type) && !this.locations.contains(key.toLowerCase())){
		    		this.locations.add(key.toLowerCase());
		    	}
		    }
		}
	}
	
	public boolean contains(String location) {
		return locations.contains(location.toLowerCase());
	}
	
	public static void main(String[] args) throws IOException{
		SimpleGaz sGaz = new SimpleGaz(new Gazetteer("hyer.txt"), "city");
		System.out.println(sGaz.contains("Chengdu"));
	}
	
}
