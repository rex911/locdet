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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.TreeSet;

/**
 * @author rex
 *
 */
public class SimpleGaz {
	public final TreeSet<String> locations = new TreeSet<String>();
	
	public SimpleGaz(String fileName, String type) throws IOException {
		InputStream fis = new FileInputStream(fileName);
		BufferedReader br = new BufferedReader(new InputStreamReader(fis, Charset.forName("UTF-8")));
		String line;
		while ((line = br.readLine()) != null) {
		    // Deal with the line
			String[] parts = line.replace("\n", "").split("\t", 8);
			Location temp = new Location(parts[0], parts[1], parts[2], parts[3],
					parts[4], parts[5], parts[6]);
			if (type.equals(parts[2])) {
				if (!this.contains(parts[1])) this.add(parts[1]);
				// add alternate names
				/*for (String name: parts[7].split(",")){
					if (!this.contains(name)) {
						this.add(name);
					}
				}*/
			}
			
		}
		// Done with the file
		br.close();
		br = null;
		fis = null;
		
	}
	
	public boolean contains(String location) {
		return locations.contains(location);
	}
	
	public void add(String location) {
		locations.add(location);
	}
	
	public boolean containsLower(String location) {
		return locations.contains(location.toLowerCase());
	}
	
	public void addLower(String location) {
		locations.add(location.toLowerCase());
	}
	
	public static void main(String[] args) throws IOException{
		SimpleGaz sGaz = new SimpleGaz("hyer.txt", "city");
		System.out.println(sGaz.contains("Chengdu"));
	}
	
}
