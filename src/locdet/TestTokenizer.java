/**
 * 
 */
package locdet;

import edu.cmu.minorthird.text.*;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Iterator;

/**
 * @author rex
 *
 */
public class TestTokenizer {

	/**
	 * @param args
	 * @throws ParseException 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException, ParseException {
		// TODO Auto-generated method stub
		String s = "121549818 375221347464384514\nWed Sep 04 11:38:35 +0000 2013\nAustin,Texas\nno_location\nI've collected 4,536 gold coins! http://t.co/zthFmtXrZG #android, #androidgames, #gameinsight";
		Document document = new Document("1", s);
		Corpus corpus = new Corpus("train", "train.labels", new TwitterTokenizer());
		Iterator<Span> iter = corpus.getTextLabels().instanceIterator("Location");
		//Iterator<Span> iter = corpus.getTextbase().documentSpanIterator();
		while (iter.hasNext()){
			Span temp = iter.next();
		    if (temp.size() == 0){
		    //corpus.getTextbase().temp.getDocumentId());

		    System.out.println(temp.getDocumentContents());
		    
			
		}
		/*TextToken[] tt = new TwitterTokenizer().splitIntoTokens(document);
		for (int i=0; i<tt.length; i++){
			System.out.println(tt[i]);
		}*/
	}
	}
}

	
