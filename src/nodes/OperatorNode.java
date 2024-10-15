package nodes;

/**
 * Interface for RelOpNode and MathOpNode
 */
public interface OperatorNode extends ExpressionNode {
    String convertToJott();
    boolean validateTree();
    void execute();
}