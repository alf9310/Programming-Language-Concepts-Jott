package nodes;

import errors.SemanticError;
import errors.SyntaxError;
import java.util.ArrayList;
import msc.*;
import provided.JottParser;
import provided.JottTree;
import provided.Token;

/*
 * Function Body Node
 * <var_dec >⋆ <body>
 */
public class FuncBodyNode implements JottTree {
    ArrayList<VarDecNode> varDecs;
    BodyNode body;
    boolean returns;
    DataType returnType;

    public FuncBodyNode(ArrayList<VarDecNode> varDecs, BodyNode body) {
        this.varDecs = varDecs;
        this.body = body;
        this.returns = false;
        this.returnType = null;
    }

    boolean allReturn() {
        return this.returns;
    }

    DataType getReturnType() {
        return this.returnType;
    }

    // parse
    public static FuncBodyNode parse(ArrayList<Token> tokens) throws Exception {
        if (tokens.isEmpty()) {
            throw new SyntaxError("Expected variable description or body got " + JottParser.finalToken.getToken(),
                    JottParser.finalToken);
        }
        ArrayList<VarDecNode> varDecs = new ArrayList<>();

        while (!tokens.isEmpty()
                && (tokens.get(0).getToken().equals("Boolean") || tokens.get(0).getToken().equals("Integer")
                        || tokens.get(0).getToken().equals("String") || tokens.get(0).getToken().equals("Double")
                        || tokens.get(0).getToken().equals("Void"))) {
            VarDecNode currentStmt = VarDecNode.parse(tokens);
            if (currentStmt != null) {
                varDecs.add(currentStmt);
            } else {
                break;
            }
        }

        BodyNode body = BodyNode.parse(tokens);

        return new FuncBodyNode(varDecs, body);
    }

    @Override
    public String convertToJott() {
        String outStr = "";
        for (VarDecNode bodyStmt : this.varDecs) {
            String currentStmt = bodyStmt.convertToJott();
            outStr += currentStmt;
            if (currentStmt.length() > 1 && currentStmt.charAt(0) == ':' && currentStmt.charAt(1) == ':') {
                // check for function call, there might be an easier way to do this...
                outStr += ";";
            }
        }

        if (this.body != null) {
            outStr += this.body.convertToJott();
        }

        return outStr;
    }

    @Override
    public boolean validateTree(SymbolTable symbolTable) throws Exception {
        // To be implemented in phase 3
        // Body can not have funcdef inside it
        for(VarDecNode varDec: this.varDecs) {
            varDec.validateTree(symbolTable);
        }

        this.body.validateTree(symbolTable);
        this.returns = this.body.allReturn();
        this.returnType = this.body.getReturnType();

        String func = symbolTable.current_scope;
        FunctionInfo info = symbolTable.getFunction(func);
        DataType funcReturn = info.getReturnDataType();
        if(funcReturn == null) {
            throw new SemanticError("Function return should be one of the data types provided or VOID", this.body.getToken());
        }

        if(funcReturn != DataType.VOID && !this.returns) {
            // function should return, but not all paths do
            throw new SemanticError("Function should return " + funcReturn + ", but not all paths are returnable", this.body.getToken());
        }

        if(this.returnType != null && this.returnType != DataType.VOID && funcReturn == DataType.VOID) {
            throw new SemanticError("Void function should not return", this.body.getToken());
        }

        return true;
    }

    /**
     * Execute the function's body
     */
    @Override
    public Object execute(SymbolTable symbolTable) throws Exception {
        // TODO: I don't think we need to execute variable declarations as they're already in scope

        // Return the body of the function
        return body.execute(symbolTable);
    }

    public static void main(String[] args) {
        /*
        try {
            System.out.println("Testing FuncBodyNode Main Method");
    
            SymbolTable symbolTable = new SymbolTable();
            symbolTable.addFunction("main", new FunctionInfo("main", "void", new HashMap<>()));
            symbolTable.enterScope("main");
    
            // Add a sample function to the symbol table for validation
            symbolTable.addFunction("testFunc", new FunctionInfo("testFunc", "Boolean", new HashMap<>()));
            symbolTable.enterScope("testFunc");
    
            // Test Case 1: Valid variable declaration and body
            ArrayList<Token> tokens1 = new ArrayList<>();
            // Variable declaration
            tokens1.add(new Token("Boolean", "testFile.jott", 1, TokenType.ID_KEYWORD));
            tokens1.add(new Token("var", "testFile.jott", 1, TokenType.ID_KEYWORD));
            tokens1.add(new Token(";", "testFile.jott", 1, TokenType.SEMICOLON));
            // Body
            tokens1.add(new Token("Return", "testFile.jott", 2, TokenType.ID_KEYWORD));
            tokens1.add(new Token("True", "testFile.jott", 2, TokenType.ID_KEYWORD));
            tokens1.add(new Token(";", "testFile.jott", 2, TokenType.SEMICOLON));
            FuncBodyNode funcBodyNode1 = FuncBodyNode.parse(tokens1);
            System.out.println("Parsed FuncBodyNode Test Case 1: " + funcBodyNode1.convertToJott());
            System.out.println("Validation Result Test Case 1: " + funcBodyNode1.validateTree(symbolTable));
    
            // TODO FIX THIS ERROR
            // Test Case 2: Missing return statement for non-void function
            FunctionInfo anotherFuncInfo = new FunctionInfo("anotherFunc", "Boolean", new HashMap<>());
            symbolTable.addFunction("anotherFunc", anotherFuncInfo);

            ArrayList<Token> tokens2 = new ArrayList<>();
            // Variable declaration
            tokens2.add(new Token("Boolean", "testFile.jott", 1, TokenType.ID_KEYWORD));
            tokens2.add(new Token("flag", "testFile.jott", 1, TokenType.ID_KEYWORD));
            tokens2.add(new Token(";", "testFile.jott", 1, TokenType.SEMICOLON));
            // Body
            tokens2.add(new Token("::", "testFile.jott", 3, TokenType.FC_HEADER));
            tokens2.add(new Token("anotherFunc", "testFile.jott", 3, TokenType.ID_KEYWORD));
            tokens2.add(new Token("[", "testFile.jott", 3, TokenType.L_BRACKET));
            tokens2.add(new Token("]", "testFile.jott", 3, TokenType.R_BRACKET));
            tokens2.add(new Token(";", "testFile.jott", 3, TokenType.SEMICOLON));
            FuncBodyNode funcBodyNode2 = FuncBodyNode.parse(tokens2);
            System.out.println("Parsed FuncBodyNode Test Case 2: " + funcBodyNode2.convertToJott());
            try {
                funcBodyNode2.validateTree(symbolTable);
            } catch (Exception e) {
                System.err.println("Validation Error Test Case 2: " + e.getMessage());
            }
    
            // TODO fFIX THIS ERROR
            // Test Case 3: Function with VOID return type and no return statement
            symbolTable.addFunction("testFunc2", new FunctionInfo("testFunc2", "void", new HashMap<>()));
            symbolTable.enterScope("testFunc2"); // Set the function's return type to VOID
            try{
                ArrayList<Token> tokens3 = new ArrayList<>();
                // Variable declaration
                tokens3.add(new Token("Integer", "testFile.jott", 1, TokenType.ID_KEYWORD));
                tokens3.add(new Token("counter", "testFile.jott", 1, TokenType.ID_KEYWORD));
                tokens3.add(new Token(";", "testFile.jott", 1, TokenType.SEMICOLON));
                // Body without a return statement
                tokens3.add(new Token("counter", "testFile.jott", 2, TokenType.ID_KEYWORD));
                tokens3.add(new Token("=", "testFile.jott", 2, TokenType.ASSIGN));
                tokens3.add(new Token("5", "testFile.jott", 2, TokenType.NUMBER));
                tokens3.add(new Token(";", "testFile.jott", 2, TokenType.SEMICOLON));
                FuncBodyNode funcBodyNode3 = FuncBodyNode.parse(tokens3);
                System.out.println("Parsed FuncBodyNode Test Case 3: " + funcBodyNode3.convertToJott());
                System.out.println("Validation Result Test Case 3: " + funcBodyNode3.validateTree(symbolTable));
            } catch (Exception e){
                System.err.println("Validation Error Test Case 3: " + e.getMessage());
            }
    
            // Reset function return type for further testing
            symbolTable.enterScope("testFunc");
    
            // Test Case 4: Invalid variable declaration
            ArrayList<Token> tokens4 = new ArrayList<>();
            // Invalid variable declaration
            tokens4.add(new Token("Booleen", "testFile.jott", 1, TokenType.ID_KEYWORD)); // Typo in type
            tokens4.add(new Token("invalidVar", "testFile.jott", 1, TokenType.ID_KEYWORD));
            tokens4.add(new Token(";", "testFile.jott", 1, TokenType.SEMICOLON));
            // Body
            tokens4.add(new Token("Return", "testFile.jott", 2, TokenType.ID_KEYWORD));
            tokens4.add(new Token("False", "testFile.jott", 2, TokenType.ID_KEYWORD));
            tokens4.add(new Token(";", "testFile.jott", 2, TokenType.SEMICOLON));
            FuncBodyNode funcBodyNode4 = FuncBodyNode.parse(tokens4);
            System.out.println("Parsed FuncBodyNode Test Case 4: " + funcBodyNode4.convertToJott());
            try {
                funcBodyNode4.validateTree(symbolTable);
            } catch (Exception e) {
                System.err.println("Validation Error Test Case 4: " + e.getMessage());
            }
    
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    */
    }
}