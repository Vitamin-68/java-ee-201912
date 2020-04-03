<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>Create an account</title>
    <link href="${contextPath}/resources/css/bootstrap.min.css" rel="stylesheet">
    <style><%@include file="resources/css/style.css"%></style>
</head>
<body>
<div class="container">
    <c:if test="${pageContext.request.userPrincipal.name != null}">
        <form id="logoutForm" method="POST" action="${contextPath}/logout">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        </form>

        <h2>Welcome ${pageContext.request.userPrincipal.name} | <a onclick="document.forms['logoutForm'].submit()">Logout</a></h2>
    </c:if>
</div>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
<script src="${contextPath}/resources/js/bootstrap.min.js"></script>

<script>
    const TYPE_CITY = 1;
    const TYPE_REGION = 2;

    async function submit(listType) {
        removeTable();
        let url = 'http://localhost:8080/getList?type='+listType;
        let response = await fetch(url);

        if (response.ok) {
            let responseList = await response.json();
            document.getElementById("container").appendChild(buildTable(responseList, listType));
        }else{
            alert("Error HTTP: " + response.status);
        }
    }

    function removeTable() {
        let elem = document.getElementById('tmpTable');
        if (elem != null && typeof elem != 'undefined')
        {
            elem.parentNode.removeChild(elem);
        }
    }

    function buildTable(data, listType) {
        let table = document.createElement("table");
        table.id = "tmpTable";
        table.className="gridtable";
        let thead = document.createElement("thead");
        let tbody = document.createElement("tbody");
        let headRow = document.createElement("tr");
        let tableTitle;
        if(listType === TYPE_CITY){
            tableTitle =  ["country_id","region_id","name", "city_id"];
        }else if(listType === TYPE_REGION){
            tableTitle =  ["country_id","city_id","name", "region_id"];
        }
        tableTitle.forEach(function(el) {
            let th=document.createElement("th");
            th.appendChild(document.createTextNode(el));
            headRow.appendChild(th);
        });

        thead.appendChild(headRow);
        table.appendChild(thead);
        data.forEach(function(el) {
            let tr = document.createElement("tr");
            for (let o in el) {
                let td = document.createElement("td");
                td.appendChild(document.createTextNode(el[o]))
                tr.appendChild(td);
            }
            tbody.appendChild(tr);
        });
        table.appendChild(tbody);
        return table;
    }
</script>

<input type="button" value="Cities" onclick="submit(TYPE_CITY)">
<input type="button" value="Regions" onclick="submit(TYPE_REGION)">

<div id="container"></div>
</body>
</html>