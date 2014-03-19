/**
 * 
 */
package crf;

import java.io.File;
import java.io.IOException;
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
		SequenceAnnotator ann = loadModel("city");
		//ArrayList<String> text = new ArrayList<String>();
		Corpus corpus = new Corpus("src/resources/disam", new TwitterTokenizer());
		BasicTextLabels labels = (BasicTextLabels) corpus.getTextLabels();
		ann.annotate(labels);
		Gazetteer gaz = new Gazetteer("src/resources/hyer.txt");
		disambiguate(labels, gaz);
		for (Iterator<Span> i =labels.getSpansWithProperty("trueLoc");i.hasNext();) {
			Span s = i.next();
			System.out.println(s);
			Location loc = gaz.getByID(labels.getProperty(s, "trueLoc"));
			if (loc != null) {
				System.out.println(loc.name);
				for (String ss : loc.higher) {
					System.out.println(ss);
				}
			}
		}
		/*labels.saveAs(new File("src/resources/test.labels"), "Minorthird TextLabels");*/
	}
	

}
