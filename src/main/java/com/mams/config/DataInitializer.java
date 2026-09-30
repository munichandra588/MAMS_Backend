package com.mams.config;

import com.mams.entity.*;
import com.mams.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final BaseRepository baseRepository;
    private final UserRepository userRepository;
    private final AssetCategoryRepository categoryRepository;
    private final AssetRepository assetRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(RoleRepository roleRepository, BaseRepository baseRepository,
                           UserRepository userRepository, AssetCategoryRepository categoryRepository,
                           AssetRepository assetRepository, PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.baseRepository = baseRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.assetRepository = assetRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        // Only insert if no roles exist (first run)
        if (roleRepository.count() > 0) {
            return;
        }

        System.out.println("=== Inserting sample data ===");

        // Roles
        Role admin = roleRepository.save(createRole("ADMIN"));
        Role commander = roleRepository.save(createRole("BASE_COMMANDER"));
        Role logistics = roleRepository.save(createRole("LOGISTICS_OFFICER"));

        // Bases
        Base western = baseRepository.save(createBase("WB", "Western", "Western Region"));
        Base central = baseRepository.save(createBase("CB", "Central", "Central Region"));
        Base southern = baseRepository.save(createBase("SB", "Southern", "Southern Region"));
        Base eastern = baseRepository.save(createBase("EB", "Eastern", "Eastern Region"));

        // Users with BCrypt passwords
        userRepository.save(createUser("Admin User", "admin@mams.mil", "admin123", admin, null));
        userRepository.save(createUser("Base Commander", "commander@mams.mil", "commander123", commander, western));
        userRepository.save(createUser("Logistics Officer", "logistics@mams.mil", "logistics123", logistics, null));

        // Asset Categories
        AssetCategory armouredVehicle = categoryRepository.save(createCategory("Armoured Vehicle"));
        AssetCategory combatVehicle = categoryRepository.save(createCategory("Combat Vehicle"));
        AssetCategory artillery = categoryRepository.save(createCategory("Artillery"));
        AssetCategory rocketSystem = categoryRepository.save(createCategory("Rocket System"));
        AssetCategory transportVehicle = categoryRepository.save(createCategory("Transport Vehicle"));
        AssetCategory supportVehicle = categoryRepository.save(createCategory("Support Vehicle"));
        AssetCategory medicalVehicle = categoryRepository.save(createCategory("Medical Vehicle"));
        AssetCategory protectedVehicle = categoryRepository.save(createCategory("Protected Vehicle"));
        AssetCategory helicopter = categoryRepository.save(createCategory("Helicopter"));
        AssetCategory smallArms = categoryRepository.save(createCategory("Small Arms"));
        AssetCategory ammunition = categoryRepository.save(createCategory("Ammunition"));

        // 20 Standard Assets
        assetRepository.save(createAsset("AV-01", "T-90 Bhishma", armouredVehicle, western, 15));
        assetRepository.save(createAsset("AV-02", "T-72 Ajeya", armouredVehicle, western, 20));
        assetRepository.save(createAsset("AV-03", "Arjun Main Battle Tank", armouredVehicle, central, 12));
        assetRepository.save(createAsset("CV-01", "BMP-2", combatVehicle, central, 25));
        assetRepository.save(createAsset("ART-01", "K9 Vajra", artillery, western, 10));
        assetRepository.save(createAsset("ART-02", "M777 Howitzer", artillery, southern, 12));
        assetRepository.save(createAsset("ART-03", "Dhanush Artillery Gun", artillery, eastern, 14));
        assetRepository.save(createAsset("RS-01", "Pinaka Rocket System", rocketSystem, central, 8));
        assetRepository.save(createAsset("TV-01", "Ashok Leyland 6x6", transportVehicle, western, 30));
        assetRepository.save(createAsset("SV-01", "Heavy Recovery Vehicle", supportVehicle, central, 5));
        assetRepository.save(createAsset("MV-01", "Armoured Ambulance", medicalVehicle, southern, 8));
        assetRepository.save(createAsset("PV-01", "Infantry Protected Vehicle", protectedVehicle, eastern, 18));
        assetRepository.save(createAsset("HEL-01", "ALH Dhruv", helicopter, southern, 6));
        assetRepository.save(createAsset("HEL-02", "Light Combat Helicopter", helicopter, southern, 4));
        assetRepository.save(createAsset("HEL-03", "Light Utility Helicopter", helicopter, eastern, 6));
        assetRepository.save(createAsset("SA-01", "INSAS Rifle", smallArms, western, 200));
        assetRepository.save(createAsset("SA-02", "7.62 mm Assault Rifle", smallArms, central, 150));
        assetRepository.save(createAsset("SA-03", "Light Machine Gun", smallArms, southern, 80));
        assetRepository.save(createAsset("AMM-01", "155 mm Artillery Ammunition", ammunition, western, 10000));
        assetRepository.save(createAsset("AMM-02", "81 mm Mortar Ammunition", ammunition, eastern, 5000));

        System.out.println("=== Sample data inserted successfully ===");
        System.out.println("Login credentials:");
        System.out.println("  ADMIN:             admin@mams.mil / admin123");
        System.out.println("  BASE_COMMANDER:    commander@mams.mil / commander123");
        System.out.println("  LOGISTICS_OFFICER: logistics@mams.mil / logistics123");
    }

    private Role createRole(String name) {
        Role role = new Role();
        role.setName(name);
        return role;
    }

    private Base createBase(String code, String name, String location) {
        Base base = new Base();
        base.setBaseCode(code);
        base.setBaseName(name);
        base.setLocation(location);
        base.setStatus("ACTIVE");
        return base;
    }

    private User createUser(String fullName, String email, String rawPassword, Role role, Base base) {
        User user = new User();
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setRole(role);
        user.setBase(base);
        user.setActive(true);
        return user;
    }

    private AssetCategory createCategory(String name) {
        AssetCategory cat = new AssetCategory();
        cat.setName(name);
        return cat;
    }

    private Asset createAsset(String code, String name, AssetCategory category, Base base, int quantity) {
        Asset asset = new Asset();
        asset.setAssetCode(code);
        asset.setAssetName(name);
        asset.setCategory(category);
        asset.setBase(base);
        asset.setQuantity(quantity);
        asset.setStatus("AVAILABLE");
        return asset;
    }
}
