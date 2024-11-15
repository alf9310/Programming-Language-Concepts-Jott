package msc;
import java.util.HashMap;
/**
 * This class is responsible for
 * 
 * @author Kaiy Muhammad, Sejal Bhattad, Lauren Kaestle, Audrey Fuller
 **/

//  Function info class
// name, return type, parameters, variables
// getReturnType, getParameters, getVariables

public class FunctionInfo {
    public String name;
    public String returnType;
    //NOTE there is a seperate parameter list to validate correct types passed in
    // also check number to params
    public HashMap<String, String> parameterTypes;
    

    public HashMap<String, HashMap<String, VarInfo>> variableMap;

    public FunctionInfo(String name, String returnType, HashMap<String, String> parameterTypes) {
        this.name = name;
        this.returnType = returnType;
        this.parameterTypes = parameterTypes;
        // var list starts empty
        this.variableMap = new HashMap<>();
    }

    public String getName() {
        return name;
    }
    public String getReturnType() {
        return returnType;
    }
    public DataType getReturnDataType() {
        if(this.returnType.equals("BOOLEAN")) {
            return DataType.BOOLEAN;
        } else if(this.returnType.equals("STRING")) {
            return DataType.STRING;
        } else if(this.returnType.equals("INTEGER")) {
            return DataType.INTEGER;
        } else if(this.returnType.equals("DOUBLE")) {
            return DataType.DOUBLE;
        } else if(this.returnType.equals("VOID")) {
            return DataType.VOID;
        } else {
            return null;    // maybe throw exception? this shouldn't be possible tho
        }
    }

    public HashMap<String, String> getParameterTypes() {
        return parameterTypes;
    }
    public HashMap<String, VarInfo> getVars(String scope) {
        return variableMap.get(scope);
    }
}
