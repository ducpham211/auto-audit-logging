package io.github.ducpham211.audit.provider;

/**
 * Triển khai mặc định khi ứng dụng không cấu hình cơ chế xác thực.
 */
public class DefaultAuditUserProvider implements AuditUserProvider {
    @Override
    public String getCurrentUsername() {
        return "SYSTEM";
    }
}

