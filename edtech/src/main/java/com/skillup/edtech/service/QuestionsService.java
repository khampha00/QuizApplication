package com.skillup.edtech.service;

import com.skillup.edtech.model.Question;
import com.skillup.edtech.repository.QuestionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class QuestionsService {

    private static final Logger log = LoggerFactory.getLogger(QuestionsService.class);
    private final QuestionRepository questionRepository;

    public QuestionsService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
        // Pre-populate with sample questions only if DB is empty

    }

    public List<Question> loadQuizzes() {
        log.debug("Loading all quizzes.");
        return questionRepository.findAll();
    }

    public Question getQuizById(int id) {
        log.debug("Fetching quiz with id: {}", id);
        Optional<Question> q = questionRepository.findById(id);
        return q.orElse(null);
    }

    public void addQuiz(Question question) {
        Question saved = questionRepository.save(question);
        log.info("Added new quiz with id: {}", saved.getId());
    }

    public void editQuiz(Question question) {
        if (question.getId() == null) {
            log.warn("Attempted to edit quiz without id");
            return;
        }
        if (questionRepository.existsById(question.getId())) {
            questionRepository.save(question);
            log.info("Edited quiz with id: {}", question.getId());
        } else {
            log.warn("Attempted to edit non-existent quiz with id: {}", question.getId());
        }
    }

    public void deleteQuiz(int id) {
        if (questionRepository.existsById(id)) {
            questionRepository.deleteById(id);
            log.info("Deleted quiz with id: {}", id);
        } else {
            log.warn("Attempted to delete non-existent quiz with id: {}", id);
        }
    }
}