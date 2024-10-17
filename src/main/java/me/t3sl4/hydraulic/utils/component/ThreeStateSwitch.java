package me.t3sl4.hydraulic.utils.component;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class ThreeStateSwitch extends Region {
    public enum SwitchState {
        LOCAL, LOCALWEB, WEB
    }

    private static final double WIDTH = 90;
    private static final double HEIGHT = 40;
    private static final double CIRCLE_RADIUS = 15;

    private Rectangle background;
    private Circle toggleCircle;
    private Text stateText;

    private final ObjectProperty<SwitchState> currentState = new SimpleObjectProperty<>(SwitchState.LOCALWEB);
    private boolean cancelStateChange = false;

    private Runnable stateChangeListener;

    public ThreeStateSwitch() {
        initializeSelf();
        initializeParts();
        layoutParts();
        setupEventHandlers();
        updateSwitchState();

        currentState.addListener((observable, oldValue, newValue) -> {
            if (stateChangeListener != null) {
                stateChangeListener.run();
            }
        });
    }

    private void initializeSelf() {
        setMinSize(WIDTH, HEIGHT);
        setPrefSize(WIDTH, HEIGHT);
        setMaxSize(WIDTH, HEIGHT);
    }

    private void initializeParts() {
        background = new Rectangle(WIDTH, HEIGHT);
        background.setArcWidth(15);
        background.setArcHeight(15);
        background.setFill(Color.LIGHTGRAY);

        toggleCircle = new Circle(CIRCLE_RADIUS);
        toggleCircle.setFill(Color.WHITE);

        stateText = new Text("Local + Online");
        stateText.setFill(Color.WHITE);
    }

    private void layoutParts() {
        getChildren().addAll(background, toggleCircle, stateText);
        updateLayout();
    }

    private void setupEventHandlers() {
        setOnMouseClicked(event -> {
            if (event.getButton().equals(MouseButton.PRIMARY) && !cancelStateChange) {
                toggleState();
            }
        });
    }

    private void toggleState() {
        switch (currentState.get()) {
            case LOCAL:
                currentState.set(SwitchState.LOCALWEB);
                break;
            case LOCALWEB:
                currentState.set(SwitchState.WEB);
                break;
            case WEB:
                currentState.set(SwitchState.LOCAL);
                break;
        }
        updateSwitchState();
    }

    private void updateSwitchState() {
        switch (currentState.get()) {
            case LOCAL:
                toggleCircle.setCenterX(CIRCLE_RADIUS);
                stateText.setText("Local");
                background.setFill(Color.RED);
                break;
            case LOCALWEB:
                toggleCircle.setCenterX(WIDTH / 2);
                stateText.setText("Local + Online");
                background.setFill(Color.LIGHTGRAY);
                break;
            case WEB:
                toggleCircle.setCenterX(WIDTH - CIRCLE_RADIUS);
                stateText.setText("Online");
                background.setFill(Color.GREEN);
                break;
        }
    }

    private void updateLayout() {
        toggleCircle.setCenterY(HEIGHT / 2);
        stateText.setY(HEIGHT + 15);
        stateText.setX((WIDTH - stateText.getLayoutBounds().getWidth()) / 2);
    }

    @Override
    protected void layoutChildren() {
        super.layoutChildren();
        updateLayout();
    }

    public SwitchState getState() {
        return currentState.get();
    }

    public void setDefaultState(SwitchState state) {
        currentState.set(state);
        updateSwitchState();
    }

    public ObjectProperty<SwitchState> defaultStateProperty() {
        return currentState;
    }

    public void setState(SwitchState state) {
        currentState.set(state);
        updateSwitchState();
    }

    public void setCancel(boolean cancel) {
        this.cancelStateChange = cancel;
    }

    public void setStateChangeListener(Runnable listener) {
        this.stateChangeListener = listener;
    }
}