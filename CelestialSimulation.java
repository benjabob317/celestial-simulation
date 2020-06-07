import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.paint.Color;

public class CelestialSimulation extends Application
{
    public void start(Stage primary)
    {
        MultipleBodySystem system = new MultipleBodySystem(384400*5);

        CelestialBody earth = new CelestialBody(384400*5*250, 384400*5*250, 0, -10, 5.972E24);
        earth.color = Color.color(0, 1, 0);

        CelestialBody moon = new CelestialBody(384400*5*50, 384400*5*250, 0, 1022/1.1, 7.34767309E22);
        moon.color = Color.color(0.5, 0.5, 0.5);

        system.addCelestialBody(earth);
        system.addCelestialBody(moon);
        system.updateScreen();
        
        primary.setScene(new Scene(system));
        primary.show();
    }
}
