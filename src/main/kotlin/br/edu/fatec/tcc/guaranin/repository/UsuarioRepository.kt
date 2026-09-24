package br.edu.fatec.tcc.guaranin.repository

import br.edu.fatec.tcc.guaranin.model.Usuario
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*


/**
 * Repositório de persistência para a entidade [Usuario].
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
interface UsuarioRepository : JpaRepository<Usuario, Long> {
    /**
     * Busca um usuário pelo seu login (e-mail).
     *
     *
     * Como o campo `login` é definido como único na entidade
     * [Usuario], no máximo um registro pode corresponder à busca.
     *
     * @param login e-mail/login do usuário a ser localizado.
     * @return um [Optional] contendo o usuário encontrado, ou vazio
     * caso nenhum usuário possua o login informado.
     */
    fun findByLogin(login: String?): Optional<Usuario?>?
}
