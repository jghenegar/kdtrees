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

    int timeCount=0;

    //point stack
    Stack<Point> ps = new Stack<>();    //doesn't account for deletions
    //stack of partitions
    Stack<Partition> sp = new Stack<>();
    // constructor makes empty kD-tree
    public PSKDTree() { }

    // add the given Point to kD-tree -- here is 2D (x and y)
    public void put(Point p, Value v) {
        if(p == null) throw new NullPointerException("p must be initialized");
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
            //add p to ps only if it's not already in the stack?
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
        if(p == null) throw new NullPointerException("p must be initialized");
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
        if(p == null) throw new NullPointerException("p must be initialized");
        return get(p)!=null; //searching again for p in the bst
    }

    public Value getNearest(Point p) {
        if(p == null) throw new NullPointerException("p must be initialized");
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
        if(p == null) throw new NullPointerException("p and root must be initialized");
        if(isEmpty()) return null;
        timeCount++;
        PointDist nearPt = new PointDist(root.p, root.p.dist(p));
        return nearest(root, p, nearPt ).p();
    }
    //this returns a pointDist so that we can access both the nearest point and the distance of
    //that point both from the one variable
    private PointDist nearest(Node current, Point p, PointDist nearPt) {
        if(current == null) return nearPt;

        //1. Check against the current point...  closer?
        if(current.p.dist(p) < nearPt.d()) {      //get distance to the query point from the partition point)
            nearPt = new PointDist(current.p, current.p.dist(p));
        }
        //2. Check which side the query point is on
        if(current.dir == Partition.Direction.DOWNUP) {
            if(p.x() > current.p.x()) {
                nearPt = nearest(current.right, p,nearPt);
            }
            else if(p.x() == current.p.x()) {
                nearPt = nearest(current.right, p,nearPt);
                nearPt = nearest(current.left, p,nearPt);
            }
            else nearPt = nearest(current.left, p,nearPt);

            //3. Check if we should check the other side!
            double partDist = current.p.x() - p.x();
            if (nearPt.d() > partDist) {
                if (p.x() > current.p.x()) nearPt = nearest(current.left, p, nearPt);
                else nearPt = nearest(current.right, p, nearPt);
            }
        }
        else {    // dir is LEFTRIGHT
            if(p.y() > current.p.y()) {
                nearPt = nearest(current.right, p,nearPt);
            }
            else if(p.y() == current.p.y()) {
                nearPt = nearest(current.left, p,nearPt);
                nearPt = nearest(current.right, p,nearPt);
            }
            else nearPt = nearest(current.left, p,nearPt);

            //3. Check if we should check the other side!
            double partDist = current.p.y() - p.y();
//            if (partDist < 0) partDist = -1 * partDist;
//            if (nearDist < 0) nearDist = -1 * nearDist;
            if (nearPt.d() > partDist) {
                if(p.y() > current.p.y()) nearPt = nearest(current.left, p,nearPt);
                else nearPt = nearest(current.right, p,nearPt);
            }
        }
        return nearPt;
    }

    // return the k nearest Points to the given Point
    public Iterable<Point> nearest(Point p, int k) {
        if(p == null) throw new NullPointerException("p must be initialized");
        timeCount++;
        //if(k <= 0) return null;
//        if(root == null) throw new NullPointerException("Null root has no nearest");
        MaxPQ<PointDist> ptpq = new MaxPQ<>();

        //add things to the global maxpq!
//        ptpq.insert(new PointDist(root.p, root.p.dist(p)));
        kNearest(root, p, k, ptpq);
        Stack<Point> ptstack = new Stack<>();
        for(PointDist pd : ptpq) {
            ptstack.push(pd.p());
        }
        return ptstack;
    }

    private void kNearest(Node current, Point p, int k, MaxPQ<PointDist> ptpq) {
        if(current == null) return;
        if(k <= 0) return;
        PointDist toAdd = new PointDist(current.p, current.p.dist(p));
        //1. Check against the current point...  closer?
        ptpq.insert(toAdd);
        if( ptpq.size() > k ) {
            ptpq.delMax();
        }

        //2. Check which side the query point is on
        if(current.dir == Partition.Direction.DOWNUP) {
            if(p.x() > current.p.x()) {
                kNearest(current.right, p, k, ptpq);
            }
            else {
                kNearest(current.left, p, k, ptpq);
            }


            //3. Check if we should check the other side!
            double partDist = current.p.x() - p.x();
            if (ptpq.max().d() > partDist || ptpq.size() < k) {
                if (p.x() > current.p.x()) kNearest(current.left, p, k, ptpq);
                else kNearest(current.right, p, k, ptpq);
            }
        }
        else {    // dir is LEFTRIGHT
            if(p.y() > current.p.y()) {
                kNearest(current.right, p, k, ptpq);
            }
            else {
                kNearest(current.left, p, k, ptpq);
            }

            //3. Check if we should check the other side!
            double partDist = current.p.y() - p.y();
//            if (partDist < 0) partDist = -1 * partDist;
//            if (nearDist < 0) nearDist = -1 * nearDist;
            if (ptpq.max().d() > partDist || ptpq.size() < k) {
                if (p.y() > current.p.y()) kNearest(current.left, p, k, ptpq);
                else kNearest(current.right, p, k, ptpq);
            }
        }
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
//        Point p = new Point(1.0, 1.0);
//        Point t = new Point(2.0, 2.0);
//        Point q = new Point(3.0, 3.0);
//        pskd.put(p, 10);
//        pskd.put(t, 20);
//        pskd.put(q, 30);
//        StdOut.println(pskd.nearest(new Point (0,0),0));

        //Point p = new Point

//        In in = new In(args[0]);
//        while(!in.isEmpty()) {
//            String var = in.readLine();
//            String[] var2 = var.split(" ");
//            double x = Double.parseDouble(var2[0]);
//            double y = Double.parseDouble(var2[1]);
//            pskd.put(new Point(x,y));
//        }
        In in = new In("input1M.txt");
        double[] d =in.readAllDoubles();
        PSKDTree<Integer> pskd = new PSKDTree<>();
        for(int i=0; i<d.length; i+=2) {
            pskd.put(new Point(d[i], d[i+1]), i);
        }
        Stopwatch stopwatch=new Stopwatch();

        for(int i = 0; i < 1000; i++) {
            pskd.nearest(new Point(StdRandom.uniform(), StdRandom.uniform()));
        }
        double time = stopwatch.elapsedTime();
        StdOut.println("Time: "+time);
        StdOut.println("Count: "+pskd.timeCount);
        StdOut.println("Ratio: "+pskd.timeCount/time);
    }

}
