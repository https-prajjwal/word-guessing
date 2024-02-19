    package com.example.assignmentone;

    import jakarta.servlet.http.HttpSession;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Controller;
    import org.springframework.ui.Model;
    import org.springframework.web.bind.annotation.*;

    @Controller
    public class AdminController {
        private final AdminRepository adminRepository;
        private final UserRepository userRepository;
        private final WordRepository wordRepository;

        public AdminController(AdminRepository adminRepository, UserRepository userRepository, WordRepository wordRepository) {

            this.adminRepository = adminRepository;
            this.userRepository = userRepository;
            this.wordRepository = wordRepository;
        }

        @GetMapping("/user")
        public String userLogin(){
            return "userLogin";
        }

        @GetMapping("/signup")
        public String signup(){
            return "userSignup";
        }

        @GetMapping("/")
        public String front() {
            return "front";
        }

        @GetMapping("/login")
        public String login() {
            return "login";
        }

        @PostMapping("/userCheck")
        public String userCheck(@RequestParam String email, @RequestParam String
                password, HttpSession session, Model model) {
            User user= userRepository.findByEmail(email);

            if (email.isEmpty()) {
                model.addAttribute("error", "No email provided");
                return "userLogin";
            }

            if (password.isEmpty()) {
                model.addAttribute("error", "No password provided");
                return "userLogin";
            }

            if (user != null && user.getPassword().equals(password)) {
                session.setAttribute("user", user);
                return "redirect:/wordFo";
            } else {
                model.addAttribute("error", "Invalid email or password");
                return "userLogin";
            }
        }


        @PostMapping("/check")
        public String check(@RequestParam String email, @RequestParam String
                password, HttpSession session, Model model) {
            Admin admin = adminRepository.findByEmail(email);

            if (email.isEmpty()) {
                model.addAttribute("error", "No email provided");
                return "login";
            }

            if (password.isEmpty()) {
                model.addAttribute("error", "No password provided");
                return "login";
            }

            if (admin != null && admin.getPassword().equals(password)) {
                session.setAttribute("admin", admin);
                return "redirect:/words/dashboard";
            } else {
                model.addAttribute("error", "Invalid email or password");
                return "login";
            }

        }


        @GetMapping("/logout")
        public String logout(HttpSession session) {
            session.removeAttribute("admin");
            return "redirect:/login";
        }


        @GetMapping("/wordFo")
        public String showForm(Model model) {
            model.addAttribute("levels", new String[]{"Easy", "Medium", "Hard"});
            model.addAttribute("selectedLevel", "");
            return "wordForm";
        }
        @PostMapping("/index")
        public String getWord(@ModelAttribute("selectedLevel") String
                                      selectedLevel, Model model, HttpSession session) {

            User user = (User) session.getAttribute("user");
            if (user == null) {
                return "redirect:/user";
            }


            int userScore = user.getScore();
            Long userId = user.getId();

            model.addAttribute("score", userScore);
            model.addAttribute("id",userId);

            Word word = wordRepository.findRandomWordByLevel(selectedLevel);
            model.addAttribute("word", word);
            String[] randomWordAndHint;
            String wordName = word.getName();
            String wordHint = word.getHint();
            String wordLevel = word.getLevel();
            randomWordAndHint = new String[]{wordName, wordHint, wordLevel};

            // Add randomWordAndHint to the model
            model.addAttribute("word", randomWordAndHint[0]);
            model.addAttribute("hint", randomWordAndHint[1]);
            model.addAttribute("level", randomWordAndHint[2]);
            session.setAttribute("selectedLevel", selectedLevel);
            return "index";}


        @Autowired
        private UserService userService;
        @PostMapping("/updateScore")
        public String updateScore(HttpSession session, Model model){
            User user = (User) session.getAttribute("user");
            if (user != null) {
                int newScore = user.getScore() + 1; // Increase score on winning
                user.setScore(newScore);
                userService.saveUser(user);
            }

            int userScore = user.getScore();
            Long userId = user.getId();
            String selectedLevel = (String) session.getAttribute("selectedLevel");
            model.addAttribute("score", userScore);
            model.addAttribute("id",userId);
            model.addAttribute("selectedLevel", selectedLevel);
            Word word = wordRepository.findRandomWordByLevel(selectedLevel);
            model.addAttribute("word", word);
            String[] randomWordAndHint;
            String wordName = word.getName();
            String wordHint = word.getHint();
            String wordLevel = word.getLevel();
            randomWordAndHint = new String[]{wordName, wordHint, wordLevel};

            // Add randomWordAndHint to the model
            model.addAttribute("word", randomWordAndHint[0]);
            model.addAttribute("hint", randomWordAndHint[1]);
            model.addAttribute("level", randomWordAndHint[2]);
            return "index";}


        @PostMapping("/reload")
        public String reload(HttpSession session, Model model){
            User user = (User) session.getAttribute("user");

            int userScore = user.getScore();
            Long userId = user.getId();
            String selectedLevel = (String) session.getAttribute("selectedLevel");
            model.addAttribute("score", userScore);
            model.addAttribute("id",userId);
            model.addAttribute("selectedLevel", selectedLevel);
            Word word = wordRepository.findRandomWordByLevel(selectedLevel);
            model.addAttribute("word", word);
            String[] randomWordAndHint;
            String wordName = word.getName();
            String wordHint = word.getHint();
            String wordLevel = word.getLevel();
            randomWordAndHint = new String[]{wordName, wordHint, wordLevel};

            // Add randomWordAndHint to the model
            model.addAttribute("word", randomWordAndHint[0]);
            model.addAttribute("hint", randomWordAndHint[1]);
            model.addAttribute("level", randomWordAndHint[2]);
            return "index";}

}
