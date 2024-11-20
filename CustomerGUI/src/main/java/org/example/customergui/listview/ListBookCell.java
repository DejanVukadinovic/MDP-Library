package org.example.customergui.listview;

import javafx.geometry.Bounds;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;

public class ListBookCell extends ListCell<BookItem> {
    private final Label label;
    private final Button button;
    private final HBox hbox;

    public ListBookCell() {
        label = new Label();
        button = new Button("Click Me");

        hbox = new HBox(10); // spacing between label and button
        hbox.getChildren().addAll(label, button);

        button.setOnAction(event -> {
            BookItem currentItem = getItem();
            if (currentItem != null) {
                System.out.println("Button clicked for item: " + currentItem.getTitle());
            }

            // Create a popup
            Popup popup = new Popup();

            // Create content for the popup (can be any node)
            assert currentItem != null;
            Label popupTitle = new Label(currentItem.getTitle());
            popupTitle.setStyle(" -fx-padding: 10px;");

            Label popupAuthor = new Label(currentItem.getAuthor());
            popupAuthor.setStyle(" -fx-padding: 10px;");

            Label popupPreview = new Label(currentItem.getPreview());
            popupPreview.setStyle(" -fx-padding: 10px;");

            Button closePopupButton = new Button("Close Popup");
            closePopupButton.setOnAction(popevent -> popup.hide());

            // Add the label and close button to a layout for the popup
            VBox popupContent = new VBox(100, popupTitle, popupAuthor, popupPreview, closePopupButton);
            popupContent.setStyle("-fx-padding: 10px; -fx-border-color: black; -fx-background-color: white;");

            // Add content to the popup
            popup.getContent().add(popupContent);

            // Set the button to show the popup when clicked

                if (!popup.isShowing()) {
                    // Get the screen coordinates of the button
                    Bounds bounds = button.localToScreen(button.getBoundsInLocal());

                    // Show the popup relative to the button's position on screen
                    popup.show(button, bounds.getMinX(), bounds.getMaxY());
                }

        });
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
