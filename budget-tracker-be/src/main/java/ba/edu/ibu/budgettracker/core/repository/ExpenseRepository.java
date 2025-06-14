package ba.edu.ibu.budgettracker.core.repository;

import ba.edu.ibu.budgettracker.core.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByUserId(Long userId);

    @Query("SELECT e FROM expenses e WHERE e.user.id = :userId AND FUNCTION('MONTH', e.date) = :month AND FUNCTION('YEAR', e.date) = :year")
    List<Expense> findByUserIdAndMonthAndYear(
            @Param("userId") Long userId,
            @Param("month") int month,
            @Param("year") int year
    );
}
