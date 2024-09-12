package provided;

/**
 * This class is responsible for tokenizing Jott code.
 * 
 * @author 
 **/

import java.util.ArrayList;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

public class JottTokenizer {
  enum State {
    START, WAITINGFORNEWLINE, REL_OP, NUMBER, IDENTIFIER, STRING
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
          if (ch == ' ' || ch == '\t') {
            continue;
          } else if (ch == '\n') {
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
            Token token = new Token(":", filename, line_num, TokenType.COLON);
            tokens.add(token);
          } else if (ch == '=') {
            Token token = new Token("=", filename, line_num, TokenType.ASSIGN);
            tokens.add(token);
          }
          // Relational operator or assignment
          else if (ch == '=') {
            state = State.REL_OP;
            buffer.append(ch);
          } 
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
          // // Identifiers/keywords
          // else if (Character.isLetter(ch)) {
          //   state = State.IDENTIFIER;
          //   buffer.append(ch);
          // }
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
            buffer.setLength(0);  // Clear buffer
          } else {
            tokens.add(new Token("=", filename, line_num, TokenType.ASSIGN));  // Single '=' assignment
            i--;  // Retract since this char needs further processing
          }
            state = State.START;
            break;

        case NUMBER:
          if (Character.isDigit(ch)) {
              buffer.append(ch);  // Continue collecting digits
          } else if (ch == '.') {
              buffer.append(ch);  // Start floating-point number
          } else {
              tokens.add(new Token(buffer.toString(), filename, line_num, TokenType.NUMBER));
              buffer.setLength(0);  // Clear buffer
              i--;  // Retract since this char needs further processing
              state = State.START;
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
      }

    }

    return tokens;
  }
}
