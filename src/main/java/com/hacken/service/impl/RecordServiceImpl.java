package com.hacken.service.impl;

import com.hacken.exception.CsvParsingException;
import com.hacken.exception.FileUploadException;
import com.hacken.exception.InvalidDelimiterException;
import com.hacken.model.Record;
import com.hacken.repository.RecordRepository;
import com.hacken.service.RecordService;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecordServiceImpl implements RecordService {

    private final RecordRepository recordRepository;

    @Override
    @Transactional
    public void saveCsvData(MultipartFile file, char delimiter) {
        log.info("Attempting to save data from MultipartFile");
        List<Record> dataList = parseCsv(file, delimiter);
        log.info("Successfully saved records to the database.");
        recordRepository.saveAll(dataList);
    }

    private List<Record> parseCsv(MultipartFile file, char delimiter) {
        List<Record> recordList;
        try {
            checkFileNotEmpty(file);
            validateDelimiter(delimiter);
            BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()));
            CSVParser csvParser = getCsvParser(delimiter, br);
            recordList = processCsvRecords(csvParser);
        } catch (IOException e) {
            log.error("Failed to parse CSV file");
            throw new CsvParsingException("Failed to parse CSV file");
        }
        return recordList;
    }

    private void checkFileNotEmpty(MultipartFile file) {
        if (file.isEmpty()) {
            log.error("File upload failed: file is empty");
            throw new FileUploadException("Failed to upload file");
        }
    }

    private void validateDelimiter(char delimiter) {
        List<Character> validDelimiters = Arrays.asList(',', ';', '|', ':', '-', '.');
        if (!validDelimiters.contains(delimiter)) {
            log.error("Invalid delimiter: {}", delimiter);
            throw new InvalidDelimiterException("Invalid delimiter");
        }
    }

    private CSVParser getCsvParser(char delimiter, BufferedReader br) throws IOException {
        CSVFormat csvFormat = CSVFormat.Builder.create().setDelimiter(delimiter).build();
        return new CSVParser(br, csvFormat);
    }

    private List<Record> processCsvRecords(CSVParser csvParser) {
        List<Record> records = new ArrayList<>();
        for (CSVRecord record : csvParser) {
            Record data = new Record(null, record.get(0), Integer.parseInt(record.get(1)), record.get(2), record.get(3));
            records.add(data);
        }
        return records;
    }

    @Override
    public Page<Record> getAllRecordsByCriteria(String fullName, Integer age, String position, String department, int pageNum, int pageSize) {
        log.info("Entering getAllRecordsByCriteria with parameters - fullName: {}, age: {}, position: {}, department: {}, pageNum: {}, pageSize: {}",
                fullName, age, position, department, pageNum, pageSize);
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, Sort.by("id"));
        Page<Record> result = recordRepository.findAll((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(buildFullNamePredicate(root, criteriaBuilder, fullName));
            predicates.add(buildAgePredicate(root, criteriaBuilder, age));
            predicates.add(buildPositionPredicate(root, criteriaBuilder, position));
            predicates.add(buildDepartmentPredicate(root, criteriaBuilder, department));
            predicates.removeIf(Objects::isNull);
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        }, pageable);
        log.info("Found {} records matching criteria and in this page is {} records", result.getTotalElements(), result.getContent().size());
        return result;
    }


    private Predicate buildFullNamePredicate(Root<Record> root, CriteriaBuilder criteriaBuilder, String fullName) {
        if (fullName != null && !fullName.isEmpty()) {
            return criteriaBuilder.equal(root.get("fullName"), fullName);
        }
        return null;
    }

    private Predicate buildAgePredicate(Root<Record> root, CriteriaBuilder criteriaBuilder, Integer age) {
        if (age != null) {
            return criteriaBuilder.equal(root.get("age"), age);
        }
        return null;
    }

    private Predicate buildPositionPredicate(Root<Record> root, CriteriaBuilder criteriaBuilder, String position) {
        if (position != null && !position.isEmpty()) {
            return criteriaBuilder.equal(root.get("position"), position);
        }
        return null;
    }

    private Predicate buildDepartmentPredicate(Root<Record> root, CriteriaBuilder criteriaBuilder, String department) {
        if (department != null && !department.isEmpty()) {
            return criteriaBuilder.equal(root.get("department"), department);
        }
        return null;
    }

    @Override
    public Page<Record> getAllRecordsByText(String text, int pageNum, int pageSize) {
        log.info("Entering getAllRecordsByText with parameters - text: {}, pageNum: {}, pageSize: {}",
                text, pageNum, pageSize);
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize,
                Sort.by("id"));
        Page<Record> result = recordRepository.findAll((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(buildFullTextPredicate(root, criteriaBuilder, text));
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        }, pageable);
        log.info("Found {} records matching criteria and in this page is {} records", result.getTotalElements(), result.getContent().size());
        return result;
    }

    private Predicate buildFullTextPredicate(Root<Record> root, CriteriaBuilder criteriaBuilder, String text) {
        if (text != null && !text.isEmpty()) {
            String lowerText = "%" + text.toLowerCase() + "%";
            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("fullName")), lowerText),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("position")), lowerText),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("department")), lowerText)
            );
        }
        return null;
    }
}
