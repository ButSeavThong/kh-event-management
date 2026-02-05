package com.thong.event.utils;

import com.thong.event.domain.Category;
import com.thong.event.feature.category.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Data seeder for initializing sample categories
 * Runs only when the category table is empty
 */
@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataSeeder {

    private final CategoryRepository categoryRepository;

    @Bean
    @Transactional
    public CommandLineRunner seedCategories() {
        return args -> {

            if (categoryRepository.count() > 0) {
                log.info("Categories already exist. Skipping category seeding.");
                return;
            }

            List<Category> categories = List.of(
                    Category.builder()
                            .name("Technology")
                            .description("Tech conferences, workshops, and meetups")
                            .build(),

                    Category.builder()
                            .name("Music")
                            .description("Concerts, festivals, and live performances")
                            .build(),

                    Category.builder()
                            .name("Education")
                            .description("Workshops, seminars, and training sessions")
                            .build(),

                    Category.builder()
                            .name("Business")
                            .description("Networking events, conferences, and exhibitions")
                            .build(),

                    Category.builder()
                            .name("Sports")
                            .description("Sports events, tournaments, and fitness activities")
                            .build()
            );

            categoryRepository.saveAll(categories);
        };
    }
}
