
<%@page contentType="text/html;charset=UTF-8" language="java" %>
<%@page import="java.time.LocalDateTime" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js"></script><%
  LocalDateTime pageReloaded = LocalDateTime.now();
  request.setAttribute("reloadTime",pageReloaded);
%>
  <script>
    function preapareAndPost() {

      var srvName = location.href;
      var redirection = document.getElementsByName("rlink").valueOf()[0].value;
      var ID = document.getElementsByName("ID").valueOf()[0].value;
      var name = document.getElementsByName("NAME").valueOf()[0].value;
      var action = "GET";
      document.getElementsByName("action").forEach((item)=>{
        if(item.checked){
          action = item.value;
        }
      });

      if(parseInt(ID) != NaN) {
        var rLink = srvName+redirection+ID+"?name="+name;
        if (action == "GET") {
          fetch(rLink,{mode: 'cors'}).then((response)=>{
            return response.text()
          }).then((text)=>{
            document.getElementById("responseBody").innerHTML=text;
          }).catch((err)=>{
            console.log(err);
          })
        }else if(action == "POST"){
          var json;
          fetch(rLink, {
            method: 'post',
            headers: {
              "Content-type": "application/x-www-form-urlencoded; charset=UTF-8"
            },
            body: 'name='+name
          }).then(function(response) {
            return response.text();
          }).then(function(text) {
                    console.log('Request successful', text);
                    document.getElementById("responseBody").innerHTML=text;
                  })
                  .catch(function(error) {
                    console.log('Request failed', error)
                  });
        }else if (action == "DELETE"){
          fetch(rLink, {
            method: 'delete',
            headers: {
              "Content-type": "application/x-www-form-urlencoded; charset=UTF-8"
            },
            body: ''
          }).then(function (response) {
            return response.text();
          }).then(function (text) {
                    document.getElementById("responseBody").innerHTML=text;
                  })
                  .catch(function (error) {
                    console.log('Request failed', error);
                  });
        }
      }
    }
  </script>
<html>
  <head>
    <title>ua.ithillel HOME WORK #</title>
  </head>
  <body>
  <div class="container">
  <div id="header-hll">
    <div class="container p-3 my-3 bg-primary text-white"><c:out value="${reloadTime}"/></div>
  Object:<select size="1" name="rlink">
    <option disabled>Choose part...</option>
    <option value="cities/">cities</option>
    <option value="regions/">regions</option>
    <option value="countries/">countries</option>
  </select>
  </div>
  <div id="body-hll">

    ID:<input name="ID" size="16">
    NAME:<input name="NAME" size="32">
    <div class="form-check">
      <label class="form-check-label">
        <input type="radio" class="form-check-input" name="action" value="GET">find by ID
      </label>
    </div>
    <div class="form-check">
      <label class="form-check-label">
        <input type="radio" class="form-check-input" name="action" value="POST">create/update
      </label>
    </div>
    <div class="form-check">
      <label class="form-check-label">
        <input type="radio" class="form-check-input" name="action" value="DELETE">delet by ID
      </label>
    </div>
    <button class="left-arrow-button" name="doAction" onclick="preapareAndPost()">Do somethig marvelous</button>

  </div>
  <div id="responseBody"></div>
  </div>
  </body>
</html>
