package capstone.interview.controller;

import capstone.interview.model.User;
import capstone.interview.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class AccountController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/delete-account")
    public String showDeleteAccountPage() {
        return "delete-account";
    }

    @PostMapping("/delete-account")
    public String deleteAccount(@RequestParam("password") String password, Model model) {
        boolean isDeleted = userService.deleteAccount(password);

        if (isDeleted) {
            return "redirect:/logout";
        } else {
            model.addAttribute("error", "비밀번호가 일치하지 않습니다.");
            return "delete-account";
        }
    }

    @GetMapping("/change-password")
    public String showChangePasswordPage() {
        return "change-password";
    }

    @PostMapping("/change-password")
    public String changePassword(@RequestParam("currentPassword") String currentPassword,
                                 @RequestParam("newPassword") String newPassword,
                                 @RequestParam("confirmPassword") String confirmPassword,
                                 Model model) {

        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "새 비밀번호가 일치하지 않습니다.");
            return "change-password";
        }

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        Optional<User> userOptional = userService.findByUsername(username);
        if (userOptional.isEmpty() || !passwordEncoder.matches(currentPassword, userOptional.get().getPassword())) {
            model.addAttribute("error", "현재 비밀번호가 일치하지 않습니다.");
            return "change-password";
        }

        User user = userOptional.get();
        user.setPassword(newPassword);  // 비밀번호는 인코딩되지 않은 상태로 설정합니다.
        userService.save(user);  // save 메서드에서 비밀번호를 인코딩하고 저장합니다.

        return "redirect:/profile";
    }


}