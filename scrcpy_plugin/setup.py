#!/usr/bin/env python3
"""
Setup script for Scrcpy Device Frame Plugin
"""

from setuptools import setup, find_packages

with open("README.md", "r", encoding="utf-8") as fh:
    long_description = fh.read()

setup(
    name="scrcpy-device-frame",
    version="1.0.0",
    author="Discord Bot Maker Team",
    author_email="",
    description="A visual plugin for scrcpy that adds a mobile device frame",
    long_description=long_description,
    long_description_content_type="text/markdown",
    url="https://github.com/Shin5hi/discord-bot-maker-android",
    packages=find_packages(),
    install_requires=[
        "PySide6>=6.0.0",
    ],
    python_requires=">=3.7",
    entry_points={
        "console_scripts": [
            "scrcpy-device-frame=scrcpy_plugin.scrcpy_device_frame:main",
        ],
    },
    classifiers=[
        "Development Status :: 4 - Beta",
        "Intended Audience :: Developers",
        "Intended Audience :: End Users/Desktop",
        "License :: OSI Approved :: MIT License",
        "Programming Language :: Python :: 3",
        "Programming Language :: Python :: 3.7",
        "Programming Language :: Python :: 3.8",
        "Programming Language :: Python :: 3.9",
        "Programming Language :: Python :: 3.10",
        "Programming Language :: Python :: 3.11",
        "Programming Language :: Python :: 3.12",
        "Operating System :: OS Independent",
        "Topic :: Desktop Environment",
        "Topic :: Multimedia :: Graphics",
        "Topic :: Utilities",
    ],
)
