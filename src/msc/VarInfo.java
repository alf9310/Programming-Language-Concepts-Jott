package msc;

/**
 * This class is responsible for 
 * 
 * @author Kaiy Muhammad, Sejal Bhattad, Lauren Kaestle, Audrey Fuller
 **/

// Var info class
// name, type, value
//getVariableType, getVariableValue
public class VarInfo {
    String name;
    DataType type;
    String value;

    public VarInfo(String name, DataType type, String value) {
        this.name = name;
        this.type = type;
        this.value = value;
    }
}