package msc;
import java.util.HashMap;

/**
 * This class is responsible for maintaining the symbol table
 * The table object is passed between all the nodes
 * 
 * 
 * @author Kaiy Muhammad, Sejal Bhattad, Lauren Kaestle, Audrey Fuller
 * 
 **/

public class SymbolTable {

    public HashMap<String, FunctionInfo> functionMap;
    // private HashMap<String, HashMap<String, VarInfo>> variableMap;
    public String current_scope = null;

    // constructor
    public SymbolTable() {
        functionMap = new HashMap<>();

        // TODO: Could define built-in functions function def nodes here to call instead of null

        // builtin print, concat, length
        HashMap<String, String> str_param = new HashMap<>();
        str_param.put("str", "String");
        FunctionInfo printInfo = new FunctionInfo("print", "void", str_param, null);
        this.addFunction("print", printInfo);
        printInfo.parameterOrder.put(1, "");
        //TODO update this to handle any data type but void

        HashMap<String, String> concat_param = new HashMap<>();
        concat_param.put("str1", "String");
        concat_param.put("str2", "String");
        FunctionInfo concatInfo = new FunctionInfo("concat", "String", concat_param, null);
        this.addFunction("concat", concatInfo);
        concatInfo.parameterOrder.put(1, "str1");
        concatInfo.parameterOrder.put(2, "str2");

        HashMap<String, String> len_param = new HashMap<>();
        len_param.put("str", "String");
        FunctionInfo lengthInfo = new FunctionInfo("length", "Integer", len_param, null);
        this.addFunction("length", lengthInfo);
        lengthInfo.parameterOrder.put(1, "str");
    }

    //  entering scope function
    public boolean enterScope(String name) {
        // verify scope is already in the func table
        if (!functionMap.containsKey(name)) {
            return false;
            // throw new RuntimeException("Scope not found in symbol table");
        }
        current_scope = name;
        return true;
    }

    //  exiting scope function
    //TODO should function info and variable info be removed?
    //TODO change type of exception thrown to audrey's
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
    
    // Add variable to current scope
    public boolean addVar(VarInfo info) {
        if (current_scope == null) {
            return false;
            // throw new RuntimeException("Not in any scope!");
        }
        // Get the function information for the current scope
        FunctionInfo functionInfo = functionMap.get(current_scope);
    
        // Check if functionInfo is null
        if (functionInfo == null) {
            return false;
            // throw new RuntimeException("Function information for scope " + current_scope + " does not exist.");
        }
    
        // Initialize the variableMap if it's null
        if (functionInfo.variableMap.get(current_scope) == null) {
            functionInfo.variableMap.put(current_scope, new HashMap<>());
        }
    
        // Now add the variable to the current scope's variable map
        functionInfo.variableMap.get(current_scope).put(info.name, info);
        return true;
    }

    //  get variable from current scope
    public VarInfo getVar(String name) {
        if (current_scope==null) {
            return null;
            // throw new RuntimeException("Not in any scope!");
        }
        // throw error if variable not in map of current scope
        FunctionInfo currentFunction = functionMap.get(current_scope);
        if (currentFunction == null) {
            return null;
            // throw new RuntimeException("Function "+current_scope+" not found in symbol table");
        }
        VarInfo var = currentFunction.variableMap.get(current_scope).get(name);
        if (var == null) {
            return null;
            // throw new RuntimeException("Variable "+name+" not found in current scope: '" + current_scope + "'");
        }
        return var;
    }

    public boolean existsInScope(String varName) {

        if (current_scope == null) {

            return false;
            // throw new RuntimeException("Not in any scope!");
        }

        // function exist in own scope
        if (current_scope == varName) {
            return true;
        }

        // Get current function and check if variable exists in current scope's variable map
        FunctionInfo currentFunction = functionMap.get(current_scope);
        if (currentFunction == null || currentFunction.variableMap.get(current_scope) == null) {
            return false;
        }
        return currentFunction.variableMap.get(current_scope).containsKey(varName);
    }

    @Override
    public String toString() {
        return functionMap.toString();
    }

}