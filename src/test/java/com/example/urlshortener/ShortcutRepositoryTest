package com.company.golinks;

import com.company.golinks.model.Shortcut;
import com.company.golinks.repository.ShortcutRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ShortcutRepositoryTest {

    @Autowired
    private ShortcutRepository repository;

    @Test
    @DisplayName("Should save and lower-case keyword automatically")
    void testSaveShortcutNormalizesKeyword() {
        Shortcut shortcut = new Shortcut(" DESIGN-SYSTEM ", "https://figma.com", "Test");
        Shortcut saved = repository.save(shortcut);

        assertEquals("design-system", saved.getKeyword());
        Optional<Shortcut> retrieved = repository.findById("design-system");
        assertTrue(retrieved.isPresent());
    }
}
