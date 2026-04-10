# Simple Personal Expense Tracker

## Project Overview

Simple Personal Expense Tracker is a native Android application built with Kotlin that helps users track their personal income and expenses. The app allows users to record financial transactions, view them with flexible filtering options, and see spending summaries. The app works fully offline with no backend or network requirements.

---

## Features Implemented

### Core Features
- **Add Transactions**: Create new expense or income entries with:
  - Title/description
  - Amount
  - Type (Expense/Income)
  - Date (with date picker)
  
- **View Transactions**: Display transactions in a list view with multiple filtering options:
  - Filter by Year (shows monthly breakdown)
  - Filter by Month (shows individual transactions)
  - Filter by Day (shows transactions for specific day)
  - View "All" for yearly summary

- **Summary Statistics**: 
  - Real-time total calculation based on selected filter
  - Displays total spending/income for selected period
  - Automatic aggregation by year/month

- **Edit Transactions**: Tap on any transaction to edit its details

- **Delete Transactions**: Long-press on any transaction to delete it

- **Offline Storage**: All data stored locally using SQLite database

- **Dynamic Date Handling**: Smart day spinner that adjusts based on selected month (handles leap years, different month lengths)

---

## Tech Stack

### Language & Platform
| Component | Version |
|-----------|---------|
| Language | Kotlin 100% |
| Platform | Android (Minimum SDK 21+) |
| IDE | Android Studio |

### Architecture & Design Patterns
- **Architecture**: MVC (Model-View-Controller)
- **Design Pattern**: Listener pattern for spinner events

### Libraries & APIs
```gradle
// Android SDK
- android.database.sqlite (SQLite)
- DatePickerDialog
- ArrayAdapter, ListView, Spinner
- Intent (Activity communication)

// AndroidX
- androidx.appcompat:appcompat
- androidx.core:core-ktx
- androidx.constraintlayout:constraintlayout
