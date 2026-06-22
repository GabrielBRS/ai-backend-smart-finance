package org.sgt.ai.backend.smartfinance.aibackendsmartfinance.domain.dto;

import org.sgt.ai.backend.smartfinance.aibackendsmartfinance.domain.vo.NivelRisco;

import java.util.UUID;

public record AnaliseRiscoResponse(
        UUID transacaoId, int scoreRisco, NivelRisco nivelRisco, String parecer) {}