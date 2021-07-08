package com.bas.processing.config;


import org.springframework.batch.item.file.separator.SimpleRecordSeparatorPolicy;

public class RecordSeparatorPolicy extends SimpleRecordSeparatorPolicy {

    /**
     此方法实现跳过空行
     */
    @Override
    public boolean isEndOfRecord(String line) {
        return line.trim().length() != 0 && super.isEndOfRecord(line);
    }

    /**
     此方法实现遇到空行则结束
     */
    @Override
    public String postProcess(String record) {
        if (record == null || record.trim().length() == 0) {
            return null;
        }
        return super.postProcess(record);
    }
}