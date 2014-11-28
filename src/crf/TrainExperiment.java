/**
 * 
 */
package crf;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import crf.fe.*;
import edu.cmu.minorthird.text.Annotator;
import edu.cmu.minorthird.text.BasicSpan;
import edu.cmu.minorthird.text.BasicTextLabels;
import edu.cmu.minorthird.text.MonotonicTextLabels;
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
 * Run this class to see results of CRF classifier<br>
 * 
 * Configurations:<br>
 * The value of variable String label specifies which label to predict<br>
 * Change the value of variable String dir to "small" to use a smaller corpus which can
 * be trained faster.<br>
 * The second argument of TextLabelsExperiment decides how training data are splitted; <br>
 * use s for 70%-30% split, use cvs for 10-fold CV.<br>
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
		
		POSTagger.tag(corpus.getTextLabels());
		
		File f = new File("sample.labels");
		((BasicTextLabels) corpus.getTextLabels()).saveAs(f, "Minorthird TextLabels");
		System.exit(1);
		
		String option = "trainer ll";
		CRFLearner crf = new CRFLearner(option);
		//BagOfWordsWindowFE fe = new BagOfWordsWindowFE();
		
		//SampleFE.BagOfWordsFE fe = new SampleFE.BagOfWordsFE();
		
		Recommended.DocumentFE fe = new Recommended.DocumentFE();
		fe.setFoldCase(false);
		
		fe.setFeatureStoragePolicy(BagOfWordsGazetteerFE.STORE_AS_BINARY);
		SequenceAnnotatorLearner learner = new SequenceAnnotatorLearner(crf, fe);
		RandomSplitter s = new RandomSplitter(0.7);
		s.split(corpus.getTextLabels().instanceIterator(label));
		CrossValSplitter cvs = new CrossValSplitter(new Random(1000), 10);
		cvs.split(corpus.getTextLabels().instanceIterator(label));
		
		TextLabelsExperiment expt = new TextLabelsExperiment(
				corpus.getTextLabels(), 
				s,
				learner,
				label, label + "_prediction");
		expt.doExperiment();
		
		/*Annotator ann=learner.getAnnotator();
		Iterator<BasicSpan> i =s.getTest(1);
		while (i.hasNext()){
			String content = i.next().getDocumentContents();
			BasicTextLabels temp = new BasicTextLabels(content);
			ann.annotate(temp);
			System.out.println(content);
			Set<String> set = temp.getTokenProperties();
			for (String ss: set){
				Iterator<Span> j =temp.instanceIterator(ss);
				while (j.hasNext()){
					System.out.println(j.next().asString());
				}
			}
		}*/
		
	}

}
