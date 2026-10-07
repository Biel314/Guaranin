package br.edu.fatec.tcc.guaranin.mapper

import br.edu.fatec.tcc.guaranin.dto.MascoteCreateDTO
import br.edu.fatec.tcc.guaranin.dto.MascoteResponseDTO
import br.edu.fatec.tcc.guaranin.dto.MascoteUpdateDTO
import br.edu.fatec.tcc.guaranin.model.Mascote
import org.mapstruct.*

/**
 * Mapper responsável pela conversão entre a entidade [Mascote] e seus respectivos DTOs.
 *
 * A implementação desta interface é gerada automaticamente em tempo de compilação
 * pelo MapStruct (via kapt) e registrada como um bean Spring, graças a
 * `componentModel = "spring"`.
 *
 * Campos do DTO de destino que não possuem correspondência na origem são
 * silenciosamente ignorados ([ReportingPolicy.IGNORE]), evitando warnings
 * de compilação para mapeamentos parciais intencionais (ex.: `id`, `password`).
 */
@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
interface MascoteMapper {

    /**
     * Converte uma entidade [Mascote] persistida em um [MascoteResponseDTO],
     * adequado para ser exposto por endpoints da API (sem dados sensíveis
     * como a senha).
     *
     * @param mascote entidade de origem, já persistida no banco de dados.
     * @return DTO de saída pronto para serialização.
     */
    fun toResponseDTO(mascote: Mascote): MascoteResponseDTO

    /**
     * Converte um [MascoteCreateDTO], recebido na requisição de cadastro,
     * em uma nova entidade [Mascote].
     *
     * O campo `id` é ignorado no mapeamento, pois é gerado pelo banco de
     * dados na persistência. A senha mapeada aqui está em texto plano —
     * cabe à camada de serviço aplicar o hash (`PasswordEncoder`) antes
     * de salvar a entidade.
     *
     * @param mascoteCreateDto dados de entrada para criação do usuário.
     * @return nova instância de [Mascote], ainda não persistida.
     */
    @Mapping(target = "id", ignore = true)
    fun toEntity(mascoteCreateDto: MascoteCreateDTO): Mascote

    /**
     * Atualiza os campos de uma entidade [Mascote] existente com os valores
     * fornecidos em um [MascoteUpdateDTO].
     *
     * O parâmetro `usuario` é modificado in-place (anotado com
     * [MappingTarget]), e o `id` original é preservado, já que é ignorado
     * no mapeamento.
     *
     * @param dto dados de atualização fornecidos pelo cliente.
     * @param usuario entidade existente a ser atualizada.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "estado", ignore = true)
    @Mapping(target = "humor", ignore = true)
    @Mapping(target = "experiencia", ignore = true)
    @Mapping(target = "nivel", ignore = true)
    @Mapping(target = "pontosVida", ignore = true)
    @Mapping(target = "dataMorte", ignore = true)
    fun updateEntityFromDTO(dto: MascoteUpdateDTO, @MappingTarget mascote: Mascote)

}