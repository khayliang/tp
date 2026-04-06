---
layout: page
title: User Guide
---

TutorFlow is a **desktop app for freelance private tutors who need to manage students, parents, billing, and lesson schedules in one place**. It is optimized for keyboard-first use, so tutors who are comfortable typing commands can update records faster than with a mouse-only workflow.

TutorFlow keeps your student list, parent / guardian details, academics, tuition billing, payment history, appointments, and attendance records together in a single interface.

![TutorFlow interface](images/UI_v1.4.png)

* Table of Contents
{:toc}

--------------------------------------------------------------------------------------------------------------------

## Quick start

1. Ensure that Java `17` or above is installed on your computer.  
   **Mac users:** follow the setup notes [here](https://se-education.org/guides/tutorials/javaInstallationMac.html).

1. Download the latest `tutorflow.jar` from the [releases page](https://github.com/AY2526S2-CS2103T-T09-3/tp/releases).

1. Move the jar file into the folder you want TutorFlow to use as its home folder.

1. Open a terminal, `cd` into that folder, and run:

   `java -jar tutorflow.jar`

1. On first launch, or when no existing data file is present, TutorFlow starts with sample data so that you can try the commands immediately.

1. Type a command into the command box and press Enter to run it. Try these first:

   * `list`
   * `view 1`
   * `find student alex`
   * `help`
   * `exit`

1. Refer to the [Command Reference](#student-management) sections below for full details.

--------------------------------------------------------------------------------------------------------------------

## TutorFlow at a glance

TutorFlow is organized around a few core areas:

* The **student list** shows the students in the current view. Any command that uses `INDEX` refers to this displayed list.
* The **detail panel** shows the selected student's full record, including numbered tags, subjects, and appointments.
* The **result display** confirms whether a command succeeded or failed.
* The **command box** is where you type commands.

<div markdown="span" class="alert alert-primary">:bulb: **Tip:**  
For commands such as `delete tag`, `delete acad`, `delete appt`, and `add attd`, the sub-item indexes come from the selected student's detail panel. Use `view INDEX` first if you need to see those numbered items clearly.
</div>

--------------------------------------------------------------------------------------------------------------------

## Reading command formats

<div markdown="block" class="alert alert-info">

**Command format notes**

* Words in `UPPER_CASE` are values you must supply.  
  Example: in `add student n/NAME`, replace `NAME` with an actual name such as `John Doe`.

* Items in square brackets are optional.  
  Example: `edit billing INDEX [a/AMOUNT] [d/DATE]`

* Items followed by `...` can be repeated.  
  Example: `add tag INDEX t/TAG [t/TAG]...`

* For commands that use prefixes, the order of prefixed fields usually does not matter.  
  Example: `p/91234567 n/John Doe` is accepted for commands that expect both fields.

* Whenever a command uses `INDEX`, it must be a positive integer such as `1`, `2`, or `3`.

* Unless stated otherwise, `INDEX` refers to the **currently displayed student list**, not to a permanent student ID.
  Some commands also use sub-item indexes such as `TAG_INDEX`, `SUBJECT_INDEX`, or `APPT_INDEX`; those come from the selected student's detail panel.

* Commands without parameters, such as `help`, `list`, `clear`, and `exit`, ignore extra text after the command word.

* If you are using a PDF version of this guide, be careful when copying multi-line commands. Some PDF viewers may remove spaces around line breaks.

</div>

--------------------------------------------------------------------------------------------------------------------

## Student Management

Use these commands to add, update, view, list, find, and remove student records.

### Adding a student : `add student`

Adds a new student to TutorFlow.

Format: `add student n/NAME p/PHONE e/EMAIL a/ADDRESS [t/TAG]...`

Details:
* `n/`, `p/`, `e/`, and `a/` are required.
* `t/` is optional and can be repeated.
* A student can be created without any tags. You can add tags later with `add tag`.

Examples:
* `add student n/John Doe p/98765432 e/johnd@example.com a/John street, block 123, #01-01`
* `add student n/Betsy Crowe p/91234567 e/betsycrowe@example.com a/10 Clementi Road t/Upper Sec t/Math`

### Editing a student : `edit student`

Edits the basic contact details of an existing student.

Format: `edit student INDEX [n/NAME] [p/PHONE] [e/EMAIL] [a/ADDRESS]`

Details:
* Edits the student at the specified `INDEX`.
* At least one field must be provided.
* Only the fields you provide are updated. Unspecified fields stay unchanged.

Examples:
* `edit student 1 p/91234567 e/johndoe@example.com`
* `edit student 2 n/Betsy Crowe`

### Deleting a student : `delete student`

Deletes the specified student from TutorFlow.

Format: `delete student INDEX`

Details:
* Deletes the student at the specified `INDEX`.
* The index must refer to a student in the current displayed list.

Examples:
* `list` followed by `delete student 2`
* `find student Betsy` followed by `delete student 1`

### Viewing a student's details : `view`

Selects a student and shows the full record in the detail panel.

Format: `view INDEX`

Details:
* Use this command when you need to inspect parent details, academics, billing, tags, subjects, appointments, or attendance.
* The detail panel also shows the numbered tag, subject, and appointment indexes used by some other commands.

Examples:
* `view 1`
* `view 3`

### Listing all students : `list`

Shows the full student list.

Format: `list`

### Locating students by name : `find student`

Finds students whose names contain any of the given keywords.

Format: `find student KEYWORD [MORE_KEYWORDS]`

Details:
* The search is case-insensitive.  
  Example: `alex` matches `Alex`
* The order of keywords does not matter.
* Only student names are searched.
* Matching is by full word, not substring.  
  Example: `Al` does not match `Alex`
* A student is returned if the name matches **at least one** keyword.

Examples:
* `find student John`
* `find student alex david`  
  ![Result for 'find student alex david'](images/findAlexDavidResult.png)

--------------------------------------------------------------------------------------------------------------------

## Tag Management

Use tags to group students by level, stream, exam target, or any other label that fits your teaching workflow.

### Adding tags to a student : `add tag`

Adds one or more tags to an existing student.

Format: `add tag INDEX t/TAG [t/TAG]...`

Details:
* Adds the given tag or tags to the student at `INDEX`.
* At least one `t/` prefix is required.
* Existing tags are kept.
* Duplicate tags are ignored.

Examples:
* `add tag 1 t/JC`
* `add tag 2 t/Upper Sec t/Programming`

### Editing a student's tags : `edit tag`

Replaces the student's full tag list.

Format: `edit tag INDEX [t/TAG]...`

Details:
* All existing tags are replaced by the tags you provide.
* Use `t/` with no value to clear all tags from that student.

Examples:
* `edit tag 1 t/JC t/J1`
* `edit tag 2 t/`

### Deleting tags from a student : `delete tag`

Deletes specific tags from a student by tag index.

Format: `delete tag INDEX t/TAG_INDEX [t/TAG_INDEX]...`

Details:
* `INDEX` is the student index in the current displayed list.
* Each `TAG_INDEX` is taken from the numbered tag list in that student's detail panel.
* At least one `t/` prefix is required.

Examples:
* `delete tag 1 t/2`
* `delete tag 1 t/2 t/3`

### Locating students by tag : `find tag`

Finds students whose tags match any of the given tag keywords.

Format: `find tag t/TAG [t/TAG]...`

Details:
* At least one `t/` prefix is required.
* Multiple `t/` prefixes are allowed.
* Tag matching is case-insensitive.
* Tag matching is partial.  
  Example: `t/math` matches the tag `Mathematics`
* A student is returned if any tag matches at least one keyword.

Examples:
* `find tag t/JC`
* `find tag t/Upper t/Programming`

--------------------------------------------------------------------------------------------------------------------

## Academic Management

Use academic records to keep track of the subjects a student takes and any overall academic notes.

### Adding subjects to a student : `add acad`

Adds one or more subjects to a student's academic record.

Format: `add acad INDEX s/SUBJECT [l/LEVEL] [s/SUBJECT [l/LEVEL]]...`

Details:
* At least one `s/` prefix is required.
* `l/LEVEL` is optional and applies to the subject immediately before it.
* Existing subjects are kept.
* Duplicate subjects are not added again.

Examples:
* `add acad 1 s/Math l/Strong`
* `add acad 1 s/Math l/Strong s/Science`

### Editing a student's academics : `edit acad`

Replaces the student's subjects and/or updates the overall academic note.

Format: `edit acad INDEX [s/SUBJECT [l/LEVEL]]... [dsc/DESCRIPTION]`

Details:
* At least one of `s/` or `dsc/` must be provided.
* If you provide subject fields, they replace the existing subject list.
* If you provide no `s/` fields, the current subject list is kept.
* Use `s/` with no value to clear all subjects.
* Use `dsc/` with no value to clear the academic description.

Examples:
* `edit acad 1 s/Math l/Strong s/Science`
* `edit acad 1 dsc/Good progress this semester`
* `edit acad 2 s/Physics l/Weak dsc/Needs extra support`
* `edit acad 3 s/`

### Deleting subjects from a student : `delete acad`

Deletes specific subjects from a student by subject index.

Format: `delete acad INDEX s/SUBJECT_INDEX [s/SUBJECT_INDEX]...`

Details:
* `INDEX` is the student index in the current displayed list.
* Each `SUBJECT_INDEX` is taken from the numbered subject list in that student's detail panel.
* At least one `s/` prefix is required.

Examples:
* `delete acad 1 s/2`
* `delete acad 1 s/2 s/4`

### Locating students by subject : `find acad`

Finds students whose subjects match any of the given subject keywords.

Format: `find acad s/SUBJECT [s/SUBJECT]...`

Details:
* At least one `s/` prefix is required.
* Multiple `s/` prefixes are allowed.
* Matching is case-insensitive.
* Matching is partial.  
  Example: `s/math` matches `Mathematics`
* A student is returned if any subject matches at least one keyword.

Examples:
* `find acad s/Math`
* `find acad s/Math s/Science`

--------------------------------------------------------------------------------------------------------------------

## Parent / Guardian Management

Use these commands to store and search for the parent or guardian details linked to each student.

### Editing parent details : `edit parent`

Sets or updates parent / guardian details for a student.

Format: `edit parent INDEX [n/PARENT_NAME] [p/PARENT_PHONE] [e/PARENT_EMAIL]`

Details:
* At least one field must be provided.
* Existing parent fields stay unchanged unless you replace them.
* If the student does not already have a parent / guardian record, include `n/PARENT_NAME` so TutorFlow can create one.

Examples:
* `edit parent 3 n/John Lim p/91234567 e/johnlim@example.com`
* `edit parent 1 p/81234567`

### Locating students by parent : `find parent`

Finds students whose parent / guardian details match the supplied keywords.

Format: `find parent [n/NAME_KEYWORDS] [p/PHONE_KEYWORDS] [e/EMAIL_KEYWORDS]`

Details:
* At least one of `n/`, `p/`, or `e/` must be provided.
* Each prefix may be used at most once.
* You can give multiple keywords inside a single prefix by separating them with spaces.  
  Example: `n/Susan Meier`
* Parent name matching is case-insensitive and based on full words.
* Parent phone and email matching are case-insensitive and based on partial text.
* Within a single field, multiple keywords behave as an `OR` search.  
  Example: `n/Susan Meier` matches a parent name containing either `Susan` or `Meier`.
* If you supply more than one field, the student must match **every supplied field**.  
  Example: `n/Susan p/9999` requires both a matching name keyword and a matching phone keyword.

Examples:
* `find parent n/Susan`
* `find parent n/Susan p/9999`
* `find parent e/example.com`

--------------------------------------------------------------------------------------------------------------------

## Billing & Payment Management

Use billing commands to track tuition fees, next payment due dates, and payment history.

### Editing billing details : `edit billing`

Updates a student's tuition fee amount and/or payment due date.

Format: `edit billing INDEX [a/AMOUNT] [d/DATE]`

Details:
* At least one of `a/` or `d/` must be provided.
* `a/AMOUNT` must be a non-negative number.
* `d/DATE` must be in ISO 8601 local date format: `YYYY-MM-DD`.
* This command changes billing settings only. It does not add a payment record.

Examples:
* `edit billing 1 a/250`
* `edit billing 1 d/2026-03-20`
* `edit billing 1 a/250 d/2026-03-20`

### Recording a payment : `add payment`

Records that a student paid on a specific date.

Format: `add payment INDEX d/DATE`

Details:
* `d/DATE` must be in ISO 8601 local date format: `YYYY-MM-DD`.
* The payment date cannot be later than today.
* Recording a payment also advances the student's billing due date by one billing cycle.

Examples:
* `add payment 1 d/2026-03-05`

### Deleting a payment record : `delete payment`

Deletes a previously recorded payment date.

Format: `delete payment INDEX d/DATE`

Details:
* `d/DATE` must be in ISO 8601 local date format: `YYYY-MM-DD`.
* The date cannot be later than today.
* The specified date must already exist in that student's payment history.
* If you delete the most recent recorded payment date, TutorFlow rolls the due date back by one billing cycle.
* If you delete an older payment date, the due date stays unchanged.

Examples:
* `delete payment 1 d/2026-03-01`
* `delete payment 2 d/2025-12-15`

### Finding students by payment due month : `find billing`

Finds students in the current list whose payment due date falls in a given month.

Format: `find billing d/YYYY-MM`

Details:
* Exactly one `d/` prefix must be provided.
* `YYYY-MM` must be a valid year-month such as `2026-03`.
* Matching ignores the day of the month.

Examples:
* `find billing d/2026-03`
* `find billing d/2025-12`

--------------------------------------------------------------------------------------------------------------------

## Appointment & Attendance Management

Use these commands to schedule lessons, see weekly appointments, and record whether a lesson happened.

### Adding an appointment : `add appt`

Adds an appointment to a student.

Format: `add appt INDEX d/DATETIME [r/RECURRENCE] dsc/DESCRIPTION`

Details:
* `d/DATETIME` must be in ISO 8601 local date-time format: `YYYY-MM-DDTHH:MM:SS`.
* `r/RECURRENCE` is optional. Valid values are `NONE`, `WEEKLY`, `BIWEEKLY`, and `MONTHLY`.
* If `r/` is omitted, TutorFlow uses `NONE`.
* `dsc/` is required and should describe the session.

Examples:
* `add appt 1 d/2026-01-29T08:00:00 dsc/Weekly algebra practice`
* `add appt 2 d/2026-02-02T15:30:00 r/WEEKLY dsc/Physics consultation`

### Deleting an appointment : `delete appt`

Deletes one or more sessions from a student.

Format: `delete appt INDEX s/SESSION_INDEX [s/SESSION_INDEX]...`

Details:
* Deletes one or more sessions from the student at `INDEX`.
* `SESSION_INDEX` refers to the numbered session shown for that student in the app.
* At least one `s/` prefix must be provided.
* All specified session indices must be valid.

Examples:
* `delete appt 1 s/1`
* `delete appt 2 s/1 s/3`

### Editing an appointment : `edit appt`

Edits a selected session for an existing student.

Format: `edit appt INDEX s/SESSION_INDEX [d/DATETIME] [r/RECURRENCE] [dsc/DESCRIPTION]`

Details:
* Edits the selected session for the student at `INDEX`.
* `SESSION_INDEX` refers to the numbered session shown for that student in the app.
* At least one of `d/`, `r/`, or `dsc/` must be provided.
* `d/DATETIME` must be in ISO 8601 local date-time format (`YYYY-MM-DDTHH:MM:SS`).
* `r/RECURRENCE` supports `NONE`, `WEEKLY`, `BIWEEKLY`, and `MONTHLY`.
* `dsc/DESCRIPTION` updates the session description.
* Any field you omit remains unchanged.

Examples:
* `edit appt 1 s/2 d/2026-02-12T09:00:00`
* `edit appt 1 s/2 r/MONTHLY dsc/Physics consultation`

### Finding students with appointments for a week : `find appt`

Shows students whose next appointment falls within the Monday-to-Sunday week containing the given date.

Format: `find appt [d/DATE]`

Details:
* If `d/DATE` is omitted, TutorFlow uses the current local date.
* `DATE` must be in ISO 8601 local date format: `YYYY-MM-DD`.

Examples:
* `find appt`
* `find appt d/2026-02-13`

### Recording appointment attendance : `add attd`

Records attendance for a specific session.

Format: `add attd INDEX s/SESSION_INDEX [y|n] [d/DATE_OR_DATE_TIME]`

Details:
* Records attendance for the student at `INDEX`.
* `SESSION_INDEX` refers to the numbered session shown for that student in the app.
* If `y` or `n` is omitted, `y` (attended) is assumed.
* `y` records that the student attended the selected session.
* `n` records that the student was absent for the selected session.
* If `d/DATE_OR_DATE_TIME` is omitted, the selected session's `next` date is used.
* `d/DATE_OR_DATE_TIME` is only allowed together with `y`.
* `d/DATE_OR_DATE_TIME` must be in ISO local date (`YYYY-MM-DD`) or date-time (`YYYY-MM-DDTHH:MM:SS`) format.
* Attendance cannot be recorded for a future date or time.
* Recording attendance for a recurring session advances its next scheduled occurrence by one recurrence cycle.
* Non-recurring sessions can only have attendance recorded once.

Examples:
* `add attd 1 s/1` records attendance (present) for the 1st session of student 1.
* `add attd 1 s/2 y` same as above but explicit.
* `add attd 1 s/2 y d/2026-01-29` records attendance on a specific date.
* `add attd 1 s/3 n` records an absence for the 3rd session of student 1.

### Deleting appointment attendance : `delete attd`

Deletes attendance records for a selected session.

Format: `delete attd INDEX s/SESSION_INDEX d/DATE_OR_DATE_TIME`

Details:
* Deletes attendance for the selected session of the student at `INDEX`.
* `SESSION_INDEX` refers to the numbered session shown for that student in the app.
* `d/` accepts either ISO local date (`YYYY-MM-DD`) or local date-time (`YYYY-MM-DDTHH:MM:SS`).
* If deleting by date, records on that date are removed.
* If deleting by date-time, only the exact record is removed.
* If the deleted attendance is the latest attendance for the session, recurring sessions roll back by one cycle.

Examples:
* `delete attd 1 s/2 d/2026-01-29`
* `delete attd 1 s/2 d/2026-01-29T08:00:00`

--------------------------------------------------------------------------------------------------------------------

## General Commands

### Viewing help : `help`

Shows the help window.

Format: `help`

![Help message](images/helpMessage.png)

### Clearing all entries : `clear`

Deletes all student records from TutorFlow.

Format: `clear`

<div markdown="span" class="alert alert-warning">:exclamation: **Caution:**  
This action is irreversible.
</div>

### Exiting the program : `exit`

Closes TutorFlow.

Format: `exit`

--------------------------------------------------------------------------------------------------------------------

## Data Management

### Saving the data

TutorFlow saves data automatically after every command that changes data. You do not need a manual save command.

### Editing the data file

TutorFlow stores data in:

`[JAR file location]/data/tutorflow.json`

Advanced users may edit the JSON file directly.

<div markdown="span" class="alert alert-warning">:exclamation: **Caution:**  
If you edit the data file into an invalid format, TutorFlow may fail to load the stored data correctly on the next run. Make a backup first if you plan to edit the file manually.
</div>

--------------------------------------------------------------------------------------------------------------------

## FAQ

**Q:** How do I move my TutorFlow data to another computer?  
**A:** Install TutorFlow on the other computer, run it once, then replace the new `data/tutorflow.json` file with the one from your old TutorFlow folder.

--------------------------------------------------------------------------------------------------------------------

## Known issues

1. **Multiple screens:** if you move the app to a secondary display and later switch back to a single-display setup, the window may reopen off-screen. Delete the `preferences.json` file before launching TutorFlow again.
1. **Help window:** if the Help window is minimized and you run `help` again, TutorFlow does not open a second Help window. Restore the minimized Help window manually.

--------------------------------------------------------------------------------------------------------------------

## Command summary

### Student Management

Action | Format | Example
-------|--------|--------
**Add student** | `add student n/NAME p/PHONE e/EMAIL a/ADDRESS [t/TAG]...` | `add student n/James Ho p/22224444 e/jamesho@example.com a/123 Clementi Rd t/Upper Sec`
**Edit student** | `edit student INDEX [n/NAME] [p/PHONE] [e/EMAIL] [a/ADDRESS]` | `edit student 2 n/James Lee e/jameslee@example.com`
**Delete student** | `delete student INDEX` | `delete student 3`
**View student** | `view INDEX` | `view 1`
**List all students** | `list` | `list`
**Find by name** | `find student KEYWORD [MORE_KEYWORDS]` | `find student James Jake`

### Tag Management

Action | Format | Example
-------|--------|--------
**Add tags** | `add tag INDEX t/TAG [t/TAG]...` | `add tag 1 t/JC t/Programming`
**Edit tags** | `edit tag INDEX [t/TAG]...` | `edit tag 1 t/JC t/J1`
**Delete tags** | `delete tag INDEX t/TAG_INDEX [t/TAG_INDEX]...` | `delete tag 1 t/2 t/3`
**Find by tag** | `find tag t/TAG [t/TAG]...` | `find tag t/JC t/Programming`

### Academic Management

Action | Format | Example
-------|--------|--------
**Add subjects** | `add acad INDEX s/SUBJECT [l/LEVEL] [s/SUBJECT [l/LEVEL]]...` | `add acad 1 s/Math l/Strong s/Science`
**Edit academics** | `edit acad INDEX [s/SUBJECT [l/LEVEL]]... [dsc/DESCRIPTION]` | `edit acad 1 s/Math l/Strong dsc/Good progress`
**Delete subjects** | `delete acad INDEX s/SUBJECT_INDEX [s/SUBJECT_INDEX]...` | `delete acad 1 s/2 s/4`
**Find by subject** | `find acad s/SUBJECT [s/SUBJECT]...` | `find acad s/Math s/Science`

### Parent / Guardian Management

Action | Format | Example
-------|--------|--------
**Edit parent** | `edit parent INDEX [n/PARENT_NAME] [p/PARENT_PHONE] [e/PARENT_EMAIL]` | `edit parent 3 n/John Lim p/91234567 e/johnlim@example.com`
**Find by parent** | `find parent [n/NAME_KEYWORDS] [p/PHONE_KEYWORDS] [e/EMAIL_KEYWORDS]` | `find parent n/Susan p/9999`

### Billing & Payment Management

Action | Format | Example
-------|--------|--------
**Edit billing** | `edit billing INDEX [a/AMOUNT] [d/DATE]` | `edit billing 1 a/250 d/2026-03-20`
**Add payment** | `add payment INDEX d/DATE` | `add payment 1 d/2026-03-05`
**Delete payment** | `delete payment INDEX d/DATE` | `delete payment 1 d/2026-03-01`
**Find by due month** | `find billing d/YYYY-MM` | `find billing d/2026-03`

### Appointment & Attendance Management

Action | Format | Example
-------|--------|--------
**Add appointment** | `add appt INDEX d/DATETIME [r/RECURRENCE] dsc/DESCRIPTION` | `add appt 1 d/2026-01-29T08:00:00 dsc/Weekly algebra practice`
**Delete appointment** | `delete appt INDEX s/SESSION_INDEX [s/SESSION_INDEX]...` | `delete appt 1 s/2 s/3`
**Edit appointment** | `edit appt INDEX s/SESSION_INDEX [d/DATETIME] [r/RECURRENCE] [dsc/DESCRIPTION]` | `edit appt 1 s/2 r/MONTHLY dsc/Physics consultation`
**Find weekly appointments** | `find appt [d/DATE]` | `find appt d/2026-02-13`
**Add attendance** | `add attd INDEX s/SESSION_INDEX [y\|n] [d/DATE_OR_DATE_TIME]` | `add attd 1 s/2 y d/2026-01-29`
**Delete attendance** | `delete attd INDEX s/SESSION_INDEX d/DATE_OR_DATE_TIME` | `delete attd 1 s/2 d/2026-01-29T08:00:00`

### General

Action | Format | Example
-------|--------|--------
**Help** | `help` | `help`
**Clear** | `clear` | `clear`
**Exit** | `exit` | `exit`
