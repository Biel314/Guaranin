package br.edu.fatec.tcc.guaranin.service

import br.edu.fatec.tcc.guaranin.dto.UsuarioURLCreateDTO
import br.edu.fatec.tcc.guaranin.dto.UsuarioURLResponseDTO
import br.edu.fatec.tcc.guaranin.dto.UsuarioURLUpdateDTO
import br.edu.fatec.tcc.guaranin.exception.URLMonitoradaNotFoundException
import br.edu.fatec.tcc.guaranin.exception.UsuarioNotFoundException
import br.edu.fatec.tcc.guaranin.exception.UsuarioURLNotFoundException
import br.edu.fatec.tcc.guaranin.mapper.UsuarioURLMapper
import br.edu.fatec.tcc.guaranin.model.UsuarioURL
import br.edu.fatec.tcc.guaranin.repository.URLMonitoradaRepository
import br.edu.fatec.tcc.guaranin.repository.UsuarioRepository
import br.edu.fatec.tcc.guaranin.repository.UsuarioURLRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

/**
 * Serviço de regras de negócio para [UsuarioURL] (CRUD).
 */
@Service
class UsuarioURLService(
    private val usuarioURLRepository: UsuarioURLRepository,
    private val usuarioRepository: UsuarioRepository,
    private val urlMonitoradaRepository: URLMonitoradaRepository,
    private val usuarioURLMapper: UsuarioURLMapper
) {

    @Transactional(readOnly = true)
    fun findAll(pageable: Pageable): Page<UsuarioURLResponseDTO> {
        return usuarioURLRepository.findAll(pageable)
            .map { usuarioURLMapper.toResponseDTO(it) }
    }

    @Transactional(readOnly = true)
    fun findById(id: UUID): UsuarioURLResponseDTO {
        return usuarioURLMapper.toResponseDTO(
            usuarioURLRepository.findById(id)
                .orElseThrow { UsuarioURLNotFoundException() }
        )
    }

    @Transactional(readOnly = true)
    fun findByUsuarioId(usuarioId: UUID, pageable: Pageable): Page<UsuarioURLResponseDTO> {
        val usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow { UsuarioNotFoundException() }
        return usuarioURLRepository.findByUsuario(usuario, pageable)
            .map { usuarioURLMapper.toResponseDTO(it) }
    }

    @Transactional(readOnly = true)
    fun findByUrlMonitoradaId(urlMonitoradaId: UUID, pageable: Pageable): Page<UsuarioURLResponseDTO> {
        val urlMonitorada = urlMonitoradaRepository.findById(urlMonitoradaId)
            .orElseThrow { URLMonitoradaNotFoundException() }
        return usuarioURLRepository.findByUrlMonitorada(urlMonitorada, pageable)
            .map { usuarioURLMapper.toResponseDTO(it) }
    }

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

    @Transactional
    fun update(updateDTO: UsuarioURLUpdateDTO): UsuarioURLResponseDTO {
        val usuarioURL = usuarioURLRepository.findById(updateDTO.id)
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

    @Transactional
    fun delete(id: UUID): UsuarioURLResponseDTO {
        val usuarioURL = usuarioURLRepository.findById(id)
            .orElseThrow { UsuarioURLNotFoundException() }
        usuarioURLRepository.delete(usuarioURL)
        return usuarioURLMapper.toResponseDTO(usuarioURL)
    }
}
