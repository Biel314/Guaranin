package br.edu.fatec.tcc.guaranin.repository.usuario

import br.edu.fatec.tcc.guaranin.model.Usuario
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*


/**
 * Repositório JPA para a entidade [Usuario], com chave primária [UUID].
 *
 * Estende [JpaRepository], herdando as operações CRUD padrão, e declara
 * consultas derivadas por e-mail e apelido. A anotação [Repository] marca a
 * interface como bean gerenciado pelo Spring.
 *
 * @see Usuario
 */
@Repository
interface UsuarioRepository : JpaRepository<Usuario, UUID> {

    /**
     * Busca um usuário pelo e-mail, que também é usado como login.
     *
     * O e-mail é único na entidade [Usuario], portanto no máximo um registro
     * pode corresponder à busca.
     *
     * @param email e-mail/login do usuário a ser localizado.
     * @return [Optional] com o usuário encontrado ou vazio quando não existe
     * usuário com o e-mail informado.
     */
    fun findByEmail(email: String): Optional<Usuario>

    /**
     * Busca um usuário pelo apelido.
     *
     * O apelido não possui restrição de unicidade no modelo atual. Caso
     * existam registros duplicados, a consulta pode falhar por resultado não
     * único.
     *
     * @param apelido apelido do usuário a ser localizado.
     * @return [Optional] com o usuário encontrado ou vazio quando nenhum
     * usuário possui o apelido informado.
     */
    fun findByApelido(apelido: String): Optional<Usuario>
}
