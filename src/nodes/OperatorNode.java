package nodes;

/**
 * Interface for RelOpNode and MathOpNode
 * Only exists so BinaryOpNode can have a generic operation node
 */
public interface OperatorNode extends ExpressionNode {
    String convertToJott();
    boolean validateTree();
    void execute();
}