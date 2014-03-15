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
		String dir = "src/resources/train";	
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
		SequenceAnnotatorLearner.SequenceAnnotator ann = (SequenceAnnotator) teacher.train(learner); 
		/*RandomSplitter s = new RandomSplitter(0.7);
		s.split(corpus.getTextLabels().instanceIterator(label));
		CrossValSplitter cvs = new CrossValSplitter(10);
		cvs.split(corpus.getTextLabels().instanceIterator(label));
		
		TextLabelsExperiment expt = new TextLabelsExperiment(
				corpus.getTextLabels(), 
				s,
				learner,
				label, label + "_prediction");
		expt.doExperiment();*/
		textDir = "src/resources/test";
		Corpus test = new Corpus(textDir, new TwitterTokenizer());
		MutableTextLabels labels = test.getTextLabels();
		POSTagger.tag(labels);
		ann.annotate(labels);
		for (Iterator<Span> i = test.getTextbase().documentSpanIterator();i.hasNext();){
			Span s = i.next();
			for (int j = 0; j< s.size(); j++) {
			if (!labels.getProperty(s.getToken(j), "_inside").equals("NEG")){
			//System.out.println(s.getDocumentContents());
			System.out.println(s.getToken(j).toString());
			}
			}
		}
	}

}
