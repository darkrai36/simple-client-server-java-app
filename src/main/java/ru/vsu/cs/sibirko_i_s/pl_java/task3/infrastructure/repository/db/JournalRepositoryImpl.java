package ru.vsu.cs.sibirko_i_s.pl_java.task3.infrastructure.repository.db;

import ru.vsu.cs.sibirko_i_s.pl_java.task3.domain.model.Journal;
import ru.vsu.cs.sibirko_i_s.pl_java.task3.domain.repository.JournalRepository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JournalRepositoryImpl implements JournalRepository {
    private final Connection connection;

    public JournalRepositoryImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Journal save(Journal journal) {
        journal.setUpdatedAt(LocalDateTime.now());

        boolean exists = false;
        String checkSql = "SELECT 1 FROM journal WHERE student_id = ?";
        try (PreparedStatement checkStmt = connection.prepareStatement(checkSql)) {
            checkStmt.setInt(1, journal.getStudentId());
            try (ResultSet rs = checkStmt.executeQuery()) {
                exists = rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error checking for log existence: " + e.getMessage(), e);
        }

        if (exists) {
            String updateSql = "UPDATE journal SET task1_completed = ?, task2_completed = ?, task3_completed = ?, updated_at = ? WHERE student_id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(updateSql)) {
                stmt.setBoolean(1, journal.isTask1Completed());
                stmt.setBoolean(2, journal.isTask2Completed());
                stmt.setBoolean(3, journal.isTask3Completed());
                stmt.setTimestamp(4, Timestamp.valueOf(journal.getUpdatedAt()));
                stmt.setInt(5, journal.getStudentId());
                stmt.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException("Error updating log: " + e.getMessage(), e);
            }
        } else {
            String insertSql = "INSERT INTO journal (student_id, task1_completed, task2_completed, task3_completed, updated_at) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(insertSql)) {
                stmt.setInt(1, journal.getStudentId());
                stmt.setBoolean(2, journal.isTask1Completed());
                stmt.setBoolean(3, journal.isTask2Completed());
                stmt.setBoolean(4, journal.isTask3Completed());
                stmt.setTimestamp(5, Timestamp.valueOf(journal.getUpdatedAt()));
                stmt.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException("Error creating log: " + e.getMessage(), e);
            }
        }
        return journal;
    }

    @Override
    public Optional<Journal> getById(int studentId) {
        String sql = "SELECT * FROM journal WHERE student_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToJournal(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error getting log: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<Journal> getAll() {
        List<Journal> journals = new ArrayList<>();
        String sql = "SELECT * FROM journal";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                journals.add(mapResultSetToJournal(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error getting all the logs: " + e.getMessage(), e);
        }
        return journals;
    }

    @Override
    public void remove(int studentId) {
        String sql = "DELETE FROM journal WHERE student_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error removing log: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Journal> findByStudentIdWithStudent(int studentId) {
        return getById(studentId);
    }

    @Override
    public List<Journal> findAllWithStudents() {
        return getAll();
    }

    @Override
    public List<Journal> findByTaskStatus(int number, boolean completed) {
        List<Journal> journals = new ArrayList<>();
        String columnName = "task" + number + "_completed";
        String sql = "SELECT * FROM journal WHERE " + columnName + " = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setBoolean(1, completed);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    journals.add(mapResultSetToJournal(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error searching by task status: " + e.getMessage(), e);
        }
        return journals;
    }

    @Override
    public List<Journal> findByGroupId(int groupId) {
        List<Journal> journals = new ArrayList<>();
        String sql = "SELECT j.* FROM journal j JOIN student s ON j.student_id = s.student_id WHERE s.group_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, groupId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    journals.add(mapResultSetToJournal(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error searching logs by group: " + e.getMessage(), e);
        }
        return journals;
    }

    @Override
    public void updateTaskStatus(int studentId, int taskNumber, boolean completed) {
        String columnName = "task" + taskNumber + "_completed";
        String sql = "UPDATE journal SET " + columnName + " = ?, updated_at = CURRENT_TIMESTAMP WHERE student_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setBoolean(1, completed);
            stmt.setInt(2, studentId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating task status: " + e.getMessage(), e);
        }
    }

    @Override
    public void markAllTasks(int studentId, boolean completed) {
        String sql = "UPDATE journal SET task1_completed = ?, task2_completed = ?, task3_completed = ?, updated_at = CURRENT_TIMESTAMP WHERE student_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setBoolean(1, completed);
            stmt.setBoolean(2, completed);
            stmt.setBoolean(3, completed);
            stmt.setInt(4, studentId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating all tasks: " + e.getMessage(), e);
        }
    }

    // Вспомогательный метод
    private Journal mapResultSetToJournal(ResultSet rs) throws SQLException {
        return new Journal(
                rs.getInt("student_id"),
                rs.getBoolean("task1_completed"),
                rs.getBoolean("task2_completed"),
                rs.getBoolean("task3_completed"),
                rs.getTimestamp("updated_at").toLocalDateTime()
        );
    }
}