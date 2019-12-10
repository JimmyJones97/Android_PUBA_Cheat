package com.tsml.hkl.Utils;

import android.content.Context;
import android.content.res.AssetManager;

import com.tsml.hkl.MainService;
import com.tsml.hkl.enty.AppData;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static com.tsml.hkl.Utils.KeyUtils.FileJK;
import static com.tsml.hkl.enty.AppData.GAME_FILE_PATH;

public class DataUtils {

    static String sos[] = {"libUE4.so", "libtprt.so", "libtersafe.so"};


    public static boolean getGameAssetsFiles(Context context) {

        AssetManager assetManager = context.getAssets();
        File filesDir = context.getFilesDir();
        String gameFilePath = filesDir.getPath();

        File gameFileBackPath = new File(filesDir.getPath() + "/GaneBack/");
        for (String so : sos) {
            InputStream ip = null;
            OutputStream ot = null;

            File dataFile = new File(String.format("%s/Game/%s", gameFilePath, so));
            if (!dataFile.getParentFile().exists()) {
                dataFile.getParentFile().mkdirs();
            }

            try {
                ip = assetManager.open(String.format("Game/%s", so));
                if (!dataFile.exists()) {
                    ot = new FileOutputStream(dataFile);
                    int ten = 0;
                    int length = 1024;
                    byte buffer[] = new byte[length];
                    while ((ten = ip.read(buffer)) != -1) {
                        byte[] bs = FileJK(buffer);
                        ot.write(bs, 0, ten);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (ot != null) {
                    try {
                        ot.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if (ip != null) {
                    try {
                        ip.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                String shell =
                        "chmod  755 " + dataFile.getPath();
                ShellUtils.execCommand(shell, true);

            }
        }

        boolean aBoolean = MainService.example.dataSave.getBoolean(AppData.GAME_BACK);
        if (!aBoolean) {
            if (!gameFileBackPath.exists()) {
                gameFileBackPath.mkdirs();
            }
            String shells[] = {
                    "cp " + GAME_FILE_PATH + "libtersafe.so " + gameFileBackPath.getPath() + "/libtersafe.so",
                    "cp " + GAME_FILE_PATH + "libtprt.so " + gameFileBackPath.getPath() + "/libtprt.so",
                    "cp " + GAME_FILE_PATH + "libUE4.so " + gameFileBackPath.getPath() + "/libUE4.so"
            };
            ShellUtils.CommandResult commandResult = ShellUtils.execCommand(shells, true);
            if (commandResult.result != 0) {
                return false;
            }

            MainService.example.dataSave.saveBoolean(AppData.GAME_BACK, true);
        }


        String shells[] = {
                "mv " + filesDir + "/Game/libtersafe.so " + GAME_FILE_PATH + "libtersafe.so",
                "mv " + filesDir + "/Game/libtprt.so " + GAME_FILE_PATH + "libtprt.so",
                "mv " + filesDir + "/Game/libUE4.so " + GAME_FILE_PATH + "libUE4.so"
        };

        ShellUtils.CommandResult commandResult = ShellUtils.execCommand(shells, true);
        if (commandResult.result == 0) {
            return true;
        }
        return false;
    }


    public static boolean Restore(Context context) {
        File filesDir = context.getFilesDir();
        File gameFileBackPath = new File(filesDir.getPath() + "/GaneBack/");
        boolean aBoolean = MainService.example.dataSave.getBoolean(AppData.GAME_BACK);
        if (aBoolean && gameFileBackPath.exists()) {
            String shells[] = {
                    "cp " + gameFileBackPath.getPath() + "/libtersafe.so " + GAME_FILE_PATH + "libtersafe.so",
                    "cp " + gameFileBackPath + "/libtprt.so " + GAME_FILE_PATH + "libtprt.so",
                    "cp " + gameFileBackPath + "/libUE4.so " + GAME_FILE_PATH + "libUE4.so"
            };
            ShellUtils.CommandResult commandResult = ShellUtils.execCommand(shells, true);
            if (commandResult.result == 0) {
                return true;
            }
        }
        return false;
    }
}
