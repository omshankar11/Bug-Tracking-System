package com.example.bugtracker.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.bugtracker.model.Bug;
import com.example.bugtracker.model.Comment;
import com.example.bugtracker.model.Project;
import com.example.bugtracker.model.User;
import com.example.bugtracker.repository.BugRepository;
import com.example.bugtracker.repository.CommentRepository;
import com.example.bugtracker.repository.ProjectRepository;
import com.example.bugtracker.repository.UserRepository;

@Service
// ✨ ADDED: Implements UserDetailsService so Spring Security can read roles from here
public class BugService implements UserDetailsService {

    private final BugRepository bugRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    public BugService(BugRepository bugRepository,
                      ProjectRepository projectRepository,
                      UserRepository userRepository,
                      CommentRepository commentRepository) {
        this.bugRepository = bugRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
    }

    // 🌟 SPRING SECURITY INTEGRATION METHOD
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        // Converts your user's roles (whether it's a Set<Role> or a collection) dynamically into Spring Authorities
        // Using .name() ensures Enums convert perfectly to strings like "ADMIN" or "USER"
        java.util.List<SimpleGrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.name()))
                .collect(Collectors.toList());

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                authorities
        );
    }

    // ─── EXISTING CRUD METHODS ───

    public List<Bug> findAllBugs() {
        return bugRepository.findAll();
    }

    public Optional<Bug> findById(Long id) {
        return bugRepository.findById(id);
    }

    public Bug save(Bug bug) {
        return bugRepository.save(bug);
    }

    public void deleteById(Long id) {
        bugRepository.deleteById(id);
    }

    public List<Project> findAllProjects() {
        return projectRepository.findAll();
    }

    public Optional<Project> findProjectById(Long id) {
        return projectRepository.findById(id);
    }

    public Project saveProject(Project project) {
        return projectRepository.save(project);
    }

    public void deleteProject(Long id) {
        projectRepository.deleteById(id);
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public Comment saveComment(Comment comment) {
        return commentRepository.save(comment);
    }
}