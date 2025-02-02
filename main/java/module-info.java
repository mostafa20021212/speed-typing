module com.example.speedtyping {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.speedtyping to javafx.fxml;
    exports com.example.speedtyping;
}