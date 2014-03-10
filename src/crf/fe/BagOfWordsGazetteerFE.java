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
import gazetteer.Gazetteer;
import gazetteer.SimpleGaz;

/**
 * @author rex
 *
 */
public class BagOfWordsGazetteerFE extends SpanGazetteerFE {
	
	static final long serialVersionUID=20140310L;
	SimpleGaz sGaz;
	String type;
	public BagOfWordsGazetteerFE(String type) throws IOException{
		this.sGaz = new SimpleGaz("hyer.txt", type);
		this.type = type;
	}
	@Override
	public void extractFeatures(TextLabels labels, Span s) {
		from(s).tokens().emit();
		try {
			from(s).tokens().eq().usewords(sGaz).emitGazetteer("Is.in.Gazetter." + type);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
