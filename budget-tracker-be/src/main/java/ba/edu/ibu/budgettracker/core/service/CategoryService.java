package ba.edu.ibu.budgettracker.core.service;

import ba.edu.ibu.budgettracker.core.model.Category;
import ba.edu.ibu.budgettracker.core.repository.CategoryRepository;
import ba.edu.ibu.budgettracker.rest.dto.CategoryDto;
import ba.edu.ibu.budgettracker.rest.dto.CategoryRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<CategoryDto> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(category -> new CategoryDto(category.getId(), category.getName()))
                .collect(Collectors.toList());
    }

    public Optional<CategoryRequest> getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .map(category -> new CategoryRequest(category.getName()));
    }

    public Category getCategoryEntityById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category not found with id: " + id));
    }
}
