package com.example.assignmentone;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Random;

@Controller
@RequestMapping("/words")
public class WordController {
    @Autowired
    private WordService wordService;

    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        List < Word > words = wordService.getAllWords();
        model.addAttribute("words", words);
        return "dashboard";
    }


    @GetMapping("/addWord")
    public String addWord(Model model) {
        // Add the "word" attribute to the model for the form in the "dashboard" page
        model.addAttribute("word", new Word());
        return "addWord";
    }

    @PostMapping("/save")
    public String saveWord(@ModelAttribute Word word, RedirectAttributes redirectAttributes, @RequestParam String name, Model model) {

        if (name.isEmpty()) {
            redirectAttributes.addFlashAttribute("successMessage", "No word provided!");
            return "redirect:/words/addWord";
        } else if (name.length() != 5) {
            redirectAttributes.addFlashAttribute("successMessage", "No five-letter word provided!");
            return "redirect:/words/addWord";
        } else{
            wordService.saveWord(word);

            redirectAttributes.addFlashAttribute("successMessage", "Word successfully saved! Add more words.");
            return "redirect:/words/addWord";
        }
    }


    @GetMapping("/edit/{id}")
    public String editWordForm(@PathVariable Long id, Model model) {
        wordService.getWordById(id).ifPresent(word -> model.addAttribute("word", word));
        return "addWord";
    }
    @GetMapping("/delete/{id}")
    public String deleteWord(@PathVariable Long id) {
        wordService.deleteWord(id);
        return "redirect:/words/dashboard";
    }



}





