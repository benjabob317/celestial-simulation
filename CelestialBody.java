import java.util.ArrayList;
import javafx.scene.paint.Color;

public class CelestialBody
{
    public double[] position; // {x, y} in m
    public double[] velocity; // {v_x, v_y} in m/s
    public double[] originalStats; // {x, y, v_x, v_y}, cheap way to resetting simulation

    public double mass;
    public ArrayList<CelestialBody> otherBodies;
    public Color color;

    private static final double gravitationalConstant = 6.6743015E-11;

    public CelestialBody(double[] position, double[] velocity, double mass)
    {
        this.position = new double[] {position[0], position[1]};
        this.velocity = new double[] {velocity[0], velocity[1]};
        this.originalStats = new double[] {position[0], position[1], velocity[0], velocity[1]};
        this.mass = mass;
        this.otherBodies = new ArrayList<CelestialBody>();
    }

    public CelestialBody(double x, double y, double v_x, double v_y, double mass)
    {
        this.position = new double[] {x, y};
        this.velocity = new double[] {v_x, v_y};
        this.originalStats = new double[] {x, y, v_x, v_y};
        this.mass = mass;
        this.otherBodies = new ArrayList<CelestialBody>();
    }
    
    public CelestialBody(double mass, Color color)
    {
        this.color = color;
        this.position = new double[] {0, 0};
        this.velocity = new double[] {0, 0};
        this.originalStats = new double[] {position[0], position[1], velocity[0], velocity[1]};
        this.mass = mass;
        this.otherBodies = new ArrayList<CelestialBody>();
    }

    public void initVectors(double x, double y, double v_x, double v_y)
    {
        originalStats = new double[] {x, y, v_x, v_y};
        setPosition(x, y);
        setVelocity(v_x, v_y);
    }

    public void setPosition(double x, double y)
    {
        position[0] = x;
        position[1] = y;
    }

    public void setVelocity(double vx, double vy)
    {
        velocity[0] = vx;
        velocity[1] = vy;
    }
    
    public void resetStats()
    {
        position[0] = originalStats[0];
        position[1] = originalStats[1];
        velocity[0] = originalStats[2];
        velocity[1] = originalStats[3];
    }

    public double velocityMagnitude()
    {
        return Math.hypot(velocity[0], velocity[1]);
    }


    public double distanceToBody(CelestialBody otherBody)
    {
        return Math.hypot(position[0] - otherBody.position[0], position[1] - otherBody.position[1]);
    }

    public double directionToBody(CelestialBody otherBody) // angle of the path to otherBody with respect to horizontal, -pi to +pi
    {
        return Math.atan2(otherBody.position[1] - position[1], otherBody.position[0] - position[0]);
    }

    public double[] gravitationalForce(CelestialBody otherBody) // vector pointing towards the other body
    {
        double magnitude = gravitationalConstant*mass*(otherBody.mass)/Math.pow(distanceToBody(otherBody), 2);
        double[] vector = {magnitude*Math.cos(directionToBody(otherBody)), magnitude*Math.sin(directionToBody(otherBody))};
        return vector;
    }

    public double[] netGravitationalForce() // net gravitational force based on the otherBodies list
    {
        double[] vector = {0, 0};
        for (CelestialBody body: otherBodies)
        {
            vector[0] += gravitationalForce(body)[0];
            vector[1] += gravitationalForce(body)[1];
        }
        return vector;
    }

    public double[] netGravitationalAccel()
    {
        double[] vector = {netGravitationalForce()[0]/mass, netGravitationalForce()[1]/mass};
        return vector;
    }


    public void updateVelocity(double secondsElapsed)
    {
        velocity[0] += (netGravitationalAccel()[0] * secondsElapsed);
        velocity[1] += (netGravitationalAccel()[1] * secondsElapsed);
    }

    public void updatePosition(double secondsElapsed)
    {
        position[0] += (velocity[0] * secondsElapsed);
        position[1] += (velocity[1] * secondsElapsed);
    }


}