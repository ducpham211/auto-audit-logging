package com.example.audit.writer;

import com.example.audit.model.AuditLog;

/**
 * Giao diện để ghi (xuất) log ra các hệ thống lưu trữ khác nhau.
 * Cho phép linh hoạt giữa việc ghi Console, Database, Kafka, v.v.
 */
public interface AuditLogWriter {
    void write(AuditLog auditLog);
}
