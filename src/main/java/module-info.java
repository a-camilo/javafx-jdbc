module com.camilo.antonio.javafxjdbc {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens com.camilo.antonio.javafxjdbc to javafx.fxml;
    exports com.camilo.antonio.javafxjdbc;
}