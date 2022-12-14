import java.util.Iterator;

/**
 * PSBruteForce is a Point collection that provides brute force
 * nearest neighbor searching using red-black tree.
 */

// red-black trees

public class PSBruteForce<Value> implements PointSearch<Value> {
    // constructor makes empty collection

    RedBlackBST<Point, Value> rbst;
    double minx;
    double miny;
    double maxx;
    double maxy;

    int timeCount=0;
    public PSBruteForce() {
        rbst = new RedBlackBST<>();
    }

    // add the given Point to KDTree
    public void put(Point p, Value v) {
        if(p == null) throw new NullPointerException("p must be initialized");
        rbst.put(p,v);
        if(rbst.size() == 1) {
            minx = p.x();
            miny = p.y();

            maxx = p.x();
            maxy = p.y();
        }
        else {
            if(p.x() < minx) minx = p.x();
            if(p.y() < miny) miny = p.y();

            if(p.x() > maxx) maxx = p.x();
            if(p.y() > maxy) maxy = p.y();
        }
    }

    public Value get(Point p) {
        if(p == null) throw new NullPointerException("p must be initialized");
        return rbst.get(p);
    }

    public boolean contains(Point p) {
        if(p == null) throw new NullPointerException("p must be initialized");
        return rbst.contains(p);
    }

    // return an iterable of all points in collection
    public Iterable<Point> points() {
        return rbst.keys();
    }

    // return the Point that is closest to the given Point
    public Point nearest(Point p) {
        if(p == null) throw new NullPointerException("p must be initialized");
        timeCount++;
        Point currentNear = null;
        double nearDist = 0;
        int idx = 0;
        Iterable<Point> iter = points();

        for( Point i : iter ) {

            double distance = p.dist(i);
            if(idx == 0 || distance < nearDist) {
                nearDist = distance;
                currentNear = i;
            }
            idx++;
        }
        return currentNear;
    }

    // return the Value associated to the Point that is closest to the given Point
    public Value getNearest(Point p) {
        if(p == null) throw new NullPointerException("p must be initialized");
        Point nearestPoint = nearest(p);
        return rbst.get(nearestPoint);
    }

    // return the min and max for all Points in collection.
    // The min-max pair will form a bounding box for all Points.
    // if KDTree is empty, return null.
    public Point min() {
        if(isEmpty()) return null;
        //for each iterable loop, remember the min
        return new Point(minx, miny);
    }
    public Point max() {
        if(isEmpty()) return null;
        //same as min, but with max
        return new Point(maxx, maxy);
    }


    // return the k nearest Points to the given Point
    public Iterable<Point> nearest(Point p, int k) {
        if(p == null) throw new NullPointerException("p must be initialized");
        timeCount++;
        MaxPQ<PointDist> ptpq = new MaxPQ<>();

        Iterable<Point> iter = points();
        for( Point i : iter ) {

                PointDist distance = new PointDist(i, p.dist(i));
                ptpq.insert(distance);
                while (ptpq.size()>k) ptpq.delMax();
        }
        Stack<Point> ptstack = new Stack<>();
        for(PointDist pd : ptpq) {
            ptstack.push(pd.p());
        }
        return ptstack;
    }

    public Iterable<Partition> partitions() { return null; }

    // return the number of Points in KDTree
    public int size() { return rbst.size(); }

    // return whether the KDTree is empty
    public boolean isEmpty() { return rbst.isEmpty(); }

    // place your timing code or unit testing here
    public static void main(String[] args) {
//        Point p = new Point(1.0, 1.0);
//        Point t = new Point(2.0, 2.0);
//        Point q = new Point(3.0, 3.0);
//        bf.put(p, 10);
//        bf.put(t, 20);
//        bf.put(q, 30);
//        StdOut.println(bf.nearest(new Point (4.0,4.0)));
        In in = new In("input1M.txt");
        double[] d =in.readAllDoubles();
        PSBruteForce<Integer> bf = new PSBruteForce<>();
        for(int i=0; i<d.length; i+=2) {
            bf.put(new Point(d[i], d[i+1]), i);
        }
        Stopwatch stopwatch=new Stopwatch();

        for(int i = 0; i < 1000; i++) {
            bf.nearest(new Point(StdRandom.uniform(), StdRandom.uniform()));
        }
        double time = stopwatch.elapsedTime();
        StdOut.println("Time: "+time);
        StdOut.println("Count: "+bf.timeCount);
        StdOut.println("Ratio: "+bf.timeCount/time);
    }
    }


