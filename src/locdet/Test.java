/**
 * 
 */
package locdet;

import java.io.IOException;
import java.text.ParseException;
import java.util.Iterator;

import edu.cmu.minorthird.text.*;
import edu.cmu.minorthird.text.learn.*;
import edu.cmu.minorthird.text.learn.SequenceAnnotatorLearner.SequenceAnnotator;
import edu.cmu.minorthird.text.learn.experiments.*;
import edu.cmu.minorthird.classify.Splitter;
import edu.cmu.minorthird.classify.experiments.CrossValSplitter;
import edu.cmu.minorthird.classify.experiments.RandomSplitter;
import edu.cmu.minorthird.classify.sequential.*;
import edu.cmu.minorthird.ui.*;

/**
 * @author rex
 *
 */
public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws NumberFormatException, IOException, ParseException {
		// TODO Auto-generated method stub
		String textDir = "train";	
		String labelDir = "train.labels";
		//Corpus corpus = new Corpus(textDir, labelDir);
		Corpus corpus = new Corpus(textDir, labelDir, new TwitterTokenizer());
		String label = "city";
		//TextLabelsAnnotatorTeacher teacher = new TextLabelsAnnotatorTeacher(corpus.getTextLabels(),
				//label);
				
		SequenceAnnotatorLearner learner = new SequenceAnnotatorLearner(new CRFLearner("trainer ll"), new Recommended.TokenFE());
		//SequenceAnnotatorLearner.SequenceAnnotator ann = (SequenceAnnotator) teacher.train(learner);
		RandomSplitter s = new RandomSplitter(0.7);
		CrossValSplitter cvs = new CrossValSplitter(5);
		cvs.split(corpus.getTextLabels().instanceIterator(label));
		s.split(corpus.getTextLabels().instanceIterator(label));
		TextLabelsExperiment expt = new TextLabelsExperiment(
				corpus.getTextLabels(), 
				cvs,
				learner,
				label, label + "_prediction");
		expt.doExperiment();
		
	}

}
