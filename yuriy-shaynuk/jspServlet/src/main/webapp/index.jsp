<%@page contentType="text/html; charset=UTF-8"
        pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%

%>
<html>
<body>
<input type="button" value="Cities" onclick="submit()">

<script>
    async function submit() {
        let url = 'http://localhost:8080/getList';
        let response = await fetch(url);

        if (response.ok) {
            let commits = await response.json(); // читаем ответ в формате JSON
            //let data = JSON.parse(commits);
            //let cities = JSON.parse(response);
            alert(commits.value[0].name);
        }else{
            alert("Error HTTP: " + response.status);
        }
    }
</script>

<h2>Hello World!</h2>
</body>
</html>
