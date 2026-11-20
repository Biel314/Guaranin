package br.edu.fatec.tcc.guaranin.service.aplicativomonitorado

import br.edu.fatec.tcc.guaranin.config.DebugConfig
import br.edu.fatec.tcc.guaranin.dto.aplicativomonitorado.AplicativoHistoricoCreateDTO
import br.edu.fatec.tcc.guaranin.dto.aplicativomonitorado.AplicativoHistoricoResponseDTO
import br.edu.fatec.tcc.guaranin.exception.aplicativomonitorado.AplicativoHistoricoNotFoundException
import br.edu.fatec.tcc.guaranin.exception.aplicativomonitorado.DataHoraHistoricoInvalidoException
import br.edu.fatec.tcc.guaranin.exception.usuario.UsuarioAplicativoNotFoundException
import br.edu.fatec.tcc.guaranin.mapper.aplicativomonitorado.AplicativoHistoricoMapper
import br.edu.fatec.tcc.guaranin.model.AplicativoHistorico
import br.edu.fatec.tcc.guaranin.repository.aplicativomonitorado.AplicativoHistoricoRepository
import br.edu.fatec.tcc.guaranin.repository.usuario.UsuarioAplicativoRepository
import br.edu.fatec.tcc.guaranin.repository.usuario.UsuarioRepository
import br.edu.fatec.tcc.guaranin.security.SecurityService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.nio.file.AccessDeniedException
import java.time.LocalDateTime

import java.util.UUID

/**
 * Service responsável pelas regras de negócio relacionadas à entidade
 * [AplicativoHistorico].
 *
 * Centraliza operações de criação, remoção e consulta de registros
 * históricos de acesso aos aplicativos monitorados.
 *
 * Cada registro representa um evento de acesso realizado por uma
 * associação [UsuarioAplicativo] em um determinado momento.
 *
 * Todas as consultas individuais lançam
 * [AplicativoHistoricoNotFoundException] quando nenhum registro é
 * encontrado.
 *
 * @property aplicativoHistoricoRepository repositório responsável pelo
 * acesso e persistência dos históricos de aplicativos.
 * @property usuarioAplicativoRepository repositório utilizado para
 * recuperação das associações usuário-aplicativo.
 * @property aplicativoHistoricoMapper mapper responsável pela conversão
 * entre entidades e DTOs.
 *
 * @see AplicativoHistorico
 * @see AplicativoHistoricoRepository
 * @see AplicativoHistoricoMapper
 */
@Service
class AplicativoHistoricoService(
    private val aplicativoHistoricoRepository: AplicativoHistoricoRepository,
    private val usuarioAplicativoRepository: UsuarioAplicativoRepository,
    private val aplicativoHistoricoMapper: AplicativoHistoricoMapper,
    private val securityService: SecurityService,
    private val debugConfig: DebugConfig
) {

    private val LIMITE_DIAS_HISTORICO: Long = 90L

    private fun isDebugMode(): Boolean {

        println("DEBUG ENABLED = ${debugConfig.enabled}")
        println("ADMIN CONFIG = ${debugConfig.adminUserId}")
        println("TOKEN USER = ${securityService.getUsuarioLogadoId()}")

        val result =
            debugConfig.enabled &&
                    securityService.getUsuarioLogadoId() ==
                    UUID.fromString(debugConfig.adminUserId)

        println("IS DEBUG = $result")

        return result
    }

    private fun validarDonoDoHistorico(historicoId: UUID) {

        val usuarioId =
            securityService.getUsuarioLogadoId()

        if (
            !aplicativoHistoricoRepository
                .existsByIdAndUsuarioAplicativoUsuarioId(
                    historicoId,
                    usuarioId
                )
        ) {
            throw AccessDeniedException(
                "Acesso negado"
            )
        }
    }

    private fun validarDataHoraHistorico(
        dataHora: LocalDateTime
    ) {
        val agora = LocalDateTime.now()

        if (dataHora.isAfter(agora.plusMinutes(5))) {
            throw DataHoraHistoricoInvalidoException()
        }

        if (dataHora.isBefore(
                agora.minusDays(LIMITE_DIAS_HISTORICO)
            )
        ) {
            throw DataHoraHistoricoInvalidoException()
        }
    }

    /**
     * Cria um novo registro de histórico de acesso a aplicativo.
     *
     * O identificador da associação usuário-aplicativo é utilizado para
     * recuperar a entidade correspondente antes da persistência do
     * histórico.
     *
     * @param dto dados utilizados para criação do registro histórico.
     * @return histórico criado e persistido.
     *
     * @throws UsuarioAplicativoNotFoundException caso a associação
     * usuário-aplicativo informada não exista.
     */
    fun create(
        dto: AplicativoHistoricoCreateDTO
    ): AplicativoHistoricoResponseDTO {
        val usuarioId = securityService.getUsuarioLogadoId()
        val agora = LocalDateTime.now()

        validarDataHoraHistorico(
            dto.dataHoraAcesso
        )

        val usuarioAplicativo =
            usuarioAplicativoRepository
                .findByUsuarioIdAndAplicativoMonitoradoPacoteIgnoreCase(
                    usuarioId,
                    dto.pacote
                )
                ?: throw UsuarioAplicativoNotFoundException()

        val historicoUso = AplicativoHistorico(
            usuarioAplicativo = usuarioAplicativo,
            dataHoraAcesso = dto.dataHoraAcesso
        )

        return aplicativoHistoricoMapper.toResponseDTO(
            aplicativoHistoricoRepository.save(historicoUso)
        )
    }


    /**
     * Retorna todos os registros de histórico de aplicativos de forma
     * paginada.
     *
     * @param pageable parâmetros de paginação e ordenação.
     * @return página contendo os registros encontrados.
     */
    private fun findAll(
        pageable: Pageable
    ): Page<AplicativoHistoricoResponseDTO> {

        return aplicativoHistoricoRepository
            .findAll(pageable)
            .map(aplicativoHistoricoMapper::toResponseDTO)
    }

    // Encontrar todos os históricos de usuário
    fun findAllByUsuarioId(
        pageable: Pageable
    ): Page<AplicativoHistoricoResponseDTO> {
        val usuarioId = securityService.getUsuarioLogadoId()

        if (isDebugMode()) {
            return findAll(pageable)
        }

        return aplicativoHistoricoRepository.
                findAllByUsuarioAplicativoUsuarioId(
                    usuarioId,
                    pageable
                )
        .map(aplicativoHistoricoMapper::toResponseDTO)
    }

    fun findHistoricoById(
        historicoId: UUID
    ): AplicativoHistoricoResponseDTO {
        if (!isDebugMode()) {
            validarDonoDoHistorico(historicoId)
        }

        val historico = aplicativoHistoricoRepository
                            .findById(historicoId)
            .orElseThrow{ AplicativoHistoricoNotFoundException() }


        return aplicativoHistoricoMapper
            .toResponseDTO(
                historico
            )
    }

    // busca todos os históricos por nome do aplicativo
    fun findAllByNomeContainingIgnoreCase(
        nome: String,
        pageable: Pageable
    ): Page<AplicativoHistoricoResponseDTO> {
        val usuarioId = securityService.getUsuarioLogadoId()

        if (isDebugMode()) {
            return aplicativoHistoricoRepository
                .findAllByUsuarioAplicativoAplicativoMonitoradoNomeContainingIgnoreCase(
                    nome,
                    pageable)
                .map(aplicativoHistoricoMapper::toResponseDTO)
        }

        return aplicativoHistoricoRepository
            .findAllByUsuarioAplicativoUsuarioIdAndUsuarioAplicativoAplicativoMonitoradoNomeContainingIgnoreCase(
                usuarioId,
                nome,
                pageable
            )
            .map(aplicativoHistoricoMapper::toResponseDTO)
    }

    // busca todos os históricos por pacote
    fun findAllByPacoteIgnoreCase(
        pacote: String,
        pageable: Pageable
    ): Page<AplicativoHistoricoResponseDTO> {

        if (isDebugMode()) {
            return aplicativoHistoricoRepository
                .findAllByUsuarioAplicativoAplicativoMonitoradoPacoteIgnoreCase(
                    pacote,
                    pageable
                )
                .map(aplicativoHistoricoMapper::toResponseDTO)
        }

        val usuarioId = securityService.getUsuarioLogadoId()

        return aplicativoHistoricoRepository
            .findAllByUsuarioAplicativoUsuarioIdAndUsuarioAplicativoAplicativoMonitoradoPacoteIgnoreCase(
                usuarioId,
                pacote,
                pageable
            )
            .map(aplicativoHistoricoMapper::toResponseDTO)
    }

    // Encontra todos os registros de historico de um usuario da data inicial
    // até a data final
    fun findAllByUsuarioAplicativoUsuarioIdAndDataHoraAcessoBetween(
        dataHoraInicial: LocalDateTime,
        dataHoraFinal: LocalDateTime,
        pageable: Pageable
    ): Page<AplicativoHistoricoResponseDTO> {
        val usuarioId = securityService.getUsuarioLogadoId()

        return aplicativoHistoricoRepository
            .findAllByUsuarioAplicativoUsuarioIdAndDataHoraAcessoBetween(
                usuarioId,
                dataHoraInicial,
                dataHoraFinal,
                pageable
            )
            .map(aplicativoHistoricoMapper::toResponseDTO)
    }

    fun findAllByUsuarioAplicativoUsuarioIdAndUsuarioAplicativoAplicativoMonitoradoIdAndDataHoraAcessoBetween(
        aplicativoMonitoradoId: UUID,
        dataHoraInicial: LocalDateTime,
        dataHoraFinal: LocalDateTime,
        pageable: Pageable
    ): Page<AplicativoHistoricoResponseDTO> {
        val usuarioId = securityService.getUsuarioLogadoId()

        return aplicativoHistoricoRepository
            .findAllByUsuarioAplicativoUsuarioIdAndUsuarioAplicativoAplicativoMonitoradoIdAndDataHoraAcessoBetween(
                usuarioId,
                aplicativoMonitoradoId,
                dataHoraInicial,
                dataHoraFinal,
                pageable
            )
            .map(aplicativoHistoricoMapper::toResponseDTO)
    }

    fun findAllByUsuarioAplicativoUsuarioIdAndDataHoraAcessoAfter(
        dataHora: LocalDateTime,
        pageable: Pageable
    ): Page<AplicativoHistoricoResponseDTO> {
        val usuarioId = securityService.getUsuarioLogadoId()

        return aplicativoHistoricoRepository
            .findAllByUsuarioAplicativoUsuarioIdAndDataHoraAcessoAfter(
                usuarioId,
                dataHora,
                pageable
            )
            .map(aplicativoHistoricoMapper::toResponseDTO)
    }

    private fun findAllByAplicativoMonitoradoId(
        aplicativoMonitoradoId: UUID,
        pageable: Pageable
    ): Page<AplicativoHistoricoResponseDTO> {

        return aplicativoHistoricoRepository
            .findAllByUsuarioAplicativoAplicativoMonitoradoId(
                aplicativoMonitoradoId,
                pageable
            )
            .map(aplicativoHistoricoMapper::toResponseDTO)
    }

    fun findAllByUsuarioAplicativoUsuarioIdAndUsuarioAplicativoAplicativoMonitoradoId(
        aplicativoMonitoradoId: UUID,
        pageable: Pageable
    ): Page<AplicativoHistoricoResponseDTO> {
        val usuarioId = securityService.getUsuarioLogadoId()

        return aplicativoHistoricoRepository
            .findAllByUsuarioAplicativoUsuarioIdAndUsuarioAplicativoAplicativoMonitoradoId(
                usuarioId,
                aplicativoMonitoradoId,
                pageable
            )
            .map(aplicativoHistoricoMapper::toResponseDTO)
    }

    fun findTopByUsuarioAplicativoUsuarioIdOrderByDataHoraAcessoDesc(): AplicativoHistoricoResponseDTO? {
        val usuarioId = securityService.getUsuarioLogadoId()

        val historico =
                    aplicativoHistoricoRepository
                        .findTopByUsuarioAplicativoUsuarioIdOrderByDataHoraAcessoDesc(
                            usuarioId
                        )
                        ?: throw AplicativoHistoricoNotFoundException()
        return aplicativoHistoricoMapper.toResponseDTO(historico)
    }
}