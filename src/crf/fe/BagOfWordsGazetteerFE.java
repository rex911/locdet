/**
 * 
 */
package crf.fe;

import java.io.File;
import java.io.IOException;

import edu.cmu.minorthird.text.Span;
import edu.cmu.minorthird.text.TextLabels;
import edu.cmu.minorthird.text.learn.SampleFE;
import edu.cmu.minorthird.text.learn.SpanGazetteerFE;

/**
 * @author rex
 *
 */
public class BagOfWordsGazetteerFE extends SpanGazetteerFE {
	
	static final long serialVersionUID=20140310L;
	String fileName = "cities.txt";

	@Override
	public void extractFeatures(TextLabels labels, Span s) {
		from(s).tokens().emit();
		try {
			from(s).tokens().eq().usewords(fileName).emitGazetteer(fileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
