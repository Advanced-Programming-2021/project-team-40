module APProject40 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires gson;
    requires java.sql;
    requires opencsv;
    opens GUI to javafx.fxml,java.sql,javafx.media;
    opens Database to gson, opencsv, java.sql;
    exports Database;
    exports GUI;
}