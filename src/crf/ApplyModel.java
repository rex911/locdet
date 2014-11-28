/**
 * 
 */
package crf;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;

import disambiguate.GeoGeoDisambiguator;
import edu.cmu.minorthird.text.BasicTextBase;
import edu.cmu.minorthird.text.BasicTextLabels;
import edu.cmu.minorthird.text.MutableTextLabels;
import edu.cmu.minorthird.text.Span;
import edu.cmu.minorthird.text.learn.SequenceAnnotatorLearner.SequenceAnnotator;
import edu.cmu.minorthird.util.IOUtil;
import gazetteer.Gazetteer;
import gazetteer.Location;

/**
 * Includes methods to apply saved annotators to text
 * 
 * @author rex
 *
 */
public class ApplyModel {
	
	static boolean[] active = {true, true, true, true};
	/**
	 * Change the activation of modules in GeoGeoDisambiguator. All modules are activated by default.<br>
	 * It is recommended to not make any changes.
	 * 
	 * @param n_active
	 */
	public static void setActive (boolean[] n_active){
		active = n_active;
	}
	
	/**
	 * Load a serialized annotator.  <br>
	 * 
	 * 
	 * @param label  limited to "city", "SP" and "country"
	 * @return the corresponding annotator
	 */
	public static SequenceAnnotator loadModel(String label) {
		SequenceAnnotator ann=null;
		try{
			ann=(SequenceAnnotator)IOUtil.loadSerialized(new File("src/resources/" + label + ".ann"));
		}catch(IOException ex){
			throw new IllegalArgumentException("Cannot load annotator "+label + ".ann");
		}
		return ann;
	}
	
	/**
	 * Annotate text
	 * 
	 * @param ann the annotator loaded by calling loadModel
	 * @param text an ArrayList of Strings; the number of Tweets in each element of the ArrayList should
	 * be small (less than 100).<br>It is recommended to put ONE Tweet as an element of the ArrayList, otherwise 
	 * the third module in GeoGeoDisambiguator won't work properly; in that case deactivate this module.
	 * @return
	 */
	public static BasicTextLabels annotate(SequenceAnnotator ann, ArrayList<String> text) {
		BasicTextBase base = new BasicTextBase(new TwitterTokenizer());
		for (int i = 0; i<text.size(); i++) {
			base.loadDocument(Integer.toString(i), text.get(i));
		}
		BasicTextLabels labels = new BasicTextLabels(base);
		ann.annotate(labels);
		return labels;
	}
	
	public static void disambiguate(BasicTextLabels labels, Gazetteer gaz) {
		String type = labels.getTypes().iterator().next();
		for (Iterator<Span> i =labels.instanceIterator(type);i.hasNext();) {
			Span s = i.next();
			Location loc = GeoGeoDisambiguator.disambiguate(gaz, labels, s, active);
			if (loc != null){
			    labels.setProperty(s, "trueLoc",loc.id);
			}
		}
		
	}
	
	public static void main(String[] args) throws IOException, ParseException {
		String lable = "city";
		SequenceAnnotator ann = loadModel(lable);
		ArrayList<String> text = new ArrayList<String>();
		String fileName = "src/resources/usa.text";
		InputStream fis = new FileInputStream(fileName );
		BufferedReader br = new BufferedReader(new InputStreamReader(fis, Charset.forName("UTF-8")));
		String line;
		while ((line = br.readLine()) != null) {
			text.add(line);
		}
		br.close();
		br = null;
		fis = null;
		/*Corpus corpus = new Corpus("src/resources/disam", new TwitterTokenizer());
		BasicTextLabels labels = (BasicTextLabels) corpus.getTextLabels();
		ann.annotate(labels);*/
		BasicTextLabels labels = annotate(ann, text);
		Gazetteer gaz = new Gazetteer("src/resources/hyer.txt");
		disambiguate(labels, gaz);
		for (Iterator<Span> i =labels.instanceIterator(lable);i.hasNext();) {
			Span s = i.next();
			String id = labels.getProperty(s, "trueLoc");
			if (id != null) {
				Location loc = gaz.getByID(id);

				if (loc != null) {
					System.out.println(loc.name);
					System.out.println(loc.lat + " " + loc.lon);

				}
			}
		}
		/*labels.saveAs(new File("src/resources/test.labels"), "Minorthird TextLabels");*/
	}
	

}
