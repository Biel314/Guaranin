package br.edu.fatec.tcc.guaranin.service

import br.edu.fatec.tcc.guaranin.model.Meta
import br.edu.fatec.tcc.guaranin.dto.MetaResponseDTO
import br.edu.fatec.tcc.guaranin.dto.MetaUpdateDTO
import br.edu.fatec.tcc.guaranin.dto.MetaCreateDTO
import br.edu.fatec.tcc.guaranin.mapper.MetaMapper
import br.edu.fatec.tcc.guaranin.repository.MetaRepository
import br.edu.fatec.tcc.guaranin.exception.InvalidDTOException
import br.edu.fatec.tcc.guaranin.exception.MetaNotFoundException
import org.springframework.stereotype.Service
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class MetaService (
    private val metaRepository: MetaRepository,
    private val metaMapper: MetaMapper,
) {

    @Transactional(readOnly = true)
    fun findByTitulo(titulo: String): MetaResponseDTO {
        return metaMapper.toResponseDTO(
            metaRepository.findByTitulo(titulo).orElseThrow { MetaNotFoundException() }
        )
    }

    @Transactional(readOnly = true)
    fun findAll(pageable : Pageable): Page<MetaResponseDTO> {
        return metaRepository.findAll(pageable).map { metaMapper.toResponseDTO(it) }
    }

    @Transactional
    fun create(metaCreateDTO: MetaCreateDTO): MetaResponseDTO {
        val meta = metaMapper.toEntity(metaCreateDTO)

        return metaMapper.toResponseDTO(metaRepository.save(meta)
        )
    }

    @Transactional
    fun update(metaUpdateDTO: MetaUpdateDTO): MetaResponseDTO {
        val uuid = metaUpdateDTO.id ?: throw InvalidDTOException()
        val meta = metaRepository.findById(uuid).orElseThrow { MetaNotFoundException() }
        metaMapper.updateEntityFromDTO(metaUpdateDTO, meta)

        return metaMapper.toResponseDTO(
            metaRepository.save(meta)
        )
    }

    @Transactional
    fun delete(id: UUID): MetaResponseDTO {
        val meta = metaRepository.findById(id).orElseThrow { MetaNotFoundException() }
       metaRepository.delete(meta)
        return metaMapper.toResponseDTO(meta)
    }

    @Transactional(readOnly = true)
    fun findByIdForUpdate(id: UUID): MetaUpdateDTO {
        val meta = metaRepository.findById(id).orElseThrow { MetaNotFoundException() }
        return metaMapper.toUpdateDTO(meta)
    }

    fun toMeta(metaUpdateDTO: MetaUpdateDTO): Meta {
        return metaMapper.toEntity(metaUpdateDTO)
    }

    fun toMeta(metaCreateDTO: MetaCreateDTO): Meta {
        return metaMapper.toEntity(metaCreateDTO)
    }
}