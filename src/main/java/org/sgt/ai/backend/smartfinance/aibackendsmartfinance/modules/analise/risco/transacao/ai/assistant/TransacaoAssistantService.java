package org.sgt.ai.backend.smartfinance.aibackendsmartfinance.modules.analise.risco.transacao.ai.assistant;

import org.sgt.ai.backend.smartfinance.aibackendsmartfinance.domain.entity.Transacao;
import org.sgt.ai.backend.smartfinance.aibackendsmartfinance.domain.vo.NivelRisco;
import org.sgt.ai.backend.smartfinance.aibackendsmartfinance.modules.analise.risco.transacao.ai.tools.TransacaoTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

@Service
public class TransacaoAssistantService {

    private final ChatClient chatClient;

    public TransacaoAssistantService(ChatClient.Builder builder,
                                     ChatMemory chatMemory,
                                     VectorStore vectorStore,
                                     TransacaoTools transacaoTools) {
        this.chatClient = builder
                .defaultSystem("""
                        Você é um analista de risco de transações do Sicoob.
                        Responda em português, objetivo e fundamentado nas normas e no
                        histórico presentes no contexto. NUNCA invente valores numéricos:
                        para qualquer score, use a ferramenta de cálculo determinístico.
                        """)
                .defaultAdvisors(
                        MessageChatMemoryAdvisor.builder(chatMemory).build(),
                        QuestionAnswerAdvisor.builder(vectorStore)
                                .searchRequest(SearchRequest.builder().topK(6).similarityThreshold(0.6).build())
                                .build())
                .defaultTools(transacaoTools)
                .build();
    }

    /** Narra o parecer a partir do score já calculado — o modelo explica, não recalcula. */
    public String gerarParecer(Transacao t, int score, NivelRisco nivel) {
        return chatClient.prompt()
                .user(u -> u.text("""
                        Gere um parecer de risco para a transação {id}.
                        Score determinístico: {score} (nível {nivel}).
                        Valor: {valor} {moeda}, tipo {tipo}, canal {canal}.
                        Aponte os principais fatores de risco com base nas normas do contexto.
                        """)
                        .param("id", t.getId()).param("score", score).param("nivel", nivel)
                        .param("valor", t.getValor()).param("moeda", t.getMoeda())
                        .param("tipo", t.getTipo()).param("canal", t.getCanal()))
                .call().content();
    }

    /** Q&A interativo, com memória por conversa e filtro de RAG por tenant. */
    public String perguntar(String conversationId, String tenantId, String pergunta) {
        return chatClient.prompt()
                .advisors(a -> a
                        .param(ChatMemory.CONVERSATION_ID, conversationId)
                        .param(QuestionAnswerAdvisor.FILTER_EXPRESSION,
                                "namespace == 'transacao' && tenant_id == '" + tenantId + "'"))
                .user(pergunta)
                .call().content();
    }
}
