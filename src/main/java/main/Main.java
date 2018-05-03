package main;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import servlets.*;

/**
 * Class <Name class>.
 *
 * @author Alexey Rastorguev (rastorguev00@gmail.com)
 * @version 0.1
 * @since 28.04.2018
 */
public class Main {
    public static void main(String[] args) throws Exception {

        ControllerServlet controllerServlet = new ControllerServlet();

        Server server = new Server(8080);
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        server.setHandler(context);
        context.addServlet(new ServletHolder(controllerServlet), "/view");
        context.addServlet(new ServletHolder(new ServletDelete()), "/delete");
        context.addServlet(new ServletHolder(new ServletEdit()), "/edit");
        context.addServlet(new ServletHolder(new ServletUpdate()), "/update");
        context.addServlet(new ServletHolder(new ServletLogin()), "/");

        server.start();
        server.join();

    }
}
