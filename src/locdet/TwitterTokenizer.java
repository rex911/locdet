/**
 * 
 */
package locdet;

import java.util.ArrayList;
import java.util.List;

import edu.cmu.minorthird.text.Document;
import edu.cmu.minorthird.text.TextToken;
import edu.cmu.minorthird.text.Tokenizer;

/**
 * @author rex
 * A tokenizer subject to MinorThird standards, created from CMU's ARK tokenizer
 */ 
public class TwitterTokenizer implements Tokenizer {

	@Override
	public String[] splitIntoTokens(String string) {
		// TODO Auto-generated method stub
		List<String> temp = Twokenize.tokenize(string);
		return temp.toArray(new String[temp.size()]);
	}

	@Override
	public TextToken[] splitIntoTokens(Document document) {
		// TODO Auto-generated method stub
		List<TextToken> tokenList=new ArrayList<TextToken>();
		TextToken[] tokenArray;
		String documentText=document.getText();
		int currPos=0;
		List<String> temp = Twokenize.tokenize(documentText);
		String[] tokenValues = temp.toArray(new String[temp.size()]);
		for(int i=0;i<tokenValues.length;i++){
			if (tokenValues[i].length() == 0) {
				System.out.println(tokenValues);
			}
			// Skip upto the first char in the next token
			currPos=documentText.indexOf(tokenValues[i],currPos);
			// Create the token
			tokenList.add(new TextToken(document,currPos,tokenValues[i].length()));
			// Skip past the text in the token.
			currPos=currPos+tokenValues[i].length();
		}
		tokenArray=tokenList.toArray(new TextToken[tokenList.size()]);

		return tokenArray;
	}

}
