package org.sgt.ai.backend.smartfinance.aibackendsmartfinance.modules.analise.risco.transacao.api;

import lombok.RequiredArgsConstructor;
import org.sgt.ai.backend.smartfinance.aibackendsmartfinance.domain.dto.*;
import org.sgt.ai.backend.smartfinance.aibackendsmartfinance.modules.analise.risco.transacao.ai.document_ingestion.TransacaoDocumentIngestionService;
import org.sgt.ai.backend.smartfinance.aibackendsmartfinance.modules.analise.risco.transacao.ai.assistant.TransacaoAssistantService;
import org.sgt.ai.backend.smartfinance.aibackendsmartfinance.modules.analise.risco.transacao.service.TransacaoService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/transacoes")
@RequiredArgsConstructor
public class TransacaoInboundController {

    private final TransacaoService transacaoService;
    private final TransacaoAssistantService assistant;
    private final TransacaoDocumentIngestionService ingestion;

    @PostMapping("/analisar")
    public AnaliseRiscoResponse analisar(@RequestBody AnalisarTransacaoRequest req) {
        return transacaoService.analisar(req);
    }

    @PostMapping("/{conversationId}/perguntar")
    public Map<String, String> perguntar(@PathVariable String conversationId,
                                         @RequestParam String tenantId,
                                         @RequestBody Map<String, String> body) {
        return Map.of("resposta", assistant.perguntar(conversationId, tenantId, body.get("pergunta")));
    }

    @PostMapping(value = "/documentos", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Map<String, Integer> ingerir(@RequestParam("arquivo") MultipartFile arquivo,
                                        @RequestParam String tenantId) throws IOException {
        return Map.of("chunksIndexados", ingestion.ingerir(arquivo.getResource(), tenantId));
    }
}
