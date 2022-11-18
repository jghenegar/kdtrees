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

    double minx;
    double miny;
    double maxx;
    double maxy;
    Node root=null;

    //point stack
    Stack<Point> ps = new Stack<>();    //doesn't account for deletions
    //stack of partitions
    Stack<Partition> sp = new Stack<>();
    // constructor makes empty kD-tree
    public PSKDTree() {
        kdt = new BST<Point, Value>();
    }

    // add the given Point to kD-tree -- here is 2D (x and y)
    public void put(Point p, Value v) {  //change?? remember to alternate x and y coords for inserting
        if (v == null) { return; } // can't do anything with a null value

        if(root==null) {
            Node x = new Node(p,v);
            x.left=null;
            x.right=null;
            x.dir= Partition.Direction.DOWNUP;
            root=x;
            minx = x.p.x();
            miny = x.p.y();

            maxx = x.p.x();
            maxy = x.p.y();
            ps.push(x.p);
            sp.push(new Partition(x.p,x.p,x.dir));
        }
        else put(root, p, v, root);
        count++;
    }

    private Node put(Node x, Point p, Value v, Node parent) {
        if (x == null) {
            Node newNode = new Node(p,v);
            if(parent.dir == Partition.Direction.DOWNUP) {
                newNode.dir = Partition.Direction.LEFTRIGHT;
                newNode.left=null;
                newNode.right=null;
            }
            else {
                newNode.dir = Partition.Direction.DOWNUP;
                newNode.left=null;
                newNode.right=null;
            }
            if(newNode.p.x() < minx) minx = newNode.p.x();
            if(newNode.p.y() < miny) miny = newNode.p.y();

            if(newNode.p.x() > maxx) maxx = newNode.p.x();
            if(newNode.p.y() > maxy) maxy = newNode.p.y();
            ps.push(p);
            sp.push(new Partition(newNode.p,newNode.p,newNode.dir));
            return newNode;
        }
        else {
            Partition.Direction parentDir = x.dir;

            if (parentDir == Partition.Direction.DOWNUP) {
                if (x.p.x() > p.x()) x.left = put(x.left, p, v, x);
                else x.right = put(x.right, p, v, x);
            } else {
                if (x.p.y() > p.y()) x.left = put(x.left, p, v, x);
                else x.right = put(x.right, p, v, x);
            }
            return x;
        }
    }

    public Value get(Point p) {
        if(isEmpty()) return null;
        return get(root, p, root); //search through the tree instead of returning null
    }

    private Value get(Node current, Point p, Node parent) {
        if(current==null) return null;
        if(parent.dir == Partition.Direction.DOWNUP) {
            if (parent.p.x() < current.p.x()) return get(parent.right, p, parent);
            else if (parent.p.x() > current.p.x()) return get(parent.left, p, parent);
            else return current.v;
        }
        else {
            if (parent.p.y() < current.p.y()) return get(parent.right, p, parent);
            else if (parent.p.y() > current.p.y()) return get(parent.left, p, parent);
            else return current.v;
        }
    }

    public boolean contains(Point p) {
        return get(p)!=null; //searching again for p in the bst
    }

    public Value getNearest(Point p) {
        if(isEmpty()) return null;
        Point nearestPoint = nearest(p);
        return get(nearestPoint);
    }

    // return an iterable of all points in collection
    public Iterable<Point> points() { return ps; }

    // return an iterable of all partitions that make up the kD-tree
    //we didn't know what to do with two points in the partition constructor,
    //so the points in each partition are the same
    public Iterable<Partition> partitions() { return sp; }

    // return the Point that is closest to the given Point
    public Point nearest(Point p) {
        Point currentNear = root.p;
        double nearDist = p.dist(root.p);


        return currentNear;
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
        return new Point(minx,miny);
    }
    public Point max() {
        if(isEmpty()) return null;
        return new Point(maxx,maxy);
    }

    // return the number of Points in kD-tree
    public int size() { return count; }

    // return whether the kD-tree is empty
    public boolean isEmpty() { return count==0; }

    // place your timing code or unit testing here
    public static void main(String[] args) {
    }

}
