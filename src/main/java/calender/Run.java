/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package calender;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.server.session.HashSessionIdManager;
import org.eclipse.jetty.server.session.HashSessionManager;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.servlet.ServletHandler;

/**
 *
 * @author Administrator
 */
public class Run {
    
public static void main(String[] args) throws Exception {
		
	ServletHandler handler = new ServletHandler();
		//add all servlets you want to use to the handler, the second argument is the path
        
        handler.addServletWithMapping(Login.class, "/login");
        // make the SearchTeacher Servlet accessible from http://localhost:8080/search
        handler.addServletWithMapping(Register.class, "/register");
        handler.addServletWithMapping(MyResource.class, "/latlng");
        handler.addServletWithMapping(logout.class, "/LogOutServlet");
        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setResourceBase("src/main/resources");
        resourceHandler.setDirectoriesListed(true);
        HashSessionManager manager = new HashSessionManager();
        SessionHandler sessions = new SessionHandler(manager);
        
        HandlerList l = new HandlerList();
        
        l.addHandler(resourceHandler);
        l.addHandler(sessions);
        l.addHandler(handler);
        //Create a new Server, add the handler to it and start
         Integer port = Integer.valueOf(System.getenv("PORT"));
        //Create a new Server, add the handler to it and start
        Server server = new Server(port);
        server.setHandler(l);
       

        server.start();
        //this dumps a lot of debug output to the console. 
        server.dumpStdErr();
        server.join();
    }
}
