#!/usr/bin/env python3
"""
Scrcpy Device Frame Plugin
===========================

Un wrapper visual para scrcpy que añade un marco de dispositivo móvil 
estilo Android Studio / Orca.dev para que la pantalla se vea más profesional.

Uso:
    python scrcpy_device_frame.py [opciones de scrcpy]
    
Ejemplo:
    python scrcpy_device_frame.py --serial 192.168.1.100:5555 --bit-rate 8M

Requisitos:
    - scrcpy instalado en el sistema
    - PySide6 (pip install PySide6)
    - Python 3.7+
"""

import sys
import subprocess
import os
from PySide6.QtWidgets import (
    QApplication, QMainWindow, QWidget, QVBoxLayout, QHBoxLayout,
    QFrame, QLabel, QPushButton, QComboBox, QStackedWidget
)
from PySide6.QtCore import Qt, QProcess, QTimer, Signal, QObject
from PySide6.QtGui import QPixmap, QIcon, QColor, QPainter, QPen, QLinearGradient


class DeviceFrame(QFrame):
    """Marco de dispositivo móvil con estilo Android Studio/Orca.dev"""
    
    def __init__(self, device_type="phone", color="black", parent=None):
        super().__init__(parent)
        self.device_type = device_type
        self.color = color
        self.setFrameShape(QFrame.NoFrame)
        self.setAttribute(Qt.WA_TranslucentBackground)
        
    def paintEvent(self, event):
        painter = QPainter(self)
        painter.setRenderHint(QPainter.Antialiasing)
        
        # Configurar colores basados en el tema
        if self.color == "black":
            frame_color = QColor(40, 40, 40)
            shadow_color = QColor(0, 0, 0, 150)
            highlight_color = QColor(100, 100, 100, 100)
        else:
            frame_color = QColor(220, 220, 220)
            shadow_color = QColor(0, 0, 0, 50)
            highlight_color = QColor(255, 255, 255, 100)
        
        # Dibujar el marco del dispositivo
        painter.setPen(QPen(frame_color, 8))
        painter.setBrush(frame_color)
        
        width = self.width()
        height = self.height()
        
        # Radio de esquina (más redondeado para estilo moderno)
        corner_radius = 25
        
        # Dibujar el cuerpo del dispositivo
        painter.drawRoundedRect(
            0, 0, width, height, corner_radius, corner_radius
        )
        
        # Dibujar el borde interior (pantalla)
        screen_margin = 8
        screen_rect = self.rect().adjusted(
            screen_margin, screen_margin, 
            -screen_margin, -screen_margin
        )
        
        # Degradado para el área de la pantalla
        gradient = QLinearGradient(
            screen_rect.topLeft(), screen_rect.bottomRight()
        )
        gradient.setColorAt(0, QColor(20, 20, 20))
        gradient.setColorAt(1, QColor(40, 40, 40))
        
        painter.setBrush(gradient)
        painter.setPen(QPen(QColor(60, 60, 60), 2))
        painter.drawRoundedRect(
            screen_rect, corner_radius - 5, corner_radius - 5
        )
        
        # Dibujar el "notch" o cámara frontal (para estilo iPhone/Android moderno)
        if self.device_type == "phone":
            notch_width = 120
            notch_height = 25
            notch_x = (width - notch_width) // 2
            notch_y = screen_margin + 5
            
            painter.setBrush(QColor(40, 40, 40))
            painter.setPen(QPen(QColor(60, 60, 60), 1))
            painter.drawRoundedRect(
                notch_x, notch_y, notch_width, notch_height, 10, 10
            )
            
            # Cámara frontal
            camera_x = notch_x + notch_width // 2 - 15
            camera_y = notch_y + notch_height // 2 - 10
            painter.setBrush(QColor(30, 30, 30))
            painter.drawEllipse(camera_x, camera_y, 20, 20)
            
            # Altavoz
            speaker_width = 60
            speaker_x = notch_x + (notch_width - speaker_width) // 2
            speaker_y = notch_y + notch_height + 5
            painter.setBrush(QColor(50, 50, 50))
            painter.drawRoundedRect(
                speaker_x, speaker_y, speaker_width, 3, 1, 1
            )
        
        # Dibujar botones de navegación (estilo Android)
        nav_height = 60
        nav_y = height - screen_margin - nav_height - 5
        nav_width = 120
        nav_x = (width - nav_width) // 2
        
        painter.setBrush(QColor(30, 30, 30, 200))
        painter.setPen(QPen(QColor(60, 60, 60), 1))
        painter.drawRoundedRect(
            nav_x, nav_y, nav_width, nav_height, 15, 15
        )
        
        # Botón central (Home)
        home_size = 40
        home_x = nav_x + (nav_width - home_size) // 2
        home_y = nav_y + (nav_height - home_size) // 2
        painter.setBrush(QColor(200, 200, 200))
        painter.drawEllipse(home_x, home_y, home_size, home_size)
        
        # Sombra exterior para efecto 3D
        painter.setBrush(shadow_color)
        painter.setPen(Qt.NoPen)
        painter.drawRoundedRect(
            5, 5, width, height, corner_radius + 2, corner_radius + 2
        )


class ScrcpyProcess(QObject):
    """Maneja el proceso de scrcpy"""
    
    finished = Signal()
    error_occurred = Signal(str)
    
    def __init__(self, parent=None):
        super().__init__(parent)
        self.process = QProcess()
        
    def start_scrcpy(self, args):
        """Iniciar scrcpy con los argumentos dados"""
        cmd = ["scrcpy"] + args
        self.process.start(" ".join(cmd))
        
        if not self.process.waitForStarted():
            self.error_occurred.emit(f"No se pudo iniciar scrcpy: {' '.join(cmd)}")
            return False
        
        return True
    
    def stop(self):
        """Detener el proceso de scrcpy"""
        if self.process.state() == QProcess.Running:
            self.process.terminate()
            if not self.process.waitForFinished(1000):
                self.process.kill()
        self.finished.emit()


class ScrcpyWindow(QMainWindow):
    """Ventana principal con el marco de dispositivo"""
    
    def __init__(self, serial=None, device_type="phone", color="black"):
        super().__init__()
        self.serial = serial
        self.device_type = device_type
        self.color = color
        self.scrcpy_process = ScrcpyProcess(self)
        
        self.init_ui()
        self.setup_connections()
        
    def init_ui(self):
        """Inicializar la interfaz de usuario"""
        self.setWindowTitle("Scrcpy Device Frame")
        self.setWindowIcon(QIcon(self.create_icon()))
        
        # Configuración de la ventana
        self.setMinimumSize(400, 800)
        self.resize(450, 900)
        
        # Widget central
        central_widget = QWidget(self)
        self.setCentralWidget(central_widget)
        
        # Layout principal
        main_layout = QVBoxLayout(central_widget)
        main_layout.setContentsMargins(0, 0, 0, 0)
        main_layout.setSpacing(0)
        
        # Barra de título personalizada
        title_bar = QFrame()
        title_bar.setStyleSheet("""
            background-color: #2b2b2b;
            border-bottom: 1px solid #404040;
            padding: 8px;
        """)
        title_layout = QHBoxLayout(title_bar)
        title_layout.setContentsMargins(10, 5, 10, 5)
        
        self.title_label = QLabel("Device Frame - Scrcpy")
        self.title_label.setStyleSheet("""
            color: #ffffff;
            font-size: 14px;
            font-weight: bold;
        """)
        title_layout.addWidget(self.title_label)
        
        title_layout.addStretch()
        
        # Botones de control
        self.minimize_btn = QPushButton("_")
        self.minimize_btn.setStyleSheet("""
            QPushButton {
                background-color: #404040;
                color: #ffffff;
                border: none;
                border-radius: 3px;
                font-size: 12px;
                min-width: 24px;
                max-width: 24px;
            }
            QPushButton:hover {
                background-color: #505050;
            }
        """)
        self.minimize_btn.setFixedSize(24, 24)
        
        self.close_btn = QPushButton("X")
        self.close_btn.setStyleSheet("""
            QPushButton {
                background-color: #ff4444;
                color: #ffffff;
                border: none;
                border-radius: 3px;
                font-size: 12px;
                min-width: 24px;
                max-width: 24px;
            }
            QPushButton:hover {
                background-color: #ff6666;
            }
        """)
        self.close_btn.setFixedSize(24, 24)
        
        title_layout.addWidget(self.minimize_btn)
        title_layout.addWidget(self.close_btn)
        
        main_layout.addWidget(title_bar)
        
        # Contenedor del dispositivo
        self.device_container = QFrame()
        self.device_container.setStyleSheet("""
            background-color: #1a1a1a;
            margin: 20px;
        """)
        
        device_layout = QVBoxLayout(self.device_container)
        device_layout.setContentsMargins(0, 0, 0, 0)
        
        # El marco del dispositivo
        self.device_frame = DeviceFrame(self.device_type, self.color)
        self.device_frame.setSizePolicy(
            QWidget.Expanding, QWidget.Expanding
        )
        
        device_layout.addWidget(self.device_frame)
        
        main_layout.addWidget(self.device_container, stretch=1)
        
        # Barra de estado
        status_bar = QFrame()
        status_bar.setStyleSheet("""
            background-color: #2b2b2b;
            border-top: 1px solid #404040;
            padding: 8px;
        """)
        status_layout = QHBoxLayout(status_bar)
        status_layout.setContentsMargins(10, 5, 10, 5)
        
        self.status_label = QLabel("Listo")
        self.status_label.setStyleSheet("""
            color: #888888;
            font-size: 12px;
        """)
        status_layout.addWidget(self.status_label)
        
        status_layout.addStretch()
        
        # Selector de dispositivo
        self.device_selector = QComboBox()
        self.device_selector.addItems(["Phone", "Tablet"])
        self.device_selector.setStyleSheet("""
            QComboBox {
                background-color: #333333;
                color: #ffffff;
                border: 1px solid #404040;
                border-radius: 4px;
                padding: 4px;
            }
        """)
        self.device_selector.setCurrentIndex(0)
        status_layout.addWidget(self.device_selector)
        
        # Botón de conexión
        self.connect_btn = QPushButton("Conectar")
        self.connect_btn.setStyleSheet("""
            QPushButton {
                background-color: #5865F2;
                color: #ffffff;
                border: none;
                border-radius: 4px;
                padding: 6px 12px;
            }
            QPushButton:hover {
                background-color: #6d79f7;
            }
            QPushButton:disabled {
                background-color: #404040;
            }
        """)
        status_layout.addWidget(self.connect_btn)
        
        main_layout.addWidget(status_bar)
        
        # Configurar el contenedor para que sea redimensionable
        self.device_container.setSizePolicy(
            QWidget.Expanding, QWidget.Expanding
        )
        
    def setup_connections(self):
        """Configurar las conexiones de señales"""
        self.close_btn.clicked.connect(self.close)
        self.minimize_btn.clicked.connect(self.showMinimized)
        self.connect_btn.clicked.connect(self.toggle_connection)
        self.device_selector.currentIndexChanged.connect(self.update_device_type)
        self.scrcpy_process.finished.connect(self.on_process_finished)
        self.scrcpy_process.error_occurred.connect(self.on_process_error)
        
    def update_device_type(self, index):
        """Actualizar el tipo de dispositivo"""
        self.device_type = self.device_selector.currentText().lower()
        self.device_frame.device_type = self.device_type
        self.device_frame.update()
        
    def toggle_connection(self):
        """Alternar la conexión de scrcpy"""
        if self.scrcpy_process.process.state() == QProcess.Running:
            self.stop_scrcpy()
        else:
            self.start_scrcpy()
            
    def start_scrcpy(self):
        """Iniciar scrcpy"""
        args = []
        
        if self.serial:
            args.extend(["--serial", self.serial])
        
        # Configuración para que scrcpy se ejecute sin borde de ventana
        args.extend([
            "--window-borderless",
            "--window-x", "0",
            "--window-y", "0",
            "--window-width", str(self.device_frame.width()),
            "--window-height", str(self.device_frame.height()),
            "--no-audio",
        ])
        
        self.status_label.setText("Conectando...")
        self.connect_btn.setEnabled(False)
        
        # Iniciar scrcpy como proceso separado
        # Para este plugin, vamos a usar un enfoque diferente:
        # Vamos a ejecutar scrcpy y luego incrustarlo
        self.scrcpy_process.start_scrcpy(args)
        
        # Por ahora, solo mostramos un mensaje
        QTimer.singleShot(1000, lambda: self.status_label.setText(
            "Scrcpy iniciado - Ajusta el tamaño de la ventana manualmente"
        ))
        self.connect_btn.setText("Desconectar")
        self.connect_btn.setEnabled(True)
        
    def stop_scrcpy(self):
        """Detener scrcpy"""
        self.scrcpy_process.stop()
        self.status_label.setText("Desconectado")
        self.connect_btn.setText("Conectar")
        
    def on_process_finished(self):
        """Manejar cuando el proceso termina"""
        self.status_label.setText("Proceso finalizado")
        self.connect_btn.setText("Conectar")
        self.connect_btn.setEnabled(True)
        
    def on_process_error(self, error):
        """Manejar errores del proceso"""
        self.status_label.setText(f"Error: {error}")
        self.connect_btn.setEnabled(True)
        
    def create_icon(self):
        """Crear un icono simple para la ventana"""
        pixmap = QPixmap(32, 32)
        pixmap.fill(Qt.transparent)
        
        painter = QPainter(pixmap)
        painter.setRenderHint(QPainter.Antialiasing)
        
        # Dibujar un simple icono de teléfono
        painter.setBrush(QColor(64, 156, 255))
        painter.setPen(QPen(QColor(64, 156, 255), 1))
        painter.drawRoundedRect(6, 6, 20, 28, 3, 3)
        
        # Pantalla
        painter.setBrush(QColor(40, 40, 40))
        painter.drawRoundedRect(8, 8, 16, 24, 1, 1)
        
        painter.end()
        return pixmap
        
    def resizeEvent(self, event):
        """Manejar el evento de redimensionamiento"""
        super().resizeEvent(event)
        # Actualizar el tamaño del marco
        self.device_frame.update()


def check_scrcpy_installed():
    """Verificar si scrcpy está instalado"""
    try:
        result = subprocess.run(
            ["which", "scrcpy"],
            capture_output=True,
            text=True
        )
        return result.returncode == 0
    except:
        return False


def main():
    """Punto de entrada principal"""
    app = QApplication(sys.argv)
    app.setApplicationName("Scrcpy Device Frame")
    app.setStyle("Fusion")
    
    # Configurar estilo oscuro
    dark_style = """
        QApplication {
            palette: dark;
        }
        QWidget {
            background-color: #1e1e1e;
            color: #ffffff;
        }
        QFrame {
            background-color: #252525;
        }
        QLabel {
            color: #cccccc;
        }
        QPushButton {
            background-color: #333333;
            color: #ffffff;
            border: 1px solid #404040;
        }
        QComboBox {
            background-color: #333333;
            color: #ffffff;
            border: 1px solid #404040;
        }
    """
    app.setStyleSheet(dark_style)
    
    # Verificar si scrcpy está instalado
    if not check_scrcpy_installed():
        print("Advertencia: scrcpy no está instalado en el sistema.")
        print("Instala scrcpy primero: https://github.com/Genymobile/scrcpy")
        print("\nEl plugin aún se ejecutará, pero no podrá iniciar scrcpy.")
    
    # Parsear argumentos
    serial = None
    device_type = "phone"
    color = "black"
    
    args = sys.argv[1:]
    if args:
        # Buscar el serial en los argumentos
        if "--serial" in args:
            idx = args.index("--serial")
            if idx + 1 < len(args):
                serial = args[idx + 1]
    
    # Crear y mostrar la ventana
    window = ScrcpyWindow(serial=serial, device_type=device_type, color=color)
    window.show()
    
    sys.exit(app.exec())


if __name__ == "__main__":
    main()
