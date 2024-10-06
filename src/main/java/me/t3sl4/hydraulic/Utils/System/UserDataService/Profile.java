package me.t3sl4.hydraulic.Utils.System.UserDataService;

import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import me.t3sl4.hydraulic.Launcher;
import me.t3sl4.hydraulic.Utils.API.HTTPRequest;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static me.t3sl4.hydraulic.Launcher.*;

public class Profile {
    public static void downloadAndSetProfilePhoto(String username, Circle secilenFoto, ImageView profilePhotoImageView) {
        String localFileFinalPath = Launcher.profilePhotoLocalPath + username + ".jpg";

        File localFile = new File(localFileFinalPath);
        if (localFile.exists()) {
            setProfilePhoto(username, secilenFoto, profilePhotoImageView);
        } else {
            String photoUrl = BASE_URL + downloadPhotoURLPrefix;

            HTTPRequest.downloadFile(photoUrl, "POST", localFileFinalPath, username, new HTTPRequest.RequestCallback() {
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

    public static void initializeTokens() {
        File authFile = new File(Launcher.tokenPath);
        if (!authFile.exists()) return;

        try (Scanner scanner = new Scanner(authFile)) {
            Map<String, String> tokenMap = new HashMap<>();
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.contains(": ")) {
                    String[] parts = line.split(": ");
                    tokenMap.put(parts[0].trim(), parts[1].trim());
                }
            }
            Launcher.userName = tokenMap.getOrDefault("userName", "");
            Launcher.userID = tokenMap.getOrDefault("userID", "");
            Launcher.accessToken = tokenMap.getOrDefault("AccessToken", "");
            Launcher.refreshToken = tokenMap.getOrDefault("RefreshToken", "");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
