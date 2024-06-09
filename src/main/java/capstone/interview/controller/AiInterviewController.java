package capstone.interview.controller;

import capstone.interview.model.Industry;
import capstone.interview.model.InterviewQuestion;
import capstone.interview.model.Job;
import capstone.interview.model.Occupation;
import capstone.interview.service.IndustryService;
import capstone.interview.service.InterviewQuestionService;
import capstone.interview.service.JobService;
import capstone.interview.service.OccupationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class AiInterviewController {
    @Autowired
    private IndustryService industryService;

    @Autowired
    private JobService jobService;

    @Autowired
    private OccupationService occupationService;

    @Autowired
    private InterviewQuestionService interviewQuestionService;

    @GetMapping("/Ai-interview-step1")
    public String aiInterviewStep1(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return "redirect:/login";
        }
        return "Ai-interview-step1";
    }

    @GetMapping("/Ai-interview-step2")
    public String aiInterviewStep2(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        if (userDetails == null) {
            return "redirect:/login";
        }

        model.addAttribute("industries", industryService.findAll());
        return "Ai-interview-step2";
    }

    @GetMapping("/industries")
    @ResponseBody
    public List<Industry> getIndustries() {
        return industryService.findAll();
    }

    @GetMapping("/jobs")
    @ResponseBody
    public List<Job> getJobsByIndustry(@RequestParam("industryName") String industryName) {
        return jobService.findByIndustryName(industryName);
    }

    @GetMapping("/occupations")
    @ResponseBody
    public List<Occupation> getOccupationsByJob(@RequestParam("jobName") String jobName) {
        return occupationService.findByJobName(jobName);
    }

    @GetMapping("/interview-questions")
    @ResponseBody
    public List<InterviewQuestion> getInterviewQuestions(@RequestParam("occupationName") String occupationName) {
        return interviewQuestionService.getQuestionsByOccupationName(occupationName);
    }

    @GetMapping("/Ai-interview-step3")
    public String aiInterviewStep3(@AuthenticationPrincipal UserDetails userDetails, @RequestParam("occupationName") String occupationName, Model model) {
        if (userDetails == null) {
            return "redirect:/login";
        }

        model.addAttribute("occupationName", occupationName);
        return "Ai-interview-step3";
    }

    @GetMapping("/Ai-interview-step4")
    public String aiInterviewStep4(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return "redirect:/login";
        }
        return "Ai-interview-step4";
    }
}
