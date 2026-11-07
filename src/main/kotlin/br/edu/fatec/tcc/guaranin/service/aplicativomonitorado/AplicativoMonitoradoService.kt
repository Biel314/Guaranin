package br.edu.fatec.tcc.guaranin.service.aplicativomonitorado

import br.edu.fatec.tcc.guaranin.dto.aplicativomonitorado.AplicativoMonitoradoCreateDTO
import br.edu.fatec.tcc.guaranin.dto.aplicativomonitorado.AplicativoMonitoradoResponseDTO
import br.edu.fatec.tcc.guaranin.dto.aplicativomonitorado.AplicativoMonitoradoUpdateDTO
import br.edu.fatec.tcc.guaranin.exception.aplicativomonitorado.AplicativoMonitoradoNotFoundException
import br.edu.fatec.tcc.guaranin.mapper.aplicativomonitorado.AplicativoMonitoradoMapper
import br.edu.fatec.tcc.guaranin.repository.aplicativomonitorado.AplicativoMonitoradoRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
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
    private val aplicativoMonitoradoMapper: AplicativoMonitoradoMapper
) {
    /**
     * Cria um novo aplicativo monitorado.
     *
     * O DTO recebido é convertido para entidade, persistido no banco de
     * dados e retornado como DTO de resposta.
     *
     * @param dto dados utilizados para criação do aplicativo monitorado.
     * @return aplicativo criado e persistido.
     */
    fun create(dto: AplicativoMonitoradoCreateDTO): AplicativoMonitoradoResponseDTO {
        val aplicativoMonitorado = aplicativoMonitoradoMapper.toEntity(dto)

        val aplicativoMonitoradoSalvo = aplicativoMonitoradoRepository.save(aplicativoMonitorado)

        return aplicativoMonitoradoMapper.toResponseDTO(aplicativoMonitoradoSalvo)
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
    fun update(
        id: UUID,
        dto: AplicativoMonitoradoUpdateDTO
    ): AplicativoMonitoradoResponseDTO {
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
    fun delete(id: UUID): AplicativoMonitoradoResponseDTO {
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
    fun findById(id: UUID): AplicativoMonitoradoResponseDTO {
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
    fun findByNome(nome: String): AplicativoMonitoradoResponseDTO {
        val aplicativoMonitorado = aplicativoMonitoradoRepository.findByNome(nome)
            .orElseThrow {  AplicativoMonitoradoNotFoundException() }

        return aplicativoMonitoradoMapper.toResponseDTO(aplicativoMonitorado)
    }

    /**
     * Busca um aplicativo monitorado pelo nome do pacote.
     *
     * @param pacote identificador único do pacote do aplicativo.
     * @return aplicativo encontrado.
     * @throws AplicativoMonitoradoNotFoundException caso nenhum registro
     * corresponda ao pacote informado.
     */
    fun findByPacote(pacote: String): AplicativoMonitoradoResponseDTO {
        val aplicativoMonitorado = aplicativoMonitoradoRepository.findByPacote(pacote)
            .orElseThrow {  AplicativoMonitoradoNotFoundException() }

        return aplicativoMonitoradoMapper.toResponseDTO(aplicativoMonitorado)
    }

    /**
     * Retorna todos os aplicativos monitorados de forma paginada.
     *
     * @param pageable parâmetros de paginação e ordenação.
     * @return página contendo os aplicativos monitorados encontrados.
     */
    fun findAll(pageable: Pageable): Page<AplicativoMonitoradoResponseDTO> {
        return aplicativoMonitoradoRepository.findAll(pageable)
            .map { aplicativoMonitoradoMapper.toResponseDTO(it) }
    }

    /**
     * Retorna todos os aplicativos marcados como padrão.
     *
     * Aplicativos padrão representam aplicativos previamente definidos
     * pelo sistema.
     *
     * @return lista contendo os aplicativos padrão encontrados.
     */
    fun findPadrao(): List<AplicativoMonitoradoResponseDTO> {
        return aplicativoMonitoradoRepository.findByPadraoTrue()
            .map { aplicativoMonitoradoMapper.toResponseDTO(it) }
    }

    /**
     * Retorna todos os aplicativos que não estão marcados como padrão.
     *
     * @return lista contendo os aplicativos não padrão encontrados.
     */
    fun findNaoPadrao(): List<AplicativoMonitoradoResponseDTO> {
        return aplicativoMonitoradoRepository.findByPadraoFalse()
            .map { aplicativoMonitoradoMapper.toResponseDTO(it) }
    }
}