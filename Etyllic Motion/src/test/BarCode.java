package test;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.List;

import br.com.etyllica.camera.FakeCamera;
import br.com.etyllica.core.application.Application;
import br.com.etyllica.core.event.GUIEvent;
import br.com.etyllica.core.event.KeyEvent;
import br.com.etyllica.core.event.PointerEvent;
import br.com.etyllica.core.video.Graphic;
import br.com.etyllica.linear.Ponto2D;
import br.com.etyllica.motion.custom.barcode.BarCodeFilter;
import br.com.etyllica.motion.features.Component;

public class BarCode extends Application{

	FakeCamera cam = new FakeCamera();

	private BarCodeFilter filter = new BarCodeFilter(w, h);

	private boolean hide = false;
	private boolean pixels = true;

	private int xOffset = 40;
	private int yOffset = 40;

	private List<Component> result;

	public BarCode(int w, int h) {
		super(w, h);
	}

	@Override
	public void load() {

		filter.setBorder(2);

		loadingPhrase = "Loading Images";

		cam.addImage("/bars/bar.png");

		loading = 25;
		loadingPhrase = "Configuring Filter";
		filter.setWandColor(Color.BLACK);

		loading = 30;
		filter.setTolerance(140);

		filter.setW(cam.getBufferedImage().getWidth());
		filter.setH(cam.getBufferedImage().getHeight());

		reset(cam.getBufferedImage());

		loading = 100;
	}

	private void reset(BufferedImage b){
		int w = b.getWidth();
		int h = b.getHeight();

		filter.setW(w);
		filter.setH(h);

		loading = 65;
		loadingPhrase = "Show Result";

		result = filter.filter(b, new Component(w, h));

		loading = 70;
		loadingPhrase = "Show Angle";
		
	}

	@Override
	public GUIEvent updateMouse(PointerEvent event) {
		// TODO Auto-generated method stub
		return GUIEvent.NONE;
	}

	@Override
	public GUIEvent updateKeyboard(KeyEvent event) {

		if(event.isKeyDown(KeyEvent.TSK_SETA_DIREITA)){
			cam.nextFrame();
			reset(cam.getBufferedImage());
		}

		else if(event.isKeyDown(KeyEvent.TSK_SETA_ESQUERDA)){
			cam.previousFrame();
			reset(cam.getBufferedImage());
		}

		if(event.isKeyDown(KeyEvent.TSK_H)){
			hide = !hide;
		}

		if(event.isKeyDown(KeyEvent.TSK_P)){
			pixels = !pixels;
		}

		return GUIEvent.NONE;
	}

	@Override
	public void draw(Graphic g) {

		g.drawImage(cam.getBufferedImage(), xOffset, yOffset);
		
		g.drawImage(cam.getBufferedImage(), xOffset, yOffset+200);

		int offset = 1;
		for(Component feature: result){

			drawBox(g, feature, offset%2*20);
			
			offset++;

		}

	}

	private void drawBox(Graphic g, Component box, int downOffset){

		g.setColor(Color.RED);

		Ponto2D a = box.getPoints().get(0);
		Ponto2D b = box.getPoints().get(1);
		Ponto2D c = box.getPoints().get(2);
		Ponto2D d = box.getPoints().get(3);

		Ponto2D ac = new Ponto2D((a.getX()+c.getX())/2, (a.getY()+c.getY())/2);
		Ponto2D ab = new Ponto2D((a.getX()+b.getX())/2, (a.getY()+b.getY())/2);

		Ponto2D bd = new Ponto2D((b.getX()+d.getX())/2, (b.getY()+d.getY())/2);
		Ponto2D cd = new Ponto2D((c.getX()+d.getX())/2, (c.getY()+d.getY())/2);

		drawLine(g, a, b);
		drawLine(g, a, c);

		drawLine(g, b, d);
		drawLine(g, c, d);

		drawPoint(g, a);
		drawPoint(g, b);
		drawPoint(g, c);
		drawPoint(g, d);

		g.setColor(Color.YELLOW);
		drawLine(g, ab, cd);
		drawPoint(g, ab);
		drawPoint(g, cd);

		g.setColor(Color.GREEN);
		drawLine(g, ac, bd);

		drawPoint(g, ac);
		drawPoint(g, bd);

		g.setColor(Color.BLACK);

		g.drawString(Integer.toString((int)(d.getX()-a.getX())), xOffset+(int)d.getX()-12, yOffset+(int)d.getY()+20+downOffset);

	}

	private void drawLine(Graphic g, Ponto2D a, Ponto2D b){		
		g.drawLine(xOffset+(int)a.getX(), yOffset+(int)a.getY(), xOffset+(int)b.getX(), yOffset+(int)b.getY());		
	}

	private void drawPoint(Graphic g, Ponto2D point){
		g.fillCircle(xOffset+(int)point.getX(), yOffset+(int)point.getY(), 3);
	}

}
