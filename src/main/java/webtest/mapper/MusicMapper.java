package webtest.mapper;

import org.apache.ibatis.annotations.Select;
import webtest.pojo.Music;

public interface MusicMapper {
    @Select("select * from temporaryplayer,music where temporaryplayer.userid=#{userAccount} and temporaryplayer.musicId=music.id")
    Music[] readMusicPlay(String userAccount);


}
