package net.scruffles.avatar;

import org.mortbay.jetty.Server;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.ServletHolder;

public class Main {

    public static void main(String[] arguments) throws Exception {
        if (arguments.length != 1) {
            System.out.println("please provide a path to the icons");
        }
        String path = arguments[0];

        Server server = new Server(8080);
        Context root = new Context(server, "/", Context.SESSIONS);
        root.addServlet(new ServletHolder(new ImageServlet(path)), "/*");
        server.start();
    }
}
