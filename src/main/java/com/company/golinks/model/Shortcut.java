package com.company.golinks.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "shortcuts")
public class Shortcut {

    @Id
    private String keyword;
    private String targetUrl;
    private String description;
    private long clicks = 0;
    private LocalDateTime createdAt = LocalDateTime.now();

    public Shortcut() {}

    public Shortcut(String keyword, String targetUrl, String description) {
        this.keyword = keyword.toLowerCase().trim();
        this.targetUrl = targetUrl.trim();
        this.description = description != null ? description.trim() : "";
    }

    public String getKeyword() { return keyword; }
    public void setKeyword(String keyword) { this.keyword = keyword.toLowerCase().trim(); }

    public String getTargetUrl() { return targetUrl; }
    public void setTargetUrl(String targetUrl) { this.targetUrl = targetUrl; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public long getClicks() { return clicks; }
    public void setClicks(long clicks) { this.clicks = clicks; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
