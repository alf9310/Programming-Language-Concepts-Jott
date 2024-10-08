package provided;

/**
 * This class is responsible for tokenizing Jott code.
 * 
 * @author Kaiy Muhammad, Sejal Bhattad, Lauren Kaestle, Audrey Fuller
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
    try (FileReader reader = new FileReader(filename)) {
      BufferedReader buff_reader = new BufferedReader(reader);
      int ch;
      while ((ch = buff_reader.read()) != -1) {
        chars.add((char) ch);
      }
    } catch (IOException exception) {
      exception.printStackTrace(System.err);
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
    try {
      for (int i = 0; i < chars.size(); i++) {
        char ch = chars.get(i);
        switch (state) {
          case START -> {
            buffer.setLength(0); // Clear buffer

            if (ch == '\n') {
              line_num++;
            } else if (ch == '#') {
              state = State.WAITINGFORNEWLINE;
            // All the single character tokens
            } else if (ch == ',') {
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
            } else if (ch == '=' || ch == '<' || ch == '>' || ch == '!') {
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
            // Number, but decimal has been found
            else if (ch == '.') {
              state = State.DECIMAL_NUMBER;
              buffer.append(ch);
            }
            // Identifiers/keywords
            else if (Character.isLetter(ch)) {
              state = State.IDENTIFIER;
              buffer.append(ch);
            }
            // String literals
            else if (ch == '"') {
              state = State.STRING;
              buffer.append(ch);
            } else if (!(ch == ' ' || ch == '\t' || ch == '\r' || ch == '\f')) {
              // if not whitespace, we don't recognize the char
              throw new Exception("Invalid character \"" + ch + "\".\n");
            }
          }

          case WAITINGFORNEWLINE -> {
            if (ch == '\n') {
              line_num++;
              state = State.START;
            }
          }

          case REL_OP -> {
            if (ch == '=') {
              buffer.append(ch);
              tokens.add(new Token(buffer.toString(), filename, line_num, TokenType.REL_OP)); // Handles "=="
            } else if (buffer.charAt(0) == '=') {
              tokens.add(new Token(buffer.toString(), filename, line_num, TokenType.ASSIGN)); // Single '=' assignment
              i--; // Retract since this char needs further processing
            } else if (buffer.charAt(0) != '!') {
              // < or >, both relOps
              tokens.add(new Token(buffer.toString(), filename, line_num, TokenType.REL_OP));
              i--;
            } else {
              // throw error, can't accept ! alone
              throw new Exception("Invalid token \"!\". \"!\" expects following \"=\"\n");
            }
            buffer.setLength(0); // Clear buffer
            state = State.START;
          }

          case NUMBER -> {
            if (Character.isDigit(ch)) {
              buffer.append(ch); // Continue collecting digits
            } else if (ch == '.') {
              buffer.append(ch); // Start floating-point number
              state = State.DECIMAL_NUMBER;
            } else {
              tokens.add(new Token(buffer.toString(), filename, line_num, TokenType.NUMBER));
              buffer.setLength(0); // Clear buffer
              i--; // Retract since this char needs further processing
              state = State.START;
            }
          }

          case DECIMAL_NUMBER -> {
            // special case for once the decimal in a number has been found
            if (Character.isDigit(ch)) {
              buffer.append(ch);
            } else if (buffer.length() > 1) {
              tokens.add(new Token(buffer.toString(), filename, line_num, TokenType.NUMBER));
              buffer.setLength(0);
              i--;
              state = State.START;
            } else {
              // error, can't have the period alone
              throw new Exception("Invalid number \"" + buffer.toString()
                  + "\". A decimal number must have at least one digit before or after the decimal\n");
            }
          }

          case STRING -> {
            if (ch == '"') {
              buffer.append(ch);
              tokens.add(new Token(buffer.toString(), filename, line_num, TokenType.STRING));
              buffer.setLength(0); // Clear buffer
              state = State.START;
            } else if (Character.isAlphabetic(ch) || Character.isDigit(ch) || ch == ' ' || ch == '\t' || ch == '\r' || ch == '\f') {
              buffer.append(ch);
            } else {
              throw new Exception("Invalid string " + buffer.toString() + ". A string must start and end with \"\n");
            }
          }

          case IDENTIFIER -> {
            if (Character.isLetter(ch) || Character.isDigit(ch)) {
              buffer.append(ch);
            } else {
              tokens.add(new Token(buffer.toString(), filename, line_num, TokenType.ID_KEYWORD));
              buffer.setLength(0);
              i--;
              state = State.START;
            }
          }

          case COLON -> {
            if (ch == ':') {
              buffer.append(ch);
              tokens.add(new Token(buffer.toString(), filename, line_num, TokenType.FC_HEADER));
            } else {
              tokens.add(new Token(buffer.toString(), filename, line_num, TokenType.COLON));
              i--;
            }
            buffer.setLength(0);
            state = State.START;
          }
        }
      }

      // handle token left at end
      if (buffer.length() > 0) {
        switch (state) {
          case DECIMAL_NUMBER -> {
            // throw error if buffer size < 2
            if (buffer.length() < 2) {
              throw new Exception("Invalid number \"" + buffer.toString()
                  + "\". A decimal number must have at least one digit before or after the decimal\n");
            } else {
              tokens.add(new Token(buffer.toString(), filename, line_num, TokenType.NUMBER)); // Ends with valid decimal
            }
          }

          case NUMBER -> {
            tokens.add(new Token(buffer.toString(), filename, line_num, TokenType.NUMBER));
          }

          case STRING -> {
            if (buffer.length() < 2 || buffer.charAt(buffer.length() - 1) != '"') {
              // throw error
              throw new Exception("Invalid string " + buffer.toString() + ". A string must start and end with \"\n");
            } else {
              tokens.add(new Token(buffer.toString(), filename, line_num, TokenType.STRING));
            }
          }

          case IDENTIFIER -> {
            tokens.add(new Token(buffer.toString(), filename, line_num, TokenType.ID_KEYWORD));
          }

          case REL_OP -> {
            if (buffer.charAt(0) == '!') {
              throw new Exception("Invalid token \"!\". \"!\" expects following \"=\"\n");
            } else {
              tokens.add(new Token(buffer.toString(), filename, line_num, TokenType.ASSIGN));
            }
          }

          case COLON -> {
            tokens.add(new Token(buffer.toString(), filename, line_num, TokenType.COLON));
          }

          default -> throw new IllegalArgumentException("Unexpected value: " + state);
        }
      }
      return tokens;

    } catch (Exception e) {
      System.err.println("Syntax Error\n" + e.getMessage() + filename + ":" + line_num);
      return null;
    }
  }
}
