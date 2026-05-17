# CodeAlpha-Java-Programming-Internship
# 🎓 Student Grade Management System

A Java Swing-based desktop application to manage and analyze student grades across multiple subjects — built as part of the **CodeAlpha Java Programming Internship**.

---

## ✨ Features

### 📥 Student Data Input
- Enter student name and marks for **4 subjects**: Math, Science, English, and Computer
- Each subject field is **colour-coded** for quick visual identification
- Input validation ensures marks stay within the **0–100** range
- Friendly error alerts for invalid or missing input

### 📊 Student Records Table
- Displays all added students in a clean, **scrollable table**
- Columns: `#`, `Name`, `Math`, `Science`, `English`, `Computer`, `Percentage`, `Grade`
- **Letter grade** is auto-calculated and **colour-coded**:

  | Grade | Percentage  | Color  |
  |-------|-------------|--------|
  | A+    | 90% – 100%  | 🟢 Green  |
  | A     | 75% – 89%   | 🟩 Light Green |
  | B     | 60% – 74%   | 🟡 Yellow |
  | C     | 45% – 59%   | 🟠 Orange |
  | F     | Below 45%   | 🔴 Red    |

### 📈 Live Subject-wise Analysis
Updates automatically after every student is added:
- **▲ Highest** mark scored in that subject across the class
- **▼ Lowest** mark scored in that subject across the class
- **~ Average** mark of the whole class in that subject

Tracked for all 4 subjects individually.

### 🏆 Class Summary Stats
- **Class Average Percentage** — overall average of all students
- **Class Topper** — student with the highest total percentage
- **Needs Improvement** — student with the lowest total percentage

### 🎨 Modern Dark UI
- Sleek **dark navy theme** with indigo/violet accent colors
- Gradient **"Add Student"** button
- Responsive layout that scales with window size
- Built entirely with **Java Swing** — no external libraries required

### 🗑️ Data Management
- **Clear All** button with a confirmation dialog to reset all records
- Input fields auto-clear after each student is successfully added

---

## 🚀 How to Run

### Prerequisites
- Java JDK 8 or above installed
- No external dependencies needed

### Steps

```bash
# 1. Clone the repository
git clone https://github.com/your-username/student-grade-manager.git

# 2. Navigate to the project folder
cd student-grade-manager

# 3. Compile the program
javac StudentGradeManagerGUI.java

# 4. Run the program
java StudentGradeManagerGUI
```

---

## 🛠️ Tech Stack

| Technology | Usage |
|------------|-------|
| Java       | Core programming language |
| Java Swing | GUI framework |
| ArrayList  | Dynamic student data storage |
| JTable     | Tabular data display |

---

## 📁 Project Structure

```
student-grade-manager/
│
├── StudentGradeManagerGUI.java   # Main application file
└── README.md                     # Project documentation
```

---

## 👨‍💻 Author

Harsh Raj
Java Programming Intern — CodeAlpha
Student ID: CA/DF1/76768

---
