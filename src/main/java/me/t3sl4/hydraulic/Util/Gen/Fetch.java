package me.t3sl4.hydraulic.Util.Gen;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import me.t3sl4.hydraulic.Controllers.LoginController;
import me.t3sl4.hydraulic.Launcher;

import java.io.IOException;
import java.util.List;

public class Fetch {
    public static void loadItems() {
        try {
            VBox pnItems = new VBox();
            List<String> orderNumbers = OrderFetcher.fetchOrderNumbers();

            for (String orderNumber : orderNumbers) {
                Node itemNode = FXMLLoader.load(Launcher.class.getResource("fxml/Item.fxml"));
                HBox itemContainer = (HBox) itemNode;

                // Bileşenlere erişim sağlayalım
                ImageView imageView = (ImageView) itemContainer.getChildren().get(0);
                Label nameLabel = (Label) itemContainer.lookup("#nameLabel");
                Label locationLabel = (Label) itemContainer.lookup("#locationLabel");
                Label orderDateLabel = (Label) itemContainer.lookup("#orderDateLabel");
                Button viewButton = (Button) itemContainer.lookup("#viewButton");
                Label priceLabel = (Label) itemContainer.lookup("#priceLabel");

                // Bileşenleri sipariş numarasına göre güncelleyelim
                //String orderDate = fetchOrderDate(orderNumber); // Sipariş tarihi veritabanından alınacak.
                //String location = fetchLocation(orderNumber); // Hedef yer veritabanından alınacak.
                //double price = fetchPrice(orderNumber); // Fiyat veritabanından alınacak.

                nameLabel.setText("KeepToo"); // Burada gerekli verileri veritabanından alarak güncelleyin.
                //locationLabel.setText(location); // Örneğin, "location" değişkenini veritabanından alınan değerle güncelleyin.
                //orderDateLabel.setText(orderDate); // Örneğin, "orderDate" değişkenini veritabanından alınan değerle güncelleyin.
                //priceLabel.setText("$" + price); // Örneğin, "price" değişkenini veritabanından alınan değerle güncelleyin.

                viewButton.setOnAction(e -> {
                    // Bu butona tıklandığında ne yapılacağını belirleyebilirsiniz.
                    // Örneğin, detayları göstermek için bir pencere açabilirsiniz.
                });

                pnItems.getChildren().add(itemNode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
