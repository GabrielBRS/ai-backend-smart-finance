package org.sgt.ai.backend.smartfinance.aibackendsmartfinance.modules.analise.risco.transacao.service;

import lombok.RequiredArgsConstructor;
import org.sgt.ai.backend.smartfinance.aibackendsmartfinance.domain.dto.*;
import org.sgt.ai.backend.smartfinance.aibackendsmartfinance.domain.entity.Transacao;
import org.sgt.ai.backend.smartfinance.aibackendsmartfinance.domain.vo.NivelRisco;
import org.sgt.ai.backend.smartfinance.aibackendsmartfinance.modules.analise.risco.transacao.ai.assistant.TransacaoAssistantService;
import org.sgt.ai.backend.smartfinance.aibackendsmartfinance.modules.analise.risco.transacao.repository.TransacaoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransacaoService {

    private final TransacaoRepository repository;
    private final ScoreRiscoPort scoreRiscoPort;
    private final TransacaoAssistantService assistant;

    @Transactional
    public AnaliseRiscoResponse analisar(AnalisarTransacaoRequest req) {
        Transacao t = Transacao.builder()
                .id(UUID.randomUUID())
                .contaOrigem(req.contaOrigem()).contaDestino(req.contaDestino())
                .valor(req.valor()).moeda(req.moeda() == null ? "BRL" : req.moeda())
                .tipo(req.tipo()).canal(req.canal())
                .descricao(req.descricao()).dataHora(Instant.now())
                .tenantId(req.tenantId())
                .build();
        repository.save(t);                       // INSERT (version null)

        int score = scoreRiscoPort.calcular(t.getId());   // plano determinístico
        NivelRisco nivel = classificar(score);
        String parecer = assistant.gerarParecer(t, score, nivel);

        t.setScoreRisco(score);
        t.setNivelRisco(nivel);
        t.setParecer(parecer);
        repository.save(t);                       // UPDATE (version setado)

        return new AnaliseRiscoResponse(t.getId(), score, nivel, parecer);
    }

    private NivelRisco classificar(int score) {
        if (score >= 850) return NivelRisco.CRITICO;
        if (score >= 650) return NivelRisco.ALTO;
        if (score >= 350) return NivelRisco.MEDIO;
        return NivelRisco.BAIXO;
    }
}
