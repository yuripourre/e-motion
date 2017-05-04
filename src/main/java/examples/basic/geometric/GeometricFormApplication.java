package examples.basic.geometric;

import br.com.etyllica.core.context.Application;
import br.com.etyllica.core.graphics.Graphics;
import br.com.etyllica.core.linear.Point2D;
import br.com.etyllica.keel.awt.source.BufferedImageSource;
import br.com.etyllica.keel.feature.Component;
import br.com.etyllica.keel.feature.hull.HullComponent;
import br.com.etyllica.keel.filter.ColorFilter;
import br.com.etyllica.keel.filter.search.flood.SoftFloodFillSearch;
import br.com.etyllica.keel.modifier.hull.FastConvexHullModifier;
import br.com.etyllica.keel.modifier.hull.HullModifier;
import br.com.etyllica.keel.modifier.hull.PathCompressionModifier;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class GeometricFormApplication extends Application {

    private BufferedImage image;
    private BufferedImageSource source = new BufferedImageSource();

    private ColorFilter blackFilter;

    private List<Component> blackComponents;

    private Component screen;

    private HullModifier<HullComponent> quickHull;

    private PathCompressionModifier pathCompressionModifier;

    private List<String> geometryForm = new ArrayList<String>();

    private List<List<Point2D>> convexHull = new ArrayList<List<Point2D>>();

    public GeometricFormApplication(int w, int h) {
        super(w, h);
    }

    @Override
    public void load() {

        loading = 0;
        //Define the area to search for elements
        screen = new Component(0, 0, w, h);

        image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);

        loading = 10;
        //Create the image with elements
        createImage(image);
        source.setImage(image);

        //Define blue and black filters
        blackFilter = new ColorFilter(w, h, Color.BLACK);

        SoftFloodFillSearch floodFill = (SoftFloodFillSearch) blackFilter.getSearchStrategy();
        floodFill.setStep(1);

        loading = 20;
        //Filter the image
        blackComponents = blackFilter.filter(source, screen);

        loading = 25;
        quickHull = new FastConvexHullModifier();

        pathCompressionModifier = new PathCompressionModifier(5);

        loading = 31;

        for (Component component : blackComponents) {
            classifyRegion(component);
        }

        loading = 50;

    }

    private void classifyRegion(Component region) {

        List<Point2D> list = pathCompressionModifier.modify(quickHull.modify(region));
        //List<Point2D> list = quickHull.modify(region).getPoints();

        int numberOfPoints = list.size();

        String form = "undefined";

        switch (numberOfPoints) {

            case 3:
                form = "Triangle";
                break;

            case 4:
                form = "Rectangle";
                break;

            case 5:
                form = "Pentagon";
                break;

            case 6:
                form = "Hexagon";
                break;

            default:
                form = "Circle";
                break;

        }

        form += " " + numberOfPoints;

        geometryForm.add(form);

        convexHull.add(list);

    }

    private void createImage(BufferedImage image) {

        Graphics2D g = image.createGraphics();

        g.setColor(Color.WHITE);

        g.fillRect(0, 0, w, h);

        g.setColor(Color.BLACK);

        g.setStroke(new BasicStroke(6f));

        //Draw triangle
        Polygon triangle = new Polygon();
        triangle.addPoint(300, 80);
        triangle.addPoint(200, 180);
        triangle.addPoint(400, 180);

        g.drawPolygon(triangle);

        g.drawOval(440, 80, 100, 100);

        //Draw Rectangle
        g.drawRect(40, 140, 100, 180);

        AffineTransform transform = new AffineTransform();
        transform.rotate(Math.toRadians(30), x + w / 2, y + h / 2);
        g.transform(transform);
        //Draw Rotated Rectangle
        g.drawRect(340, 260, 110, 180);

    }

    @Override
    public void draw(Graphics g) {
        g.setAlpha(100);
        g.drawImage(image, 0, 0);

        g.setAlpha(90);

        //Draw a red line around the black components

        for (int i = 0; i < blackComponents.size(); i++) {

            Component component = blackComponents.get(i);

            g.setStroke(new BasicStroke(3f));
            g.setColor(Color.RED);
            g.drawRect(component.getRectangle());

            g.setColor(Color.CYAN);

			/*for(Point2D point: component.getPoints()) {		
				g.fillRect((int)point.getX(), (int)point.getY(), 1, 1);
			}*/

            g.drawStringShadow(geometryForm.get(i), component.getRectangle());

            g.setStroke(new BasicStroke(1f));

            for (Point2D point : convexHull.get(i)) {
                g.setColor(Color.BLACK);
                g.drawCircle(point, 5);
            }

        }

    }

}