# Expense Tracker - Ứng dụng Quản lý Chi tiêu Cá nhân

![Android](https://img.shields.io/badge/Platform-Android-green.svg)
![Java](https://img.shields.io/badge/Language-Java%2017-orange.svg)
![Firebase](https://img.shields.io/badge/Backend-Firebase-yellow.svg)
![License](https://img.shields.io/badge/License-MIT-blue.svg)

---

## 1. Giới thiệu

**Expense Tracker** là ứng dụng quản lý chi tiêu cá nhân trên nền tảng Android, được phát triển với mục đích giúp người dùng theo dõi và quản lý các khoản thu nhập và chi tiêu hàng ngày một cách đơn giản và hiệu quả.

### Mục đích

- Ghi chép các giao dịch thu nhập và chi tiêu một cách nhanh chóng
- Phân loại chi tiêu theo các danh mục (Ăn uống, Di chuyển, Học tập, v.v.)
- Xem thống kê tổng quan về tình hình tài chính theo tháng
- Lưu trữ dữ liệu an toàn trên cloud, truy cập được mọi lúc mọi nơi

### Đối tượng sử dụng

- Sinh viên và người đi làm muốn kiểm soát chi tiêu hàng ngày
- Người quản lý tài chính gia đình
- Bất kỳ ai muốn có thói quen ghi chép và theo dõi thu chi cá nhân

### Phạm vi bài toán

Đây là đồ án cuối kỳ môn **Điện toán di động**, tập trung vào các chức năng cốt lõi của việc quản lý chi tiêu cá nhân. Ứng dụng được thiết kế với kiến trúc phân tầng rõ ràng, tích hợp Firebase để xử lý authentication và lưu trữ dữ liệu realtime.

---

## 2. Chức năng chính

### 🔐 Quản lý tài khoản
- **Đăng ký**: Tạo tài khoản mới với email và mật khẩu
- **Đăng nhập**: Xác thực người dùng qua Firebase Authentication
- **Đăng xuất**: Thoát khỏi tài khoản an toàn
- **Lưu trạng thái**: Tự động đăng nhập lại khi mở ứng dụng

### 💰 Quản lý giao dịch
- **Thêm giao dịch**: Ghi chép thu nhập hoặc chi tiêu với đầy đủ thông tin
  - Số tiền
  - Loại (Thu nhập / Chi tiêu)
  - Danh mục
  - Ghi chú
  - Ngày giao dịch
- **Xóa giao dịch**: Long press để xóa giao dịch không cần thiết
- **Auto-ID**: Mỗi giao dịch được gán ID duy nhất tự động bởi Firestore

### 📊 Thống kê và báo cáo
- **Thống kê tháng hiện tại**: Hiển thị tổng thu nhập, tổng chi tiêu, và số dư
- **Danh sách giao dịch gần đây**: Xem 10 giao dịch mới nhất trên màn hình chính
- **Danh sách toàn bộ giao dịch**: Xem tất cả các giao dịch đã thực hiện
- **Realtime updates**: Dữ liệu được cập nhật tự động khi có thay đổi

### 🔒 Bảo mật
- **Dữ liệu tách biệt**: Mỗi người dùng chỉ có thể truy cập dữ liệu của chính mình
- **Firestore Security Rules**: Kiểm soát quyền truy cập dựa trên Firebase Authentication UID
- **Mã hóa**: Tất cả dữ liệu được mã hóa khi truyền tải qua HTTPS

---

## 3. Kiến trúc & Thiết kế

### Kiến trúc phân tầng (Layered Architecture)

Ứng dụng được xây dựng theo mô hình **Clean Architecture** với các tầng được phân tách rõ ràng:

```
┌─────────────────────────────────────────┐
│         UI LAYER                        │
│  (Activities, Fragments, Adapters)     │
└────────────────┬────────────────────────┘
                 │
                 ▼
┌─────────────────────────────────────────┐
│      REPOSITORY LAYER                   │
│  (TransactionRepository,                │
│   CategoryRepository)                   │
└────────────────┬────────────────────────┘
                 │
                 ▼
┌─────────────────────────────────────────┐
│      DATA SOURCE LAYER                  │
│  (AuthManager, FirestoreManager)       │
└────────────────┬────────────────────────┘
                 │
                 ▼
┌─────────────────────────────────────────┐
│      FIREBASE SERVICES                  │
│  (Authentication, Cloud Firestore)     │
└─────────────────────────────────────────┘
```

### Lợi ích của kiến trúc này

1. **Separation of Concerns**: Mỗi tầng có trách nhiệm rõ ràng, không lẫn lộn logic
2. **Testability**: Dễ dàng viết unit test và integration test cho từng tầng
3. **Maintainability**: Code dễ đọc, dễ sửa, dễ mở rộng
4. **Scalability**: Có thể thêm features mới mà không ảnh hưởng code hiện có
5. **Flexibility**: Dễ dàng thay đổi implementation (ví dụ: đổi từ Firestore sang Room Database)

### Lý do sử dụng Firestore Auto-ID

**Quyết định thiết kế**: Sử dụng Firestore Auto-ID cho `transactionId` thay vì tự generate ID.

**Lý do**:
- ✅ **Uniqueness guarantee**: Firestore đảm bảo ID là duy nhất trong toàn bộ database
- ✅ **Distributed-friendly**: Auto-ID được thiết kế để hoạt động tốt trong môi trường phân tán
- ✅ **No collision**: Không có xung đột ID khi nhiều client tạo transaction đồng thời
- ✅ **Simplicity**: Không cần implement logic sinh ID phức tạp (UUID, timestamp, etc.)
- ✅ **Performance**: ID được tối ưu cho indexing và query

**Implementation**:
```
Firestore tự động sinh ID khi sử dụng:
- collection.add() hoặc
- collection.document() (không tham số)
```

---

## 4. Công nghệ sử dụng

### 📱 Android & Java
- **Platform**: Android SDK API 24-34 (Android 7.0 - Android 14)
- **Language**: Java 17 (LTS)
- **Build Tool**: Gradle 8.x với Kotlin DSL

### 🔥 Firebase Platform
- **Firebase Authentication**: Quản lý đăng ký, đăng nhập với Email/Password
- **Cloud Firestore**: Cơ sở dữ liệu NoSQL realtime trên cloud
- **Firebase Storage**: Lưu trữ hình ảnh hóa đơn (optional, chưa triển khai)
- **Firebase BOM**: Version 34.7.0

### 🎨 UI/UX
- **Material Design 3**: Components chuẩn Material Design
- **AndroidX Libraries**: AppCompat, ConstraintLayout, Navigation Component
- **RecyclerView**: Hiển thị danh sách giao dịch hiệu quả

### 🏗️ Design Patterns
- **Singleton Pattern**: AuthManager, FirestoreManager, Repository classes
- **Repository Pattern**: Tách biệt business logic khỏi data source
- **Observer Pattern**: Realtime listeners cho Firestore updates
- **Callback Pattern**: Xử lý async operations

---

## 5. Cấu trúc dự án

```
com.dammanhdungvn.quanlychitieucanhan/
│
├── config/                          # Cấu hình tập trung
│   └── AppConfig.java               # Constants, feature flags, validation rules
│
├── data/                            # Data layer
│   ├── model/                       # Data models (POJOs)
│   │   ├── User.java
│   │   ├── Transaction.java
│   │   ├── Category.java
│   │   └── MonthlyStats.java
│   │
│   ├── repository/                  # Repository pattern
│   │   ├── TransactionRepository.java
│   │   └── CategoryRepository.java
│   │
│   └── datasource/                  # Predefined data
│       └── PredefinedCategories.java
│
├── firebase/                        # Firebase integration
│   ├── AuthManager.java             # Authentication logic
│   └── FirestoreManager.java        # Firestore operations
│
├── ui/                              # Presentation layer
│   ├── auth/                        # Authentication screens
│   │   ├── SplashActivity.java
│   │   ├── LoginActivity.java
│   │   └── RegisterActivity.java
│   │
│   ├── home/                        # Main screens
│   │   ├── MainActivity.java
│   │   ├── HomeFragment.java
│   │   └── TransactionAdapter.java
│   │
│   ├── transaction/                 # Transaction management
│   │   ├── TransactionListFragment.java
│   │   └── AddEditTransactionActivity.java
│   │
│   └── profile/                     # User profile
│       └── ProfileFragment.java
│
└── utils/                           # Utility classes
    ├── DateUtils.java               # Date formatting và manipulation
    ├── CurrencyUtils.java           # Currency formatting
    ├── ValidationUtils.java         # Input validation
    └── DialogUtils.java             # Dialog helpers
```

### Giải thích các package chính

- **config**: Chứa `AppConfig` - single source of truth cho tất cả configuration constants
- **data**: Chứa models, repositories, và data sources
- **firebase**: Quản lý tất cả interactions với Firebase services
- **ui**: Chứa Activities, Fragments, và Adapters cho presentation layer
- **utils**: Các utility classes được tái sử dụng xuyên suốt project

---

## 6. Firebase Setup

⚠️ **QUAN TRỌNG**: Firebase project và dữ liệu **KHÔNG** được bao gồm trong repository này. Bạn cần tự tạo Firebase project và cấu hình theo hướng dẫn dưới đây.

### 6.1 Các dịch vụ Firebase cần bật

Truy cập [Firebase Console](https://console.firebase.google.com/) và thực hiện:

1. **Tạo Firebase Project mới**
   - Project name: "Expense Tracker" (hoặc tên bạn muốn)
   - Chọn region phù hợp (ví dụ: asia-southeast1)

2. **Thêm Android App**
   - Package name: `com.dammanhdungvn.quanlychitieucanhan`
   - Download file `google-services.json` và đặt vào thư mục `app/`

3. **Bật Firebase Authentication**
   - Vào **Authentication** → **Sign-in method**
   - Enable **Email/Password** provider
   - Không cần enable Email link

4. **Tạo Cloud Firestore Database**
   - Vào **Firestore Database** → **Create database**
   - Chọn **Production mode**
   - Location: asia-southeast1 (Singapore) hoặc gần bạn nhất

5. **Cấu hình Firestore Security Rules** (XEM SECTION 6.3)

6. **Firebase Storage** (Optional - chưa triển khai)
   - Vào **Storage** → **Get started**
   - Sử dụng cho upload hình ảnh hóa đơn trong tương lai

### 6.2 Cấu trúc Firestore (Schema)

Firestore sử dụng mô hình document-based với cấu trúc như sau:

```
(Root Database)
│
└── users/ (Collection)
    │
    └── {userId}/ (Document - Firebase Auth UID)
        │
        ├── Fields:
        │   ├── userId: string
        │   ├── email: string
        │   ├── displayName: string
        │   └── createdAt: timestamp
        │
        └── transactions/ (Subcollection)
            │
            └── {transactionId}/ (Document - Auto-generated)
                │
                └── Fields:
                    ├── transactionId: string (auto-generated)
                    ├── amount: number
                    ├── type: string ("income" | "expense")
                    ├── category: string
                    ├── note: string
                    ├── date: timestamp
                    ├── imageUrl: string (nullable)
                    └── createdAt: timestamp
```

**Lưu ý quan trọng**:
- `{userId}` và `{transactionId}` là **document IDs**, không phải fields
- Firestore là schema-less: collections chỉ xuất hiện khi có dữ liệu
- `transactionId` được Firestore tự động generate, không cần tạo thủ công
- Mỗi user có subcollection `transactions` riêng, hoàn toàn tách biệt

### 6.3 Firestore Security Rules

**Nguyên tắc bảo mật**:
- Mỗi user chỉ có thể truy cập dữ liệu của chính mình
- Quyền truy cập dựa trên Firebase Authentication UID
- Không có wildcard permissions, tất cả đều phải authenticated

**Cấu hình Security Rules**:

Vào **Firestore Database** → **Rules** tab và paste đoạn code sau:

```javascript
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    
    // User documents
    match /users/{userId} {
      // Chỉ user với uid matching với userId mới có quyền đọc/ghi
      allow read, write: if request.auth != null 
                         && request.auth.uid == userId;
      
      // Transactions subcollection
      match /transactions/{transactionId} {
        // Kế thừa quyền từ parent: chỉ owner mới truy cập được
        allow read, write: if request.auth != null 
                           && request.auth.uid == userId;
      }
    }
  }
}
```

**Giải thích**:
- `request.auth != null`: Đảm bảo user đã đăng nhập
- `request.auth.uid == userId`: Đảm bảo user chỉ truy cập document của mình
- Security rules áp dụng cho cả subcollections

**Publish Rules** và test bằng cách:
- Đăng ký một user mới trong app
- Thử truy cập Firestore console → kiểm tra dữ liệu đã được tạo đúng structure

---

### 📝 Lưu ý về Firebase Data

> **Firebase project và dữ liệu không được export kèm trong repository này.**
> 
> Database có thể được tạo lại bằng cách:
> 1. Tạo Firebase project mới
> 2. Cấu hình Authentication và Firestore theo hướng dẫn trên
> 3. Apply Security Rules
> 4. Chạy app và đăng ký user mới
> 5. Thêm giao dịch → Firestore sẽ tự động tạo collections và documents

---

## 7. Quản lý cấu hình

### 7.1 Centralized App Configuration

Tất cả các configuration quan trọng được quản lý tập trung trong class `AppConfig.java`.

**Nguyên tắc**:
- ❌ **KHÔNG** hard-code string literals trong business logic
- ✅ **SỬ DỤNG** constants từ `AppConfig`
- ✅ Mọi thay đổi config chỉ cần sửa ở một nơi duy nhất

**Ví dụ**:
```java
// BAD ❌
db.collection("users").document(userId)...

// GOOD ✅
db.collection(AppConfig.USERS_COLLECTION).document(userId)...
```

### 7.2 Các config quan trọng có thể thay đổi

Developers có thể tùy chỉnh các giá trị sau trong `AppConfig.java`:

#### 🔧 Firestore Collections
```java
USERS_COLLECTION = "users"
TRANSACTIONS_SUBCOLLECTION = "transactions"
CUSTOM_CATEGORIES_SUBCOLLECTION = "customCategories"
```

#### 🔧 Transaction Types
```java
TRANSACTION_TYPE_INCOME = "income"
TRANSACTION_TYPE_EXPENSE = "expense"
```

#### 🔧 Feature Flags
```java
ENABLE_RECEIPT_UPLOAD = false        // Bật/tắt upload ảnh hóa đơn
ENABLE_OFFLINE_MODE = true           // Bật/tắt offline support
HOME_RECENT_TRANSACTIONS_LIMIT = 10  // Số giao dịch hiển thị trên Home
```

#### 🔧 Validation Rules
```java
MIN_PASSWORD_LENGTH = 6
MAX_NOTE_LENGTH = 500
MIN_TRANSACTION_AMOUNT = 0.01
MAX_TRANSACTION_AMOUNT = 999999999999.99
```

#### 🔧 Currency Settings
```java
CURRENCY_SYMBOL = "VNĐ"
CURRENCY_LOCALE = "vi-VN"
```

#### 🔧 Environment Settings
```java
isDebug()           // Kiểm tra debug mode
isLoggingEnabled()  // Bật/tắt logging
```

**Lợi ích**:
- Thay đổi config dễ dàng mà không cần sửa core logic
- Dễ dàng switch giữa development và production configs
- Feature flags cho phép enable/disable tính năng nhanh chóng
- Type-safe với compile-time checking

---

## 8. Yêu cầu môi trường

### Phần mềm cần thiết

- **Android Studio**: Flamingo (2022.2.1) hoặc mới hơn
  - Download: https://developer.android.com/studio

- **JDK**: Java Development Kit 17 (LTS)
  - Download: https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html
  - Hoặc sử dụng OpenJDK 17

- **Android SDK**: API Level 24-34
  - Tự động cài đặt qua Android Studio SDK Manager

### Thiết bị

- **Thiết bị Android thật**: Android 7.0 (API 24) trở lên
  - Enable USB Debugging trong Developer Options
  
- **Android Emulator**: 
  - Tạo AVD (Android Virtual Device) với API 24+
  - Khuyến nghị: API 31 hoặc 34 để test các tính năng mới nhất

### Kết nối Internet

- ⚠️ **Bắt buộc**: Ứng dụng cần kết nối internet để:
  - Đăng ký và đăng nhập
  - Đồng bộ dữ liệu với Firestore
  - Realtime updates

---

## 9. Hướng dẫn chạy dự án

### Bước 1: Clone repository

```bash
git clone https://github.com/yourusername/expense-tracker-android.git
cd expense-tracker-android
```

### Bước 2: Mở project trong Android Studio

1. Mở Android Studio
2. Chọn **File** → **Open**
3. Navigate đến thư mục project và chọn **Open**
4. Android Studio sẽ tự động detect đây là Android project

### Bước 3: Sync Gradle

1. Android Studio sẽ tự động bắt đầu Gradle sync
2. Nếu không, click vào **File** → **Sync Project with Gradle Files**
3. Đợi quá trình download dependencies hoàn tất (có thể mất vài phút lần đầu)

### Bước 4: Cấu hình Firebase

⚠️ **QUAN TRỌNG**: File `google-services.json` không được commit vào Git vì lý do bảo mật.

**Option A: Sử dụng Firebase project có sẵn**
1. Lấy file `google-services.json` từ Firebase Console
2. Copy file vào thư mục `app/` (cùng cấp với `app/build.gradle.kts`)

**Option B: Tạo Firebase project mới**
1. Làm theo hướng dẫn trong [Section 6: Firebase Setup](#6-firebase-setup)
2. Download `google-services.json` và đặt vào `app/`

**Kiểm tra**:
```bash
# File phải tồn tại tại đường dẫn này
app/google-services.json
```

### Bước 5: Build project

**Option A: Sử dụng Android Studio**
1. Click vào **Build** → **Make Project** (Ctrl+F9)
2. Đợi build hoàn tất, kiểm tra Build Output

**Option B: Sử dụng Command Line**
```bash
# Clean và build project
./gradlew clean
./gradlew assembleDebug

# Hoặc trực tiếp install
./gradlew installDebug
```

### Bước 6: Chạy ứng dụng

**Trên thiết bị thật**:
1. Kết nối thiết bị qua USB
2. Enable USB Debugging
3. Click **Run** button (▶️) trong Android Studio
4. Chọn thiết bị của bạn và click **OK**

**Trên Emulator**:
1. Mở AVD Manager (Tools → AVD Manager)
2. Chạy một AVD đã tạo sẵn
3. Click **Run** button (▶️)
4. Chọn emulator và click **OK**

**Sử dụng Terminal**:
```bash
# Install và run trực tiếp
./gradlew installDebug

# Run app (sau khi install)
adb shell am start -n com.dammanhdungvn.quanlychitieucanhan/.ui.auth.SplashActivity
```

### Bước 7: Test ứng dụng

1. **Đăng ký tài khoản mới**
   - Mở app → Click "Đăng ký"
   - Nhập email, password, display name
   - Click "Đăng ký"

2. **Thêm giao dịch**
   - Click FAB button (+)
   - Điền thông tin giao dịch
   - Click "Lưu"

3. **Xem thống kê**
   - Quay về màn hình Home
   - Kiểm tra statistics card đã cập nhật

4. **Xóa giao dịch**
   - Long press trên transaction item
   - Confirm xóa

---

## 10. Ảnh demo giao diện

### Screenshots

Các ảnh demo được lưu trong thư mục `demo/`:

#### Màn hình Đăng nhập
![Login Screen](demo/login.png)
*Giao diện đăng nhập với email và password*

#### Màn hình Đăng ký
![Register Screen](demo/register.png)
*Form đăng ký tài khoản mới*

#### Màn hình chính (Home)
![Home Screen](demo/home.png)
*Thống kê tháng hiện tại và danh sách giao dịch gần đây*

#### Màn hình thêm giao dịch
![Add Transaction](demo/add_transaction.png)
*Form thêm giao dịch thu nhập hoặc chi tiêu*

#### Màn hình danh sách giao dịch
![Transaction List](demo/transactions.png)
*Danh sách toàn bộ giao dịch đã thực hiện*

#### Màn hình thông tin cá nhân
![Profile Screen](demo/profile.png)
*Thông tin người dùng và tùy chọn đăng xuất*

---

## 11. Troubleshooting

### Lỗi thường gặp và cách khắc phục

#### 1. Build failed: "google-services.json is missing"
**Nguyên nhân**: Chưa có file `google-services.json`  
**Giải pháp**: 
- Download từ Firebase Console
- Đặt vào `app/google-services.json`
- Sync Gradle lại

#### 2. App crash khi đăng nhập: "FirebaseApp not initialized"
**Nguyên nhân**: Firebase chưa được cấu hình đúng  
**Giải pháp**:
- Kiểm tra package name trong `google-services.json` phải là `com.dammanhdungvn.quanlychitieucanhan`
- Clean và rebuild project
- Reinstall app

#### 3. Authentication failed: "The email address is badly formatted"
**Nguyên nhân**: Email không đúng format  
**Giải pháp**: Nhập email hợp lệ (có @ và domain)

#### 4. Firestore permission denied
**Nguyên nhân**: Security Rules chưa được cấu hình  
**Giải pháp**: 
- Vào Firebase Console → Firestore → Rules
- Copy Security Rules từ Section 6.3
- Publish rules

#### 5. Gradle sync failed
**Nguyên nhân**: Network issues hoặc version incompatibility  
**Giải pháp**:
- Kiểm tra kết nối internet
- Invalidate Caches và restart Android Studio
- Update Gradle wrapper: `./gradlew wrapper --gradle-version=8.2`

#### 6. JDK version mismatch
**Nguyên nhân**: Project yêu cầu JDK 17  
**Giải pháp**:
- File → Project Structure → SDK Location
- Đặt JDK location đến JDK 17
- Apply và sync lại

---

## 12. Ghi chú

### ⚠️ Quan trọng

- **Firebase Project**: Không được export kèm vì lý do bảo mật. Bạn cần tự tạo Firebase project theo hướng dẫn.

- **Dữ liệu**: Database sẽ trống ban đầu. Firestore collections được tạo tự động khi có dữ liệu đầu tiên.

- **Mục đích**: Ứng dụng được phát triển cho mục đích học tập và đồ án môn học **Điện toán di động**.

- **Môi trường**: Đã test trên Android 7.0 (API 24) đến Android 14 (API 34).

### 📚 Tài liệu tham khảo

- [Android Developer Guide](https://developer.android.com/guide)
- [Firebase Documentation](https://firebase.google.com/docs)
- [Material Design Guidelines](https://material.io/design)
- [Cloud Firestore Documentation](https://firebase.google.com/docs/firestore)

### 🔄 Phiên bản

- **Version**: 1.0
- **Min SDK**: 24 (Android 7.0)
- **Target SDK**: 34 (Android 14)
- **Java**: 17

### 📝 Changelog

**Version 1.0 (2024-12)**
- ✅ Authentication với Firebase
- ✅ CRUD operations cho transactions
- ✅ Realtime statistics
- ✅ Material Design UI
- ✅ Firestore Security Rules
- ✅ Centralized configuration

### 🚀 Hướng phát triển

Các tính năng có thể được thêm trong tương lai:
- [ ] Edit transaction
- [ ] Custom categories
- [ ] Charts và biểu đồ
- [ ] Budget planning
- [ ] Receipt image upload (Firebase Storage)
- [ ] Export reports (PDF, Excel)
- [ ] Multi-currency support
- [ ] Recurring transactions
- [ ] Dark mode optimization

---

## 13. Tác giả

**Sinh viên thực hiện**: Đàm Mạnh Dũng

**Môn học**: Điện toán di động

**Học kỳ**: I, năm học 2024-2025

**Giảng viên hướng dẫn**: [Tên giảng viên]

---

## 14. License

```
MIT License

Copyright (c) 2024 Đàm Mạnh Dũng

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

---

## 15. Liên hệ

Nếu có bất kỳ câu hỏi hoặc góp ý nào, vui lòng liên hệ:

- **Email**: [your.email@example.com]
- **GitHub**: [https://github.com/yourusername](https://github.com/yourusername)

---

<div align="center">
  <p><strong>⭐ Nếu project này hữu ích, đừng quên cho một star nhé! ⭐</strong></p>
  <p>Made with ❤️ for learning Android Development</p>
</div>

