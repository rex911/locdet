/**
 * 
 */
package locdet;

/**
 * @author rex
 *
 */

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import edu.cmu.minorthird.text.*;
public class Corpus {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws NumberFormatException 
	 * @throws ParseException 
	 */
	private MutableTextBase tb;
	private MutableTextLabels tl;
	
	// constructor. build the corpus from both 
	public Corpus (String textDir, String labelDir) throws IOException, ParseException {
		File textFile = new File(textDir);
		File labelFile = new File(labelDir);
		TextBaseLoader tbl = new TextBaseLoader(TextBaseLoader.DOC_PER_FILE);
		this.tb = tbl.load(textFile);
		TextLabelsLoader tll = new TextLabelsLoader();
		this.tl = tll.loadOps(tb, labelFile);
	}
	public Corpus (String textDir, String labelDir, Tokenizer tok) throws IOException, ParseException {
		File textFile = new File(textDir);
		File labelFile = new File(labelDir);
		TextBaseLoader tbl = new TextBaseLoader(TextBaseLoader.DOC_PER_FILE);
		this.tb = tbl.load(textFile, tok);
		TextLabelsLoader tll = new TextLabelsLoader();
		this.tl = tll.loadOps(tb, labelFile);
	}
	public Corpus (String textDir) throws IOException, ParseException {
		File textFile = new File(textDir);
		TextBaseLoader tbl = new TextBaseLoader(TextBaseLoader.DOC_PER_FILE);
		this.tb = tbl.load(textFile);
	}
	public MutableTextBase getTextbase() {
		return this.tb;
	}
	public MutableTextLabels getTextLabels() {
		return tl;
	}
	

}
