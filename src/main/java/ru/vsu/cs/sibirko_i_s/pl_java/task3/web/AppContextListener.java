package ru.vsu.cs.sibirko_i_s.pl_java.task3.web;

import ru.vsu.cs.sibirko_i_s.pl_java.task3.domain.repository.GroupRepository;
import ru.vsu.cs.sibirko_i_s.pl_java.task3.domain.repository.JournalRepository;
import ru.vsu.cs.sibirko_i_s.pl_java.task3.domain.repository.StudentRepository;
import ru.vsu.cs.sibirko_i_s.pl_java.task3.infrastructure.repository.db.GroupRepositoryImpl;
import ru.vsu.cs.sibirko_i_s.pl_java.task3.infrastructure.repository.db.JournalRepositoryImpl;
import ru.vsu.cs.sibirko_i_s.pl_java.task3.infrastructure.repository.db.StudentRepositoryImpl;
import ru.vsu.cs.sibirko_i_s.pl_java.task3.service.GroupService;
import ru.vsu.cs.sibirko_i_s.pl_java.task3.service.JournalService;
import ru.vsu.cs.sibirko_i_s.pl_java.task3.service.StudentService;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@WebListener
public class AppContextListener implements ServletContextListener {

    private Connection connection;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            Class.forName("org.postgresql.Driver");

            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/java_db", "postgres", "Wewillbecomehokage196.");

            GroupRepository groupRepository = new GroupRepositoryImpl(connection);
            StudentRepository studentRepository = new StudentRepositoryImpl(connection);
            JournalRepository journalRepository = new JournalRepositoryImpl(connection);

            JournalService journalService = new JournalService(journalRepository);
            StudentService studentService = new StudentService(studentRepository, groupRepository, journalService);
            GroupService groupService = new GroupService(groupRepository, studentService);

            ServletContext context = sce.getServletContext();
            context.setAttribute("groupService", groupService);
            context.setAttribute("studentService", studentService);
            context.setAttribute("journalService", journalService);

            System.out.println("Приложение успешно запущено, сервисы инициализированы!");

        } catch (Exception e) {
            throw new RuntimeException("Ошибка при инициализации приложения: " + e.getMessage(), e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Соединение с БД успешно закрыто.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}