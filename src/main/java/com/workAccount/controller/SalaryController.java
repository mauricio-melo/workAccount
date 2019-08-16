package com.workAccount.controller;

import com.workAccount.dto.SalaryResultDTO;
import com.workAccount.service.SalaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.math.BigDecimal;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(SalaryController.SALARY_ENDPOINT)
public class SalaryController {
    public static final String SALARY_ENDPOINT = "/salary";
    private final SalaryService service;

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<SalaryResultDTO> getSalary(@Valid @RequestParam(value = "salary") final BigDecimal salary) {
        return ResponseEntity.ok(this.service.calculateSalary(salary.setScale(2, BigDecimal.ROUND_HALF_EVEN)));
    }


}
