<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
<%@ page isELIgnored="false"%>	
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Lista de produtos</title>
</head>
<body>

	<h1>Lista de produtos</h1>
    <table>
        <tr>
            <td>Título</td>
            <td>Descrição</td>
            <td>Página</td>
        </tr>

        <c:forEach items="${produtos}" var="produto">
            <tr>
                <td>${produto.titulo}</td>
                <td>${produto.descricao}</td>
                <td>${produto.paginas}</td>
            </tr> 
        </c:forEach>
    </table>

</body>
</html>