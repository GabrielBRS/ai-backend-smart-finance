package org.sgt.ai.backend.smartfinance.aibackendsmartfinance.modules.analise.risco.transacao.ai.document_ingestion;

import lombok.RequiredArgsConstructor;
import org.sgt.ai.backend.smartfinance.aibackendsmartfinance.modules.analise.risco.transacao.repository.TransacaoVectorRepository;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TransacaoDocumentIngestionService {

    private final TransacaoVectorRepository vectorRepository;

    public int ingerir(Resource arquivo, String tenantId) {
        List<Document> brutos = new TikaDocumentReader(arquivo).get();
        List<Document> chunks = new TokenTextSplitter().apply(brutos);

        List<Document> enriquecidos = chunks.stream()
                .map(d -> Document.builder()
                        .text(d.getText())
                        .metadata(Map.of(
                                "namespace", TransacaoVectorRepository.NAMESPACE,
                                "tenant_id", tenantId,
                                "fonte", String.valueOf(arquivo.getFilename())))
                        .build())
                .toList();

        vectorRepository.indexar(enriquecidos);
        return enriquecidos.size();
    }
}
