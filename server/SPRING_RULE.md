# Java Spring Boot - Coding Convention

> **Mục đích:** Tài liệu này định nghĩa các quy tắc code chuẩn cho dự án Java Spring Boot, đảm bảo tính nhất quán, dễ bảo trì và tuân thủ best practices hiện đại.

---

## 1. Cấu trúc dự án (Project Structure)

### 1.1. Tổ chức Package - By Feature

**Quy tắc:** Ưu tiên tổ chức theo **feature/module** thay vì theo layer (controller, service, repository).

```
com.company.project
├── user
│   ├── User.java (Entity)
│   ├── UserRepository.java
│   ├── UserService.java
│   ├── UserController.java
│   ├── dto
│   │   ├── UserRequestDto.java
│   │   └── UserResponseDto.java
├── order
│   ├── Order.java
│   ├── OrderRepository.java
│   ├── OrderService.java
│   ├── OrderController.java
│   └── dto
│       ├── OrderRequestDto.java
│       └── OrderResponseDto.java
├── common
│   ├── config
│   ├── exception
│   └── util
└── Application.java
```

**Tại sao?**

- Dễ tìm kiếm: Tất cả code liên quan đến một tính năng nằm trong cùng một package.
- Tăng tính modular: Dễ tách feature thành microservice riêng nếu cần.
- Giảm coupling: Các feature ít phụ thuộc lẫn nhau.

---

## 2. Spring Core - Dependency Injection

### 2.1. Constructor Injection (Bắt buộc)

**Quy tắc:** Luôn sử dụng **Constructor Injection**, tránh `@Autowired` trên field.

✅ **Đúng:**

```java
@Service
public class UserService {
    private final UserRepository userRepository;
    private final EmailService emailService;

    public UserService(UserRepository userRepository, EmailService emailService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
    }
}
```

❌ **Sai:**

```java
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
}
```

**Tại sao?**

- **Immutability:** Dependencies được final, không thể thay đổi sau khi khởi tạo.
- **Testability:** Dễ dàng mock dependencies trong Unit Test.
- **Null Safety:** Đảm bảo dependencies không bao giờ null.

### 2.2. Phân rõ vai trò Annotation

| Annotation    | Mục đích       | Ví dụ                               |
| ------------- | -------------- | ----------------------------------- |
| `@Service`    | Business logic | `UserService`, `OrderService`       |
| `@Repository` | Data access    | `UserRepository`, `OrderRepository` |
| `@Component`  | Bean chung     | `JwtTokenProvider`, `FileUtils`     |

---

## 3. Spring Boot Configuration

### 3.1. File cấu hình - application.yml

**Quy tắc:** Sử dụng `.yml` thay vì `.properties`.

```yaml
# application.yml
spring:
  application:
    name: my-project
  datasource:
    url: jdbc:mysql://localhost:3306/mydb
    username: root
    password: ${DB_PASSWORD}

app:
  jwt:
    secret-key: ${JWT_SECRET}
    expiration-ms: 86400000
```

**Tại sao?**

- Cấu trúc phân cấp rõ ràng, dễ đọc hơn `.properties`.
- Hỗ trợ nested properties tự nhiên.

### 3.2. Spring Profiles

**Quy tắc:** Tách biệt config cho từng môi trường:

- `application.yml` - Config chung
- `application-dev.yml` - Development
- `application-staging.yml` - Staging
- `application-prod.yml` - Production

**Kích hoạt profile:**

```bash
# Via command line
java -jar app.jar --spring.profiles.active=prod

# Via environment variable
export SPRING_PROFILES_ACTIVE=prod
```

### 3.3. Type-safe Configuration với @ConfigurationProperties

✅ **Đúng:**

```java
@Configuration
@ConfigurationProperties(prefix = "app.jwt")
@Validated
public class JwtProperties {
    @NotBlank
    private String secretKey;

    @Min(3600000) // Tối thiểu 1 giờ
    private long expirationMs;

    // Getters and Setters
}
```

**Tại sao?**

- Type-safe: Compiler kiểm tra kiểu dữ liệu.
- Validation: Tích hợp Jakarta Validation.
- Autocomplete: IDE hỗ trợ gợi ý tốt hơn.

---

## 4. Spring Web - REST API Design

### 4.1. Thiết kế Endpoint

**Quy tắc:**

- Sử dụng **danh từ số nhiều** (`/users`, `/orders`)
- Sử dụng **HTTP verbs** đúng mục đích
- Trả về **status code** chính xác

| HTTP Method | Endpoint      | Mục đích          | Status Code             |
| ----------- | ------------- | ----------------- | ----------------------- |
| GET         | `/users`      | Lấy danh sách     | 200 OK                  |
| GET         | `/users/{id}` | Lấy chi tiết      | 200 OK / 404 Not Found  |
| POST        | `/users`      | Tạo mới           | 201 Created             |
| PUT         | `/users/{id}` | Cập nhật toàn bộ  | 200 OK / 204 No Content |
| PATCH       | `/users/{id}` | Cập nhật một phần | 200 OK                  |
| DELETE      | `/users/{id}` | Xóa               | 204 No Content          |

### 4.2. Sử dụng DTOs (Bắt buộc)

**Quy tắc:** Không bao giờ expose JPA Entity ra ngoài API.

✅ **Đúng:**

```java
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(
        @Valid @RequestBody UserRequestDto request
    ) {
        UserResponseDto response = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
```

**Tại sao?**

- **Bảo mật:** Tránh lộ thông tin nhạy cảm (password hash, internal IDs).
- **Flexibility:** API contract độc lập với database schema.
- **Tránh N+1 query:** Lazy loading relationships không hoạt động ngoài transaction.

---

## 5. Database - MySQL & Spring Data JPA

### 5.1. Quy tắc đặt tên

| Loại         | Convention   | Ví dụ                               |
| ------------ | ------------ | ----------------------------------- |
| Table        | `snake_case` | `user_orders`, `product_categories` |
| Column       | `snake_case` | `first_name`, `created_at`          |
| JPA Entity   | `PascalCase` | `UserOrder`, `ProductCategory`      |
| Entity Field | `camelCase`  | `firstName`, `createdAt`            |

### 5.2. JPA Entity Best Practices

```java
@Entity
@Table(name = "users")
@Getter @Setter
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore // Tránh circular reference
    private List<Order> orders = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
```

**Lưu ý:**

- ⚠️ Tránh `@Data` của Lombok với JPA Entity (gây vấn đề với `equals`/`hashCode`).
- ✅ Sử dụng `@Getter`, `@Setter`, `@NoArgsConstructor` riêng lẻ.

### 5.3. FetchType - Luôn dùng LAZY

**Quy tắc:** Mặc định luôn dùng `FetchType.LAZY` cho relationships.

```java
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "user_id")
private User user;
```

**Tại sao?**

- Tránh N+1 query problem.
- Chỉ load data khi thực sự cần.
- Sử dụng `@EntityGraph` hoặc `JOIN FETCH` khi cần eager load.

---

## 6. Spring Security - JWT Authentication

### 6.1. Cấu trúc JWT

```
com.company.project
└── security
    ├── SecurityConfig.java
    ├── JwtAuthenticationFilter.java
    ├── JwtTokenProvider.java
    └── CustomUserDetailsService.java
```

### 6.2. SecurityFilterChain Configuration

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/**", "/health").permitAll()
                .anyRequest().authenticated()
            )
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
```

### 6.3. JWT Token Provider

```java
@Component
public class JwtTokenProvider {
    private final JwtProperties jwtProperties;

    public JwtTokenProvider(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        // ⚠️ KHÔNG bao giờ lưu password vào JWT
        return Jwts.builder()
            .setClaims(claims)
            .setSubject(userDetails.getUsername())
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + jwtProperties.getExpirationMs()))
            .signWith(getSigningKey(), SignatureAlgorithm.HS256)
            .compact();
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.getSecretKey());
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
```

### 6.4. JWT Authentication Filter

```java
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider,
                                  UserDetailsService userDetailsService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                   HttpServletResponse response,
                                   FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            String username = jwtTokenProvider.extractUsername(jwt);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                if (jwtTokenProvider.validateToken(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities()
                        );
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}
```

**Lưu ý bảo mật:**

- ✅ Lưu secret key trong environment variable hoặc secret manager.
- ✅ Sử dụng token expiration hợp lý (15-60 phút cho access token).
- ✅ Implement refresh token mechanism.
- ❌ KHÔNG lưu password, credit card, hoặc dữ liệu nhạy cảm trong JWT payload.

---

## 8. Object Storage với MinIO

### 8.1. Configuration

```yaml
# application.yml
minio:
  endpoint: http://localhost:9000
  access-key: ${MINIO_ACCESS_KEY}
  secret-key: ${MINIO_SECRET_KEY}
  bucket-name: my-project-bucket
```

```java
@Configuration
public class MinioConfig {
    @Value("${minio.endpoint}")
    private String endpoint;

    @Value("${minio.access-key}")
    private String accessKey;

    @Value("${minio.secret-key}")
    private String secretKey;

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
            .endpoint(endpoint)
            .credentials(accessKey, secretKey)
            .build();
    }
}
```

### 8.2. File Storage Service

```java
@Service
@Slf4j
public class MinioStorageService {
    private final MinioClient minioClient;

    @Value("${minio.bucket-name}")
    private String bucketName;

    public MinioStorageService(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    public String uploadFile(MultipartFile file, String folder) {
        try {
            // Generate unique filename
            String filename = String.format("%s/%s-%s",
                folder,
                UUID.randomUUID(),
                file.getOriginalFilename()
            );

            // Upload to MinIO
            minioClient.putObject(
                PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(filename)
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build()
            );

            log.info("File uploaded successfully: {}", filename);
            return filename;

        } catch (Exception e) {
            log.error("Error uploading file to MinIO", e);
            throw new FileStorageException("Could not upload file", e);
        }
    }

    public InputStream downloadFile(String filename) {
        try {
            return minioClient.getObject(
                GetObjectArgs.builder()
                    .bucket(bucketName)
                    .object(filename)
                    .build()
            );
        } catch (Exception e) {
            log.error("Error downloading file from MinIO", e);
            throw new FileStorageException("Could not download file", e);
        }
    }

    public void deleteFile(String filename) {
        try {
            minioClient.removeObject(
                RemoveObjectArgs.builder()
                    .bucket(bucketName)
                    .object(filename)
                    .build()
            );
            log.info("File deleted successfully: {}", filename);
        } catch (Exception e) {
            log.error("Error deleting file from MinIO", e);
            throw new FileStorageException("Could not delete file", e);
        }
    }
}
```

### 8.3. Controller Example

```java
@RestController
@RequestMapping("/files")
public class FileController {
    private final MinioStorageService storageService;

    public FileController(MinioStorageService storageService) {
        this.storageService = storageService;
    }

    @PostMapping("/upload")
    public ResponseEntity<FileUploadResponse> uploadFile(
        @RequestParam("file") MultipartFile file
    ) {
        String fileUrl = storageService.uploadFile(file, "documents");
        return ResponseEntity.ok(new FileUploadResponse(fileUrl));
    }
}
```

**Quy tắc đặt tên:**

- Bucket: `[project-name]-[environment]` (vd: `myapp-prod`, `myapp-dev`)
- Object key: `[folder]/[uuid]-[filename]` (vd: `avatars/123e4567-user.jpg`)

---

## 9. Build Tools & CI/CD

### 9.1. Dockerfile - Multi-stage Build

```dockerfile
# Stage 1: Build
FROM eclipse-temurin:17-jdk-alpine AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Runtime
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Create non-root user
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

# Copy JAR from builder stage
COPY --from=builder /app/target/*.jar app.jar

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

**Tại sao multi-stage build?**

- Giảm kích thước image (chỉ chứa JRE, không có JDK và Maven).
- Tăng bảo mật (không chứa source code và build tools trong image cuối).

### 9.3. Maven/Gradle Profiles

```xml
<!-- pom.xml -->
<profiles>
    <profile>
        <id>dev</id>
        <activation>
            <activeByDefault>true</activeByDefault>
        </activation>
        <properties>
            <spring.profiles.active>dev</spring.profiles.active>
        </properties>
    </profile>

    <profile>
        <id>prod</id>
        <properties>
            <spring.profiles.active>prod</spring.profiles.active>
        </properties>
    </profile>
</profiles>
```

**Build cho production:**

```bash
mvn clean package -Pprod
```

---

## 10. DTOs & Validation

### 10.1. Sử dụng Java Records (Java 17+)

```java
// Request DTO
public record CreateUserRequest(
    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 50)
    String firstName,

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    String email,

    @NotBlank
    @Size(min = 8, message = "Password must be at least 8 characters")
    String password
) {}

// Response DTO
public record UserResponse(
    Long id,
    String firstName,
    String email,
    LocalDateTime createdAt
) {}
```

### 10.2. Validation trong Controller

```java
@RestController
@RequestMapping("/users")
@Validated
public class UserController {

    @PostMapping
    public ResponseEntity<UserResponse> createUser(
        @Valid @RequestBody CreateUserRequest request
    ) {
        // Validation tự động xảy ra trước khi vào method
        // ...
    }
}
```

**Tại sao dùng Records?**

- Immutable by default (tất cả fields là final).
- Tự động generate constructor, `equals()`, `hashCode()`, `toString()`.
- Code ngắn gọn hơn class truyền thống.

### 10.3. Ánh xạ DTO (DTO Mapping) với MapStruct

**Quy tắc**: Sử dụng MapStruct để tự động sinh code mapping (ánh xạ) giữa JPA Entities và DTOs. Tránh việc mapping thủ công (viết code getter/setter).

**Tại sao?**

- Giảm code boilerplate: Tự động hóa việc copy A -> B nhàm chán và dễ sai sót.
- Hiệu năng cao: MapStruct sinh code Java tại thời điểm compile (build), không dùng reflection lúc runtime. Nó chạy nhanh như code bạn tự viết tay.
- Type-safe: Trình biên dịch sẽ báo lỗi nếu mapping bị sai (ví dụ: kiểu dữ liệu không khớp), thay vì lỗi lúc runtime.
- Tích hợp Spring: Với @Mapper(componentModel = "spring"), MapStruct tự tạo ra một Spring Bean, bạn chỉ cần inject và sử dụng.

---

## 11. Exception Handling

### 11.1. Global Exception Handler

```java
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(
        ResourceNotFoundException ex
    ) {
        log.error("Resource not found: {}", ex.getMessage());
        ErrorResponse error = new ErrorResponse(
            HttpStatus.NOT_FOUND.value(),
            ex.getMessage(),
            LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
        MethodArgumentNotValidException ex
    ) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
            errors.put(error.getField(), error.getDefaultMessage())
        );

        ErrorResponse error = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            "Validation failed",
            LocalDateTime.now(),
            errors
        );
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        log.error("Unexpected error occurred", ex);
        ErrorResponse error = new ErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "An unexpected error occurred",
            LocalDateTime.now()
        );
        return ResponseEntity.internalServerError().body(error);
    }
}
```

### 11.2. Custom Exception

```java
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String resource, Long id) {
        super(String.format("%s with id %d not found", resource, id));
    }
}
```

---

## 14. Lombok - Best Practices

### 14.1. Sử dụng Annotation phù hợp

| Annotation                 | Sử dụng cho              | Lưu ý                              |
| -------------------------- | ------------------------ | ---------------------------------- |
| `@Data`                    | DTOs, POJOs đơn giản     | ⚠️ TRÁNH dùng với JPA Entities     |
| `@Getter` `@Setter`        | JPA Entities             | Kiểm soát tốt hơn                  |
| `@Builder`                 | DTOs, Test Data          | Tạo object dễ dàng                 |
| `@Slf4j`                   | Tất cả các class cần log | Inject logger tự động              |
| `@NoArgsConstructor`       | JPA Entities (bắt buộc)  | JPA yêu cầu default constructor    |
| `@AllArgsConstructor`      | DTOs, Builder pattern    | Kết hợp với `@Builder`             |
| `@RequiredArgsConstructor` | Dependency Injection     | Tạo constructor cho `final` fields |

### 14.2. Ví dụ sử dụng

```java
// DTO - Dùng @Data và @Builder
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private Long id;
    private String name;
    private BigDecimal price;
}

// JPA Entity - Dùng @Getter/@Setter riêng lẻ
@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private BigDecimal price;
}

// Service - Dùng @Slf4j và @RequiredArgsConstructor
@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public ProductDto getProduct(Long id) {
        log.debug("Fetching product: {}", id);
        // ...
    }
}
```

**Tại sao KHÔNG dùng @Data với JPA Entity?**

- `@Data` tự động generate `equals()` và `hashCode()` trên tất cả fields.
- Với JPA relationships, điều này có thể gây circular reference và performance issues.
- Nên tự implement hoặc dùng chỉ `id` field cho `equals`/`hashCode`.

---

## 15. Transaction Management

### 15.1. Quy tắc sử dụng @Transactional

```java
@Service
@Transactional(readOnly = true) // Default cho toàn class
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final EmailService emailService;

    public OrderService(OrderRepository orderRepository,
                       ProductRepository productRepository,
                       EmailService emailService) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.emailService = emailService;
    }

    // Read-only method (sử dụng default của class)
    public OrderDto getOrder(Long id) {
        return orderRepository.findById(id)
            .map(this::mapToDto)
            .orElseThrow(() -> new ResourceNotFoundException("Order", id));
    }

    // Write method - override với readOnly = false
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public OrderDto createOrder(CreateOrderRequest request) {
        // Kiểm tra tồn kho
        Product product = productRepository.findById(request.productId())
            .orElseThrow(() -> new ResourceNotFoundException("Product", request.productId()));

        if (product.getStock() < request.quantity()) {
            throw new InsufficientStockException("Not enough stock");
        }

        // Tạo order
        Order order = new Order();
        order.setProduct(product);
        order.setQuantity(request.quantity());

        // Giảm tồn kho
        product.setStock(product.getStock() - request.quantity());
        productRepository.save(product);

        Order savedOrder = orderRepository.save(order);

        // ⚠️ External call nên đặt NGOÀI transaction nếu có thể
        // hoặc sử dụng @TransactionalEventListener
        emailService.sendOrderConfirmation(savedOrder);

        return mapToDto(savedOrder);
    }
}
```

### 15.2. Best Practices

**✅ Nên làm:**

- Đặt `@Transactional(readOnly = true)` ở class level cho read-heavy services.
- Override với `readOnly = false` cho write methods.
- Sử dụng `rollbackFor = Exception.class` để rollback cả checked exceptions.
- Giữ transaction ngắn gọn (tránh call external APIs trong transaction).

**❌ Không nên:**

- Đặt `@Transactional` trên private methods (không hoạt động).
- Call method có `@Transactional` từ cùng class (proxy không hoạt động).
- Để transaction chạy quá lâu (giữ database connection).

---

## 16. API Versioning

### 16.1. URI Versioning (Khuyến nghị)

```java
@RestController
@RequestMapping("/api/v1/users")
public class UserControllerV1 {
    @GetMapping
    public List<UserDto> getUsers() {
        // Version 1 logic
    }
}

@RestController
@RequestMapping("/api/v2/users")
public class UserControllerV2 {
    @GetMapping
    public PagedResponse<UserDto> getUsers(Pageable pageable) {
        // Version 2 logic với pagination
    }
}
```

**Tại sao URI Versioning?**

- Dễ hiểu và dễ test (có thể test trực tiếp từ browser).
- Dễ route trong API Gateway/Load Balancer.
- Rõ ràng cho documentation (Swagger).

---

## 17. Pagination & Sorting

### 17.1. Sử dụng Spring Data Pageable

```java
@RestController
@RequestMapping("/api/v1/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<Page<ProductDto>> getProducts(
        @RequestParam(required = false) String search,
        @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC)
        Pageable pageable
    ) {
        Page<ProductDto> products = productService.getProducts(search, pageable);
        return ResponseEntity.ok(products);
    }
}
```

**URL Examples:**

```
GET /api/v1/products?page=0&size=10
GET /api/v1/products?page=0&size=10&sort=name,asc
GET /api/v1/products?page=0&size=10&sort=price,desc&sort=name,asc
GET /api/v1/products?search=laptop&page=0&size=20
```

### 17.2. Service Implementation

```java
@Service
@Transactional(readOnly = true)
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Page<ProductDto> getProducts(String search, Pageable pageable) {
        Page<Product> products;

        if (search != null && !search.isBlank()) {
            products = productRepository.findByNameContainingIgnoreCase(search, pageable);
        } else {
            products = productRepository.findAll(pageable);
        }

        return products.map(this::mapToDto);
    }
}
```

---

## 19. Environment Variables & Secrets Management

### 19.1. Quy tắc đặt tên Environment Variables

**Format:** `APP_[FEATURE]_[PROPERTY]` (UPPERCASE, separated by underscore)

```bash
# Database
DB_HOST=localhost
DB_PORT=3306
DB_NAME=mydb
DB_USERNAME=root
DB_PASSWORD=secretpassword

# JWT
JWT_SECRET_KEY=your-secret-key-here-min-256-bits
JWT_EXPIRATION_MS=86400000

# Redis
REDIS_HOST=localhost
REDIS_PORT=6379
REDIS_PASSWORD=redis-password

# MinIO
MINIO_ENDPOINT=http://localhost:9000
MINIO_ACCESS_KEY=minioadmin
MINIO_SECRET_KEY=minioadmin
MINIO_BUCKET_NAME=my-bucket
```

### 19.2. Mapping trong application.yml

```yaml
spring:
  datasource:
    url: jdbc:mysql://${DB_HOST:localhost}:${DB_PORT:3306}/${DB_NAME:mydb}
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}

app:
  jwt:
    secret-key: ${JWT_SECRET_KEY}
    expiration-ms: ${JWT_EXPIRATION_MS:86400000}
```

**Lưu ý:**

- ✅ Sử dụng default values với syntax `${VAR:default}`.
- ❌ KHÔNG commit sensitive data vào Git.
- ✅ Sử dụng `.env` file cho local development (thêm vào `.gitignore`).

---

## 20. Performance Optimization

### 20.1. N+1 Query Problem

**❌ Sai (gây N+1 query):**

```java
@GetMapping("/orders")
public List<OrderDto> getOrders() {
    List<Order> orders = orderRepository.findAll();
    return orders.stream()
        .map(order -> {
            // Mỗi order sẽ trigger 1 query để load user
            String userName = order.getUser().getName();
            return new OrderDto(order.getId(), userName);
        })
        .toList();
}
```

**✅ Đúng (dùng JOIN FETCH):**

```java
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT o FROM Order o JOIN FETCH o.user")
    List<Order> findAllWithUser();
}
```

### 20.2. Database Indexing

```sql
-- Migration file: V5__add_indexes.sql
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_orders_user_id ON orders(user_id);
CREATE INDEX idx_orders_created_at ON orders(created_at);
CREATE INDEX idx_products_category_name ON products(category, name);
```

**Khi nào cần index?**

- Cột được sử dụng trong WHERE clause thường xuyên.
- Cột được sử dụng trong JOIN conditions.
- Cột được sử dụng cho sorting (ORDER BY).

### 20.3. Connection Pooling

```yaml
spring:
  datasource:
    hikari:
      maximum-pool-size: 10
      minimum-idle: 5
      connection-timeout: 30000
      idle-timeout: 600000
      max-lifetime: 1800000
```

---

## 21. Code Review Checklist

Trước khi submit Pull Request, đảm bảo:

### 21.1. Code Quality

- [ ] Code tuân thủ convention này
- [ ] Không có hardcoded values (config, credentials)
- [ ] Không có commented code
- [ ] Không có `System.out.println()` hoặc `printStackTrace()`
- [ ] Variable/method names có ý nghĩa và rõ ràng

### 21.2. Security

- [ ] Không expose JPA Entities ra API
- [ ] Input validation với `@Valid`
- [ ] Sensitive data không được log
- [ ] JWT secret không hardcode
- [ ] SQL injection được prevent (dùng Parameterized Query)

### 21.3. Database

- [ ] Migration script được tạo cho mọi schema changes
- [ ] Relationships sử dụng `FetchType.LAZY`
- [ ] Có index cho các cột được query thường xuyên

### 21.4. Testing

- [ ] Unit tests cho business logic
- [ ] Integration tests cho critical flows
- [ ] Test coverage >= 80% cho Service layer
- [ ] Happy path và error cases được test

### 21.5. Documentation

- [ ] Public methods có JavaDoc
- [ ] API endpoints có OpenAPI annotations
- [ ] README được update nếu có thay đổi setup

---

## 22. Common Mistakes & How to Avoid

### 22.1. Lỗi thường gặp

| Lỗi                          | Hậu quả                       | Giải pháp                     |
| ---------------------------- | ----------------------------- | ----------------------------- |
| Dùng `@Autowired` trên field | Khó test, không immutable     | Constructor Injection         |
| Expose Entity ra API         | Tight coupling, security risk | Luôn dùng DTOs                |
| Không dùng Migration         | DB inconsistency              | Flyway/Liquibase bắt buộc     |
| `FetchType.EAGER` everywhere | N+1 query, performance        | Dùng LAZY, JOIN FETCH khi cần |
| Hardcode config values       | Khó deploy, không secure      | Environment variables         |
| Không handle exceptions      | Poor UX, security leaks       | `@ControllerAdvice`           |
| Log với `System.out`         | Không structured, không level | SLF4J `@Slf4j`                |
| Không validate input         | Security vulnerabilities      | `@Valid` + Jakarta Validation |

---

## 23. Useful Resources

### 23.1. Official Documentation

- [Spring Boot Reference](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [Spring Data JPA](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/)
- [Spring Security](https://docs.spring.io/spring-security/reference/)

### 23.2. Best Practices Guides

- [Baeldung Spring Tutorials](https://www.baeldung.com/spring-tutorial)
- [Spring Boot Best Practices](https://github.com/spring-guides)

### 23.3. Tools

- **Code Quality:** SonarQube, Checkstyle
- **API Testing:** Postman, Bruno, REST Client (VS Code)
- **Database:** DBeaver, MySQL Workbench
- **Monitoring:** Spring Boot Actuator + Prometheus + Grafana

---

## 24. Tổng kết

### 24.1. Những nguyên tắc VÀNG

1. **Constructor Injection** - Luôn luôn
2. **DTOs cho API** - Không bao giờ expose Entity
3. **Database Migration** - Bắt buộc cho mọi schema change
4. **Lazy Loading** - Default cho relationships
5. **Exception Handling** - `@ControllerAdvice` toàn cục
6. **Logging** - SLF4J với appropriate levels
7. **Validation** - `@Valid` trên DTO
8. **Testing** - Minimum 80% coverage cho business logic
9. **Security** - JWT best practices, không lưu sensitive data
10. **Docker** - Multi-stage build, non-root user

### 24.2. Quy trình áp dụng Convention

1. **Onboarding:** Mọi developer mới phải đọc và hiểu convention này
2. **Code Review:** Checklist phải được follow trong mọi PR
3. **CI/CD:** Automated checks (Checkstyle, SonarQube) trong pipeline
4. **Continuous Update:** Convention được review và update mỗi quarter

---

**Version:** 1.0.0  
**Last Updated:** October 2025  
**Maintained by:** Software Architecture Team

---

_Tài liệu này là living document. Mọi đóng góp và cải tiến đều được hoan nghênh thông qua Pull Request._
