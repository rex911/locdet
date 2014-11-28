/**
 * 
 */
package crf;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Iterator;

import crf.fe.BagOfWordsGazetteerFE;
import crf.fe.BagOfWordsPOSGazetteerWindowFE;
import crf.fe.POSGazetteerWindowFE;
import edu.cmu.minorthird.classify.sequential.CRFLearner;
import edu.cmu.minorthird.text.MutableTextLabels;
import edu.cmu.minorthird.text.Span;
import edu.cmu.minorthird.text.learn.SequenceAnnotatorLearner;
import edu.cmu.minorthird.text.learn.TextLabelsAnnotatorTeacher;
import edu.cmu.minorthird.text.learn.SequenceAnnotatorLearner.SequenceAnnotator;
import edu.cmu.minorthird.util.IOUtil;

/**
 * Train annotators and save them as file.<br>
 * This class is no longer needed once annotators are trained, unless re-training is required.
 * 
 * @author rex
 *
 */
public class TrainModel {
	private static String resourceDir = "src/resources/";
	/**
	 * @param trainingSet the training set used; use "train"
	 * @param label the label used; use either "city", "SP" or "country"
	 * @throws IOException
	 * @throws ParseException
	 */
	public static void saveModel(String trainingSet, String label) throws IOException, ParseException {
		String dir = resourceDir + trainingSet;	
		String textDir = dir;
		String labelDir = dir + ".labels";
		//Corpus corpus = new Corpus(textDir, labelDir);
		Corpus corpus = new Corpus(textDir, labelDir, new TwitterTokenizer());
		
		TextLabelsAnnotatorTeacher teacher = new TextLabelsAnnotatorTeacher(corpus.getTextLabels(), label);
		POSTagger.tag(corpus.getTextLabels());
		String option = "trainer ll";
		CRFLearner crf = new CRFLearner(option);
		POSGazetteerWindowFE fe = new POSGazetteerWindowFE(label);
		fe.setFeatureWindowSize(3);
		fe.setFeatureStoragePolicy(BagOfWordsGazetteerFE.STORE_AS_BINARY);
		SequenceAnnotatorLearner learner = new SequenceAnnotatorLearner(crf, fe);
		learner.setAnnotationType(label);
		SequenceAnnotatorLearner.SequenceAnnotator ann = (SequenceAnnotator) teacher.train(learner); 
		String saveAs = resourceDir + label + "_nobow_t.ann";
		try{
			IOUtil.saveSerialized(ann,new File(saveAs));
		}catch(IOException e){
			throw new IllegalArgumentException("can't save to "+saveAs+": "+e);
		}
	}
	/**
	 * @param args
	 * @throws ParseException 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException, ParseException {
		String[] labels = {"city", "country", "SP"};
		for (String label : labels) {
			saveModel("train_t", label);
		}

	}

}
