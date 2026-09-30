# Military Asset Management System (M.A.M.S.) — Database Setup

This folder contains the complete SQL schemas, table definitions, and initial seed data for the **M.A.M.S.** MySQL database.

---

## 📁 Files Included

| File | Description |
|---|---|
| [`01_create_database.sql`](file:///backend/database/01_create_database.sql) | Creates the `military_asset_management` database with proper character set. |
| [`02_create_tables.sql`](file:///backend/database/02_create_tables.sql) | Creates all relational tables (`roles`, `bases`, `users`, `asset_categories`, `assets`, `purchases`, `transfers`, `assignments`, `expenditures`, `audit_logs`). |
| [`03_insert_sample_data.sql`](file:///backend/database/03_insert_sample_data.sql) | Seeds initial data: roles, 4 operational bases, test user accounts, 11 categories, and 20 military assets. |
| [`init.sql`](file:///backend/database/init.sql) | All-in-one script combining database creation, table creation, and sample data population. |

---

## 🚀 Quick Setup Instructions

### Option 1: Execute All-in-One (`init.sql`)
Run the all-in-one script directly from MySQL CLI:
```bash
mysql -u root -p < init.sql
```

### Option 2: Step-by-Step Execution
Run each script in sequential order:
```bash
mysql -u root -p < 01_create_database.sql
mysql -u root -p < 02_create_tables.sql
mysql -u root -p < 03_insert_sample_data.sql
```

### Option 3: MySQL Workbench / DBeaver / GUI Client
1. Open and execute `01_create_database.sql` to initialize the database.
2. Open and execute `02_create_tables.sql` to generate schema tables.
3. Open and execute `03_insert_sample_data.sql` to populate initial bases, assets, and users.

---

## 🏛️ Operational Bases Seeded

| Base ID | Base Code | Base Name | Region | Status |
|:---:|:---:|---|---|:---:|
| 1 | `WB` | Western | Western Region | `ACTIVE` |
| 2 | `CB` | Central | Central Region | `ACTIVE` |
| 3 | `SB` | Southern | Southern Region | `ACTIVE` |
| 4 | `EB` | Eastern | Eastern Region | `ACTIVE` |

---

## 🛡️ Pre-seeded Assets (IDs 1–20)

| Asset ID | Asset Code | Asset Name | Category | Base |
|:---:|:---:|---|---|:---:|
| 1 | `AV-01` | T-90 Bhishma | Armoured Vehicle | Western |
| 2 | `AV-02` | T-72 Ajeya | Armoured Vehicle | Western |
| 3 | `AV-03` | Arjun Main Battle Tank | Armoured Vehicle | Central |
| 4 | `CV-01` | BMP-2 | Combat Vehicle | Central |
| 5 | `ART-01` | K9 Vajra | Artillery | Western |
| 6 | `ART-02` | M777 Howitzer | Artillery | Southern |
| 7 | `ART-03` | Dhanush Artillery Gun | Artillery | Eastern |
| 8 | `RS-01` | Pinaka Rocket System | Rocket System | Central |
| 9 | `TV-01` | Ashok Leyland 6x6 | Transport Vehicle | Western |
| 10 | `SV-01` | Heavy Recovery Vehicle | Support Vehicle | Central |
| 11 | `MV-01` | Armoured Ambulance | Medical Vehicle | Southern |
| 12 | `PV-01` | Infantry Protected Vehicle | Protected Vehicle | Eastern |
| 13 | `HEL-01` | ALH Dhruv | Helicopter | Southern |
| 14 | `HEL-02` | Light Combat Helicopter | Helicopter | Southern |
| 15 | `HEL-03` | Light Utility Helicopter | Helicopter | Eastern |
| 16 | `SA-01` | INSAS Rifle | Small Arms | Western |
| 17 | `SA-02` | 7.62 mm Assault Rifle | Small Arms | Central |
| 18 | `SA-03` | Light Machine Gun | Small Arms | Southern |
| 19 | `AMM-01` | 155 mm Artillery Ammunition | Ammunition | Western |
| 20 | `AMM-02` | 81 mm Mortar Ammunition | Ammunition | Eastern |

---

## 🔑 Pre-seeded Login Credentials

| Role | Email | Password | Access Scope |
|---|---|---|---|
| **ADMIN** | `admin@mams.mil` | `admin123` | Global Administration & Configuration |
| **BASE_COMMANDER** | `commander@mams.mil` | `commander123` | Multi-base Command & Operations |
| **LOGISTICS_OFFICER** | `logistics@mams.mil` | `logistics123` | Procurement & Logistics Management |
