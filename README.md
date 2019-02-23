# LeeAlgorithm
BFS based on the Lee Algorithm

This project was first forked from the public record:
https://www.techiedelight.com/lee-algorithm-shortest-path-in-a-maze/

This is a simple maze solving program for a class on algorithm efficiency taught at Portland State University.

The lee algorithm is a breadth-first Search algorithm that is guaranteed to find the minimum path between to locations if it exists. The one major drawback of using this algorithm, is that it does require a large amount of memory.

Currently the program handles any PNG image of equal height and width that is also black and white, that is, with RGB color values of (0,0,0) and (255,255,255). Based on the grading criteria for this project, the start and finish locations of a given maze is currently hard-coded for (0, 0) and (width -1, height -1).

Images can be loaded by adding them to the ‘example’ directory and changing line 216 of main to the name of the file.
