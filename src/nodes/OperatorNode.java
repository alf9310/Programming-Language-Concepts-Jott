package nodes;

/**
 * Interface for RelOpNode and MathOpNode
 * Only exists so BinaryOpNode can have a generic operation node
 */
public interface OperatorNode extends ExpressionNode {
    @Override
    String convertToJott();

    @Override
    boolean validateTree();
    
    @Override
    void execute();
}