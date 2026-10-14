# Scrcpy Device Frame Plugin

Un plugin visual para **scrcpy** que añade un marco de dispositivo móvil estilo **Android Studio** / **Orca.dev** para que la pantalla se vea más profesional y atractiva.

## 📱 Vista Previa

El plugin transforma una ventana simple de scrcpy en un dispositivo móvil con:
- Marco realista con bordes redondeados
- "Notch" y cámara frontal (estilo iPhone/Android moderno)
- Botones de navegación en la parte inferior
- Efectos de sombra y degradados
- Tema oscuro por defecto (como Android Studio)

## ⚡ Características

- ✅ **Marco de dispositivo realista** - Se ve como un teléfono móvil real
- ✅ **Tema oscuro profesional** - Estilo Android Studio/Orca.dev
- ✅ **Soporte para múltiples dispositivos** - Phone y Tablet
- ✅ **Fácil de usar** - Solo ejecuta el script
- ✅ **Personalizable** - Cambia el tipo de dispositivo y color
- ✅ **Integración con scrcpy** - Usa todos los argumentos de scrcpy

## 📦 Requisitos

- **Python 3.7+**
- **scrcpy** instalado en tu sistema
- **PySide6** (para la interfaz gráfica)

## 🚀 Instalación

### 1. Instalar scrcpy

Primero, asegúrate de tener scrcpy instalado:

```bash
# En Linux (Debian/Ubuntu)
sudo apt install scrcpy

# En macOS
brew install scrcpy

# En Windows
# Descarga desde https://github.com/Genymobile/scrcpy/releases
```

### 2. Instalar dependencias de Python

```bash
pip install PySide6
```

### 3. Descargar el plugin

Clona este repositorio o descarga el archivo `scrcpy_device_frame.py`:

```bash
git clone https://github.com/Shin5hi/discord-bot-maker-android.git
cd discord-bot-maker-android
```

## 🎮 Uso

### Ejecución básica

```bash
python scrcpy_device_frame.py
```

### Con dispositivo específico

```bash
python scrcpy_device_frame.py --serial 192.168.1.100:5555
```

### Pasar argumentos a scrcpy

Todos los argumentos después del script se pasan directamente a scrcpy:

```bash
python scrcpy_device_frame.py --serial 192.168.1.100:5555 --bit-rate 8M --max-fps 60
```

### Opciones del plugin

| Opción | Descripción | Valor por defecto |
|--------|-------------|-------------------|
| `--serial` | Serial del dispositivo | `None` (selecciona automáticamente) |
| `--device-type` | Tipo de dispositivo (`phone` o `tablet`) | `phone` |
| `--color` | Color del dispositivo (`black` o `white`) | `black` |

## 🎨 Personalización

Puedes cambiar el tipo de dispositivo y color en tiempo de ejecución usando la interfaz gráfica:

1. **Selector de dispositivo**: Cambia entre Phone y Tablet
2. **Botón Conectar/Desconectar**: Inicia o detiene scrcpy
3. **Barra de estado**: Muestra el estado de la conexión

## 🔧 Configuración Avanzada

### Cambiar el tamaño de la ventana

Edita el archivo `scrcpy_device_frame.py` y modifica:

```python
self.resize(450, 900)  # Cambia a tu tamaño preferido
```

### Cambiar los colores

Modifica los colores en la clase `DeviceFrame`:

```python
frame_color = QColor(40, 40, 40)  # Color del marco
shadow_color = QColor(0, 0, 0, 150)  # Color de la sombra
highlight_color = QColor(100, 100, 100, 100)  # Color del resaltado
```

## 🐛 Solución de Problemas

### scrcpy no se inicia

1. Verifica que scrcpy esté instalado: `which scrcpy` (Linux/macOS) o `where scrcpy` (Windows)
2. Asegúrate de que el dispositivo esté conectado y con depuración USB habilitada
3. Prueba scrcpy directamente primero: `scrcpy --serial TU_SERIAL`

### Error de PySide6

```bash
ModuleNotFoundError: No module named 'PySide6'
```

Instala PySide6:
```bash
pip install PySide6
```

### La ventana aparece en blanco

1. Asegúrate de que scrcpy esté en tu PATH
2. Prueba con un dispositivo conectado
3. Verifica que la depuración USB esté habilitada en el dispositivo

## 📝 Notas

- Este plugin es un **wrapper visual** para scrcpy, no modifica scrcpy en sí
- Todos los argumentos de scrcpy son compatibles
- El marco es puramente decorativo y no afecta el funcionamiento de scrcpy
- Para mejor experiencia, usa el plugin en modo ventana (no a pantalla completa)

## 🎯 Futuras Mejoras

- [ ] Soporte para múltiples dispositivos simultáneamente
- [ ] Personalización avanzada del marco (colores, grosor, etc.)
- [ ] Integración con ADB para detección automática de dispositivos
- [ ] Modo de pantalla completa con marco
- [ ] Temas personalizables (claro/oscuro/colores personalizados)

## 📄 Licencia

MIT License - Libre para uso personal y comercial.

---

**¡Disfruta de tu scrcpy con estilo profesional!** 🎉

Inspirado en los diseños de Android Studio y Orca.dev.
