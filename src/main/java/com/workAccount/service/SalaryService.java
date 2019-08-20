package com.workAccount.service;

import com.workAccount.dto.TributeDTO;
import com.workAccount.dto.SalaryResultDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.workAccount.constant.Tributes.INSS;
import static com.workAccount.constant.Tributes.IRRF;

@Service
@RequiredArgsConstructor(onConstructor = @__({@Lazy}))
public class SalaryService {
    private final TributesService tributesService;

    public SalaryResultDTO calculateSalary(final BigDecimal salary) {
        final TributeDTO inss = TributeDTO.builder()
                .name(INSS)
                .value(this.tributesService.calculateInss(salary))
                .build();

        final TributeDTO irrf = TributeDTO.builder()
                .name(IRRF)
                .value(this.tributesService.calculateIrrf(salary.subtract(inss.getValue())))
                .build();

        return SalaryResultDTO.builder()
                .salary(salary)
                .tributes(Arrays.asList(inss, irrf))
                .liquidSalary(this.calculateLiquidSalary(salary, Arrays.asList(inss.getValue(), irrf.getValue()), new ArrayList<>()))
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
