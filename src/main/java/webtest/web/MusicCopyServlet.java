package webtest.web;

import com.alibaba.fastjson.JSON;
import webtest.pojo.Comment;
import webtest.pojo.Music;
import webtest.pojo.User;
import webtest.service.MusicService;
import webtest.service.UserService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/musicCopy/*")
public class MusicCopyServlet extends BaseServlet{

    MusicService service=new MusicService();
    public void readMusicPlay(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Cookie[] cookies = request.getCookies();
        String userAccount = "";
        if (null != cookies) {
            for (Cookie cookie : cookies) {
                if ("userAccount".equals(cookie.getName())) {
                    userAccount = cookie.getValue();
                }
            }
            System.out.println(userAccount);
            Music[] musics = service.readMusicPlay(userAccount);
            //可以传过来了，先将对象序列化为json；
            String jsons = JSON.toJSONString(musics);
            System.out.println(jsons);
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(jsons);
        } else {
            response.getWriter().write("errorInfo");
        }


    }
    public void deleteTable(HttpServletRequest request,HttpServletResponse response) {

            service.deleteTable(getUserId(request,response));

    }
    public void likeAction(HttpServletRequest request,HttpServletResponse response) {
        int musicId = Integer.parseInt(request.getParameter("musicId"));

            service.likeAction(getUserId(request,response), musicId);

    }

    public void removeLikeAction(HttpServletRequest request,HttpServletResponse response)
    {
        int musicId = Integer.parseInt(request.getParameter("musicId"));
        service.removeLikeAction(getUserId(request,response), musicId);
    }
    public void commentDetail(HttpServletRequest request,HttpServletResponse response)
    {
        response.setCharacterEncoding("UTF-8");
        int id=Integer.parseInt(request.getParameter("commentId"));
        Comment[] comments=service.commentDetail(id);
        String jsons=JSON.toJSONString(comments);
        try {
            response.getWriter().write(jsons);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void readComment(HttpServletRequest request,HttpServletResponse response)
    {

        response.setCharacterEncoding("UTF-8");
        int musicId=Integer.parseInt(request.getParameter("musicId"));
        Comment[] comments=service.readComment(musicId);
        String jsons=JSON.toJSONString(comments);
        try {
            response.getWriter().write(jsons);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void likeInfo(HttpServletRequest request,HttpServletResponse response)
    {

        response.setCharacterEncoding("UTF-8");
        Music[]musics=service.selectLikeSong(getUserId(request,response));
        String jsons=JSON.toJSONString(musics);
        try {
            response.getWriter().write(jsons);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public String getUserId(HttpServletRequest request,HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        String userAccount = "";
        if (null != cookies) {
            for (Cookie cookie : cookies) {
                if ("userAccount".equals(cookie.getName())) {
                    userAccount = cookie.getValue();
                }
            }

        }
        return userAccount;
    }
}
