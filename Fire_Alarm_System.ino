#include <WiFi.h>
#include <FirebaseESP32.h>

// Firebase configuration
#define FIREBASE_HOST "fire-alarm-system-a8a2e-default-rtdb.firebaseio.com"
#define FIREBASE_AUTH "AIzaSyCzpL74YEKFP-xSHCyXMZE9YgeO7UW7Gnk"

// WiFi credentials
const char* ssid = "xyz";
const char* password = "9708061457";

// Define sensor and buzzer pins
#define FLAME_SENSOR_PIN 13
#define BUZZER_PIN 14

FirebaseData firebaseData;
FirebaseConfig firebaseConfig;
FirebaseAuth firebaseAuth;

void setup() {
  Serial.begin(115200);
  pinMode(FLAME_SENSOR_PIN, INPUT);
  pinMode(BUZZER_PIN, OUTPUT);
  digitalWrite(BUZZER_PIN, LOW);

  // Connect to WiFi
  WiFi.begin(ssid, password);
  while (WiFi.status() != WL_CONNECTED) {
    delay(1000);
    Serial.println("Connecting to WiFi...");
  }
  Serial.println("Connected to WiFi");

  // Set Firebase host and authentication
  firebaseConfig.host = FIREBASE_HOST;
  firebaseConfig.signer.tokens.legacy_token = FIREBASE_AUTH;

  // Initialize Firebase
  Firebase.begin(&firebaseConfig, &firebaseAuth);
  Firebase.reconnectWiFi(true);
}

void loop() {
  int flameDetected = digitalRead(FLAME_SENSOR_PIN);

  if (flameDetected == LOW) {
    Serial.println("Flame detected!");
    digitalWrite(BUZZER_PIN, HIGH);
    if (Firebase.setBool(firebaseData, "/fireDetected", true)) {
      Serial.println("Firebase updated: fireDetected = true");
    } else {
      Serial.println("Failed to update Firebase");
    }
  } else {
    Serial.println("No flame detected.");
    digitalWrite(BUZZER_PIN, LOW);
    if (Firebase.setBool(firebaseData, "/fireDetected", false)) {
      Serial.println("Firebase updated: fireDetected = false");
    } else {
      Serial.println("Failed to update Firebase");
    }
  }

  delay(2000);
}
