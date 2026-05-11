# 📱 Quiz2_ComplaintApp

<p align="center">
  <img src="https://img.shields.io/badge/Kotlin-Expert-purple?style=for-the-badge&logo=kotlin" />
  <img src="https://img.shields.io/badge/Firebase-Firestore-orange?style=for-the-badge&logo=firebase" />
  <img src="https://img.shields.io/badge/Android-Studio-green?style=for-the-badge&logo=android-studio" />
</p>

---

## 🚀 Overview
A professional **Community Complaint Management System** built with Kotlin and Firebase. This app allows users to register complaints, set priority levels, and track issues in real-time using a scalable architecture.

### ✨ Key Features
- **Real-time Database:** Powered by Firebase Firestore for instant updates.
- **Priority Logic:** Categorize complaints by **High**, **Medium**, or **Low**.
- **MVVM Architecture:** Clean separation of concerns for better maintainability.
- **Optimized UI:** Custom Spinners, RecyclerViews with `DiffUtil`, and Material Design components.

---

## 🛠️ Project Structure
This project follows the **SOLID** principles with a modular package structure:

| Package | Responsibility |
| :--- | :--- |
| **`models`** | Data classes (POJOs) for Firestore mapping. |
| **`utils`** | Repository logic, Constants, and Form Validation. |
| **`adapters`** | Custom RecyclerView adapters for efficient list rendering. |
| **`viewmodels`** | Business logic and UI state management. |

---

## 📦 Tech Stack & Libraries
- **Language:** Kotlin 1.8.20
- **Database:** Firebase Firestore
- **UI:** Material Components, XML Layouts
- **Dependency Management:** Gradle Version Catalog (`libs.versions.toml`)

---

## 🔧 Installation
1. Clone the repository:
   ```bash
   git clone [https://github.com/NomanAli42856/MAD_Quiz2_ComplaintApp.git](https://github.com/NomanAli42856/MAD_Quiz2_ComplaintApp.git)
