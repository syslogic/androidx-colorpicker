package io.syslogic.colorpicker.compose

/**
 * OnColorChangedListener.
 * @author Martin Zeitler
 */
interface OnColorChangedListener {

    /**
     * OnColorChanged Callback
     * @param color the selected color.
     */
    fun onColorChanged(color: Int)
}
