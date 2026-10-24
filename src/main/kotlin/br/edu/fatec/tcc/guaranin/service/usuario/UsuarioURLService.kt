package br.edu.fatec.tcc.guaranin.service.usuario

import br.edu.fatec.tcc.guaranin.dto.usuario.UsuarioURLCreateDTO
import br.edu.fatec.tcc.guaranin.dto.usuario.UsuarioURLResponseDTO
import br.edu.fatec.tcc.guaranin.dto.usuario.UsuarioURLUpdateDTO
import br.edu.fatec.tcc.guaranin.exception.urlmonitorada.URLMonitoradaNotFoundException
import br.edu.fatec.tcc.guaranin.exception.usuario.UsuarioNotFoundException
import br.edu.fatec.tcc.guaranin.exception.usuario.UsuarioURLNotFoundException
import br.edu.fatec.tcc.guaranin.mapper.usuario.UsuarioURLMapper
import br.edu.fatec.tcc.guaranin.model.UsuarioURL
import br.edu.fatec.tcc.guaranin.repository.urlmonitorada.URLMonitoradaRepository
import br.edu.fatec.tcc.guaranin.repository.usuario.UsuarioRepository
import br.edu.fatec.tcc.guaranin.repository.usuario.UsuarioURLRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

/**
 * Serviço de regras de negócio para [UsuarioURL] (CRUD).
 *
 * Resolve as entidades [Usuario] e
 * [br.edu.fatec.tcc.guaranin.model.URLMonitorada] antes de criar ou atualizar
 * associações, garantindo que os identificadores informados existam.
 *
 * @property usuarioURLRepository repositório de associações.
 * @property usuarioRepository repositório usado para resolver usuários.
 * @property urlMonitoradaRepository repositório usado para resolver URLs monitoradas.
 * @property usuarioURLMapper mapper entre entidade e DTOs.
 */
@Service
class UsuarioURLService(
    private val usuarioURLRepository: UsuarioURLRepository,
    private val usuarioRepository: UsuarioRepository,
    private val urlMonitoradaRepository: URLMonitoradaRepository,
    private val usuarioURLMapper: UsuarioURLMapper
) {

    /**
     * Lista associações de forma paginada.
     *
     * @param pageable parâmetros de paginação e ordenação.
     * @return página de DTOs de resposta.
     */
    @Transactional(readOnly = true)
    fun findAll(pageable: Pageable): Page<UsuarioURLResponseDTO> {
        return usuarioURLRepository.findAll(pageable)
            .map { usuarioURLMapper.toResponseDTO(it) }
    }

    /**
     * Busca uma associação pelo identificador.
     *
     * @param id identificador único da associação.
     * @return DTO da associação encontrada.
     * @throws UsuarioURLNotFoundException quando a associação não existe.
     */
    @Transactional(readOnly = true)
    fun findById(id: UUID): UsuarioURLResponseDTO {
        return usuarioURLMapper.toResponseDTO(
            usuarioURLRepository.findById(id)
                .orElseThrow { UsuarioURLNotFoundException() }
        )
    }

    /**
     * Lista associações de um usuário de forma paginada.
     *
     * @param usuarioId identificador único do usuário.
     * @param pageable parâmetros de paginação e ordenação.
     * @return página de DTOs das associações encontradas.
     * @throws UsuarioNotFoundException quando o usuário não existe.
     */
    @Transactional(readOnly = true)
    fun findByUsuarioId(usuarioId: UUID, pageable: Pageable): Page<UsuarioURLResponseDTO> {
        val usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow { UsuarioNotFoundException() }
        return usuarioURLRepository.findByUsuario(usuario, pageable)
            .map { usuarioURLMapper.toResponseDTO(it) }
    }

    /**
     * Lista associações de uma URL monitorada de forma paginada.
     *
     * @param urlMonitoradaId identificador único da URL monitorada.
     * @param pageable parâmetros de paginação e ordenação.
     * @return página de DTOs das associações encontradas.
     * @throws URLMonitoradaNotFoundException quando a URL monitorada não existe.
     */
    @Transactional(readOnly = true)
    fun findByUrlMonitoradaId(urlMonitoradaId: UUID, pageable: Pageable): Page<UsuarioURLResponseDTO> {
        val urlMonitorada = urlMonitoradaRepository.findById(urlMonitoradaId)
            .orElseThrow { URLMonitoradaNotFoundException() }
        return usuarioURLRepository.findByUrlMonitorada(urlMonitorada, pageable)
            .map { usuarioURLMapper.toResponseDTO(it) }
    }

    /**
     * Cria uma associação entre usuário e URL monitorada.
     *
     * @param createDTO dados da associação a ser criada.
     * @return DTO da associação persistida.
     * @throws UsuarioNotFoundException quando o usuário não existe.
     * @throws URLMonitoradaNotFoundException quando a URL monitorada não existe.
     */
    @Transactional
    fun create(createDTO: UsuarioURLCreateDTO): UsuarioURLResponseDTO {
        val usuario = usuarioRepository.findById(createDTO.usuarioId)
            .orElseThrow { UsuarioNotFoundException() }
        val urlMonitorada = urlMonitoradaRepository.findById(createDTO.urlMonitoradaId)
            .orElseThrow { URLMonitoradaNotFoundException() }

        val usuarioURL = usuarioURLMapper.toEntity(createDTO).apply {
            this.usuario = usuario
            this.urlMonitorada = urlMonitorada
        }

        return usuarioURLMapper.toResponseDTO(
            usuarioURLRepository.save(usuarioURL)
        )
    }

    /**
     * Atualiza parcialmente uma associação existente.
     *
     * Campos nulos no DTO preservam os valores atuais. Quando um novo
     * identificador de usuário ou de URL monitorada é informado, a entidade
     * correspondente é resolvida antes da persistência.
     *
     * @param id identificador único da associação.
     * @param updateDTO dados a serem atualizados.
     * @return DTO da associação após a atualização.
     * @throws UsuarioURLNotFoundException quando a associação não existe.
     * @throws UsuarioNotFoundException quando o novo usuário não existe.
     * @throws URLMonitoradaNotFoundException quando a nova URL monitorada não existe.
     */
    @Transactional
    fun update(id: UUID, updateDTO: UsuarioURLUpdateDTO): UsuarioURLResponseDTO {
        val usuarioURL = usuarioURLRepository.findById(id)
            .orElseThrow { UsuarioURLNotFoundException() }

        usuarioURLMapper.updateEntityFromDTO(updateDTO, usuarioURL)

        if (updateDTO.usuarioId != null) {
            usuarioURL.usuario = usuarioRepository.findById(updateDTO.usuarioId)
                .orElseThrow { UsuarioNotFoundException() }
        }

        if (updateDTO.urlMonitoradaId != null) {
            usuarioURL.urlMonitorada = urlMonitoradaRepository.findById(updateDTO.urlMonitoradaId)
                .orElseThrow { URLMonitoradaNotFoundException() }
        }

        return usuarioURLMapper.toResponseDTO(
            usuarioURLRepository.save(usuarioURL)
        )
    }

    /**
     * Remove uma associação pelo identificador.
     *
     * @param id identificador único da associação.
     * @return DTO da associação removida.
     * @throws UsuarioURLNotFoundException quando a associação não existe.
     */
    @Transactional
    fun delete(id: UUID): UsuarioURLResponseDTO {
        val usuarioURL = usuarioURLRepository.findById(id)
            .orElseThrow { UsuarioURLNotFoundException() }
        usuarioURLRepository.delete(usuarioURL)
        return usuarioURLMapper.toResponseDTO(usuarioURL)
    }
}
