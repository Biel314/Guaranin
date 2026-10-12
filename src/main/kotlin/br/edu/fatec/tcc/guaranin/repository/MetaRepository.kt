package br.edu.fatec.tcc.guaranin.repository

import br.edu.fatec.tcc.guaranin.model.Meta
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

/**
 * Repositório de persistência para a entidade [Meta].
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
interface MetaRepository : JpaRepository<Meta, UUID> {

    fun findByTitulo(titulo: String): List<Meta>
}
