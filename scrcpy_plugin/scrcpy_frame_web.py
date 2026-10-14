#!/usr/bin/env python3
"""
Scrcpy Device Frame - Versión Web
==================================

Una versión web del marco de dispositivo que usa Flask y HTML5 Canvas.
Puedes ejecutarlo y acceder desde un navegador.

Uso:
    python scrcpy_frame_web.py [--port PORT] [--host HOST]

Ejemplo:
    python scrcpy_frame_web.py --port 5000 --host 0.0.0.0

Luego abre: http://localhost:5000
"""

import sys
import argparse
from flask import Flask, render_template_string


HTML_TEMPLATE = """
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Scrcpy Device Frame</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }
        
        body {
            background-color: #1e1e1e;
            color: #ffffff;
            font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, sans-serif;
            height: 100vh;
            display: flex;
            flex-direction: column;
        }
        
        .title-bar {
            background-color: #2b2b2b;
            border-bottom: 1px solid #404040;
            padding: 10px 20px;
            display: flex;
            align-items: center;
            justify-content: space-between;
        }
        
        .title-bar h1 {
            font-size: 16px;
            font-weight: 600;
        }
        
        .window-controls {
            display: flex;
            gap: 8px;
        }
        
        .window-controls button {
            width: 24px;
            height: 24px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 14px;
            font-weight: bold;
        }
        
        .close-btn {
            background-color: #ff4444;
            color: white;
        }
        
        .minimize-btn {
            background-color: #404040;
            color: white;
        }
        
        .device-container {
            flex: 1;
            display: flex;
            justify-content: center;
            align-items: center;
            padding: 20px;
            overflow: auto;
        }
        
        .device-wrapper {
            position: relative;
            max-width: 100%;
            max-height: 100%;
        }
        
        canvas {
            background-color: #1a1a1a;
            max-width: 100%;
            max-height: 100%;
        }
        
        .status-bar {
            background-color: #2b2b2b;
            border-top: 1px solid #404040;
            padding: 10px 20px;
            display: flex;
            align-items: center;
            justify-content: space-between;
        }
        
        .status-bar .status-text {
            color: #888888;
            font-size: 14px;
        }
        
        .controls {
            display: flex;
            gap: 15px;
            align-items: center;
        }
        
        .controls select, .controls button {
            padding: 8px 16px;
            border: none;
            border-radius: 4px;
            background-color: #333333;
            color: #ffffff;
            font-size: 14px;
            cursor: pointer;
        }
        
        .controls button {
            background-color: #5865F2;
        }
        
        .controls button:hover {
            background-color: #6d79f7;
        }
        
        .controls button:disabled {
            background-color: #404040;
            cursor: not-allowed;
        }
        
        .connect-btn.disconnected {
            background-color: #5865F2;
        }
        
        .connect-btn.connected {
            background-color: #ff4444;
        }
    </style>
</head>
<body>
    <div class="title-bar">
        <h1>Device Frame - Scrcpy</h1>
        <div class="window-controls">
            <button class="minimize-btn" onclick="window.minimize()">_</button>
            <button class="close-btn" onclick="window.close()">X</button>
        </div>
    </div>
    
    <div class="device-container">
        <div class="device-wrapper">
            <canvas id="deviceCanvas"></canvas>
        </div>
    </div>
    
    <div class="status-bar">
        <span class="status-text" id="statusText">Listo</span>
        <div class="controls">
            <select id="deviceType" onchange="updateDeviceType()">
                <option value="phone">Phone</option>
                <option value="tablet">Tablet</option>
            </select>
            <select id="deviceColor" onchange="updateDeviceColor()">
                <option value="black">Negro</option>
                <option value="white">Blanco</option>
            </select>
            <button class="connect-btn disconnected" id="connectBtn" onclick="toggleConnection()">
                Conectar
            </button>
        </div>
    </div>
    
    <script>
        const canvas = document.getElementById('deviceCanvas');
        const ctx = canvas.getContext('2d');
        const statusText = document.getElementById('statusText');
        const connectBtn = document.getElementById('connectBtn');
        const deviceTypeSelect = document.getElementById('deviceType');
        const deviceColorSelect = document.getElementById('deviceColor');
        
        let deviceType = 'phone';
        let deviceColor = 'black';
        let isConnected = false;
        
        // Tamaño inicial del canvas
        let canvasWidth = Math.min(window.innerWidth - 40, 450);
        let canvasHeight = Math.min(window.innerHeight - 120, 900);
        
        canvas.width = canvasWidth;
        canvas.height = canvasHeight;
        
        function drawDeviceFrame() {
            ctx.clearRect(0, 0, canvas.width, canvas.height);
            
            // Colores
            let frameColor, screenColor, borderColor, notchColor, cameraColor, navColor, homeColor;
            
            if (deviceColor === 'black') {
                frameColor = '#282828';
                screenColor = '#141414';
                borderColor = '#3c3c3c';
                notchColor = '#282828';
                cameraColor = '#1e1e1e';
                navColor = '#1e1e1e';
                homeColor = '#c8c8c8';
            } else {
                frameColor = '#e0e0e0';
                screenColor = '#f5f5f5';
                borderColor = '#b0b0b0';
                notchColor = '#e0e0e0';
                cameraColor = '#d0d0d0';
                navColor = '#d0d0d0';
                homeColor = '#333333';
            }
            
            // Dibujar el cuerpo del dispositivo
            const cornerRadius = 25;
            drawRoundedRect(0, 0, canvas.width, canvas.height, cornerRadius, frameColor, borderColor, 2);
            
            // Dibujar el área de la pantalla
            const screenMargin = 8;
            drawRoundedRect(
                screenMargin, screenMargin,
                canvas.width - screenMargin * 2, canvas.height - screenMargin * 2,
                cornerRadius - 5, screenColor, borderColor, 1
            );
            
            // Dibujar el notch (para phone)
            if (deviceType === 'phone') {
                const notchWidth = 120;
                const notchHeight = 25;
                const notchX = (canvas.width - notchWidth) / 2;
                const notchY = screenMargin + 5;
                
                drawRoundedRect(
                    notchX, notchY, notchWidth, notchHeight,
                    10, notchColor, borderColor, 1
                );
                
                // Cámara frontal
                const cameraX = notchX + notchWidth / 2 - 15;
                const cameraY = notchY + notchHeight / 2 - 10;
                ctx.fillStyle = cameraColor;
                ctx.beginPath();
                ctx.arc(cameraX + 10, cameraY + 10, 10, 0, Math.PI * 2);
                ctx.fill();
                ctx.strokeStyle = borderColor;
                ctx.lineWidth = 1;
                ctx.stroke();
                
                // Altavoz
                const speakerWidth = 60;
                const speakerX = notchX + (notchWidth - speakerWidth) / 2;
                const speakerY = notchY + notchHeight + 5;
                drawRoundedRect(
                    speakerX, speakerY, speakerWidth, 3,
                    1, '#323232', borderColor, 1
                );
            }
            
            // Dibujar botones de navegación
            const navHeight = 60;
            const navY = canvas.height - screenMargin - navHeight - 5;
            const navWidth = 120;
            const navX = (canvas.width - navWidth) / 2;
            
            drawRoundedRect(
                navX, navY, navWidth, navHeight,
                15, navColor, borderColor, 1
            );
            
            // Botón central (Home)
            const homeSize = 40;
            const homeX = navX + (navWidth - homeSize) / 2;
            const homeY = navY + (navHeight - homeSize) / 2;
            ctx.fillStyle = homeColor;
            ctx.beginPath();
            ctx.arc(homeX + homeSize / 2, homeY + homeSize / 2, homeSize / 2, 0, Math.PI * 2);
            ctx.fill();
            ctx.strokeStyle = borderColor;
            ctx.lineWidth = 1;
            ctx.stroke();
        }
        
        function drawRoundedRect(x, y, width, height, radius, fillColor, strokeColor, lineWidth) {
            ctx.fillStyle = fillColor;
            ctx.strokeStyle = strokeColor;
            ctx.lineWidth = lineWidth;
            
            ctx.beginPath();
            ctx.moveTo(x + radius, y);
            ctx.lineTo(x + width - radius, y);
            ctx.quadraticCurveTo(x + width, y, x + width, y + radius);
            ctx.lineTo(x + width, y + height - radius);
            ctx.quadraticCurveTo(x + width, y + height, x + width - radius, y + height);
            ctx.lineTo(x + radius, y + height);
            ctx.quadraticCurveTo(x, y + height, x, y + height - radius);
            ctx.lineTo(x, y + radius);
            ctx.quadraticCurveTo(x, y, x + radius, y);
            ctx.closePath();
            
            if (fillColor) {
                ctx.fill();
            }
            if (strokeColor && lineWidth > 0) {
                ctx.stroke();
            }
        }
        
        function updateDeviceType() {
            deviceType = deviceTypeSelect.value;
            drawDeviceFrame();
        }
        
        function updateDeviceColor() {
            deviceColor = deviceColorSelect.value;
            drawDeviceFrame();
        }
        
        function toggleConnection() {
            if (isConnected) {
                stopScrcpy();
            } else {
                startScrcpy();
            }
        }
        
        function startScrcpy() {
            statusText.textContent = 'Conectando a scrcpy...';
            connectBtn.textContent = 'Desconectar';
            connectBtn.className = 'connect-btn connected';
            isConnected = true;
            
            // Mostrar mensaje
            setTimeout(() => {
                statusText.textContent = 'Scrcpy iniciado - Ejecuta scrcpy manualmente con: scrcpy --window-borderless --window-width ' + canvas.width + ' --window-height ' + canvas.height;
            }, 1000);
        }
        
        function stopScrcpy() {
            statusText.textContent = 'Desconectado';
            connectBtn.textContent = 'Conectar';
            connectBtn.className = 'connect-btn disconnected';
            isConnected = false;
        }
        
        // Redimensionar canvas
        function resizeCanvas() {
            const container = document.querySelector('.device-container');
            const maxWidth = container.clientWidth - 40;
            const maxHeight = container.clientHeight - 40;
            
            canvasWidth = Math.min(maxWidth, 450);
            canvasHeight = Math.min(maxHeight, 900);
            
            canvas.width = canvasWidth;
            canvas.height = canvasHeight;
            drawDeviceFrame();
        }
        
        window.addEventListener('resize', () => {
            resizeCanvas();
        });
        
        // Inicializar
        resizeCanvas();
        drawDeviceFrame();
    </script>
</body>
</html>
"""


def create_app():
    """Crear la aplicación Flask"""
    app = Flask(__name__)
    
    @app.route('/')
    def index():
        return render_template_string(HTML_TEMPLATE)
    
    return app


def main():
    """Punto de entrada principal"""
    parser = argparse.ArgumentParser(description="Scrcpy Device Frame - Versión Web")
    parser.add_argument("--port", type=int, default=5000, help="Puerto del servidor")
    parser.add_argument("--host", default="127.0.0.1", help="Host del servidor")
    
    args = parser.parse_args()
    
    app = create_app()
    
    print(f"Iniciando servidor en http://{args.host}:{args.port}")
    print("Abre tu navegador y navega a esa dirección")
    print("\nPara usar con scrcpy:")
    print("  1. Abre el navegador y ve a http://localhost:5000")
    print("  2. Ajusta el tamaño de la ventana del navegador")
    print("  3. Ejecuta scrcpy con: scrcpy --window-borderless --window-width WIDTH --window-height HEIGHT")
    print("\nPresiona Ctrl+C para detener el servidor")
    
    app.run(host=args.host, port=args.port, debug=False)


if __name__ == "__main__":
    main()
