/**
 * 
 */
package crf;

import java.io.IOException;
import java.text.ParseException;

import crf.fe.BagOfWordsGazetteerFE;
import edu.cmu.minorthird.text.learn.*;
import edu.cmu.minorthird.text.learn.experiments.*;
import edu.cmu.minorthird.classify.experiments.CrossValSplitter;
import edu.cmu.minorthird.classify.experiments.RandomSplitter;
import edu.cmu.minorthird.classify.sequential.*;
import edu.cmu.minorthird.ui.*;

/**
 * Run this class to see results of CRF classifier
 * 
 * Configurations:
 * The value of variable String label specifies which label to predict
 * Change the value of variable String dir to "small" to use a smaller corpus which can
 * be trained faster.
 * The second argument of TextLabelsExperiment decides how training data are splitted; 
 * use s for 70%-30% split, use cvs for 5-fold CV.
 * 
 * @author rex
 *
 */
public class Train {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws NumberFormatException, IOException, ParseException {
		String dir = "train";	
		String label = "city";
		String textDir = dir;
		String labelDir = dir + ".labels";
		//Corpus corpus = new Corpus(textDir, labelDir);
		Corpus corpus = new Corpus(textDir, labelDir, new TwitterTokenizer());
		
		//TextLabelsAnnotatorTeacher teacher = new TextLabelsAnnotatorTeacher(corpus.getTextLabels(),
				//label);
		//new POSTagger().tag(corpus.getTextLabels());
		String option = "trainer ll";
		CRFLearner crf = new CRFLearner(option);
		//crf.setMaxIters(200);
		BagOfWordsGazetteerFE fe = new BagOfWordsGazetteerFE("city");
		fe.setFeatureStoragePolicy(BagOfWordsGazetteerFE.STORE_AS_BINARY);
		SequenceAnnotatorLearner learner = new SequenceAnnotatorLearner(crf, fe);
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
