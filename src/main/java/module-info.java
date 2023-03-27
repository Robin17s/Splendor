module splendor {
    exports cui;
    exports domain;
    exports gui;
    exports main;

    requires javafx.base;
    requires javafx.graphics;
    requires javafx.controls;
    requires java.sql;

    opens gui to javafx.graphics;
}