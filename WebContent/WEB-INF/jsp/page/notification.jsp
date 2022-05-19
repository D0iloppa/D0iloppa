<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<script src="${pageContext.request.contextPath}/resource/js/notification.js"></script>
<input type="hidden" id="NotiPrjId" value="${prj_id}">
<input type="hidden" id="getLastConPrj" value="${getLastConPrj}">
<input type="hidden" id="NotiPage" value="${page}">
<input type="hidden" id="NotiFolderId" value="${folderId}">
<input type="hidden" id="NotiPrjNm" value="${getLastConPrj.prj_nm}">
<input type="hidden" id="NotiFolderPath" value="${folder_path}">