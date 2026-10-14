#!/usr/bin/env python3
"""
Scrcpy Device Frame - Versión Simple
=====================================

Una versión simplificada del marco de dispositivo que usa tkinter.
Funciona en sistemas donde PySide6 no está disponible.

Uso:
    python scrcpy_frame_simple.py [--serial SERIAL] [--width WIDTH] [--height HEIGHT]

Ejemplo:
    python scrcpy_frame_simple.py --serial 192.168.1.100:5555 --width 400 --height 800
"""

import sys
import subprocess
import argparse
import tkinter as tk
from tkinter import ttk
from PIL import Image, ImageDraw, ImageTk


class DeviceFrameCanvas:
    """Lienzo que dibuja un marco de dispositivo móvil"""
    
    def __init__(self, master, width=400, height=800, device_type="phone", color="black"):
        self.master = master
        self.width = width
        self.height = height
        self.device_type = device_type
        self.color = color
        self.canvas = None
        self.image = None
        self.photo_image = None
        
        self.create_canvas()
        
    def create_canvas(self):
        """Crear el lienzo"""
        if self.canvas:
            self.canvas.destroy()
            
        self.canvas = tk.Canvas(
            self.master,
            width=self.width,
            height=self.height,
            highlightthickness=0,
            bg="#1a1a1a"
        )
        self.canvas.pack(fill=tk.BOTH, expand=True)
        
        self.draw_device_frame()
        
    def draw_device_frame(self):
        """Dibujar el marco del dispositivo"""
        # Crear imagen con PIL
        self.image = Image.new('RGB', (self.width, self.height), color='#1a1a1a')
        draw = ImageDraw.Draw(self.image)
        
        # Colores
        if self.color == "black":
            frame_color = (40, 40, 40)
            screen_color = (20, 20, 20)
            border_color = (60, 60, 60)
            notch_color = (40, 40, 40)
            camera_color = (30, 30, 30)
        else:
            frame_color = (220, 220, 220)
            screen_color = (240, 240, 240)
            border_color = (180, 180, 180)
            notch_color = (220, 220, 220)
            camera_color = (200, 200, 200)
        
        # Dibujar el cuerpo del dispositivo
        corner_radius = 25
        draw.rounded_rectangle(
            [(0, 0), (self.width, self.height)],
            radius=corner_radius,
            fill=frame_color,
            outline=border_color,
            width=2
        )
        
        # Dibujar el área de la pantalla (con margen)
        screen_margin = 8
        screen_rect = [
            (screen_margin, screen_margin),
            (self.width - screen_margin, self.height - screen_margin)
        ]
        draw.rounded_rectangle(
            screen_rect,
            radius=corner_radius - 5,
            fill=screen_color,
            outline=border_color,
            width=1
        )
        
        # Dibujar el notch (para phone)
        if self.device_type == "phone":
            notch_width = 120
            notch_height = 25
            notch_x = (self.width - notch_width) // 2
            notch_y = screen_margin + 5
            
            draw.rounded_rectangle(
                [(notch_x, notch_y), (notch_x + notch_width, notch_y + notch_height)],
                radius=10,
                fill=notch_color,
                outline=border_color,
                width=1
            )
            
            # Cámara frontal
            camera_x = notch_x + notch_width // 2 - 15
            camera_y = notch_y + notch_height // 2 - 10
            draw.ellipse(
                [(camera_x, camera_y), (camera_x + 20, camera_y + 20)],
                fill=camera_color,
                outline=border_color,
                width=1
            )
            
            # Altavoz
            speaker_width = 60
            speaker_x = notch_x + (notch_width - speaker_width) // 2
            speaker_y = notch_y + notch_height + 5
            draw.rounded_rectangle(
                [(speaker_x, speaker_y), (speaker_x + speaker_width, speaker_y + 3)],
                radius=1,
                fill=(50, 50, 50) if self.color == "black" else (180, 180, 180),
                outline=border_color,
                width=1
            )
        
        # Dibujar botones de navegación
        nav_height = 60
        nav_y = self.height - screen_margin - nav_height - 5
        nav_width = 120
        nav_x = (self.width - nav_width) // 2
        
        draw.rounded_rectangle(
            [(nav_x, nav_y), (nav_x + nav_width, nav_y + nav_height)],
            radius=15,
            fill=(30, 30, 30) if self.color == "black" else (200, 200, 200),
            outline=border_color,
            width=1
        )
        
        # Botón central (Home)
        home_size = 40
        home_x = nav_x + (nav_width - home_size) // 2
        home_y = nav_y + (nav_height - home_size) // 2
        draw.ellipse(
            [(home_x, home_y), (home_x + home_size, home_y + home_size)],
            fill=(200, 200, 200) if self.color == "black" else (30, 30, 30),
            outline=border_color,
            width=1
        )
        
        # Convertir a PhotoImage y mostrar
        self.photo_image = ImageTk.PhotoImage(self.image)
        self.canvas.create_image(0, 0, anchor=tk.NW, image=self.photo_image)
        
    def resize(self, width, height):
        """Redimensionar el marco"""
        self.width = width
        self.height = height
        self.create_canvas()


class ScrcpySimpleWindow:
    """Ventana principal con marco de dispositivo simple"""
    
    def __init__(self, serial=None, width=400, height=800, device_type="phone", color="black"):
        self.root = tk.Tk()
        self.serial = serial
        self.width = width
        self.height = height
        self.device_type = device_type
        self.color = color
        self.scrcpy_process = None
        
        self.init_ui()
        
    def init_ui(self):
        """Inicializar la interfaz"""
        self.root.title("Scrcpy Device Frame - Simple")
        self.root.geometry(f"{self.width + 40}x{self.height + 100}")
        self.root.minsize(450, 900)
        
        # Estilo oscuro
        self.root.configure(bg="#1e1e1e")
        
        # Configurar el grid
        self.root.grid_columnconfigure(0, weight=1)
        self.root.grid_rowconfigure(1, weight=1)
        
        # Barra de título
        title_frame = tk.Frame(self.root, bg="#2b2b2b", height=40)
        title_frame.grid(row=0, column=0, sticky="ew", padx=10, pady=5)
        title_frame.grid_columnconfigure(0, weight=1)
        
        title_label = tk.Label(
            title_frame,
            text="Device Frame - Scrcpy",
            bg="#2b2b2b",
            fg="#ffffff",
            font=("Arial", 12, "bold")
        )
        title_label.grid(row=0, column=0, sticky="w", padx=10)
        
        close_btn = tk.Button(
            title_frame,
            text="X",
            bg="#ff4444",
            fg="#ffffff",
            bd=0,
            font=("Arial", 10, "bold"),
            command=self.root.destroy
        )
        close_btn.grid(row=0, column=1, padx=5)
        close_btn.configure(width=24, height=24)
        
        # Contenedor del dispositivo
        device_frame = tk.Frame(self.root, bg="#1a1a1a", padx=20, pady=10)
        device_frame.grid(row=1, column=0, sticky="nsew", padx=10, pady=5)
        device_frame.grid_columnconfigure(0, weight=1)
        device_frame.grid_rowconfigure(0, weight=1)
        
        # El canvas del dispositivo
        self.device_canvas = DeviceFrameCanvas(
            device_frame,
            self.width,
            self.height,
            self.device_type,
            self.color
        )
        
        # Barra de estado
        status_frame = tk.Frame(self.root, bg="#2b2b2b", height=40)
        status_frame.grid(row=2, column=0, sticky="ew", padx=10, pady=5)
        status_frame.grid_columnconfigure(1, weight=1)
        
        self.status_label = tk.Label(
            status_frame,
            text="Listo",
            bg="#2b2b2b",
            fg="#888888",
            font=("Arial", 10)
        )
        self.status_label.grid(row=0, column=0, sticky="w", padx=10)
        
        # Selector de dispositivo
        device_label = tk.Label(
            status_frame,
            text="Dispositivo:",
            bg="#2b2b2b",
            fg="#cccccc",
            font=("Arial", 10)
        )
        device_label.grid(row=0, column=1, sticky="e", padx=5)
        
        self.device_var = tk.StringVar(value=self.device_type)
        device_selector = ttk.Combobox(
            status_frame,
            textvariable=self.device_var,
            values=["phone", "tablet"],
            state="readonly",
            width=8
        )
        device_selector.grid(row=0, column=2, padx=5)
        device_selector.bind("<<ComboboxSelected>>", self.on_device_change)
        
        # Botón de conexión
        self.connect_btn = tk.Button(
            status_frame,
            text="Conectar",
            bg="#5865F2",
            fg="#ffffff",
            bd=0,
            font=("Arial", 10, "bold"),
            command=self.toggle_connection
        )
        self.connect_btn.grid(row=0, column=3, padx=10)
        
        # Hacer que la ventana sea redimensionable
        self.root.bind("<Configure>", self.on_resize)
        
    def on_resize(self, event):
        """Manejar el redimensionamiento"""
        new_width = event.width - 40
        new_height = event.height - 120
        
        if new_width > 100 and new_height > 100:
            self.width = new_width
            self.height = new_height
            self.device_canvas.resize(self.width, self.height)
            
    def on_device_change(self, event):
        """Cambiar el tipo de dispositivo"""
        self.device_type = self.device_var.get()
        self.device_canvas.device_type = self.device_type
        self.device_canvas.draw_device_frame()
        
    def toggle_connection(self):
        """Alternar la conexión"""
        if self.scrcpy_process:
            self.stop_scrcpy()
        else:
            self.start_scrcpy()
            
    def start_scrcpy(self):
        """Iniciar scrcpy"""
        cmd = ["scrcpy"]
        
        if self.serial:
            cmd.extend(["--serial", self.serial])
        
        cmd.extend([
            "--window-borderless",
            "--window-x", "0",
            "--window-y", "0",
            "--window-width", str(self.width),
            "--window-height", str(self.height),
            "--no-audio",
        ])
        
        self.status_label.config(text="Conectando...")
        self.connect_btn.config(text="Desconectar", bg="#ff4444")
        
        try:
            self.scrcpy_process = subprocess.Popen(
                cmd,
                stdout=subprocess.PIPE,
                stderr=subprocess.PIPE
            )
            self.root.after(1000, lambda: self.status_label.config(
                text="Scrcpy iniciado - Ajusta el tamaño manualmente"
            ))
        except FileNotFoundError:
            self.status_label.config(text="Error: scrcpy no encontrado")
            self.connect_btn.config(text="Conectar", bg="#5865F2")
            self.scrcpy_process = None
            
    def stop_scrcpy(self):
        """Detener scrcpy"""
        if self.scrcpy_process:
            self.scrcpy_process.terminate()
            self.scrcpy_process = None
            
        self.status_label.config(text="Desconectado")
        self.connect_btn.config(text="Conectar", bg="#5865F2")
        
    def run(self):
        """Ejecutar la aplicación"""
        self.root.mainloop()


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
    parser = argparse.ArgumentParser(description="Scrcpy Device Frame - Versión Simple")
    parser.add_argument("--serial", help="Serial del dispositivo")
    parser.add_argument("--width", type=int, default=400, help="Ancho de la ventana")
    parser.add_argument("--height", type=int, default=800, help="Alto de la ventana")
    parser.add_argument("--device-type", default="phone", choices=["phone", "tablet"],
                        help="Tipo de dispositivo")
    parser.add_argument("--color", default="black", choices=["black", "white"],
                        help="Color del dispositivo")
    
    args = parser.parse_args()
    
    # Verificar si scrcpy está instalado
    if not check_scrcpy_installed():
        print("Advertencia: scrcpy no está instalado en el sistema.")
        print("Instala scrcpy primero: https://github.com/Genymobile/scrcpy")
    
    # Crear y ejecutar la ventana
    app = ScrcpySimpleWindow(
        serial=args.serial,
        width=args.width,
        height=args.height,
        device_type=args.device_type,
        color=args.color
    )
    app.run()


if __name__ == "__main__":
    main()
