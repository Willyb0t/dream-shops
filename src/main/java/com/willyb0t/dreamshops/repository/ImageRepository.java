package com.willyb0t.dreamshops.repository;

import com.willyb0t.dreamshops.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
