package br.edu.fatec.tcc.guaranin.repository;

import br.edu.fatec.tcc.guaranin.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositório de persistência para a entidade {@link Usuario}.
 *
 * <p>Estende {@link JpaRepository}, herdando automaticamente as operações
 * de CRUD padrão (salvar, buscar por id, listar, deletar, etc.), sem
 * necessidade de implementação manual.</p>
 *
 * <p>A anotação {@link Repository} marca esta interface como um bean
 * gerenciado pelo Spring, permitindo sua injeção via construtor em
 * services e outros componentes.</p>
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    /**
     * Busca um usuário pelo seu login (e-mail).
     *
     * <p>Como o campo {@code login} é definido como único na entidade
     * {@link Usuario}, no máximo um registro pode corresponder à busca.</p>
     *
     * @param login e-mail/login do usuário a ser localizado.
     * @return um {@link Optional} contendo o usuário encontrado, ou vazio
     * caso nenhum usuário possua o login informado.
     */
    Optional<Usuario> findByLogin(String login);
}