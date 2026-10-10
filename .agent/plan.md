# Project Plan

Upwork helper, help new users how to setup the profile, include Gemini for help generating profile descriptions, tips, etc.

## Project Brief

# Project Brief: Upwork Profile Helper

## Features
- **Profile Creation Wizard:** A step-by-step guided flow that helps new freelancers input their core details, such as skills, past experience, and target hourly rates.
- **Gemini-Powered Bio Generator:** An AI assistant feature that takes the user's raw inputs and automatically generates professional, engaging, and highly optimized profile descriptions.
- **Freelancer Success Tips:** A dedicated section providing actionable advice, platform best practices, and tips for writing effective proposals on Upwork.
- **Profile Preview & Copy:** A final review screen where users can see their compiled profile and seamlessly copy the AI-generated text to paste directly into their Upwork account.

## High-Level Tech Stack
- **Language:** Kotlin
- **UI Toolkit:** Jetpack Compose
- **Navigation:** Jetpack Navigation 3 (state-driven navigation for the step-by-step wizard)
- **Adaptive Layouts:** Compose Material Adaptive (to ensure responsive design across mobile phones and larger screens/tablets)
- **Architecture & State Management:** MVVM with `ViewModel` and `StateFlow`
- **Asynchronous Operations:** Kotlin Coroutines
- **AI Integration:** Google Generative AI SDK for Android (for integrating the Gemini model)

## Implementation Steps
**Total Duration:** 59m 20s

### Task_1_SetupCoreNavigation: Set up project dependencies, MVVM architecture, Jetpack Navigation 3, and Compose Material Adaptive layout scaffolding. Create placeholders for Wizard, Tips, and Preview screens.
- **Status:** COMPLETED
- **Updates:** Dependencies for Navigation 3, Adaptive UI, and Gemini SDK added. MVVM setup with WizardViewModel. NavigationSuiteScaffold integrated for adaptive layout. Built app successfully.
- **Acceptance Criteria:**
  - Dependencies for Navigation, Adaptive, and Gemini SDK added
  - Navigation flow defined
  - Main screen scaffolded with adaptive layout
- **Duration:** 24m 10s

### Task_2_ImplementWizardAndTips: Implement the Profile Creation Wizard to collect user details (skills, experience, rate) and build the Freelancer Success Tips section. Back these with ViewModel.
- **Status:** COMPLETED
- **Updates:** Wizard UI and Tips UI fully implemented with ViewModel support and 14 passing unit tests. Built app successfully.
- **Acceptance Criteria:**
  - Wizard UI collects user input accurately
  - StateFlow in ViewModel updates on input
  - Success Tips screen is readable and navigable
- **Duration:** 5m 20s

### Task_3_GeminiIntegrationAndPreview: Integrate the Google Generative AI SDK to generate a professional bio based on ViewModel data. Implement the Profile Preview & Copy screen.
- **Status:** COMPLETED
- **Updates:** Gemini SDK integrated with gemini-1.5-flash and dynamic key/mock fallback. Profile Preview screen implemented with bio generation, copy to clipboard, and snackbar feedback. 19 unit tests passing. Built app successfully.
- **Acceptance Criteria:**
  - API_KEY integration is complete and secure
  - Bio generation works and displays text
  - Copy to clipboard functionality works on Preview screen
- **Duration:** 7m 7s

### Task_4_RunAndVerify: Run and Verify the application. Instruct critic_agent to verify application stability (no crashes), confirm alignment with user requirements, and report critical UI issues.
- **Status:** COMPLETED
- **Updates:** Verification completed successfully. The critic agent confirmed application stability with zero crashes, fixed experience level chips clipping using FlowRow, and resolved badge pill word wrapping. All unit tests passed and app build succeeded.
- **Acceptance Criteria:**
  - make sure all existing tests pass
  - build pass
  - app does not crash
  - critic_agent reports no critical issues
- **Duration:** 22m 43s

