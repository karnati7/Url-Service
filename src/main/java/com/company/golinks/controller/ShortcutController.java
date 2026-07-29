package com.company.golinks.controller;

import com.company.golinks.model.Shortcut;
import com.company.golinks.repository.ShortcutRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;
import java.util.Optional;

@Controller
public class ShortcutController {

    private final ShortcutRepository repository;

    public ShortcutController(ShortcutRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/api/shortcuts")
    @ResponseBody
    public List<Shortcut> getAllShortcuts() {
        return repository.findAll();
    }

    @PostMapping("/api/shortcuts")
    @ResponseBody
    public ResponseEntity<?> createOrUpdateShortcut(@RequestBody Shortcut shortcut) {
        if (shortcut.getKeyword() == null || shortcut.getKeyword().isBlank() ||
            shortcut.getTargetUrl() == null || shortcut.getTargetUrl().isBlank()) {
            return ResponseEntity.badRequest().body("Keyword and Target URL are required.");
        }

        String targetUrl = shortcut.getTargetUrl().trim();
        if (!targetUrl.startsWith("http://") && !targetUrl.startsWith("https://")) {
            targetUrl = "https://" + targetUrl;
        }
        shortcut.setTargetUrl(targetUrl);

        Shortcut saved = repository.save(shortcut);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @DeleteMapping("/api/shortcuts/{keyword}")
    @ResponseBody
    public ResponseEntity<Void> deleteShortcut(@PathVariable String keyword) {
        repository.deleteById(keyword.toLowerCase().trim());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{keyword:[a-zA-Z0-9\\-_]+}")
    public RedirectView redirectToTarget(@PathVariable String keyword) {
        Optional<Shortcut> shortcutOpt = repository.findById(keyword.toLowerCase().trim());
        
        if (shortcutOpt.isPresent()) {
            Shortcut shortcut = shortcutOpt.get();
            shortcut.setClicks(shortcut.getClicks() + 1);
            repository.save(shortcut);

            RedirectView redirectView = new RedirectView(shortcut.getTargetUrl());
            redirectView.setStatusCode(HttpStatus.FOUND);
            return redirectView;
        } else {
            return new RedirectView("/?notfound=" + keyword);
        }
    }
}
