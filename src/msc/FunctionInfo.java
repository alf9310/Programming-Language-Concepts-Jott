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
    public HashMap<Integer, String> parameterOrder; // key is current parameter # (starting at 1), value is its name

    public HashMap<String, HashMap<String, VarInfo>> variableMap;

    public FunctionInfo(String name, String returnType, HashMap<String, String> parameterTypes) {
        this.name = name;
        this.returnType = returnType;
        this.parameterTypes = parameterTypes;
        // var list starts empty
        this.variableMap = new HashMap<>();
        this.parameterOrder = new HashMap<>();
    }

    public String getName() {
        return name;
    }
    public String getReturnType() {
        return returnType;
    }
    public DataType getReturnDataType() {
        if(this.returnType.equals("Boolean")) {
            return DataType.BOOLEAN;
        } else if(this.returnType.equals("String")) {
            return DataType.STRING;
        } else if(this.returnType.equals("Integer")) {
            return DataType.INTEGER;
        } else if(this.returnType.equals("Double")) {
            return DataType.DOUBLE;
        } else if(this.returnType.equals("Void")) {
            return DataType.VOID;
        } else {
            return null;    // maybe throw exception? this shouldn't be possible tho
        }
    }

    public HashMap<String, String> getParameterTypes() {
        return parameterTypes;
    }
    public HashMap<String, DataType> getParameterDataTypes() {
        HashMap<String, DataType> paramTypes = new HashMap<>();

        for(String key: parameterTypes.keySet()) {
            DataType value = null;
            String val = parameterTypes.get(key);
            if(val.equals("Boolean")) {
                value = DataType.BOOLEAN;
            } else if(val.equals("String")) {
                value =DataType.STRING;
            } else if(val.equals("Integer")) {
                value = DataType.INTEGER;
            } else if(val.equals("Double")) {
                value = DataType.DOUBLE;
            } else if(val.equals("Void")) {
                value = DataType.VOID;
            }
            paramTypes.put(key, value);
        }

        return paramTypes;
    }
    public HashMap<String, VarInfo> getVars(String scope) {
        return variableMap.get(scope);
    }
}
