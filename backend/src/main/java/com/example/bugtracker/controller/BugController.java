package com.example.bugtracker.controller;

import java.util.Optional;
import java.util.Set;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.bugtracker.model.Bug;
import com.example.bugtracker.model.Comment;
import com.example.bugtracker.model.Project;
import com.example.bugtracker.model.Role;
import com.example.bugtracker.model.User;
import com.example.bugtracker.repository.UserRepository;
import com.example.bugtracker.service.BugService;

import jakarta.validation.Valid;

@Controller
public class BugController {

    private final BugService bugService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public BugController(BugService bugService, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.bugService = bugService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login"; 
    }

    @GetMapping("/bugs")
    public String showDashboard(Model model) {
        model.addAttribute("bugs", bugService.findAllBugs()); 
        return "bugs"; 
    }

    @GetMapping("/bugs/new")
    public String showCreateForm(Model model) {
        model.addAttribute("bug", new Bug());
        model.addAttribute("projects", bugService.findAllProjects());
        model.addAttribute("users", bugService.findAllUsers());
        return "bug-form";
    }

    @PostMapping("/bugs")
    public String createBug(
            @Valid @ModelAttribute Bug bug,
            BindingResult result,
            @RequestParam(required = false) Long projectId,
            @RequestParam(required = false) Long assignedToId,
            Model model) {
        if (result.hasErrors()) {
            model.addAttribute("projects", bugService.findAllProjects());
            model.addAttribute("users", bugService.findAllUsers());
            return "bug-form";
        }
        if (projectId != null) {
            bugService.findProjectById(projectId).ifPresent(bug::setProject);
        }
        if (assignedToId != null) {
            userRepository.findById(assignedToId).ifPresent(bug::setAssignedTo);
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            userRepository.findByUsername(auth.getName()).ifPresent(bug::setCreatedBy);
        }
        bugService.save(bug);
        return "redirect:/bugs";
    }

    @GetMapping("/bugs/{id}")
    public String viewBug(@PathVariable Long id, Model model) {
        Optional<Bug> bug = bugService.findById(id);
        if (bug.isEmpty()) {
            return "redirect:/bugs";
        }
        model.addAttribute("bug", bug.get());
        model.addAttribute("comment", new Comment());
        return "bug-detail";
    }

    @PostMapping("/bugs/{id}/comments")
    public String addComment(@PathVariable Long id, @Valid @ModelAttribute Comment comment, BindingResult result, Model model) {
        Optional<Bug> existingBug = bugService.findById(id);
        if (existingBug.isEmpty()) {
            return "redirect:/bugs";
        }
        if (result.hasErrors()) {
            model.addAttribute("bug", existingBug.get());
            return "bug-detail";
        }
        comment.setBug(existingBug.get());
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        userRepository.findByUsername(auth.getName()).ifPresent(comment::setAuthor);
        bugService.saveComment(comment);
        return "redirect:/bugs/" + id;
    }

    @GetMapping("/admin/projects")
    public String viewProjects(Model model) {
        model.addAttribute("projects", bugService.findAllProjects());
        return "admin/projects";
    }

    @GetMapping("/admin/projects/new")
    public String showProjectForm(Model model) {
        model.addAttribute("project", new Project());
        return "admin/project-form";
    }

    @PostMapping("/admin/projects")
    public String saveProject(@Valid @ModelAttribute Project project, BindingResult result) {
        if (result.hasErrors()) {
            return "admin/project-form";
        }
        bugService.saveProject(project);
        return "redirect:/admin/projects";
    }

    @GetMapping("/admin/users")
    public String viewUsers(Model model) {
        model.addAttribute("users", bugService.findAllUsers());
        return "admin/users";
    }

    @GetMapping("/admin/users/new")
    public String showUserForm(Model model) {
        model.addAttribute("user", new User());
        return "admin/user-form";
    }

    @PostMapping("/admin/users")
    public String saveUser(@Valid @ModelAttribute User user, BindingResult result) {
        if (result.hasErrors()) {
            return "admin/user-form";
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        // ✨ FIXED: If no roles were explicitly selected in the admin form, fallback gracefully
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            user.setRoles(Set.of(Role.USER));
        }
        
        bugService.saveUser(user);
        return "redirect:/admin/users";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "register";
        }
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            result.rejectValue("username", "error.user", "Username already exists");
            return "register";
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        // ✨ FIXED: Checks if registering account username matches your admin identifier
        if ("om_tiwari".equalsIgnoreCase(user.getUsername())) {
            user.setRoles(Set.of(Role.ADMIN));
        } else {
            user.setRoles(Set.of(Role.USER));
        }
        
        bugService.saveUser(user);
        return "redirect:/login";
    }
}