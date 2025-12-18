
package hellofx;
 /* Amber Rodriguez
    * CMSC 315- Data Structures and Analysis
    * Project 4 
    * 12.9.25


first class 
 an immutable class that defines a vertex of the graph and contains the x and y coordinates of the vertex along with its 
name.
 */
public class Vertex {
    private double x, y;
    private String name;

    public Vertex(String name, double x, double y) {
        this.name = name;
        this.x = x;
        this.y = y;
    }

    public double getX() { 
        return x; 
    }

    public double getY() { 
        return y; 
    }

    public String getName() { 
        return name; 

    }

}



