package br.tulio.projetospring.controllers.docs;

import br.tulio.projetospring.data.dto.person.UploadFileResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "File Endpoint")
public interface FileControllerDocs {

    @Operation( summary = "Upload a file",
            description = "Upload a Multipart file via attachment",
            //tags = {"File"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200", content =
                            @Content(schema = @Schema(implementation = UploadFileResponseDTO.class))
                    ),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),
            }

    )
    UploadFileResponseDTO  uploadFile(MultipartFile file);

    @Operation( summary = "Upload multiple files",
            description = "Upload multiple Multipart file via attachment",
            //tags = {"File"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200", content =
                            @Content( array = @ArraySchema( schema = @Schema(implementation = UploadFileResponseDTO.class)))
                    ),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),
            }

    )
    List<UploadFileResponseDTO> uploadMultipleFiles(MultipartFile[] files);


    @Operation( summary = "Download a file",
            description = "Download a Multipart file by giving its name and extension",
            //tags = {"File"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200", content =
                                @Content(mediaType = "application/octet-stream", schema = @Schema(type = "string", format = "binary"))
                    ),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),
            }

    )
    ResponseEntity<Resource> downloadFile(String fileName, HttpServletRequest request);

}
