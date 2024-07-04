package com.hacken.controller;

import com.hacken.actuator.CsvUploadMetrics;
import com.hacken.dto.RecordDTO;
import com.hacken.exception.CustomError;
import com.hacken.facade.RecordFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Controller
@RequiredArgsConstructor
@Tag(name = "Record Management", description = "Endpoints for managing records")
public class RecordController {

    private final RecordFacade recordFacade;

    private final CsvUploadMetrics csvUploadMetrics;

    @Operation(
            summary = "Upload CSV file",
            description = "Uploads a CSV file and saves the data to the database. You can use such delimiters as ',', ';', '|', ':', '-', '.' ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "File uploaded and data saved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid delimiter",
                    content = @Content(schema = @Schema(implementation = CustomError.class))),
            @ApiResponse(responseCode = "400", description = "Failed to upload file",
                    content = @Content(schema = @Schema(implementation = CustomError.class)))
    })
    @PostMapping(value = "/records/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadCsv(@Parameter(name = "CSV file to upload", required = true)
                                            @RequestPart("file") MultipartFile file,
                                            @Parameter(name = "delimiter", example = ",")
                                            @RequestParam("delimiter") char delimiter) {
        log.info("Attempting to save data from MultipartFile");
        recordFacade.saveCsvData(file, delimiter);
        csvUploadMetrics.incrementCsvUpload();
        log.info("Successfully saved records to the database.");
        return ResponseEntity.status(HttpStatus.CREATED).body("File uploaded and data saved successfully");
    }

    @GetMapping("/records/search")
    @Operation(
            summary = "Search records by criteria",
            description = "Retrieves records based on provided search criteria. Supports filtering by full name, age, position, and department."
    )
    public ResponseEntity<Page<RecordDTO>> getAllRecordsByCriteria(
            @Parameter(name = "Full name to search") @RequestParam(required = false, defaultValue = "") String fullName,
            @Parameter(name = "Age to search") @RequestParam(required = false) Integer age,
            @Parameter(name = "Position to search") @RequestParam(required = false, defaultValue = "") String position,
            @Parameter(name = "Department to search") @RequestParam(required = false, defaultValue = "") String department,
            @Parameter(name = "Page number (1-based)") @RequestParam(defaultValue = "1")
            @Min(value = 1, message = "page should be greater or equals one") int pageNum,
            @Parameter(name = "Page size (1-based)") @RequestParam(defaultValue = "1")
            @Min(value = 1, message = "page size should be greater or equals one") int pageSize) {
        log.info("Entering getAllRecordsByCriteria with parameters - fullName: {}, age: {}, position: {}, department: {}, pageNum: {}, pageSize: {}",
                fullName, age, position, department, pageNum, pageSize);
        Page<RecordDTO> records = recordFacade.getAllRecordsByCriteria(fullName, age, position, department, pageNum, pageSize);
        log.info("Found {} records matching criteria and in this page is {} records", records.getTotalElements(), records.getContent().size());
        return new ResponseEntity<>(records, HttpStatus.OK);
    }

    @GetMapping("/records/text")
    @Operation(
            summary = "Search records by text",
            description = "Retrieves records based on provided text, searching across multiple fields."
    )
    public ResponseEntity<Page<RecordDTO>> getAllRecordsByText(
            @Parameter(name = "Text to search for in records") @RequestParam(defaultValue = "") String text,
            @Parameter(name = "Page number (1-based)") @RequestParam(defaultValue = "1")
            @Min(value = 1, message = "page should be greater or equals one") int pageNum,
            @Parameter(name = "Page size (1-based)") @RequestParam(defaultValue = "1")
            @Min(value = 1, message = "page size should be greater or equals one") int pageSize) {
        log.info("Entering getAllRecordsByText with parameters - text: {}, pageNum: {}, pageSize: {}",
                text, pageNum, pageSize);
        Page<RecordDTO> records = recordFacade.getAllRecordsByText(text, pageNum, pageSize);
        log.info("Found {} records matching criteria and in this page is {} records", records.getTotalElements(), records.getContent().size());
        return new ResponseEntity<>(records, HttpStatus.OK);
    }

}
