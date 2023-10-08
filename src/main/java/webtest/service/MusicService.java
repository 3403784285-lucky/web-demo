package webtest.service;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import webtest.mapper.MusicMapper;
import webtest.mapper.UserMapper;
import webtest.pojo.Music;
import webtest.pojo.User;
import webtest.utils.SqlSessionFactoryUtils;

public class MusicService {
    SqlSessionFactory factory= SqlSessionFactoryUtils.getSqlSessionFactory();
    public Music[] readMusicPlay(String userAccount)
    {
        SqlSession sqlSession=factory.openSession();
        MusicMapper mapper=sqlSession.getMapper(MusicMapper.class);
        Music[] musics=mapper.readMusicPlay(userAccount);
        return musics;
    }
}
