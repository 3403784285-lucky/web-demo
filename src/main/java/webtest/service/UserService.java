package webtest.service;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import webtest.mapper.UserMapper;
import webtest.pojo.Comment;
import webtest.pojo.PlayList;
import webtest.pojo.User;
import webtest.utils.MD5Util;
import webtest.utils.RSAUtil;
import webtest.utils.SqlSessionFactoryUtils;
import webtest.utils.TokenUtil;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class UserService {
    SqlSessionFactory factory= SqlSessionFactoryUtils.getSqlSessionFactory();
    public User login(String userAccount,String password)
    {
        SqlSession sqlSession=factory.openSession();
        UserMapper mapper=sqlSession.getMapper(UserMapper.class);
        User user=mapper.select(userAccount, password);
        sqlSession.close();
        return user;
    }
    public User judgeEmail(String email)
    {
        //sqlSession最好不要混，也就是每个方法里面创建一个
        SqlSession sqlSession=factory.openSession();
        UserMapper mapper=sqlSession.getMapper(UserMapper.class);
        User user=mapper.selectByEmail(email);
        sqlSession.close();
        return user;
    }
    public User judgeReAccount(String account)
    {
        SqlSession sqlSession=factory.openSession();
        UserMapper mapper=sqlSession.getMapper(UserMapper.class);
        User user=mapper.selectByUserAccount(account);
        sqlSession.close();
        return user;
    }
    public void addMember(User user)
    {
        SqlSession sqlSession=factory.openSession();
        UserMapper mapper=sqlSession.getMapper(UserMapper.class);
        mapper.add(user.getEmail(),user.getUserAccount(),user.getPassword());
        //我靠，增删改查怎么能忘记提交事务，我哭死
        sqlSession.commit();
        sqlSession.close();

    }
    public void modifyPassword(User user)
    {
        SqlSession sqlSession=factory.openSession();
        UserMapper mapper=sqlSession.getMapper(UserMapper.class);
        mapper.modifyPassword(user.getEmail(),user.getPassword());
        //我靠，增删改查怎么能忘记提交事务，我哭死
        sqlSession.commit();
        sqlSession.close();
    }

    public void initUser(String email)
    {
        SqlSession sqlSession=factory.openSession();
        UserMapper mapper=sqlSession.getMapper(UserMapper.class);
        mapper.initUser(email);
        sqlSession.commit();
        sqlSession.close();



    }
    public User selectProfile(String email)
    {
        SqlSession sqlSession=factory.openSession();
        UserMapper mapper=sqlSession.getMapper(UserMapper.class);
        User user=mapper.selectProfile(email);
        sqlSession.close();
        return user;

    }
    public String selectEmail(String userAccount)
    {
        SqlSession sqlSession=factory.openSession();
        UserMapper mapper=sqlSession.getMapper(UserMapper.class);
        String email=mapper.selectEmail(userAccount);
        sqlSession.close();
        return email;

    }
    public int selectCountConcerned(String userAccount)
    {
        SqlSession sqlSession=factory.openSession();
        UserMapper mapper=sqlSession.getMapper(UserMapper.class);
        Integer count=mapper.selectCountConcerned(userAccount);
        sqlSession.close();
        if (count==null) return 0;
        else return count;

    }
    public int selectCountConcern(String userAccount)
    {
        SqlSession sqlSession=factory.openSession();
        UserMapper mapper=sqlSession.getMapper(UserMapper.class);
        Integer count=mapper.selectCountConcern(userAccount);
        sqlSession.close();
        if (count==null) return 0;
        else return count;
    }
    public User selectProfileByUserAccount(String userAccount)
    {
        SqlSession sqlSession=factory.openSession();
        UserMapper mapper=sqlSession.getMapper(UserMapper.class);
        User user=mapper. selectProfileByUserAccount(userAccount);
        sqlSession.close();
        return user;

    }
    public Comment replyCommentPerson(Comment comment)
    {
        SqlSession sqlSession=factory.openSession();
        UserMapper mapper=sqlSession.getMapper(UserMapper.class);
        mapper.replyCommentPerson(comment);
        sqlSession.commit();
        sqlSession.close();
        return comment;
    }
    public void submitPlayListInfo(PlayList playList)
    {
        SqlSession sqlSession=factory.openSession();
        UserMapper mapper=sqlSession.getMapper(UserMapper.class);
        mapper.submitPlayListInfo(playList);
        sqlSession.commit();
        sqlSession.close();
    }
    public Comment submitCommentPerson(Comment comment)
    {
        SqlSession sqlSession=factory.openSession();
        UserMapper mapper=sqlSession.getMapper(UserMapper.class);
        mapper.submitCommentPerson(comment);
        sqlSession.commit();
        sqlSession.close();
        return comment;
    }
    public void updateProfile(User user)
    {
        SqlSession sqlSession=factory.openSession();
        UserMapper mapper=sqlSession.getMapper(UserMapper.class);
        mapper.updateProfile(user.getImage(), user.getName(),user.getSignature(),user.getGender(),user.getBirthday(),user.getEmail());
        sqlSession.commit();
        sqlSession.close();
    }
    public User selectUserSurvive(String userAccount)
    {
        SqlSession sqlSession=factory.openSession();
        UserMapper mapper=sqlSession.getMapper(UserMapper.class);
        User result=mapper.selectByUserAccount(userAccount);
        sqlSession.commit();
        sqlSession.close();
        return  result;

    }


    public Map<String,String> refreshForDts(String userAccount) throws Exception {
        Map<String,String>map=new HashMap<>();
        String accessToken = TokenUtil.generateToken(userAccount);
        String refreshToken = TokenUtil.generateRefreshToken(userAccount);
        map.put("accessToken",accessToken);
        map.put("refreshToken",refreshToken);
        return map;
    }

}
