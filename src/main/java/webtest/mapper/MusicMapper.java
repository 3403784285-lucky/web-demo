package webtest.mapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import webtest.pojo.Comment;
import webtest.pojo.Music;

public interface MusicMapper {
    @Select("select * from temporaryplayer,music,liketable where temporaryplayer.userid=#{userAccount} and temporaryplayer.musicId=music.id and music.id=liketable.musicid and liketable.likepersonid=#{userAccount}")
    Music[] readMusicPlay(String userAccount);
    @Delete(" delete from temporaryplayer where userid=#{userAccount}")
    void deleteTable(String userAccount);
    @Insert("insert into liketable(musicid,likepersonid)values (#{musicId},#{userAccount})")
    void likeAction(@Param("musicId")int musicId,@Param("userAccount")String userAccount);

    Comment[]commentDetail(int id);

    Comment[] readComment(int musicId);
    @Delete("delete from liketable where musicid=#{musicId} and likepersonid=#{userAccount}")
    void removeLikeAction(@Param("userAccount") String userAccount,@Param("musicId") int musicId);
    @Select("select * from liketable,music where liketable.likepersonid=#{userAccount} and liketable.musicid=music.id")
    Music[] selectLikeSong(String userAccount);




}
