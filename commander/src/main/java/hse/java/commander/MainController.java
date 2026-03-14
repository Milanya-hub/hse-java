package hse.java.commander;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import java.lang.Throwable;
import java.nio.file.*;
import java.util.stream.Stream;

public class MainController {

    @FXML
    public ListView<String> left;

    @FXML
    public ListView<String> right;

    private Path leftPath;
    private Path rightPath;
    private ListView<String> Panel;

    public void init() {
        leftPath = Paths.get(System.getProperty("user.dir"));
        rightPath = Paths.get(System.getProperty("user.dir"));

        Panel = left;
        dir(left, leftPath);
        dir(right, rightPath);

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
    }

    private void update() {
        dir(left, leftPath);
        dir(right, rightPath);
    }

    private void dir(ListView<String> panel, Path path) {
        panel.getItems().clear();
        panel.getItems().add("...");
        try (Stream<Path> f = Files.list(path)) {
            f.forEach(p -> panel.getItems().add(p.getFileName().toString()));
        } catch (Throwable e) {}
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
                    dir(left, leftPath);
                } else {
                    rightPath = par;
                    dir(right, rightPath);
                }
            }
            return;
        }

        Path touch = curr.resolve(name);
        if (Files.isDirectory(touch)) {
            if (panel == left) {
                leftPath = touch;
                dir(left, leftPath);
            } else {
                rightPath = touch;
                dir(right, rightPath);
            }
        }
    }

    @FXML
    public void copy() {
        ListView<String> srcPanel = Panel;

        Path first_path = (Panel == left) ? leftPath : rightPath;
        Path snd_path = (Panel == left) ? rightPath : leftPath;

        String name = srcPanel.getSelectionModel().getSelectedItem();
        if (name == null || name.equals("...")) return;

        Path src = first_path.resolve(name);
        Path dst = snd_path.resolve(name);

        try {
            Files.copy(src, dst, StandardCopyOption.REPLACE_EXISTING);
        } catch (Throwable e) {}

        update();
    }

    @FXML
    public void move() {
        ListView<String> srcPanel = Panel;

        Path first_path = (Panel == left) ? leftPath : rightPath;
        Path snd_path = (Panel == left) ? rightPath : leftPath;

        String name = srcPanel.getSelectionModel().getSelectedItem();
        if (name == null || name.equals("...")) return;

        Path src = first_path.resolve(name);
        Path dst = snd_path.resolve(name);

        try {
            Files.move(src, dst, StandardCopyOption.REPLACE_EXISTING);
        } catch (Throwable e) {}
        update();
    }

    @FXML
    public void delete() {
        ListView<String> panel = Panel;
        Path path = (panel == left) ? leftPath : rightPath;

        String name = panel.getSelectionModel().getSelectedItem();
        if (name == null || name.equals("...")) return;

        Path file = path.resolve(name);
        try {
            Files.deleteIfExists(file);
        } catch (Throwable e) {}
        update();
    }
}