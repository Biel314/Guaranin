package br.edu.fatec.tcc.guaranin.service.usuario

import br.edu.fatec.tcc.guaranin.dto.usuario.UsuarioCreateDTO
import br.edu.fatec.tcc.guaranin.dto.usuario.UsuarioResponseDTO
import br.edu.fatec.tcc.guaranin.dto.usuario.UsuarioUpdateDTO
import br.edu.fatec.tcc.guaranin.exception.usuario.UsuarioNotFoundException
import br.edu.fatec.tcc.guaranin.mapper.usuario.UsuarioMapper
import br.edu.fatec.tcc.guaranin.model.Usuario
import br.edu.fatec.tcc.guaranin.repository.usuario.UsuarioRepository
import br.edu.fatec.tcc.guaranin.validation.usuario.ValidApelido
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.validation.annotation.Validated
import java.util.*

/**
 * Serviço de regras de negócio para gerenciamento de [Usuario] (CRUD).
 *
 * Orquestra persistência, mapeamento de DTOs e validações de domínio. A
 * anotação [Validated] habilita a validação de restrições declaradas nos
 * parâmetros dos métodos, como [ValidApelido].
 *
 * @property usuarioRepository repositório de persistência de usuários.
 * @property usuarioMapper mapper entre entidade e DTOs.
 * @see Usuario
 * @see UsuarioRepository
 * @see UsuarioMapper
 */
@Validated
@Service
class UsuarioService(
    private val usuarioRepository: UsuarioRepository,
    private val usuarioMapper: UsuarioMapper,
    //private val passwordEncoder: Encoder
) {

    /**
     * Busca um usuário pelo endereço de e-mail.
     *
     * @param email E-mail cadastrado do usuário.
     * @return [UsuarioResponseDTO] correspondente.
     * @throws UsuarioNotFoundException se nenhum usuário for encontrado com o e-mail informado.
     */
    @Transactional(readOnly = true)
    fun findByEmail(email: String): UsuarioResponseDTO {
        return usuarioMapper.toResponseDTO(
            usuarioRepository.findByEmail(email)
                .orElseThrow { UsuarioNotFoundException() }
        )
    }

    /**
     * Lista todos os usuários cadastrados de forma paginada.
     *
     * @param pageable Parâmetros de paginação e ordenação.
     * @return Página contendo [UsuarioResponseDTO].
     */
    @Transactional(readOnly = true)
    fun findAll(pageable: Pageable): Page<UsuarioResponseDTO> {
        return usuarioRepository.findAll(pageable)
            .map { usuarioMapper.toResponseDTO(it) }
    }

    /**
     * Cadastra um novo usuário no sistema.
     *
     * @param usuarioCreateDTO DTO com os dados de criação ([UsuarioCreateDTO]).
     * @return [UsuarioResponseDTO] do usuário recém-criado.
     */
    @Transactional
    fun create(usuarioCreateDTO: UsuarioCreateDTO): UsuarioResponseDTO {
        val usuario = usuarioMapper.toEntity(usuarioCreateDTO)

        val usuarioSalvo = usuarioRepository.save(usuario)

        //TODO Define password criptography
        //usuario.password = passwordEncoder.encode(usuario.password);
        return usuarioMapper.toResponseDTO(usuarioSalvo)
    }

    /**
     * Atualiza os dados de um usuário existente.
     *
     * @param id Identificador único ([UUID]) do usuário a ser atualizado.
     * @param usuarioUpdateDTO DTO com os dados de atualização ([UsuarioUpdateDTO]).
     * @return [UsuarioResponseDTO] do usuário atualizado.
     * @throws UsuarioNotFoundException se o usuário não for encontrado no banco de dados.
     */
    @Transactional
    fun update(id: UUID, usuarioUpdateDTO: UsuarioUpdateDTO): UsuarioResponseDTO {
        val usuario = usuarioRepository.findById(id)
            .orElseThrow { UsuarioNotFoundException() }
        usuarioMapper.updateEntityFromDTO(usuarioUpdateDTO, usuario)

        if (!usuarioUpdateDTO.password.isNullOrEmpty()) {
            //TODO Define password criptography
            // usuario.password = passwordEncoder.encode(usuarioUpdateDTO.password)
        }
        return usuarioMapper.toResponseDTO(
            usuarioRepository.save(usuario)
        )
    }

    /**
     * Atualiza apenas o apelido de um usuário existente.
     *
     * O apelido é validado por [ValidApelido], que aplica o padrão
     * [br.edu.fatec.tcc.guaranin.validation.RegexPatterns.USUARIO] e rejeita
     * valores nulos, em branco ou fora do formato permitido.
     *
     * @param id identificador único do usuário.
     * @param apelido novo apelido, com 3 a 26 caracteres.
     * @return DTO do usuário após a atualização.
     * @throws jakarta.validation.ConstraintViolationException quando o apelido
     * não atende às regras de validação.
     * @throws UsuarioNotFoundException quando o usuário não existe.
     */
    @Transactional
    fun updateApelido(
        id: UUID,
        @ValidApelido
        apelido: String
    ): UsuarioResponseDTO {
        val usuario = usuarioRepository.findById(id)
            .orElseThrow { UsuarioNotFoundException() }
        usuario.apelido = apelido
        return usuarioMapper.toResponseDTO(
            usuarioRepository.save(usuario)
        )
    }

    /**
     * Remove um usuário do sistema pelo seu identificador único.
     *
     * @param id Identificador único ([UUID]) do usuário.
     * @return [UsuarioResponseDTO] do usuário removido.
     * @throws UsuarioNotFoundException se o usuário não for encontrado.
     */
    @Transactional
    fun delete(id: UUID): UsuarioResponseDTO {
        val usuario = usuarioRepository.findById(id)
            .orElseThrow { UsuarioNotFoundException() }
        usuarioRepository.delete(usuario)
        return usuarioMapper.toResponseDTO(usuario)
    }

    /**
     * Busca um usuário pelo seu identificador único.
     *
     * @param id Identificador único ([UUID]) do usuário.
     * @return [UsuarioResponseDTO] correspondente.
     * @throws UsuarioNotFoundException se o usuário não for encontrado.
     */
    @Transactional(readOnly = true)
    fun findById(id: UUID): UsuarioResponseDTO {
        val usuario = usuarioRepository.findById(id)
            .orElseThrow { UsuarioNotFoundException() }
        return usuarioMapper.toResponseDTO(usuario)
    }

    /**
     * Busca um usuário pelo ID e o converte para [UsuarioUpdateDTO] para fins de edição no formulário.
     *
     * @param id Identificador único do usuário ([UUID]).
     * @return DTO contendo os dados atuais do usuário para atualização.
     * @throws UsuarioNotFoundException se o usuário não for encontrado.
     */
    @Transactional(readOnly = true)
    fun findByIdForUpdate(id: UUID): UsuarioUpdateDTO {
        val usuario = usuarioRepository.findById(id)
            .orElseThrow { UsuarioNotFoundException() }
        return usuarioMapper.toUpdateDTO(usuario)
    }

    /**
     * Converte um [UsuarioUpdateDTO] em uma entidade [Usuario].
     *
     * @param usuarioUpdateDTO DTO de atualização.
     * @return Entidade [Usuario] mapeada.
     */
    fun toUsuario(usuarioUpdateDTO: UsuarioUpdateDTO): Usuario {
        return usuarioMapper.toEntity(usuarioUpdateDTO)
    }

    /**
     * Converte um [UsuarioCreateDTO] em uma entidade [Usuario].
     *
     * @param usuarioCreateDTO DTO de criação.
     * @return Entidade [Usuario] mapeada.
     */
    fun toUsuario(usuarioCreateDTO: UsuarioCreateDTO): Usuario {
        return usuarioMapper.toEntity(usuarioCreateDTO)
    }
}
