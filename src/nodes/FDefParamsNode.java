package nodes;

import errors.*;
import java.util.ArrayList;
import msc.*;
import provided.JottTree;
import provided.Token;
import provided.TokenType;

/*
 * Function Definition Parameters Node
 * <id >: < type > < function_def_params_t >⋆ | ε
 */
public class FDefParamsNode implements JottTree {
    IDNode id;
    TypeNode type;
    ArrayList<FDefParamsTNode> fDefParamsTNode;

    public FDefParamsNode() {
        this.type = null;
        this.id = null;
        this.fDefParamsTNode = null;

    }

    public FDefParamsNode(TypeNode type, IDNode id, ArrayList<FDefParamsTNode> fDefParamsTNode) {
        this.type = type;
        this.id = id;
        this.fDefParamsTNode = fDefParamsTNode;
    }

    // Parsing
    public static FDefParamsNode parse(ArrayList<Token> tokens) throws Exception {
        if (tokens.isEmpty()) {
            return new FDefParamsNode();
        }

        // Parse the ID
        IDNode id = IDNode.parse(tokens);

        // Parse colon
        Token colon = tokens.remove(0);
        if (colon.getTokenType() != TokenType.COLON) {
            throw new SyntaxError("Expected colon : got " + colon.getToken(), colon);
        }

        // Parse the type
        TypeNode type = TypeNode.parse(tokens);

        // <FDefParamsTNode>⋆
        ArrayList<FDefParamsTNode> list = new ArrayList<>();

        if (tokens.isEmpty()) {
            return new FDefParamsNode(type, id, list);
        }

        Token currentToken = tokens.get(0);
        while (!tokens.isEmpty() && currentToken.getTokenType() == TokenType.COMMA) {
            list.add(FDefParamsTNode.parse(tokens));
            if (!tokens.isEmpty()) {
                currentToken = tokens.get(0);
            }
        }
        return new FDefParamsNode(type, id, list);
    }

    @Override
    public String convertToJott() {
        StringBuilder str = new StringBuilder();
        if (id != null) {
            str.append(id.convertToJott());
            str.append(":");
            str.append(type.convertToJott());
            for (FDefParamsTNode paramt : fDefParamsTNode) {
                str.append(paramt.convertToJott());
            }
        }
        return str.toString();
    }

    @Override
    public boolean validateTree(SymbolTable symbolTable) throws Exception {
        // No parameters case
        if (id == null || type == null) {
            return true;
        }

        // 
        FunctionInfo func = symbolTable.getFunction(symbolTable.current_scope);
        String value = "";
        if(this.type.getType() == DataType.BOOLEAN) {
            value = "Boolean";
        } else if(this.type.getType() == DataType.INTEGER) {
            value = "Integer";
        } else if(this.type.getType() == DataType.DOUBLE) {
            value = "Double";
        } else if(this.type.getType() == DataType.STRING) {
            value = "String";
        }
        func.parameterTypes.put(this.id.getToken().getToken(), value);
        func.parameterOrder.put(func.parameterTypes.size(), this.id.getToken().getToken());
        symbolTable.addVar(new VarInfo(id.convertToJott(), type.getType(), null));

        // Validate the main parameter (id and type), if present
        if (id != null && type != null) {
            id.validateTree(symbolTable);
            type.validateTree(symbolTable);
        }
    
        // Validate each additional parameter in fDefParamsTNode
        for (FDefParamsTNode paramt : fDefParamsTNode) {
            if (!paramt.validateTree(symbolTable)) {
                throw new SemanticError("Invalid additional parameter in function definition.", null);
            }
        }
        
        return true;
    }

    @Override
    public Object execute(SymbolTable symbolTable) {
        // To be implemented in phase 4
        throw new UnsupportedOperationException("Execution not supported yet.");
    }

    public static void main(String[] args) {
        /*
        try {
            System.out.println("Testing FDefParamsNode Main Method");

            SymbolTable symbolTable = new SymbolTable();
            symbolTable.addFunction("main", new FunctionInfo("main", "void", new HashMap<>()));
            symbolTable.enterScope("main");

            // Test Case 1: Valid Parameter 'x:Integer'
            try {
                ArrayList<Token> tokens1 = new ArrayList<>();
                tokens1.add(new Token("x", "testFile.jott", 1, TokenType.ID_KEYWORD));
                tokens1.add(new Token(":", "testFile.jott", 1, TokenType.COLON));
                tokens1.add(new Token("Integer", "testFile.jott", 1, TokenType.ID_KEYWORD));

                FDefParamsNode FDefParamsNode1 = FDefParamsNode.parse(tokens1);
                symbolTable.addVar(new VarInfo("x", DataType.INTEGER, null));
                boolean isValid1 = FDefParamsNode1.validateTree(symbolTable);
                System.out.println("Test Case 1 Passed: " + isValid1);
            } catch (Exception e) {
                System.err.println("Test Case 1 Failed: " + e.getMessage());
            }

            // Test Case 2: Valid Parameters 'x:Integer, y:Double'
            try {
                ArrayList<Token> tokens2 = new ArrayList<>();
                tokens2.add(new Token("x", "testFile.jott", 1, TokenType.ID_KEYWORD));
                tokens2.add(new Token(":", "testFile.jott", 1, TokenType.COLON));
                tokens2.add(new Token("Integer", "testFile.jott", 1, TokenType.ID_KEYWORD));
                tokens2.add(new Token(",", "testFile.jott", 1, TokenType.COMMA));
                tokens2.add(new Token("y", "testFile.jott", 1, TokenType.ID_KEYWORD));
                tokens2.add(new Token(":", "testFile.jott", 1, TokenType.COLON));
                tokens2.add(new Token("Double", "testFile.jott", 1, TokenType.ID_KEYWORD));

                FDefParamsNode FDefParamsNode2 = FDefParamsNode.parse(tokens2);
                symbolTable.addVar(new VarInfo("x", DataType.INTEGER, null));
                symbolTable.addVar(new VarInfo("y", DataType.DOUBLE, null));
                boolean isValid2 = FDefParamsNode2.validateTree(symbolTable);
                System.out.println("Test Case 2 Passed: " + isValid2);
            } catch (Exception e) {
                System.err.println("Test Case 2 Failed: " + e.getMessage());
            }

            // Test Case 3: Invalid Parameter 'x:UnknownType'
            try {
                ArrayList<Token> tokens3 = new ArrayList<>();
                tokens3.add(new Token("x", "testFile.jott", 1, TokenType.ID_KEYWORD));
                tokens3.add(new Token(":", "testFile.jott", 1, TokenType.COLON));
                tokens3.add(new Token("UnknownType", "testFile.jott", 1, TokenType.ID_KEYWORD));

                FDefParamsNode FDefParamsNode3 = FDefParamsNode.parse(tokens3);
                FDefParamsNode3.validateTree(symbolTable);
                System.err.println("Test Case 3 Failed: Invalid parameter type not caught");
            } catch (Exception e) {
                System.out.println("Test Case 3 Passed: " + e.getMessage());
            }

            // Test Case 4: Empty Parameter List
            try {
                ArrayList<Token> tokens4 = new ArrayList<>();
                FDefParamsNode FDefParamsNode4 = FDefParamsNode.parse(tokens4);
                boolean isValid4 = FDefParamsNode4.validateTree(symbolTable);
                System.out.println("Test Case 4 Passed: " + isValid4);
            } catch (Exception e) {
                System.err.println("Test Case 4 Failed: " + e.getMessage());
            }

            // Test Case 5: Duplicate Parameter Name 'x:Integer, x:Double'
            try {
                ArrayList<Token> tokens5 = new ArrayList<>();
                tokens5.add(new Token("x", "testFile.jott", 1, TokenType.ID_KEYWORD));
                tokens5.add(new Token(":", "testFile.jott", 1, TokenType.COLON));
                tokens5.add(new Token("Integer", "testFile.jott", 1, TokenType.ID_KEYWORD));
                tokens5.add(new Token(",", "testFile.jott", 1, TokenType.COMMA));
                tokens5.add(new Token("x", "testFile.jott", 1, TokenType.ID_KEYWORD));
                tokens5.add(new Token(":", "testFile.jott", 1, TokenType.COLON));
                tokens5.add(new Token("Double", "testFile.jott", 1, TokenType.ID_KEYWORD));

                FDefParamsNode FDefParamsNode5 = FDefParamsNode.parse(tokens5);
                symbolTable.addVar(new VarInfo("x", DataType.INTEGER, null));
                symbolTable.addVar(new VarInfo("x", DataType.DOUBLE, null));
                FDefParamsNode5.validateTree(symbolTable);
                System.err.println("Test Case 5 Failed: Duplicate parameter not caught");
            } catch (Exception e) {
                System.out.println("Test Case 5 Passed: " + e.getMessage());
            }

        } catch (Exception e) {
            System.err.println("Error in main method: " + e.getMessage());
        }
    */
    }
}