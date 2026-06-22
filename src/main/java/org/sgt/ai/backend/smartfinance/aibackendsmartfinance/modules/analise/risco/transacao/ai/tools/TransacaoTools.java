package org.sgt.ai.backend.smartfinance.aibackendsmartfinance.modules.analise.risco.transacao.ai.tools;

import lombok.RequiredArgsConstructor;
import org.sgt.ai.backend.smartfinance.aibackendsmartfinance.domain.entity.Transacao;
import org.sgt.ai.backend.smartfinance.aibackendsmartfinance.modules.analise.risco.transacao.repository.TransacaoRepository;
import org.sgt.ai.backend.smartfinance.aibackendsmartfinance.modules.analise.risco.transacao.service.ScoreRiscoPort;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class TransacaoTools {

    private final TransacaoRepository transacaoRepository;
    private final ScoreRiscoPort scoreRiscoPort;

    @Tool(description = "Busca os dados de uma transação pelo seu identificador (UUID).")
    public Transacao buscarTransacao(@ToolParam(description = "UUID da transação") String id) {
        return transacaoRepository.findById(UUID.fromString(id)).orElse(null);
    }

    @Tool(description = "Histórico recente de transações de uma conta, da mais nova para a mais antiga.")
    public List<Transacao> historicoDaConta(
            @ToolParam(description = "número da conta") String conta,
            @ToolParam(description = "máximo de registros") int limite) {
        return transacaoRepository.historicoPorConta(conta, Math.min(limite, 50));
    }

    @Tool(description = "Score de risco DETERMINÍSTICO (0–1000) calculado no motor CUDA. "
            + "Use SEMPRE este número como oficial; nunca estime de cabeça.")
    public int scoreDeterministico(@ToolParam(description = "UUID da transação") String id) {
        return scoreRiscoPort.calcular(UUID.fromString(id));
    }
}
