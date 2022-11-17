import java.util.Iterator;

/**
 * PSKDTree is a Point collection that provides nearest neighbor searching using
 * 2d tree
 */
public class PSKDTree<Value> implements PointSearch<Value> {

    private class Node {
        Point p;
        Value v;
        Node left, right;
        Partition.Direction dir;

        public Node(Point pt, Value val) {
            p = pt;
            v = val;
        }
    }

    BST<Point, Value> kdt;
    int count=0;

    Point minimum;
    Point maximum;
    Node root=null;
    // constructor makes empty kD-tree
    public PSKDTree() {
        kdt = new BST<Point, Value>();
    }

    // add the given Point to kD-tree -- here is 2D (x and y)
    public void put(Point p, Value v) {  //change?? remember to alternate x and y coords for inserting
        if (v == null) { return; } // can't do anything with a null value

        if(root==null) {
            Node x = new Node();
            x.p=p;
            x.v=v;
            x.left=null;
            x.right=null;
            x.dir= Partition.Direction.DOWNUP;
            root=x;
        }
        else put(root, p, v);
        count++;
    }

    private Node put(Node x, Point p, Value v) {
        if (x == null) return new Node(p, v);
        Partition.Direction parentDir = x.dir;
        if(parentDir == Partition.Direction.DOWNUP) {
            if(x.p.x() > p.x()) x.left = put(x.left, p, v);
            else x.right = put(x.right,p,v);
        }
        else {
            if(x.p.y() > p.y()) x.left = put(x.left, p, v);
            else x.right = put(x.right,p,v);
        }
        return x;
    }

    public Value get(Point p) {
        if(isEmpty()) return null;
        return null; //search through the tree instead of returning null
    }

    public boolean contains(Point p) {
        return false; //searching again for p in the bst
    }

    public Value getNearest(Point p) {
        if(isEmpty()) return null;
        return null;
    }

    // return an iterable of all points in collection
    public Iterable<Point> points() { return null; } //account for deletions?

    // return an iterable of all partitions that make up the kD-tree
    public Iterable<Partition> partitions() {
        return null;
    }

    // return the Point that is closest to the given Point
    public Point nearest(Point p) {
        return null;
    }

    // return the k nearest Points to the given Point
    public Iterable<Point> nearest(Point p, int k) {
        return null;
    }

    // return the min and max for all Points in collection.
    // The min-max pair will form a bounding box for all Points.
    // if kD-tree is empty, return null.
    public Point min() {
        if(isEmpty()) return null;
        return null;
    }
    public Point max() {
        if(isEmpty()) return null;
        return null;
    }

    // return the number of Points in kD-tree
    public int size() { return count; }

    // return whether the kD-tree is empty
    public boolean isEmpty() { return count==0; }

    // place your timing code or unit testing here
    public static void main(String[] args) {
    }

}
