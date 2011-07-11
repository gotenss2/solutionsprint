/**
 *	Generated by DragonFly
 *
 */
package servoservice;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.osgi.framework.BundleContext;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;


import com.buglabs.application.AbstractServiceTracker;
import com.buglabs.bug.module.camera.pub.ICameraDevice;
import com.buglabs.bug.module.camera.pub.ICameraModuleControl;
import com.buglabs.bug.module.vonhippel.pub.IVonHippelSerialPort;
import com.buglabs.services.ws.PublicWSAdmin;

 /**
 *	Service tracker for the BugApp Bundle;
 *
 */
public class ServoServiceTracker extends AbstractServiceTracker implements Servlet {	
	private PublicWSAdmin wsAdmin; 	
	private HttpService http_service;
	private ConfigurationAdmin config_admin;
	private IVonHippelSerialPort vh_serial;
	private ICameraModuleControl camera_control;
	private ICameraDevice camera;
	private BundleContext context;
	private ServoServlet servlet; 

	public ServoServiceTracker(BundleContext context) {
		super(context);
		this.context = context;
	}
	
	/**
	 * Determines if the application can start.
	 */
	public boolean canStart() {
		return super.canStart();
	}
	
	/**
	 * If canStart returns true
     * this method is called to start the application.
     * Place your fun logic here. 
	 */
	public void doStart() {
		wsAdmin = (PublicWSAdmin)getService(PublicWSAdmin.class);
		http_service = (HttpService) this.getService(HttpService.class);
		camera = (ICameraDevice) this.getService(ICameraDevice.class);
		camera_control = (ICameraModuleControl) this.getService(ICameraModuleControl.class);
		vh_serial = (IVonHippelSerialPort) this.getService(IVonHippelSerialPort.class);
		servlet = new ServoServlet();
		CaptureHttpContext capcon =  new CaptureHttpContext(context);
		
		servlet.setVonHippelSerialPort(vh_serial);
		//servlet.setCamera(camera);
		servlet.setCameraModuleControl(camera_control);
		servlet.setBundleContext(context);
		servlet.setHttpService(http_service);
		try {
			//http_service.registerServlet(ServoServlet.ALIAS, servlet, null, null);
			http_service.registerResources(CaptureHttpContext.CAPTURE_ALIAS, CaptureHttpContext.NAME,capcon);
			
		} catch (NamespaceException e) {
			e.printStackTrace();
		}
		wsAdmin.registerService(servlet); 
	}

	/**
	 * Called when a service that this application depends is unregistered.
	 */
	public void doStop() {
		//if (http_service != null) {
		//	http_service.unregister(ServoServlet.ALIAS);
		//}
		if (http_service != null) {
			http_service.unregister(CaptureHttpContext.CAPTURE_ALIAS);
		}
		if (wsAdmin != null)
			wsAdmin.unregisterService(servlet); 
	}

	/**
	 * Allows the user to set the service dependencies by
     * adding them to services list returned by getServices().
     * i.e.nl getServices().add(MyService.class.getName());
	 */
	public void initServices() {
		getServices().add("org.osgi.service.http.HttpService");
		getServices().add("com.buglabs.bug.module.camera.pub.ICameraDevice");
		getServices().add("com.buglabs.bug.module.camera.pub.ICameraModuleControl");
		getServices().add("com.buglabs.bug.module.vonhippel.pub.IVonHippelSerialPort");
		getServices().add("com.buglabs.services.ws.PublicWSAdmin");
	}

	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	public ServletConfig getServletConfig() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getServletInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	public void init(ServletConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		
	}

	public void service(ServletRequest arg0, ServletResponse arg1)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		
	}
		
}

