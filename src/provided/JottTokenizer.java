package provided;

/**
 * This class is responsible for tokenizing Jott code.
 * 
 * @author 
 **/

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class JottTokenizer {
  enum State {
    START, WAITINGFORNEWLINE, REL_OP, NUMBER, DECIMAL_NUMBER, IDENTIFIER, STRING, COLON
  }

  // @return character array
  public static ArrayList<Character> process_file(String filename) {
    ArrayList<Character> chars = new ArrayList<>();
    // TODO not sure why I have to change the file name. Will ask prof --Kaiy
    try (FileReader reader = new FileReader("test-cases/" + filename)) {
      BufferedReader buff_reader = new BufferedReader(reader);
      int ch = 0;
      while ((ch = buff_reader.read()) != -1) {
        chars.add((char) ch);
        System.out.print((char) ch);
      }
    } catch (IOException exception) {
      exception.printStackTrace();
      System.err.println("Error processing " + filename);
    }

    return chars;
  }

  /**
   * Takes in a filename and tokenizes that file into Tokens
   * based on the rules of the Jott Language
   * 
   * @param filename the name of the file to tokenize; can be relative or absolute
   *                 path
   * @return an ArrayList of Jott Tokens
   */
  public static ArrayList<Token> tokenize(String filename) {
    ArrayList<Character> chars = process_file(filename);
    ArrayList<Token> tokens = new ArrayList<>();

    int line_num = 1;
    State state = State.START;
    // For accumulating multi-character tokens like numbers and identifiers
    StringBuilder buffer = new StringBuilder();

    for (int i = 0; i < chars.size(); i++) {
      char ch = chars.get(i);
      switch (state) {
        case START:
          // Whitespace
          /*if (ch == ' ' || ch == '\t') {
            continue;
          } else*/ 
          if (ch == '\n') {
            line_num++;
          } else if (ch == '#') {
            state = State.WAITINGFORNEWLINE;
          }
          // All the single character tokens
          else if (ch == ',') {
            Token token = new Token(",", filename, line_num, TokenType.COMMA);
            tokens.add(token);
          } else if (ch == '[') {
            Token token = new Token("[", filename, line_num, TokenType.L_BRACKET);
            tokens.add(token);
          } else if (ch == ']') {
            Token token = new Token("]", filename, line_num, TokenType.R_BRACKET);
            tokens.add(token);
          } else if (ch == '{') {
            Token token = new Token("{", filename, line_num, TokenType.L_BRACE);
            tokens.add(token);
          } else if (ch == '}') {
            Token token = new Token("}", filename, line_num, TokenType.R_BRACE);
            tokens.add(token);
          } else if (ch == ';') {
            Token token = new Token(";", filename, line_num, TokenType.SEMICOLON);
            tokens.add(token);
          } else if (ch == ':') {
            buffer.append(ch);
            state = State.COLON;
            //Token token = new Token(":", filename, line_num, TokenType.COLON);
            //tokens.add(token);
          } else if (ch == '=' || ch == '<' || ch == '>' || ch == '!') {
            //Token token = new Token("=", filename, line_num, TokenType.ASSIGN);
            //tokens.add(token);
          //}
          // Relational operator or assignment
          //else if (ch == '=') {
            state = State.REL_OP;
            buffer.append(ch);
          } // add in <, >, !, etc. too?
          // Mathematical operators
          else if (ch == '+' || ch == '-' || ch == '*' || ch == '/') {
            Token token = new Token(String.valueOf(ch), filename, line_num, TokenType.MATH_OP);
            tokens.add(token);
          }
          // Numbers (start of number)
          else if (Character.isDigit(ch)) {
            state = State.NUMBER;
            buffer.append(ch);
          }
          // Number, but decimal has been found
          else if (ch == '.') {
            state = State.DECIMAL_NUMBER;
            buffer.append(ch);
          }
          // // Identifiers/keywords
          else if (Character.isLetter(ch)) {
            state = State.IDENTIFIER;
            buffer.append(ch);
          }
          // String literals
          else if (ch == '"') {
            state = State.STRING;
            buffer.append(ch);
          }
          break;

        case WAITINGFORNEWLINE:
          if (ch == '\n') {
            line_num++;
            state = State.START;
          }
          break;

        case REL_OP:
          if (ch == '=') {
            buffer.append(ch);
            tokens.add(new Token(buffer.toString(), filename, line_num, TokenType.REL_OP));  // Handles "=="
          } else if(buffer.charAt(0) == '=') {
            tokens.add(new Token(buffer.toString(), filename, line_num, TokenType.ASSIGN));  // Single '=' assignment
            i--;  // Retract since this char needs further processing
          } else if(buffer.charAt(0) != '!') {
            // < or >, both relOps
            tokens.add(new Token(buffer.toString(), filename, line_num, TokenType.REL_OP));
            i--;
          } else {
            // throw error, can't accept ! alone
          }
            buffer.setLength(0);  // Clear buffer
            state = State.START;
            break;

        case NUMBER:
          if (Character.isDigit(ch)) {
              buffer.append(ch);  // Continue collecting digits
          } else if (ch == '.') {
              buffer.append(ch);  // Start floating-point number
              state = State.DECIMAL_NUMBER;
          } else {
              tokens.add(new Token(buffer.toString(), filename, line_num, TokenType.NUMBER));
              buffer.setLength(0);  // Clear buffer
              i--;  // Retract since this char needs further processing
              state = State.START;
          }
          break;

          case DECIMAL_NUMBER:
            // special case for once the decimal in a number has been found
            if(Character.isDigit(ch)) {
              buffer.append(ch);
            } else if(buffer.length() > 1) {
              tokens.add(new Token(buffer.toString(), filename, line_num, TokenType.NUMBER));
              buffer.setLength(0);
              i--;
              state = State.START;
            } else {
              // error, can't have the period alone
            }
            break;

          case STRING:
            buffer.append(ch);  // Continue collecting the string literal
            if (ch == '"') {
                tokens.add(new Token(buffer.toString(), filename, line_num, TokenType.STRING));
                buffer.setLength(0);  // Clear buffer
                state = State.START;
            }
            break;
          case IDENTIFIER:
            if(Character.isLetter(ch) || Character.isDigit(ch)) {
              buffer.append(ch);
            } else {
              tokens.add(new Token(buffer.toString(), filename, line_num, TokenType.ID_KEYWORD));
              buffer.setLength(0);
              i--;
              state = State.START;
            }
            break;
          case COLON:
            if(ch == ':') {
              buffer.append(ch);
              tokens.add(new Token(buffer.toString(), filename, line_num, TokenType.FC_HEADER));
            } else {
              tokens.add(new Token(buffer.toString(), filename, line_num, TokenType.COLON));
              i--;
            }
            buffer.setLength(0);
            state = State.START;
            break;
      }

    }

    // handle token left at end
    if(buffer.length() > 0) {
      switch(state){
        case DECIMAL_NUMBER:
          // throw error if buffer size < 2
        case NUMBER:
          tokens.add(new Token(buffer.toString(), filename, line_num, TokenType.NUMBER));
          break;
        case STRING:
          if(buffer.length() < 2 || buffer.charAt(buffer.length() - 1) != '"') {
            // throw error
          } else {
            tokens.add(new Token(buffer.toString(), filename, line_num, TokenType.STRING));
          }
          break;
        case IDENTIFIER:
          tokens.add(new Token(buffer.toString(), filename, line_num, TokenType.ID_KEYWORD));
          break;
        case REL_OP:
          tokens.add(new Token(buffer.toString(), filename, line_num, TokenType.ASSIGN));
          break;
      }
    }

    return tokens;
  }
}
