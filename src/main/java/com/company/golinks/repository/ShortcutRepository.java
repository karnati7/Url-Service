package com.company.golinks.repository;

import com.company.golinks.model.Shortcut;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShortcutRepository extends JpaRepository<Shortcut, String> {
}
