package hellofx;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.scene.Group;

/*
 /* Amber Rodriguez
    * CMSC 315 - Data Structures and Analysis
    * Project 4 
    * 12.9.25
    * 
    * 
The third class 

an extension of the javafx Pane class that visually displays the 
graph. It contains an event handler that responds to mouse clicks that creates new vertices 
and a method that is called to draw edges. */

    public class GraphView extends Pane { 
   
    private Graphs<Vertex> graph;  
    private Group group = new Group();
    private char nextLabel = 'A';

    

    public GraphView(Graphs <Vertex>graph) {
        
        this.graph = graph;
        this.getChildren().add(group);
        
        this.setOnMouseClicked(e -> {
            if (nextLabel <= 'Z') {
                Vertex v = new Vertex(String.valueOf(nextLabel), e.getX(), e.getY());
                graph.addVertex(v);
                nextLabel++;
                    repaintGraph();
            }
        });
    }

    
  
  public void repaintGraph() { 
    
    group.getChildren().clear(); 

   // Draw vertices
        for (Vertex v : graph.getVertices()) {
            group.getChildren().add(new Circle(v.getX(), v.getY(), 16));
            group.getChildren().add(new Text(v.getX() - 8, v.getY() - 18, v.getName()));
        }

        for (int i = 0; i < graph.getSize(); i++) {
        Vertex v1 = graph.getVertex(i);
        for (int j : graph.getNeighbors(i)) {
        Vertex v2 = graph.getVertex(j);
        if (i < j) { 
            group.getChildren().add(new Line(v1.getX(), v1.getY(), v2.getX(), v2.getY()));
        }
    }
}
  }
    


    public Graphs<Vertex> getGraph() {
        return graph;
    }
}
