package com.hacken.facade;

import com.hacken.dto.RecordDTO;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

public interface RecordFacade {
    void saveCsvData(MultipartFile file, char delimiter);

    Page<RecordDTO> getAllRecordsByCriteria(String fullName, Integer age, String position, String department, int pageNum, int pageSize);

    Page<RecordDTO> getAllRecordsByText(String text, int pageNum, int pageSize);

}
