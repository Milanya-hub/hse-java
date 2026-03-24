module hse.java.commander {
    requires javafx.controls;
    requires javafx.fxml;
    requires static lombok;

    opens hse.java.commander to javafx.fxml;
    exports hse.java.commander;
}