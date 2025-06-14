package ba.edu.ibu.budgettracker.rest.controller;

import ba.edu.ibu.budgettracker.core.service.ExpenseService;
import ba.edu.ibu.budgettracker.rest.dto.CategoryChartDto;
import ba.edu.ibu.budgettracker.rest.dto.DailyExpenseDto;
import ba.edu.ibu.budgettracker.rest.dto.ExpenseDto;
import ba.edu.ibu.budgettracker.rest.dto.ExpenseRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {
    private final ExpenseService expenseService;


    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @GetMapping("/filter")
    public ResponseEntity<List<ExpenseDto>> filterExpenses(
            @RequestParam Long userId,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate,
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Double minAmount,
            @RequestParam(required = false) Double maxAmount
    ) {
        List<ExpenseDto> filteredExpenses = expenseService.filterExpenses(
                userId,
                categoryId,
                startDate,
                endDate,
                month,
                year,
                minAmount,
                maxAmount
        );
        return ResponseEntity.ok(filteredExpenses);
    }

    @GetMapping("/daily-overview")
    public ResponseEntity<List<DailyExpenseDto>> getDailyExpenseOverview(
            @RequestParam Long userId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate
    ) {
        List<DailyExpenseDto> dailyOverview = expenseService.getDailyExpenseOverview(userId, startDate, endDate);
        return ResponseEntity.ok(dailyOverview);
    }

    @GetMapping("/chart-data")
    public ResponseEntity<List<CategoryChartDto>> getCategoryChartData(
            @RequestParam Long userId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate
    ) {
        List<CategoryChartDto> chartData = expenseService.getCategoryChartData(userId, startDate, endDate);
        return ResponseEntity.ok(chartData);
    }

    @PostMapping
    public ResponseEntity<ExpenseDto> createExpense(@RequestBody ExpenseRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(expenseService.createExpense(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExpenseDto> updateExpense(@PathVariable Long id, @RequestBody ExpenseRequest request) {
        try {
            ExpenseDto updatedExpense = expenseService.updateExpense(id, request);
            return ResponseEntity.ok(updatedExpense);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable Long id) {
        try {
            expenseService.deleteExpense(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
