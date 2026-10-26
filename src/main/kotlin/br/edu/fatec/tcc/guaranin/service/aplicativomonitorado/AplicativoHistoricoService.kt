package br.edu.fatec.tcc.guaranin.service.aplicativomonitorado

import br.edu.fatec.tcc.guaranin.dto.aplicativomonitorado.AplicativoHistoricoCreateDTO
import br.edu.fatec.tcc.guaranin.dto.aplicativomonitorado.AplicativoHistoricoResponseDTO
import br.edu.fatec.tcc.guaranin.exception.aplicativomonitorado.AplicativoHistoricoNotFoundException
import br.edu.fatec.tcc.guaranin.exception.usuario.UsuarioAplicativoNotFoundException
import br.edu.fatec.tcc.guaranin.mapper.aplicativomonitorado.AplicativoHistoricoMapper
import br.edu.fatec.tcc.guaranin.model.AplicativoHistorico
import br.edu.fatec.tcc.guaranin.repository.aplicativomonitorado.AplicativoHistoricoRepository
import br.edu.fatec.tcc.guaranin.repository.usuario.UsuarioAplicativoRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

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
    private val aplicativoHistoricoMapper: AplicativoHistoricoMapper
) {

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

        val usuarioAplicativo =
            usuarioAplicativoRepository.findById(dto.usuarioAplicativoId)
                .orElseThrow {
                    UsuarioAplicativoNotFoundException()
                }

        val aplicativoHistorico = AplicativoHistorico(
            usuarioAplicativo = usuarioAplicativo,
            dataHoraAcesso = dto.dataHoraAcesso
        )

        return aplicativoHistoricoMapper.toResponseDTO(
            aplicativoHistoricoRepository.save(aplicativoHistorico)
        )
    }

    /**
     * Remove um registro de histórico de aplicativo.
     *
     * Após localizar a entidade, o registro é removido da base de dados
     * e retornado como DTO de resposta.
     *
     * @param id identificador do registro histórico.
     * @return histórico removido.
     *
     * @throws AplicativoHistoricoNotFoundException caso o registro não
     * exista.
     */
    fun delete(
        id: UUID
    ): AplicativoHistoricoResponseDTO {

        val aplicativoHistorico =
            aplicativoHistoricoRepository.findById(id)
                .orElseThrow {
                    AplicativoHistoricoNotFoundException()
                }

        aplicativoHistoricoRepository.delete(aplicativoHistorico)

        return aplicativoHistoricoMapper.toResponseDTO(
            aplicativoHistorico
        )
    }

    /**
     * Busca um registro de histórico pelo identificador.
     *
     * @param id identificador único do histórico.
     * @return histórico encontrado.
     *
     * @throws AplicativoHistoricoNotFoundException caso o registro não
     * exista.
     */
    fun findById(
        id: UUID
    ): AplicativoHistoricoResponseDTO {

        val aplicativoHistorico =
            aplicativoHistoricoRepository.findById(id)
                .orElseThrow {
                    AplicativoHistoricoNotFoundException()
                }

        return aplicativoHistoricoMapper.toResponseDTO(
            aplicativoHistorico
        )
    }

    /**
     * Retorna todos os registros de histórico de aplicativos de forma
     * paginada.
     *
     * @param pageable parâmetros de paginação e ordenação.
     * @return página contendo os registros encontrados.
     */
    fun findAll(
        pageable: Pageable
    ): Page<AplicativoHistoricoResponseDTO> {

        return aplicativoHistoricoRepository
            .findAll(pageable)
            .map(aplicativoHistoricoMapper::toResponseDTO)
    }

    /**
     * Retorna todos os registros históricos associados a uma entidade
     * [UsuarioAplicativo].
     *
     * A consulta é realizada de forma paginada para evitar carregamento
     * excessivo de dados em memória.
     *
     * @param usuarioAplicativoId identificador da associação
     * usuário-aplicativo.
     * @param pageable parâmetros de paginação e ordenação.
     *
     * @return página contendo os registros históricos encontrados.
     */
    fun findByUsuarioAplicativo(
        usuarioAplicativoId: UUID,
        pageable: Pageable
    ): Page<AplicativoHistoricoResponseDTO> {

        return aplicativoHistoricoRepository
            .findAllByUsuarioAplicativoId(
                usuarioAplicativoId,
                pageable
            )
            .map(aplicativoHistoricoMapper::toResponseDTO)
    }
}