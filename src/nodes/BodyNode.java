package nodes;

import errors.SemanticError;
import errors.SyntaxError;
import java.util.ArrayList;
import java.util.HashMap;
import msc.*;
import provided.JottParser;
import provided.JottTree;
import provided.Token;
import provided.TokenType;

/*
 * Body Node
 * Can either be a boolean equation or a mathmatical equation
 * < body_stmt >⋆ < return_stmt >
 */
public class BodyNode implements JottTree {
    ArrayList<BodyStmtNode> bodyStmts;
    ReturnStmtNode returnStmt;
    boolean returns;
    DataType returnType;

    public BodyNode(ArrayList<BodyStmtNode> bodyStmtNodes, ReturnStmtNode returnStmtNode) {
        this.bodyStmts = bodyStmtNodes;
        this.returnStmt = returnStmtNode;
        this.returns = false;
        this.returnType = null;
    }

    public DataType getReturnType() throws Exception {
        return this.returnType;
    }

    public boolean allReturn() {
        return this.returns;
    }

    public Token getToken() {
        return this.returnStmt.getToken();
    }

    // parse
    public static BodyNode parse(ArrayList<Token> tokens) throws Exception {
        if (tokens.isEmpty()) {
            throw new SyntaxError("Expected return statement got " + JottParser.finalToken.getToken(),
                    JottParser.finalToken);
        }

        ArrayList<BodyStmtNode> bodyStmts = new ArrayList<>();
        while (!tokens.isEmpty() && !tokens.get(0).getToken().equals("Return")) {
            BodyStmtNode currentStmt = BodyStmtNode.parse(tokens);
            if (currentStmt != null) {
                bodyStmts.add(currentStmt);
            } else {
                break;
            }
        }

        ReturnStmtNode returnStmt = ReturnStmtNode.parse(tokens);

        return new BodyNode(bodyStmts, returnStmt);
    }

    @Override
    public String convertToJott() {
        String outStr = "";
        for (BodyStmtNode bodyStmt : this.bodyStmts) {
            String currentStmt = bodyStmt.convertToJott();
            outStr += currentStmt;
            if (currentStmt.length() > 1 && currentStmt.charAt(0) == ':' && currentStmt.charAt(1) == ':') {
                // check for function call, there might be an easier way to do this...
                outStr += ";";
            }
        }

        if (this.returnStmt != null) {
            outStr += this.returnStmt.convertToJott();
        }

        return outStr;
    }

    @Override
    public boolean validateTree(SymbolTable symbolTable) throws Exception {
        for(BodyStmtNode bodyStmt: this.bodyStmts) {
            if(this.returns == true) {
                throw new SemanticError("Unreachable code after return statement", bodyStmt.getToken());
            }

            //System.out.println("Handling Body statement(s)");
            bodyStmt.validateTree(symbolTable); // should check return types match in here

            if(bodyStmt.allReturn()) {
                this.returns = true;
            }
            if(bodyStmt.getReturnType() != null) {
                this.returnType = bodyStmt.getReturnType();
            }
        }

        //System.out.println("Handling Return");
        // handle return at end of body
        this.returnStmt.validateTree(symbolTable);  // should do return typechecking in here

        if(this.returnStmt.getReturnType() != null) {
            if(this.returns == true) {
                // all paths return before the end but another exists
                throw new SemanticError("Unreachable return at end", this.returnStmt.getToken());
            }

            this.returns = true;    // all paths return if return is at end of body
            this.returnType = this.returnStmt.getReturnType();
        }

        return true;
    }

        /*
        for(BodyStmtNode bodyStmt: this.bodyStmts) {
            if(this.returns == true) {
                throw new SemanticError("Unreachable code after return statement", bodyStmt.getToken());
            }

            bodyStmt.validateTree(symbolTable);

            DataType returnType = bodyStmt.getReturnType();

            if(returnType != null) {
                if(this.returnType == null) {
                    this.returnType = returnType;
                } else if(this.returnType != returnType) {
                    // this might print the wrong line number...
                    throw new SemanticError("Body does not always return same data type", bodyStmt.getToken());
                }
            }

            if(bodyStmt.allReturn()) {
                this.returns = true;
            }
        }

        this.returnStmt.validateTree(symbolTable);

        if(this.returnStmt.getReturnType() == null) {
            if(!this.returns && this.returnType != null) {
                // not all paths return but at least one does
                // use first body stmt for token because there's no return token
                throw new SemanticError("Only some paths of function return", this.bodyStmts.get(0).getToken());
            }
        } else {
            if(this.returns == true) {
                // all paths return before the end but another
                throw new SemanticError("Unreachable return at end", this.returnStmt.getToken());
            } else if(this.returnType == null) {
                this.returnType = this.returnStmt.getReturnType();
            } else if (this.returnType != this.returnStmt.getReturnType()) {
                // return type in loops/if doesn't match one at end
                throw new SemanticError("Return types do not match", this.returnStmt.getToken());
            }
            this.returns = true;
        }

        return true;
        */

    @Override
    public void execute() {
        // To be implemented in phase 4
        throw new UnsupportedOperationException("Execution not supported yet.");
    }

    public static void main(String[] args) {
        System.out.println("-----Testing BodyNode validateTree Method-----");
        // Initialize SymbolTable
        SymbolTable symbolTable = new SymbolTable();
        symbolTable.addFunction("main", new FunctionInfo("main", "void", new HashMap<>()));
        symbolTable.enterScope("main");
        symbolTable.addFunction("func1", new FunctionInfo("func1", "Integer", new HashMap<>()));
        symbolTable.enterScope("func1");

        // Add test variables
        symbolTable.addVar(new VarInfo("x", DataType.INTEGER, "5")); // Variable x as INTEGER
        symbolTable.addVar(new VarInfo("y", DataType.DOUBLE, "3.14")); // Variable y as DOUBLE
        try {
            // Test Case 1: Valid BodyNode with return at the end
            ArrayList<Token> tokens1 = new ArrayList<>();
            tokens1.add(new Token("x", "testFile.jott", 1, TokenType.ID_KEYWORD)); // x
            tokens1.add(new Token("=", "testFile.jott", 1, TokenType.ASSIGN)); // =
            tokens1.add(new Token("5", "testFile.jott", 1, TokenType.NUMBER)); // 5
            tokens1.add(new Token(";", "testFile.jott", 1, TokenType.SEMICOLON)); // ;
            tokens1.add(new Token("Return", "testFile.jott", 2, TokenType.ID_KEYWORD)); // Return
            tokens1.add(new Token("x", "testFile.jott", 2, TokenType.ID_KEYWORD)); // x
            tokens1.add(new Token(";", "testFile.jott", 2, TokenType.SEMICOLON)); // ;
    
            BodyNode bodyNode1 = BodyNode.parse(tokens1);
            System.out.println("Parsing BodyNode with return at the end: " + bodyNode1.convertToJott());
            bodyNode1.validateTree(symbolTable); // Should pass validation
            System.out.println("Validation passed for BodyNode with return at the end");
        } catch (Exception e){
            System.err.println("Unexpected Error: " + e.getMessage());
        }
        // Set up new function scope
        symbolTable.exitScope();
        symbolTable.addFunction("func2", new FunctionInfo("func2", "Integer", new HashMap<>()));    
        symbolTable.enterScope("func2");
        symbolTable.addVar(new VarInfo("z", DataType.INTEGER, null)); // Variable z as INTEGER (no initial value)
        /*
        try {
            // Test Case 2: BodyNode with unreachable code after return
            // NOTE: Parser already removes unreachable code 
            ArrayList<Token> tokens2 = new ArrayList<>();
            tokens2.add(new Token("z", "testFile.jott", 3, TokenType.ID_KEYWORD)); // z
            tokens2.add(new Token("=", "testFile.jott", 3, TokenType.ASSIGN)); // =
            tokens2.add(new Token("5", "testFile.jott", 3, TokenType.NUMBER)); // 5
            tokens2.add(new Token(";", "testFile.jott", 3, TokenType.SEMICOLON)); // ;
            tokens2.add(new Token("Return", "testFile.jott", 4, TokenType.ID_KEYWORD)); // Return
            tokens2.add(new Token("z", "testFile.jott", 4, TokenType.ID_KEYWORD)); // z
            tokens2.add(new Token(";", "testFile.jott", 4, TokenType.SEMICOLON)); // ;
            tokens2.add(new Token("z", "testFile.jott", 5, TokenType.ID_KEYWORD)); // z
            tokens2.add(new Token("=", "testFile.jott", 5, TokenType.ASSIGN)); // =
            tokens2.add(new Token("4", "testFile.jott", 5, TokenType.NUMBER)); // 4
            tokens2.add(new Token(";", "testFile.jott", 5, TokenType.SEMICOLON)); // ;
    
            BodyNode bodyNode2 = BodyNode.parse(tokens2);
            System.out.println("Parsing BodyNode with unreachable code: " + bodyNode2.convertToJott());
            bodyNode2.validateTree(symbolTable); // Should throw SemanticError for unreachable code
            System.out.println("!!!TEST FAILED: Validation passed for BodyNode with unreachable code\n");
        } catch (Exception e){
            System.err.println("Expected Error: " + e.getMessage());
        }
        */
        try{
            // Test Case 3: Invalid return type mismatch
            ArrayList<Token> tokens3 = new ArrayList<>();
            tokens3.add(new Token("Return", "testFile.jott", 6, TokenType.ID_KEYWORD)); // Return
            tokens3.add(new Token("3.14", "testFile.jott", 6, TokenType.NUMBER)); // 3.14
            tokens3.add(new Token(";", "testFile.jott", 6, TokenType.SEMICOLON)); // ;
    
            BodyNode bodyNode3 = BodyNode.parse(tokens3);
            System.out.println("Parsing BodyNode with invalid return type: " + bodyNode3.convertToJott());
            bodyNode3.validateTree(symbolTable); // Should throw SemanticError for type mismatch
            System.out.println("Validation passed for BodyNode with invalid return type");
        } catch (Exception e){
            System.err.println("Expected Error: " + e.getMessage());
        }
        /*
        // Set up new function scope
        symbolTable.exitScope();
        symbolTable.addFunction("func3", new FunctionInfo("func3", "Integer", new HashMap<>()));    
        symbolTable.enterScope("func3");
        symbolTable.addVar(new VarInfo("z", DataType.INTEGER, null)); // Variable z as INTEGER (no initial value)
        try {
            // Test Case 4: Missing return statement in non-void function
            // NOTE: This will be tested in FunctionBodNode, not BodyNode 
            ArrayList<Token> tokens4 = new ArrayList<>();
            tokens4.add(new Token("z", "testFile.jott", 7, TokenType.ID_KEYWORD)); // z
            tokens4.add(new Token("=", "testFile.jott", 7, TokenType.ASSIGN)); // =
            tokens4.add(new Token("5", "testFile.jott", 7, TokenType.NUMBER)); // 5
            tokens4.add(new Token(";", "testFile.jott", 7, TokenType.SEMICOLON)); // ;
    
            BodyNode bodyNode4 = BodyNode.parse(tokens4);
            System.out.println("Parsing BodyNode with missing return statement: " + bodyNode4.convertToJott());
            bodyNode4.validateTree(symbolTable); // Should throw SemanticError for missing return
            System.out.println("!!!TEST FAILED: Validation passed for BodyNode with missing return statement\n");
        } catch (Exception e) {
            System.err.println("Expected Error: " + e.getMessage());
        }
        */
    }
}