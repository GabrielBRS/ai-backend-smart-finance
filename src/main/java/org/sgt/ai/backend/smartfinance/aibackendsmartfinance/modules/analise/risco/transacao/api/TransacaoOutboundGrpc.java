package org.sgt.ai.backend.smartfinance.aibackendsmartfinance.modules.analise.risco.transacao.api;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import jakarta.annotation.PreDestroy;
import org.sgt.ai.backend.smartfinance.aibackendsmartfinance.modules.analise.risco.transacao.service.ScoreRiscoPort;
import org.sgt.score.v1.ScoreRequest;                 // gerado
import org.sgt.score.v1.ScoreResponse;                // gerado
import org.sgt.score.v1.TransacaoScoreServiceGrpc;    // gerado
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class TransacaoOutboundGrpc implements ScoreRiscoPort {

    private final ManagedChannel channel;
    private final TransacaoScoreServiceGrpc.TransacaoScoreServiceBlockingStub stub;

    public TransacaoOutboundGrpc(@Value("${sgt.score.grpc.target:192.168.15.202:50051}") String target) {
        this.channel = ManagedChannelBuilder.forTarget(target).usePlaintext().build();  // mTLS depois
        this.stub = TransacaoScoreServiceGrpc.newBlockingStub(channel);
    }

    @Override
    public int calcular(UUID transacaoId) {
        ScoreResponse r = stub.calcularScore(
                ScoreRequest.newBuilder().setTransacaoId(transacaoId.toString()).build());
        return r.getScore();
    }

    @PreDestroy
    void shutdown() { channel.shutdownNow(); }
}
