package ru.helper.worker.business.create_order.model.enums;

public enum OrderCategory {
    ELECTRICAL_WORKS(1, "Электромонтажные работы"),
    PLUMBING_WORKS(2, "Сантехнические работы"),
    FINISHING_WORKS(3, "Отделочные работы"),
    WINDOWS_DOORS_INSTALLATION(4, "Установка и ремонт окон и дверей"),
    FLOORING(5, "Напольные покрытия"),
    TILING(6, "Плиточные работы"),
    PAINTING(7, "Малярные работы и покраска"),
    DRYWALL_WORKS(8, "Гипсокартонные работы"),
    CARPENTRY_WORKS(9, "Плотницкие работы"),
    DEMOLITION_WORKS(10, "Демонтажные работы"),
    FURNITURE_ASSEMBLY(11, "Установка мебели и сборка"),
    HVAC_WORKS(12, "Кондиционирование и вентиляция"),
    SMART_HOME_INSTALLATION(13, "Умный дом и автоматизация"),
    MAJOR_RENOVATION(14, "Капитальный ремонт помещений"),
    INSULATION_WORKS(15, "Изоляционные работы");

    private final int code;
    private final String description;

    OrderCategory(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static String getDescriptionByCode(int code) {
        for (OrderCategory type : OrderCategory.values()) {
            if (type.getCode() == code) {
                return type.getDescription();
            }
        }
        throw new IllegalArgumentException("Unknown order category code: " + code);
    }
}
