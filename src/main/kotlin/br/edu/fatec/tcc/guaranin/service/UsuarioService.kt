package br.edu.fatec.tcc.guaranin.service

import br.edu.fatec.tcc.guaranin.dto.UsuarioCreateDTO
import br.edu.fatec.tcc.guaranin.dto.UsuarioResponseDTO
import br.edu.fatec.tcc.guaranin.dto.UsuarioUpdateDTO
import br.edu.fatec.tcc.guaranin.exception.UsuarioNotFoundException
import br.edu.fatec.tcc.guaranin.mapper.UsuarioMapper
import br.edu.fatec.tcc.guaranin.repository.UsuarioRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.beans.Encoder

@Service
class UsuarioService(
    private val usuarioRepository: UsuarioRepository,
    private val usuarioMapper: UsuarioMapper,
    //private val passwordEncoder: Encoder
) {

    @Transactional(readOnly = true)
    fun findByEmail(email: String): UsuarioResponseDTO {
        return usuarioMapper.toResponseDTO(
            usuarioRepository.findByLogin(email)
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
        //TODO Define password criptography
        //usuario.password = passwordEncoder.encode(usuario.password);
        return usuarioMapper.toResponseDTO(
            usuarioRepository.save(usuario)
        )
    }

    @Transactional
    fun update(usuarioUpdateDTO: UsuarioUpdateDTO): UsuarioResponseDTO {
        val usuario = usuarioRepository.findById(usuarioUpdateDTO.id)
            .orElseThrow { UsuarioNotFoundException() }
        usuarioMapper.updateEntityFromDTO(usuarioUpdateDTO, usuario)

        if (!usuarioUpdateDTO.password.isNullOrEmpty()) {
            //TODO Define password criptography
            // usuario.password = passwordEncoder.encode(usuarioUpdateDTO.password)
        }
        return usuarioMapper.toResponseDTO(
            usuarioRepository.save(usuario)
        )
        return TODO("Provide the return value")
    }

    @Transactional
    fun delete(id: Long): UsuarioResponseDTO {
        val usuario = usuarioRepository.findById(id)
            .orElseThrow { UsuarioNotFoundException() }
        usuarioRepository.delete(usuario)
        return usuarioMapper.toResponseDTO(usuario)
    }
}