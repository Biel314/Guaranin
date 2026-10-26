package br.edu.fatec.tcc.guaranin.service.urlmonitorada

import br.edu.fatec.tcc.guaranin.dto.urlmonitorada.URLHistoricoCreateDTO
import br.edu.fatec.tcc.guaranin.dto.urlmonitorada.URLHistoricoResponseDTO
import br.edu.fatec.tcc.guaranin.dto.urlmonitorada.URLHistoricoUpdateDTO
import br.edu.fatec.tcc.guaranin.exception.urlmonitorada.URLHistoricoNotFoundException
import br.edu.fatec.tcc.guaranin.mapper.urlmonitorada.URLHistoricoMapper
import br.edu.fatec.tcc.guaranin.model.URLHistorico
import br.edu.fatec.tcc.guaranin.repository.urlmonitorada.URLHistoricoRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.*

/**
 * Serviço de regras de negócio para [URLHistorico].
 *
 * Centraliza consultas paginadas, criação, atualização e remoção do histórico
 * de acessos, convertendo entidades em [URLHistoricoResponseDTO] por meio do
 * [URLHistoricoMapper]. Operações de leitura usam transações somente leitura.
 *
 * @property urlHistoricoRepository repositório de persistência do histórico.
 * @property urlHistoricoMapper mapper entre entidade e DTOs.
 * @see URLHistorico
 * @see URLHistoricoResponseDTO
 */
@Service
class URLHistoricoService(
    private val urlHistoricoRepository: URLHistoricoRepository,
    private val urlHistoricoMapper: URLHistoricoMapper
) {
    /**
     * Lista todos os históricos de forma paginada.
     *
     * @param pageable parâmetros de paginação e ordenação.
     * @return página de DTOs de resposta.
     */
    @Transactional(readOnly = true)
    fun findAll(pageable: Pageable): Page<URLHistoricoResponseDTO> {
        return urlHistoricoRepository.findAll(pageable)
            .map { urlHistoricoMapper.toResponseDTO(it) }
    }

    /**
     * Busca um histórico pelo identificador.
     *
     * @param id identificador único do histórico.
     * @return DTO do histórico encontrado.
     * @throws URLHistoricoNotFoundException quando o histórico não existe.
     */
    @Transactional(readOnly = true)
    fun findById(id: UUID): URLHistoricoResponseDTO {
        return urlHistoricoMapper.toResponseDTO(
            urlHistoricoRepository.findById(id)
                .orElseThrow { URLHistoricoNotFoundException() })
    }

    /**
     * Busca o histórico associado a uma relação `UsuarioURL`.
     *
     * @param usuarioURLId identificador da associação `UsuarioURL`.
     * @return DTO do histórico encontrado.
     * @throws URLHistoricoNotFoundException quando não há histórico para a
     * associação informada.
     */
    @Transactional(readOnly = true)
    fun findByUsuarioURL(usuarioURLId: UUID): URLHistoricoResponseDTO {
        return urlHistoricoMapper.toResponseDTO(
            urlHistoricoRepository.findByUsuarioUrlId(usuarioURLId).orElseThrow {
                URLHistoricoNotFoundException()
            }
        )
    }

    /**
     * Lista os históricos de um usuário de forma paginada.
     *
     * @param usuarioId identificador único do usuário.
     * @param pageable parâmetros de paginação e ordenação.
     * @return página de DTOs dos históricos encontrados.
     */
    @Transactional(readOnly = true)
    fun findAllByUsuarioId(usuarioId: UUID, pageable: Pageable): Page<URLHistoricoResponseDTO> {
        return urlHistoricoRepository.findByUsuarioUrlUsuarioId(usuarioId, pageable)
            .map { urlHistoricoMapper.toResponseDTO(it) }
    }

    /**
     * Lista os históricos de uma data e hora de acesso específicas.
     *
     * @param dataHoraAcesso data e hora exata do acesso.
     * @param pageable parâmetros de paginação e ordenação.
     * @return página de DTOs dos históricos encontrados.
     */
    @Transactional(readOnly = true)
    fun findAllByDataHoraAcesso(dataHoraAcesso: LocalDateTime, pageable: Pageable): Page<URLHistoricoResponseDTO> {
        return urlHistoricoRepository.findByDataHoraAcesso(dataHoraAcesso, pageable)
            .map { urlHistoricoMapper.toResponseDTO(it) }
    }

    /**
     * Cria um registro de histórico de acesso.
     *
     * @param urlHistoricoCreateDTO dados do histórico a ser criado.
     * @return DTO do histórico persistido.
     */
    @Transactional
    fun create(urlHistoricoCreateDTO: URLHistoricoCreateDTO): URLHistoricoResponseDTO {
        return urlHistoricoMapper.toResponseDTO(
            urlHistoricoRepository.save(
                urlHistoricoMapper.toEntity(urlHistoricoCreateDTO)
            )
        )
    }

    /**
     * Atualiza parcialmente um histórico existente.
     *
     * Campos nulos no DTO preservam os valores atuais da entidade.
     *
     * @param id identificador único do histórico.
     * @param urlHistoricoUpdateDTO dados a serem atualizados.
     * @return DTO do histórico após a atualização.
     * @throws URLHistoricoNotFoundException quando o histórico não existe.
     */
    @Transactional
    fun update(id: UUID, urlHistoricoUpdateDTO: URLHistoricoUpdateDTO): URLHistoricoResponseDTO {
        val urlHistorico = urlHistoricoRepository.findById(id)
            .orElseThrow { URLHistoricoNotFoundException() }
        urlHistoricoMapper.updateEntityFromDTO(urlHistoricoUpdateDTO, urlHistorico)
        return urlHistoricoMapper.toResponseDTO(urlHistoricoRepository.save(urlHistorico))
    }

    /**
     * Remove um histórico pelo identificador.
     *
     * @param id identificador único do histórico.
     * @return DTO do histórico removido.
     * @throws URLHistoricoNotFoundException quando o histórico não existe.
     */
    @Transactional
    fun delete(id: UUID): URLHistoricoResponseDTO {
        val urlHistorico: URLHistorico = urlHistoricoRepository.findById(id)
            .orElseThrow { URLHistoricoNotFoundException() }
        urlHistoricoRepository.delete(urlHistorico)
        return urlHistoricoMapper.toResponseDTO(urlHistorico)
    }
}