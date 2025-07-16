package br.tulio.projetospring.controllers.docs;

import br.tulio.projetospring.data.dto.security.AccountCredentialsDTO;
import br.tulio.projetospring.data.dto.security.TokenDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag( name = "Authentication Endpoint")
public interface AuthControllerDocs {

    @Operation(summary = "Authenticates an user and returns a token",
            description = "Authenticates an user and returns a token",
            //tags = {"Authentication"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200", content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = TokenDTO.class)
                    )),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),
            }
    )
    public ResponseEntity<?> signIn(@RequestBody AccountCredentialsDTO credentials);


    @Operation(summary = "Refresh token for authenticated user and returns its new token",
            description = "Refresh token for authenticated user and returns its new token",
            //tags = {"Authentication"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200", content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = TokenDTO.class)
                    )),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),
            }
    )
    public ResponseEntity<?> refreshToken(@PathVariable("username") String userName, @RequestHeader("Authorization") String refreshToken);


    @Operation(summary = "Create an user for authentication purposes ",
            description = "Create an user for authentication purposes, MUST BE REMOVED FROM COMMERCIAL PROJECTS",
            //tags = {"Authentication"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200", content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = AccountCredentialsDTO.class)
                    )),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),
            }
    )
    public ResponseEntity<AccountCredentialsDTO> createUser(@RequestBody AccountCredentialsDTO credentials);

}
