import java.util.Scanner;

class AdjacencyListNode
{
    public int nodeIndex;
    public int weight;

    public AdjacencyListNode next;

    AdjacencyListNode(int nodeIndex, int weight)
    {
        this.nodeIndex = nodeIndex;
        this.weight = weight;
        this.next = null;
    }
}

class AdjacencyList
{
    public AdjacencyListNode head;

    AdjacencyList()
    {
        head = null;
    }

    public void storeEdge(int nodeIndex, int weight)
    {
        if (head == null)
        {
            head = new AdjacencyListNode(nodeIndex, weight);
        }
        else
        {
            AdjacencyListNode temp;

            temp = head;
            while (temp.next != null)
            {
                temp = temp.next;
            }

            temp.next = new AdjacencyListNode(nodeIndex, weight);
        }
    }
}

class Graph
{
    private AdjacencyList adjacencyListArray[];
    public Vertex vertex[];
    public int numberOfNodes;
    private int numberOfEdges;

    Graph(int totalNodes, int totalEdges)
    {
        adjacencyListArray = new AdjacencyList[totalNodes];
        numberOfNodes = totalNodes;
        numberOfEdges = totalEdges;

        for (int i = 0; i < numberOfNodes; i++)
        {
            adjacencyListArray[i] = new AdjacencyList();
        }
    }

    public void addEdge(int startIndex, int endIndex, int weight)
    {
        adjacencyListArray[startIndex].storeEdge(endIndex, weight);
    }

    public void initializeSingleSource(Graph graph, int source)
    {
        vertex = new Vertex[graph.numberOfNodes];

        for (int i = 0; i < graph.numberOfNodes; i++)
        {
            vertex[i] = new Vertex();
            vertex[i].index = i;
            vertex[i].distance = 10000;
            vertex[i].predecessor = null;
            vertex[i].heapIndex = -1;
        }

        vertex[source].index = source;
        vertex[source].distance = 0;
    }

    public void relax(MinHeap minHeap, Vertex start, Vertex end, int weight)
    {
        if (end.distance > start.distance + weight)
        {
            end.distance = start.distance + weight;
            end.predecessor = start;

            minHeap.heapIncreaseKey(end.heapIndex);
        }
    }

    public void dijkstra(Graph graph, int source)
    {
        initializeSingleSource(graph, source);

        MinHeap minHeap = new MinHeap(graph.numberOfNodes);
        for (int i = 0; i < graph.numberOfNodes; i++)
        {
            minHeap.insert(vertex[i]);
        }

        displayTableStart(graph, source);

        while (minHeap.isEmpty() != true)
        {
            Vertex u;
            u = minHeap.extractMin();
            u.heapIndex = -1;

            AdjacencyListNode temp;
            temp = graph.adjacencyListArray[u.index].head;

            while (temp != null)
            {
                Vertex v = vertex[temp.nodeIndex];
                int w = temp.weight;

                relax(minHeap, u, v, w);

                temp = temp.next;
            }

            displayCurrentMinimumDistances(graph);
        }

        displayFinalPaths(graph, source);
    }

    public void displayTableStart(Graph graph, int source)
    {
        System.out.println("\n");
        for (int i = 0; i < graph.numberOfNodes; i++)
        {
            System.out.print("V" + (int)(i + 1) + "\t");
        }
        System.out.println("\n");

        for (int i = 0; i < graph.numberOfNodes; i++)
        {
            if (i == source)
            {
                System.out.print("0\t");
            }
            else
            {
                System.out.print("Inf\t");
            }
        }
        System.out.println("\n");
    }

    public void displayCurrentMinimumDistances(Graph graph)
    {
        for (int i = 0; i < graph.numberOfNodes; i++)
        {
            int dist = vertex[i].distance;

            if (dist == 10000)
            {
                System.out.print("Inf\t");
            }
            else
            {
                System.out.print(vertex[i].distance + "\t");
            }
        }
        System.out.println("\n");
    }

    public void displayPath(Vertex v, int source)
    {
        if (v.index == source)
        {
            System.out.print("Path :- " + (int)(v.index + 1));
            return;
        }
        else
        {
            displayPath(v.predecessor, source);
            System.out.print(" => " + (int)(v.index + 1));
        }
    }

    public void displayFinalPaths(Graph graph, int source)
    {
        System.out.println("\n");
        for (int i = 0; i < graph.numberOfNodes; i++)
        {
            Vertex v = vertex[i];

            System.out.println("For vertex V" + (int)(i + 1));

            if (v.distance == 10000)
            {
                System.out.print("No path from source to this vertex");
            }
            else
            {
                System.out.println("Shortest distance :- " + v.distance);
                displayPath(v, source);
            }
            System.out.println("\n");
        }
    }

    public void displayAdjacencyListArray()
    {
        System.out.println("\nGraph representation with adjacency list :- \n");
        for (int i = 0; i < numberOfNodes; i++)
        {
            AdjacencyListNode temp = adjacencyListArray[i].head;

            System.out.print("Vertex " + (int)(i + 1));
            if (temp == null)
            {
                System.out.print(" => null");
            }

            while (temp != null)
            {
                System.out.print(" => " + (int)(temp.nodeIndex + 1) + " / " + temp.weight);
                temp = temp.next;
            }
            System.out.println();
        }
    }
}

class Vertex
{
    public int index;
    public int distance;
    public int heapIndex;
    public Vertex predecessor;
}

class MinHeap
{
    public Vertex heap[];
    private int capacity;
    public int heapSize;

    MinHeap(int size)
    {
        heap = new Vertex[size];
        capacity = size;
        heapSize = 0;
    }

    public int parent(int index) { return (index - 1) / 2; }
    public int leftChild(int index) { return (2 * index + 1); }
    public int rightChild(int index) { return (2 * index + 2); }

    public void swapValuesAt(int i, int j)
    {
        Vertex temp;

        temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;

        int tempIndex;

        tempIndex = heap[i].heapIndex;
        heap[i].heapIndex = heap[j].heapIndex;
        heap[j].heapIndex = tempIndex;
    }

    public void heapIncreaseKey(int i)
    {
        while (i > 0 && heap[parent(i)].distance > heap[i].distance)
        {
            swapValuesAt(i, parent(i));
            i = parent(i);
        }
    }

    public void insert(Vertex v)
    {
        if (heapSize == capacity)
        {
            System.out.println("\nOverflow");
            return;
        }

        heapSize += 1;
        v.heapIndex = heapSize - 1;
        heap[heapSize - 1] = v;

        heapIncreaseKey(heapSize - 1);
    }

    public void heapify(int i)
    {
        int l = leftChild(i);
        int r = rightChild(i);
        int smallest = i;

        if (l < heapSize && heap[l].distance < heap[i].distance)
        {
            smallest = l;
        }
        if (r < heapSize && heap[r].distance < heap[smallest].distance)
        {
            smallest = r;
        }
        if (smallest != i)
        {
            swapValuesAt(i, smallest);
            heapify(smallest);
        }
    }

    public Vertex extractMin()
    {
        Vertex min;
        min = heap[0];

        swapValuesAt(0, heapSize - 1);
        heapSize -= 1;

        heapify(0);

        return min;
    }

    public boolean isEmpty()
    {
        if (heapSize == 0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}

class Dijkstra
{
    public static void main(String[] args)
    {
        Scanner in = new Scanner(System.in);

        System.out.print("Enter the number of nodes in the graph :- ");
        int numberOfNodes = in.nextInt();

        System.out.print("\nEnter the number of edges in the graph :- ");
        int numberOfEdges = in.nextInt();

        Graph graph = new Graph(numberOfNodes, numberOfEdges);

        System.out.println("\nEnter the edges :- ");
        for (int i = 0; i < numberOfEdges; i++)
        {
            int startIndex = in.nextInt();
            int endIndex = in.nextInt();
            int weight = in.nextInt();

            graph.addEdge(startIndex - 1, endIndex - 1, weight);
        }

        System.out.print("\nEnter the source :- ");
        int source = in.nextInt();

        graph.displayAdjacencyListArray();

        graph.dijkstra(graph, source - 1);
    }
}
