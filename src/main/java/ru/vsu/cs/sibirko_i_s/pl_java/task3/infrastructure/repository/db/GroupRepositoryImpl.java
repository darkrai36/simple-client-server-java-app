package ru.vsu.cs.sibirko_i_s.pl_java.task3.infrastructure.repository.db;

import ru.vsu.cs.sibirko_i_s.pl_java.task3.domain.model.Group;
import ru.vsu.cs.sibirko_i_s.pl_java.task3.domain.repository.GroupRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GroupRepositoryImpl implements GroupRepository {
    private final Connection connection;

    public GroupRepositoryImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Group save(Group group) {
        if (group.getGroupId() == 0) {
            String sql = "INSERT INTO study_group (group_name, created_at) VALUES (?, ?) RETURNING group_id";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, group.getName());
                statement.setTimestamp(2, Timestamp.valueOf(group.getCreatedAt()));

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        group.setGroupId(resultSet.getInt("group_id"));
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException("Error saving group: " + e.getMessage(), e);
            }
        } else {
            String sql = "UPDATE study_group SET group_name = ? WHERE group_id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, group.getName());
                statement.setInt(2, group.getGroupId());
                statement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException("Error updating group: " + e.getMessage(), e);
            }
        }
        return group;
    }

    @Override
    public Optional<Group> getById(int id) {
        String sql = "SELECT * FROM study_group WHERE group_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapResultSetToGroup(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error searching group: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<Group> getAll() {
        List<Group> groups = new ArrayList<>();
        String sql = "SELECT * FROM study_group";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                groups.add(mapResultSetToGroup(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error getting list of groups: " + e.getMessage(), e);
        }
        return groups;
    }

    @Override
    public void remove(int id) {
        String sql = "DELETE FROM study_group WHERE group_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting group: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Group> findByName(String name) {
        String sql = "SELECT * FROM study_group WHERE group_name = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapResultSetToGroup(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error searching for group by name: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    private Group mapResultSetToGroup(ResultSet resultSet) throws SQLException {
        return new Group(
                resultSet.getInt("group_id"),
                resultSet.getString("group_name"),
                resultSet.getTimestamp("created_at").toLocalDateTime()
        );
    }
}