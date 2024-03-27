package org.example.Models;

import org.example.Helper.ConstantString;

import java.util.Timer;
import java.util.TimerTask;

public class AutoSaveManager {
    private static final long DEFAULT_SAVE_INTERVAL_MS = 5 * 60 * 1000; // 5 phút mặc định
    private long saveIntervalMs;
    private Timer autoSaveTimer;

    // Biến static để lưu instance của lớp
    private static AutoSaveManager instance;

    // Hàm private constructor để ngăn việc khởi tạo từ bên ngoài
    private AutoSaveManager() {
        this.saveIntervalMs = DEFAULT_SAVE_INTERVAL_MS;
        this.autoSaveTimer = new Timer(true); // Sử dụng daemon thread
    }

    // Phương thức public để lấy instance của lớp
    public static AutoSaveManager getInstance() {
        if (instance == null) {
            instance = new AutoSaveManager();
        }
        return instance;
    }

    public void startAutoSave() {
        autoSaveTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                // Gọi phương thức save ở đây
                DictionaryManager.getInstance().saveFavoriteWordsToXML(ConstantString.VIETNAMESE_TO_ENGLISH_FAVORITE_FILE_PATH,
                        ConstantString.ENGLISH_TO_VIETNAMESE_FAVORITE_FILE_PATH);
                DictionaryManager.getInstance().saveDictionaryToXML(ConstantString.VIETNAMESE_TO_ENGLISH_FILE_PATH,
                        ConstantString.ENGLISH_TO_VIETNAMESE_FILE_PATH);
                System.out.println("Auto save completed.");
            }
        }, saveIntervalMs, saveIntervalMs);
    }

    public void setSaveIntervalMinutes(long saveIntervalMinutes) {
        this.saveIntervalMs = saveIntervalMinutes * 60 * 1000;
        restartAutoSave();
    }

    private void restartAutoSave() {
        autoSaveTimer.cancel();
        autoSaveTimer = new Timer(true);
        startAutoSave();
    }
}

