# M.A.M.S. Backend (Military Asset Management System)

Spring Boot REST API backend for the Military Asset Management System. Provides secure authentication (JWT), Role-Based Access Control (RBAC), multi-base inventory tracking across Western, Central, Southern, and Eastern bases, inter-base transfers, purchases, assignments, expenditures, and audit logging.

---

## 🏛️ Tech Stack & Architecture

- **Language / Framework**: Java 21, Spring Boot 3.2.5
- **Security**: Spring Security 6 with stateless JWT Bearer token authentication & BCrypt password hashing
- **Persistence**: Spring Data JPA / Hibernate ORM with MySQL 8.0 Dialect
- **Database**: MySQL 8.0 with relational foreign keys and transaction management

---

## 🎖️ Operational Bases

| Base Code | Base Name | Region |
|:---:|---|---|
| `WB` | **Western** | Western Region |
| `CB` | **Central** | Central Region |
| `SB` | **Southern** | Southern Region |
| `EB` | **Eastern** | Eastern Region |

---

## 🛡️ 20 Standard Military Assets (IDs 1–20)

| Asset ID | Asset Name | Category |
|:---:|---|---|
| 1 | T-90 Bhishma | Armoured Vehicle |
| 2 | T-72 Ajeya | Armoured Vehicle |
| 3 | Arjun Main Battle Tank | Armoured Vehicle |
| 4 | BMP-2 | Combat Vehicle |
| 5 | K9 Vajra | Artillery |
| 6 | M777 Howitzer | Artillery |
| 7 | Dhanush Artillery Gun | Artillery |
| 8 | Pinaka Rocket System | Rocket System |
| 9 | Ashok Leyland 6x6 | Transport Vehicle |
| 10 | Heavy Recovery Vehicle | Support Vehicle |
| 11 | Armoured Ambulance | Medical Vehicle |
| 12 | Infantry Protected Vehicle | Protected Vehicle |
| 13 | ALH Dhruv | Helicopter |
| 14 | Light Combat Helicopter | Helicopter |
| 15 | Light Utility Helicopter | Helicopter |
| 16 | INSAS Rifle | Small Arms |
| 17 | 7.62 mm Assault Rifle | Small Arms |
| 18 | Light Machine Gun | Small Arms |
| 19 | 155 mm Artillery Ammunition | Ammunition |
| 20 | 81 mm Mortar Ammunition | Ammunition |

---

## 🔑 Pre-configured Accounts & Roles

| Role | Email | Password | Access Rights |
|---|---|---|---|
| **ADMIN** | `admin@mams.mil` | `admin123` | Full access: base management, global assets, audit logs |
| **BASE_COMMANDER** | `commander@mams.mil` | `commander123` | Access across all bases: transfers, personnel assignments, expenditures |
| **LOGISTICS_OFFICER** | `logistics@mams.mil` | `logistics123` | Record purchases, manage inventory inflows |

---

## 🚀 Getting Started

### 1. Database Setup (MySQL)
Create and populate the database using the SQL scripts in `database/`:
```bash
mysql -u root -p < database/01_create_database.sql
mysql -u root -p < database/02_create_tables.sql
mysql -u root -p < database/03_insert_sample_data.sql
```

Ensure `src/main/resources/application.properties` matches your local MySQL credentials:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/military_asset_management?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=root
```

### 2. Build & Run
```bash
# Compile and install
mvn clean install

# Run the development server (runs on port 8080)
mvn spring-boot:run
```

### 3. Package Executable JAR
```bash
mvn clean package -DskipTests
java -jar target/military-asset-management-1.0.0.jar
```

---

## 📡 Key REST API Endpoints

| Method | Endpoint | Description | Auth Required |
|---|---|---|---|
| `POST` | `/api/auth/login` | Authenticate user & receive JWT token | No |
| `GET` | `/api/dashboard/summary` | Get aggregated inventory movement metrics | Yes |
| `GET` | `/api/bases` | List all operational bases | Yes |
| `GET` | `/api/assets` | List military assets (filter by base/category) | Yes |
| `POST` | `/api/assets` | Register a new asset | Admin / Commander |
| `GET` | `/api/asset-categories` | List all 11 asset categories | Yes |
| `GET` | `/api/transfers` | List inter-base transfers | Yes |
| `POST` | `/api/transfers` | Execute inter-base transfer | Admin / Commander / Logistics |
| `GET` | `/api/purchases` | List purchases / stock inflow | Yes |
| `POST` | `/api/purchases` | Record stock purchase | Admin / Commander / Logistics |
| `GET` | `/api/assignments` | List personnel asset assignments | Admin / Commander |
| `POST` | `/api/assignments` | Issue asset to personnel | Admin / Commander |
| `GET` | `/api/expenditures` | List expended assets | Admin / Commander |
| `POST` | `/api/expenditures` | Record asset expenditure | Admin / Commander |
| `GET` | `/api/audit-logs` | Retrieve chronological audit trail | Admin |
