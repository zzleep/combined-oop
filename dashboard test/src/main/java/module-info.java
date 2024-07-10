module com.example.dashboard_test {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;
    requires java.desktop;
    requires TimingFramework;
    requires miglayout;
    requires java.logging;
    requires java.sql;

    opens com.example.dashboard_test to javafx.fxml;
    exports com.example.dashboard_test;
}