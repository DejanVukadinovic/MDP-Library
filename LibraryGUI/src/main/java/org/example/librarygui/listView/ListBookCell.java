package org.example.librarygui.listView;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;

public class ListBookCell extends ListCell<BookItem> {
    private final Label label;
    private final HBox hbox;

    public ListBookCell() {
        label = new Label();

        hbox = new HBox(10); // spacing between label and button
        hbox.getChildren().addAll(label);

    }

    @Override
    protected void updateItem(BookItem item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setText(null);
            setGraphic(null);
        } else {
            label.setText(item.getTitle());
            setGraphic(hbox);
        }
    }
}
