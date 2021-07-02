module APProject40 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires gson;
    requires java.sql;
    requires opencsv;
    requires org.junit.jupiter;
    requires org.junit.jupiter.engine;
    opens GUI to javafx.fxml, java.sql, javafx.media;
    opens Database to gson, opencsv, java.sql;
    opens Controller to org.junit.jupiter, org.junit.jupiter.engine;
    exports Controller;
    exports Database;
    exports GUI;
}