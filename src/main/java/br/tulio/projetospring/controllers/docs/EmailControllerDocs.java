package br.tulio.projetospring.controllers.docs;

import br.tulio.projetospring.data.dto.request.EmailRequestDTO;
import br.tulio.projetospring.file.exporter.MediaTypes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Email Endpoint")
public interface EmailControllerDocs {


    @Operation( summary = "Sends a simple e-mail",
            description = "Sends a simple e-mail by providing details, subject and body",
            //tags = {"E-mail"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),
            }

    )
    ResponseEntity<String> sendEmail(EmailRequestDTO emailRequestDTO);

    @Operation( summary = "Sends a simple e-mail with attachment",
            description = "Sends a simple e-mail with attachment by providing details, subject, body and the attachment",
            //tags = {"E-mail"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),
            }

    )
    ResponseEntity<String> sendEmailWithAttachment(String emailRequestJson, MultipartFile file);
}
