package io.github.ducpham211.audit.provider;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Lấy thông tin user từ Spring Security.
 * Lớp này chỉ được tạo nếu project sử dụng Spring Security.
 */
public class SecurityAuditUserProvider implements AuditUserProvider {
    @Override
    public String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !authentication.getPrincipal().equals("anonymousUser")) {
            return authentication.getName();
        }
        return "ANONYMOUS";
    }
}

