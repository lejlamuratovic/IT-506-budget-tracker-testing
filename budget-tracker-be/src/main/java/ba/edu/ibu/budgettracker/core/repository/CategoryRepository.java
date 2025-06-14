package ba.edu.ibu.budgettracker.core.repository;

import ba.edu.ibu.budgettracker.core.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
