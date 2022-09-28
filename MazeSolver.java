import javalib.impworld.World;
import javalib.impworld.WorldScene;
import javalib.worldimages.*;
import tester.*;

import java.awt.*;
//import java.awt.event.KeyEvent;
import java.util.*;

//represents a list of elements 
interface IList<T> extends Iterable<T> {
  // checks whether the list is empty
  boolean isEmpty();

  // returns the listLength of the list
  int listLength();

  // iterating through the list
  Iterator<T> iterator();

  // adds an element to the list
  IList<T> add(T t);

  // gets the index(first item) in the list
  T getIndex(int index);

  // append one list to another list
  IList<T> appendList(IList<T> list);

  // gets the rest of the list
  IList<T> getNext();

  // checks if there is another element left in the list
  boolean hasNext();

  // gets the element at a certain point
  T getDataPoint();

}

//empty list of elements 
class Empty<T> implements IList<T>, Iterable<T> {
  public int listLength() {
    return 0;
  }

  public boolean isEmpty() {
    return true;
  }

  public Iterator<T> iterator() {
    return new ListIterator<T>(this);
  }

  public IList<T> add(T element) {
    return new Cons<T>(element, new Empty<T>());
  }

  public T getIndex(int index) {
    return null;
  }

  public IList<T> appendList(IList<T> list) {
    return list;
  }

  public IList<T> getNext() {
    throw new IllegalArgumentException("list is empty cannot get the next element");
  }

  public boolean hasNext() {
    return false;
  }

  public T getDataPoint() {
    return null;
  }

}

//represents a list with elements 
class Cons<T> implements IList<T>, Iterable<T> {
  T first;
  IList<T> rest;

  Cons(T first, IList<T> rest) {
    this.first = first;
    this.rest = rest;
  }

  public int listLength() {
    return 1 + this.rest.listLength();
  }

  public boolean isEmpty() {
    return false;
  }

  public Iterator<T> iterator() {
    return new ListIterator<T>(this);
  }

  public IList<T> add(T element) {
    if (rest.isEmpty()) {
      rest = new Cons<T>(element, new Empty<T>());
    }
    else {
      rest = rest.add(element);
    }
    return this;
  }

  public T getIndex(int index) {
    if (index == 0) {
      return this.first;
    }
    else {
      return this.rest.getIndex(index = index - 1);
    }
  }

  public IList<T> appendList(IList<T> element) {
    if (rest.isEmpty()) {
      rest = element;
    }
    else {
      rest.appendList(element);
    }
    return this;
  }

  public IList<T> getNext() {
    return this.rest;
  }

  public boolean hasNext() {
    return true;
  }

  public T getDataPoint() {
    return this.first;
  }

}

// the iterator for the list 
class ListIterator<T> implements Iterator<T> {
  IList<T> currentNode;

  ListIterator(IList<T> currentNode) {
    this.currentNode = currentNode;
  }

  public boolean hasNext() {
    return currentNode.hasNext();
  }

  public T next() {
    T temp = this.currentNode.getDataPoint();
    this.currentNode = this.currentNode.getNext();
    return temp;
  }
}

//iterates through the dequeue 
class DequeIterator<T> implements Iterator<T> {
  ANode<T> currentNode;

  DequeIterator(ANode<T> currentNode) {
    this.currentNode = currentNode;
  }

  public boolean hasNext() {
    return currentNode.hasNext();
  }

  public T next() {
    T temp = this.currentNode.next.getDataPoint();
    this.currentNode = this.currentNode.next;
    return temp;
  }
}

//represents a deque list 
class Deque<T> implements Iterable<T> {
  Sentinel<T> header;

  public Iterator<T> iterator() {
    return new DequeIterator<T>(this.header);
  }

  Deque() {
    this.header = new Sentinel<T>();
  }

  Deque(Sentinel<T> header) {
    this.header = header;
  }

  // number of nodes in the deque list
  int size() {
    return this.header.size();
  }

  // add an element to the start of the list
  T addAtHead(T e) {
    return header.addAtHead(e);
  }

  // add an element to the end of the deque list
  T addAtBottom(T e) {
    return header.addAtBottom(e);
  }

  // remove from the head of the deque list
  T removeFromHead() {
    if (this.size() == 0) {
      throw new UnsupportedOperationException();
    }
    else {
      return header.removeFromHead();
    }
  }

  // remove from the bottom of the deque list
  T removeFromBottom() {
    if (this.size() == 0) {
      throw new UnsupportedOperationException();
    }
    else {
      return header.removeFromBottom();
    }
  }

  ANode<T> find(IPred<T> pred) {
    return header.find(pred, true);
  }

  // removes a node from the deque
  void removeNode(ANode<T> n) {
    if (!n.equals(header)) {
      header.removeNodeFirst(n);
    }
  }
}

//abstract node class
abstract class ANode<T> {
  ANode<T> next;
  ANode<T> prev;

  int size(ANode<T> n) {
    if (this.next.equals(n)) {
      return 1;
    }
    else {
      return 1 + this.next.size(n);
    }
  }

  T remove() {
    this.prev.next = this.next;
    this.next.prev = this.prev;
    return this.getDataPoint();
  }

  ANode<T> find(IPred<T> pred) {
    return this;
  }

  void removeNode(ANode<T> n) {
    if (this.equals(n)) {
      this.remove();
    }
    else {
      this.next.removeNode(n);
    }
  }

  T getDataPoint() {
    return null;
  }

  boolean hasNext() {
    return !next.isSentinel();
  }

  // checks whether the node is a sentinel
  boolean isSentinel() {
    return false;
  }
}

//represents a sentinel
class Sentinel<T> extends ANode<T> {

  Sentinel() {
    this.next = this;
    this.prev = this;
  }

  int size() {
    if (this.next.equals(this)) {
      return 0;
    }
    return this.next.size(this);
  }

  T addAtHead(T s) {
    new Node<T>(s, this.next, this);
    return this.next.getDataPoint();
  }

  T addAtBottom(T s) {
    new Node<T>(s, this, this.prev);
    return this.next.getDataPoint();
  }

  T removeFromHead() {
    return this.next.remove();
  }

  T removeFromBottom() {
    return this.prev.remove();
  }

  // finds node that meets predicate
  ANode<T> find(IPred<T> pred, boolean b) {
    return this.next.find(pred);
  }

  // goes through sentinel first to search for the node
  void removeNodeFirst(ANode<T> node) {
    this.next.removeNode(node);
  }

  // removes the node
  void removeNode(ANode<T> node) {
    return;
  }

  boolean isSentinel() {
    return true;
  }
}

//represents a node 
class Node<T> extends ANode<T> {
  T data;

  Node(T data) {
    this.data = data;
    this.next = null;
    this.prev = null;
  }

  // makes the node w/ certain parameters
  Node(T data, ANode<T> next, ANode<T> prev) {
    if ((next == null) || (prev == null)) {
      throw new UnsupportedOperationException();
    }
    this.data = data;
    this.next = next;
    this.prev = prev;
    prev.next = this;
    next.prev = this;
  }

  ANode<T> find(IPred<T> pred) {
    if (pred.apply(this.data)) {
      return this;
    }
    else {
      return this.next.find(pred);
    }
  }

  T getDataPoint() {
    return this.data;
  }
}

//certain predicate 
interface IPred<T> {
  boolean apply(T pred);
}

//compare
interface IComparator<T> {
  boolean apply(T val1, T val2);
}

//compares the weights of the edges 
class CompareEdgesWeight implements IComparator<Edge> {
  public boolean apply(Edge edge1, Edge edge2) {
    return edge1.weight < edge2.weight;
  }
}

//represents a queue data structure - first in first out 
class Queue<T> {
  Deque<T> items;

  Queue() {
    this.items = new Deque<T>();
  }

  Queue(IList<T> ts) {
    items = new Deque<T>();
    for (T t : ts) {
      items.addAtBottom(t);
    }
  }

  // essentially add at tail - enqueue adds an item to the end of the queue
  void enqueue(T element) {
    items.addAtBottom(element);
  }

  boolean isEmpty() {
    return items.size() == 0;
  }

  // removes the last element of the queue
  T dequeue() {
    return items.removeFromBottom();
  }

}

//represents stack data structure - last in first out
class Stack<T> {
  Deque<T> items;

  Stack() {
    this.items = new Deque<T>();
  }

  Stack(IList<T> ts) {
    items = new Deque<T>();
    for (T t : ts) {
      items.addAtBottom(t);
    }
  }

  // adds an item to the front of the list
  void push(T element) {
    items.addAtHead(element);
  }

  boolean isEmpty() {
    return items.size() == 0;
  }

  // removes an item from the start of the list
  T pop() {
    return items.removeFromHead();
  }
}

//search alg
abstract class Search {
  HashMap<Integer, Vertex> ourPath;

  // draws the path
  void drawPath(HashMap<Integer, Vertex> h, Vertex next) {
    while (h.containsKey(next.identify())) {
      next.isInPath = true;
      next = h.get(next.identify());
    }
  }
}

//breadth first search - use Queue
class BreadthFirst extends Search {
  Queue<Vertex> vertexList;

  BreadthFirst(IList<Vertex> v) {
    this.vertexList = new Queue<Vertex>();
    vertexList.enqueue(v.getDataPoint());
    v.getDataPoint().travelled = true;
    ourPath = new HashMap<Integer, Vertex>();
  }

  public boolean hasNext() {
    return !vertexList.isEmpty();
  }

  public Queue<Vertex> next() {
    Vertex v = vertexList.dequeue();
    for (Edge e : v.allEdges) {
      if (!e.to.travelled) {
        ourPath.put(e.to.identify(), e.from);
        if (e.to.x == MazeGameWorld.WIDTH - 1 && e.to.y == MazeGameWorld.HEIGHT - 1) {
          drawPath(ourPath, e.to);
          vertexList = new Queue<Vertex>();
        }
        else {
          e.to.travelled = true;
          vertexList.enqueue(e.to);
        }
      }
    }
    return vertexList;
  }
}

//depth first search - depth first 
class DepthFirst extends Search {
  Stack<Vertex> vertexList;

  DepthFirst(IList<Vertex> v) {
    this.vertexList = new Stack<Vertex>();
    vertexList.push(v.getDataPoint());
    v.getDataPoint().travelled = true;
    ourPath = new HashMap<Integer, Vertex>();
  }

  public boolean hasNext() {
    return !vertexList.isEmpty();

  }

  public Stack<Vertex> next() {
    Vertex v = vertexList.pop();
    for (Edge e : v.allEdges) {
      if (!e.to.travelled) {
        ourPath.put(e.to.identify(), e.from);
        if (e.to.x == MazeGameWorld.WIDTH - 1 && e.to.y == MazeGameWorld.HEIGHT - 1) {
          drawPath(ourPath, e.to);
          vertexList = new Stack<Vertex>();
        }
        else {
          vertexList.push(v);
          e.to.travelled = true;
          vertexList.push(e.to);
          break;
        }
      }
    }
    return vertexList;
  }
}

//represents a vertex
class Vertex {
  int x;
  int y;

  boolean travelled;

  boolean isInPath;

  ArrayList<Edge> allEdges;

  Vertex(int x, int y) {
    this.x = x;
    this.y = y;
    this.allEdges = new ArrayList<Edge>();
    this.travelled = false;
    this.isInPath = false;
  }

  // identify each vertex
  int identify() {
    return 500 * y + x;
  }
}

//represents an edge
class Edge {
  Vertex to;
  Vertex from;
  int weight;

  Edge(Vertex from, Vertex to, int weight) {
    this.from = from;
    this.to = to;
    this.weight = weight;
  }
}

// the game
class MazeGameWorld extends World {

  static final int WIDTH = 100;

  static final int HEIGHT = 60;

  static final int SCALE = 10;

  boolean bfs;
  boolean dfs;
  BreadthFirst b;
  DepthFirst d;

  IList<Vertex> vertices;

  IList<Edge> walls;

  MazeGameWorld() {
    setup();
  }

  // set up the maze
  void setup() {
    ArrayList<ArrayList<Vertex>> v = makeVertices();
    ArrayList<Edge> allEdges = getAllEdges(v);
    v = kruskals(v);
    walls = makeWalls(v, allEdges);
    vertices = new Empty<Vertex>();
    for (ArrayList<Vertex> vertexList : v) {
      for (Vertex vt : vertexList) {
        vertices = vertices.add(vt);
      }
    }
    bfs = false;
    dfs = false;
    b = new BreadthFirst(vertices);
    d = new DepthFirst(vertices);
  }

  // make walls for the maze
  IList<Edge> makeWalls(ArrayList<ArrayList<Vertex>> v, ArrayList<Edge> all) {
    IList<Edge> w = new Empty<Edge>();
    for (Edge e : all) {
      boolean valid = true;
      for (ArrayList<Vertex> l : v) {
        for (Vertex vt : l) {
          for (Edge e2 : vt.allEdges) {
            if (e.equals(e2) || (e.to == e2.from && e.from == e2.to)) {
              valid = false;
            }
          }
        }
      }
      if (valid) {
        w = w.add(e);
      }
    }
    return w;
  }

  // get all the edges in an array list
  ArrayList<Edge> getAllEdges(ArrayList<ArrayList<Vertex>> v) {
    ArrayList<Edge> all = new ArrayList<Edge>();
    for (ArrayList<Vertex> verts : v) {
      for (Vertex vt : verts) {
        for (Edge ed : vt.allEdges) {
          all.add(ed);
        }
      }
    }
    return all;
  }

  // arraylist of vertices
  ArrayList<ArrayList<Vertex>> makeVertices() {
    ArrayList<ArrayList<Vertex>> vertices = new ArrayList<ArrayList<Vertex>>();
    for (int x = 0; x < WIDTH; x++) {
      ArrayList<Vertex> temp = new ArrayList<Vertex>();
      for (int y = 0; y < HEIGHT; y++) {
        temp.add(new Vertex(x, y));
      }
      vertices.add(temp);
    }
    Random r = new Random();
    for (ArrayList<Vertex> vList : vertices) {
      for (Vertex v : vList) {
        if (v.x != 0) {
          v.allEdges.add(new Edge(v, vertices.get(v.x - 1).get(v.y), r.nextInt(1000)));
        }
        if (v.x != WIDTH - 1) {
          v.allEdges.add(new Edge(v, vertices.get(v.x + 1).get(v.y), r.nextInt(1000)));
        }
        if (v.y != 0) {
          v.allEdges.add(new Edge(v, vertices.get(v.x).get(v.y - 1), r.nextInt(1000)));
        }
        if (v.y != HEIGHT - 1) {
          v.allEdges.add(new Edge(v, vertices.get(v.x).get(v.y + 1), r.nextInt(1000)));
        }
      }
    }
    return vertices;
  }

  // alg
  ArrayList<ArrayList<Vertex>> kruskals(ArrayList<ArrayList<Vertex>> v) {
    ArrayList<Edge> allEdges = getAllEdges(v);
    for (ArrayList<Vertex> i : v) {
      for (Vertex j : i) {
        j.allEdges = new ArrayList<Edge>();
      }
    }
    int totalCells = HEIGHT * WIDTH;
    IList<Edge> sT = new Empty<Edge>();
    ArrayList<Edge> allEdgesSorted = sort(allEdges);
    HashMap<Integer, Integer> hash = new HashMap<Integer, Integer>();
    for (int i = 0; i <= (1000 * HEIGHT) + WIDTH; i++) {
      hash.put(i, i);
    }
    ArrayList<Edge> l = allEdgesSorted;
    while (sT.listLength() < totalCells - 1) {
      Edge e = l.get(0);
      if (this.find(hash, e.to.identify()) != this.find(hash, e.from.identify())) {
        sT = sT.add(e);
        e.from.allEdges.add(e);
        e.to.allEdges.add(new Edge(e.to, e.from, e.weight));
        int temp = (find(hash, e.to.identify()));
        hash.remove(find(hash, e.to.identify()));
        hash.put(temp, find(hash, e.from.identify()));
      }
      l.remove(0);
    }
    return v;
  }

  int find(HashMap<Integer, Integer> hashmap, int x) {
    if (hashmap.get(x) == x) {
      return x;
    }
    else {
      return find(hashmap, hashmap.get(x));
    }
  }

  // sorts list in terms of edge weight
  ArrayList<Edge> sort(ArrayList<Edge> l) {
    if (l.size() <= 1) {
      return l;
    }
    ArrayList<Edge> l1 = new ArrayList<Edge>();
    ArrayList<Edge> l2 = new ArrayList<Edge>();
    for (int i = 0; i < l.size() / 2; i++) {
      l1.add(l.get(i));
    }
    for (int i = l.size() / 2; i < l.size(); i++) {
      l2.add(l.get(i));
    }
    l1 = sort(l1);
    l2 = sort(l2);
    return merge(l1, l2);
  }

  // merge the list of edges to compare and sort
  ArrayList<Edge> merge(ArrayList<Edge> l1, ArrayList<Edge> l2) {
    ArrayList<Edge> l3 = new ArrayList<Edge>();
    IComparator<Edge> c = new CompareEdgesWeight();
    while (l1.size() > 0 && l2.size() > 0) {
      if (c.apply(l1.get(0), l2.get(0))) {
        l3.add(l1.get(0));
        l1.remove(0);
      }
      else {
        l3.add(l2.get(0));
        l2.remove(0);
      }
    }
    while (l1.size() > 0) {
      l3.add(l1.get(0));
      l1.remove(0);
    }
    while (l2.size() > 0) {
      l3.add(l2.get(0));
      l2.remove(0);
    }
    return l3;
  }

  // color of game
  Color gameColor(Vertex v) {
    if (v.x == WIDTH - 1 && v.y == HEIGHT - 1) {
      return Color.black;
    }
    else if (v.isInPath) {
      return Color.red;
    }
    else if (v.x == 0 && v.y == 0) {
      return Color.blue;
    }
    else if (v.travelled) {
      return Color.green;
    }
    else {
      return Color.white;
    }
  }

  public void onTick() {
    if (bfs) {
      if (b.hasNext()) {
        b.next();
      }
    }
    if (dfs) {
      if (d.hasNext()) {
        d.next();
      }
    }
  }

  // key pressed for breadth first or depth first or reset map
  public void onKeyEvent(String ke) {
    if (ke.equals("b")) {
      bfs = true;
      dfs = false;
      reset();
    }
    else if (ke.equals("d")) {
      bfs = false;
      dfs = true;
      reset();
    }
    else if (ke.equals("r")) {
      setup();
    }
  }

  public void reset() {
    for (Vertex v : vertices) {
      v.isInPath = false;
      v.travelled = false;
    }
    b = new BreadthFirst(vertices);
    d = new DepthFirst(vertices);
  }

  public WorldScene makeScene() {
    WorldScene w = new WorldScene(WIDTH * SCALE, HEIGHT * SCALE);
    for (Vertex v : vertices) {
      Color col = gameColor(v);
      w.placeImageXY(new RectangleImage(SCALE, SCALE, OutlineMode.SOLID, col),
          (v.x * SCALE) + (SCALE * 1 / 2), (v.y * SCALE) + (SCALE * 1 / 2));
    }
    for (Edge e : walls) {
      if (e.to.x == e.from.x) {
        w.placeImageXY(new RectangleImage(SCALE, SCALE / 10, OutlineMode.SOLID, Color.black),
            (e.to.x * SCALE) + (SCALE * 1 / 2),
            ((e.to.y + e.from.y) * SCALE / 2) + (SCALE * 1 / 2));
      }
      else {
        w.placeImageXY(new RectangleImage(SCALE / 10, SCALE, OutlineMode.SOLID, Color.black),
            ((e.to.x + e.from.x) * SCALE / 2) + (SCALE * 1 / 2),
            (e.to.y * SCALE) + (SCALE * 1 / 2));
      }
    }
    return w;
  }
}

//examples of the maze game and the testers 
class ExamplesMazeGame {

  // test the add method
  void testAdd(Tester t) {
    IList<String> s = new Empty<String>();
    t.checkExpect(s.add("mazegame"), new Cons<String>("mazegame", new Empty<String>()));
    t.checkExpect(s.add("mazegame").add("finished"),
        new Cons<String>("mazegame", new Cons<String>("finished", new Empty<String>())));
    t.checkExpect(s.add(""), new Cons<String>("", new Empty<String>()));
    t.checkExpect(s.add("").add("Dhilan").add("Damian"), new Cons<String>("",
        new Cons<String>("Dhilan", new Cons<String>("Damian", new Empty<String>()))));
  }

  // test listLength method
  void testlistLength(Tester t) {
    IList<String> mtList = new Empty<String>();
    IList<String> consList = new Cons<String>("MazeGame",
        new Cons<String>("is", new Cons<String>("difficult", new Empty<String>())));
    IList<String> consList2 = new Cons<String>("MazeGame", new Cons<String>("is",
        new Cons<String>("very", new Cons<String>("difficult", new Empty<String>()))));

    t.checkExpect(mtList.listLength(), 0);
    t.checkExpect(consList.listLength(), 3);
    t.checkExpect(consList2.listLength(), 4);
  }

  // test append list method
  void testAppendList(Tester t) {
    IList<String> mtList = new Empty<String>();
    IList<String> consList = new Cons<String>("MazeGame",
        new Cons<String>("is", new Cons<String>("difficult", new Empty<String>())));
    IList<String> consList2 = new Cons<String>("MazeGame", new Cons<String>("is",
        new Cons<String>("very", new Cons<String>("difficult", new Empty<String>()))));
    IList<String> combinedList = new Cons<String>("MazeGame",
        new Cons<String>("is",
            new Cons<String>("very", new Cons<String>("difficult", new Cons<String>("MazeGame",
                new Cons<String>("is", new Cons<String>("difficult", new Empty<String>())))))));

    t.checkExpect(mtList.appendList(consList), consList);
    t.checkExpect(consList.appendList(mtList), consList);
    t.checkExpect(consList2.appendList(consList), combinedList);

  }

  // test get Data method
  void testgetDataPoint(Tester t) {
    IList<String> mtList = new Empty<String>();
    IList<String> consList = new Cons<String>("MazeGame",
        new Cons<String>("is", new Cons<String>("difficult", new Empty<String>())));
    IList<String> consList2 = new Cons<String>("MazeGame", new Cons<String>("is",
        new Cons<String>("very", new Cons<String>("difficult", new Empty<String>()))));

    t.checkExpect(mtList.getDataPoint(), null);
    t.checkExpect(consList.getDataPoint(), "MazeGame");
    t.checkExpect(consList2.getDataPoint(), "MazeGame");

  }

  // test get next method
  void testGetNext(Tester t) {
    IList<String> mtList = new Empty<String>();
    IList<String> consList = new Cons<String>("MazeGame",
        new Cons<String>("is", new Cons<String>("difficult", new Empty<String>())));
    IList<String> consList2 = new Cons<String>("MazeGame", new Cons<String>("is",
        new Cons<String>("very", new Cons<String>("difficult", new Empty<String>()))));

    t.checkExpect(consList.getNext(),
        new Cons<String>("is", new Cons<String>("difficult", mtList)));
    t.checkExpect(consList2.getNext(),
        new Cons<String>("is", new Cons<String>("very", new Cons<String>("difficult", mtList))));
  }

  // test identify function
  void testId(Tester t) {
    Vertex vertex = new Vertex(0, 0);
    Vertex vertex2 = new Vertex(2, 9);
    Vertex vertex3 = new Vertex(3, 4);

    t.checkExpect(vertex.identify(), 0);
    t.checkExpect(vertex2.identify(), 4502);
    t.checkExpect(vertex3.identify(), 2003);

  }

  // test merge method
  void testMerge(Tester t) {
    MazeGameWorld maze = new MazeGameWorld();

    Edge edge1 = new Edge(null, null, 35);
    Edge edge2 = new Edge(null, null, 14);
    Edge edge3 = new Edge(null, null, 10);
    Edge edge4 = new Edge(null, null, 30);
    Edge edge5 = new Edge(null, null, 23);

    ArrayList<Edge> list1 = new ArrayList<Edge>();
    ArrayList<Edge> list2 = new ArrayList<Edge>();
    ArrayList<Edge> mergedList = new ArrayList<Edge>();

    list1.add(edge1);
    list1.add(edge2);
    list1.add(edge3);

    list2.add(edge4);
    list2.add(edge5);

    mergedList.add(edge4);
    mergedList.add(edge5);
    mergedList.add(edge1);
    mergedList.add(edge2);
    mergedList.add(edge3);

    t.checkExpect(maze.merge(list1, list2), mergedList);
  }

  // test sort method
  void testSort(Tester t) {
    MazeGameWorld maze = new MazeGameWorld();

    Edge edge1 = new Edge(null, null, 35);
    Edge edge2 = new Edge(null, null, 14);
    Edge edge3 = new Edge(null, null, 10);
    Edge edge4 = new Edge(null, null, 30);
    Edge edge5 = new Edge(null, null, 23);

    ArrayList<Edge> notSorted = new ArrayList<Edge>();
    ArrayList<Edge> sorted = new ArrayList<Edge>();

    notSorted.add(edge1);
    notSorted.add(edge2);
    notSorted.add(edge3);
    notSorted.add(edge4);
    notSorted.add(edge5);

    sorted.add(edge3);
    sorted.add(edge2);
    sorted.add(edge5);
    sorted.add(edge4);
    sorted.add(edge1);

    t.checkExpect(maze.sort(notSorted), sorted);
  }

  // test game world
  void testMazeGameWorld(Tester t) {
    MazeGameWorld maze = new MazeGameWorld();
    t.checkExpect(maze.vertices.listLength(), MazeGameWorld.WIDTH * MazeGameWorld.HEIGHT);

    HashMap<Integer, Integer> h = new HashMap<Integer, Integer>();
    h.put(3, 3);
    h.put(4, 3);

    t.checkExpect(maze.find(h, 3), 3);
    t.checkExpect(maze.find(h, 4), 3);
  }

  void testGame(Tester t) {
    MazeGameWorld m = new MazeGameWorld();
    m.bigBang(MazeGameWorld.WIDTH * MazeGameWorld.SCALE, MazeGameWorld.HEIGHT * MazeGameWorld.SCALE,
        0.005);
  }
}
