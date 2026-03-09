import ru.vsu.cs.sibirko_i_s.pl_java.task3.domain.model.Group;
import ru.vsu.cs.sibirko_i_s.pl_java.task3.domain.model.Journal;
import ru.vsu.cs.sibirko_i_s.pl_java.task3.domain.model.Student;
import ru.vsu.cs.sibirko_i_s.pl_java.task3.domain.repository.GroupRepository;
import ru.vsu.cs.sibirko_i_s.pl_java.task3.domain.repository.JournalRepository;
import ru.vsu.cs.sibirko_i_s.pl_java.task3.domain.repository.StudentRepository;
import ru.vsu.cs.sibirko_i_s.pl_java.task3.infrastructure.repository.inmemory.GroupRepositoryInMemory;
import ru.vsu.cs.sibirko_i_s.pl_java.task3.infrastructure.repository.inmemory.JournalRepositoryInMemory;
import ru.vsu.cs.sibirko_i_s.pl_java.task3.infrastructure.repository.inmemory.StudentRepositoryInMemory;
import ru.vsu.cs.sibirko_i_s.pl_java.task3.service.GroupService;
import ru.vsu.cs.sibirko_i_s.pl_java.task3.service.JournalService;
import ru.vsu.cs.sibirko_i_s.pl_java.task3.service.StudentService;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        GroupRepository groupRepository = new GroupRepositoryInMemory();
        StudentRepository studentRepository = new StudentRepositoryInMemory();
        JournalRepository journalRepository = new JournalRepositoryInMemory(studentRepository);

        JournalService journalService = new JournalService(journalRepository);
        StudentService studentService = new StudentService(studentRepository, groupRepository, journalService);
        GroupService groupService = new GroupService(groupRepository, studentService);

        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome!");

        while (true) {
            printMenu();
            System.out.print("Choose action: ");
            String choice = scanner.nextLine();

            try {
                switch (choice) {
                    case "1" -> {
                        System.out.print("Print new group name: ");
                        String name = scanner.nextLine();
                        Group group = groupService.createGroup(name);
                        System.out.println("Group has created successfully: " + group);
                    }
                    case "2" -> {
                        System.out.print("Print group id to remove: ");
                        int groupId = Integer.parseInt(scanner.nextLine());
                        groupService.deleteGroup(groupId);
                        System.out.println("Group and all its students are deleted successfully.");
                    }
                    case "3" -> {
                        System.out.print("Print group id: ");
                        int groupId = Integer.parseInt(scanner.nextLine());
                        System.out.print("Print student first name: ");
                        String firstName = scanner.nextLine();
                        System.out.print("Print student last name: ");
                        String lastName = scanner.nextLine();

                        Student student = studentService.createStudent(groupId, firstName, lastName);
                        System.out.println("Student added successfully, journal created: " + student);
                    }
                    case "4" -> {
                        System.out.print("Print student id to remove: ");
                        int studentId = Integer.parseInt(scanner.nextLine());
                        studentService.deleteStudent(studentId);
                        System.out.println("Student and his journal deleted successfully.");
                    }
                    case "5" -> {
                        System.out.print("Print student id: ");
                        int studentId = Integer.parseInt(scanner.nextLine());
                        System.out.print("Print number of task (1, 2 or 3): ");
                        int taskNumber = Integer.parseInt(scanner.nextLine());
                        System.out.print("is task completed? (true/false): ");
                        boolean status = Boolean.parseBoolean(scanner.nextLine());

                        journalService.updateTaskStatus(studentId, taskNumber, status);
                        System.out.println("Task status updated!");
                    }
                    case "6" -> showAllData(groupService, studentService, journalService);
                    case "0" -> {
                        System.out.println("End work...");
                        return;
                    }
                    default -> System.out.println("Incorrect command, try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Please, print digit, where should be ID or number.");
            } catch (IllegalArgumentException e) {
                System.out.println("Business-logic error: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("An unexpected error occurred: " + e.getMessage());
            }

            System.out.println("--------------------------------------------------");
        }
    }

    private static void printMenu() {
        System.out.println("\n--- MAIN MENU ---");
        System.out.println("1. Create group");
        System.out.println("2. Delete group (and all its students)");
        System.out.println("3. Add student to group");
        System.out.println("4. Delete student");
        System.out.println("5. Change student's task status in journal");
        System.out.println("6. Show all groups, students and their journals");
        System.out.println("0. Exit");
    }

    private static void showAllData(GroupService groupService, StudentService studentService, JournalService journalService) {
        List<Group> groups = groupService.getAllGroups();
        if (groups.isEmpty()) {
            System.out.println("No groops in system.");
            return;
        }

        for (Group group : groups) {
            System.out.println("\n[Group ID: " + group.getGroupId() + "] " + group.getName());
            List<Student> students = studentService.getStudentsByGroupId(group.getGroupId());

            if (students.isEmpty()) {
                System.out.println("  There are no students in this group.");
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
                    System.out.println(" | Journal is not found!");
                }
            }
        }
    }
}