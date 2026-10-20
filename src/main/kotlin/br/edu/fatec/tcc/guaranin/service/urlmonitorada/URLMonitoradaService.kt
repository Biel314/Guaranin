package br.edu.fatec.tcc.guaranin.service.urlmonitorada

import br.edu.fatec.tcc.guaranin.dto.urlmonitorada.URLMonitoradaCreateDTO
import br.edu.fatec.tcc.guaranin.dto.urlmonitorada.URLMonitoradaResponseDTO
import br.edu.fatec.tcc.guaranin.dto.urlmonitorada.URLMonitoradaUpdateDTO
import br.edu.fatec.tcc.guaranin.exception.urlmonitorada.URLMonitoradaNotFoundException
import br.edu.fatec.tcc.guaranin.mapper.urlmonitorada.URLMonitoradaMapper
import br.edu.fatec.tcc.guaranin.model.URLMonitorada
import br.edu.fatec.tcc.guaranin.repository.urlmonitorada.URLMonitoradaRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

/**
 * Serviço de regras de negócio para [URLMonitorada] (CRUD).
 */
@Service
class URLMonitoradaService(
    private val urlMonitoradaRepository: URLMonitoradaRepository,
    private val urlMonitoradaMapper: URLMonitoradaMapper
) {

    @Transactional(readOnly = true)
    fun findAll(pageable: Pageable): Page<URLMonitoradaResponseDTO> {
        return urlMonitoradaRepository.findAll(pageable)
            .map { urlMonitoradaMapper.toResponseDTO(it) }
    }

    @Transactional(readOnly = true)
    fun findById(id: UUID): URLMonitoradaResponseDTO {
        return urlMonitoradaMapper.toResponseDTO(
            urlMonitoradaRepository.findById(id)
                .orElseThrow { URLMonitoradaNotFoundException() })
    }

    @Transactional(readOnly = true)
    fun findByLink(link: String): URLMonitoradaResponseDTO {
        val normalizedLink = URLMonitoradaLinkNormalizer.normalize(link)
        return urlMonitoradaMapper.toResponseDTO(
            urlMonitoradaRepository.findByLink(normalizedLink).orElseThrow {
                URLMonitoradaNotFoundException()
            }
        )
    }

    @Transactional
    fun create(urlMonitoradaCreateDTO: URLMonitoradaCreateDTO): URLMonitoradaResponseDTO {
        val normalizedDTO = urlMonitoradaCreateDTO.copy(
            link = URLMonitoradaLinkNormalizer.normalize(urlMonitoradaCreateDTO.link)
        )
        return urlMonitoradaMapper.toResponseDTO(
            urlMonitoradaRepository.save(
                urlMonitoradaMapper.toEntity(normalizedDTO)
            )
        )
    }

    @Transactional
    fun update(id: UUID, urlMonitoradaUpdateDTO: URLMonitoradaUpdateDTO): URLMonitoradaResponseDTO {
        val urlMonitorada = urlMonitoradaRepository.findById(id)
            .orElseThrow { URLMonitoradaNotFoundException() }
        val normalizedDTO = urlMonitoradaUpdateDTO.copy(
            link = urlMonitoradaUpdateDTO.link?.let(URLMonitoradaLinkNormalizer::normalize)
        )
        urlMonitoradaMapper.updateEntityFromDTO(normalizedDTO, urlMonitorada)
        return urlMonitoradaMapper.toResponseDTO(urlMonitoradaRepository.save(urlMonitorada))
    }

    @Transactional
    fun delete(id: UUID): URLMonitoradaResponseDTO {
        val urlMonitorada: URLMonitorada = urlMonitoradaRepository.findById(id)
            .orElseThrow { URLMonitoradaNotFoundException() }
        urlMonitoradaRepository.delete(urlMonitorada)
        return urlMonitoradaMapper.toResponseDTO(urlMonitorada)
    }
}
