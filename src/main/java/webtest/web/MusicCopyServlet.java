package webtest.web;

import com.alibaba.fastjson.JSON;
import webtest.mapper.MusicMapper;
import webtest.pojo.Music;
import webtest.pojo.User;
import webtest.service.MusicService;
import webtest.service.UserService;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet("/musicCopy/*")
public class MusicCopyServlet extends BaseServlet{
    UserService service1=new UserService();
    MusicService service2=new MusicService();
    public void readMusicPlay(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session=request.getSession();
        User user=(User)session.getAttribute("user");
        System.out.println(user.getUserAccount());
        Music[] musics=service2.readMusicPlay(user.getUserAccount());
        //之前不知道传过来，现在可以传过来了，先将对象序列化为json；
        String jsons=JSON.toJSONString(musics);
        System.out.println(jsons);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(jsons);

    }


}
