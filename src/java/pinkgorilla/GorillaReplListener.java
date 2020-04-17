package pinkgorilla;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebListener;
import javax.websocket.DeploymentException;
import javax.websocket.server.ServerContainer;
import javax.websocket.server.ServerEndpointConfig;

@WebListener
public class GorillaReplListener implements ServletContextListener {
    String servletName = "GorillaReplServlet";
    public static final String PREFIX = "/gorilla-repl/";
    static final Logger log = LoggerFactory.getLogger(GorillaReplListener.class.getName());


    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        log.info("Adding {} with mapping {}", servletName, PREFIX + "*");
        ServletRegistration servletRegistration = servletContextEvent.getServletContext().addServlet(servletName, "gorilla_repl.RingServlet");
        servletRegistration.addMapping(PREFIX + "*");
        /*
        ServerEndpointConfig config = ServerEndpointConfig.Builder.create(NReplEndpoint.class, "/repl").build();
        try {

            // http://www.programcreek.com/java-api-examples/index.php?api=javax.websocket.server.ServerEndpointConfig
            // https://docs.oracle.com/javaee/7/api/javax/websocket/server/ServerEndpointConfig.Builder.html
            // /lambdalf/gorilla-repl
            // https://docs.oracle.com/javaee/7/api/javax/websocket/server/ServerContainer.html
            // For websocket enabled web containers, developers may obtain a reference to the ServerContainer
            // instance by retrieving it as an attribute named javax.websocket.server.ServerContainer on the ServletContext.

            // TODO : Tomcat 7 does not play with programmatic Endpoint :( https://bz.apache.org/bugzilla/show_bug.cgi?id=58232
            // The fix is available in trunk, 8.0.x (for 8.0.25 onwards) and 7.0.x (for 7.0.64 onwards)
            final ServerContainer serverContainer = (ServerContainer) servletContextEvent.getServletContext()
                    .getAttribute("javax.websocket.server.ServerContainer");
            serverContainer.addEndpoint(config);
        } catch (DeploymentException e) {
            throw new RuntimeException(e);
        }
        */
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
    }
}
