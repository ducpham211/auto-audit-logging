package io.github.ducpham211.audit.writer;

import io.github.ducpham211.audit.model.AuditLog;

/**
 * Giao diện để ghi (xuất) log ra các hệ thống lưu trữ khác nhau.
 * Cho phép linh hoạt giữa việc ghi Console, Database, Kafka, v.v.
 */
public interface AuditLogWriter {
    void write(AuditLog auditLog);
}

