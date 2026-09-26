package br.edu.fatec.tcc.guaranin.mapper

import br.edu.fatec.tcc.guaranin.dto.URLMonitoradaCreateDTO
import br.edu.fatec.tcc.guaranin.dto.URLMonitoradaResponseDTO
import br.edu.fatec.tcc.guaranin.dto.URLMonitoradaUpdateDTO
import br.edu.fatec.tcc.guaranin.model.URLMonitorada
import org.mapstruct.*

/**
 * Mapper responsável pela conversão entre a entidade [URLMonitorada]
 * e seus respectivos Data Transfer Objects (DTOs).
 *
 * Utiliza o MapStruct para realizar as conversões automaticamente.
 */
@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
interface URLMonitoradaMapper {

    /**
     * Converte uma entidade [URLMonitorada] em
     * [URLMonitoradaResponseDTO].
     *
     * @param urlMonitorada entidade que será convertida.
     * @return DTO contendo os dados da URL monitorada.
     */
    fun toResponseDTO(urlMonitorada: URLMonitorada): URLMonitoradaResponseDTO

    /**
     * Converte um [URLMonitoradaCreateDTO] em uma entidade
     * [URLMonitorada].
     *
     * O identificador da entidade não é mapeado, pois deve ser
     * gerenciado pela camada de persistência.
     *
     * @param urlMonitoradaCreateDTO DTO contendo os dados necessários
     * para criação da URL monitorada.
     * @return entidade [URLMonitorada] criada a partir do DTO.
     */
    @Mapping(target = "id", ignore = true)
    fun toEntity(urlMonitoradaCreateDTO: URLMonitoradaCreateDTO): URLMonitorada

    /**
     * Atualiza uma entidade [URLMonitorada] existente utilizando
     * os dados fornecidos por um [URLMonitoradaUpdateDTO].
     *
     * Propriedades nulas presentes no DTO são ignoradas, preservando
     * os valores atuais da entidade.
     *
     * O identificador da entidade também não é atualizado.
     *
     * @param urlMonitoradaUpdateDTO DTO contendo os dados para atualização.
     * @param urlMonitorada entidade existente que será atualizada.
     */
    @Mapping(target = "id", ignore = true)
    fun updateEntityFromDTO(urlMonitoradaUpdateDTO: URLMonitoradaUpdateDTO, @MappingTarget urlMonitorada: URLMonitorada)
}

