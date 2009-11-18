package net.scruffles.avatar;

import java.io.File;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RegistrationServlet
        extends HttpServlet
{
    private String imageLoc;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String image = req.getParameter("image");
        String imageLoc = RegistrationServlet.class.getResource("/included/ant.jpg").getFile();
        imageLoc = new File(new File(imageLoc).getParentFile(), "babyOrangatang.jpg").getAbsolutePath();
        DataStore.getInstance().addUserInfo(new UserInfo(email, imageLoc));
        DataStore.getInstance().persist();
        resp.setContentType("text/html");
        resp.getOutputStream().write("<html><body>Good Girl!".getBytes());
    }
}
