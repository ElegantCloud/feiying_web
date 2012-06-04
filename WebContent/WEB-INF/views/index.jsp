<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link href="/feiying/css/css.css" type="text/css" rel="stylesheet" />
<title>安徽联通飞影</title>
</head>
<body>
	<div id="wrap">
		<div class="master">
			<div class="logo">
				<img src="/feiying/images/big_logo1.png" />
			</div>
			<div class="phone">
				<img src="/feiying/images/pic.jpg" />
			</div>
			<div class="container">
				<div class="nr">
					<p>
					飞影是安徽联通为其3G手机用户精心打造的免费网络视频播放软件,该软件将目前互联网上最新最	热的视频聚合在一个统一界面中,避免用户反复访问不同视频网站带来的繁琐.该软件通过细致贴心的界面设计、聚合了丰富多彩的视频节目、高清流畅的播放服务,为安徽联通的3G用户带来不一样的在线视频观看体验.
					</p>
					<p>详情请电话咨询（输入您的手机号码并点击呼叫按钮）</p>
					<p>
						<input id="iptPhoneNumber" type="text" value=""/>
						<button id="btnPhoneCall" title=""></button>
					</p>
				</div>
				<div class="xiazai">
					<ul>
						<li><a href="/feiying/appget/android"><img
								src="/feiying/images/download1.png" />
						</a>
						</li>
						<li><a href="#"><img src="/feiying/images/download2.png" />
						</a>
						</li>
					</ul>
				</div>
			</div>
			<div class="logo2">
				<img src="/feiying/images/big_logo2.png" />
			</div>
		</div>
		<div class="footer">2012 版权所有,安徽联通</div>
	</div>
</body>
<script type="text/javascript" src="http://code.jquery.com/jquery-1.7.2.min.js"></script>
<script type="text/javascript">
$(function(){
	$("#btnPhoneCall").click(function(){
		var phoneNumber = $('#iptPhoneNumber').val();
		if ($.isNumeric(phoneNumber)){
			$.post('http://fy1.richitec.com/ccs/webcall/call', 
				{
					caller:'sip:018652970720@richitec.com',
					callee:'sip:0'+phoneNumber+'@richitec.com'
				}, 
				function(data){alert(data)});
		}
	});
});
</script>
</html>