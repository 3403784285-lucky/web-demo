var wheelMark=0;
var img=document.getElementById('avatar');
img.src=localStorage.getItem("image");
document.getElementsByClassName('information')[0].innerHTML=localStorage.getItem("name");
console.log("11111111111111111111");
const fileAddress =musics[num]["song"];
var lrcArray=[];
fetch(fileAddress)
    .then(response => response.text())
    .then(data =>
    {

        var parts=data.split('\n');
        for(let index=0;index<parts.length;index++)
        {
            parts[index]=getLrcObj(parts[index]);
        }

        lrcArray=parts;
        inputLrc();
        console.log("事件绑定啦");
        audio.addEventListener('timeupdate',setPosition);

    })
    .catch(error => console.error(error));
function  getLrcObj(content)
{
    //把一句分成两部分
    var twoParts=content.split("]");
    //取从第二个字符开始的子串
    var time=twoParts[0].substr(1);
    //将时间都转换成秒
    var timeParts=time.split(':');
    //这里加号运算符表示转换成数字
    var seconds= +timeParts[1];
    var min= +timeParts[0];
    seconds=min*60+seconds;
    var words=twoParts[1];
    return{
        seconds:seconds,
            words:words,
    };


}

function inputLrc()
{
    for(let index=0;index<lrcArray.length;index++)
    {
        var li=document.createElement('li');
        li.innerText=lrcArray[index].words;
        li.style.margin='10px auto 10px 20px';
        li.addEventListener('click',function ()
        {
            document.getElementsByClassName('time-dot')[0].innerText = transTime(lrcArray[index].seconds);
            audio.currentTime = lrcArray[index].seconds;
        })
        document.getElementsByClassName('song-main')[0].appendChild(li);

    }
}
var wheelClockCopy;
var i=0;
function setPosition()
{

        var index=getLrcIndex();
        var activeLi=document.getElementsByClassName('active')[0];
        var songMain=document.getElementsByClassName('song-main')[0];
        var songRoll=document.getElementsByClassName('song-roll')[0];
        if(activeLi)
        {
            activeLi.classList.remove('active');
            activeLi.style.fontSize=16+'px';
            activeLi.style.color='black';
            activeLi.style.fontWeight='normal';

        }
        songMain.children[index].classList.add('active');
        activeLi=songMain.children[index];
        activeLi.style.fontSize=19+'px';
        activeLi.style.color='green';
        activeLi.style.fontWeight='bolder';
        songRoll.scrollTop=(activeLi.offsetTop+activeLi.clientHeight/2)-songRoll.clientHeight/2;




}
function  getLrcIndex()
{
    var time=document.getElementsByTagName('audio')[0].currentTime;
    for(let index=0;index<lrcArray.length;index++)
    {
        if(lrcArray[index].seconds>time)
        {
            return index-1;
        }
    }
}

var wheelClock;

function stopWheel()
{

        document.getElementsByClassName('split-border')[0].style.display='none';
        audio.addEventListener("timeupdate", setPosition);
        console.log("事件绑定啦");
        if(clearTimeout(wheelClock))
        {

        };


}
document.getElementsByClassName('song-roll')[0].addEventListener('wheel',function ()
{

   moveWheelJudge();
});
function moveWheelJudge()
{
            document.getElementsByClassName('split-border')[0].style.display='block';
            if(wheelClock==null||!clearTimeout(wheelClock))
            {
                audio.removeEventListener("timeupdate", setPosition);
                wheelClock=setTimeout(stopWheel,3000);
                console.log("事件解绑啦");

            }

}



