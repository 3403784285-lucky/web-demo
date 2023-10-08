package webtest.pojo;
public class Music {
    int id;
    String playRange;
    String musicSrc;
    int musicId;
    String image;
    String name;
    String song;
    String singerId;
    String count;
    String commentCount;
    String time;

    public Music(int id, String playRange,String musicSrc,int musicId, String image, String name, String song, String singerId, String count, String commentCount, String time) {
        this.id = id;
        this.playRange = playRange;
        this.musicSrc=musicSrc;
        this.musicId=musicId;
        this.image = image;
        this.name = name;
        this.song = song;
        this.singerId = singerId;
        this.count = count;
        this.commentCount = commentCount;
        this.time = time;
    }
    public Music()
    {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlayRange() {
        return playRange;
    }

    public String getMusicSrc() {
        return musicSrc;
    }

    public void setMusicSrc(String musicSrc) {
        this.musicSrc = musicSrc;
    }

    public void setPlayRange(String playRange) {
        this.playRange = playRange;
    }

    public int getMusicId() {
        return musicId;
    }

    public void setMusicId(int musicId) {
        this.musicId = musicId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSong() {
        return song;
    }

    public void setSong(String song) {
        this.song = song;
    }

    public String getSingerId() {
        return singerId;
    }

    public void setSingerId(String singerId) {
        this.singerId = singerId;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(String commentCount) {
        this.commentCount = commentCount;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Music{" +
                "id=" + id +
                ", playRange='" + playRange + '\'' +
                ", musicSrc='" + musicSrc + '\'' +
                ", musicId=" + musicId +
                ", image='" + image + '\'' +
                ", name='" + name + '\'' +
                ", song='" + song + '\'' +
                ", singerId='" + singerId + '\'' +
                ", count='" + count + '\'' +
                ", commentCount='" + commentCount + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
