package net.scruffles.avatar;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ImageServlet
        extends HttpServlet
{
    private String path;

    public ImageServlet(String path) {
        this.path = path;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        justDoIt(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        justDoIt(req, resp);
    }

    // todo consider adding exception handling
    protected void justDoIt(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        UserInfo userInfo = DataStore.lookupInfo(getHashFromRequest(req));
        if (userInfo != null) {

            resp.setContentType("image/jpg");
            File dir = new File(path);
            File file = new File(dir, userInfo.getIconLocation());

            FileInputStream fileInputStream = new FileInputStream(file);
            int i = fileInputStream.read();
            while (i != -1) {
                resp.getOutputStream().write(i);
                i = fileInputStream.read();
            }
            resp.getOutputStream().flush();
        }
        else {
            resp.getOutputStream().write(("<html><body>not found").getBytes());
        }
    }

    private String getHashFromRequest(HttpServletRequest req) {
        String url = req.getRequestURI();
        String hash = url.substring(url.lastIndexOf("/") + 1);
        hash = hash.replaceAll("\\.jpg$|\\.gif$|\\.png$", "");
        return hash;
    }
}