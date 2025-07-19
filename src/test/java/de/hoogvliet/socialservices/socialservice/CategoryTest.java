package de.hoogvliet.socialservices.socialservice;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class CategoryTest {

    @Test
    void ensure_that_equals_checks_on_id_and_name() {
        Category cat1 = new Category();
        cat1.setId(24);
        cat1.setName("Test24");
        Category cat2 = new Category();
        cat2.setId(24);
        cat2.setName("Test24");
        assertEquals(cat1, cat2);
    }

    @Test
    void ensure_that_equals_recognizes_differences_in_id() {
        Category cat1 = new Category();
        cat1.setId(24);
        cat1.setName("Test24");
        Category cat2 = new Category();
        cat2.setId(25);
        cat2.setName("Test24");
        assertNotEquals(cat1, cat2);
    }

    @Test
    void ensure_that_equals_recognizes_differences_in_name() {
        Category cat1 = new Category();
        cat1.setId(24);
        cat1.setName("Test24");
        Category cat2 = new Category();
        cat2.setId(24);
        cat2.setName("Test25");
        assertNotEquals(cat1, cat2);
    }

    @Test
    void ensure_that_equals_recognizes_differences_in_both() {
        Category cat1 = new Category();
        cat1.setId(24);
        cat1.setName("Test24");
        Category cat2 = new Category();
        cat2.setId(25);
        cat2.setName("Test25");
        assertNotEquals(cat1, cat2);
    }

}