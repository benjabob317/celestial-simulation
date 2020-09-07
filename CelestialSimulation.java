import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import java.util.ArrayList;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Slider;
import javafx.scene.text.Text;
import javafx.geometry.Pos;





public class CelestialSimulation extends Application
{
    public ArrayList<MultipleBodySystem> systems;

    public void start(Stage primary)
    {
        this.systems = new ArrayList<MultipleBodySystem>();
        BorderPane bp = new BorderPane();
        bp.setTop(buildTopBar());

        CelestialBody earth = new CelestialBody(5.972E24, Color.color(0, 1, 0));
        CelestialBody moon = new CelestialBody(7.34767309E22, Color.color(0.5, 0.5, 0.5));
        CelestialBody mars = new CelestialBody(6.9E23, Color.color(1, 0, 0));
        CelestialBody sun = new CelestialBody(1.989E30, Color.color(1, 1, 0));


        /*MultipleBodySystem earthMoon = new MultipleBodySystem(800, 800, 384400*5, 10, bp);
        earth.initVectors(0, 0, 0, -0.1109*1022*0.1);
        moon.initVectors(384400000, 0, 0, 1022);
        earthMoon.addCelestialBody(earth);
        earthMoon.addCelestialBody(moon);*/

        MultipleBodySystem earthMoonSun = new MultipleBodySystem(800, 800, 5*1.495978707E8, 40, bp);
        sun.initVectors(0, 0, 0, 0);
        earth.initVectors(1.495978707E11, 0, 0, 30000);
        moon.initVectors(1.495978707E11 + 384400000, 0, 0, 30000 + 1022);
        earthMoonSun.addCelestialBody(sun);
        earthMoonSun.addCelestialBody(earth);
        earthMoonSun.addCelestialBody(moon);
    

        systems.add(earthMoonSun);
        systems.get(0).updateScreen();        
        primary.setScene(new Scene(bp));
        primary.show();
    }

    public HBox buildTopBar()
    {
        Button play = new Button("Play");
        play.setOnAction(e -> {
            systems.get(0).start();
        });

        Button pause = new Button("Pause");
        pause.setOnAction(e -> {
            systems.get(0).stop();
        });

        Slider distanceSlider = new Slider(4, 10, Math.log10(5*1.495978707E8));
        distanceSlider.setShowTickMarks(true);
        distanceSlider.setShowTickLabels(true);
        distanceSlider.setMajorTickUnit(1);
        distanceSlider.setBlockIncrement(0.5);
        distanceSlider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val) {
                systems.get(0).updateScale(Math.pow(10, distanceSlider.getValue()));
            }
        });

        VBox sliderBox = new VBox();
        sliderBox.setAlignment(Pos.CENTER);
        sliderBox.getChildren().addAll(distanceSlider, new Text("Distance Scale"));

        Slider distLogSlider = new Slider(0, 1, 1);
        distLogSlider.setShowTickMarks(true);
        distLogSlider.setShowTickLabels(true);
        distLogSlider.setMajorTickUnit(.1);
        distLogSlider.setBlockIncrement(.01);
        distLogSlider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val) {
                systems.get(0).updateDistLogCoef(distLogSlider.getValue());
            }
        });

        VBox distLogBox = new VBox();
        distLogBox.setAlignment(Pos.CENTER);
        distLogBox.getChildren().addAll(distLogSlider, new Text("Distance Log"));



        Button reset = new Button("Reset");
        reset.setOnAction(e -> {
            systems.get(0).stop();
            systems.get(0).resetBodies();
            systems.get(0).updateScreen();
        });


        Slider bodyScaleSlider = new Slider(0, 150, 100);
        bodyScaleSlider.setShowTickMarks(true);
        bodyScaleSlider.setShowTickLabels(true);
        bodyScaleSlider.setMajorTickUnit(10);
        bodyScaleSlider.setBlockIncrement(2.5);
        bodyScaleSlider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val) {
                systems.get(0).updateBodyScale(bodyScaleSlider.getValue());
            }
        });

        VBox bodySliderBox = new VBox();
        bodySliderBox.setAlignment(Pos.CENTER);
        bodySliderBox.getChildren().addAll(bodyScaleSlider, new Text("Body Scale"));


        Slider bodyLogSlider = new Slider(0, 1, 1);
        bodyLogSlider.setShowTickMarks(true);
        bodyLogSlider.setShowTickLabels(true);
        bodyLogSlider.setMajorTickUnit(.1);
        bodyLogSlider.setBlockIncrement(.01);
        bodyLogSlider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val) {
                systems.get(0).updateRadLogCoef(bodyLogSlider.getValue());
            }
        });

        VBox bodyLogBox = new VBox();
        bodyLogBox.setAlignment(Pos.CENTER);
        bodyLogBox.getChildren().addAll(bodyLogSlider, new Text("Body Log"));

        HBox out = new HBox();
        out.getChildren().addAll(play, pause, sliderBox, distLogBox, reset, bodySliderBox, bodyLogBox);
        return out;
    }
}
