# 🚀 Quick Start Guide - Book Manager với Spring Security

## ⚡ Bắt Đầu Nhanh (3 Bước)

### Bước 1: Đảm bảo MySQL đang chạy
```bash
# Windows - Kiểm tra MySQL Service
# Hoặc chạy từ terminal:
mysql -u root -p

# Nếu chưa có database:
CREATE DATABASE bookdb;
```

### Bước 2: Chạy ứng dụng
```bash
cd c:\baituan5

# Cách 1: Sử dụng Maven Wrapper
./mvnw spring-boot:run

# Cách 2: Sử dụng IDE (nhấn F5 hoặc click Run)
# Tìm file: BookManagerApplication.java
# Click chuột phải → Run as Spring Boot App
```

### Bước 3: Truy cập ứng dụng
```
🌐 http://localhost:8080/login

📝 Tài khoản test:
   - user / password123    (USER role)
   - admin / admin123      (ADMIN role)
```

---

## 🎯 Kiểm Tra Tính Năng

### Test USER Account (Quyền hạn giới hạn)
1. Đăng nhập: `user` / `password123`
2. Xem danh sách sách ✅
3. Thử click "Thêm Sách Mới" → 403 Forbidden ✅
4. Thử xóa sách → 403 Forbidden ✅

### Test ADMIN Account (Toàn quyền)
1. Đăng nhập: `admin` / `admin123`
2. Xem danh sách sách ✅
3. Click "Thêm Sách Mới" → Có thể thêm ✅
4. Click "Sửa" → Có thể sửa ✅
5. Click "Xóa" → Có thể xóa ✅

### Test Logout
1. Click "Đăng Xuất" → Redirect tới /login ✅
2. Thử truy cập /books mà không login → Redirect tới /login ✅

---

## 🔍 Logs Khi Khởi Động

```
[INFO] Scanning for projects...
[INFO] Building bookmanager 0.0.1-SNAPSHOT
[INFO] 
[INFO] --- spring-boot-maven-plugin:3.1.4:run
[INFO] 
Starting BookManagerApplication using Java 17...
✅ Tài khoản USER đã được tạo: user / password123
✅ Tài khoản ADMIN đã được tạo: admin / admin123
```

---

## 📁 Cấu Trúc Project

```
C:\baituan5
├── pom.xml                    ← Maven configuration
├── mvnw / mvnw.cmd           ← Maven wrapper
├── SPRING_SECURITY_GUIDE.md  ← 📖 Hướng dẫn chi tiết
├── IMPLEMENTATION_SUMMARY.md ← 📋 Tóm tắt thay đổi
├── QUICK_START.md            ← 🚀 File này
├── src/main/java/com/example/bookmanager/
│   ├── BookManagerApplication.java
│   ├── config/
│   │   ├── SecurityConfig.java       ← 🔐 Cấu hình Spring Security
│   │   └── DataInitializer.java      ← 🔨 Khởi tạo users/roles
│   ├── controller/
│   │   ├── BookController.java       ← Thêm @PreAuthorize
│   │   └── LoginController.java      ← 🆕 Xử lý đăng nhập
│   ├── model/
│   │   ├── Book.java
│   │   ├── User.java                 ← 🆕 User entity
│   │   └── Role.java                 ← 🆕 Role entity
│   ├── repository/
│   │   ├── BookRepository.java
│   │   ├── UserRepository.java       ← 🆕 User repo
│   │   └── RoleRepository.java       ← 🆕 Role repo
│   └── service/
│       └── CustomUserDetailsService.java ← 🆕 Load user từ DB
├── src/main/resources/
│   ├── application.properties
│   └── templates/
│       ├── login.html                ← 🆕 Trang đăng nhập
│       ├── list.html                 ← Cập nhật: user info, logout
│       └── form.html                 ← Cập nhật: styling
└── target/                   ← Compiled files
```

---

## 🛠️ Lệnh Hữu Ích

```bash
# Build project
./mvnw clean package -DskipTests

# Run tests
./mvnw test

# Clean build
./mvnw clean install

# Compile only
./mvnw clean compile

# Show dependencies
./mvnw dependency:tree

# Run with specific profile
./mvnw spring-boot:run -Dspring-boot.run.arguments="--server.port=9090"
```

---

## 💾 Database Commands

```sql
-- Kiểm tra users đã tạo
SELECT * FROM users;

-- Kiểm tra roles
SELECT * FROM roles;

-- Kiểm tra user-role mapping
SELECT u.username, r.name 
FROM user_roles ur
JOIN users u ON ur.user_id = u.id
JOIN roles r ON ur.role_id = r.id;

-- Tạo user mới (mật khẩu phải encode BCrypt)
INSERT INTO users (username, password, email, enabled) 
VALUES ('john', '$2a$10/...encoded...', 'john@example.com', true);

-- Gán role cho user
INSERT INTO user_roles (user_id, role_id) VALUES (3, 1); -- USER role
```

---

## 🐛 Troubleshooting

### ❌ Lỗi "Connection refused"
```
Nguyên nhân: MySQL không chạy
Giải pháp: Start MySQL service
```

### ❌ Lỗi "Table 'bookdb.users' doesn't exist"
```
Nguyên nhân: JPA chưa tự động tạo bảng
Giải pháp: application.properties có spring.jpa.hibernate.ddl-auto=update
```

### ❌ Lỗi 403 Forbidden
```
Nguyên nhân: Không có quyền
Giải pháp: Kiểm tra role của user trong database
SELECT * FROM users WHERE username='user';
```

### ❌ Lỗi "Invalid username or password"
```
Nguyên nhân: Sai username/password
Giải pháp: Dùng tài khoản demo: user/password123 hoặc admin/admin123
```

### ❌ Lỗi "CSRF token missing"
```
Nguyên nhân: CSRF protection bị lỗi
Giải pháp: Đã disable trong SecurityConfig
```

---

## 📚 File Hướng Dẫn

| File | Mô Tả |
|------|-------|
| **SPRING_SECURITY_GUIDE.md** | 📖 Hướng dẫn chi tiết về cách hoạt động |
| **IMPLEMENTATION_SUMMARY.md** | 📋 Tóm tắt tất cả các file được tạo/sửa |
| **QUICK_START.md** | 🚀 Bắt đầu nhanh (file này) |

---

## ✨ Tính Năng Chính

- ✅ Xác thực người dùng (Username/Password)
- ✅ Mã hóa mật khẩu (BCryptPasswordEncoder)
- ✅ 2 vai trò (USER, ADMIN)
- ✅ Kiểm soát quyền truy cập (@PreAuthorize)
- ✅ Session Management
- ✅ Logout functionality
- ✅ Thymeleaf Security Integration
- ✅ Tư động khởi tạo users/roles

---

## 🎓 Học Thêm

Đọc chi tiết từ **SPRING_SECURITY_GUIDE.md** để hiểu:
- Cách hoạt động của Spring Security
- @PreAuthorize annotation
- Thymeleaf security dialect (sec:authorize)
- Cách tạo user mới
- Cách debug vấn đề

---

## 🎉 Done!

Bạn đã thành công tích hợp Spring Security vào Book Manager!

**Tiếp theo?**
1. Kiểm tra tính năng với tài khoản demo
2. Đọc hướng dẫn chi tiết (SPRING_SECURITY_GUIDE.md)
3. Tùy chỉnh theo nhu cầu của bạn
4. Triển khai lên production 🚀

---

**Năng lực hỗ trợ**:
- 📝 Hỏi/đáp về Spring Security
- 🔧 Điều chỉnh quyền hạn
- 📊 Quản lý users
- 🎨 Cải thiện giao diện

---

**Happy Coding! 💻**
