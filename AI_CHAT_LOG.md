# AI Chat Log – Simple Personal Expense Tracker

**Candidate**: Do Quy Bach  
**Position**: Android Mobile Intern (Kotlin)

---

## 1. Asking How to Push Code to GitHub
**Prompt**:  
> "How to push code from Android Studio to Git?"

**AI Suggestion**:  
- Guided me through commit, configure remote, and push using both GUI and terminal

**What I Used**:  
- Used terminal commands: `git add .`, `git commit -m "..."`, `git push`

**What I Modified**:  
- When encountering `rejected` error, I used `git push --force` because the repo was newly created with no important data.

---

## 2. Asking How to Write README.md
**Prompt**:  
> "Create a README.md following the technical test requirements from my Kotlin code"

**AI Suggestion**:  
- Suggested README structure with 7 sections: Overview, Features, Tech Stack, Architecture, API, Data Model, How to Run

**What I Used**:  
- Adopted the structure and key content about features, tech stack, and how to run the app

**What I Modified**:  
- Simplified the content to be shorter and closer to my actual implementation (e.g., using plain SQLite instead of Room)

---

## 3. Asking About Simple UI Design
**Prompt**:  
> "Build a simple UI for an expense tracker app, Kotlin, offline"

**AI Suggestion**:  
- Use `ListView` + `ArrayAdapter` to display transaction list
- Use `Spinner` for filtering by day/month/year
- Store data using `SQLiteOpenHelper`

**What I Used**:  
- Designed UI with `ListView`, `Spinner`, and `DatePickerDialog` as AI suggested
- Used `SQLiteOpenHelper` for offline CRUD operations

**What I Modified**:  
- Implemented my own filtering logic using `strftime` in SQLite queries
- Handled amount display: negative for expenses, positive for income

---

## Personal Reflection
- AI helped me save time understanding README structure and Git push workflow
- I did not blindly copy AI code — I always reviewed and adjusted to fit my app's logic
- Small project, simple UI → I prioritized clean, maintainable code over complex architecture
