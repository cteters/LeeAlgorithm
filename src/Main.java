import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Queue;
import java.io.File;
import javax.imageio.ImageIO;

import java.util.*;

/**
 *   THE LEE ALGORITHM:
 *
 *      A Breadth-First search method that was developed by C. Y. Lee and first published
 *  in 1961 under the title "An Algorithm for Path Connections and Its Applications", in
 *  the IRE Transactions on Electronic Computers, EC-10 (2) 346-365. The basic algorithm
 *  is as follows:
 *
 *  1) Initialization
 *      - Select start point, mark with 0
 *      - i := 0
 *
 *  2)  Wave expansion
 *          REPEAT
 *              - Mark all unlabeled neighbors of points marked with i with i+1
 *              - i := i+1
 *          UNTIL ((target reached) or (no points can be marked))
 *
 *  3)  Backtrace
 *          - go to the target point
 *          REPEAT
 *              - go to the next node that has a lower mark than the current node
 *              - add this node to path
 *          UNTIL (start point reached)
 *
 *  4)  Clearance
 *          - block the path for the future wirings
 *          - delete all marks
**/

// queue node used in BFS
class Node
{
    // (x, y) represents matrix cell coordinates
    // dist represent its minimum distance from the source
    int x, y, dist;
    boolean start;

    Node(int x, int y, int dist, boolean start) {
        this.x = x;
        this.y = y;
        this.dist = dist;
        this.start = start;
    }
}

class Main
{

    // Below arrays details all 4 possible movements from a cell
    private static final int row[] = { -1, 0, 0, 1 };
    private static final int col[] = { 0, -1, 1, 0 };

    // Function to check if it is possible to go to position (row, col)
    // from current position. The function returns false if (row, col)
    // is not a valid position or has value 0 or it is already visited
    private static boolean isValid(int mat[][], boolean visited[][], int row, int col, int M, int N)
    {
        return (row >= 0) && (row < M) && (col >= 0) && (col < N)
                && mat[row][col] == 1 && !visited[row][col];
    }

    // Find Shortest Possible Route in a matrix mat from source
    // cell (i, j) to destination cell (x, y)
    private static int[][] BFS(int mat[][], int i, int j, int x, int y, int M, int N)
    {
        // construct a matrix to keep track of visited cells
        boolean[][] visited = new boolean[M][N];

        // construct a matrix to keep track of the route
        int matRoute[][] = mat;

        // create an empty queue
        Queue<Node> q = new ArrayDeque<>();

        // mark source cell as visited and enqueue the source node
        visited[i][j] = true;
        q.add(new Node(i, j, 0, true));

        Queue<Node> backupQ = new ArrayDeque<>();
        matRoute[i][j] = 128;

        // run till queue is not empty
        while (!q.isEmpty())
        {
           // Retrieves and removes the head of the queue represented by this deque
           Node node = q.poll();

           backupQ.add(node);

            // (i, j) represents current cell and dist stores its
            // minimum distance from the source
            i = node.x;
            j = node.y;
            int dist = node.dist;

            // if destination is found, update min_dist and stop
            if (i == x && j == y){
                matRoute[i][j] = 128;
                break;
            }

            // check for all 4 possible movements from current cell
            // and enqueue each valid movement
            for (int k = 0; k < 4; k++)
            {
                // check if it is possible to go to position
                if (isValid(mat, visited, i + row[k], j + col[k], M, N))
                {
                    // mark next cell as visited and enqueue it
                    visited[i + row[k]][j + col[k]] = true;

                    //q.add(new Node(i + row[k], j + col[k], dist + 1));
                    q.add(new Node(i + row[k], j + col[k], dist + 1, false));

                }
            }
        }

        Node rear = ((ArrayDeque<Node>) backupQ).pollLast();
        Node prev = ((ArrayDeque<Node>) backupQ).pollLast();

        while (!backupQ.isEmpty()){

           if (rear.x == prev.x || rear.y == prev.y) {
               if (rear.x == prev.x - 1 || rear.x == prev.x + 1 || rear.y == prev.y - 1 || rear.y == prev.y + 1) {
                   matRoute[prev.x][prev.y] = 128;
                   // uncomment to see the coordinates of the route printed
                   // System.out.println("(" + prev.x + " , " + prev.y + ")");
                   rear = prev;
               }
           }
           prev = ((ArrayDeque<Node>) backupQ).pollLast();
        }

        return matRoute;
    }


    // Find Shortest Possible Route in a matrix mat from source
    // cell (i, j) to destination cell (x, y)
    private static Map<String, Node> BFS(int mat[][], int i, int j, int x, int y, int M, int N, int foo)
    {
        // construct a matrix to keep track of visited cells
        boolean[][] visited = new boolean[M][N];

        // construct a matrix to keep track of the route
        Map<String, Node> closed = new HashMap<String, Node>();

        // create an empty queue
        Queue<Node> q = new ArrayDeque<>();

        // mark source cell as visited and enqueue the source node
        visited[i][j] = true;
        closed.put( Integer.toString(i) + " " + Integer.toString(j), new Node(i, j, 0, true) );
        q.add(new Node(i, j, 0, true));

        // run till queue is not empty
        while (!q.isEmpty())
        {
            // Retrieves and removes the head of the queue represented by this deque
            Node node = q.poll();

            // (i, j) represents current cell and dist stores its
            // minimum distance from the source
            i = node.x;
            j = node.y;
            int dist = node.dist;

            // check for all 4 possible movements from current cell
            // and enqueue each valid movement
            for (int k = 0; k < 4; k++)
            {
                // check if it is possible to go to position
                if (isValid(mat, visited, i + row[k], j + col[k], M, N))
                {
                    // mark next cell as visited and enqueue it
                    visited[i + row[k]][j + col[k]] = true;

                     int tempI = i + row[k];
                     int tempJ = j + col[k];

                     //add next coordinates and current node to closed map
                     closed.put(Integer.toString(tempI)+ " " + Integer.toString(tempJ), node);

                    q.add(new Node(i + row[k], j + col[k], dist + 1, false));
                }
            }
        }

        return closed;
    }

    private static int endDist(int mat[][], int i, int j, int x, int y, int M, int N)
    {
        // construct a matrix to keep track of visited cells
        boolean[][] visited = new boolean[M][N];

        // create an empty queue
        Queue<Node> q = new ArrayDeque<>();

        // mark source cell as visited and enqueue the source node
        visited[i][j] = true;
        q.add(new Node(i, j, 0, true));

        Queue<Node> backupQ = new ArrayDeque<>();

        // stores length of longest path from source to destination
        int min_dist = Integer.MAX_VALUE;

        // run till queue is not empty
        while (!q.isEmpty())
        {
            // Retrieves and removes the head of the queue represented by this deque
            Node node = q.poll();

            backupQ.add(node);
            Node prev;
            if(node != null) {
                prev = node;
                backupQ.add(prev);
            }

            // (i, j) represents current cell and dist stores its
            // minimum distance from the source
            i = node.x;
            j = node.y;
            int dist = node.dist;

            // if destination is found, update min_dist and stop
            if (i == x && j == y){
                min_dist = dist;
                break;
            }

            // check for all 4 possible movements from current cell
            // and enqueue each valid movement
            for (int k = 0; k < 4; k++)
            {
                // check if it is possible to go to position
                if (isValid(mat, visited, i + row[k], j + col[k], M, N))
                {
                    // mark next cell as visited and enqueue it
                    visited[i + row[k]][j + col[k]] = true;
                    q.add(new Node(i + row[k], j + col[k], dist + 1, false));
                }
            }
        }

        return min_dist;
    }


    // Shortest path in a Maze
    public static void main(String[] args) throws IOException
    {
       String imageName = "perfect2k";

        if(imageName.charAt(imageName.length()-4) != '.')
            imageName = imageName + ".png";

        File file;
        String address = System.getProperty("user.dir");
        if(address.charAt(0) == '/')
            file = new File((System.getProperty("user.dir")) + "/examples/" + imageName);
        else
            file = new File((System.getProperty("user.dir")) + "\\examples\\" + imageName);
        EasyBufferedImage image = EasyBufferedImage.createImage(file);
        System.out.println("File Size in bytes "+file.length());

        int[][] maze = image.getPixels2D(1);
        int M = maze.length;
        int N = maze.length;

        for(int i = 0; i < maze.length; i++)
            for(int j = 0; j < maze.length; j++)
                if(maze[i][j] == 255)
                    maze[i][j] =1;

        System.out.println("\nFinding the length to the shortest possible path...");
        int maxDist = endDist(maze, 0, 0, maze.length-1, maze.length-1, M, N);

        long runTime = System.currentTimeMillis();

        if (maxDist >= 0) {
            System.out.print("The shortest path from start to finish " +
                    "has length of " + maxDist + " pixels.");

            System.out.println("\n\nMapping the actual route...");

            // Uncomment to see a version of the code that uses hash maps
            //Map<String, Node> shortestRoute = BFS(maze, 0, 0, maze.length-1, maze.length-1, M, N, 0);
            //convertPath(M, N, shortestRoute, maze);

            // Uncomment to see a version of this version of the code activated for solving the path
            maze = BFS(maze, 0, 0, maze.length-1, maze.length-1, M, N);


            System.out.print("\nThe run time to record the path took approximately ");
            System.out.println((System.currentTimeMillis()-runTime)/1000 + " seconds.");

            System.out.print("\nThe actual path is currently stored as a 2D array");
            System.out.print(" and can be converted to an image, but this can take more than a minute");
            System.out.print(" depending on the size of the array.");

            System.out.println("\n\nConverting to image, please wait...\n");

            for(int i = 0; i < maze.length; i++)
                for(int j = 0; j < maze.length; j++)
                    if(maze[i][j] == 1)
                        maze[i][j] = 255;


            EasyBufferedImage bufferedImage = EasyBufferedImage.createImage(maze);
            String name = "solution.png";
            File myNewPNGFile = new File(name);
            ImageIO.write(bufferedImage, "PNG", myNewPNGFile);

            System.out.print("File highlighting the shortest path can be found at:");
            System.out.print(System.getProperty("user.dir") + "/" + name);
        }
        else {
            System.out.print("Destination can't be reached from given source");
        }

        System.out.println("\n\nFin~");
    }


    public static int [][] convertPath(int M, int N, Map<String, Node> closed, int [][] mat ){

        int [][] matRoute = mat;
        //List<String> path = new ArrayList<String>();
        String location = Integer.toString(M-1) + " " + Integer.toString(N-1);
        Node node = closed.get(location);
        //path.add(location);
        //path.add(node.x + " " + node.y);

        while(node.start != true) {
            location = Integer.toString(node.x) + " " + Integer.toString(node.y);
            node = closed.get(location);
            //path.add(node.y + " " + node.x);
            matRoute[node.x][node.y] = 128;
        }

        //Collections.reverse(path);

        //System.out.print("The Path is: ");
        //System.out.println(path);
        //System.out.println("With a length of: " + path.size());

        return matRoute;
    }
}
