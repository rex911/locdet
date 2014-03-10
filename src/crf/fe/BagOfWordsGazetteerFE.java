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
import gazetteer.SimpleGaz;

/**
 * @author rex
 *
 */
public class BagOfWordsGazetteerFE extends SpanGazetteerFE {
	
	static final long serialVersionUID=20140310L;
	SimpleGaz sGaz;
	String fileName;
	public BagOfWordsGazetteerFE(String fileName) throws IOException{
		this.sGaz = new SimpleGaz(fileName);
		this.fileName = fileName;
	}
	@Override
	public void extractFeatures(TextLabels labels, Span s) {
		from(s).tokens().emit();
		try {
			from(s).tokens().eq().usewords(sGaz).emitGazetteer(fileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
