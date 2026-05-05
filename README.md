# Auto Audit Logging Starter

**Auto Audit Logging Starter** là một thư viện (plug-and-play) dành cho hệ sinh thái Spring Boot. Dự án cung cấp một cơ chế tự động ghi nhận (lưu vết) toàn bộ các thao tác thay đổi dữ liệu nhạy cảm thông qua kỹ thuật lập trình hướng khía cạnh (AOP), giúp lập trình viên không cần phải viết code ghi log thủ công trong từng hàm xử lý nghiệp vụ.

## 🚀 Tính năng nổi bật

*   **Tích hợp dễ dàng (Plug-and-play):** Hoạt động ngay lập tức chỉ với việc thêm dependency và cấu hình file `application.properties`.
*   **Không xâm lấn (Non-Invasive):** Tách biệt hoàn toàn logic ghi log khỏi logic nghiệp vụ chính bằng cách sử dụng Custom Annotation `@AutoAudit`.
*   **Thu thập dữ liệu chi tiết:** Tự động trích xuất:
    *   Tên người thực hiện (từ Spring Security Context).
    *   Tên Class và Method.
    *   Tham số đầu vào (Input Arguments).
    *   Trạng thái thực thi (Thành công / Ngoại lệ).
    *   Thời gian thực thi (Execution time).
*   **Bảo vệ dữ liệu (Data Masking):** Tự động che giấu (mask) các dữ liệu nhạy cảm như password, thẻ tín dụng trong tham số trước khi ghi log.
*   **An toàn bộ nhớ (Skip Large Objects):** Tự động bỏ qua các file dung lượng lớn (MultipartFile, byte[]) để tránh rủi ro tràn RAM (OutOfMemory).
*   **Hiệu năng cao:** Ghi log hoàn toàn **bất đồng bộ (Asynchronous)**, không làm tăng thời gian phản hồi (latency) của giao dịch chính.
*   **Linh hoạt:** Hỗ trợ đẩy log ra Console (JSON format) hoặc chèn xuống Database (PostgreSQL).

## 🛠 Yêu cầu hệ thống & Công nghệ

*   **Java:** 17+
*   **Framework:** Spring Boot 3.x, Spring AOP, Spring Data JPA
*   **Cơ chế:** Đóng gói chuẩn theo cơ chế Auto-Configuration của Spring Boot 3 (`META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`).

---

## 💻 Hướng dẫn chạy và cài đặt Local (Dành cho Developer)

Vì đây là một thư viện (Spring Boot Starter) cục bộ, bạn cần build và cài đặt nó vào Maven Local Repository trước khi sử dụng ở các project khác.

### 1. Build và Cài đặt thư viện

1.  Clone repository này về máy.
2.  Mở Terminal (hoặc CMD/PowerShell) tại thư mục gốc của project (chứa file `pom.xml`).
3.  Chạy lệnh sau để build và đẩy thư viện vào `.m2` local:

```bash
mvn clean install
```

> **Lưu ý:** Nếu bạn đang dùng IDE như IntelliJ IDEA hay Eclipse, bạn có thể mở tab **Maven** -> chọn project -> **Lifecycle** -> chạy `clean` rồi chạy `install`.

Khi Terminal thông báo `BUILD SUCCESS`, thư viện đã sẵn sàng trên máy của bạn.

---

## 📖 Hướng dẫn sử dụng trong Project khác

Sau khi đã `install` thành công ở bước trên, bạn có thể áp dụng thư viện này vào bất kỳ project Spring Boot 3 nào.

### 1. Thêm Dependency

Mở file `pom.xml` của project mà bạn muốn tích hợp, thêm dependency sau:

```xml
<dependency>
    <groupId>com.example</groupId>
    <artifactId>auto-audit-logging-starter</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

### 2. Bật tính năng Audit Logging

Thư viện sử dụng cơ chế `@ConditionalOnProperty`, do đó bạn cần kích hoạt nó một cách rõ ràng.
Trong file `application.properties` (hoặc `application.yml`) của project, thêm cấu hình:

```properties
# Kích hoạt thư viện Auto Audit Logging
audit.enabled=true
```

### 3. Sử dụng Annotation `@AutoAudit`

Bạn chỉ cần đặt annotation `@AutoAudit` lên bất kỳ phương thức Service hoặc Controller nào mà bạn muốn lưu vết thao tác:

```java
import com.example.audit.annotation.AutoAudit;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @AutoAudit
    public void updateUserProfile(Long userId, String newName) {
        // Logic nghiệp vụ của bạn ở đây...
    }
}
```

Mỗi khi hàm `updateUserProfile` được gọi, hệ thống sẽ tự động quét, lấy thông tin và ghi lại log một cách bất đồng bộ ở background.

## 🤝 Đóng góp (Contributing)
Mọi đóng góp (Pull Request, Issues) đều được chào đón để làm cho thư viện trở nên hoàn thiện hơn!