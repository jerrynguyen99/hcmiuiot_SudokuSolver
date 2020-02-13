package solved;
class DancingNode {
    DancingNode left, right, top, bottom;
    ColumnNode column;

    DancingNode() {
        left = right = top = bottom = this;
    }

    DancingNode(ColumnNode c) {
        this();
        column = c;
    }

    DancingNode linkDown(DancingNode node) {
        assert(this.column == node.column);
        node.bottom = bottom;
        node.bottom.top = node;
        node.top = this;
        bottom = node;
        return node;
    }

    DancingNode linkRight(DancingNode node) {
        node.right = right;
        node.right.left = node;
        node.left = this;
        right = node;
        return node;
    }

    void removeLeftRight() {
        left.right = right;
        right.left = left;
    }

    void reinsertLeftRight() {
        left.right = this;
        right.left = this;
    }

    void removeTopBottom() {
        top.bottom = bottom;
        bottom.top = top;
    }

    void reinsertTopBottom() {
        top.bottom = this;
        bottom.top = this;
    }
}


class ColumnNode extends DancingNode {

    int size;
    String name;

    ColumnNode(String n) {
        super();
        size = 0;
        name = n;
        column = this;
    }

    void cover() {
        removeLeftRight();

        for (DancingNode i = bottom; i != this; i = i.bottom) {
            for (DancingNode j = i.right; j != i; j = j.right) {
                j.removeTopBottom();
                j.column.size--;
            }
        }
    }

    void uncover() {
        for (DancingNode i = top; i != this; i = i.top) {
            for (DancingNode j = i.left; j != i; j = j.left) {
                j.column.size++;
                j.reinsertTopBottom();
            }
        }

        reinsertLeftRight();
    }
}