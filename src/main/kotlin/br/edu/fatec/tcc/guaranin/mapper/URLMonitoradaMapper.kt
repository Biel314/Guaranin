package br.edu.fatec.tcc.guaranin.mapper

import br.edu.fatec.tcc.guaranin.dto.URLMonitoradaCreateDTO
import br.edu.fatec.tcc.guaranin.dto.URLMonitoradaResponseDTO
import br.edu.fatec.tcc.guaranin.dto.URLMonitoradaUpdateDTO
import br.edu.fatec.tcc.guaranin.model.URLMonitorada
import org.mapstruct.*

/**
 * Mapper MapStruct para conversão entre entidade [URLMonitorada] e DTOs.
 *
 * A implementação desta interface é gerada automaticamente em tempo de compilação
 * pelo MapStruct (via kapt) e registrada como um bean Spring, graças a
 * `componentModel = "spring"`.
 *
 * Campos do DTO de destino que não possuem correspondência na origem são
 * silenciosamente ignorados ([ReportingPolicy.IGNORE]), evitando warnings
 * de compilação para mapeamentos parciais intencionais (ex.: `id`).
 */
@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
interface URLMonitoradaMapper {

    /**
     * Converte uma entidade [URLMonitorada] persistida em um
     * [URLMonitoradaResponseDTO].
     *
     * @param urlMonitorada entidade de origem, já persistida no banco de dados.
     * @return DTO de saída pronto para serialização.
     */
    fun toResponseDTO(urlMonitorada: URLMonitorada): URLMonitoradaResponseDTO

    /**
     * Converte um [URLMonitoradaCreateDTO], recebido na requisição de cadastro,
     * em uma nova entidade [URLMonitorada].
     *
     * O campo `id` é ignorado no mapeamento, pois é gerado pelo banco de
     * dados na persistência.
     *
     * @param urlMonitoradaCreateDTO dados de entrada para criação do registro.
     * @return nova instância de [URLMonitorada], ainda não persistida.
     */
    @Mapping(target = "id", ignore = true)
    fun toEntity(urlMonitoradaCreateDTO: URLMonitoradaCreateDTO): URLMonitorada

    /**
     * Atualiza os campos de uma entidade [URLMonitorada] existente com os valores
     * fornecidos em um [URLMonitoradaUpdateDTO].
     *
     * O parâmetro `urlMonitorada` é modificado in-place (anotado com
     * [MappingTarget]), e o `id` original é preservado, já que é ignorado
     * no mapeamento. Propriedades `null` no DTO mantêm o valor atual.
     *
     * @param urlMonitoradaUpdateDTO dados de atualização fornecidos pelo cliente.
     * @param urlMonitorada entidade existente a ser atualizada.
     */
    @Mapping(target = "id", ignore = true)
    fun updateEntityFromDTO(
        urlMonitoradaUpdateDTO: URLMonitoradaUpdateDTO,
        @MappingTarget urlMonitorada: URLMonitorada
    )
}
