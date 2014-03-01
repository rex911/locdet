/**
 * 
 */
package locdet;

/**
 * @author rex
 *
 */

import edu.cmu.minorthird.text.Span;
import edu.cmu.minorthird.text.TextLabels;
import edu.cmu.minorthird.text.learn.*;

public class FeatureExtractor extends SpanFE{

	@Override
	public void extractFeatures(TextLabels labels, Span span) {
		// TODO Auto-generated method stub
		from(span).tokens().eq().emit();
	}

}
