import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

public class Helper {
    public static StackPane getTitle() {
        Image battleshipImage = new Image(Helper.class.getResourceAsStream("/images/battleship.png"));
        ImageView battleshipImageView = new ImageView(battleshipImage);
        battleshipImageView.setFitHeight(1250);
        battleshipImageView.setFitWidth(1500);
        battleshipImageView.setPreserveRatio(true);
        battleshipImageView.setTranslateX(180);
        battleshipImageView.setTranslateY(-20);


        StackPane centeredImageView = new StackPane(battleshipImageView);
        centeredImageView.setAlignment(Pos.CENTER); // This will center the image

        battleshipImageView.setFitHeight(1250);
        battleshipImageView.setFitWidth(1500);
        battleshipImageView.setPreserveRatio(true);
        battleshipImageView.setTranslateX(180);
        battleshipImageView.setTranslateY(-20);

        return centeredImageView;
    }
}
