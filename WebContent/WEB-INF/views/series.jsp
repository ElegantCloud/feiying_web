<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.List"%>
<%@ page import="com.feixun.impl.Series"%>
<%@ page import="com.feixun.utity.Utity"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="apple-mobile-web-app-capable" content="yes" />
<meta name="apple-mobile-web-app-status-bar-style" content="Default" />
<meta name="viewport"
	content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no" />
<link href="/feixun/css/share.css" type="text/css" rel="stylesheet" />
<title>安徽联通飞影</title>
</head>
<%
	Map<String, Object> bean = (Map<String, Object>)request.getAttribute("bean");
	List<Map<String, Object>> episodeList = (List<Map<String, Object>>)request.getAttribute("episodeList");
%>
<body>
<jsp:include page="_header.jsp"></jsp:include>
<div class="content">
   	<h1 class="title"><%=Utity.htmlCheck((String) bean.get("title"))%></h1>
   	<div class="detail">
		<img class="image left" src="<%=bean.get("image_url") != null ? bean.get("image_url") : ""%>" />	
        <div class="info">
			<div>
        		<div class="left">导演：</div>
        		<div><%=Utity.htmlCheck((String) bean.get("director"))%></div>
        	</div>
        	<div>
        		<div class="left">主演：</div>
        		<div><%=Utity.htmlCheck((String) bean.get("actor"))%></div>
        	</div>
        	<div>
        		<div class="left">年份：</div>
        		<div><%=Utity.htmlCheck((String) bean.get("release_date"))%></div>
        	</div>  
			<div>
        		<div class="left">地区：</div>
        		<div><%=Utity.htmlCheck((String) bean.get("origin"))%></div>
        	</div> 
        </div>
	</div>
</div>
 <ul class="episode">
  	<%
	for (int i = 0; i < episodeList.size(); i++) {
%>
<%
	Map<String, Object> episodeBean = episodeList.get(i);
%>
<% if(i % 4 == 0) { %>
<li>
<% } %>
<a href=<%=episodeBean.get("video_url")%>> 第<%=i+1%>集 </a>
<% if(i % 4 == 3) { %>
</li>
<% } %>
<%
	}
%>

  </ul>
<jsp:include page="_footer.jsp"></jsp:include>
</body>
</html>