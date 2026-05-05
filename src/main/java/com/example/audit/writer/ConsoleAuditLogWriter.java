package com.example.audit.writer;

import com.example.audit.model.AuditLog;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Trình ghi log mặc định, xuất thông tin log ra Console dưới định dạng JSON.
 */
public class ConsoleAuditLogWriter implements AuditLogWriter {

    private static final Logger logger = LoggerFactory.getLogger(ConsoleAuditLogWriter.class);
    private final ObjectMapper objectMapper;

    public ConsoleAuditLogWriter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void write(AuditLog auditLog) {
        try {
            String json = objectMapper.writeValueAsString(auditLog);
            logger.info("AUDIT_LOG: {}", json);
        } catch (JsonProcessingException e) {
            logger.error("Failed to serialize audit log to JSON", e);
            logger.info("AUDIT_LOG: {}", auditLog.toString());
        }
    }
}
