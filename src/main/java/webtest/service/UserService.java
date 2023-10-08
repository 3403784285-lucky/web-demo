package webtest.service;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import webtest.mapper.UserMapper;
import webtest.pojo.User;
import webtest.utils.SqlSessionFactoryUtils;

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
    public void updateProfile(User user)
    {
        SqlSession sqlSession=factory.openSession();
        UserMapper mapper=sqlSession.getMapper(UserMapper.class);
        mapper.updateProfile(user.getImage(), user.getName(),user.getSignature(),user.getGender(),user.getBirthday(),user.getEmail());
        sqlSession.commit();
        sqlSession.close();
    }

}
