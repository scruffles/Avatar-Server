package net.scruffles.avatar;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class IncludedImageServlet
        extends ImageByHashcodeServlet
{
    private String path;
    private static final int MAX_SIZE = 512;
    private static final int DEFAULT_SIZE = 80;

    public IncludedImageServlet(String path) {
        super(path);
    }

    // todo consider adding exception handling
    protected void justDoIt(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int size = getImageSize(req);

        String url = req.getRequestURI();
        String imageName = url.substring(url.lastIndexOf("/") + 1);
        System.out.println("imageName = " + imageName);

        if (imageName != null) {
            resp.setContentType("image/jpg");

            URL imageUrl = this.getClass().getResource("/included/" + imageName);

            writeImage(resp, size, imageUrl);
        }
        else {
            URL resource = IncludedImageServlet.class.getResource("/default.gif");
            writeImage(resp, size, new File(resource.getFile()));
        }
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
    }
}