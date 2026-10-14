package br.edu.fatec.tcc.guaranin.service

import br.edu.fatec.tcc.guaranin.dto.UsuarioCreateDTO
import br.edu.fatec.tcc.guaranin.dto.UsuarioResponseDTO
import br.edu.fatec.tcc.guaranin.dto.UsuarioUpdateDTO
import br.edu.fatec.tcc.guaranin.exception.InvalidDTOException
import br.edu.fatec.tcc.guaranin.exception.UsuarioNotFoundException
import br.edu.fatec.tcc.guaranin.mapper.UsuarioMapper
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
class UsuarioService(
    private val usuarioRepository: UsuarioRepository,
    private val usuarioMapper: UsuarioMapper,
    private val mascoteRepository: MascoteRepository
    //private val passwordEncoder: Encoder
) {

    @Transactional(readOnly = true)
    fun findByEmail(email: String): UsuarioResponseDTO {
        return usuarioMapper.toResponseDTO(
            usuarioRepository.findByEmail(email)
                .orElseThrow { UsuarioNotFoundException() }
        )
    }

    @Transactional(readOnly = true)
    fun findAll(pageable: Pageable): Page<UsuarioResponseDTO> {
        return usuarioRepository.findAll(pageable)
            .map { usuarioMapper.toResponseDTO(it) }
    }

    @Transactional
    fun create(usuarioCreateDTO: UsuarioCreateDTO): UsuarioResponseDTO {
        val usuario = usuarioMapper.toEntity(usuarioCreateDTO)

        val usuarioSalvo = usuarioRepository.save(usuario)

        val mascote = Mascote(
            usuario = usuarioSalvo,
            nome = "Guaranin",
            estado = Estado.SAUDAVEL,
            humor = Humor.FELIZ,
            nivel = 1,
            experiencia = 0,
            pontosVida = 7
        )

        mascoteRepository.save(mascote)
        //TODO Define password criptography
        //usuario.password = passwordEncoder.encode(usuario.password);
        return usuarioMapper.toResponseDTO(
            usuarioRepository.save(usuarioSalvo)
        )
    }

    @Transactional
    fun update(usuarioUpdateDTO: UsuarioUpdateDTO): UsuarioResponseDTO {
        val uuid = usuarioUpdateDTO.id
            ?: throw InvalidDTOException()
        val usuario = usuarioRepository.findById(uuid)
            .orElseThrow { UsuarioNotFoundException() }
        usuarioMapper.updateEntityFromDTO(usuarioUpdateDTO, usuario)

        if (!usuarioUpdateDTO.password.isNullOrEmpty()) {
            //TODO Define password criptography
            // usuario.password = passwordEncoder.encode(usuarioUpdateDTO.password)
        }
        return usuarioMapper.toResponseDTO(
            usuarioRepository.save(usuario)
        )
    }

    @Transactional
    fun delete(id: UUID): UsuarioResponseDTO {
        val usuario = usuarioRepository.findById(id)
            .orElseThrow { UsuarioNotFoundException() }
        usuarioRepository.delete(usuario)
        return usuarioMapper.toResponseDTO(usuario)
    }

    /**
     * Busca um usuário pelo ID e o converte para [UsuarioUpdateDTO] para fins de edição no formulário.
     *
     * @param id identificador único do usuário ([UUID]).
     * @return DTO contendo os dados atuais do usuário.
     * @throws UsuarioNotFoundException se o usuário não for encontrado.
     */
    @Transactional(readOnly = true)
    fun findByIdForUpdate(id: UUID): UsuarioUpdateDTO {
        val usuario = usuarioRepository.findById(id)
            .orElseThrow { UsuarioNotFoundException() }
        return usuarioMapper.toUpdateDTO(usuario)
    }

    fun toUsuario(usuarioUpdateDTO: UsuarioUpdateDTO): Usuario {
        return usuarioMapper.toEntity(usuarioUpdateDTO)
    }

    fun toUsuario(usuarioCreateDTO: UsuarioCreateDTO): Usuario {
        return usuarioMapper.toEntity(usuarioCreateDTO)
    }

}