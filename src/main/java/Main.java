import ru.vsu.cs.sibirko_i_s.pl_java.task3.domain.model.Group;
import ru.vsu.cs.sibirko_i_s.pl_java.task3.domain.model.Journal;
import ru.vsu.cs.sibirko_i_s.pl_java.task3.domain.model.Student;
import ru.vsu.cs.sibirko_i_s.pl_java.task3.domain.repository.GroupRepository;
import ru.vsu.cs.sibirko_i_s.pl_java.task3.domain.repository.JournalRepository;
import ru.vsu.cs.sibirko_i_s.pl_java.task3.domain.repository.StudentRepository;
import ru.vsu.cs.sibirko_i_s.pl_java.task3.infrastructure.repository.db.GroupRepositoryImpl;
import ru.vsu.cs.sibirko_i_s.pl_java.task3.infrastructure.repository.db.JournalRepositoryImpl;
import ru.vsu.cs.sibirko_i_s.pl_java.task3.infrastructure.repository.db.StudentRepositoryImpl;
import ru.vsu.cs.sibirko_i_s.pl_java.task3.service.GroupService;
import ru.vsu.cs.sibirko_i_s.pl_java.task3.service.JournalService;
import ru.vsu.cs.sibirko_i_s.pl_java.task3.service.StudentService;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {

    @FunctionalInterface
    public interface Command {
        void execute() throws Exception;
    }

    private static final String DB_URL = "jdbc:postgresql://localhost:5432/java_db";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "Wewillbecomehokage196.";

    public static void main(String[] args) {

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            System.out.println("Success! Connected to the database.");

            GroupRepository groupRepository = new GroupRepositoryImpl(connection);
            StudentRepository studentRepository = new StudentRepositoryImpl(connection);
            JournalRepository journalRepository = new JournalRepositoryImpl(connection);

            JournalService journalService = new JournalService(journalRepository);
            StudentService studentService = new StudentService(studentRepository, groupRepository, journalService);
            GroupService groupService = new GroupService(groupRepository, studentService);

            Scanner scanner = new Scanner(System.in);

            Map<String, Command> commands = new HashMap<>();

            commands.put("1", () -> {
                System.out.print("Enter the name of the new group: ");
                String name = scanner.nextLine();
                groupService.createGroup(name);
                System.out.println("Group created successfully.");
            });

            commands.put("2", () -> {
                System.out.print("Enter group ID to delete: ");
                int groupId = Integer.parseInt(scanner.nextLine());
                groupService.deleteGroup(groupId);
                System.out.println("Group and all its students deleted successfully.");
            });

            commands.put("3", () -> {
                System.out.print("Enter group ID: ");
                int groupId = Integer.parseInt(scanner.nextLine());
                System.out.print("Enter student's first name: ");
                String firstName = scanner.nextLine();
                System.out.print("Enter student's last name: ");
                String lastName = scanner.nextLine();

                studentService.createStudent(groupId, firstName, lastName);
                System.out.println("Student added and journal created successfully.");
            });

            commands.put("4", () -> {
                System.out.print("Enter student ID to delete: ");
                int studentId = Integer.parseInt(scanner.nextLine());
                studentService.deleteStudent(studentId);
                System.out.println("Student and their journal deleted successfully.");
            });

            commands.put("5", () -> {
                System.out.print("Enter student ID: ");
                int studentId = Integer.parseInt(scanner.nextLine());
                System.out.print("Enter task number (1, 2, or 3): ");
                int taskNumber = Integer.parseInt(scanner.nextLine());
                System.out.print("Is the task completed? (true/false): ");
                boolean status = Boolean.parseBoolean(scanner.nextLine());

                journalService.updateTaskStatus(studentId, taskNumber, status);
                System.out.println("Task status updated!");
            });

            commands.put("6", () -> showAllData(groupService, studentService, journalService));

            System.out.println("Welcome!");

            while (true) {
                printMenu();
                System.out.print("Choose an action: ");
                String choice = scanner.nextLine();

                if ("0".equals(choice)) {
                    System.out.println("Exiting program...");
                    break;
                }

                Command command = commands.get(choice);

                if (command != null) {
                    try {
                        command.execute();
                    } catch (NumberFormatException e) {
                        System.out.println("Error: Please enter a valid number.");
                    } catch (IllegalArgumentException e) {
                        System.out.println("Business logic error: " + e.getMessage());
                    } catch (Exception e) {
                        System.out.println("An unexpected error occurred: " + e.getMessage());
                    }
                } else {
                    System.out.println("Invalid command. Please try again.");
                }
                System.out.println("--------------------------------------------------");
            }

        } catch (SQLException e) {
            System.err.println("Database connection failed: " + e.getMessage());
        }
    }

    private static void printMenu() {
        System.out.println("\n--- MAIN MENU ---");
        System.out.println("1. Create group");
        System.out.println("2. Delete group (and all its students)");
        System.out.println("3. Add student to group");
        System.out.println("4. Delete student");
        System.out.println("5. Change student's task status");
        System.out.println("6. Show all data");
        System.out.println("0. Exit");
    }

    private static void showAllData(GroupService groupService, StudentService studentService, JournalService journalService) {
        List<Group> groups = groupService.getAllGroups();
        if (groups.isEmpty()) {
            System.out.println("No groups found in the system.");
            return;
        }

        for (Group group : groups) {
            System.out.println("\n[Group ID: " + group.getGroupId() + "] " + group.getName());
            List<Student> students = studentService.getStudentsByGroupId(group.getGroupId());

            if (students.isEmpty()) {
                System.out.println("  No students in this group yet.");
                continue;
            }

            for (Student student : students) {
                System.out.print("  - Student ID: " + student.getStudentId() + ", " +
                        student.getStudentFirstName() + " " + student.getStudentLastName());

                try {
                    Journal journal = journalService.getJournalByStudentId(student.getStudentId());
                    System.out.printf(" | Tasks: 1[%s], 2[%s], 3[%s]\n",
                            journal.isTask1Completed() ? "X" : " ",
                            journal.isTask2Completed() ? "X" : " ",
                            journal.isTask3Completed() ? "X" : " ");
                } catch (IllegalArgumentException e) {
                    System.out.println(" | Journal not found!");
                }
            }
        }
    }
}