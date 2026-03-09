<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Студенты группы ${group.name}</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 40px; }
        table { border-collapse: collapse; width: 80%; margin-top: 20px; }
        th, td { border: 1px solid #dddddd; text-align: left; padding: 8px; }
        th { background-color: #f2f2f2; }
        .form-container { margin-bottom: 20px; padding: 15px; border: 1px solid #ccc; width: 80%; box-sizing: border-box; }
        .task-form { display: inline; margin: 0; }
        .task-btn { padding: 5px 10px; cursor: pointer; border-radius: 4px; font-weight: bold; border: 1px solid;}
        .done { background-color: #d4edda; color: #155724; border-color: #c3e6cb; }
        .not-done { background-color: #f8d7da; color: #721c24; border-color: #f5c6cb; }
        .back-link { display: inline-block; margin-bottom: 20px; text-decoration: none; color: #0056b3; }
    </style>
</head>
<body>
    <h2>Группа: ${group.name}</h2>
    <a href="${pageContext.request.contextPath}/groups" class="back-link">← Вернуться к списку групп</a>

    <div class="form-container">
        <h3>Добавить студента</h3>
        <form action="${pageContext.request.contextPath}/students" method="post">
            <input type="hidden" name="action" value="create">
            <input type="hidden" name="groupId" value="${group.groupId}">
            <input type="text" name="firstName" required placeholder="Имя">
            <input type="text" name="lastName" required placeholder="Фамилия">
            <button type="submit">Добавить</button>
        </form>
    </div>

    <h3>Журнал группы</h3>
    <c:choose>
        <c:when test="${empty students}">
            <p>В этой группе пока нет студентов.</p>
        </c:when>
        <c:otherwise>
            <table>
                <tr>
                    <th>ID</th>
                    <th>Имя Фамилия</th>
                    <th>Задача 1</th>
                    <th>Задача 2</th>
                    <th>Задача 3</th>
                    <th>Действия</th>
                </tr>
                <c:forEach var="student" items="${students}">
                    <c:set var="journal" value="${journals[student.studentId]}" />
                    <tr>
                        <td>${student.studentId}</td>
                        <td>${student.studentFirstName} ${student.studentLastName}</td>

                        <td>
                            <form class="task-form" action="${pageContext.request.contextPath}/students" method="post">
                                <input type="hidden" name="action" value="update_task">
                                <input type="hidden" name="groupId" value="${group.groupId}">
                                <input type="hidden" name="studentId" value="${student.studentId}">
                                <input type="hidden" name="taskNumber" value="1">
                                <input type="hidden" name="status" value="${!journal.task1Completed}">
                                <button type="submit" class="task-btn ${journal.task1Completed ? 'done' : 'not-done'}">
                                    ${journal.task1Completed ? 'Сдано' : 'Не сдано'}
                                </button>
                            </form>
                        </td>

                        <td>
                            <form class="task-form" action="${pageContext.request.contextPath}/students" method="post">
                                <input type="hidden" name="action" value="update_task">
                                <input type="hidden" name="groupId" value="${group.groupId}">
                                <input type="hidden" name="studentId" value="${student.studentId}">
                                <input type="hidden" name="taskNumber" value="2">
                                <input type="hidden" name="status" value="${!journal.task2Completed}">
                                <button type="submit" class="task-btn ${journal.task2Completed ? 'done' : 'not-done'}">
                                    ${journal.task2Completed ? 'Сдано' : 'Не сдано'}
                                </button>
                            </form>
                        </td>

                        <td>
                            <form class="task-form" action="${pageContext.request.contextPath}/students" method="post">
                                <input type="hidden" name="action" value="update_task">
                                <input type="hidden" name="groupId" value="${group.groupId}">
                                <input type="hidden" name="studentId" value="${student.studentId}">
                                <input type="hidden" name="taskNumber" value="3">
                                <input type="hidden" name="status" value="${!journal.task3Completed}">
                                <button type="submit" class="task-btn ${journal.task3Completed ? 'done' : 'not-done'}">
                                    ${journal.task3Completed ? 'Сдано' : 'Не сдано'}
                                </button>
                            </form>
                        </td>

                        <td>
                            <form action="${pageContext.request.contextPath}/students" method="post" style="display:inline;">
                                <input type="hidden" name="action" value="delete">
                                <input type="hidden" name="groupId" value="${group.groupId}">
                                <input type="hidden" name="studentId" value="${student.studentId}">
                                <button type="submit" onclick="return confirm('Удалить студента ${student.studentFirstName}?');">Удалить</button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
            </table>
        </c:otherwise>
    </c:choose>
</body>
</html>