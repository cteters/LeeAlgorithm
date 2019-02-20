import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Queue;
import java.io.File;
import javax.imageio.ImageIO;

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

    Node(int x, int y, int dist) {
        this.x = x;
        this.y = y;
        this.dist = dist;
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
    private static int[][] BFS(int mat[][], int i, int j, int x, int y, int M, int N, int maxDist)
    {
        // construct a matrix to keep track of visited cells
        boolean[][] visited = new boolean[M][N];

        // construct a matrix to keep track of the route
        int matRoute[][] = mat;

        // create an empty queue
        Queue<Node> q = new ArrayDeque<>();

        // mark source cell as visited and enqueue the source node
        visited[i][j] = true;
        q.add(new Node(i, j, 0));

        Queue<Node> backupQ = new ArrayDeque<>();
        matRoute[i][j] = 128;

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
                matRoute[i][j] = 128;
                break;
            }

            // check for all 4 possible movements from current cell
            // and enqueue each valid movement
            for (int k = 0; k < 4; k++)
            {
                // check if it is possible to go to position
                // (i + row[k], j + col[k]) from current position
                if (isValid(mat, visited, i + row[k], j + col[k], M, N))
                {
                    // mark next cell as visited and enqueue it
                    visited[i + row[k]][j + col[k]] = true;
                    q.add(new Node(i + row[k], j + col[k], dist + 1));

                    int length = ((ArrayDeque<Node>) q).getLast().x;
                    int width = ((ArrayDeque<Node>) q).getLast().y;

                    if(length > 0 && width > 0){
                        if(length+1 < M && width +1 < N) {
                            if (matRoute[length - 1][width] +
                                    matRoute[length + 1][width] +
                                    matRoute[length][width - 1] +
                                    matRoute[length][width + 1] > 2) {
                                int endCheck = endDist(mat, length, width, x, y, M, N);
                                if (endCheck + ((ArrayDeque<Node>) q).getLast().dist == maxDist)
                                    matRoute[length][width] = 128;
                            }
                        }
                    }
                    else if(length == 0){
                       if(width +1 < N) {
                           if (matRoute[length][width -1] + matRoute[length][width +1] + matRoute[length+1][width] > 2) {
                               int endCheck = endDist(mat, length, width, x, y, M, N);
                               if (endCheck + ((ArrayDeque<Node>) q).getLast().dist == maxDist)
                                   matRoute[length][width] = 128;
                           }
                       }
                    }
                    else if(width == 0){
                        if(length +1 < M) {
                            if (matRoute[length-1][width] + matRoute[length+1][width] + matRoute[length][width+1] > 2) {
                                int endCheck = endDist(mat, length, width, x, y, M, N);
                                if (endCheck + ((ArrayDeque<Node>) q).getLast().dist == maxDist)
                                    matRoute[length][width] = 128;
                            }
                        }
                    }
                }
            }
        }


       return matRoute;
    }

    private static int endDist(int mat[][], int i, int j, int x, int y, int M, int N)
    {
        // construct a matrix to keep track of visited cells
        boolean[][] visited = new boolean[M][N];

        // create an empty queue
        Queue<Node> q = new ArrayDeque<>();

        // mark source cell as visited and enqueue the source node
        visited[i][j] = true;
        q.add(new Node(i, j, 0));

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
                    q.add(new Node(i + row[k], j + col[k], dist + 1));
                }
            }
        }

        return min_dist;
    }

    // Shortest path in a Maze
    public static void main(String[] args) throws IOException
    {
       String imageName = "combo400";

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

        if (maxDist >= 0) {
            System.out.print("The shortest path from source to destination " +
                    "has length of " + maxDist + " pixels.");
        }
        else {
            System.out.print("Destination can't be reached from given source");
        }

        System.out.println("\n\nMapping the actual route...");
        maze = BFS(maze, 0, 0, maze.length-1, maze.length-1, M, N, maxDist);

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

        System.out.println("\n\nFin~");
    }
}