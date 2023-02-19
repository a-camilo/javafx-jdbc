module com.camilo.antonio.javafxjdbc {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    
    exports com.camilo.antonio.javafxjdbc;
    opens com.camilo.antonio.javafxjdbc to javafx.fxml;
}