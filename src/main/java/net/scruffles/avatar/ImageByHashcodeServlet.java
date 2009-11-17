package net.scruffles.avatar;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ImageByHashcodeServlet
        extends HttpServlet
{
    private String path;
    private static final int MAX_SIZE = 512;
    private static final int DEFAULT_SIZE = 80;

    public ImageByHashcodeServlet(String path) {
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
        int size = getImageSize(req);
        UserInfo userInfo = DataStore.getInstance().lookupInfo(getHashFromRequest(req));

        if (userInfo != null) {
            resp.setContentType("image/jpg");

            File file = findImageFile(userInfo);

            writeImage(resp, size, file);
        }
        else {
            URL resource = ImageByHashcodeServlet.class.getResource("/default.gif");
            writeImage(resp, size, new File(resource.getFile()));
        }
    }

    protected void writeImage(HttpServletResponse resp, int size, File file) throws IOException {
        writeImage(resp, size, ImageIO.read(file));
    }

    protected void writeImage(HttpServletResponse resp, int size, URL url) throws IOException {
        writeImage(resp, size, ImageIO.read(url));
    }

    private void writeImage(HttpServletResponse resp, int size, BufferedImage image) throws IOException {
        image = createResizedCopy(image, size, size, true);

        ImageIO.write(image, "jpg", resp.getOutputStream());
        resp.getOutputStream().flush();
    }

    protected int getImageSize(HttpServletRequest req) {
        String sizeString = req.getParameter("s");
        int size = DEFAULT_SIZE;
        if (sizeString != null) {
            size = Integer.parseInt(sizeString);
        }
        return Math.min(MAX_SIZE, size);
    }

    private File findImageFile(UserInfo userInfo) {
        File dir = new File(path);
        File file = new File(dir, userInfo.getIconLocation());
        return file;
    }

    private BufferedImage createResizedCopy(Image originalImage, int scaledWidth, int scaledHeight,
            boolean preserveAlpha)
    {
        int imageType = preserveAlpha ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
        BufferedImage scaledBI = new BufferedImage(scaledWidth, scaledHeight, imageType);
        Graphics2D g = scaledBI.createGraphics();
        if (preserveAlpha) {
            g.setComposite(AlphaComposite.Src);
        }
        g.drawImage(originalImage, 0, 0, scaledWidth, scaledHeight, null);
        g.dispose();
        return scaledBI;
    }

    private String getHashFromRequest(HttpServletRequest req) {
        String url = req.getRequestURI();
        String hash = url.substring(url.lastIndexOf("/") + 1);
        hash = hash.replaceAll("\\.jpg$|\\.gif$|\\.png$", "");
        return hash;
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        DataStore.initialize(new File(path));
    }
}