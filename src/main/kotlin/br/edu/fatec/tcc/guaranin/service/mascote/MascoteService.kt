package br.edu.fatec.tcc.guaranin.service.mascote

import br.edu.fatec.tcc.guaranin.dto.mascote.MascoteCreateDTO
import br.edu.fatec.tcc.guaranin.dto.mascote.MascoteResponseDTO
import br.edu.fatec.tcc.guaranin.dto.mascote.MascoteUpdateDTO
import br.edu.fatec.tcc.guaranin.exception.InvalidDTOException
import br.edu.fatec.tcc.guaranin.exception.mascote.MascoteNotFoundException
import br.edu.fatec.tcc.guaranin.exception.mascote.MascoteVivoException
import br.edu.fatec.tcc.guaranin.exception.usuario.UsuarioNotFoundException
import br.edu.fatec.tcc.guaranin.mapper.mascote.MascoteMapper
import br.edu.fatec.tcc.guaranin.model.Estado
import br.edu.fatec.tcc.guaranin.model.Humor
import br.edu.fatec.tcc.guaranin.model.Mascote
import br.edu.fatec.tcc.guaranin.model.Usuario
import br.edu.fatec.tcc.guaranin.repository.mascote.MascoteRepository
import br.edu.fatec.tcc.guaranin.repository.usuario.UsuarioRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
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
    fun findById(id: UUID): MascoteResponseDTO {
        val mascote = mascoteRepository.findById(id)
            .orElseThrow { MascoteNotFoundException() }

        println("id usuario: ${mascote.usuario}")
        println("apelido usuario: ${mascote.usuario?.apelido}")

        return mascoteMapper.toResponseDTO(mascote)
    }

    @Transactional(readOnly = true)
    fun findByNome(nome: String): MascoteResponseDTO {
        return mascoteMapper.toResponseDTO(
            mascoteRepository.findByNome(nome)
                .orElseThrow { MascoteNotFoundException() }
        )
    }

    @Transactional(readOnly = true)
    fun findAllByUsuarioId(
        usuarioId: UUID,
        pageable: Pageable
    ): Page<MascoteResponseDTO> {
        val mascotes = mascoteRepository
            .findByUsuarioId(usuarioId, pageable)

        println("TOTAL = ${mascotes.totalElements}")

        return mascotes.map {
            mascoteMapper.toResponseDTO(it)
        }
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

    @Transactional(readOnly = true)
    fun possuiMascoteVivo(
        usuarioId: UUID,
    ): Boolean {
        return mascoteRepository
            .existsByUsuarioIdAndEstadoNot(
                usuarioId,
                Estado.MORTO
            )
    }
    @Transactional
    fun create(
        mascoteCreateDTO: MascoteCreateDTO,
        usuarioId: UUID
    ): MascoteResponseDTO {
        val usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow { UsuarioNotFoundException() }

        if (
            mascoteRepository.existsByUsuarioIdAndEstadoNot(
                usuarioId,
                Estado.MORTO
            )
        ) {
            throw MascoteVivoException()
        }

        val mascote = mascoteMapper.toEntity(mascoteCreateDTO)

        mascote.usuario = usuario
        mascote.estado = Estado.SAUDAVEL
        mascote.humor = Humor.FELIZ
        mascote.nivel = 1
        mascote.experiencia = 0
        mascote.dataMorte = null

        val mascoteSalvo = mascoteRepository.save(mascote)

        println("USUARIO ID: ${mascoteSalvo.usuario?.id}")
        println("USUARIO APELIDO: ${mascoteSalvo.usuario?.apelido}")

        return mascoteMapper.toResponseDTO(mascoteSalvo)
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

    //teste
    fun matarMascote(
        id: UUID,
    ): MascoteResponseDTO {
        val mascote = mascoteRepository.findById(id)
            .orElseThrow { MascoteNotFoundException() }

        mascote.pontosVida = 0 // para teste

        if (mascote.pontosVida == 0) {
            mascote.estado = Estado.MORTO
        }

        mascote.dataMorte = LocalDate.now()

        val mascoteSalvo = mascoteRepository.save(mascote)

        return mascoteMapper.toResponseDTO(mascoteSalvo)
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