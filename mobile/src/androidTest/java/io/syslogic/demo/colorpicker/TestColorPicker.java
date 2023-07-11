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
        this.startActivity();
    }

    @Test
    public void ColorPickerDialogFragment() {

        UiObject2 button = this.getButtonDialog();
        Assert.assertTrue(button.isClickable());
        button.click();
        sleep(200);

        /* Randomly tapping the ColorPickerView for 5 seconds. */
        for (int i=0; i < 100; i++) {
            UiObject2 colorPickerView = this.getColorPickerView();
            Assert.assertTrue(colorPickerView.isFocusable());
            this.randomlyClick(colorPickerView);
            colorPickerView.recycle();
            sleep(50);
        }

        UiObject2 newColorPanel = this.getNewColorPanel();
        Assert.assertTrue(newColorPanel.isFocusable());
        newColorPanel.click();
        sleep(200);
    }

    @Test
    public void ColorPickerPreference() {

        UiObject2 button = this.getButtonPreferences();
        Assert.assertTrue(button.isClickable());
        button.click();
        button.recycle();
        sleep(200);

        UiObject2 colorCode = this.mDevice.findObject(By.textStartsWith("Tap"));
        colorCode.click();
        colorCode.recycle();
        sleep(200);

        /* Randomly tapping the ColorPickerView for 5s. */
        for (int i=0; i < 100; i++) {
            UiObject2 colorPickerView = this.getColorPickerView();
            Assert.assertTrue(colorPickerView.isFocusable());
            this.randomlyClick(colorPickerView);
            colorPickerView.recycle();
            sleep(50);
        }

        UiObject2 newColorPanel = this.getNewColorPanel();
        Assert.assertTrue(newColorPanel.isFocusable());
        newColorPanel.click();
        newColorPanel.recycle();
        sleep(200);
    }
}
