package webtest.mapper;

import org.apache.ibatis.annotations.*;
import webtest.pojo.Comment;
import webtest.pojo.PlayList;
import webtest.pojo.User;

import java.sql.Date;

public interface UserMapper {
    //    用户登陆的
    @Select("select * from user where userAccount=#{userAccount} and password=#{password}")
    User select(@Param("userAccount") String userAccount,@Param("password") String password);
    //根据用户名查询用户对象
    @Select("select *from user where email=#{email}")
    User selectByEmail(String email);
    //添加用户信息,注意顺序
    @Insert("insert  into user (email,userAccount,password) values(#{email},#{userAccount},#{password})")
    void add(@Param("email")String email,@Param("userAccount")String userAccount,@Param("password")String password);
    //查重账号
    @Select("select * from user where userAccount=#{userAccount}")
    User selectByUserAccount(String account);
    @Update("update user set password=#{password} where email=#{email}")
    void modifyPassword(@Param("email") String email,@Param("password")String password);
    @Select("select email from user where userAccount=#{userAccount}")
    String selectEmail(String userAccount);
    @Select("select * from user,userprofile where userAccount=#{userAccount} and user.email=userprofile.email")
    User selectProfileByUserAccount(String userAccount);
    @Insert("insert into userprofile (email) values (#{email}))")
    void initUser(String email);
    @Select("select *from userprofile where email=#{email}")
    User selectProfile(String email);

    Integer selectCountConcern(String userAccount);

    Integer selectCountConcerned(String userAccount);

    @Insert("insert into playlist values(null,#{playName},#{playCover},#{playSignature},#{playLabel}),#{userId})")
    void  submitPlayListInfo(PlayList playList);

    @Update("update userprofile set image=#{image},name=#{name},signature=#{signature},gender=#{gender},birthday=#{birthday}where email=#{email}")
    void updateProfile(@Param("image")String image, @Param("name")String name, @Param("signature")String signature, @Param("gender")String gender, @Param("birthday")Date birthday,@Param("email")String email);

    @Select("select refreshtoken from user where userAccount=#{userAccount} ")
    String getRefreshToken(String userAccount);

    @Update("update user set refreshtoken=#{refreshToken} where userAccount=#{userAccount}")
    void updateRefreshToken(@Param("refreshToken")String refreshToken,@Param("userAccount")String userAccount);

    void submitCommentPerson(Comment comment);

    void replyCommentPerson(Comment comment);



}
