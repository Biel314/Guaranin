package br.edu.fatec.tcc.guaranin.service.aplicativomonitorado

import br.edu.fatec.tcc.guaranin.dto.aplicativomonitorado.AplicativoMonitoradoCreateDTO
import br.edu.fatec.tcc.guaranin.dto.aplicativomonitorado.AplicativoMonitoradoResponseDTO
import br.edu.fatec.tcc.guaranin.dto.aplicativomonitorado.AplicativoMonitoradoUpdateDTO
import br.edu.fatec.tcc.guaranin.exception.aplicativomonitorado.AplicativoMonitoradoNotFoundException
import br.edu.fatec.tcc.guaranin.exception.usuario.UsuarioAplicativoJaExisteException
import br.edu.fatec.tcc.guaranin.exception.usuario.UsuarioAplicativoNotFoundException
import br.edu.fatec.tcc.guaranin.exception.usuario.UsuarioNotFoundException
import br.edu.fatec.tcc.guaranin.mapper.aplicativomonitorado.AplicativoMonitoradoMapper
import br.edu.fatec.tcc.guaranin.model.AplicativoMonitorado
import br.edu.fatec.tcc.guaranin.model.Mascote
import br.edu.fatec.tcc.guaranin.model.UsuarioAplicativo
import br.edu.fatec.tcc.guaranin.repository.aplicativomonitorado.AplicativoMonitoradoRepository
import br.edu.fatec.tcc.guaranin.repository.usuario.UsuarioAplicativoRepository
import br.edu.fatec.tcc.guaranin.repository.usuario.UsuarioRepository
import br.edu.fatec.tcc.guaranin.security.AuthorizationService
import br.edu.fatec.tcc.guaranin.security.SecurityService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.nio.file.AccessDeniedException
import java.util.UUID

/**
 * Service responsável pelas regras de negócio relacionadas à entidade
 * [AplicativoMonitorado].
 *
 * Centraliza operações de criação, atualização, remoção e consulta de
 * aplicativos monitorados, além da conversão entre entidades e DTOs
 * através do [AplicativoMonitoradoMapper].
 *
 * Todas as consultas individuais lançam
 * [AplicativoMonitoradoNotFoundException] quando nenhum registro é
 * encontrado.
 *
 * @property aplicativoMonitoradoRepository repositório responsável pelo
 * acesso e persistência dos aplicativos monitorados.
 * @property aplicativoMonitoradoMapper mapper responsável pela conversão
 * entre entidades e DTOs.
 *
 * @see AplicativoMonitorado
 * @see AplicativoMonitoradoRepository
 * @see AplicativoMonitoradoMapper
 */
@Service
class AplicativoMonitoradoService(
    private val aplicativoMonitoradoRepository: AplicativoMonitoradoRepository,
    private val usuarioAplicativoRepository: UsuarioAplicativoRepository,
    private val usuarioRepository: UsuarioRepository,
    private val aplicativoMonitoradoMapper: AplicativoMonitoradoMapper,
    private val securityService: SecurityService,
    private val authorizationService: AuthorizationService,
) {

    private fun validarDonoAplicativo(aplicativoMonitoradoId: UUID) {

        val usuarioId =
            securityService.getUsuarioLogadoId()

        if (
            !usuarioAplicativoRepository
                .existsByUsuarioIdAndAplicativoMonitoradoId(
                    usuarioId,
                    aplicativoMonitoradoId
                )
        ) {
            throw AccessDeniedException(
                "Acesso negado"
            )
        }
    }

    private fun validarDonoAplicativo(aplicativoMonitorado: AplicativoMonitorado) {

        val usuarioId =
            securityService.getUsuarioLogadoId()

        val usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow { UsuarioNotFoundException() }

        if (
            !usuarioAplicativoRepository
                .existsByUsuarioAndAplicativoMonitorado(
                    usuario,
                    aplicativoMonitorado
                )
        ) {
            throw AccessDeniedException(
                "Acesso negado"
            )
        }
    }

    private fun toResponseDTO(
        usuarioAplicativo: UsuarioAplicativo,
    ): AplicativoMonitoradoResponseDTO {
        return aplicativoMonitoradoMapper.toResponseDTO(
            usuarioAplicativo.aplicativoMonitorado
        )
    }
    /**
     * Cria um novo aplicativo monitorado.
     *
     * O DTO recebido é convertido para entidade, persistido no banco de
     * dados e retornado como DTO de resposta.
     *
     * @param dto dados utilizados para criação do aplicativo monitorado.
     * @return aplicativo criado e persistido.
     */
    @Transactional
    fun create(
        dto: AplicativoMonitoradoCreateDTO
    ): AplicativoMonitoradoResponseDTO {

        val usuarioId = securityService.getUsuarioLogadoId()

        val usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow { UsuarioNotFoundException() }

        val aplicativoMonitorado =
            aplicativoMonitoradoRepository
                .findByPacote(dto.pacote)
                .orElseGet {

                    aplicativoMonitoradoRepository.save(
                        aplicativoMonitoradoMapper
                            .toEntity(dto)
                    )
                }

        // Verifica se o vínculo já existe
        if (
            usuarioAplicativoRepository
                .existsByUsuarioIdAndAplicativoMonitoradoId(
                    usuario.id!!,
                    aplicativoMonitorado.id!!
                )
        ) {
            throw UsuarioAplicativoJaExisteException()
        }

        usuarioAplicativoRepository.save(
            UsuarioAplicativo(
                usuario = usuario,
                aplicativoMonitorado = aplicativoMonitorado
            )
        )

        return aplicativoMonitoradoMapper
            .toResponseDTO(aplicativoMonitorado)
    }

    /**
     * Atualiza um aplicativo monitorado existente.
     *
     * O registro é localizado pelo identificador informado e atualizado
     * com os dados fornecidos no DTO.
     *
     * @param id identificador do aplicativo monitorado.
     * @param dto novos dados do aplicativo.
     * @return aplicativo atualizado.
     * @throws AplicativoMonitoradoNotFoundException caso o aplicativo não
     * seja encontrado.
     */
    @Transactional
    fun update(
        id: UUID,
        dto: AplicativoMonitoradoUpdateDTO
    ): AplicativoMonitoradoResponseDTO {
        validarDonoAplicativo(id)

        val aplicativoMonitorado = aplicativoMonitoradoRepository.findById(id)
            .orElseThrow {  AplicativoMonitoradoNotFoundException() }
        aplicativoMonitoradoMapper.updateEntityFromDTO(dto, aplicativoMonitorado)

        return aplicativoMonitoradoMapper.toResponseDTO(
            aplicativoMonitoradoRepository.save(aplicativoMonitorado)
        )
    }

    /**
     * Remove um aplicativo monitorado.
     *
     * Após localizar a entidade, o registro é removido da base de dados
     * e retornado como DTO de resposta.
     *
     * @param id identificador do aplicativo monitorado.
     * @return aplicativo removido.
     * @throws AplicativoMonitoradoNotFoundException caso o aplicativo não
     * seja encontrado.
     */
    @Transactional
    fun delete(id: UUID): AplicativoMonitoradoResponseDTO {
        validarDonoAplicativo(id)

        val aplicativoMonitorado = aplicativoMonitoradoRepository.findById(id)
            .orElseThrow {  AplicativoMonitoradoNotFoundException() }

        aplicativoMonitoradoRepository.delete(aplicativoMonitorado)

        return aplicativoMonitoradoMapper.toResponseDTO(aplicativoMonitorado)
    }

    /**
     * Busca um aplicativo monitorado pelo identificador.
     *
     * @param id identificador único do aplicativo monitorado.
     * @return aplicativo encontrado.
     * @throws AplicativoMonitoradoNotFoundException caso o aplicativo não
     * exista.
     */
    @Transactional(readOnly = true)
    fun findById(id: UUID): AplicativoMonitoradoResponseDTO {
        validarDonoAplicativo(id)

        val aplicativoMonitorado = aplicativoMonitoradoRepository.findById(id)
            .orElseThrow { AplicativoMonitoradoNotFoundException() }

        return aplicativoMonitoradoMapper.toResponseDTO(aplicativoMonitorado)
    }

    /**
     * Busca um aplicativo monitorado pelo nome.
     *
     * @param nome nome do aplicativo monitorado.
     * @return aplicativo encontrado.
     * @throws AplicativoMonitoradoNotFoundException caso nenhum registro
     * corresponda ao nome informado.
     */
    @Transactional(readOnly = true)
    fun findByNome(nome: String): AplicativoMonitoradoResponseDTO {
        val aplicativoMonitorado = aplicativoMonitoradoRepository.findByNome(nome)
            .orElseThrow {  AplicativoMonitoradoNotFoundException() }

        validarDonoAplicativo(aplicativoMonitorado)

        return aplicativoMonitoradoMapper.toResponseDTO(aplicativoMonitorado)
    }

    @Transactional(readOnly = true)
    fun findAllByNomeContainingIgnoreCase(nome: String, page: Pageable): Page<AplicativoMonitoradoResponseDTO> {
        val usuarioId = securityService.getUsuarioLogadoId()


        return usuarioAplicativoRepository
            .findAllByUsuarioIdAndAplicativoMonitoradoNomeContainingIgnoreCase(
                usuarioId,
                nome,
                page
            )
            .map (::toResponseDTO)
    }

    /**
     * Busca aplicativos monitorados pelo nome do pacote.
     *
     * @param pacote identificador único do pacote do aplicativo.
     * @return aplicativo encontrado.
     * @throws AplicativoMonitoradoNotFoundException caso nenhum registro
     * corresponda ao pacote informado.
     */
    @Transactional(readOnly = true)
    fun findByPacote(pacote: String, pageable: Pageable): Page<AplicativoMonitoradoResponseDTO> {
        val usuarioId = securityService.getUsuarioLogadoId()

        return usuarioAplicativoRepository.findAllByUsuarioIdAndAplicativoMonitoradoPacoteContainingIgnoreCase(
            usuarioId,
            pacote,
            pageable
        )
            .map(::toResponseDTO)
    }

    /**
     * Retorna todos os aplicativos monitorados de forma paginada.
     *
     * @param pageable parâmetros de paginação e ordenação.
     * @return página contendo os aplicativos monitorados encontrados.
     */
    @EntityGraph(
        attributePaths = ["aplicativoMonitorado"]
    )
    @Transactional(readOnly = true)
    fun findAllByUsuarioId(pageable: Pageable): Page<AplicativoMonitoradoResponseDTO> {
        val usuarioId = securityService.getUsuarioLogadoId()

        return usuarioAplicativoRepository
            .findAllByUsuarioId(usuarioId, pageable)
            .map { usuarioAplicativo ->
                aplicativoMonitoradoMapper.toResponseDTO(
                    usuarioAplicativo.aplicativoMonitorado
                )
            }
    }

    /**
     * Retorna todos os aplicativos marcados como padrão.
     *
     * Aplicativos padrão representam aplicativos previamente definidos
     * pelo sistema.
     *
     * @return lista contendo os aplicativos padrão encontrados.
     */
    @Transactional(readOnly = true)
    fun findPadrao(pageable: Pageable): Page<AplicativoMonitoradoResponseDTO> {
        val usuarioId = securityService.getUsuarioLogadoId()

        return usuarioAplicativoRepository.findAllByUsuarioIdAndAplicativoMonitoradoPadrao(
            usuarioId,
            true,
            pageable
        )
            .map(::toResponseDTO)
    }

    /**
     * Retorna todos os aplicativos que não estão marcados como padrão.
     *
     * @return lista contendo os aplicativos não padrão encontrados.
     */
    @Transactional(readOnly = true)
    fun findNaoPadrao(pageable: Pageable): Page<AplicativoMonitoradoResponseDTO> {
        val usuarioId = securityService.getUsuarioLogadoId()

        return usuarioAplicativoRepository.findAllByUsuarioIdAndAplicativoMonitoradoPadrao(
            usuarioId,
            false,
            pageable
        )
            .map(::toResponseDTO)
    }
}