package com.skillup.edtech.controller;

import com.skillup.edtech.model.Question;
import com.skillup.edtech.model.User;
import com.skillup.edtech.service.QuestionsService;
import com.skillup.edtech.service.QuizUserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Controller
public class QuizController {

    private final QuestionsService questionsService;
    private final QuizUserDetailsService userDetailsService;

    public QuizController(QuestionsService questionsService, QuizUserDetailsService userDetailsService) {
        this.questionsService = questionsService;
        this.userDetailsService = userDetailsService;
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/login"; // Home page redirects to login [cite: 124]
    }

    @GetMapping("/login") // [cite: 86]
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register") // [cite: 87]
    public String registerPage(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register") // [cite: 88]
    public String registerUser(@ModelAttribute User user, RedirectAttributes redirectAttributes) {
        try {
            userDetailsService.registerUser(user);
            redirectAttributes.addFlashAttribute("success", "Registration successful! Please log in.");
            return "redirect:/login";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/register";
        }
    }

    // Admin: View all questions
    @GetMapping("/quiz-list")
    public String quizList(Model model) {
        model.addAttribute("questions", questionsService.loadQuizzes());
        return "quiz-list";
    }

    // Admin: Show add quiz form
    @GetMapping("/add-quiz") // [cite: 89]
    public String addQuizForm(Model model) {
        model.addAttribute("question", new Question());
        return "add-quiz";
    }

    // Admin: Process adding a quiz
    @PostMapping("/add-quiz") // [cite: 90]
    public String addQuizSubmit(@ModelAttribute Question question) {
        questionsService.addQuiz(question);
        return "redirect:/quiz-list";
    }

    // Admin: Show edit quiz form
    @GetMapping("/edit-quiz/{id}") // [cite: 91]
    public String editQuizForm(@PathVariable int id, Model model) {
        Question question = questionsService.getQuizById(id);
        if (question != null) {
            model.addAttribute("question", question);
            return "edit-quiz";
        }
        return "redirect:/quiz-list";
    }

    // CHANGED: accept POST instead of PUT
    @PostMapping("/edit-quiz") // [cite: 92]
    public String editQuizSubmit(@ModelAttribute Question question) {
        questionsService.editQuiz(question);
        return "redirect:/quiz-list";
    }

    // Optional: also make delete POST-based for plain HTML forms
    @PostMapping("/delete-quiz/{id}") // [cite: 93]
    public String deleteQuiz(@PathVariable int id) {
        questionsService.deleteQuiz(id);
        return "redirect:/quiz-list";
    }

    // User: View quiz
    @GetMapping("/quiz") // [cite: 94]
    public String takeQuiz(Model model) {
        model.addAttribute("questions", questionsService.loadQuizzes());
        return "quiz";
    }

    // User: Submit quiz answers
    @PostMapping("/submit") // [cite: 95]
    public String submitQuiz(@RequestParam Map<String, String> answers, RedirectAttributes redirectAttributes) {
        AtomicInteger score = new AtomicInteger(0);
        answers.forEach((key, value) -> {
            if (key.startsWith("q_")) {
                int questionId = Integer.parseInt(key.substring(2));
                Question question = questionsService.getQuizById(questionId);
                if (question != null && question.getCorrectAnswer().equals(value)) {
                    score.incrementAndGet();
                }
            }
        });
        redirectAttributes.addFlashAttribute("score", score.get());
        redirectAttributes.addFlashAttribute("total", questionsService.loadQuizzes().size());
        return "redirect:/result";
    }

    // User: View results
    @GetMapping("/result") // [cite: 96]
    public String showResult() {
        return "result";
    }
}