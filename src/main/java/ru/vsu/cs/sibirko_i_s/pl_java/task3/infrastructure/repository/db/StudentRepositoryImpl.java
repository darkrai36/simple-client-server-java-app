package ru.vsu.cs.sibirko_i_s.pl_java.task3.infrastructure.repository.db;

import ru.vsu.cs.sibirko_i_s.pl_java.task3.domain.model.Student;
import ru.vsu.cs.sibirko_i_s.pl_java.task3.domain.repository.StudentRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StudentRepositoryImpl implements StudentRepository {
    private final Connection connection;

    public StudentRepositoryImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Student save(Student student) {
        if (student.getStudentId() == 0) {
            String sql = "INSERT INTO student (group_id, student_name, student_lastname) VALUES (?, ?, ?) RETURNING student_id";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, student.getGroupId());
                statement.setString(2, student.getStudentFirstName());
                statement.setString(3, student.getStudentLastName());

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        student.setStudentId(resultSet.getInt("student_id"));
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException("Error saving student: " + e.getMessage(), e);
            }
        } else {
            String sql = "UPDATE student SET group_id = ?, student_name = ?, student_lastname = ? WHERE student_id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, student.getGroupId());
                statement.setString(2, student.getStudentFirstName());
                statement.setString(3, student.getStudentLastName());
                statement.setInt(4, student.getStudentId());
                statement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException("Error updating student: " + e.getMessage(), e);
            }
        }
        return student;
    }

    @Override
    public Optional<Student> getById(int id) {
        String sql = "SELECT * FROM student WHERE student_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapResultSetToStudent(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error searching student: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<Student> getAll() {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM student";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                students.add(mapResultSetToStudent(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error getting all students: " + e.getMessage(), e);
        }
        return students;
    }

    @Override
    public void remove(int id) {
        String sql = "DELETE FROM student WHERE student_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting student: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Student> findStudentsByGroupId(int groupId) {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM student WHERE group_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, groupId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    students.add(mapResultSetToStudent(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error while searching for students in a group: " + e.getMessage(), e);
        }
        return students;
    }

    @Override
    public Optional<Student> findByFullName(String firstName, String lastName) {
        String sql = "SELECT * FROM student WHERE student_name = ? AND student_lastname = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, firstName);
            statement.setString(2, lastName);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapResultSetToStudent(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error searching for student by first name: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Student> findByLastName(String lastName) {
        String sql = "SELECT * FROM student WHERE student_lastname = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, lastName);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapResultSetToStudent(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error searching for student by last name: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public void moveStudentToNewGroup(int studentId, int newGroupId) {
        String sql = "UPDATE student SET group_id = ? WHERE student_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, newGroupId);
            statement.setInt(2, studentId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error when transferring a student to a new group: " + e.getMessage(), e);
        }
    }

    private Student mapResultSetToStudent(ResultSet resultSet) throws SQLException {
        return new Student(
                resultSet.getInt("student_id"),
                resultSet.getInt("group_id"),
                resultSet.getString("student_name"),
                resultSet.getString("student_lastname")
        );
    }
}