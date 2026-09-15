package br.edu.ifsp.orderflow.events;

/**
 * Evento que será publicado quando um pagamento for aprovado.
 *
 * @param pedidoId
 * @param transacaoId
 * @param ocorridoEm
 */
import java.time.Instant;

public record PagamentoAprovado(String pedidoId, String transacaoId, Instant ocorridoEm) implements IDomainEvent {}