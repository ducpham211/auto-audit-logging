package io.github.ducpham211.audit.service;

import io.github.ducpham211.audit.model.AuditLog;
import io.github.ducpham211.audit.writer.AuditLogWriter;
import org.springframework.scheduling.annotation.Async;

import java.util.List;

/**
 * Dịch vụ xử lý luồng ghi log bất đồng bộ.
 */
public class AuditLogService {

    private final List<AuditLogWriter> logWriters;

    public AuditLogService(List<AuditLogWriter> logWriters) {
        this.logWriters = logWriters;
    }

    /**
     * Hàm này được thực thi trên một Thread riêng biệt (Async),
     * giúp luồng chính không bị chậm trễ khi ghi log.
     * 
     * @param auditLog thông tin cần lưu vết
     */

    @Async("auditLogThreadPool")
    public void processLog(AuditLog auditLog) {
        for (AuditLogWriter writer : logWriters) {
            writer.write(auditLog);
        }
    }
}

