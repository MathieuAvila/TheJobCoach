function com_TheJobCoach_webapp_mainpage_MainPage(){var O='',vb='" for "gwt:onLoadErrorFn"',tb='" for "gwt:onPropertyErrorFn"',hb='"><\/script>',Y='#',Db='&',$b='.cache.html',$='/',Zb=':',nb='::',gc='<script defer="defer">com_TheJobCoach_webapp_mainpage_MainPage.onInjectionDone(\'com.TheJobCoach.webapp.mainpage.MainPage\')<\/script>',gb='<script id="',qb='=',Z='?',Lb='ActiveXObject',sb='Bad handler "',Mb='ChromeTab.ChromeFrame',fc='DOMContentLoaded',Yb="GWT module 'com.TheJobCoach.webapp.mainpage.MainPage' may need to be (re)compiled",ib='SCRIPT',Gb='Unexpected exception in locale detection, using default: ',Fb='_',Eb='__gwt_Locale',fb='__gwt_marker_com.TheJobCoach.webapp.mainpage.MainPage',jb='base',bb='baseUrl',S='begin',R='bootstrap',Kb='chromeframe',ab='clear.cache.gif',P='com.TheJobCoach.webapp.mainpage.MainPage',db='com.TheJobCoach.webapp.mainpage.MainPage.nocache.js',mb='com.TheJobCoach.webapp.mainpage.MainPage::',pb='content',Bb='default',X='end',Sb='gecko',Tb='gecko1_8',T='gwt.codesvr=',U='gwt.hosted=',V='gwt.hybrid',_b='gwt/clean/clean.css',ub='gwt:onLoadErrorFn',rb='gwt:onPropertyErrorFn',ob='gwt:property',ec='head',Wb='hosted.html?com_TheJobCoach_webapp_mainpage_MainPage',dc='href',Rb='ie6',Qb='ie8',Pb='ie9',wb='iframe',_='img',xb="javascript:''",ac='link',Vb='loadExternalRefs',Ab='locale',Cb='locale=',kb='meta',zb='moduleRequested',W='moduleStartup',Ob='msie',lb='name',Ib='opera',yb='position:absolute;width:0;height:0;border:none',bc='rel',Nb='safari',cb='script',Xb='selectingPermutation',Q='startup',cc='stylesheet',eb='undefined',Ub='unknown',Hb='user.agent',Jb='webkit';var l=window,m=document,n=l.__gwtStatsEvent?function(a){return l.__gwtStatsEvent(a)}:null,o=l.__gwtStatsSessionId?l.__gwtStatsSessionId:null,p,q,r,s=O,t={},u=[],v=[],w=[],x=0,y,z;n&&n({moduleName:P,sessionId:o,subSystem:Q,evtGroup:R,millis:(new Date).getTime(),type:S});if(!l.__gwt_stylesLoaded){l.__gwt_stylesLoaded={}}if(!l.__gwt_scriptsLoaded){l.__gwt_scriptsLoaded={}}function A(){var b=false;try{var c=l.location.search;return (c.indexOf(T)!=-1||(c.indexOf(U)!=-1||l.external&&l.external.gwtOnLoad))&&c.indexOf(V)==-1}catch(a){}A=function(){return b};return b}
function B(){if(p&&q){var b=m.getElementById(P);var c=b.contentWindow;if(A()){c.__gwt_getProperty=function(a){return G(a)}}com_TheJobCoach_webapp_mainpage_MainPage=null;c.gwtOnLoad(y,P,s,x);n&&n({moduleName:P,sessionId:o,subSystem:Q,evtGroup:W,millis:(new Date).getTime(),type:X})}}
function C(){function e(a){var b=a.lastIndexOf(Y);if(b==-1){b=a.length}var c=a.indexOf(Z);if(c==-1){c=a.length}var d=a.lastIndexOf($,Math.min(c,b));return d>=0?a.substring(0,d+1):O}
function f(a){if(a.match(/^\w+:\/\//)){}else{var b=m.createElement(_);b.src=a+ab;a=e(b.src)}return a}
function g(){var a=F(bb);if(a!=null){return a}return O}
function h(){var a=m.getElementsByTagName(cb);for(var b=0;b<a.length;++b){if(a[b].src.indexOf(db)!=-1){return e(a[b].src)}}return O}
function i(){var a;if(typeof isBodyLoaded==eb||!isBodyLoaded()){var b=fb;var c;m.write(gb+b+hb);c=m.getElementById(b);a=c&&c.previousSibling;while(a&&a.tagName!=ib){a=a.previousSibling}if(c){c.parentNode.removeChild(c)}if(a&&a.src){return e(a.src)}}return O}
function j(){var a=m.getElementsByTagName(jb);if(a.length>0){return a[a.length-1].href}return O}
var k=g();if(k==O){k=h()}if(k==O){k=i()}if(k==O){k=j()}if(k==O){k=e(m.location.href)}k=f(k);s=k;return k}
function D(){var b=document.getElementsByTagName(kb);for(var c=0,d=b.length;c<d;++c){var e=b[c],f=e.getAttribute(lb),g;if(f){f=f.replace(mb,O);if(f.indexOf(nb)>=0){continue}if(f==ob){g=e.getAttribute(pb);if(g){var h,i=g.indexOf(qb);if(i>=0){f=g.substring(0,i);h=g.substring(i+1)}else{f=g;h=O}t[f]=h}}else if(f==rb){g=e.getAttribute(pb);if(g){try{z=eval(g)}catch(a){alert(sb+g+tb)}}}else if(f==ub){g=e.getAttribute(pb);if(g){try{y=eval(g)}catch(a){alert(sb+g+vb)}}}}}}
function E(a,b){return b in u[a]}
function F(a){var b=t[a];return b==null?null:b}
function G(a){var b=v[a](),c=u[a];if(b in c){return b}var d=[];for(var e in c){d[c[e]]=e}if(z){z(a,d,b)}throw null}
var H;function I(){if(!H){H=true;var a=m.createElement(wb);a.src=xb;a.id=P;a.style.cssText=yb;a.tabIndex=-1;m.body.appendChild(a);n&&n({moduleName:P,sessionId:o,subSystem:Q,evtGroup:W,millis:(new Date).getTime(),type:zb});a.contentWindow.location.replace(s+K)}}
v[Ab]=function(){var b=null;var c=Bb;try{if(!b){var d=location.search;var e=d.indexOf(Cb);if(e>=0){var f=d.substring(e+7);var g=d.indexOf(Db,e);if(g<0){g=d.length}b=d.substring(e+7,g)}}if(!b){b=F(Ab)}if(!b){b=l[Eb]}if(b){c=b}while(b&&!E(Ab,b)){var h=b.lastIndexOf(Fb);if(h<0){b=null;break}b=b.substring(0,h)}}catch(a){alert(Gb+a)}l[Eb]=c;return b||Bb};u[Ab]={EN:0,FR:1,'default':2};v[Hb]=function(){var c=navigator.userAgent.toLowerCase();var d=function(a){return parseInt(a[1])*1000+parseInt(a[2])};if(function(){return c.indexOf(Ib)!=-1}())return Ib;if(function(){return c.indexOf(Jb)!=-1||function(){if(c.indexOf(Kb)!=-1){return true}if(typeof window[Lb]!=eb){try{var b=new ActiveXObject(Mb);if(b){b.registerBhoIfNeeded();return true}}catch(a){}}return false}()}())return Nb;if(function(){return c.indexOf(Ob)!=-1&&m.documentMode>=9}())return Pb;if(function(){return c.indexOf(Ob)!=-1&&m.documentMode>=8}())return Qb;if(function(){var a=/msie ([0-9]+)\.([0-9]+)/.exec(c);if(a&&a.length==3)return d(a)>=6000}())return Rb;if(function(){return c.indexOf(Sb)!=-1}())return Tb;return Ub};u[Hb]={gecko1_8:0,ie6:1,ie8:2,ie9:3,opera:4,safari:5};com_TheJobCoach_webapp_mainpage_MainPage.onScriptLoad=function(){if(H){q=true;B()}};com_TheJobCoach_webapp_mainpage_MainPage.onInjectionDone=function(){p=true;n&&n({moduleName:P,sessionId:o,subSystem:Q,evtGroup:Vb,millis:(new Date).getTime(),type:X});B()};D();C();var J;var K;if(A()){if(l.external&&(l.external.initModule&&l.external.initModule(P))){l.location.reload();return}K=Wb;J=O}n&&n({moduleName:P,sessionId:o,subSystem:Q,evtGroup:R,millis:(new Date).getTime(),type:Xb});if(!A()){try{alert(Yb);return;var L=J.indexOf(Zb);if(L!=-1){x=Number(J.substring(L+1));J=J.substring(0,L)}K=J+$b}catch(a){return}}var M;function N(){if(!r){r=true;if(!__gwt_stylesLoaded[_b]){var a=m.createElement(ac);__gwt_stylesLoaded[_b]=a;a.setAttribute(bc,cc);a.setAttribute(dc,s+_b);m.getElementsByTagName(ec)[0].appendChild(a)}B();if(m.removeEventListener){m.removeEventListener(fc,N,false)}if(M){clearInterval(M)}}}
if(m.addEventListener){m.addEventListener(fc,function(){I();N()},false)}var M=setInterval(function(){if(/loaded|complete/.test(m.readyState)){I();N()}},50);n&&n({moduleName:P,sessionId:o,subSystem:Q,evtGroup:R,millis:(new Date).getTime(),type:X});n&&n({moduleName:P,sessionId:o,subSystem:Q,evtGroup:Vb,millis:(new Date).getTime(),type:S});m.write(gc)}
com_TheJobCoach_webapp_mainpage_MainPage();