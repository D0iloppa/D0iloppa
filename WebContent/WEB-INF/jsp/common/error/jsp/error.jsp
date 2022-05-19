<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isErrorPage="true"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>     
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>HPS ERROR PAGE</title>
</head>
<body>

<h1>//ERROR//</h1>

<c:out value="${requestScope['javax.servlet.error.message']}"/>
<c:out value="${requestScope['javax.servlet.error.exception_type']}"/>
<c:out value="${requestScope['javax.servlet.error.message']}"/>
<c:out value="${requestScope['javax.servlet.error.exception']}"/>
<c:out value="${requestScope['javax.servlet.error.request_uri']}"/>


</body>
</html>