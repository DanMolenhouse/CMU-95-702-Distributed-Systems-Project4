<%--
  Created by IntelliJ IDEA.
  User: danmolenhouse
  Date: 4/9/22
  Time: 11:10 PM
  To change this template use File | Settings | File Templates.
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Project 4 Dashboard</title>
</head>
<style>

    <%--Table creation using: https://stackoverflow.com/questions/27728399/how-to-make-nice-table-in-jsp --%>
    <%-- More table formatting: https://stackoverflow.com/questions/32985047/create-table-dynamically-and-get-back-their-values-in-jsp-page --%>
    <%-- More table formatting: https://www.w3schools.com/tags/tag_th.asp --%>
    <%--Copied some style/layout design frommy previous Web App Development final project as well--%>
    table#analysis th {
        background-color: black;
        color: whitesmoke;
    }
    table#log th {
             background-color: black;
             color: whitesmoke;
    }

    <%--The following adds a table grid --%>
    table, th, td {
        border: 1px solid black;
        border-collapse: collapse;
    }
</style>
<body>
<h1>Project 4 Dashboard - TV Show Search Database - dmolenho</h1>
<table style="width:100%" id="analysis">
    <caption><b>TV Show Search Analysis</b></caption>
    <br>
    <tr>
        <th>Analysis</th>
        <th>Result</th>
    </tr>
    <tr>
        <td>Most recent date of access:</td>
        <td><%= request.getAttribute("date")%>
        </td>
    </tr>
    <tr>
        <td>Most recent search:</td>
        <td><%= request.getAttribute("recentSearch")%>
        </td>
    </tr>
    <tr>
        <td>All search terms:</td>
        <td><%= request.getAttribute("search")%>
        </td>
    </tr>
</table>
<br>

<table style="width:100%" id="log">
    <br>
    <caption><b>Search Log</b><br></caption>
    <tr>
        <th>Device Type</th>
        <th>Browser</th>
        <th>Search Term</th>
        <th>Access Date</th>
        <th>Number of results</th>
    </tr>
    <%= request.getAttribute("logs")%>
</table>
</body>
</html>
