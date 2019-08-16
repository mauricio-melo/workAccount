package com.workAccount.service;

import com.workAccount.dto.SalaryResultDTO;
import com.workAccount.dto.TributesDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__({@Lazy}))
public class SalaryService {
    private final TributesService tributesService;

    public SalaryResultDTO calculateSalary(final BigDecimal salary) {
        final BigDecimal inss = this.tributesService.calculateInss(salary);
        final BigDecimal irrf = this.tributesService.calculateIrrf(salary.subtract(inss));
        return SalaryResultDTO.builder()
                .salary(salary)
                .tributes(TributesDTO.builder()
                        .inss(inss)
                        .irrf(irrf)
                        .build())
                .liquidSalary(this.calculateLiquidSalary(salary, Arrays.asList(inss, irrf), new ArrayList<>()))
                .build();
    }

    private BigDecimal calculateLiquidSalary(final BigDecimal salary, final List<BigDecimal> discounts, final List<BigDecimal> additions) {
        return salary.subtract(discounts.stream()
                             .reduce(BigDecimal::add)
                             .orElse(BigDecimal.ZERO))
                     .add(additions.stream()
                             .reduce(BigDecimal::add)
                             .orElse(BigDecimal.ZERO))
                     .setScale(2, BigDecimal.ROUND_HALF_EVEN);
    }
}
