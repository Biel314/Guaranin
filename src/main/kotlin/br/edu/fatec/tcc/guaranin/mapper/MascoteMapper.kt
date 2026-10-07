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
    @Mapping(source = "usuario.id", target = "usuarioId")
    fun toResponseDTO(mascote: Mascote): MascoteResponseDTO

    /**
     * Converte uma entidade [Mascote] persistida em um [MascoteUpdateDTO],
     * utilizado para preencher o formulário de edição.
     *
     * @param mascote entidade de origem.
     * @return DTO de atualização correspondente.
     */
    fun toUpdateDTO(mascote: Mascote): MascoteUpdateDTO

    /**
     * Converte um [MascoteCreateDTO], recebido na requisição de cadastro,
     * em uma nova entidade [Mascote].
     *
     * O campo `id` é ignorado no mapeamento, pois é gerado pelo banco de
     * dados na persistência.
     *
     * @param mascoteCreateDTO dados de entrada para criação do mascote.
     * @return nova instância de [Mascote], ainda não persistida.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "estado", ignore = true)
    @Mapping(target = "humor", ignore = true)
    @Mapping(target = "experiencia", ignore = true)
    @Mapping(target = "nivel", ignore = true)
    @Mapping(target = "pontosVida", ignore = true)
    @Mapping(target = "dataMorte", ignore = true)
    fun toEntity(mascoteCreateDTO: MascoteCreateDTO): Mascote

    /**
     * Converte um [MascoteUpdateDTO] em uma entidade [Mascote].
     *
     * @param mascoteUpdateDTO dados de atualização de usuário.
     * @return nova instância de [Mascote].
     */
    fun toEntity(mascoteUpdateDTO: MascoteUpdateDTO): Mascote

    /**
     * Atualiza os campos de uma entidade [Mascote] existente com os valores
     * fornecidos em um [MascoteUpdateDTO].
     *
     * O parâmetro `mascote` é modificado in-place (anotado com
     * [MappingTarget]), e o `id` original é preservado, já que é ignorado
     * no mapeamento. Além disso, o `usuario`, `estado`, `humor`, `experiencia`, `nivel`,
     * `pontosVida` e `dataMorte` são administrados pelo sistema, não possuindo
     * interação direta com o usuário
     *
     * @param dto dados de atualização fornecidos pelo cliente.
     * @param mascote entidade existente a ser atualizada.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "estado", ignore = true)
    @Mapping(target = "humor", ignore = true)
    @Mapping(target = "experiencia", ignore = true)
    @Mapping(target = "nivel", ignore = true)
    @Mapping(target = "pontosVida", ignore = true)
    @Mapping(target = "dataMorte", ignore = true)
    fun updateEntityFromDTO(dto: MascoteUpdateDTO, @MappingTarget mascote: Mascote)

}