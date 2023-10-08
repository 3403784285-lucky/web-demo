const fileAddress = './lrc/yongyuanfeixingmoshi.lrc';
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
        document.getElementsByTagName('audio')[0].ontimeupdate=moveWheelJudge;





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
        document.getElementsByClassName('song-main')[0].appendChild(li);

    }
}
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
       activeLi.style.fontSize=15+'px';
       activeLi.style.color='black';
       activeLi.style.fontWeight='normal';

   }
    songMain.children[index].classList.add('active');
    activeLi=songMain.children[index];
    activeLi.style.fontSize=17+'px';
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
var moveWheel1=true;
var moveWheel2=false;
var moveWheel3=false;
var wheelClock;

function stopWheel()
{

        document.getElementsByClassName('split-border')[0].style.display='none';
        moveWheel3=false;
        moveWheel2=false;


}
document.getElementsByClassName('song-roll')[0].addEventListener('wheel',function ()
{
   moveWheel2=true;
})
function moveWheelJudge()
{
    if(moveWheel2==false)
    {
        setPosition();
    }
    else if(moveWheel2==true)
    {
        if (moveWheel3==false)
        {
            document.getElementsByClassName('split-border')[0].style.display='block';
            moveWheel3=true;
            setTimeout(stopWheel,3000);

        }
        else
        {
            clearTimeout(wheelClock);
            wheelClock=setTimeout(stopWheel,3000);
        }

    }
}
