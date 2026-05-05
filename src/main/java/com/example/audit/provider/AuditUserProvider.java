package com.example.audit.provider;

/**
 * Giao diện cung cấp thông tin người dùng đang thực hiện thao tác.
 */
public interface AuditUserProvider {
    String getCurrentUsername();
}
