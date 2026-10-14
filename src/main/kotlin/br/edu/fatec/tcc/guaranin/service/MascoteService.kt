package br.edu.fatec.tcc.guaranin.service

import br.edu.fatec.tcc.guaranin.dto.MascoteCreateDTO
import br.edu.fatec.tcc.guaranin.dto.MascoteResponseDTO
import br.edu.fatec.tcc.guaranin.dto.MascoteUpdateDTO
import br.edu.fatec.tcc.guaranin.exception.InvalidDTOException
import br.edu.fatec.tcc.guaranin.exception.MascoteNotFoundException
import br.edu.fatec.tcc.guaranin.exception.MascoteVivoException
import br.edu.fatec.tcc.guaranin.exception.UsuarioNotFoundException
import br.edu.fatec.tcc.guaranin.mapper.MascoteMapper
import br.edu.fatec.tcc.guaranin.model.Estado
import br.edu.fatec.tcc.guaranin.model.Humor
import br.edu.fatec.tcc.guaranin.model.Mascote
import br.edu.fatec.tcc.guaranin.model.Usuario
import br.edu.fatec.tcc.guaranin.repository.MascoteRepository
import br.edu.fatec.tcc.guaranin.repository.UsuarioRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

/**
 * Serviço de regras de negócio para [Usuario] (CRUD).
 */
@Service
class MascoteService (
    private val mascoteRepository: MascoteRepository,
    private val usuarioRepository: UsuarioRepository,
    private val mascoteMapper: MascoteMapper,
) {

    @Transactional(readOnly = true)
    fun findByNome(nome: String): MascoteResponseDTO {
        return mascoteMapper.toResponseDTO(
            mascoteRepository.findByNome(nome)
                .orElseThrow { MascoteNotFoundException() }
        )
    }

    @Transactional(readOnly = true)
    fun findAll(pageable: Pageable): Page<MascoteResponseDTO> {
        return mascoteRepository.findAll(pageable)
            .map { mascoteMapper.toResponseDTO(it) }
    }

    @Transactional
    fun findByIdAndEstado(usuarioId: UUID): MascoteResponseDTO {
        return mascoteMapper.toResponseDTO(
            mascoteRepository.findByUsuarioIdAndEstado(
                usuarioId,
                Estado.SAUDAVEL
            ).orElseThrow { MascoteNotFoundException() }
        )
    }

    @Transactional
    fun create(
        mascoteCreateDTO: MascoteCreateDTO,
        usuarioId: UUID
    ): MascoteResponseDTO {
        val usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow { UsuarioNotFoundException() }
        val mascote = mascoteMapper.toEntity(mascoteCreateDTO)
        mascote.usuario = usuario

        val possuiMascoteVivo =
            mascoteRepository.existsByUsuarioIdAndEstadoNot(
                usuarioId,
                Estado.MORTO
            )
       if (possuiMascoteVivo) {
           throw MascoteVivoException()
       } else {

        mascote.estado = Estado.SAUDAVEL
        mascote.humor = Humor.FELIZ
        mascote.nivel = 1
        mascote.pontosVida = 7
        mascote.experiencia = 0


        return mascoteMapper.toResponseDTO(
            mascoteRepository.save(mascote)
        )
    }

    @Transactional
    fun update(mascoteUpdateDTO: MascoteUpdateDTO): MascoteResponseDTO {
        val uuid = mascoteUpdateDTO.id
            ?: throw InvalidDTOException()
        val mascote = mascoteRepository.findById(uuid)
            .orElseThrow { MascoteNotFoundException() }
        mascoteMapper.updateEntityFromDTO(mascoteUpdateDTO, mascote)

       return mascoteMapper.toResponseDTO(
            mascoteRepository.save(mascote)
        )
    }

    @Transactional
    fun findByUsuarioIdForUpdate(
        usuarioId: UUID
    ): MascoteUpdateDTO {
        val mascote = mascoteRepository.findByUsuarioId(usuarioId)
            .orElseThrow { MascoteNotFoundException() }

        return mascoteMapper.toUpdateDTO(mascote)
    }

    @Transactional
    fun delete(id: UUID): MascoteResponseDTO {
        val mascote = mascoteRepository.findById(id)
            .orElseThrow { MascoteNotFoundException() }
        mascoteRepository.delete(mascote)
        return mascoteMapper.toResponseDTO(mascote)
    }

    /**
     * Busca um usuário pelo ID e o converte para [MascoteUpdateDTO] para fins de edição no formulário.
     *
     * @param id identificador único do mascote ([UUID]).
     * @return DTO contendo os dados atuais do mascote.
     * @throws MascoteNotFoundException se o mascote não for encontrado.
     */
    @Transactional(readOnly = true)
    fun findByIdForUpdate(id: UUID): MascoteUpdateDTO {
        val mascote = mascoteRepository.findById(id)
            .orElseThrow { MascoteNotFoundException() }
        return mascoteMapper.toUpdateDTO(mascote)
    }

    fun toEntity(mascoteUpdateDTO: MascoteUpdateDTO): Mascote {
        return mascoteMapper.toEntity(mascoteUpdateDTO)
    }

    fun toEntity(mascoteCreateDTO: MascoteCreateDTO): Mascote {
        return mascoteMapper.toEntity(mascoteCreateDTO)
    }

}