package io.syslogic.demo.colorpicker;

import static org.hamcrest.core.IsNull.notNullValue;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Rect;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject2;
import androidx.test.uiautomator.Until;

import org.junit.Assert;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

import java.util.Random;

/**
 * Application Test Suite
 *
 * @author Martin Zeitler
 */
@RunWith(org.junit.runners.Suite.class)
@SuiteClasses({TestColorPicker.class})
public class TestSuite {

    private static final int LAUNCH_TIMEOUT = 5000;

    String packageName;
    UiDevice mDevice;

    /**
     * uses package manager to find the package name of the device launcher.
     * usually this package is "com.android.launcher" but can be different at times.
     * this is a generic solution which works on all platforms.`
     */
    @Nullable
    private String getLauncherPackageName() {

        /* create a launcher Intent */
        final Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);

        /* use PackageManager to get the launcher package name */
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        PackageManager pm = context.getPackageManager();
        ResolveInfo resolveInfo = pm.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);

        if (resolveInfo != null) {
            return resolveInfo.activityInfo.name;
        } else {
            return null;
        }
    }

    /** launches the blueprint application */
    void startMainActivity() {

        /* initialize UiDevice */
        this.mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        Assert.assertThat(this.mDevice, notNullValue());

        /* Start from the home screen */
        this.mDevice.pressHome();

        /* Obtain the launcher package */
        String launcherPackage = getLauncherPackageName();
        Assert.assertThat(launcherPackage, notNullValue());

        /* Wait for launcher */
        this.mDevice.wait(Until.hasObject(By.pkg(launcherPackage).depth(0)), LAUNCH_TIMEOUT);

        /* Setting the package name and obtaining the launch intent */
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        this.packageName = context.getPackageName().replace(".test", "");
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(this.packageName);

        if(intent != null) {

            intent.setComponent(new ComponentName(this.packageName, this.packageName + ".activity.MainActivity"));
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);

            /* Wait for the app to appear */
            this.mDevice.wait(Until.hasObject(By.pkg(this.packageName.replace(".debug", "")).depth(0)), LAUNCH_TIMEOUT);
        }
    }

    UiObject2 getButtonDialog() {
        return this.mDevice.findObject(By.res(this.packageName, "button_dialog"));
    }

    UiObject2 getButtonPreferences() {
        return this.mDevice.findObject(By.res(this.packageName, "button_preferences"));
    }

    UiObject2 getColorPickerView() {
        return this.mDevice.findObject(By.res(this.packageName, "color_picker_view"));
    }

    @SuppressWarnings("unused")
    UiObject2 getOldColorPanel() {
        return this.mDevice.findObject(By.res(this.packageName, "old_color_panel"));
    }

    UiObject2 getNewColorPanel() {
        return this.mDevice.findObject(By.res(this.packageName, "new_color_panel"));
    }

    /** TODO: This randomly clicks the whole panel... */
    void randomlyClick(@NonNull UiObject2 view) {
        Rect rect = view.getVisibleBounds();
        Random rnd = new Random();
        int[] coordinate = new int[] {
                rect.left + rnd.nextInt(rect.right - rect.left + 1),
                rect.top + rnd.nextInt(rect.bottom -rect.top + 1)
        };
        this.mDevice.click(coordinate[0], coordinate[1]);
    }

    void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
