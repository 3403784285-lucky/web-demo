package webtest.service;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import webtest.mapper.MusicMapper;
import webtest.mapper.UserMapper;
import webtest.pojo.Comment;
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
        sqlSession.close();
        return musics;
    }
    public void deleteTable(String userAccount)
    {
        SqlSession sqlSession=factory.openSession();
        MusicMapper mapper=sqlSession.getMapper(MusicMapper.class);
        mapper.deleteTable(userAccount);
        sqlSession.commit();
        sqlSession.close();

    }
    public void removeLikeAction(String userAccount,int musicId)
    {
        SqlSession sqlSession=factory.openSession();
        MusicMapper mapper=sqlSession.getMapper(MusicMapper.class);
        mapper.removeLikeAction(userAccount,musicId);
        sqlSession.commit();
        sqlSession.close();
    }
    public Music[] selectLikeSong(String userAccount)
    {
        SqlSession sqlSession=factory.openSession();
        MusicMapper mapper=sqlSession.getMapper(MusicMapper.class);
        Music[]musics=mapper.selectLikeSong(userAccount);
        sqlSession.close();
        return musics;
    }
    public void likeAction(String userAccount,int musicId)
    {
        SqlSession sqlSession=factory.openSession();
        MusicMapper mapper=sqlSession.getMapper(MusicMapper.class);
        mapper.likeAction(musicId,userAccount);
        sqlSession.commit();
        sqlSession.close();

    }
    public Comment[] readComment(int musicId)
    {
        SqlSession sqlSession=factory.openSession();
        MusicMapper mapper=sqlSession.getMapper(MusicMapper.class);
        Comment[] comments=mapper.readComment(musicId);
        sqlSession.close();
        return comments;
    }
    public Comment[] commentDetail(int id)
    {
        SqlSession sqlSession=factory.openSession();
        MusicMapper mapper=sqlSession.getMapper(MusicMapper.class);
        Comment[] comments=mapper.commentDetail(id);
        sqlSession.close();
        return comments;
    }
}
