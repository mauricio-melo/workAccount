package com.workAccount.config;

import com.workAccount.dto.TaxationDTO;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.List;

@Configuration
@Data
@ConfigurationProperties("taxations.irrf")
public class IrrfConfig {
    private BigDecimal low;
    private List<TaxationDTO> values;
    private BigDecimal high;
    private BigDecimal subtract;
}