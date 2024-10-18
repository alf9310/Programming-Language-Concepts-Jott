package provided;
/**
 * This class is responsible for paring Jott Tokens
 * into a Jott parse tree.
 *
 * @author Kaiy Muhammad, Sejal Bhattad, Lauren Kaestle, Audrey Fuller 
 */

import java.util.ArrayList;
import nodes.ProgramNode;

public class JottParser {

  // For when the program errors on an empty token list, reference the final token
  public static Token finalToken; 

    /**
     * Parses an ArrayList of Jotton tokens into a Jott Parse Tree.
     * @param tokens the ArrayList of Jott tokens to parse
     * @return the root of the Jott Parse Tree represented by the tokens.
     *         or null upon an error in parsing.
     */
    public static JottTree parse(ArrayList<Token> tokens) throws Exception{
      if(!tokens.isEmpty()){
        finalToken = tokens.get(tokens.size() - 1);
      }

      return ProgramNode.parse(tokens);
    }
}
