package ba.edu.ibu.budgettracker.rest.controller;

import ba.edu.ibu.budgettracker.core.model.Budget;
import ba.edu.ibu.budgettracker.core.service.BudgetService;
import ba.edu.ibu.budgettracker.rest.dto.BudgetDto;
import ba.edu.ibu.budgettracker.rest.dto.BudgetRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/budgets")
public class BudgetController {
    private final BudgetService budgetService;

    public BudgetController(BudgetService budgetService) {
        this.budgetService = budgetService;
    }

    @GetMapping("/user")
    public ResponseEntity<BudgetDto> getBudget(
            @RequestParam Long userId,
            @RequestParam Integer month,
            @RequestParam Integer year
    ) {
        Optional<Budget> budgetOptional = budgetService.getBudgetByUserAndMonthAndYear(userId, month, year);

        return budgetOptional
                .map(budget -> {
                    BudgetDto budgetDto = new BudgetDto(
                            budget.getId(),
                            budget.getAmount(),
                            budget.getRemaining(),
                            budget.getMonth(),
                            budget.getYear(),
                            budget.getUser().getId()
                    );
                    return ResponseEntity.ok(budgetDto);
                })
                .orElse(ResponseEntity.ok(null)); // Return null if no budget exists
    }

    @PostMapping
    public ResponseEntity<BudgetDto> createBudget(@RequestBody BudgetRequest request) {
        BudgetDto createdBudget = budgetService.createBudget(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBudget);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BudgetDto> updateBudget(@PathVariable Long id, @RequestBody BudgetRequest request) {
        try {
            BudgetDto updatedBudget = budgetService.updateBudget(id, request);
            return ResponseEntity.ok(updatedBudget);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBudget(@PathVariable Long id) {
        try {
            budgetService.deleteBudget(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
