package br.edu.fatec.tcc.guaranin.mapper.aplicativomonitorado

import br.edu.fatec.tcc.guaranin.dto.aplicativomonitorado.AplicativoMonitoradoCreateDTO
import br.edu.fatec.tcc.guaranin.dto.aplicativomonitorado.AplicativoMonitoradoResponseDTO
import br.edu.fatec.tcc.guaranin.dto.aplicativomonitorado.AplicativoMonitoradoUpdateDTO
import br.edu.fatec.tcc.guaranin.model.AplicativoMonitorado
import org.mapstruct.*

/**
 * Mapper MapStruct para conversão entre a entidade [AplicativoMonitrado] e seus DTOs.
 *
 * A implementação é gerada em tempo de compilação pelo MapStruct via kapt e
 * registrada como bean Spring por `componentModel = "spring"`. Destinos sem
 * correspondência na origem são ignorados ([ReportingPolicy.IGNORE]) e
 * propriedades nulas não sobrescrevem valores existentes
 * ([NullValuePropertyMappingStrategy.IGNORE]).
 *
 * @see AplicativoMonitorado
 * @see AplicativoMonitoradoCreateDTO
 * @see AplicativoMonitoradoUpdateDTO
 * @see AplicativoMonitoradoResponseDTO
 */
@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
interface AplicativoMonitoradoMapper {

    /**
     * Converte uma entidade [AplicativoMonitorado] persistida em um [AplicativoMonitoradoResponseDTO],
     * adequado para ser exposto por endpoints da API (sem dados sensíveis
     * como a senha).
     *
     * @param aplicativoMonitorado entidade de origem, já persistida no banco de dados.
     * @return DTO de saída pronto para serialização.
     */
    fun toResponseDTO(aplicativoMonitorado: AplicativoMonitorado): AplicativoMonitoradoResponseDTO

    /**
     * Converte uma entidade [AplicativoMonitorado] persistida em um [AplicativoMonitoradoUpdateDTO],
     * utilizado para preencher o formulário de edição.
     *
     * @param aplicativo entidade de origem.
     * @return DTO de atualização correspondente.
     */
    fun toUpdateDTO(aplicativoMonitorado: AplicativoMonitorado): AplicativoMonitoradoUpdateDTO

    /**
     * Converte um [AplicativoMonitoradoCreateDTO], recebido na requisição de cadastro,
     * em uma nova entidade [AplicativoMonitorado].
     *
     * O campo `id` é ignorado no mapeamento, pois é gerado pelo banco de
     * dados na persistência.
     *
     * @param aplicativoMonitoradoCreateDTO dados de entrada para criação do usuário.
     * @return nova instância de [AplicativoMonitorado], ainda não persistida.
     */
    @Mapping(target = "id", ignore = true)
    fun toEntity(aplicativoMonitoradoCreateDTO: AplicativoMonitoradoCreateDTO): AplicativoMonitorado

    /**
     * Converte um [AplicativoMonitoradoUpdateDTO] em uma entidade [AplicativoMonitorado].
     *
     * @param aplicativoMonitoradoUpdateDTO dados de atualização de usuário.
     * @return nova instância de [AplicativoMonitorado].
     */
    fun toEntity(aplicativoMonitoradoUpdateDTO: AplicativoMonitoradoUpdateDTO): AplicativoMonitorado

    /**
     * Atualiza os campos de uma entidade [AplicativoMonitorado] existente com os valores
     * fornecidos em um [AplicativoMonitoradoUpdateDTO].
     *
     * O parâmetro `aplicativo` é modificado in-place (anotado com
     * [MappingTarget]), e o `id` original é preservado, já que é ignorado
     * no mapeamento.
     *
     * @param dto dados de atualização fornecidos pelo cliente.
     * @param aplicativoMonitorado entidade existente a ser atualizada.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "padrao", ignore = true)
    fun updateEntityFromDTO(dto: AplicativoMonitoradoUpdateDTO, @MappingTarget aplicativoMonitorado: AplicativoMonitorado)


}