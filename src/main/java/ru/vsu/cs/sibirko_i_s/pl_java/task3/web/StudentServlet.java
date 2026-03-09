package ru.vsu.cs.sibirko_i_s.pl_java.task3.web;

import ru.vsu.cs.sibirko_i_s.pl_java.task3.domain.model.Group;
import ru.vsu.cs.sibirko_i_s.pl_java.task3.domain.model.Journal;
import ru.vsu.cs.sibirko_i_s.pl_java.task3.domain.model.Student;
import ru.vsu.cs.sibirko_i_s.pl_java.task3.service.GroupService;
import ru.vsu.cs.sibirko_i_s.pl_java.task3.service.JournalService;
import ru.vsu.cs.sibirko_i_s.pl_java.task3.service.StudentService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/students")
public class StudentServlet extends HttpServlet {
    private StudentService studentService;
    private GroupService groupService;
    private JournalService journalService;

    @Override
    public void init() throws ServletException {
        // Достаем все 3 сервиса из глобального контекста
        studentService = (StudentService) getServletContext().getAttribute("studentService");
        groupService = (GroupService) getServletContext().getAttribute("groupService");
        journalService = (JournalService) getServletContext().getAttribute("journalService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            // Получаем ID группы из URL (например: /students?groupId=1)
            int groupId = Integer.parseInt(req.getParameter("groupId"));

            Group group = groupService.getGroupById(groupId);
            List<Student> students = studentService.getStudentsByGroupId(groupId);

            // Собираем журналы для этих студентов в словарь (Map), чтобы легко доставать их в JSP по ID студента
            Map<Integer, Journal> journals = new HashMap<>();
            for (Student student : students) {
                journals.put(student.getStudentId(), journalService.getJournalByStudentId(student.getStudentId()));
            }

            req.setAttribute("group", group);
            req.setAttribute("students", students);
            req.setAttribute("journals", journals);

            req.getRequestDispatcher("/WEB-INF/views/students.jsp").forward(req, resp);

        } catch (Exception e) {
            // Если кто-то передал неверный ID или группы нет - выкидываем обратно на список групп
            resp.sendRedirect(req.getContextPath() + "/groups");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");
        String groupIdStr = req.getParameter("groupId");

        try {
            if ("create".equals(action)) {
                int groupId = Integer.parseInt(groupIdStr);
                String firstName = req.getParameter("firstName");
                String lastName = req.getParameter("lastName");

                // Каскадное создание студента и его пустого журнала сработает автоматически!
                studentService.createStudent(groupId, firstName, lastName);

            } else if ("delete".equals(action)) {
                int studentId = Integer.parseInt(req.getParameter("studentId"));
                studentService.deleteStudent(studentId);

            } else if ("update_task".equals(action)) {
                int studentId = Integer.parseInt(req.getParameter("studentId"));
                int taskNumber = Integer.parseInt(req.getParameter("taskNumber"));
                boolean status = Boolean.parseBoolean(req.getParameter("status"));
                journalService.updateTaskStatus(studentId, taskNumber, status);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Перенаправляем обратно на страницу этой же группы
        resp.sendRedirect(req.getContextPath() + "/students?groupId=" + groupIdStr);
    }
}
