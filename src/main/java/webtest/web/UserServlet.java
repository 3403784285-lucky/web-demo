package webtest.web;
import com.alibaba.fastjson.JSON;
import webtest.pojo.Music;
import webtest.pojo.User;
import webtest.service.MusicService;
import webtest.service.UserService;
import webtest.utils.GetMyAccount;
import webtest.utils.Md5Code;
import webtest.utils.PreEmail;
import webtest.web.BaseServlet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebServlet("/userCopy/*")
public class UserServlet extends BaseServlet {
        UserService service=new UserService();
       public void register(HttpServletRequest request,HttpServletResponse response) throws IOException {
           //接受用户名和密码
           HttpSession session=request.getSession();
           String email=request.getParameter("email");
           Pattern pp=Pattern.compile("\\d{10}@qq\\.com");
           if(email!=null)
           {

               Matcher m=pp.matcher(email);
               boolean mm=m.matches();
               if(mm)
               {
                   PreEmail p=new PreEmail(email);
                   String verificationPswd=p.getConfirmPassword();
                   if(session.getAttribute("email")!=null)
                   {
                       session.removeAttribute("email");
                   }
                   session.setAttribute("verificationPswd",verificationPswd);
                   session.setAttribute("email",email);
                   p.getEmail();
                   response.getWriter().write("success");
               }
               else
               {
                   System.out.println("不符合格式");
                   response.getWriter().write("fail");
               }

           }
           String email1=request.getParameter("email1");
           if(email1!=null)
           {
               Matcher m1=pp.matcher(email1);
               boolean mm1=m1.matches();
               if(mm1)
               {
                   User user=service.judgeEmail(email1);
                   if(user==null)
                   {

                       response.getWriter().write("correct");
                   }
                   else
                   {
                       System.out.println("账户已存在");
                       response.getWriter().write("exist");

                   }
               }
               else
               {
                   response.getWriter().write("fail");
               }

           }
           String email2=request.getParameter("email2");
           if(email2!=null)
           {
               if(service.judgeEmail(email2)==null)
               {

                   String password=request.getParameter("password");
                   String verification=request.getParameter("verification");
                   Pattern pp1=Pattern.compile("^.*(?=.{6,})(?=.*\\d)(?=.*[A-Z])(?=.*[a-z])(?=.*[!@#$%^&*? ]).*$");
                   Matcher m=pp1.matcher(password);
                   boolean mm=m.matches();
                   if(!mm)
                   {
                       response.getWriter().write("passwordError");
                   }
                   else
                   {
                       System.out.println(session.getAttribute("verificationPswd"));
                       if(verification.equals(session.getAttribute("verificationPswd")))
                       {

                           if(email2.equals(session.getAttribute("email")))
                           {
                               String account=null;
                               account= GetMyAccount.getMyAccount();
                               while(service.judgeReAccount(account)!=null)
                               {
                                   account=GetMyAccount.getMyAccount();
                               }
                               User user=new User();
                               user.setPassword(new Md5Code(password).getMd5Password());
                               user.setUserAccount(account);
                               user.setEmail(email2);
                               service.addMember(user);
                               //响应和数据处理不可以打乱顺序
                               System.out.println(user.getUserAccount());
                               service.initUser(email2);
                               response.getWriter().write("registerSuccess");
                           }
                           else
                           {
                               response.getWriter().write("unmatch");
                           }



                       }
                       else
                       {
                           response.getWriter().write("verificationError");
                       }
                   }


               }
               else
               {
                   response.getWriter().write("exist");

               }

           }




       }
       public void login(HttpServletRequest request,HttpServletResponse response)
       {
           //接受用户名和密码
           String userAccount=request.getParameter("userAccount");
           String password=request.getParameter("password");

           System.out.println("账号"+userAccount);
           System.out.println("密码"+password);
           //调用service查询
           User user=service.login(userAccount,new Md5Code(password).getMd5Password());

           //判断user是否为null
           if(user!=null)
           {
               //登陆成功，界面跳转

               //将登陆成功后的user对象存储到session中
               HttpSession session=request.getSession();
               String email=service.selectEmail(userAccount);
               user.setEmail(email);
               session.setAttribute("user",user);
               try {
                   response.getWriter().write("true");
               } catch (IOException e) {
                   e.printStackTrace();
               }
               //用全名
               System.out.println("密码正确");
           }
           else
           {
               try {
                   response.getWriter().write("false");
               } catch (IOException e) {
                   e.printStackTrace();
               }
               System.out.println("密码错误");



           }
       }
       public void forget(HttpServletRequest req,HttpServletResponse resp) throws IOException {
           //接受用户名和密码
           HttpSession session=req.getSession();
           String email=req.getParameter("email");
           Pattern pp=Pattern.compile("\\d{10}@qq\\.com");
           if(email!=null)
           {

               Matcher m=pp.matcher(email);
               boolean mm=m.matches();
               if(mm)
               {
                   PreEmail p=new PreEmail(email);
                   String verificationPswd=p.getConfirmPassword();
                   session.setAttribute("verificationPswd",verificationPswd);
                   session.setAttribute("email",email);
                   p.getEmail();
                   resp.getWriter().write("success");
               }
               else
               {
                   resp.getWriter().write("fail");
               }

           }
           String email1=req.getParameter("email1");
           if(email1!=null)
           {
               Matcher m1=pp.matcher(email1);
               boolean mm1=m1.matches();
               if(mm1)
               {
                   User user=service.judgeEmail(email1);
                   if(user!=null)
                   {

                       resp.getWriter().write("correct");
                   }
                   else
                   {
                       resp.getWriter().write("unexist");
                   }
               }
               else
               {
                   resp.getWriter().write("fail");
               }

           }
           String email2=req.getParameter("email2");
           if(email2!=null)
           {
               if(service.judgeEmail(email2)!=null)
               {
                   System.out.println("这里本该是邮箱有了");
                   String password=req.getParameter("password");
                   String verification=req.getParameter("verification");
                   Pattern pp1=Pattern.compile("^.*(?=.{6,})(?=.*\\d)(?=.*[A-Z])(?=.*[a-z])(?=.*[!@#$%^&*? ]).*$");
                   Matcher m=pp1.matcher(password);
                   boolean mm=m.matches();
                   if(!mm)
                   {
                       resp.getWriter().write("passwordError");
                   }
                   else
                   {
                       System.out.println(session.getAttribute("verificationPswd"));
                       if(verification.equals(session.getAttribute("verificationPswd")))
                       {

                           if(email2.equals(session.getAttribute("email")))
                           {
                               String repassword=req.getParameter("repassword");
                               if(repassword.equals(password))
                               {
                                   String account=null;
                                   account= GetMyAccount.getMyAccount();
                                   while(service.judgeReAccount(account)!=null)
                                   {
                                       account=GetMyAccount.getMyAccount();
                                   }
                                   User user=new User();
                                   user.setPassword(new Md5Code(password).getMd5Password());
                                   user.setUserAccount(account);
                                   user.setEmail(email2);
                                   service.modifyPassword(user);
                                   //响应和数据处理不可以打乱顺序
                                   System.out.println(user.getUserAccount());
                                   resp.getWriter().write("registerSuccess");
                               }
                               else
                               {
                                   resp.getWriter().write("punmatch");

                               }

                           }
                           else
                           {
                               resp.getWriter().write("unmatch");
                           }



                       }
                       else
                       {
                           resp.getWriter().write("verificationError");
                       }
                   }


               }
               else
               {
                   resp.getWriter().write("unexist");

               }

           }

       }
       public void userInfo(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user=new User();
        System.out.println("试试这个函数");
        HttpSession session=request.getSession();
        user=(User)session.getAttribute("user");
        if(user!=null)
        {
            User user1=service.selectProfile(user.getEmail());
            user1.setUserAccount(user.getUserAccount());
            user1.setPassword(user.getPassword());
            user1.setEmail(user.getEmail());
            session.removeAttribute("user");
            session.setAttribute("user",user1);
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(user1.getImage()+" "+user1.getName());

        }
        else
        {
            response.getWriter().write("fail");
        }
    }



}
