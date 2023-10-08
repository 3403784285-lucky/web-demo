
var player=document.getElementById("player");
var audio=document.getElementsByTagName('audio')[0];
var musicname=document.getElementsByClassName('china')[0];
var prev=document.getElementsByClassName('prev')[0];
var next=document.getElementsByClassName('next')[0];
var model=document.getElementsByClassName('model')[0];
var processBarBg=document.getElementById('processBarBg');
var volumeBarBg=document.getElementById('volumeBarBg');
var dot=document.getElementById('processDot');
var volumeBar=document.getElementById('volumeBar');
var loudness=document.getElementsByClassName('loudness')[0];
var music=document.getElementsByClassName('music-list')[0];
var musicBroadcast= document.getElementsByClassName('music-broadcast')[0];

var rate=0;
var mark=0;
var num=0;
var rateCopy;
var height;
var musics;
// musicBroadcast.style.display='none';
window.onload=function () {
    axios.post("http://localhost:8080/web-demo/musicCopy/readMusicPlay").then(function (response) {
        musics = response.data;
        for (let i = 0; i < musics.length; i++) {
            if (musics[i]["playRange"] != null) {
                num = musics[i]["id"] - 1;
                var li=document.createElement('li');
                li.setAttribute("style", "width: 100%;height: 30px;line-height: 30px;");

                var span1 = document.createElement("span");
                span1.setAttribute("style", "margin-right: 140px;");
                span1.innerHTML = musics[i]["name"];

                var span2 = document.createElement("span");
                span2.setAttribute("style", "margin-right: 80px;color: #7c8892;");
                span2.innerHTML = "陈雪凝";

                var span3 = document.createElement("span");
                span3.setAttribute("style", "color: #d0d5ca; font-size: 13px;");
                span3.innerHTML = "3:12";

                li.appendChild(span1);
                li.appendChild(span2);
                li.appendChild(span3);
                document.getElementById("musicListLi").appendChild(li);


            }
        }
        if(musics.length==0)
        {
            //将为零的情况忽略了
            document.getElementsByTagName("footer")[0].disabled=true;
        }

        musicname.innerHTML = musics[num]["name"];
        audio.src = musics[num]["musicSrc"];
        audio.load();
        audio.addEventListener('loadedmetadata',function ()
        {
            //之前一直没用弄明白我靠原来是没有加载完就将它获取了以至于得到的是NaN
            audio.currentTime = musics[num]["playRange"];
            updateProgress();
        });

    }).catch(function (error) {
        // 请求失败，提示错误信息
        console.error(error);
    });
}
    dragProcessDotEvent();

//网页加载完毕后会立刻执行的一个操作
//有两个模式，列表循环和单曲循环，默认是列表循环
// 还没有考虑是否是空的情况
//音乐有多首，添加音乐数组
//拖动进度条也可以调节进度
// 获取音频对象
// 获取第一个音频对象
// 点击播放暂停图片时，控制音乐的播放与暂停

    player.addEventListener('click', myFunction1);

    function myFunction1() {
        //更新进度条与当前播放时间
        audio.addEventListener('timeupdate', function () {
            updateProgress();
        });
        // 两天的变化，一个空格是无法清楚的，每个空格隔开就相当于一个类名；

        if (audio.paused) {
            audio.play();
            player.classList.remove("icon-zanting");
            player.classList.add("icon-suspend");


        } else {
            audio.pause();
            player.classList.remove("icon-suspend");
            player.classList.add("icon-zanting");


        }
    }

//定义方法参数可以不指定类型
    function updateProgress() {
        // 更新进度条
        var value = audio.currentTime / audio.duration;
        console.log(audio.currentTime+"hhh"+audio.duration);
        document.getElementById('processBar').style.width = value * 100 + '%';
        document.getElementById('processDot').style.left = value * 100 + '%';
        document.getElementById('audioCurTime').innerText = transTime(audio.currentTime);
        document.getElementById('totalTime').innerHTML = transTime(audio.duration);

    }

    audio.addEventListener('ended', function () {

        //如果设置了单曲循环该监听器则会消失
        num++;
        if (num == musics.length) num = 0;
        audio.src = musics[num]["musicSrc"];
        musicname.innerHTML = musics[num]["name"];
        audio.play();

    });

    model.addEventListener('click', function () {
        if (mark == 0) {
            audio.loop = true;
            mark = 1;
            model.classList.remove("icon-yinleliebiao");
            model.classList.add("icon-xunhuanbofang");

        } else {
            mark = 0;
            audio.loop = false;
            model.classList.remove("icon-xunhuanbofang");
            model.classList.add("icon-yinleliebiao");

        }
    });

    prev.addEventListener('click', function () {
        num--;
        if (num < 0) num = musics.length - 1;
        audio.src = musics[num]["musicSrc"];
        musicname.innerHTML = musics[num]["name"];
        player.classList.remove("icon-zanting");
        player.classList.add("icon-suspend");
        audio.play();
    });

    next.addEventListener('click', function () {
        num++;
        if (num ==musics.length) num = 0;
        audio.src = musics[num]["musicSrc"];
        musicname.innerHTML = musics[num]["name"];
        player.classList.remove("icon-zanting");
        player.classList.add("icon-suspend");
        audio.play();

    });

    function transTime(value) {
        var time = "";
        var h = parseInt(value / 3600);
        value %= 3600;
        var m = parseInt(value / 60);
        var s = parseInt(value % 60);
        if (h > 0) {
            time = formatTime(h + ":" + m + ":" + s);
        } else {
            time = formatTime(m + ":" + s);
        }
        return time;
    }

    function formatTime(value) {
        var time = "";
        var s = value.split(":");
        var i = 0;
        for (; i < s.length - 1; i++) {
            time += s[i].length == 1 ? ("0" + s[i]) : s[i];
            time += ":";
        }
        time += s[i].length == 1 ? ("0" + s[i]) : s[i];
        return time;
    }

    processBarBg.addEventListener('mousedown', function (event) {
        //音乐开始播放之后和播放过并且暂停了也可以
        if (!audio.paused || audio.currentTime != 0) {
            var pgsWidth = parseFloat(window.getComputedStyle(processBarBg, null).width.replace('px', ''));
            var rate = event.offsetX / pgsWidth;
            audio.currentTime = audio.duration * rate;
            updateProgress();
        }
    });

    function dragProcessDotEvent() {

        var position = {
            oriOffestLeft: 0,
            oriX: 0,
            maxLeft: 0,
            maxRight: 0
        };
        //监听器出了一次性函数也可以传函数名进去，不传参数也可以执行
        //标记是否拖动开始
        var flag = false;
        dot.addEventListener('mousedown', down, false);
        document.addEventListener('mousemove', move, false);
        document.addEventListener('mouseup', end, false);

        function down(event) {
            if (!audio.paused || audio.currentTime != 0) {
                //只有在这种情况下才表示拖动开始
                flag = true;
                position.oriOffestLeft = dot.offsetLeft;
                position.oriX = event.clientX;
                position.maxLeft = position.oriOffestLeft;
                position.maxRight = document.getElementById('processBarBg').offsetWidth - position.oriOffestLeft;

                //禁止默认事件（！！！）
                if (event) {
                    event.preventDefault();

                }
                //禁止事件冒泡
                if (event) {
                    event.stopPropagation();
                }

            }
        }

        function move(event) {
            if (flag) {
                var clientX = event.clientX;
                var length = clientX - position.oriX;//用现在的鼠标坐标-原来的点的坐标
                if (length > position.maxRight) {
                    length = position.maxRight;
                } else if (length < -position.maxLeft) {
                    length = -position.maxLeft;
                }
                var processBarBg = document.getElementById('processBarBg');
                var pgsWidth = parseFloat(window.getComputedStyle(processBarBg, null).width.replace('px', ''));
                var rate = (position.oriOffestLeft + length) / pgsWidth;
                audio.currentTime = audio.duration * rate;
                updateProgress();

            }

        }

        function end() {
            flag = false;
        }


    }

    volumeBarBg.addEventListener('mousedown', function (event) {

        var volumeHeight = parseFloat(window.getComputedStyle(volumeBarBg, null).height.replace('px', ''));
        rate = event.offsetY / volumeHeight;
        audio.volume = 1 - rate;
        volumeBar.style.height = rate * 100 + '%';


        if (rate == 1) {
            loudness.classList.remove("icon-yinliang");
            loudness.classList.add("icon-jingyin");
        }
    }, false);

    loudness.addEventListener('click', function (event) {
        if (rate != 1) {
            loudness.classList.remove("icon-yinliang");
            loudness.classList.add("icon-jingyin");
            rateCopy = rate;
            height = volumeBar.style.height;
            volumeBar.style.height = 80 + 'px';
            rate = 1;
            audio.volume = 1 - rate;
        } else {
            loudness.classList.remove("icon-jingyin");
            loudness.classList.add("icon-yinliang");
            rate = rateCopy;
            volumeBar.style.height = height;
            audio.volume = 1 - rate;
        }
    });

    music.addEventListener('click', function () {

        if (musicBroadcast.style.display == 'none') {
            musicBroadcast.style.display = 'block';
        } else {
            musicBroadcast.style.display = 'none';
        }

    });
