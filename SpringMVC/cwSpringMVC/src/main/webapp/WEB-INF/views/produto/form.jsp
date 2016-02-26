<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page isELIgnored="false"%>	
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Cadastro de produto</title>
</head>
<body>

	<form:form action='${pageContext.request.contextPath}/produtos' method="post" commandName="produto">

		<p>
			<label>Título <form:errors path="titulo"/></label></br> <input type="text" name="titulo" />
		</p>

		<p>
			<label>Descrição <form:errors path="descricao"/></label></br> <input type="text" name="descricao" />
		</p>

		<p>
			<label>No. de páginas <form:errors path="paginas"/></label></br> <input type="text" name="paginas" />
		</p>

		<fieldset>
			<legend>Preços</legend>
			<c:forEach items="${tiposEntrega}" var="tipoEntrega"
				varStatus="status">
				<p>
					<label>${tipoEntrega}</label></br> 
					<input type="text"
						name="precos[${status.index }].valor" /> <input type="hidden"
						name="precos[${status.index }].tipoEntrega" value="${tipoEntrega}" />
				</p>
			</c:forEach>
		</fieldset>

		<input type="submit" text="Gravar" />
	</form:form>
</body>
</html>