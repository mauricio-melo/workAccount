package com.workAccount.service;

import com.workAccount.config.InssConfig;
import com.workAccount.config.IrrfConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor(onConstructor = @__({@Lazy}))
public class TributesService {
    private final InssConfig inssConfig;
    private final IrrfConfig irrfConfig;

    public BigDecimal calculateInss(final BigDecimal salary) {
        return this.inssConfig.getValues().stream()
                .filter(inss -> salary.compareTo(inss.getSalary()) <= 0)
                .map(inss -> salary.multiply(inss.getPercent()).setScale(2, BigDecimal.ROUND_HALF_EVEN)).findFirst()
                .orElseGet(() -> this.inssConfig.getHigh().setScale(2, BigDecimal.ROUND_HALF_EVEN));
    }

    public BigDecimal calculateIrrf(final BigDecimal salary) {
        if (salary.compareTo(this.irrfConfig.getLow()) >= 0) {
            return this.irrfConfig.getValues().stream()
                    .filter(irrf -> salary.compareTo(irrf.getSalary()) <= 0)
                    .map(irrf -> salary.multiply(irrf.getPercent()).subtract(irrf.getSubtract()).setScale(2, BigDecimal.ROUND_HALF_EVEN)).findFirst()
                    .orElseGet(() -> salary.multiply(this.irrfConfig.getHigh()).subtract(this.irrfConfig.getSubtract()).setScale(2, BigDecimal.ROUND_HALF_EVEN));
        } else {
            return BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_EVEN);
        }
    }
}
