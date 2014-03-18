/**
 * 
 */
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
public class POSGazetteerWindowFE extends SpanGazetteerFE implements Serializable{
	
	private static final long serialVersionUID = 20140318L;
    protected int windowSize=1;
    SimpleGaz sGaz;
	String type;
	
	public POSGazetteerWindowFE(String type) throws IOException{
		this.sGaz = new SimpleGaz("src/resources/hyer.txt", type);
		this.type = type;
	}
	
	public void setFeatureWindowSize(int n){
		windowSize=n;
	}

	public int getFeatureWindowSize(){
		return windowSize;
	}

	@Override
	public void extractFeatures(TextLabels labels, Span s) {
		from(s).tokens().prop("POS").emit();
		try {
			from(s).tokens().eq().usewords(sGaz).emitGazetteer("Is.in.Gazetter." + type);
		} catch (IOException e) {
			e.printStackTrace();
		}
		for(int i=0;i<windowSize;i++){
			from(s).left().token(-i-1).prop("POS").emit();
			from(s).right().token(i).prop("POS").emit();
		}

	}
}
