<%@ page pageEncoding="UTF-8"%>
<%@ include file="./head.jsp"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
{
	"statusCode":"200",
	"message":"${ActionMessage}",
	"confirmMsg":"${confirmMsg}",
	"navTabId":"${navTabId}",
	"rel":"dlg_page",
	"callbackType":"${callbackType}",<c:if test="${not empty ForwardUrlKey}" var="urltest">
	"forwardUrl":"${ctx}/${ForwardUrlKey}"</c:if><c:if test="${!urltest}">
	"forwardUrl":""</c:if>
	
}
