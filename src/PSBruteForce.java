import java.util.Iterator;

/**
 * PSBruteForce is a Point collection that provides brute force
 * nearest neighbor searching using red-black tree.
 */
public class PSBruteForce<Value> implements PointSearch<Value> {
    // constructor makes empty collection
    public PSBruteForce() { }

    // add the given Point to KDTree
    public void put(Point p, Value v) {
    }

    public Value get(Point p) {
        return null;
    }

    public boolean contains(Point p) {
        return false;
    }

    // return an iterable of all points in collection
    public Iterable<Point> points() {
        return null;
    }

    // return the Point that is closest to the given Point
    public Point nearest(Point p) {
        return null;
    }

    // return the Value associated to the Point that is closest to the given Point
    public Value getNearest(Point p) {
        return null;
    }

    // return the min and max for all Points in collection.
    // The min-max pair will form a bounding box for all Points.
    // if KDTree is empty, return null.
    public Point min() { return null; }
    public Point max() { return null; }

    // return the k nearest Points to the given Point
    public Iterable<Point> nearest(Point p, int k) {
        return null;
    }

    public Iterable<Partition> partitions() { return null; }

    // return the number of Points in KDTree
    public int size() { return 0; }

    // return whether the KDTree is empty
    public boolean isEmpty() { return true; }

    // place your timing code or unit testing here
    public static void main(String[] args) {
    }
}
