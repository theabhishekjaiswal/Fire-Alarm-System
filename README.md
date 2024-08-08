# Fire Alarm System with Realtime Mobile Alerts

## Introduction
This project aims to develop a Fire Alarm System that detects the presence of fire using sensors and sends real-time alerts to a mobile application. The system integrates hardware components with a mobile app to enhance safety and convenience.

## Table of Contents
- [Objectives](#objectives)
- [Components](#components)
- [Tech Stack](#tech-stack)
- [Circuit Diagram](#circuit-diagram)
- [Mobile Application Development](#mobile-application-development)
- [Testing](#testing)
- [Results](#results)
- [Mobile Screenshorts](#mobile-screenshorts)
- [Usage](#usage)
- [Features](#features)
- [Learnings](#learnings)
- [Physics of the Flame Sensor](#physics-of-the-flame-sensor)
- [Conclusion](#conclusion)
- [Demonstration Video](#demonstration-video)

## Objectives
- **Detection**: Create a system that detects the presence of fire using sensors.
- **Notification**: Send real-time alerts to a mobile application when a fire is detected.
- **Integration**: Seamlessly integrate hardware components with a mobile application for enhanced safety and convenience.

## Components
- **Flame Sensor**: Detects the presence of fire or heat.
- **Wi-Fi Module (ESP-WROOM-32)**: Facilitates wireless communication between the hardware and the mobile app.
- **LED RGB**: Provides visual indication of the system status (e.g., normal, warning, alert).
- **USB Cable & Jumper Wires**: Used for connecting and powering the components.

## Tech Stack
- **Arduino IDE**: Development environment for programming the Arduino microcontroller.
- **Arduino Programming**: Code to handle sensor inputs, process data, and communicate with the mobile app.
- **Android Studio**: Integrated development environment (IDE) for building the mobile application.
- **Kotlin**: Programming language for developing the Android app.
- **Firebase**: Backend platform for real-time database management and notifications.

## Circuit Diagram
- The flame sensor connects to digital pin D13 for reading the sensor output and to the 5V and GND pins for power.
- The Wi-Fi module (ESP-WROOM-32) is connected to the 3.3V pin for power and the RX/TX pins for serial communication with the Arduino.
- The RGB LED connections are defined in the code but typically connect to different digital pins for controlling the LED colors.

## Mobile Application Development
The Android app is developed using Kotlin in Android Studio. It connects to Firebase to receive notifications about fire detection and displays real-time alerts to the user.

## Testing
The system is tested under different scenarios to verify that the flame sensor correctly detects fire, the LED responds appropriately, and the mobile app receives real-time notifications.

## Results
- **Detection Accuracy**: The flame sensor effectively detects fire, and the system activates the buzzer and LED as expected.
- **Real-Time Notifications**: Notifications are successfully sent to the mobile app through Firebase, providing timely alerts.
- **System Reliability**: The integrated system demonstrates consistent performance and reliable fire detection.


## Usage
1. Power on the system.
2. Ensure the mobile application is installed .
3. When a fire is detected, the system will:
   - Activate the RGB LED to indicate the alert status.
   - Send a real-time notification to the mobile app.

## Features
- Real-time fire detection and alerts.
- Visual indication of system status through an RGB LED.
- Mobile application integration for remote alerts.

## Learnings
- **Integration Skills**: Enhanced understanding of integrating hardware with software and managing real-time data.
- **Firebase Utilization**: Gained practical experience in using Firebase for real-time database management and notifications.
- **Problem-Solving**: Improved ability to troubleshoot and resolve issues related to sensor accuracy, communication reliability, and app development.

## Physics of the Flame Sensor
- **Infrared Radiation**: Flames emit IR radiation due to their high temperature.
- **Detection Mechanism**:
  - **Photodiode/Phototransistor**: Converts IR radiation into an electrical signal.
  - **Photodiode**: Generates a current proportional to IR intensity.
  - **Phototransistor**: Alters conductivity based on IR radiation.
- **Output Types**:
  - **Analog Output**: Voltage varies with IR intensity.
  - **Digital Output**: High or low signal based on IR threshold.
- **Sensitivity and Range**:
  - **Sensitivity**: Detects small amounts of IR radiation.
  - **Range**: Effective distance for flame detection.

## Conclusion
The Fire Alarm System project successfully meets its objective of detecting fire and sending real-time notifications to a mobile application. By combining hardware and software components, the project enhances safety through timely alerts and reliable performance. This project provided valuable experience in system integration, real-time communication, and application development.

## Demonstration Video
To view the demonstration of the Fire Alarm System in action, please follow the link below or scan the QR code.
- **Watch the Video Here**: [Demonstration Video](https://www.youtube.com/watch?v=S4Cb5PRZVgY&ab_channel=TheAbhishekJaiswal)
- **Scan the QR Code to Watch**: ![QR Code](#)
