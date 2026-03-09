<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Список групп</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 40px; }
        table { border-collapse: collapse; width: 60%; margin-top: 20px; }
        th, td { border: 1px solid #dddddd; text-align: left; padding: 8px; }
        th { background-color: #f2f2f2; }
        .form-container { margin-bottom: 20px; padding: 15px; border: 1px solid #ccc; width: 60%; box-sizing: border-box; }
    </style>
</head>
<body>
    <h2>Система учета задач</h2>

    <div class="form-container">
        <h3>Добавить новую группу</h3>
        <form action="${pageContext.request.contextPath}/groups" method="post">
            <input type="hidden" name="action" value="create">
            <input type="text" name="groupName" required placeholder="Введите название группы">
            <button type="submit">Создать</button>
        </form>
    </div>

    <h3>Список академических групп</h3>
    <c:choose>
        <c:when test="${empty groups}">
            <p>В системе пока нет ни одной группы.</p>
        </c:when>
        <c:otherwise>
            <table>
                <tr>
                    <th>ID</th>
                    <th>Название группы</th>
                    <th>Действия</th>
                </tr>
                <c:forEach var="group" items="${groups}">
                    <tr>
                        <td>${group.groupId}</td>
                        <td>${group.name}</td>
                        <td>
                            <a href="${pageContext.request.contextPath}/students?groupId=${group.groupId}">Студенты</a>

                            <form action="${pageContext.request.contextPath}/groups" method="post" style="display:inline; margin-left: 15px;">
                                <input type="hidden" name="action" value="delete">
                                <input type="hidden" name="groupId" value="${group.groupId}">
                                <button type="submit" onclick="return confirm('Вы уверены, что хотите удалить группу и ВСЕХ её студентов?');">Удалить</button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
            </table>
        </c:otherwise>
    </c:choose>
</body>
</html>