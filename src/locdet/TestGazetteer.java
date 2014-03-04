/**
 * 
 */
package locdet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

import gazetteer.Gazetteer;
import gazetteer.Location;

/**
 * @author rex
 *
 */
public class TestGazetteer {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Gazetteer gaz = new Gazetteer("hyer.txt");
		PrintWriter out = new PrintWriter("sortedhyer.txt");
		for(Map.Entry<String,List<Location>> entry : gaz.locations.entrySet()) {
			  String key = entry.getKey();
			  List<Location> value = entry.getValue();
			  Iterator<Location> i = value.iterator();
			  while (i.hasNext()) {
			      out.println(i.next().line);
			  }
		}
		out.close();
		
	}

}
