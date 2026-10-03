package br.edu.fatec.tcc.guaranin.service

import br.edu.fatec.tcc.guaranin.dto.URLMonitoradaCreateDTO
import br.edu.fatec.tcc.guaranin.dto.URLMonitoradaResponseDTO
import br.edu.fatec.tcc.guaranin.dto.URLMonitoradaUpdateDTO
import br.edu.fatec.tcc.guaranin.exception.URLMonitoradaNotFoundException
import br.edu.fatec.tcc.guaranin.mapper.URLMonitoradaMapper
import br.edu.fatec.tcc.guaranin.model.URLMonitorada
import br.edu.fatec.tcc.guaranin.repository.URLMonitoradaRepository
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
        return urlMonitoradaMapper.toResponseDTO(
            urlMonitoradaRepository.findByLink(link).orElseThrow {
                URLMonitoradaNotFoundException()
            }
        )
    }

    @Transactional
    fun create(urlMonitoradaCreateDTO: URLMonitoradaCreateDTO): URLMonitoradaResponseDTO {
        return urlMonitoradaMapper.toResponseDTO(
            urlMonitoradaRepository.save(
                urlMonitoradaMapper.toEntity(urlMonitoradaCreateDTO)
            )
        )
    }

    @Transactional
    fun update(urlMonitoradaUpdateDTO: URLMonitoradaUpdateDTO): URLMonitoradaResponseDTO {
        val urlMonitorada = urlMonitoradaRepository.findById(urlMonitoradaUpdateDTO.id)
            .orElseThrow { URLMonitoradaNotFoundException() }
        urlMonitoradaMapper.updateEntityFromDTO(urlMonitoradaUpdateDTO, urlMonitorada)
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
