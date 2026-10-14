package br.edu.fatec.tcc.guaranin.repository

import br.edu.fatec.tcc.guaranin.model.Estado
import br.edu.fatec.tcc.guaranin.model.Mascote
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*


/**
 * Repositório de persistência para a entidade [Mascote].
 *
 *
 * Estende [JpaRepository], herdando automaticamente as operações
 * de CRUD padrão (salvar, buscar por id, listar, deletar, etc.), sem
 * necessidade de implementação manual.
 *
 *
 * A anotação [Repository] marca esta interface como um bean
 * gerenciado pelo Spring, permitindo sua injeção via construtor em
 * services e outros componentes.
 */
@Repository
interface MascoteRepository : JpaRepository<Mascote, UUID> {


    fun findByNome(nome: String?): Optional<Mascote>

    fun findByUsuarioId(usuarioId: UUID): Optional<Mascote>

    fun findAllByUsuarioId(usuarioId: UUID): List<Mascote>

    fun findByUsuarioIdAndEstado(usuarioId: UUID, estado: Estado): Optional<Mascote>
    fun existsByUsuarioIdAndEstadoNot(usuarioId: UUID, morto: Estado): Boolean
}
