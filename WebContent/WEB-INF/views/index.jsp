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
					<p>飞影5元包月套餐，看视频免流量费，不占用原有套餐流量。</p>
					<p>
						安徽联通用户请发送DZFY（5元/月）到1065583398订购飞影业务；退订飞影业务发送TDFY到1065583398。</p>
					<p>
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