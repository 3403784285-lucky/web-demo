//代码的顺序不能乱调，不然会获取不到
//不是用户登录也可以听歌，但是列表不可以储存
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
var musicFrame= document.getElementsByClassName('music-frame')[0];
var musicListLi=document.getElementById('musicListLi');
var musicImage=document.getElementsByClassName('music-image')[0];
// var deleteTable=document.getElementsByClassName('delete-table')[0];
var likeTag=document.getElementsByClassName('like')[0];
var rate=0;
var mark=0;
var num=0;
var init=0;
var numCopy=-1;
var markNumOr=1;
var rateCopy;
var height;
//不能删除单条音乐，在临时列表里面
let musics=JSON.parse(localStorage.getItem("musics"));
console.log(musics);
musicFrame.style.display='none';

    if (document.getElementsByClassName('active-image')[0]!=null)
    {
        document.getElementsByClassName('active-image')[0].style.animationPlayState='paused';
        document.getElementsByClassName('stick')[0].classList.add('rotateCircle');
    }
    console.log("2");

if(musics=="errorInfo")
{

}
else
{
    if(musics[num]["likePersonId"]!=null||musics[num]["likePersonId"]!='')
    {
        likeTag.classList.remove('icon-jushoucang');
        likeTag.classList.add('icon-svg_nav_Like_press');
    }
    for (let i = 0; i < musics.length; i++) {
        if (musics[i]["playRange"] != null) {
            num = i;
        }
        var li=document.createElement('li');
        if(markNumOr%2==0)
        {
            li.setAttribute("style", "width: 100%;height: 30px;line-height: 30px;background: rgba(224,224,220,0.27);display: flex;");
        }
        else
        {
            li.setAttribute("style", "width: 100%;height: 30px;line-height: 30px;background: white;display: flex;");
        }
        markNumOr++;
        var span1 = document.createElement("span");
        span1.setAttribute("style", " width: 110px;margin-right: 60px;");
        span1.innerHTML = musics[i]["name"];

        var span2 = document.createElement("span");
        span2.setAttribute("style", "color: #7c8892; width: 80px;margin-right:40px;");
        span2.innerHTML = "陈雪凝";

        var span3 = document.createElement("span");
        span3.setAttribute("style", "color: #d0d5ca; font-size: 13px;width: 60px;");
        span3.innerHTML=musics[i]["totalTime"];
        li.appendChild(span1);
        li.appendChild(span2);
        li.appendChild(span3);
        musicListLi.appendChild(li);
    }

    var observer=new MutationObserver(function (mutations)
    {
        if(musicListLi.children.length==0)
        {
            console.log("禁用加消失");

            if(!audio.paused)
                audio.pause();
            disableLinks();
        }
        else
        {
            console.log("重新启用");
            releaseLinks();

        }
    });
    var config={childList:true,subtree:true};
    observer.observe(musicListLi,config);
    function disableLinks() {
        var footerBtn = document.getElementsByClassName("footer-btn")[0];
        var links = footerBtn.getElementsByClassName('iconfont');
        for (let i = 0; i < links.length-1; i++) {
            console.log("wawa"+i);
            links[i].style.visibility='hidden';
        }
    }
    function  releaseLinks()
    {
        var footer = document.getElementsByTagName("footer")[0];
        var links = footer.getElementsByClassName('iconfont');
        for (let i = 0; i < links.length-1; i++) {
            console.log("haha"+i);
            links[i].style.visibility='visible';
        }
    }


}


var items=musicListLi.getElementsByTagName('li');
for(let i=0;i<items.length;i++)
{
    items[i].addEventListener('click',function ()
    {
        audio.src = musics[i]["musicSrc"];
        musicname.innerHTML = musics[i]["name"];
        musicImage.src=musics[num]["image"];
        if(audio.paused)
        {
            player.classList.remove("icon-zanting");
            player.classList.add("icon-suspend");
            audio.play();
        }
        else
        {
            audio.play();
        }
    })
}
for(let i=0;i<items.length;i++)
{
    items[i].addEventListener('mouseover',function ()
    {
        console.log("第"+i+"个元素悬浮");
        items[i].style.background='rgba(139,126,126,0.56)';
    });
    items[i].addEventListener('mouseout',function ()
    {
        console.log("第"+i+"个元素移开");
        if(i%2==0)
        {

            items[i].style.background='rgba(224,224,220,0.27)';
        }
        else
        {
            items[i].style.background='white';
        }
    });
}

musicname.innerHTML = musics[num]["name"];
audio.src = musics[num]["musicSrc"];
musicImage.src=musics[num]["image"];
audio.load();
audio.addEventListener('loadedmetadata',function ()
{
    //之前一直没用弄明白我靠原来是没有加载完就将它获取了以至于得到的是NaN
    if(init==0)
    {
        audio.currentTime = musics[num]["playRange"];
        init++;
    }

    updateProgress();
});
audio.addEventListener('timeupdate',function ()
{
    if(numCopy!=num&&numCopy!=-1)
    {
        if (numCopy%2==0)
        {
            items[numCopy].style.background='rgba(224,224,220,0.27)';
        }
        else
        {
            items[numCopy].style.background='white';
        }
    }
    numCopy=num;
    updateProgress();
    items[num].style.background='##6CC7F6';
});



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

        // 两天的变化，一个空格是无法清楚的，每个空格隔开就相当于一个类名；

        if (audio.paused) {
            audio.play();
            console.log("播放");
            player.classList.remove("icon-zanting");
            player.classList.add("icon-suspend");
           if ( document.getElementsByClassName("stick")[0]!=null)
           {
               document.getElementsByClassName("stick")[0].classList.remove("rotateCircle");
               document.getElementsByClassName('active-image')[0].style.animationPlayState='running';
           }





        } else {
            audio.pause();
            console.log("暂停");
            player.classList.remove("icon-suspend");
            player.classList.add("icon-zanting");
            if ( document.getElementsByClassName("stick")[0]!=null)
            {
                document.getElementsByClassName("stick")[0].classList.add("rotateCircle");
                document.getElementsByClassName('active-image')[0].style.animationPlayState='paused';

            }



        }
    }

//定义方法参数可以不指定类型
    function updateProgress() {
        // 更新进度条
        var value = audio.currentTime / audio.duration;

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
        musicImage.src=musics[num]["image"];
        player.classList.remove("icon-zanting");
        player.classList.add("icon-suspend");
        audio.play();
    });

    next.addEventListener('click', function () {
        num++;
        if (num ==musics.length) num = 0;
        audio.src = musics[num]["musicSrc"];
        musicname.innerHTML = musics[num]["name"];
        musicImage.src=musics[num]["image"];
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

        if (musicFrame.style.display == 'none') {
            musicFrame.style.display = 'block';
        } else {
            musicFrame.style.display = 'none';
        }

    });
    // deleteTable.addEventListener('click',function ()
    // {
    //     let param = new URLSearchParams()
    //     param.append('musicId',musics[num]["musicId"]+"");
    //     axios.post("http://localhost:8080/web-demo/musicCopy/deleteTable",param).then(function (response) {
    //         while(musicListLi.firstChild)
    //         {
    //             musicListLi.removeChild(musicListLi.firstChild);
    //         }
    //
    //     }).catch(function(error) {
    //         // 请求失败，提示错误信息
    //         console.error(error);
    //     });
    // });
    likeTag.addEventListener('click',function ()
    {         let param = new URLSearchParams()
              param.append('musicId',musics[num]["musicId"]+"");
            if(likeTag.classList.contains('icon-jushoucang'))
        {

            axios.post("http://localhost:8080/web-demo/musicCopy/likeAction",param).then(function (response) {
                likeTag.classList.remove('icon-jushoucang');
                likeTag.classList.add('icon-svg_nav_Like_press');
            }).catch(function(error) {
                // 请求失败，提示错误信息
                console.error(error);
            });
        }
        else
        {
            axios.post("http://localhost:8080/web-demo/musicCopy/removeLikeAction",param).then(function (response) {
                likeTag.classList.remove('icon-svg_nav_Like_press');
                likeTag.classList.add('icon-jushoucang');
            }).catch(function(error) {
                // 请求失败，提示错误信息
                console.error(error);
            });

        }


    });
    musicname.addEventListener('click',function()
    {
        window.location.href = '/web-demo/song.html';
    });







