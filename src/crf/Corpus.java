/**
 * 
 */
package crf;

/**
 * Load corpus from files
 * 
 * @author rex
 *
 */

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import edu.cmu.minorthird.text.*;
public class Corpus {

	private MutableTextBase tb;
	private MutableTextLabels tl;
	
	 
	/**
	 * Load a labeled corpus without specified tokenizer(i.e. a default regex tokenizer will be
	 * used)
	 * 
	 * @param textDir
	 * @param labelDir
	 * @throws IOException
	 * @throws ParseException
	 */
	public Corpus (String textDir, String labelDir) throws IOException, ParseException {
		File textFile = new File(textDir);
		File labelFile = new File(labelDir);
		TextBaseLoader tbl = new TextBaseLoader(TextBaseLoader.DOC_PER_FILE);
		this.tb = tbl.load(textFile);
		TextLabelsLoader tll = new TextLabelsLoader();
		this.tl = tll.loadOps(tb, labelFile);
	}
	/**
	 * Load a labeld corpus using a specified tokenizer; in this project, it is recommended to use
	 * crf.TwitterTokenizer
	 * 
	 * @param textDir
	 * @param labelDir
	 * @param tok
	 * @throws IOException
	 * @throws ParseException
	 */
	public Corpus (String textDir, String labelDir, Tokenizer tok) throws IOException, ParseException {
		File textFile = new File(textDir);
		File labelFile = new File(labelDir);
		TextBaseLoader tbl = new TextBaseLoader(TextBaseLoader.DOC_PER_FILE);
		this.tb = tbl.load(textFile, tok);
		TextLabelsLoader tll = new TextLabelsLoader();
		this.tl = tll.loadOps(tb, labelFile);
	}
	/**
	 * Load an unlabeled corpus, using specified tokenizer
	 * 
	 * @param textDir
	 * @param tok
	 * @throws IOException
	 * @throws ParseException
	 */
	public Corpus (String textDir, Tokenizer tok) throws IOException, ParseException {
		File textFile = new File(textDir);
		TextBaseLoader tbl = new TextBaseLoader(TextBaseLoader.DOC_PER_FILE);
		this.tb = tbl.load(textFile, tok);
		this.tl = new BasicTextLabels(tb);
	}
	/**
	 * Load an unlabeled corpus without specifying a tokenizer
	 * 
	 * @param textDir
	 * @throws IOException
	 * @throws ParseException
	 */
	public Corpus (String textDir) throws IOException, ParseException {
		File textFile = new File(textDir);
		TextBaseLoader tbl = new TextBaseLoader(TextBaseLoader.DOC_PER_FILE);
		this.tb = tbl.load(textFile);
		this.tl = new BasicTextLabels(tb);
	}
	public MutableTextBase getTextbase() {
		return this.tb;
	}
	public MutableTextLabels getTextLabels() {
		return tl;
	}
	

}
