package com.company.golinks;

import com.company.golinks.model.Shortcut;
import com.company.golinks.repository.ShortcutRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ShortcutControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ShortcutRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    @Test
    @DisplayName("GET / - Should serve index view")
    void testIndexView() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }

    @Test
    @DisplayName("POST /api/shortcuts - Should create a shortcut with valid inputs")
    void testCreateShortcutSuccess() throws Exception {
        Shortcut newShortcut = new Shortcut("design-system", "https://figma.com", "Figma Design");

        mockMvc.perform(post("/api/shortcuts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newShortcut)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.keyword", is("design-system")))
                .andExpect(jsonPath("$.targetUrl", is("https://figma.com")))
                .andExpect(jsonPath("$.description", is("Figma Design")));

        assertTrue(repository.findById("design-system").isPresent());
    }

    @Test
    @DisplayName("POST /api/shortcuts - Should auto-append https:// if missing")
    void testCreateShortcutAutoPrependsHttps() throws Exception {
        Shortcut newShortcut = new Shortcut("payroll", "gusto.com/login", "HR");

        mockMvc.perform(post("/api/shortcuts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newShortcut)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.targetUrl", is("https://gusto.com/login")));
    }

    @Test
    @DisplayName("GET /{keyword} - Should perform 302 Redirect and increment click count")
    void testRedirectAndIncrementClicks() throws Exception {
        Shortcut shortcut = new Shortcut("oncall", "https://pagerduty.com/oncall", "PagerDuty");
        repository.save(shortcut);

        mockMvc.perform(get("/oncall"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("https://pagerduty.com/oncall"));

        Shortcut updated = repository.findById("oncall").orElseThrow();
        assertEquals(1, updated.getClicks());
    }

    @Test
    @DisplayName("DELETE /api/shortcuts/{keyword} - Should delete shortcut")
    void testDeleteShortcut() throws Exception {
        repository.save(new Shortcut("temp", "https://example.com", "Temporary"));

        mockMvc.perform(delete("/api/shortcuts/temp"))
                .andExpect(status().isOk());

        assertFalse(repository.findById("temp").isPresent());
    }
}
