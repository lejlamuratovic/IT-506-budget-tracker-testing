package ba.edu.ibu.budgettracker.core.service;

import ba.edu.ibu.budgettracker.core.model.Budget;
import ba.edu.ibu.budgettracker.core.model.Expense;
import ba.edu.ibu.budgettracker.core.model.User;
import ba.edu.ibu.budgettracker.core.repository.BudgetRepository;
import ba.edu.ibu.budgettracker.core.repository.ExpenseRepository;
import ba.edu.ibu.budgettracker.rest.dto.BudgetDto;
import ba.edu.ibu.budgettracker.rest.dto.BudgetRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BudgetService {
    private final BudgetRepository budgetRepository;
    private final ExpenseRepository expenseRepository;
    private final UserService userService;

    public BudgetService(BudgetRepository budgetRepository, ExpenseRepository expenseRepository, UserService userService) {
        this.budgetRepository = budgetRepository;
        this.expenseRepository = expenseRepository;
        this.userService = userService;
    }

    public BudgetDto createBudget(BudgetRequest request) {
        User user = userService.getUserEntityById(request.getUserId());
        Budget budget = new Budget(
                request.getAmount(),
                request.getAmount(),
                request.getMonth(),
                request.getYear(),
                user
        );
        Budget savedBudget = budgetRepository.save(budget);
        return new BudgetDto(
                savedBudget.getId(),
                savedBudget.getAmount(),
                savedBudget.getRemaining(),
                savedBudget.getMonth(),
                savedBudget.getYear(),
                savedBudget.getUser().getId()
        );
    }

    public Optional<Budget> getBudgetByUserAndMonthAndYear(Long userId, int month, int year) {
        return budgetRepository.findByUserIdAndMonthAndYear(userId, month, year);
    }

    public BudgetDto updateBudget(Long id, BudgetRequest request) {
        return budgetRepository.findById(id)
                .map(budget -> {
                    User user = userService.getUserEntityById(request.getUserId());

                    double totalExpenses = expenseRepository.findByUserIdAndMonthAndYear(
                                    budget.getUser().getId(),
                                    budget.getMonth(),
                                    budget.getYear()
                            ).stream()
                            .mapToDouble(Expense::getAmount)
                            .sum();

                    budget.setAmount(request.getAmount());
                    budget.setRemaining(request.getAmount() - totalExpenses); // Adjust remaining based on total expenses
                    budget.setMonth(request.getMonth());
                    budget.setYear(request.getYear());
                    budget.setUser(user);

                    Budget updatedBudget = budgetRepository.save(budget);

                    return new BudgetDto(
                            updatedBudget.getId(),
                            updatedBudget.getAmount(),
                            updatedBudget.getRemaining(),
                            updatedBudget.getMonth(),
                            updatedBudget.getYear(),
                            updatedBudget.getUser().getId()
                    );
                })
                .orElseThrow(() -> new IllegalArgumentException("Budget not found with id: " + id));
    }

    public void deleteBudget(Long id) {
        if (!budgetRepository.existsById(id)) {
            throw new IllegalArgumentException("Budget not found with id: " + id);
        }
        budgetRepository.deleteById(id);
    }
}
