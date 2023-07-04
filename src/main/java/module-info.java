module com.example.user2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires redis.clients.jedis;


    opens com.example.HuynhHuuPhuoc to javafx.fxml;
    exports com.example.HuynhHuuPhuoc;
    opens com.example.HuynhHuuPhuoc.Model to javafx.base;
}