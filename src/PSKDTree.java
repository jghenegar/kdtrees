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
    }
    BST<Point, Value> kdt;
    int count=0;
    Point minimum;
    Point maximum;
    // constructor makes empty kD-tree
    public PSKDTree() {
        kdt = new BST<Point, Value>();
    }

    // add the given Point to kD-tree
    public void put(Point p, Value v) {  //change??
        kdt.put(p,v);
        count++;
    }

    public Value get(Point p) {
        if(isEmpty()) return null;
        return null;
    }

    public boolean contains(Point p) {
        return false;
    }

    public Value getNearest(Point p) {
        if(isEmpty()) return null;
        return null;
    }

    // return an iterable of all points in collection
    public Iterable<Point> points() { return null; }

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
