package ru.vsu.cs.sibirko_i_s.pl_java.task3.web;

import ru.vsu.cs.sibirko_i_s.pl_java.task3.domain.model.Group;
import ru.vsu.cs.sibirko_i_s.pl_java.task3.service.GroupService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/groups") // Пустая строка означает корень сайта (главная страница)
public class GroupServlet extends HttpServlet {

    private GroupService groupService;

    @Override
    public void init() throws ServletException {
        // Достаем наш сервис из глобальной памяти, куда его положил Listener
        this.groupService = (GroupService) getServletContext().getAttribute("groupService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 1. Получаем данные из БД (через сервис)
        List<Group> groups = groupService.getAllGroups();

        // 2. Кладем данные в объект запроса, чтобы JSP могла их прочитать
        req.setAttribute("groups", groups);

        // 3. Перенаправляем пользователя на JSP-страницу для отрисовки HTML
        req.getRequestDispatcher("/WEB-INF/views/groups.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Обязательно устанавливаем кодировку, чтобы кириллица из формы не превратилась в кракозябры!
        req.setCharacterEncoding("UTF-8");

        // Получаем скрытое поле, чтобы понять, какую именно форму отправил пользователь
        String action = req.getParameter("action");

        try {
            if ("create".equals(action)) {
                String groupName = req.getParameter("groupName");
                groupService.createGroup(groupName);
            } else if ("delete".equals(action)) {
                int groupId = Integer.parseInt(req.getParameter("groupId"));
                groupService.deleteGroup(groupId);
            }
        } catch (Exception e) {
            // В реальном проекте тут нужно передавать ошибку на страницу, но пока просто выведем в консоль
            e.printStackTrace();
        }

        // Тот самый паттерн PRG: перенаправляем браузер обратно на GET-запрос списка групп
        resp.sendRedirect(req.getContextPath() + "/groups");
    }
}