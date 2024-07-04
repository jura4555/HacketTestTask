package com.hacken.service;

import com.hacken.model.Record;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

public interface RecordService {

    public void saveCsvData(MultipartFile file, char delimiter);

    Page<Record> getAllRecordsByCriteria(String fullName, Integer age, String position, String department, int pageNum, int pageSize);

    Page<Record> getAllRecordsByText(String text, int pageNum, int pageSize);

}
