import javafx.scene.layout.BorderPane;
import java.util.ArrayList;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.animation.PauseTransition;
import javafx.util.Duration;


public class MultipleBodySystem extends Canvas
{
    private ArrayList<CelestialBody> celestialBodies;
    private BorderPane bp;
    private double scale; // meters per pixel pretty much
    private GraphicsContext pen;
    private double maxMass;
    private double bodyScale;
    private boolean running;
    private double radLogCoef;
    private double distLogCoef;

    public MultipleBodySystem(double width, double height, double scale, double bodyScale, BorderPane bp)
    {
        super(width, height);
        this.pen = getGraphicsContext2D();
        this.maxMass = 0;
        this.scale = scale;
        this.celestialBodies = new ArrayList<CelestialBody>();
        this.bp = bp;
        bp.setCenter(this);
        this.bodyScale = bodyScale;
        this.running = false;
        this.radLogCoef = 1;
        this.distLogCoef = 1;
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

    public void resetBodies()
    {
        for (CelestialBody body: celestialBodies)
        {
            body.resetStats();
        }
    }

    public void updateScreen()
    {
        //System.out.println(celestialBodies);
        pen.setFill(Color.WHITE);
        pen.fillRect(0, 0, getWidth(), getHeight()); // clears the screen

        for (CelestialBody body: celestialBodies)
        {
            pen.setFill(body.color);
            double radius = bodyScale*Math.pow(Math.E, radLogCoef*Math.log(Math.pow(body.mass, 1.0/3.0)/Math.pow(maxMass, 1.0/3.0)));
            pen.fillOval(getWidth()/2 + Math.pow(Math.E, distLogCoef*Math.log(body.position[0]/scale)) - radius, getHeight()/2 + Math.pow(Math.E, distLogCoef*Math.log(body.position[1]/scale)) - radius, radius*2, radius*2);
        }
    }

    public void updateScale(double newScale)
    {
        scale = newScale;
        updateScreen();
    }

    public void updateBodyScale(double newScale)
    {
        bodyScale = newScale;
        updateScreen();
    }

    public void updateRadLogCoef(double newCoef)
    {
        radLogCoef = newCoef;
        updateScreen();
    }

    public void updateDistLogCoef(double newCoef)
    {
        distLogCoef = newCoef;
        updateScreen();
    }

    public void start()
    {
        if (!this.running)
        {
            this.running = true;
            new Thread(() -> {
                while (this.running)
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
        }
    }

    public void stop()
    {
        this.running = false;
    }

}