package org.sgt.ai.backend.smartfinance.aibackendsmartfinance.domain.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Table;
import org.sgt.ai.backend.smartfinance.aibackendsmartfinance.domain.vo.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Table("transacao")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Transacao {
    @Id
    private UUID id;
    @Version
    private Long version;

    private String contaOrigem;
    private String contaDestino;
    private BigDecimal valor;
    private String moeda;
    private TipoTransacao tipo;
    private Canal canal;
    private Instant dataHora;
    private String descricao;
    private String tenantId;

    // resultado (preenchido após o scoring)
    private Integer scoreRisco;
    private NivelRisco nivelRisco;
    private String parecer;
}