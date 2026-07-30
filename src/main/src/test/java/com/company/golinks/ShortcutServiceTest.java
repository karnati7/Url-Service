package com.company.golinks.service;

import com.company.golinks.model.Shortcut;
import com.company.golinks.repository.ShortcutRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShortcutServiceTest {

    @Mock
    private ShortcutRepository repository;

    @InjectMocks
    private ShortcutService service;

    @Test
    @DisplayName("Should fetch all shortcuts from repository")
    void testGetAllShortcuts() {
        when(repository.findAll()).thenReturn(List.of(
                new Shortcut("oncall", "https://pagerduty.com", "On-Call"),
                new Shortcut("payroll", "https://gusto.com", "Payroll")
        ));

        List<Shortcut> shortcuts = service.getAllShortcuts();

        assertEquals(2, shortcuts.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should prepend https:// to URL if protocol is missing during save")
    void testSaveShortcutPrependsHttps() {
        Shortcut input = new Shortcut("payroll", "gusto.com/login", "HR");
        when(repository.save(any(Shortcut.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Shortcut saved = service.saveShortcut(input);

        assertEquals("https://gusto.com/login", saved.getTargetUrl());
        verify(repository, times(1)).save(input);
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when keyword or targetUrl is missing")
    void testSaveShortcutValidationErrors() {
        Shortcut missingKeyword = new Shortcut("", "https://example.com", "Test");
        Shortcut missingUrl = new Shortcut("test", "", "Test");

        assertThrows(IllegalArgumentException.class, () -> service.saveShortcut(missingKeyword));
        assertThrows(IllegalArgumentException.class, () -> service.saveShortcut(missingUrl));
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Should process redirect and increment click count")
    void testProcessRedirectSuccess() {
        Shortcut existing = new Shortcut("design-system", "https://figma.com", "Design");
        existing.setClicks(5);

        when(repository.findById("design-system")).thenReturn(Optional.of(existing));

        Optional<String> targetUrl = service.processRedirect("DESIGN-SYSTEM");

        assertTrue(targetUrl.isPresent());
        assertEquals("https://figma.com", targetUrl.get());
        assertEquals(6, existing.getClicks());
        verify(repository, times(1)).save(existing);
    }

    @Test
    @DisplayName("Should return empty optional if shortcut keyword does not exist")
    void testProcessRedirectNotFound() {
        when(repository.findById("unknown")).thenReturn(Optional.empty());

        Optional<String> targetUrl = service.processRedirect("unknown");

        assertTrue(targetUrl.isEmpty());
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Should delete shortcut by normalized keyword")
    void testDeleteShortcut() {
        service.deleteShortcut(" ONCALL ");

        verify(repository, times(1)).deleteById("oncall");
    }
}
