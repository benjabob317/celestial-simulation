import javafx.scene.layout.BorderPane;
import java.util.ArrayList;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.animation.PauseTransition;
import javafx.util.Duration;


public class MultipleBodySystem extends BorderPane
{
    private ArrayList<CelestialBody> celestialBodies;
    private Canvas canvas;
    private double scale; // meters per pixel pretty much
    private GraphicsContext pen;
    private double maxMass;

    public MultipleBodySystem(double width, double height, double scale)
    {
        this.canvas = new Canvas(width, height);
        setCenter(canvas);
        this.pen = canvas.getGraphicsContext2D();
        this.maxMass = 0;
        this.scale = scale;
        this.celestialBodies = new ArrayList<CelestialBody>();
        setTop(buildTopBar());
    }

    public void addCelestialBody(CelestialBody body)
    {
        for (CelestialBody celBody: celestialBodies)
        {
            body.otherBodies.add(celBody);  // Makes the new body aware of the previously existing bodies
        }
            for (int i = 0; i < celestialBodies.size(); i++)
        {
            celestialBodies.get(i).otherBodies.add(body); // Makes the previosuly existing bodies be aware of the new body
        }
        if (body.mass > maxMass)
        {
            maxMass = body.mass;
        }
        celestialBodies.add(body);
    }

    public void updateBodies(double secondsElapsed)
    {
        for (CelestialBody body: celestialBodies)
        {
            body.updateVelocity(secondsElapsed);
        }
        for (CelestialBody body: celestialBodies)
        {
            body.updatePosition(secondsElapsed);
        }
    }

    public void updateScreen()
    {
        //System.out.println(celestialBodies);
        pen.setFill(Color.WHITE);
        pen.fillRect(0, 0, canvas.getWidth(), canvas.getHeight()); // clears the screen

        for (CelestialBody body: celestialBodies)
        {
            pen.setFill(body.color);
            double radius = 50*(Math.pow(body.mass, 1.0/3.0)/Math.pow(maxMass, 1.0/3.0));
            pen.fillOval(body.position[0]/scale - radius, body.position[1]/scale - radius, radius*2, radius*2);
        }
    }

    public HBox buildTopBar()
    {
        Button play = new Button("Play");
        

        play.setOnAction(e -> {
            new Thread(() -> {
                for (int screenUpdate = 0; screenUpdate < 1800; screenUpdate++)
                {
                    for (int posUpdate = 0; posUpdate < 24; posUpdate++)
                    {
                        updateBodies(600);
                    }
                    updateScreen();
                    try
                    {
                        Thread.currentThread().sleep(10);
                    }
                    catch(InterruptedException ex)
                    {
                        Thread.currentThread().interrupt();
                    }
                }
            }).start();
        });
        HBox out = new HBox();
        out.getChildren().add(play);
        return out;
    }

}