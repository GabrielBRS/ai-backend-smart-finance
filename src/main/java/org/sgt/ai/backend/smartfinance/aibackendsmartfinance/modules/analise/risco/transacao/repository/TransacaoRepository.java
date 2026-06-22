package org.sgt.ai.backend.smartfinance.aibackendsmartfinance.modules.analise.risco.transacao.repository;

import org.sgt.ai.backend.smartfinance.aibackendsmartfinance.domain.entity.Transacao;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface TransacaoRepository extends ListCrudRepository<Transacao, UUID> {

    @Query("""
        SELECT * FROM transacao
        WHERE conta_origem = :conta OR conta_destino = :conta
        ORDER BY data_hora DESC
        LIMIT :limite
        """)
    List<Transacao> historicoPorConta(@Param("conta") String conta, @Param("limite") int limite);
}