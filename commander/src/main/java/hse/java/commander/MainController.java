package hse.java.commander;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import java.lang.Throwable;
import java.nio.file.*;
import java.util.stream.Stream;

public class MainController {

    @FXML
    public ListView<String> left;

    @FXML
    public ListView<String> right;

    @FXML
    public Button copy;

    @FXML
    public Button move;

    @FXML
    public Button delete;

    private Path leftPath;
    private Path rightPath;
    private ListView<String> Panel;

    // for testing
    public void setInitialDirs(Path leftStart, Path rightStart) {
        this.leftPath = leftStart;
        this.rightPath = rightStart;
        update();
    }

    public void initialize() {
        if (leftPath == null) {
            leftPath = Paths.get(System.getProperty("user.home"));
        }
        if (rightPath == null) {
            rightPath = Paths.get(System.getProperty("user.home"));
        }
        Panel = left;
        update();

        left.setOnMouseClicked(e -> {
            Panel = left;
            if (e.getClickCount() == 2) {
                open(left);
            }
        });

        right.setOnMouseClicked(e -> {
            Panel = right;
            if (e.getClickCount() == 2) {
                open(right);
            }
        });

        copy.setOnAction(e -> copy());
        move.setOnAction(e -> move());
        delete.setOnAction(e -> delete());
    }

    private void dir(ListView<String> panel, Path path) {
        panel.getItems().clear();
        panel.getItems().add("...");
        try (Stream<Path> f = Files.list(path)) {
            f.forEach(p -> panel.getItems().add(p.getFileName().toString()));
        } catch (Throwable e) {}
    }

    private void update() {
        dir(left, leftPath);
        dir(right, rightPath);
    }

    private void open(ListView<String> panel) {
        String name = panel.getSelectionModel().getSelectedItem();
        if (name == null) return;

        Path curr = panel == left ? leftPath : rightPath;

        if (name.equals("...")) {
            Path par = curr.getParent();
            if (par != null) {
                if (panel == left) {
                    leftPath = par;
                } else {
                    rightPath = par;
                }
                update();
            }
            return;
        }
        Path next = curr.resolve(name);
        if (Files.isDirectory(next)) {
            if (panel == left) {
                leftPath = next;
            } else {
                rightPath = next;
            }
            update();
        }
    }

    @FXML
    public void copy() {
        if (Panel == null) return;

        String name = Panel.getSelectionModel().getSelectedItem();
        if (name == null || name.equals("...")) return;

        Path srcPath = (Panel == left) ? leftPath : rightPath;
        Path dstPath = (Panel == left) ? rightPath : leftPath;

        Path src = srcPath.resolve(name);
        Path dst = dstPath.resolve(name);

        try {
            Files.copy(src, dst, StandardCopyOption.REPLACE_EXISTING);
        } catch (Throwable e) {}

        update();
    }

    @FXML
    public void move() {
        if (Panel == null) return;

        String name = Panel.getSelectionModel().getSelectedItem();
        if (name == null || name.equals("...")) return;

        Path srcPath = (Panel == left) ? leftPath : rightPath;
        Path dstPath = (Panel == left) ? rightPath : leftPath;

        Path src = srcPath.resolve(name);
        Path dst = dstPath.resolve(name);

        try {
            Files.move(src, dst, StandardCopyOption.REPLACE_EXISTING);
        } catch (Throwable e) {}
        update();
    }

    @FXML
    public void delete() {
        if (Panel == null) return;

        String name = Panel.getSelectionModel().getSelectedItem();
        if (name == null || name.equals("...")) return;

        Path dir = (Panel == left) ? leftPath : rightPath;
        Path file = dir.resolve(name);

        try {
            Files.deleteIfExists(file);
        } catch (Throwable e) {}
        update();
    }
}