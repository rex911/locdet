/**
 * 
 */
package crf.fe;

import edu.cmu.minorthird.text.Span;
import edu.cmu.minorthird.text.TextLabels;
import edu.cmu.minorthird.text.learn.SpanFE;

/**
 * @author rex
 *
 */
public class BagOfWordsPOSWindowFE extends SpanFE {
	
	private static final long serialVersionUID = 20140311L;
    protected int windowSize=1;
	
	public void setFeatureWindowSize(int n){
		windowSize=n;
	}

	public int getFeatureWindowSize(){
		return windowSize;
	}

	@Override
	public void extractFeatures(TextLabels labels, Span s) {
		from(s).tokens().emit();
		from(s).tokens().prop("POS").emit();
		for(int i=0;i<windowSize;i++){
			from(s).left().token(-i-1).eq().emit();
			from(s).right().token(i).eq().emit();
			from(s).left().token(-i-1).prop("POS").emit();
			from(s).right().token(i).prop("POS").emit();
		}

	}
}
