package archiver;

public class NodeTree implements Comparable<NodeTree>{
    Character symbol;
    int weight;
    NodeTree left;
    NodeTree right;

    public NodeTree(Character content, int weight) {
        this.symbol = content;
        this.weight = weight;
    }

    public NodeTree(Character content, int weight, NodeTree left, NodeTree right) {
        this.symbol = content;
        this.weight = weight;
        this.left = left;
        this.right = right;
    }

    @Override
    public int compareTo(NodeTree o) {
        return o.weight - weight;
    }

    //обход дерева и создание кода для символа
    public String getCodeForCharacter(Character ch, String parentPath) {
        //дошли до нужного листа
        if (symbol == ch) {
            return parentPath;
        } else {
            if (left != null) {
                String path = left.getCodeForCharacter(ch, parentPath + 0);
                if (path != null) {
                    return path;
                }
            }
            if (right != null) {
                String path = right.getCodeForCharacter(ch, parentPath + 1);
                if (path != null) {
                    return path;
                }
            }
        }
        return null;
    }
}
