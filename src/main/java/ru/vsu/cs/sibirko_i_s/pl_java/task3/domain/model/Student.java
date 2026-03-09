package ru.vsu.cs.sibirko_i_s.pl_java.task3.domain.model;

public class Student {
    private int studentId;
    private int groupId;
    private String studentFirstName;
    private String studentLastName;

    public Student() {
    }

    public Student(String studentFirstName, String studentLastName) {
        if (studentFirstName == null || studentFirstName.trim().isEmpty()) {
            throw new IllegalArgumentException("Имя студента не введено!");
        } else if (studentLastName == null || studentLastName.trim().isEmpty()) {
            throw new IllegalArgumentException("Фамилия студента не введена!");
        }

        this.studentFirstName = studentFirstName;
        this.studentLastName = studentLastName;
    }
    public Student(int studentId, int groupId, String studentFirstName, String studentLastName) {
        this(studentFirstName, studentLastName);
        this.studentId = studentId;
        this.groupId = groupId;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getStudentFirstName() {
        return studentFirstName;
    }

    public void setStudentFirstName(String studentFirstName) {
        if (studentFirstName == null || studentFirstName.trim().isEmpty()) {
            throw new IllegalArgumentException("Имя студента не введено!");
        }
        this.studentFirstName = studentFirstName;
    }

    public String getStudentLastName() {
        return studentLastName;
    }

    public void setStudentLastName(String studentLastName) {
        this.studentLastName = studentLastName;
    }

    @Override
    public String toString() {
        return "Student{" +
                "studentId=" + studentId +
                ", groupId=" + groupId +
                ", studentFirstName='" + studentFirstName + '\'' +
                ", studentLastName='" + studentLastName + '\'' +
                '}';
    }
}
