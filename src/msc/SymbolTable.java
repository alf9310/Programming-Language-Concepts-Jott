package msc;
import provided.*;
import java.util.HashMap;

/**
 * This class is responsible for maintaining the symbol table
 * The table object is passed between all the nodes
 * 
 *  ????? Do I need a variablemap or a scope stack
 * Trace out valid.jott example on paper
 * 
 * 
 * @author Kaiy Muhammad, Sejal Bhattad, Lauren Kaestle, Audrey Fuller
 * 
 **/

public class SymbolTable {

    private HashMap<String, FunctionInfo> functionMap;
    // private HashMap<String, HashMap<String, VarInfo>> variableMap;
    private String current_scope = null;

    // constructor
    public SymbolTable() {
        functionMap = new HashMap<>();
    }

    //  entering scope function
    public void enterScope(String name) {
        // verify scope is already in the func table
        if (!functionMap.containsKey(name)) {
            throw new RuntimeException("Scope not found in symbol table");
        }
        current_scope = name;
    }

    //  exiting scope function
    //TODO should function info and variable info be removed?
    //TODO change type of exception thrown to aubrey's
    public void exitScope() {
        current_scope = null;
    }

    //  add/declare function
    //NOTE each function will need to make a functioninfo object
    public void addFunction(String name, FunctionInfo info) {
        functionMap.put(name, info);
    }

    //  get function
    public FunctionInfo getFunction(String name) {
        return functionMap.get(name);
    }
    
    //  add variable to current scope
    //NOTE will need to make a varinfo object
    public void addVar (VarInfo info) {
        if (current_scope==null) {
        throw new RuntimeException("Not in any scope!");
        }
        // add variable to current scopes variable map
        functionMap.get(current_scope).variableMap.get(current_scope).put(info.name, info);
    }

    //  get variable from current scope
    public VarInfo getVar(String name) {
        if (current_scope==null) {
            throw new RuntimeException("Not in any scope!");
        }
        // throw error if variable not in map of current scope
        FunctionInfo currentFunction = functionMap.get(current_scope);
        if (currentFunction == null) {
            throw new RuntimeException("Function "+current_scope+" not found in symbol table");
        }
        VarInfo var = currentFunction.variableMap.get(current_scope).get(name);
        if (var == null) {
            throw new RuntimeException("Variable "+name+" not found in current scope: '" + current_scope + "'");
        }
        return var;
    }

    @Override
    public String toString() {
        return functionMap.toString();
    }

}