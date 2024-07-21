package com.example.dashboard_test;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class ButtonCell<T> extends TableCell<T, Void> {
    private final Button deleteButton = new Button("Delete");

    public ButtonCell(TableView<T> table) {
        deleteButton.setOnAction(event -> {
            T item = getTableView().getItems().get(getIndex());
            table.getItems().remove(item);
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText("Item deleted successfully.");
            alert.showAndWait();
        });
    }

    @Override
    protected void updateItem(Void item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setGraphic(null);
        } else {
            setGraphic(deleteButton);
        }
    }
}