package webtest.web;

import webtest.pojo.User;
import webtest.service.UserService;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@WebServlet("/editServlet")
public class EditServlet extends HttpServlet {
    UserService service=new UserService();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {



    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session=req.getSession();
        User user=(User) session.getAttribute("user");
        System.out.println(user.getSignature());
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(user.getGender()+" "+user.getSignature()+" "+user.getName()+" "+user.getImage()+" "+user.getBirthday());


    }
}