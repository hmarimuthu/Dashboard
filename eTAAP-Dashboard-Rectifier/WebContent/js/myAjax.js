function myAjax(url, mtd, retType, options)
{
this.url = url;
this.method = mtd.toUpperCase() || 'GET';
this.returnType = retType.toUpperCase() || 'XML';
this.options = options || {};
this.params = this.options.params || '' ;
this.params += "&dummy=" + new Date().getTime();
this.callBack = this.options.callBack || '';
this.onError = this.options.onError || '';
this.onTimeout = this.options.onTimeout || '';
this.header = this.options.header || new Array();
// this.userId = this.options.userid || null;
// this.password = this.options.password || null;
this.httpObj = null;
this.timeout = false;
this.timeoutStart = new Date().getTime();
this.timeoutConstant = 240 * 10000; // time in miliseconds
this.config();
this.createAjax();
//sessionMaxTime = sessionMaxTimeOutLimit;
};
myAjax.prototype.config = function (){
this.httpObj = this.httpRequest();
if (this.url.length == 0 )
{
alert('please send HTTP url for ajax request...')
return false;
}
if (this.returnType != 'XML' && this.returnType != 'TEXT')
{
this.returnType = 'XML';
}
};
myAjax.prototype.httpRequest = function (){
var httprequest = null;
if (window.XMLHttpRequest){ // if Mozilla, Safari etc
httprequest = new XMLHttpRequest();
}else if (window.ActiveXObject){ // if IE
try {
httprequest = new ActiveXObject("Msxml2.XMLHTTP");
}catch (e){
try{
httprequest = new ActiveXObject("Microsoft.XMLHTTP");
}catch (e){
Alert('Your browser does not support HTTP request...');
return false;
}
}
}
if (httprequest.overrideMimeType)
httprequest.overrideMimeType('text/xml')
return httprequest
};
myAjax.prototype.checkTimeout = function (){
var curTime = new Date().getTime();
if (( curTime - this.timeoutStart) > this.timeoutConstant){
this.timeout = true;
}
if (this.timeout){
if (this.onTimeout && this.onTimeout.length > 0 ){
eval(this.onTimeout+'(this.httpObj.readyState)')
}else {
alert('Ajax call time out.\nPlease try again or contact System Administrator.');
if(document.getElementById("interVeil"))
{
document.getElementById("interVeil").style.display = "none";
}
if(document.getElementById("searchbar"))
{
document.getElementById("searchbar").style.display = "none";
}
}
return true;
}
return false;
};
myAjax.prototype.createAjax = function (){
if (this.method == 'POST'){
this.httpObj.open(this.method, this.url, true);
// set default request header
this.httpObj.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
try
{
this.httpObj.setRequestHeader("Content-length", this.params.length+"\r\n"); // Fix for HTTP Error 413 Request entity too large
}
catch (error)
{}
this.httpObj.setRequestHeader("Connection", "close");
for (var x=0; x < this.header.length ;x++ ) {
if (this.header[x][0].toUpperCase() != "Content-Type".toUpperCase() && this.header[x][0].toUpperCase() != "Content-length".toUpperCase() && this.header[x][0].toUpperCase() != "Connection".toUpperCase() )
{
this.httpObj.setRequestHeader(this.header[x][0],this.header[x][1])
}
}
this.httpObj.send(this.params);
}
if (this.method == 'GET'){
this.url = this.url + (this.params.length > 0 ? '?'+this.params : '')
this.httpObj.open(this.method, this.url, true);
this.httpObj.send(null);
}
var owner = this;
this.httpObj.onreadystatechange = function(){
// owner.showProgress();
if (owner.checkTimeout()) {
owner.httpObj.abort();
owner.httpObj = null;
return false;
}
if (owner.httpObj.readyState === 4) { // done
var ajaxResponse = null;
// owner.hideProgress();
try{console.info("status : "+owner.httpObj.status);}catch(e){console = { info: function() {}}}
switch (owner.httpObj.status) {
case 200:
if (owner.httpObj.responseText.indexOf('ErrorOnSessionExpire') > -1 ) {
if (owner.onError || owner.onError > 0){
eval(owner.onError+ '(owner.httpObj.responseText)')
}else{
//owner.handleErrFullPage('<h1>Error:</h1></p><h1>'+owner.httpObj.responseText+'</h1>');
}
}
else {
if (owner.returnType == 'XML') {
ajaxResponse = owner.httpObj.responseXML;
}else{
ajaxResponse = owner.httpObj.responseText;
}
if (owner.callBack || owner.callBack > 0){
eval(owner.callBack + '(ajaxResponse)');
}
}
break;
case 404:
alert('Error: Not Found. The requested URL ' + owner.url + ' could not be found.\n'+owner.httpObj.statusText);
break;
default:
owner.handleErrFullPage(owner.httpObj.responseText);
ajaxResponse = "";
break;
}
}
}
};
myAjax.prototype.showProgress = function(){
var processAjax = document.getElementById('processAjax');
if (!processAjax)
{
processAjax = document.createElement('div');
processAjax.id = 'processAjax'
processAjax.className = 'processAjax'
processAjax.style.cssText = "position:absolute;left:0px;top:0px;width:100px;height:100px;background-color:#aaaaaa;z-index:99;filter:alpha(opacity=40);text-align:center;vertical-align:center;"
span = document.createElement('span');
span.innerHTML = '&nbsp;'
span.style.cssText = 'width:100%;height:100%;background:url(../images/myloader.gif) center center no-repeat;'
processAjax.appendChild(span);
document.body.appendChild(processAjax);
}
processAjax.style.display='block';
var ajaxPos = this.windowCenter({'width':processAjax.offsetWidth,'height':processAjax.offsetHeight})
processAjax.style.left = ajaxPos.x
processAjax.style.top = ajaxPos.y
};
myAjax.prototype.hideProgress = function(){
var ajaxProcess = document.getElementById('processAjax');
if (ajaxProcess){
ajaxProcess.style.display='none'
}
};
myAjax.prototype.getUrl = function(){
return this.url;
};
myAjax.prototype.getResponse = function(){
if (this.httpObj.readyState == 4 && this.httpObj.status == 200) {
if (this.returnType == 'XML') {
return this.httpObj.responseXML;
}else{
return this.httpObj.responseText;
}
}
return null;
};
myAjax.prototype.handleErrFullPage = function(strIn) {
var errorWin;
try {
// errorWin = window.open('', 'Error');
// errorWin.document.body.innerHTML = strIn;
alert('Error Occured in ajax call.\nPlease try again or contact System Administrator.');
if(document.getElementById("interVeil"))
{
document.getElementById("interVeil").style.display = "none";
}
if(document.getElementById("searchbar"))
{
document.getElementById("searchbar").style.display = "none";
}
}
catch(e) {
alert('An error occurred, but the error message cannot be displayed because of your browser\'s pop-up blocker.\n' +
'Please allow pop-ups from this Web site.');
}
};
myAjax.prototype.getPageScroll =function () {
if(!window.pageYOffset) {
//strict mode
if(!(document.documentElement.scrollTop == 0)) {
offsetY = document.documentElement.scrollTop;
offsetX = document.documentElement.scrollLeft;
}
//quirks mode
else{
offsetY = document.body.scrollTop;
offsetX = document.body.scrollLeft;
}
}
//w3c
else{
offsetX = window.pageXOffset;
offsetY = window.pageYOffset;
}
return { 'x': offsetX, 'y': offsetY };
};
//-----------------------------------------------------------------------------
myAjax.prototype.windowCenter= function () {
var hWnd = (arguments[0] != null) ? arguments[0] : {width:0,height:0};
var _x = 0;
var _y = 0;
_pgScroll = this.getPageScroll();
_winSize = this.getWindowSize();
_x = ((_winSize.width - hWnd.width)/2)+_pgScroll.x;
_y = ((_winSize.height - hWnd.height)/2)+_pgScroll.y;
return{x:_x,y:_y};
};
//-----------------------------------------------------------------------------
myAjax.prototype.getWindowSize= function (){
var w = 0;
var h = 0;
//IE
if(!window.innerWidth){
//strict mode
if(!(document.documentElement.clientWidth == 0)){
w = document.documentElement.clientWidth;
h = document.documentElement.clientHeight;
}
//quirks mode
else{
w = document.body.clientWidth;
h = document.body.clientHeight;
}
}
//w3c
else{
w = window.innerWidth;
h = window.innerHeight;
}
return {width:w,height:h};
}; 
