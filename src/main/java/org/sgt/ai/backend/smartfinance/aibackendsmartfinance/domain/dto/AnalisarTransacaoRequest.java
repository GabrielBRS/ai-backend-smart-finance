package org.sgt.ai.backend.smartfinance.aibackendsmartfinance.domain.dto;

import org.sgt.ai.backend.smartfinance.aibackendsmartfinance.domain.vo.Canal;
import org.sgt.ai.backend.smartfinance.aibackendsmartfinance.domain.vo.TipoTransacao;

import java.math.BigDecimal;

public record AnalisarTransacaoRequest(
        String contaOrigem, String contaDestino, BigDecimal valor, String moeda,
        TipoTransacao tipo, Canal canal, String descricao, String tenantId) {}
