package com.hacken.mapper;

import com.hacken.dto.RecordDTO;
import com.hacken.model.Record;
import lombok.experimental.UtilityClass;

@UtilityClass
public class RecordMapperUtil {

    public RecordDTO toRecordDto(Record record) {
        return new RecordDTO()
                .setFullName(record.getFullName())
                .setAge(record.getAge())
                .setPosition(record.getPosition())
                .setDepartment(record.getDepartment());
    }
}
