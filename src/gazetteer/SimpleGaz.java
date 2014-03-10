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
import java.util.TreeSet;

/**
 * @author rex
 *
 */
public class SimpleGaz {
	public final TreeSet<String> locations = new TreeSet<String>();
	
	public SimpleGaz(String fileName) throws IOException {
		InputStream fis = new FileInputStream(fileName);
		BufferedReader in = new BufferedReader(new InputStreamReader(fis, Charset.forName("UTF-8")));
		String line;
		while((line=in.readLine())!=null){
			line=line.trim();
			locations.add(line.toLowerCase());
		}
		in.close();
	}
	
	public boolean contains(String location) {
		return locations.contains(location.toLowerCase());
	}
	
}
