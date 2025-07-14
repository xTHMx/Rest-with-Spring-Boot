package br.tulio.projetospring.controllers.docs;

import br.tulio.projetospring.data.dto.person.PersonDTO;
import br.tulio.projetospring.data.dto.person.v2.PersonDTOV2;
import br.tulio.projetospring.file.exporter.MediaTypes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.io.Resource;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PersonControllerDocs {

    @Operation(summary = "Adds a New Person",
            description = "Adds a new person by passing a JSON, XML or YAML with its data", tags = {"People"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200", content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = PersonDTO.class)
                    )),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),

            }
    )
    PersonDTO create(@RequestBody PersonDTO person);


    @Operation(summary = "Load People from a uploaded sheet ",
            description = "Load People from a upload .csv or .xlsx sheet and convert directly into DTOs", tags = {"People"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200",
                            content = @Content( schema = @Schema(implementation = PersonDTO.class))
                    ),
                    @ApiResponse(description = "No Context", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),

            }
    )
    List<PersonDTO> loadPeopleFromFile(MultipartFile file);


    @Operation(summary = "Finds a Person",
            description = "Finds a specific person by given ID", tags = {"People"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200", content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = PersonDTO.class)
                    )),
                    @ApiResponse(description = "No Context", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),

            }
    )
    PersonDTO findByID(@PathVariable("id") Long id);


    @Operation(summary = "Updates a Person",
            description = "Updates a specific person by given ID. New data must be passed as JSON, XML or YAML following the class structure.",
            tags = {"People"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200", content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = PersonDTO.class)
                    )),
                    @ApiResponse(description = "No Context", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),

            }
    )
    PersonDTO update(@RequestBody PersonDTO person);

    @Operation(summary = "Disable a Person",
            description = "Disable a specific person by given ID.",
            tags = {"People"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200", content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = PersonDTO.class)
                    )),
                    @ApiResponse(description = "No Context", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),

            }
    )
    PersonDTO disablePerson(@PathVariable("id") Long id);

    @Operation(summary = "Deletes a Specific Person",
            description = "Deletes a specific person by their ID", tags = {"People"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200", content = @Content),
                    @ApiResponse(description = "No Context", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),

            }
    )
    ResponseEntity<?> delete(@PathVariable("id") Long id);

    @Operation(summary = "Finds All People",
            description = "Finds All People", tags = {"People"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200", content = @Content(
                                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                    array = @ArraySchema(schema = @Schema(implementation = PersonDTO.class))
                                                )),
                    @ApiResponse(description = "No Context", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),

            }
    )
    ResponseEntity<PagedModel<EntityModel<PersonDTO>>> findAll(
            @RequestParam(value = "page", defaultValue = "0") Integer page, //Pagina da pesquisa
            @RequestParam(value = "size", defaultValue = "10") Integer size, //Tamanho de instancias por pagina
            @RequestParam(value = "direction", defaultValue = "asc") String direction //Pesquisa de forma ascendente ou descendente
    );

    @Operation(summary = "Finds a Person by Name",
            description = "Finds a Person by Name", tags = {"People"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200", content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = PersonDTO.class))
                    )),
                    @ApiResponse(description = "No Context", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),

            }
    )
    ResponseEntity<PagedModel<EntityModel<PersonDTO>>> findByName(
            @PathVariable(value = "firstName") String firstName,
            @RequestParam(value = "page", defaultValue = "0") Integer page, //Pagina da pesquisa
            @RequestParam(value = "size", defaultValue = "10") Integer size, //Tamanho de instancias por pagina
            @RequestParam(value = "direction", defaultValue = "asc") String direction //Pesquisa de forma ascendente ou descendente
    );



    @Operation(summary = "Export People",
            description = "Exports a page of People in .xlsx or .csv format", tags = {"People"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200", content = {
                            @Content(mediaType = MediaTypes.APPLICATION_XLSX_VALUE),
                            @Content(mediaType = MediaTypes.APPLICATION_CSV_VALUE)
                    }),
                    @ApiResponse(description = "No Context", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),

            }
    )
    ResponseEntity<Resource> exportPage(
            @RequestParam(value = "page", defaultValue = "0") Integer page, //Pagina da pesquisa
            @RequestParam(value = "size", defaultValue = "10") Integer size, //Tamanho de instancias por pagina
            @RequestParam(value = "direction", defaultValue = "asc") String direction, //Pesquisa de forma ascendente ou descendente
            HttpServletRequest request
    );


    @Operation(summary = "Adds a New Person with V2 values",
            description = "Adds a new person by passing a JSON, XML or YAML with its data. Uses V2 DTO model", tags = {"People"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200", content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = PersonDTO.class)
                    )),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),

            }
    )
    PersonDTOV2 create(@RequestBody PersonDTOV2 person);
}
