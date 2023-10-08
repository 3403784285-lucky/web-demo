package webtest.web;
import webtest.pojo.User;
import webtest.service.UserService;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@WebServlet("/personServlet")
public class PersonServlet extends HttpServlet {
    UserService service=new UserService();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {



    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session=req.getSession();
        if(session.getAttribute("mark")==null)
        {
            User user=(User) session.getAttribute("user");
            int count=service.selectCountConcern(user.getUserAccount());
            int counted=service.selectCountConcerned(user.getUserAccount());
            resp.setCharacterEncoding("UTF-8");
            resp.getWriter().write(user.getGender()+" "+user.getSignature()+" "+user.getName()+" "+user.getImage()+" "+count+" "+counted);

        }
       else
        {
            User user=(User) session.getAttribute("user");
            User userCopy=new User();
            userCopy.setImage(req.getParameter("image"));
            userCopy.setGender(req.getParameter("gender"));
            userCopy.setName(req.getParameter("name"));
            userCopy.setSignature(req.getParameter("signature"));
            String date=req.getParameter("birthday");
            userCopy.setEmail(user.getEmail());
            userCopy.setUserAccount(user.getUserAccount());
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
            java.sql.Date birthday=null;
            try {
                System.out.println(userCopy+"哈哈哈");
                Date dateCopy=simpleDateFormat.parse(date);
                 birthday=new java.sql.Date(dateCopy.getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            userCopy.setBirthday(birthday);
            service.updateProfile(userCopy);


        }



    }
}
