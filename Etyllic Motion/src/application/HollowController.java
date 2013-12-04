package application;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import br.com.etyllica.camera.CameraV4L4J;
import br.com.etyllica.core.application.Application;
import br.com.etyllica.core.event.GUIEvent;
import br.com.etyllica.core.event.KeyEvent;
import br.com.etyllica.core.event.PointerEvent;
import br.com.etyllica.core.video.Graphic;
import br.com.etyllica.motion.features.Component;
import br.com.etyllica.motion.hollowcontroller.FindRedLedActivatedFilter;
import br.com.etyllica.motion.hollowcontroller.FindRedLedFilter;


public class HollowController extends Application {

	private List<Component> rootComponent = new ArrayList<Component>();

	public HollowController(int w, int h) {
		super(w,h);
	}

	private CameraV4L4J cam;

	private BufferedImage buf;

	private FindRedLedFilter filter;
	
	private FindRedLedActivatedFilter activeFilter;

	private List<Component> lastButtons;

	private List<Component> components;

	@Override
	public void load() {

		cam = new CameraV4L4J(0);

		rootComponent.add(new Component(cam.getBufferedImage().getWidth(),cam.getBufferedImage().getHeight()));

		//Loading Filters
		filter = new FindRedLedFilter(cam.getBufferedImage().getWidth(),cam.getBufferedImage().getHeight());
		activeFilter = new FindRedLedActivatedFilter(cam.getBufferedImage().getWidth(),cam.getBufferedImage().getHeight());

		lastButtons = new ArrayList<Component>(8);

		loading = 100;

	}

	@Override
	public void timeUpdate(){

		System.out.println("TIME UPDATE");

	}

	@Override
	public GUIEvent updateKeyboard(KeyEvent event){

		if(event.isKeyDown(KeyEvent.TSK_R)){
			activated = false;
		}
		
		return GUIEvent.NONE;
	}

	private boolean activated = false;

	@Override
	public void draw(Graphic g) {

		buf = cam.getBufferedImage();

		g.drawImage(buf,0,0);

		activated = false;
		
		for(Component component: lastButtons){

			List<Component> list = new ArrayList<Component>();
			list.add(component);

			List<Component> active = activeFilter.filter(buf, list);

			Color color = Color.YELLOW;

			if(active!=null){
				
				color = Color.RED;
				activated = true;
				
			}
			g.setColor(color);

			g.drawRect(component.getCamada());			

		}

		if(!activated){

			components = filter.filter(buf, rootComponent);

			if(components!=null){

				Color color = Color.GREEN;
				if(components.size()==8){

					lastButtons.clear();
					lastButtons.addAll(components);

					color = Color.BLUE;
				}

				for(Component component: components){
					g.setColor(color);
					g.drawRect(component.getCamada());
					/*g.setColor(Color.WHITE);
					g.escreveLabelSombra(component.getMenorX(), component.getMenorY(), component.getW(), component.getH(), Integer.toString(component.getNumeroPontos()),Color.BLACK);*/
				}

			}
		}

	}

	@Override
	public GUIEvent updateMouse(PointerEvent event) {
		// TODO Auto-generated method stub
		return GUIEvent	.NONE;
	}

}