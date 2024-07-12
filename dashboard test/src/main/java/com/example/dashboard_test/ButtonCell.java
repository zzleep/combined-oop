package com.example.dashboard_test;

import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableView;
import javafx.collections.ObservableList;

public class ButtonCell extends TableCell<Occupancy, Void> {
    private final Button deleteButton = new Button("Delete");

    public ButtonCell(TableView<Occupancy> table, DatabaseHandler dbHandler) {
        deleteButton.setOnAction(event -> {
            Occupancy item = getTableRow().getItem();
            dbHandler.deleteOccupancy(item.getOccupancyId()); // Delete from database
            ObservableList<Occupancy> data = table.getItems();
            data.remove(item); // Remove from TableView
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