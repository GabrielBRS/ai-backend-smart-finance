package org.sgt.ai.backend.smartfinance.aibackendsmartfinance.modules.analise.risco.transacao.service;

import java.util.UUID;

public interface ScoreRiscoPort {
    int calcular(UUID transacaoId);   // 0..1000, determinístico e auditável
}