/**
 * 
 */
package crf;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Iterator;

import edu.cmu.minorthird.text.BasicTextBase;
import edu.cmu.minorthird.text.BasicTextLabels;
import edu.cmu.minorthird.text.MutableTextLabels;
import edu.cmu.minorthird.text.Span;
import edu.cmu.minorthird.text.learn.SequenceAnnotatorLearner.SequenceAnnotator;
import edu.cmu.minorthird.util.IOUtil;

/**
 * Apply
 * 
 * @author rex
 *
 */
public class ApplyModel {
	public static SequenceAnnotator loadModel(String label) {
		SequenceAnnotator ann=null;
		try{
			ann=(SequenceAnnotator)IOUtil.loadSerialized(new File("src/resources/" + label + ".ann"));
		}catch(IOException ex){
			throw new IllegalArgumentException("Cannot load annotator "+label + ".ann");
		}
		return ann;
	}
	
	public static BasicTextLabels annotate(SequenceAnnotator ann, String[] text) {
		BasicTextBase base = new BasicTextBase(new TwitterTokenizer());
		for (int i = 0; i<text.length; i++) {
			base.loadDocument(Integer.toString(i), text[i]);
		}
		BasicTextLabels labels = new BasicTextLabels(base);
		ann.annotate(labels);
		return labels;
	}
	
	public static void main(String[] args) throws IOException, ParseException {
		SequenceAnnotator ann = loadModel("country");
		String[] text = {"How is Los Angeles", "I live in Ottawa, Ontario","None",
				"2014 Chengdu no_location is the best city",
				" Get to know UK slopestyle snowboarder @aimee_fuller on the road to @Sochi2014 http://t.co/II0VzLXCPO",
				" #Punjabi Podcast: Analyzing Canada's Olympic hockey roster http://t.co/oFJsOnYtAE Have a listen! @hockeynight @CBCOlympics #Sochi2014",
				 "HUGE congratulations to @trenni. Stay warm in Russia! Nastrovje! @NBCOlympics #Sochi2014"};
		BasicTextLabels labels = annotate(ann, text);
		String type = labels.getTypes().iterator().next();
		System.out.println(type);
		for (Iterator<Span> i =labels.instanceIterator(type);i.hasNext();) {
			Span s = i.next();
			System.out.println(s);
		}
	}

}
