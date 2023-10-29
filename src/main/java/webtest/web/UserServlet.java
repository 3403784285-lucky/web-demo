package webtest.web;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import webtest.pojo.Comment;
import webtest.pojo.PlayList;
import webtest.pojo.User;
import webtest.service.MusicService;
import webtest.service.UserService;
import webtest.utils.*;
import webtest.web.BaseServlet;

import javax.jws.soap.SOAPBinding;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
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
           System.out.println("生成账号"+user.getUserAccount());

           //判断user是否为null
           if(user!=null)
           {
               //登陆成功，界面跳转
               String email=service.selectEmail(userAccount);
               user.setEmail(email);
                //将个人信息存进去
               try {
                   Map<String,String>map=service.refreshForDts(user.getUserAccount());
                   String json=JSON.toJSONString(map);
                   response.getWriter().write(json);
               } catch (Exception e) {
                   e.printStackTrace();
               }

               //用全名
               System.out.println("密码正确");
           }
           else
           {


               //先要判断用户是否存在
                if(service.selectUserSurvive(userAccount)==null)
                {
                    try {
                        response.getWriter().write("short");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                else
                {
                    //如果存在但是密码错误
                    try {
                        response.getWriter().write("infoFalse");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.out.println("密码错误");


                }




           }
       }
        public void modifyPlayListInfo(HttpServletRequest request,HttpServletResponse response)
        {
            response.setCharacterEncoding("UTF-8");
            String haha=utilJudge(request,response);
            if(!haha.equals("fail"))
            {
                String userAccount=TokenUtil.verifyToken(request.getParameter("refreshToken"));
                Cookie cookie=new Cookie("userAccount",userAccount);
                cookie.setMaxAge(-1);
                cookie.setPath("/");
                response.addCookie(cookie);
                PlayList playList=new PlayList();
                Date date = new Date();//获取当前的日期
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
                String str = df.format(date);
                playList.setCreateTime(str);
                playList.setPlayCover(request.getParameter("playCover"));
                playList.setPlayLabel(request.getParameter("playLabel"));
                playList.setUserId(userAccount);
                playList.setPlayName(request.getParameter("playName"));
                playList.setPlaySignature(request.getParameter("playSignature"));
                service.submitPlayListInfo(playList);
                if(haha.equals("over"))
                {
                    Map<String,String>mapCopy= null;
                    try {
                        mapCopy = service.refreshForDts(userAccount);
                        String jsonString1 = JSON.toJSONString(playList);
                        Map map = JSON.parseObject(jsonString1, Map.class);
                        map.put("accessToken",mapCopy.get("accessToken"));
                        map.put("refreshToken",mapCopy.get("refreshToken"));
                        String jsonString = JSON.toJSONString(map);
                        response.getWriter().write(jsonString);


                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
                else
                {
                    String accessToken=request.getParameter("accessToken");
                    String refreshToken=request.getParameter("refreshToken");
                    String jsonString1 = JSON.toJSONString(playList);
                    Map map = JSON.parseObject(jsonString1, Map.class);
                    map.put("accessToken",accessToken);
                    map.put("refreshToken",refreshToken);
                    String jsonString = JSON.toJSONString(map, SerializerFeature.WriteMapNullValue);
                    try {
                        response.getWriter().write(jsonString);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
            else
            {
                if (request.getCookies()!=null)
                {
                    for (Cookie cookie :request.getCookies()) {
                        if ("userAccount".equals(cookie.getName())) {
                            cookie.setValue(null);
                            cookie.setMaxAge(0);
                            cookie.setPath("/");
                            response.addCookie(cookie);
                        }

                    }

                }
                try {
                    response.getWriter().write("fail");
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }

     /*  public void playlistInfo(HttpServletRequest request,HttpServletResponse response)
       {

           response.setCharacterEncoding("UTF-8");
           String haha=utilJudge(request,response);
           if(!haha.equals("fail"))
           {
               String userAccount=TokenUtil.verifyToken(request.getParameter("refreshToken"));
               Cookie cookie=new Cookie("userAccount",userAccount);
               cookie.setMaxAge(-1);
               cookie.setPath("/");
               response.addCookie(cookie);
                // 这里有点问题 PlayList[] playLists=service.selectPlayList(String )

               if(haha.equals("over"))
               {
                   Map<String,String>mapCopy= null;
                   try {
                       mapCopy = service.refreshForDts(userAccount);
                       String jsonString1 = JSON.toJSONString(comment);
                       Map map = JSON.parseObject(jsonString1, Map.class);
                       map.put("accessToken",mapCopy.get("accessToken"));
                       map.put("refreshToken",mapCopy.get("refreshToken"));
                       String jsonString = JSON.toJSONString(map);
                       response.getWriter().write(jsonString);


                   } catch (Exception e) {
                       e.printStackTrace();
                   }


               }
               else
               {
                   String accessToken=request.getParameter("accessToken");
                   String refreshToken=request.getParameter("refreshToken");
                   String jsonString1 = JSON.toJSONString(comment);
                   Map map = JSON.parseObject(jsonString1, Map.class);
                   map.put("accessToken",accessToken);
                   map.put("refreshToken",refreshToken);
                   String jsonString = JSON.toJSONString(map, SerializerFeature.WriteMapNullValue);
                   try {
                       response.getWriter().write(jsonString);
                   } catch (IOException e) {
                       e.printStackTrace();
                   }
               }

           }
           else
           {
               if (request.getCookies()!=null)
               {
                   for (Cookie cookie :request.getCookies()) {
                       if ("userAccount".equals(cookie.getName())) {
                           cookie.setValue(null);
                           cookie.setMaxAge(0);
                           cookie.setPath("/");
                           response.addCookie(cookie);
                       }

                   }

               }
               try {
                   response.getWriter().write("fail");
               } catch (IOException e) {
                   e.printStackTrace();
               }

           }
       }*/
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
           response.setCharacterEncoding("UTF-8");
           System.out.println("试试这个函数");
           String haha=utilJudge(request,response);
           if(!haha.equals("fail"))
           {
               String userAccount=TokenUtil.verifyToken(request.getParameter("refreshToken"));
               Cookie cookie=new Cookie("userAccount",userAccount);
               cookie.setMaxAge(-1);
               cookie.setPath("/");
               response.addCookie(cookie);
               User user=new User();
               user=service.selectProfileByUserAccount(userAccount);
               if(haha.equals("over"))
               {

                   try {
                       Map<String,String>mapCopy=service.refreshForDts(userAccount);
                       String jsonString1 = JSON.toJSONString(user);
                       Map map = JSON.parseObject(jsonString1, Map.class);
                       map.put("accessToken",mapCopy.get("accessToken"));
                       map.put("refreshToken",mapCopy.get("refreshToken"));
                       String jsonString = JSON.toJSONString(map);
                       response.getWriter().write(jsonString);

                   } catch (Exception e) {
                       e.printStackTrace();
                   }

               }
               else
               {
                   String accessToken=request.getParameter("accessToken");
                   String refreshToken=request.getParameter("refreshToken");
                   String jsonString1 = JSON.toJSONString(user);
                   Map map = JSON.parseObject(jsonString1, Map.class);
                   map.put("accessToken",accessToken);
                   map.put("refreshToken",refreshToken);
                   String jsonString = JSON.toJSONString(map);
                   response.getWriter().write(jsonString);


               }

           }
           else
           {
               if (request.getCookies()!=null)
               {
                   for (Cookie cookie :request.getCookies()) {
                       if ("userAccount".equals(cookie.getName())) {
                           cookie.setValue(null);
                           cookie.setMaxAge(0);
                           cookie.setPath("/");
                           response.addCookie(cookie);
                       }

                   }

             }
               response.getWriter().write("fail");

           }

       }
       public void logout(String refreshToken, Long userId)
       {
           //登陆出去的代码
           //userDao.deleteRefreshToken(refreshToken, userId);
       }
       public void editInfo(HttpServletRequest request,HttpServletResponse response)
       {
           //修改资料

           response.setCharacterEncoding("UTF-8");
           String haha=utilJudge(request,response);
           if(!haha.equals("fail"))
           {
               User userCopy=new User();
               userCopy.setImage(request.getParameter("image"));
               userCopy.setGender(request.getParameter("gender"));
               userCopy.setName(request.getParameter("name"));
               userCopy.setSignature(request.getParameter("signature"));
               String date=request.getParameter("birthday");
               //账号，邮箱
               String userAccount=TokenUtil.verifyToken(request.getParameter("accessToken"));
               System.out.println(userAccount+"检测区域");
               userCopy.setUserAccount(userAccount);
               userCopy.setEmail(service.selectEmail(userAccount));
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
               Cookie cookie=new Cookie("userAccount",userAccount);
               cookie.setMaxAge(-1);
               cookie.setPath("/");
               response.addCookie(cookie);
               if(haha.equals("over"))
               {

                   try {
                       Map<String,String>mapCopy=service.refreshForDts(userAccount);
                       String jsonString = JSON.toJSONString(mapCopy);
                       response.getWriter().write(jsonString);

                   } catch (Exception e) {
                       e.printStackTrace();
                   }

               }
               else
               {
                   String accessToken=request.getParameter("accessToken");
                   String refreshToken=request.getParameter("refreshToken");
                   Map<String,String>map=new HashMap<>();
                   map.put("accessToken",accessToken);
                   map.put("refreshToken",refreshToken);
                   String jsonString = JSON.toJSONString(map);
                   try {
                       response.getWriter().write(jsonString);
                   } catch (IOException e) {
                       e.printStackTrace();
                   }


               }



           }
           else
           {
               if (request.getCookies()!=null)
               {
                   for (Cookie cookie :request.getCookies()) {
                       if ("userAccount".equals(cookie.getName())) {
                           cookie.setValue(null);
                           cookie.setMaxAge(0);
                           cookie.setPath("/");
                           response.addCookie(cookie);
                       }

                   }

               }
               try {
                   response.getWriter().write("fail");
               } catch (IOException e) {
                   e.printStackTrace();
               }

           }










       }
       public void submitCommentPerson(HttpServletRequest request,HttpServletResponse response)
       {
           Comment comment=new Comment();
           comment.setCommentContent(request.getParameter("commentContent"));
           comment.setCreateTime(request.getParameter("createTime"));
           comment.setSongId(Integer.parseInt(request.getParameter("songId")));
           comment.setUserName(request.getParameter("userName"));
           response.setCharacterEncoding("UTF-8");
           String haha=utilJudge(request,response);
           if(!haha.equals("fail"))
           {
               String userAccount=TokenUtil.verifyToken(request.getParameter("refreshToken"));
               Cookie cookie=new Cookie("userAccount",userAccount);
               cookie.setMaxAge(-1);
               cookie.setPath("/");
               response.addCookie(cookie);
               comment.setUserId(userAccount);
               comment=service.submitCommentPerson(comment);
               if(haha.equals("over"))
               {
                   Map<String,String>mapCopy= null;
                   try {
                       mapCopy = service.refreshForDts(userAccount);
                       String jsonString1 = JSON.toJSONString(comment);
                       Map map = JSON.parseObject(jsonString1, Map.class);
                       map.put("accessToken",mapCopy.get("accessToken"));
                       map.put("refreshToken",mapCopy.get("refreshToken"));
                       String jsonString = JSON.toJSONString(map);
                       response.getWriter().write(jsonString);


                   } catch (Exception e) {
                       e.printStackTrace();
                   }


               }
               else
               {
                   String accessToken=request.getParameter("accessToken");
                   String refreshToken=request.getParameter("refreshToken");
                   String jsonString1 = JSON.toJSONString(comment);
                   Map map = JSON.parseObject(jsonString1, Map.class);
                   map.put("accessToken",accessToken);
                   map.put("refreshToken",refreshToken);
                   String jsonString = JSON.toJSONString(map, SerializerFeature.WriteMapNullValue);
                   try {
                       response.getWriter().write(jsonString);
                   } catch (IOException e) {
                       e.printStackTrace();
                   }
               }

           }
           else
           {
               if (request.getCookies()!=null)
               {
                   for (Cookie cookie :request.getCookies()) {
                       if ("userAccount".equals(cookie.getName())) {
                           cookie.setValue(null);
                           cookie.setMaxAge(0);
                           cookie.setPath("/");
                           response.addCookie(cookie);
                       }

                   }

               }
               try {
                   response.getWriter().write("fail");
               } catch (IOException e) {
                   e.printStackTrace();
               }

           }
       }
       public void replyCommentPerson(HttpServletRequest request,HttpServletResponse response)
       {
           Comment comment=new Comment();
           comment.setCommentContent(request.getParameter("commentContent"));
           comment.setCreateTime(request.getParameter("createTime"));
           comment.setUserName(request.getParameter("userName"));
           comment.setSongId(Integer.parseInt(request.getParameter("songId")));
           comment.setAncestorId(Integer.parseInt(request.getParameter("ancestorId")));
           comment.setReceiverId(Integer.parseInt(request.getParameter("receiverId")));
           //算到这路多级评论
           response.setCharacterEncoding("UTF-8");
           String haha=utilJudge(request,response);
           if(!haha.equals("fail"))
           {
               String userAccount=TokenUtil.verifyToken(request.getParameter("refreshToken"));
               Cookie cookie=new Cookie("userAccount",userAccount);
               cookie.setMaxAge(-1);
               cookie.setPath("/");
               response.addCookie(cookie);
               comment.setUserId(userAccount);
               comment=service.replyCommentPerson(comment);
               if(haha.equals("over"))
               {
                   Map<String,String>mapCopy= null;
                   try {
                       mapCopy = service.refreshForDts(userAccount);
                       String jsonString1 = JSON.toJSONString(comment);
                       Map map = JSON.parseObject(jsonString1, Map.class);
                       map.put("accessToken",mapCopy.get("accessToken"));
                       map.put("refreshToken",mapCopy.get("refreshToken"));
                       String jsonString = JSON.toJSONString(map, SerializerFeature.WriteMapNullValue);
                       System.out.println("打印出来了");
                       response.getWriter().write(jsonString);


                   } catch (Exception e) {
                       e.printStackTrace();
                   }
               }
               else
               {
                   String accessToken=request.getParameter("accessToken");
                   String refreshToken=request.getParameter("refreshToken");
                   String jsonString1 = JSON.toJSONString(comment);
                   Map map = JSON.parseObject(jsonString1, Map.class);
                   map.put("accessToken",accessToken);
                   map.put("refreshToken",refreshToken);
                   System.out.println("打印出来了");
                   String jsonString = JSON.toJSONString(map);
                   try {
                       response.getWriter().write(jsonString);
                   } catch (IOException e) {
                       e.printStackTrace();
                   }
               }

           }
           else
           {
               if (request.getCookies()!=null)
               {
                   for (Cookie cookie :request.getCookies()) {
                       if ("userAccount".equals(cookie.getName())) {
                           cookie.setValue(null);
                           cookie.setMaxAge(0);
                           cookie.setPath("/");
                           response.addCookie(cookie);
                       }

                   }

               }
               try {
                   response.getWriter().write("fail");
               } catch (IOException e) {
                   e.printStackTrace();
               }

           }
       }
             public String utilJudge(HttpServletRequest request,HttpServletResponse response)
        {
            String m="fail";
            String accessToken=request.getParameter("accessToken");
            String refreshToken=request.getParameter("refreshToken");
            String accessResult=TokenUtil.verifyToken(accessToken);
           if(accessResult.equals("Fail"))
           {
               //token被篡改了



           }
           else if(accessResult.equals("Over"))
           {
               //过期了

               String refreshResult=TokenUtil.verifyToken(refreshToken);
               if(refreshResult.equals("Over")||refreshResult.equals("Fail"))
               {
                   //这个也过不去

               }
               else
               {
                   m="over";
               }


           }
           else
           {
               //通过
               m=accessResult;
           }
           System.out.println("工具类里面测试"+m);
           return m;


        }
        public void personInfo(HttpServletRequest request,HttpServletResponse response)
        {

            response.setCharacterEncoding("UTF-8");
            System.out.println("试试这个函数");
            String haha=utilJudge(request,response);
            if(!haha.equals("fail"))
            {
                String userAccount=TokenUtil.verifyToken(request.getParameter("refreshToken"));
                Cookie cookie=new Cookie("userAccount",userAccount);
                cookie.setMaxAge(-1);
                cookie.setPath("/");
                response.addCookie(cookie);
                User user=service.selectProfileByUserAccount(userAccount);
                if(haha.equals("over"))
                {

                    try {
                        Map<String,String>mapCopy=service.refreshForDts(userAccount);
                        String jsonString1 = JSON.toJSONString(user);
                        Map map = JSON.parseObject(jsonString1, Map.class);
                        map.put("accessToken",mapCopy.get("accessToken"));
                        map.put("refreshToken",mapCopy.get("refreshToken"));
                        int concernCount=service.selectCountConcern(userAccount);
                        int concernedCount=service.selectCountConcerned(userAccount);
                        map.put("concernCount",concernCount+"");
                        map.put("concernedCount",concernedCount+"");
                        String jsonString = JSON.toJSONString(map);
                        response.getWriter().write(jsonString);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                else
                {
                    String accessToken=request.getParameter("accessToken");
                    String refreshToken=request.getParameter("refreshToken");
                    String jsonString1 = JSON.toJSONString(user);
                    Map map = JSON.parseObject(jsonString1, Map.class);
                    int concernCount=service.selectCountConcern(userAccount);
                    int concernedCount=service.selectCountConcerned(userAccount);
                    map.put("concernCount",concernCount+"");
                    map.put("concernedCount",concernedCount+"");
                    map.put("accessToken",accessToken);
                    map.put("refreshToken",refreshToken);
                    String jsonString = JSON.toJSONString(map);
                    try {
                        response.getWriter().write(jsonString);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }

            }
            else
            {
                if (request.getCookies()!=null)
                {
                    for (Cookie cookie :request.getCookies()) {
                        if ("userAccount".equals(cookie.getName())) {
                            cookie.setValue(null);
                            cookie.setMaxAge(0);
                            cookie.setPath("/");
                            response.addCookie(cookie);
                        }

                    }

                }
                try {
                    response.getWriter().write("fail");
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }


        }




}
