package webtest.web;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class BaseServlet extends HttpServlet {
    @Override
    protected  void service(HttpServletRequest req, HttpServletResponse resp)
    {
        //获取8080后面的
        String uri= req.getRequestURI();
        //获取最后一个/后面的内容
        String methodName=uri.substring(uri.lastIndexOf('/')+1);
        System.out.println(methodName);
        try
        {
            Method method=this.getClass().getMethod(methodName,HttpServletRequest.class,HttpServletResponse.class);
            method.invoke(this,req,resp);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
