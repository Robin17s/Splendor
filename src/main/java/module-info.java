/**
 * <h2>Splendor</h2>
 * <h3>G53 Edition</h3>
 * In this module, all code relating to SDP 2022-2023 G53 will be present. Console-related code will be put in {@link cui},
 * Startup-related code in {@link main},
 * Graphical code in {@link gui},
 * and Database- and Storage-related code in {@link persistence}.
 */
module splendor {
    exports cui;
    exports domain;
    exports gui;
    exports main;
    exports persistence;

    requires javafx.base;
    requires javafx.graphics;
    requires javafx.controls;
    requires java.sql;

    opens gui to javafx.graphics;
}