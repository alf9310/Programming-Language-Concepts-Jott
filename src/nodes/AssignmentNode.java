package nodes;

import errors.SemanticError;
import errors.SyntaxError;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import msc.*;
import provided.JottParser;
import provided.Token;
import provided.TokenType;

/*
 * Assignment Node
 * <id> = <expr>;
 */
public class AssignmentNode implements BodyStmtNode {

    IDNode id;
    ExpressionNode expression;

    public AssignmentNode(IDNode id, ExpressionNode expression) {
        this.id = id;
        this.expression = expression;
    }

    @Override
    public boolean allReturn() {
        return false;
    }

    @Override
    public DataType getReturnType() {
        return null;
    }

    // Parsing
    public static AssignmentNode parse(ArrayList<Token> tokens) throws Exception {
        // Check if there is tokens
        if (tokens.isEmpty()) {
            throw new SyntaxError("Expected " + TokenType.ID_KEYWORD + " got " + JottParser.finalToken.getToken(),
                    JottParser.finalToken);
        }
        // Check that there are at least 4 tokens
        if (tokens.size() < 4) {
            throw new SyntaxError("Expected at least 4 tokens for Assignment", JottParser.finalToken);
        }

        // Parse the ID
        IDNode id = IDNode.parse(tokens);
        // Parse assignment operator
        Token assignment = tokens.remove(0);
        if (assignment.getTokenType() != TokenType.ASSIGN) {
            throw new SyntaxError("Expected " + TokenType.ASSIGN + " got " + assignment.getTokenType(), assignment);
        }
        // Parse the expression
        ExpressionNode expression = ExpressionNode.parse(tokens);
        // Parse semicolon
        Token semicolon = tokens.remove(0);
        if (semicolon.getTokenType() != TokenType.SEMICOLON) {
            throw new SyntaxError("Expected " + TokenType.SEMICOLON + " got " + semicolon.getTokenType(), semicolon);
        }
        return new AssignmentNode(id, expression);
    }

    @Override
    public String convertToJott() {
        return id.convertToJott() + " = " + expression.convertToJott() + ";";
    }

    /*
     * Id and expression must be valid
     * Data types of left and right side of = should match
     */
    @Override
    public boolean validateTree(SymbolTable symbolTable) throws Exception {
        id.validateTree(symbolTable);
        expression.validateTree(symbolTable);

        if (id.getType(symbolTable) != expression.getType(symbolTable)) {
            throw new SemanticError("Id and Expression must be of the same data type", id.getToken());
        }

        // Throw error if trying to define a variable with a reserved name
        // These are not allowed as variable or function names
        List<String> forbidden = Arrays.asList("print", "concat","length", "while", "if", "elseif", "else",
                "void", "boolean", "integer", "double", "string");
        if (forbidden.contains(id.getToken().getToken())) {
            throw new SemanticError("Reserved keyword! Cannot be function/variable name: " + id.getToken().getToken(), id.getToken());
        }

        return true;
    }

    /**
     * Execute the expression and set it as the value of the id's VarInfo in the symbolTable
     */
    @Override
    public Object execute(SymbolTable symbolTable) throws Exception {
        // Evaluate the expression to get its value
        Object value = expression.execute(symbolTable);

        // Retrieve the variable information from the symbol table
        VarInfo varInfo = symbolTable.getVar(id.getToken().getToken());

        // Assign the value to the variable in the symbol table
        // TODO Do we want to keep the value attribute of a variable a string? 
        // TODO or change it to a generic object to later make math with integers and such easier?
        if (value instanceof String string) {
            varInfo.value = string;
        } else {
            varInfo.value = String.valueOf(value);
        }

        // Not a function return, so don't return anything
        return null;

    }

    @Override
    public Token getToken() {
        return this.id.getToken();
    }

    public static void main(String[] args) {
    /*
        System.out.println("Testing AssignmentNode Main Method");
        SymbolTable symbolTable = new SymbolTable();
        symbolTable.addFunction("main", new FunctionInfo("main", "void", new HashMap<>()));
        Boolean succeeded = symbolTable.enterScope("main");
        if (!succeeded) {
            throw new UnsupportedOperationException("failed to enter scope!");
        }
        
    
        try {
            // Declare variable `var` as an integer
            VarInfo varInfo = new VarInfo("var", DataType.INTEGER, null);
            succeeded = symbolTable.addVar(varInfo);
            if (!succeeded) {
                throw new UnsupportedOperationException("failed to add var");
            }
    
            // Test Case 1: Valid assignment, var = 4;
            ArrayList<Token> tokens1 = new ArrayList<>();
            tokens1.add(new Token("var", "testFile.jott", 1, TokenType.ID_KEYWORD));
            tokens1.add(new Token("=", "testFile.jott", 1, TokenType.ASSIGN));
            tokens1.add(new Token("4", "testFile.jott", 1, TokenType.NUMBER));
            tokens1.add(new Token(";", "testFile.jott", 1, TokenType.SEMICOLON));
    
            AssignmentNode assignmentNode1 = AssignmentNode.parse(tokens1);
            System.out.println("Parsed AssignmentNode 'var=4;': " + assignmentNode1.convertToJott());
            System.out.println("Validation Result: " + assignmentNode1.validateTree(symbolTable)); // Expected: true
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    
        try {
            // Test Case 2: Valid expression with addition, var = 4 + 5;
            ArrayList<Token> tokens2 = new ArrayList<>();
            tokens2.add(new Token("var", "testFile.jott", 2, TokenType.ID_KEYWORD));
            tokens2.add(new Token("=", "testFile.jott", 2, TokenType.ASSIGN));
            tokens2.add(new Token("4", "testFile.jott", 2, TokenType.NUMBER));
            tokens2.add(new Token("+", "testFile.jott", 2, TokenType.MATH_OP));
            tokens2.add(new Token("5", "testFile.jott", 2, TokenType.NUMBER));
            tokens2.add(new Token(";", "testFile.jott", 2, TokenType.SEMICOLON));
    
            AssignmentNode assignmentNode2 = AssignmentNode.parse(tokens2);
            System.out.println("Parsed AssignmentNode 'var=4+5;': " + assignmentNode2.convertToJott());
            System.out.println("Validation Result: " + assignmentNode2.validateTree(symbolTable)); // Expected: true
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    
        try {
            // Test Case 3: Invalid chained operations, var = 4 ++ 5;
            ArrayList<Token> tokens3 = new ArrayList<>();
            tokens3.add(new Token("var", "testFile.jott", 3, TokenType.ID_KEYWORD));
            tokens3.add(new Token("=", "testFile.jott", 3, TokenType.ASSIGN));
            tokens3.add(new Token("4", "testFile.jott", 3, TokenType.NUMBER));
            tokens3.add(new Token("+", "testFile.jott", 3, TokenType.MATH_OP));
            tokens3.add(new Token("+", "testFile.jott", 3, TokenType.MATH_OP));
            tokens3.add(new Token("5", "testFile.jott", 3, TokenType.NUMBER));
            tokens3.add(new Token(";", "testFile.jott", 3, TokenType.SEMICOLON));
    
            AssignmentNode assignmentNode3 = AssignmentNode.parse(tokens3);
            System.out.println("Parsed AssignmentNode 'var=4++5;': " + assignmentNode3.convertToJott());
            System.out.println("Validation Result: " + assignmentNode3.validateTree(symbolTable)); // Expected: throws SemanticError
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    
        try {
            // Test Case 4: Mismatched types, var = "hello";
            ArrayList<Token> tokens4 = new ArrayList<>();
            tokens4.add(new Token("var", "testFile.jott", 4, TokenType.ID_KEYWORD));
            tokens4.add(new Token("=", "testFile.jott", 4, TokenType.ASSIGN));
            tokens4.add(new Token("\"hello\"", "testFile.jott", 4, TokenType.STRING));
            tokens4.add(new Token(";", "testFile.jott", 4, TokenType.SEMICOLON));
    
            AssignmentNode assignmentNode4 = AssignmentNode.parse(tokens4);
            System.out.println("Parsed AssignmentNode 'var=\"hello\";': " + assignmentNode4.convertToJott());
            System.out.println("Validation Result: " + assignmentNode4.validateTree(symbolTable)); // Expected: throws SemanticError for type mismatch
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    
        try {
            // Test Case 5: Valid expression with negative number, var = 3 + -7;
            ArrayList<Token> tokens5 = new ArrayList<>();
            tokens5.add(new Token("var", "testFile.jott", 5, TokenType.ID_KEYWORD));
            tokens5.add(new Token("=", "testFile.jott", 5, TokenType.ASSIGN));
            tokens5.add(new Token("3", "testFile.jott", 5, TokenType.NUMBER));
            tokens5.add(new Token("+", "testFile.jott", 5, TokenType.MATH_OP));
            tokens5.add(new Token("-", "testFile.jott", 5, TokenType.MATH_OP));
            tokens5.add(new Token("7", "testFile.jott", 5, TokenType.NUMBER));
            tokens5.add(new Token(";", "testFile.jott", 5, TokenType.SEMICOLON));
    
            AssignmentNode assignmentNode5 = AssignmentNode.parse(tokens5);
            System.out.println("Parsed AssignmentNode 'var=3+-7;': " + assignmentNode5.convertToJott());
            System.out.println("Validation Result: " + assignmentNode5.validateTree(symbolTable)); // Expected: true
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    */
    }
}