package ru.vsu.cs.sibirko_i_s.pl_java.task3.domain.model;

import java.time.LocalDateTime;

public class Journal {
    private int studentId;
    private boolean task1Completed;
    private boolean task2Completed;
    private boolean task3Completed;
    private LocalDateTime updatedAt;

    public Journal() {
    }

    public Journal(int studentId, boolean task1Completed, boolean task2Completed, boolean task3Completed, LocalDateTime updatedAt) {
        this.studentId = studentId;
        this.task1Completed = task1Completed;
        this.task2Completed = task2Completed;
        this.task3Completed = task3Completed;
        this.updatedAt = updatedAt;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public boolean isTask1Completed() {
        return task1Completed;
    }

    public void setTask1Completed(boolean task1Completed) {
        this.task1Completed = task1Completed;
    }

    public boolean isTask2Completed() {
        return task2Completed;
    }

    public void setTask2Completed(boolean task2Completed) {
        this.task2Completed = task2Completed;
    }

    public boolean isTask3Completed() {
        return task3Completed;
    }

    public void setTask3Completed(boolean task3Completed) {
        this.task3Completed = task3Completed;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "Journal{" +
                "studentId=" + studentId +
                ", task1Completed=" + task1Completed +
                ", task2Completed=" + task2Completed +
                ", task3Completed=" + task3Completed +
                ", updatedAt=" + updatedAt +
                '}';
    }

    public boolean isAllTasksCompleted() {
        return task1Completed && task2Completed && task3Completed;
    }

    public int getCompletedTasksCount() {
        int count = 0;
        if (task1Completed) {
            count++;
        }

        if (task2Completed) {
            count++;
        }

        if (task3Completed) {
            count++;
        }

        return count;
    }
}
