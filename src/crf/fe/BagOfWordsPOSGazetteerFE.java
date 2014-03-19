package crf.fe;

import java.io.IOException;
import java.io.Serializable;

import edu.cmu.minorthird.text.Span;
import edu.cmu.minorthird.text.TextLabels;
import edu.cmu.minorthird.text.learn.SpanGazetteerFE;
import edu.cmu.minorthird.util.SimpleGaz;

/**
 * @author rex
 *
 */
public class BagOfWordsPOSGazetteerFE extends SpanGazetteerFE implements Serializable{
	static final long serialVersionUID=20140310L;
	SimpleGaz sGaz;
	String type;
	public BagOfWordsPOSGazetteerFE(String type) throws IOException{
		this.sGaz = new SimpleGaz("src/resources/hyer.txt", type);
		this.type = type;
	}
	@Override
	public void extractFeatures(TextLabels labels, Span s) {
		from(s).tokens().emit();
		from(s).tokens().prop("POS").emit();
		try {
			from(s).tokens().eq().usewords(sGaz).emitGazetteer("Is.in.Gazetter." + type);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
