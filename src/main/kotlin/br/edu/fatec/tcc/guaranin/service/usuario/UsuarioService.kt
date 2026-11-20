package br.edu.fatec.tcc.guaranin.service.usuario

import br.edu.fatec.tcc.guaranin.dto.usuario.UsuarioCreateDTO
import br.edu.fatec.tcc.guaranin.dto.usuario.UsuarioResponseDTO
import br.edu.fatec.tcc.guaranin.dto.usuario.UsuarioUpdateDTO
import br.edu.fatec.tcc.guaranin.exception.InvalidDTOException
import br.edu.fatec.tcc.guaranin.exception.security.PasswordEncodingException
import br.edu.fatec.tcc.guaranin.exception.usuario.UsuarioNotFoundException
import br.edu.fatec.tcc.guaranin.mapper.usuario.UsuarioMapper
import br.edu.fatec.tcc.guaranin.model.Usuario
import br.edu.fatec.tcc.guaranin.repository.usuario.UsuarioRepository
import br.edu.fatec.tcc.guaranin.security.SecurityService
import br.edu.fatec.tcc.guaranin.validation.usuario.ValidApelido
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.validation.annotation.Validated

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
    private val securityService: SecurityService,
    private val passwordEncoder: PasswordEncoder
) {


    /**
     * Cadastra um novo usuário no sistema.
     *
     * @param usuarioCreateDTO DTO com os dados de criação ([UsuarioCreateDTO]).
     * @return [UsuarioResponseDTO] do usuário recém-criado.
     */
    @Transactional
    fun create(usuarioCreateDTO: UsuarioCreateDTO): UsuarioResponseDTO {
        val usuario = usuarioMapper.toEntity(usuarioCreateDTO)

        if (usuario.password.isNullOrEmpty())
            throw InvalidDTOException()

        usuario.password = passwordEncoder.encode(usuario.password)
            ?: throw PasswordEncodingException()

        val usuarioSalvo = usuarioRepository.save(usuario)

        return usuarioMapper.toResponseDTO(usuarioSalvo)
    }

    /**
     * Atualiza os dados do usuário autenticado.
     *
     * @param usuarioUpdateDTO DTO com os dados de atualização ([UsuarioUpdateDTO]).
     * @return [UsuarioResponseDTO] do usuário atualizado.
     * @throws UsuarioNotFoundException se o usuário não for encontrado no banco de dados.
     */
    @Transactional
    fun update(usuarioUpdateDTO: UsuarioUpdateDTO): UsuarioResponseDTO {
        val usuario = usuarioRepository.findById(
            securityService.getUsuarioLogadoId()
        ).orElseThrow { UsuarioNotFoundException() }

        usuarioMapper.updateEntityFromDTO(usuarioUpdateDTO, usuario)

        if (!usuarioUpdateDTO.password.isNullOrEmpty()) {
            usuario.password = passwordEncoder.encode(usuarioUpdateDTO.password)
                ?: throw PasswordEncodingException()
        }
        return usuarioMapper.toResponseDTO(
            usuarioRepository.save(usuario)
        )
    }

    /**
     * Atualiza apenas o apelido do usuário autenticado.
     *
     * O apelido é validado por [ValidApelido], que aplica o padrão
     * [br.edu.fatec.tcc.guaranin.validation.RegexPatterns.USUARIO] e rejeita
     * valores nulos, em branco ou fora do formato permitido.
     *
     * @param apelido novo apelido, com 3 a 26 caracteres.
     * @return DTO do usuário após a atualização.
     * @throws jakarta.validation.ConstraintViolationException quando o apelido
     * não atende às regras de validação.
     * @throws UsuarioNotFoundException quando o usuário não existe.
     */
    @Transactional
    fun updateApelido(
        @ValidApelido
        apelido: String
    ): UsuarioResponseDTO {
        val usuario = usuarioRepository.findById(
            securityService.getUsuarioLogadoId()
        ).orElseThrow { UsuarioNotFoundException() }

        usuario.apelido = apelido
        return usuarioMapper.toResponseDTO(
            usuarioRepository.save(usuario)
        )
    }

    /**
     * Remove o usuário autenticado.
     *
     * @return [UsuarioResponseDTO] do usuário removido.
     * @throws UsuarioNotFoundException se o usuário não for encontrado.
     */
    @Transactional
    fun delete(): UsuarioResponseDTO {
        val usuario = usuarioRepository.findById(
            securityService.getUsuarioLogadoId()
        ).orElseThrow { UsuarioNotFoundException() }
        securityService.validarUsuario(usuario.id)
        usuarioRepository.delete(usuario)
        //TODO setup Logout
        //authorizationService.logoutUsuarioAutenticado()
        return usuarioMapper.toResponseDTO(usuario)
    }

    /**
     * Converte um [UsuarioUpdateDTO] em uma entidade [Usuario].
     *
     * @param usuarioUpdateDTO DTO de atualização.
     * @return Entidade [Usuario] mapeada.
     */
    fun toUsuario(usuarioUpdateDTO: UsuarioUpdateDTO): Usuario {
        val usuario: Usuario = usuarioMapper.toEntity(usuarioUpdateDTO)
        usuario.id = securityService.getUsuarioLogadoId()
        return usuario
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
