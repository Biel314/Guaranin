package br.edu.fatec.tcc.guaranin.mapper

import br.edu.fatec.tcc.guaranin.dto.UsuarioCreateDTO
import br.edu.fatec.tcc.guaranin.dto.UsuarioResponseDTO
import br.edu.fatec.tcc.guaranin.dto.UsuarioUpdateDTO
import br.edu.fatec.tcc.guaranin.model.Usuario
import org.mapstruct.*

/**
 * Mapper responsável pela conversão entre a entidade [Usuario] e seus respectivos DTOs.
 *
 * A implementação desta interface é gerada automaticamente em tempo de compilação
 * pelo MapStruct (via kapt) e registrada como um bean Spring, graças a
 * `componentModel = "spring"`.
 *
 * Campos do DTO de destino que não possuem correspondência na origem são
 * silenciosamente ignorados ([ReportingPolicy.IGNORE]), evitando warnings
 * de compilação para mapeamentos parciais intencionais (ex.: `id`, `password`).
 */
/**
 * Mapper MapStruct para conversão entre entidade [Usuario] e DTOs.
 */
@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
interface UsuarioMapper {

    /**
     * Converte uma entidade [Usuario] persistida em um [UsuarioResponseDTO],
     * adequado para ser exposto por endpoints da API (sem dados sensíveis
     * como a senha).
     *
     * @param usuario entidade de origem, já persistida no banco de dados.
     * @return DTO de saída pronto para serialização.
     */
    fun toResponseDTO(usuario: Usuario): UsuarioResponseDTO

    /**
     * Converte um [UsuarioCreateDTO], recebido na requisição de cadastro,
     * em uma nova entidade [Usuario].
     *
     * O campo `id` é ignorado no mapeamento, pois é gerado pelo banco de
     * dados na persistência. A senha mapeada aqui está em texto plano —
     * cabe à camada de serviço aplicar o hash (`PasswordEncoder`) antes
     * de salvar a entidade.
     *
     * @param usuarioCreateDTO dados de entrada para criação do usuário.
     * @return nova instância de [Usuario], ainda não persistida.
     */
    @Mapping(target = "id", ignore = true)
    fun toEntity(usuarioCreateDTO: UsuarioCreateDTO): Usuario

    /**
     * Atualiza os campos de uma entidade [Usuario] existente com os valores
     * fornecidos em um [UsuarioUpdateDTO].
     *
     * O parâmetro `usuario` é modificado in-place (anotado com
     * [MappingTarget]), e o `id` original é preservado, já que é ignorado
     * no mapeamento.
     *
     * @param dto dados de atualização fornecidos pelo cliente.
     * @param usuario entidade existente a ser atualizada.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    fun updateEntityFromDTO(dto: UsuarioUpdateDTO, @MappingTarget usuario: Usuario)

}