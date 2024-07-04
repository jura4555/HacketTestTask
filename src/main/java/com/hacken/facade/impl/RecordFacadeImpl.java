package com.hacken.facade.impl;

import com.hacken.dto.RecordDTO;
import com.hacken.facade.RecordFacade;
import com.hacken.mapper.RecordMapperUtil;
import com.hacken.model.Record;
import com.hacken.service.RecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;


@Component
@RequiredArgsConstructor
public class RecordFacadeImpl implements RecordFacade {

    private final RecordService recordService;

    @Override
    public void saveCsvData(MultipartFile file, char delimiter) {
        recordService.saveCsvData(file, delimiter);
    }

    @Override
    public Page<RecordDTO> getAllRecordsByCriteria(String fullName, Integer age, String position, String department, int pageNum, int pageSize) {
        Page<Record> recordPage = recordService.getAllRecordsByCriteria(fullName, age, position, department, pageNum, pageSize);
        return recordPage.map(RecordMapperUtil::toRecordDto);
    }

    @Override
    public Page<RecordDTO> getAllRecordsByText(String text, int pageNum, int pageSize) {
        Page<Record> recordPage = recordService.getAllRecordsByText(text, pageNum, pageSize);
        return recordPage.map(RecordMapperUtil::toRecordDto);
    }
}
