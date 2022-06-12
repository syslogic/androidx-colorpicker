package io.syslogic.demo.colorpicker;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiObject2;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Color-Picker Test Case
 *
 * @author Martin Zeitler
 */
@RunWith(AndroidJUnit4.class)
public class TestColorPicker extends TestSuite {

    @Before
    public void startActivityFromHomeScreen() {
        this.startMainActivity();
    }

    @Test
    public void ColorPickerDialogFragment() {

        UiObject2 button = this.mDevice.findObject(By.res(this.packageName, "button_dialog"));
        Assert.assertTrue(button.isClickable());
        button.click();
        sleep(200);

        UiObject2 colorPickerView = this.mDevice.findObject(By.res(this.packageName, "color_picker_view"));
        Assert.assertTrue(colorPickerView.isFocusable());
        this.randomClick(colorPickerView);
        sleep(200);

        UiObject2 newColorPanel = this.mDevice.findObject(By.res(this.packageName, "new_color_panel"));
        Assert.assertTrue(newColorPanel.isFocusable());
        newColorPanel.click();
        sleep(200);
    }

    @Test
    public void ColorPickerPreference() {

        UiObject2 button = this.mDevice.findObject(By.res(this.packageName, "button_preferences"));
        Assert.assertTrue(button.isClickable());
        button.click();
        button.recycle();
        sleep(200);

        UiObject2 colorCode = this.mDevice.findObject(By.textStartsWith("Tap"));
        colorCode.click();
        colorCode.recycle();
        sleep(200);

        UiObject2 colorPickerView = this.mDevice.findObject(By.res(this.packageName, "color_picker_view"));
        Assert.assertTrue(colorPickerView.isFocusable());
        this.randomClick(colorPickerView);
        colorPickerView.recycle();
        sleep(200);

        UiObject2 newColorPanel = this.mDevice.findObject(By.res(this.packageName, "new_color_panel"));
        Assert.assertTrue(newColorPanel.isFocusable());
        newColorPanel.click();
        newColorPanel.recycle();
        sleep(200);
    }
}
