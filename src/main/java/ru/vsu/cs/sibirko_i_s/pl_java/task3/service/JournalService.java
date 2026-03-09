package ru.vsu.cs.sibirko_i_s.pl_java.task3.service;

import ru.vsu.cs.sibirko_i_s.pl_java.task3.domain.model.Journal;
import ru.vsu.cs.sibirko_i_s.pl_java.task3.domain.repository.JournalRepository;

import java.time.LocalDateTime;
import java.util.List;

public class JournalService {

    private final JournalRepository journalRepository;

    public JournalService(JournalRepository journalRepository) {
        this.journalRepository = journalRepository;
    }

    public void createJournalForStudent(int studentId) {
        if (journalRepository.getById(studentId).isPresent()) {
            throw new IllegalArgumentException("У студента с ID " + studentId + " уже есть журнал!");
        }

        Journal newJournal = new Journal(studentId, false, false, false, LocalDateTime.now());
        journalRepository.save(newJournal);
    }

    public Journal getJournalByStudentId(int studentId) {
        return journalRepository.getById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Журнал для студента с ID " + studentId + " не найден."));
    }

    public void updateTaskStatus(int studentId, int taskNumber, boolean completed) {
        getJournalByStudentId(studentId);
        journalRepository.updateTaskStatus(studentId, taskNumber, completed);
    }

    public List<Journal> getJournalsByGroupId(int groupId) {
        return journalRepository.findByGroupId(groupId);
    }

    public void deleteJournal(int studentId) {
        journalRepository.remove(studentId);
    }
}