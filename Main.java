package hellofx;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.util.stream.Collectors;
 /* Amber Rodriguez
    * CMSC 315 - Data Structures and Analysis
    * Project 4 
    * 12.9.25
 


fourth class 
 contain the main method and should create the GUI including all the 
buttons and text fields. It should include event handlers for each of the buttons.
    */

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
      
        Graphs<Vertex> graph = new Graphs<Vertex>();
        GraphView graphView = new GraphView(graph);
        graphView.setPrefSize(550, 400);
     
       
        Text vertexlabel1 = new Text("Vertex 1");
        TextField vertex1 = new TextField();
        vertex1.setPrefWidth(50);

        Text vertexlabel2 = new Text("Vertex 2");
        TextField vertex2 = new TextField();
        vertex2.setPrefWidth(50);
   

        Button btnAddEdge = new Button("Add Edge");
        Button btnConnected = new Button("Is Connected");
        Button btnCycles = new Button("Has Cycles?");
        Button btnDFS = new Button("Depth First Search");
        Button btnBFS = new Button("Breadth First Search");

        TextField message = new TextField();
         message.setPrefWidth(100);

         btnAddEdge.setOnAction(e -> {
    Vertex v1 = graph.getVertices().stream()
        .filter(v -> v.getName().equals(vertex1.getText()))
        .findFirst().orElse(null);
    Vertex v2 = graph.getVertices().stream()
        .filter(v -> v.getName().equals(vertex2.getText()))
        .findFirst().orElse(null);

    if (v1 == null || v2 == null) {
        message.setText("Error: One or both vertices do not exist.");
    } else {
        int i1 = graph.getIndex(v1);
        int i2 = graph.getIndex(v2);
        graph.addEdge(i1, i2);
        graphView.repaintGraph();  
    }
});

       
        btnConnected.setOnAction(e -> {
            message.setText(graph.isConnected() ? "Graph is connected" : "Graph is not connected");
        });

        btnCycles.setOnAction(e -> {
            message.setText(graph.hasCycle() ? "Graph has cycles" : "Graph has no cycles");
        });

        btnDFS.setOnAction(e -> {
            if (graph.getSize() > 0) {
                Graph.SearchTree dfsTree = graph.dfs(0); 
                String order = dfsTree.getSearchOrder().stream()
                        .map(i -> graph.getVertex(i).getName())
                        .collect(Collectors.joining(", "));
                message.setText("DFS order: " + order);
            }
        });

        btnBFS.setOnAction(e -> {
            if (graph.getSize() > 0) {
                Graph.SearchTree bfsTree = graph.bfs(0); 
                String order = bfsTree.getSearchOrder().stream()
                        .map(i -> graph.getVertex(i).getName())
                        .collect(Collectors.joining(", "));
                message.setText("BFS order: " + order);
            }
        });

        HBox topButtons = new HBox(10, btnAddEdge, vertexlabel1, vertex1, vertexlabel2, vertex2);
        topButtons.setAlignment(Pos.CENTER);
        
        HBox bottomButtons = new HBox(10,btnConnected, btnCycles, btnDFS, btnBFS);
        bottomButtons.setAlignment(Pos.CENTER);

        VBox combined = new VBox(10,topButtons, graphView,bottomButtons,message);
        combined.setAlignment(Pos.CENTER);
        combined.setPrefSize(550, 600);

        Scene scene = new Scene(combined, 550, 600);
        primaryStage.setTitle("Interactive Graph Builder");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
