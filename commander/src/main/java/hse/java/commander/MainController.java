package hse.java.commander;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

public class MainController {

    @FXML
    public Button move;

    public void initialize() {
        move.setOnMouseClicked(event -> {

        });
        System.out.println(System.getProperty("user.home"));
       left.getItems().add("Kek");

       left.setOnMouseClicked(event -> {
           if (event.getClickCount() == 2) {
               int index = left.getSelectionModel().getSelectedIndex();
               if (index >= 0) {
                   left.getItems().set(index, "clicked");
               }
           }
       });
   }

    @FXML
    public ListView<String> left;

    @FXML
    public ListView<String> right;


}
