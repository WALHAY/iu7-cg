package com.walhay.lab.widgets;

import com.walhay.lab.utils.ColorUtils;

import javafx.geometry.Insets;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;

public class AlgoListCell extends ListCell<AlgoEntry> {
	private final CheckBox active = new CheckBox();
	private final ColorPicker color = new ColorPicker();

	private final HBox layout = new HBox(10, active, color);

	public AlgoListCell() {
		HBox.setMargin(active, new Insets(5, 0, 0, 0));
		active.setPrefWidth(150);
	}

	@Override
	protected void updateItem(AlgoEntry item, boolean empty) {
		super.updateItem(item, empty);
		if (!empty && item != null) {
			color.setValue(ColorUtils.toFXColor(item.getColor()));

			active.setText(item.getType().getName());
			active.setSelected(item.isActive());

			active.setOnAction(e -> item.setActive(active.isSelected()));
			color.setOnAction(e -> item.setColor(ColorUtils.toAwtColor(color.getValue())));

			setGraphic(layout);
		} else
			setGraphic(null);
	}
}
