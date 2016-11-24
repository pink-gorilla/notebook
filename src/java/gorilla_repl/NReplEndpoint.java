package gorilla_repl;

import clojure.java.api.Clojure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.net.URI;
import java.util.List;

// TODO : Tomcat 7 does not play with programmatic Endpoint :( https://bz.apache.org/bugzilla/show_bug.cgi?id=58232
// The fix is available in trunk, 8.0.x (for 8.0.25 onwards) and 7.0.x (for 7.0.64 onwards)
@ServerEndpoint(value = GorillaReplListener.PREFIX + "repl")
public class NReplEndpoint {
    static final Logger log = LoggerFactory.getLogger(NReplEndpoint.class.getName());

    @OnOpen
    public void start(Session session) {
        URI uri = session.getRequestURI();
        String path = uri.getPath();
        log.info("Establishing session for " + path);
    }

    @OnClose
    public void end(Session wsSession) {
        log.info("End session {}", wsSession.getId());
    }

    @OnMessage
    public void incoming(String message, Session session) {
        String key = session.getId();
        log.info("Received " + message + " for session " + key);
        String ns = "gorilla-repl.jee-interop";
        String fn = "process-json-message";
        log.info("Preparing to invoke {}/{}", ns, fn);
        try {
            Object sym = Clojure.var("clojure.core", "symbol").invoke(ns);
            Clojure.var("clojure.core", "require").invoke(sym);
            // Not quite sure if the Remote should go in as well
            List<String> retval = (List<String>) Clojure.var(ns, fn).invoke(message, session.getUserProperties());
            log.info("Got {}", retval.size() + " responses");
            List<String> responses = (List) retval;
            for (String response : responses) {
                log.info("Sending response", response);
                session.getBasicRemote().sendText(response);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            try {
                session.close();
            } catch (IOException e1) {
                log.error(e1.getMessage(), e1);
            }
            throw new RuntimeException(e);
        }
    }

}
