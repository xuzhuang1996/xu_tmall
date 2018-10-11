// JavaScript Document
//以下可以改为你的图标
var head="display:''"
img1=new Image()
img1.src="images/d_left_1.gif"
img2=new Image()
img2.src="images/d_left_2.gif"

/*function change(){
   if(!document.all)
      return
   if (event.srcElement.id=="foldheader") {
      var srcIndex = event.srcElement.sourceIndex
      var nested = document.all[srcIndex+1]
      if (nested.style.display=="none") {
         nested.style.display=''
         event.srcElement.style.listStyleImage="url(d_left_1.gif)"
      }
      else {
         nested.style.display="none"
         event.srcElement.style.listStyleImage="url(d_left_2.gif)"
      }
   }
}*/

//document.onclick=change

function MM_openBrWindow(theURL,winName,features) { //v2.0
  window.open(theURL,winName,features);
}