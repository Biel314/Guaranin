package br.edu.fatec.tcc.guaranin.service.aplicativomonitorado

import br.edu.fatec.tcc.guaranin.dto.aplicativomonitorado.AplicativoMonitoradoCreateDTO
import br.edu.fatec.tcc.guaranin.dto.aplicativomonitorado.AplicativoMonitoradoResponseDTO
import br.edu.fatec.tcc.guaranin.dto.aplicativomonitorado.AplicativoMonitoradoUpdateDTO
import br.edu.fatec.tcc.guaranin.mapper.aplicativomonitorado.AplicativoMonitoradoMapper
import br.edu.fatec.tcc.guaranin.repository.aplicativomonitorado.AplicativoMonitoradoRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class AplicativoMonitoradoService(
    private val aplicativoMonitoradoRepository: AplicativoMonitoradoRepository,
    private val aplicativoMonitoradoMapper: AplicativoMonitoradoMapper
) {
    fun create(dto: AplicativoMonitoradoCreateDTO): AplicativoMonitoradoResponseDTO {
        val aplicativoMonitorado = aplicativoMonitoradoMapper.toEntity(dto)

        val aplicativoMonitoradoSalvo = aplicativoMonitoradoRepository.save(aplicativoMonitorado)

        return aplicativoMonitoradoMapper.toResponseDTO(aplicativoMonitoradoSalvo)
    }

    fun update(
        id: UUID,
        dto: AplicativoMonitoradoUpdateDTO
    ) {
        val aplicativoMonitorado = aplicativoMonitoradoRepository.findById(id)
            .orElse { throw AplicativoMonitoradoNotFoundException() }

        aplicativoMonitoradoMapper.toUpdateDTO(aplicativoMonitorado)


    }

    fun delete(id: UUID) {

    }

    fun findById(id: UUID) {
        aplicativoMonitoradoRepository.findById(id)
    }

    fun findByNome(nome: String) {
        aplicativoMonitoradoRepository.findByNome(nome)
    }

    fun findByPacote(pacote: String) {
        aplicativoMonitoradoRepository.findByPacote(pacote)
    }

    fun findAll() {
        aplicativoMonitoradoRepository.findAll()
    }

    fun findPadrao() {
        aplicativoMonitoradoRepository
    }

    fun findNaoPadrao() {
        aplicativoMonitoradoRepository
    }
}