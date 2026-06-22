package org.sgt.ai.backend.smartfinance.aibackendsmartfinance.modules.analise.risco.transacao.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TransacaoVectorRepository {

    public static final String NAMESPACE = "transacao";
    private final VectorStore vectorStore;

    public void indexar(List<Document> documentos) {
        vectorStore.add(documentos);   // metadados já vêm preenchidos na ingestão
    }

    public List<Document> buscarSimilares(String consulta, String tenantId, int topK) {
        var b = new FilterExpressionBuilder();
        var filtro = b.and(
                b.eq("namespace", NAMESPACE),
                b.eq("tenant_id", tenantId)).build();
        return vectorStore.similaritySearch(SearchRequest.builder()
                .query(consulta)
                .topK(topK)
                .similarityThreshold(0.6)
                .filterExpression(filtro)
                .build());
    }
}