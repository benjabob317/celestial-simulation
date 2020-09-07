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
    public double scale; // meters per pixel pretty much
    private GraphicsContext pen;
    private double maxMass;
    public double bodyScale;
    private boolean running;
    public double radLogCoef;
    public double distLogCoef;
    private CelestialBody refBody;
    public double animationHours;

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
        this.refBody = null;
        this.animationHours = 4;
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

    public void updateScreen() // eqiuvalent to a draw method
    {
        pen.setFill(Color.WHITE);
        pen.fillRect(0, 0, getWidth(), getHeight()); // clears the screen

        for (CelestialBody body: celestialBodies)
        {
            pen.setFill(body.color);
            double radius = bodyScale*Math.pow(Math.E, radLogCoef*Math.log(Math.pow(body.mass, 1.0/3.0)/Math.pow(maxMass, 1.0/3.0)));
            
            if (refBody != null)
            {
                pen.fillOval(getWidth()/2 - Math.cos(body.directionToBody(refBody))*Math.pow(Math.E, distLogCoef*Math.log(body.distanceToBody(refBody)/scale)) - radius, getHeight()/2 - Math.sin(body.directionToBody(refBody))*Math.pow(Math.E, distLogCoef*Math.log(body.distanceToBody(refBody)/scale)) - radius, radius*2, radius*2);
            }
            else {
                pen.fillOval(getWidth()/2 + body.position[0]/scale - radius, getHeight()/2 + body.position[1]/scale - radius, radius*2, radius*2);
            }

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
    
    public void updateRefBody(CelestialBody newRefBody)
    {
        refBody = newRefBody;
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
                    for (int posUpdate = 0; posUpdate < 6*animationHours; posUpdate++)
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