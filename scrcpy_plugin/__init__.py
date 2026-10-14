"""
Scrcpy Device Frame Plugin
===========================

Un plugin visual para scrcpy que añade un marco de dispositivo móvil 
estilo Android Studio / Orca.dev.

Uso:
    from scrcpy_plugin import ScrcpyWindow, DeviceFrame
    
    app = QApplication([])
    window = ScrcpyWindow(serial="192.168.1.100:5555")
    window.show()
    app.exec()
"""

from .scrcpy_device_frame import ScrcpyWindow, DeviceFrame, ScrcpyProcess

__version__ = "1.0.0"
__author__ = "Discord Bot Maker Team"

__all__ = ["ScrcpyWindow", "DeviceFrame", "ScrcpyProcess"]
