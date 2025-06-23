package com.walhay.lab.widgets;

import com.walhay.lab.utils.ColorUtils;
import com.walhay.lab.utils.EllipseType;
import javafx.geometry.Insets;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.Spinner;
import javafx.scene.layout.HBox;

public class AlgoListCell<T> extends ListCell<AlgoEntry<T>> {
	public static final int CELL_HEIGHT = 35;
	private final CheckBox active = new CheckBox();
	private final ColorPicker color = new ColorPicker();
	private final Label spinnerLabel = new Label("Points:");
	private final Spinner<Integer> pointCount = new Spinner<>(1, 10000000, 100);

	private final HBox layout = new HBox(10, active, color, spinnerLabel, pointCount);

	public AlgoListCell() {
		Insets margin = new Insets(5, 0, 0, 0);
		HBox.setMargin(active, margin);
		HBox.setMargin(spinnerLabel, margin);

		active.setPrefWidth(150);
		pointCount.setEditable(true);
		setMinHeight(CELL_HEIGHT);
	}

	@Override
	protected void updateItem(AlgoEntry<T> item, boolean empty) {
		super.updateItem(item, empty);
		if (!empty && item != null) {
			if (item.getType() instanceof EllipseType type) {
				boolean visible = type.equals(EllipseType.PARAM);
				pointCount.setVisible(visible);
				spinnerLabel.setVisible(visible);

				pointCount.valueProperty().addListener(e -> item.setPoints(pointCount.getValue()));
			}

			color.setValue(ColorUtils.toFXColor(item.getColor()));

			active.setText(item.getType().toString());
			active.setSelected(item.isActive());

			active.setOnAction(e -> item.setActive(active.isSelected()));
			color.setOnAction(e -> item.setColor(ColorUtils.toAwtColor(color.getValue())));

			setGraphic(layout);
		} else
			setGraphic(null);
		updateSelected(false);
	}
}
