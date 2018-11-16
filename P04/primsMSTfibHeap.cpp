#include <bits/stdc++.h>

using namespace std;

class FibHeapNode
{
    public:

        int index;
        int key;
        int predecessor;
        int degree;
        bool mark;

        FibHeapNode* parent;
        FibHeapNode* child;
        FibHeapNode* left;
        FibHeapNode* right;
};

class FibHeap
{
    public:

        int n;
        FibHeapNode* min;

        FibHeap* makeFibHeap()
        {
            FibHeap* H = new FibHeap;
            H->min = NULL;
            H->n = 0;

            return H;
        }

        void fibHeapInsert(FibHeap* H, FibHeapNode* x)
        {
            x->degree = 0;
            x->parent = NULL;
            x->child = NULL;
            x->mark = false;

            if (H->min == NULL)
            {
                x->right = x;
                x->left = x;

                H->min = x;
            }
            else
            {
                insertInList(H->min, x);
                if (x->key < H->min->key)
                {
                    H->min = x;
                }
            }
            H->n += 1;
        }

        void insertInList(FibHeapNode* a, FibHeapNode* b)
        {
            FibHeapNode* temp = a->right;
            a->right = b;
            b->left = a;
            b->right = temp;
            temp->left = b;
        }

        void addListToRootList(FibHeapNode* a, FibHeapNode* x)
        {
            if (x != NULL)
            {
                FibHeapNode* temp = x;
                do
                {
                    temp->parent = NULL;
                    temp = temp->right;
                }
                while (temp != x);

                FibHeapNode* b = a->right;
                FibHeapNode* y = x->right;

                a->right = y;
                x->right = b;
                b->left = x;
                y->left = a;
            }
        }

        FibHeapNode* fibHeapExtractMin(FibHeap* H)
        {
            FibHeapNode* z = H->min;
            if (z != NULL)
            {
                FibHeapNode* x = z->child;
                addListToRootList(H->min, x);
                z->child = NULL;
                z->degree = 0;

                z->left->right = z->right;
                z->right->left = z->left;
                if (z == z->right)
                {
                    H->min = NULL;
                }
                else
                {
                    H->min = z->right;
                    consolidate(H);
                }
                H->n -= 1;
            }

            return z;
        }

        void consolidate(FibHeap* H)
        {
            int D = H->n;
            FibHeapNode* A[D];
            for (int i = 0; i <= D; i++)
            {
                A[i] = NULL;
            }

            FibHeapNode* arr[10000];
            FibHeapNode* temp = H->min;
            int count = 0;
            do
            {
                arr[count] = temp;
                count += 1;
                temp = temp->right;
            }
            while (temp != H->min);

            FibHeapNode* w = H->min;
            for (int i = 0; i < count; i++)
            {
                w = arr[i];
                FibHeapNode* x = w;
                int d = x->degree;
                while (A[d] != NULL)
                {
                    FibHeapNode* y = A[d];
                    if (x->key > y->key)
                    {
                        FibHeapNode* temp = x;
                        x = y;
                        y = temp;
                    }
                    fibHeapLink(H, y, x);
                    A[d] = NULL;
                    d = d + 1;
                }
                A[d] = x;
            }

            H->min = NULL;

            for (int i = 0; i <= D; i++)
            {
                if (A[i] != NULL)
                {
                    if (H->min == NULL)
                    {
                        H->min = A[i];
                        H->min->right = H->min;
                        H->min->left = H->min;
                    }
                    else
                    {
                        FibHeapNode* temp = H->min->right;
                        H->min->right = A[i];
                        A[i]->left = H->min;
                        A[i]->right = temp;
                        temp->left = A[i];

                        if (A[i]->key < H->min->key)
                        {
                            H->min = A[i];
                        }
                    }
                }
            }
        }

        void fibHeapLink(FibHeap* H, FibHeapNode* y, FibHeapNode* x)
        {
            y->left->right = y->right;
            y->right->left = y->left;

            y->parent = x;
            y->left = y;
            y->right = y;
            if (x->child != NULL)
            {
                insertInList(x->child, y);
            }
            else
            {
                x->child = y;
                y->right = y;
                y->left = y;
            }
            x->degree += 1;
            y->mark = false;
        }

        void fibHeapDecreaseKey(FibHeap* H, FibHeapNode* x, int k)
        {
            x->key = k;
            FibHeapNode* y = x->parent;

            if (y != NULL && x->key < y->key)
            {
                cut(H, x, y);
                cascadingCut(H, y);
            }

            if (x->key < H->min->key)
            {
                H->min = x;
            }
        }

        void cut(FibHeap* H, FibHeapNode* x, FibHeapNode* y)
        {
            if (y->child == x)
            {
                y->child = x->right;
                if (x == x->right)
                {
                    y->child = NULL;
                }
            }
            x->right->left = x->left;
            x->left->right = x->right;
            y->degree -= 1;
            insertInList(H->min, x);
            x->parent = NULL;
            x->mark = false;
        }

        void cascadingCut(FibHeap* H, FibHeapNode* y)
        {
            FibHeapNode* z = y->parent;
            if (z != NULL)
            {
                if (y->mark == false)
                {
                    y->mark = true;
                }
                else
                {
                    cut(H, y, z);
                    cascadingCut(H, z);
                }
            }
        }

        bool isEmpty(FibHeap* H)
        {
            if (H->min == NULL)
            {
                return true;
            }
            else
            {
                return false;
            }
        }
};


class Graph
{
    private:

        int noOfNodes;
        int noOfEdges;
        bool directed;

        FibHeapNode** vertices;
        vector <pair<int, int> >* adjList;

    public:

        Graph()
        {
            cout << "Enter the number of nodes and number of edges :- ";
            cin >> noOfNodes >> noOfEdges;

            adjList = new vector <pair<int, int> >[noOfNodes];

            cout << "\nEnter the edges :- \n" << endl;
            int u, v, w;
            for (int i = 0; i < noOfEdges; i++)
            {
                cin >> u >> v >> w;
                addEdge(u - 1, v - 1, w);
            }

            cout << "\nEnter the starting node :- ";
            int source;
            cin >> source;

            primsMST(source - 1);
        }

        void addEdge(int u, int v, int w)
        {
            adjList[v].push_back(make_pair(u, w));
            adjList[u].push_back(make_pair(v, w));
        }

        void primsMST(int source)
        {
            FibHeapNode* nodeArr[noOfNodes];
            FibHeap fh;
            FibHeap* H = fh.makeFibHeap();
            for (int i = 0; i < noOfNodes; i++)
            {
                FibHeapNode* x = new FibHeapNode;
                x->index = i;
                x->key = INT_MAX;
                x->predecessor = -1;

                if (i == source)
                {
                    x->key = 0;
                }
                nodeArr[i] = x;
                fh.fibHeapInsert(H, x);
            }

            int visited[noOfNodes] = {0};
            int totalWeight = 0;
            while (!fh.isEmpty(H))
            {
                FibHeapNode* u = fh.fibHeapExtractMin(H);
                if (visited[u->index] != 1)
                {
                    visited[u->index] = 1;
                    totalWeight += u->key;

                    for (auto vw = adjList[u->index].begin(); vw != adjList[u->index].end(); vw++)
                    {
                        int v = vw->first;
                        int w = vw->second;
                        if (visited[v] == 0 && w < nodeArr[v]->key)
                        {
                            nodeArr[v]->predecessor = u->index;
                            fh.fibHeapDecreaseKey(H, nodeArr[v], w);
                        }
                    }
                }
            }

            displayMST(nodeArr);
            cout << "\nSum of weights of edges of MST :- " <<  totalWeight << endl;
        }

        void displayMST(FibHeapNode** arr)
        {
            cout << "\nThe Edges to be taken are :- \n" << endl;
            for (int i = 0; i < noOfNodes; i++)
            {
                if (arr[i]->predecessor != -1)
                {
                    cout << i + 1 << " " << arr[i]->predecessor + 1 << endl;
                }
            }
        }

        ~Graph()
        {
            delete[] adjList;
        }
};

int main()
{
    Graph graph;

    return 0;
}
