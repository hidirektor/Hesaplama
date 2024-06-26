package me.t3sl4.hydraulic.Utility.Data.User;

import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import me.t3sl4.hydraulic.Launcher;
import me.t3sl4.hydraulic.Utility.HTTP.HTTPRequest;

import java.io.File;

import static me.t3sl4.hydraulic.Launcher.*;

public class Profile {
    public static void downloadAndSetProfilePhoto(String username, Circle secilenFoto, ImageView profilePhotoImageView) {
        String localFileFinalPath = Launcher.profilePhotoLocalPath + username + ".jpg";

        File localFile = new File(localFileFinalPath);
        if (localFile.exists()) {
            setProfilePhoto(username, secilenFoto, profilePhotoImageView);
        } else {
            String photoUrl = BASE_URL + downloadPhotoURLPrefix;
            String jsonBody = "{\"username\":\"" + username + "\"} ";

            HTTPRequest.sendRequest4File(photoUrl, jsonBody, localFileFinalPath, new HTTPRequest.RequestCallback() {
                @Override
                public void onSuccess(String response) {
                    setProfilePhoto(username, secilenFoto, profilePhotoImageView);
                }

                @Override
                public void onFailure() {
                    System.out.println("Profil fotoğrafı indirilemedi.");
                }
            });
        }
    }

    private static void setProfilePhoto(String username, Circle secilenFoto, ImageView profilePhotoImageView) {
        String photoPath = profilePhotoLocalPath + username + ".jpg";
        File photoFile = new File(photoPath);

        if (photoFile.exists()) {
            Image image = new Image(photoFile.toURI().toString());
            secilenFoto.setFill(new ImagePattern(image));
            secilenFoto.setEffect(new DropShadow(+25d, 0d, +2d, Color.valueOf("#05071F")));
            secilenFoto.setVisible(true);
            profilePhotoImageView.setImage(image);
            profilePhotoImageView.setVisible(false);
        }
    }
}
