package nodes;

import errors.SyntaxError;
import java.util.ArrayList;
import java.util.HashMap;
import msc.*;
import provided.JottParser;
import provided.JottTree;
import provided.Token;
import provided.TokenType;

/*
 * Expression Node
 * <operand> | <operand> <relop> <operand> | 
 * <operand> <mathop> <operand> | <string_literal> |
 * <bool>
 */
public interface ExpressionNode extends JottTree {

    // Determine if the node is BooleanNode, StringLiteralNode, or
    // BinaryOpNode(RelOp & MathOp).
    // Returns the proper node type that you created above.
    public static ExpressionNode parse(ArrayList<Token> tokens) throws Exception {

        // Check if there is tokens
        if (tokens.isEmpty()) {
            throw new SyntaxError("Expected Operand, string literal or boolean got " + JottParser.finalToken.getToken(),
                    JottParser.finalToken);
        }
        
        // < bool >
        if (tokens.get(0).getToken().equals("True") || tokens.get(0).getToken().equals("False")) {  
            return BooleanNode.parse(tokens);
        }
        // <operand> | <operand> <relop> <operand> | <operand> <mathop> <operand>
         else if (isOperand(tokens.get(0))) {

            if (tokens.size() > 1) {
                // -num operand
                if (tokens.get(0).getTokenType() == TokenType.MATH_OP && tokens.get(0).getToken().equals("-")
                        && tokens.get(1).getTokenType() == TokenType.NUMBER) {
                    if (tokens.size() > 3) {
                        Token op = tokens.get(2);
                        if (op.getTokenType() == TokenType.REL_OP || op.getTokenType() == TokenType.MATH_OP) {
                            return BinaryOpNode.parse(tokens);
                        }
                    }
                } else if (tokens.get(0).getTokenType() == TokenType.FC_HEADER) {
                    int index = 1;
                    while (index < tokens.size()) {
                        if (tokens.get(index).getTokenType() == TokenType.R_BRACKET) {
                            break;
                        }
                        index++;
                    }
                    // number of tokens leftover after the func call
                    int leftover = tokens.size() - index;
                    if (leftover > 1) {
                        Token op = tokens.get(index + 1);
                        if (op.getTokenType() == TokenType.REL_OP || op.getTokenType() == TokenType.MATH_OP) {
                            return BinaryOpNode.parse(tokens);
                        }
                    }
                } else if (tokens.size() > 2) {
                    // Check if the next token is <relop> or <mathop>
                    Token op = tokens.get(1);
                    if (op.getTokenType() == TokenType.REL_OP || op.getTokenType() == TokenType.MATH_OP) {
                        return BinaryOpNode.parse(tokens);
                    }
                }
            }

            // <operand>
            return OperandNode.parse(tokens);

            // < string_literal >
        } else if (tokens.get(0).getTokenType() == TokenType.STRING) {
            return StringLiteralNode.parse(tokens);
        }

        throw new SyntaxError("Unexpected token " + tokens.get(0).getToken() + " after expression", tokens.get(0));
    }

    // Helper method to determine if a token is an operand (<id> | <num> |
    // <func_call> | -<num>)
    // Return true if it is
    static boolean isOperand(Token token) {
        TokenType type = token.getTokenType();
        // Check for valid operand types
        if (type == TokenType.ID_KEYWORD) {
            return true;
        } else if (type == TokenType.NUMBER) {
            return true;
        } else if (type == TokenType.FC_HEADER) {
            return true;
        } else if (type == TokenType.MATH_OP && token.getToken().equals("-")) {
            return true;
        }
        return false;
    }

    @Override
    public boolean validateTree(SymbolTable symbolTable) throws Exception;

    @Override
    public Object execute(SymbolTable symbolTable);

    public DataType getType(SymbolTable symbolTable) throws Exception;

    public Token getToken();

    public static void main(String[] args) {
        System.out.println("Testing ExpressionNode Main Method");
        // Validate tree tests
        testValidateTreeSimpleOperand();
        testValidateTreeInvalidOperand();
        testValidateTreeMathOperation();
        testValidateTreeUndefinedVariable();
        testValidateTreeBooleanLiteral();
    }
    
    private static void testValidateTreeSimpleOperand() {
        try {
            ArrayList<Token> tokens = new ArrayList<>();
            tokens.add(new Token("x", "testFile.jott", 1, TokenType.ID_KEYWORD));
    
            SymbolTable symbolTable = new SymbolTable();
            symbolTable.addFunction("main", new FunctionInfo("main", "void", new HashMap<>()));
            symbolTable.enterScope("main");
            symbolTable.addVar(new VarInfo("x", DataType.INTEGER, null));
    
            ExpressionNode result = ExpressionNode.parse(tokens);
            boolean isValid = result.validateTree(symbolTable);
            System.out.println("Test Validate Tree 1 (Simple Operand): " + isValid);
        } catch (Exception e) {
            System.err.println("!FAILED TEST! Test Validate Tree 1 Failed: " + e.getMessage());
        }
    }
    
    private static void testValidateTreeInvalidOperand() {
        try {
            ArrayList<Token> tokens = new ArrayList<>();
            tokens.add(new Token("y", "testFile.jott", 2, TokenType.ID_KEYWORD));
    
            SymbolTable symbolTable = new SymbolTable();
            symbolTable.addFunction("main", new FunctionInfo("main", "void", new HashMap<>()));
            symbolTable.enterScope("main");
            // "y" is not added to the symbol table intentionally (it's not declared)
    
            ExpressionNode result = ExpressionNode.parse(tokens);
            boolean isValid = result.validateTree(symbolTable);
            System.out.println("!FAILED TEST! Test Validate Tree 2 (Invalid Operand): " + isValid);
        } catch (Exception e) {
            System.err.println("Test Validate Tree 2 Passed (Expected Error): " + e.getMessage());
        }
    }
    
    private static void testValidateTreeMathOperation() {
        try {
            ArrayList<Token> tokens = new ArrayList<>();
            tokens.add(new Token("x", "testFile.jott", 3, TokenType.ID_KEYWORD));
            tokens.add(new Token("+", "testFile.jott", 3, TokenType.MATH_OP));
            tokens.add(new Token("3", "testFile.jott", 3, TokenType.NUMBER));
    
            SymbolTable symbolTable = new SymbolTable();
            symbolTable.addFunction("main", new FunctionInfo("main", "void", new HashMap<>()));
            symbolTable.enterScope("main");
            symbolTable.addVar(new VarInfo("x", DataType.INTEGER, null));
    
            ExpressionNode result = ExpressionNode.parse(tokens);
            boolean isValid = result.validateTree(symbolTable);
            System.out.println("Test Validate Tree 3 (Math Operation): " + isValid);
        } catch (Exception e) {
            System.err.println("!FAILED TEST! Test Validate Tree 3 Failed: " + e.getMessage());
        }
    }
    
    private static void testValidateTreeUndefinedVariable() {
        try {
            ArrayList<Token> tokens = new ArrayList<>();
            tokens.add(new Token("y", "testFile.jott", 4, TokenType.ID_KEYWORD));
            tokens.add(new Token("-", "testFile.jott", 4, TokenType.MATH_OP));
            tokens.add(new Token("3", "testFile.jott", 4, TokenType.NUMBER));
    
            SymbolTable symbolTable = new SymbolTable();
            symbolTable.addFunction("main", new FunctionInfo("main", "void", new HashMap<>()));
            symbolTable.enterScope("main");
            // "y" is not added to the symbol table intentionally
    
            ExpressionNode result = ExpressionNode.parse(tokens);
            boolean isValid = result.validateTree(symbolTable);
            System.out.println("!FAILED TEST! Test Validate Tree 4 (Undefined Variable): " + isValid);
        } catch (Exception e) {
            System.err.println("Test Validate Tree 4 Passed (Expected Error): " + e.getMessage());
        }
    }
    
    private static void testValidateTreeBooleanLiteral() {
        try {
            ArrayList<Token> tokens = new ArrayList<>();
            tokens.add(new Token("True", "testFile.jott", 5, TokenType.ID_KEYWORD));
    
            SymbolTable symbolTable = new SymbolTable();
            symbolTable.addFunction("main", new FunctionInfo("main", "void", new HashMap<>()));
            symbolTable.enterScope("main");
    
            ExpressionNode result = ExpressionNode.parse(tokens);
            boolean isValid = result.validateTree(symbolTable);
            System.out.println("Test Validate Tree 5 (Boolean Literal): " + isValid);
        } catch (Exception e) {
            System.err.println("!FAILED TEST! Test Validate Tree 5 Failed: " + e.getMessage());
        }
    }
}