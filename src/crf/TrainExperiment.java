/**
 * 
 */
package crf;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Iterator;
import java.util.Set;

import crf.fe.*;
import edu.cmu.minorthird.text.Annotator;
import edu.cmu.minorthird.text.BasicTextLabels;
import edu.cmu.minorthird.text.MutableTextLabels;
import edu.cmu.minorthird.text.Span;
import edu.cmu.minorthird.text.learn.*;
import edu.cmu.minorthird.text.learn.SequenceAnnotatorLearner.SequenceAnnotator;
import edu.cmu.minorthird.text.learn.experiments.*;
import edu.cmu.minorthird.classify.experiments.CrossValSplitter;
import edu.cmu.minorthird.classify.experiments.RandomSplitter;
import edu.cmu.minorthird.classify.sequential.*;
import edu.cmu.minorthird.ui.*;
import edu.cmu.minorthird.util.IOUtil;

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
public class TrainExperiment {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws NumberFormatException, IOException, ParseException {
		String dir = "src/resources/disam";	
		String label = "city";
		String textDir = dir;
		String labelDir = dir + ".labels";
		//Corpus corpus = new Corpus(textDir, labelDir);
		Corpus corpus = new Corpus(textDir, labelDir, new TwitterTokenizer());
		
		TextLabelsAnnotatorTeacher teacher = new TextLabelsAnnotatorTeacher(corpus.getTextLabels(), label);
		POSTagger.tag(corpus.getTextLabels());
		String option = "trainer ll";
		CRFLearner crf = new CRFLearner(option);
		BagOfWordsPOSGazetteerWindowFE fe = new BagOfWordsPOSGazetteerWindowFE(label);
		//fe.setFoldCase(false);
		fe.setFeatureStoragePolicy(BagOfWordsGazetteerFE.STORE_AS_BINARY);
		SequenceAnnotatorLearner learner = new SequenceAnnotatorLearner(crf, fe);
		RandomSplitter s = new RandomSplitter(0.7);
		s.split(corpus.getTextLabels().instanceIterator(label));
		CrossValSplitter cvs = new CrossValSplitter(10);
		cvs.split(corpus.getTextLabels().instanceIterator(label));
		
		TextLabelsExperiment expt = new TextLabelsExperiment(
				corpus.getTextLabels(), 
				cvs,
				learner,
				label, label + "_prediction");
		expt.doExperiment();
		
	}

}
