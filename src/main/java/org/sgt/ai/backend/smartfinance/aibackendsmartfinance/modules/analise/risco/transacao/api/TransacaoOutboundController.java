package org.sgt.ai.backend.smartfinance.aibackendsmartfinance.modules.analise.risco.transacao.api;

import lombok.RequiredArgsConstructor;
import org.sgt.ai.backend.smartfinance.aibackendsmartfinance.domain.dto.AnaliseRiscoResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class TransacaoOutboundController {

    private final RestClient.Builder restClientBuilder;

    public void notificarResultado(AnaliseRiscoResponse resultado, String webhookUrl) {
        restClientBuilder.build().post()
                .uri(webhookUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .body(resultado)
                .retrieve()
                .toBodilessEntity();
    }
}
