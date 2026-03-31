---
layout: page
title: User Guide
---

TutorFlow is a **desktop app for managing contacts, optimized for use via a Command Line Interface** (CLI) while still having the benefits of a Graphical User Interface (GUI). If you can type fast, TutorFlow can get your contact management tasks done faster than traditional GUI apps.

* Table of Contents
{:toc}

--------------------------------------------------------------------------------------------------------------------

## Quick start

1. Ensure you have Java `17` or above installed in your Computer.<br>
   **Mac users:** Ensure you have the precise JDK version prescribed [here](https://se-education.org/guides/tutorials/javaInstallationMac.html).

1. Download the latest `.jar` file from [here](https://github.com/AY2526S2-CS2103T-T09-3/tp/releases).

1. Copy the file to the folder you want to use as the _home folder_ for TutorFlow.

1. Open a command terminal, `cd` into the folder you put the jar file in, and use the `java -jar tutorflow.jar` command to run the application.<br>
   A GUI similar to the below should appear in a few seconds. Note how the app contains some sample data.<br>
   ![Ui](images/UI_v1.4.png)

1. Type the command in the command box and press Enter to execute it. e.g. typing **`help`** and pressing Enter will open the help window.<br>
   Some example commands you can try:

   * `list` : Lists all contacts.

   * `add student n/John Doe p/98765432 e/johnd@example.com a/John street, block 123, #01-01` : Adds a contact named `John Doe` to the address book.

   * `delete 3` : Deletes the 3rd contact shown in the current list.

   * `clear` : Deletes all contacts.

   * `exit` : Exits the app.

1. Refer to the [Features](#features) below for details of each command.

--------------------------------------------------------------------------------------------------------------------

## Features

<div markdown="block" class="alert alert-info">

**:information_source: Notes about the command format:**<br>

* Words in `UPPER_CASE` are the parameters to be supplied by the user.<br>
  e.g. in `add student n/NAME`, `NAME` is a parameter which can be used as `add student n/John Doe`.

* Items in square brackets are optional.<br>
  e.g `n/NAME [t/TAG]` can be used as `n/John Doe t/friend` or as `n/John Doe`.

* Items with `…`​ after them can be used multiple times including zero times.<br>
  e.g. `[t/TAG]…​` can be used as ` ` (i.e. 0 times), `t/friend`, `t/friend t/family` etc.

* Parameters can be in any order.<br>
  e.g. if the command specifies `n/NAME p/PHONE_NUMBER`, `p/PHONE_NUMBER n/NAME` is also acceptable.

* Extraneous parameters for commands that do not take in parameters (such as `help`, `list`, `exit` and `clear`) will be ignored.<br>
  e.g. if the command specifies `help 123`, it will be interpreted as `help`.

* If you are using a PDF version of this document, be careful when copying and pasting commands that span multiple lines as space characters surrounding line-breaks may be omitted when copied over to the application.
</div>

### Viewing help : `help`

Shows a message explaining how to access the help page.

![help message](images/helpMessage.png)

Format: `help`


### Adding a student: `add student`

Adds a student to the address book.

Format: `add student n/NAME p/PHONE_NUMBER e/EMAIL a/ADDRESS [t/TAG]…​`

`add` is a command family that supports subcommands such as `student` and `payment`.

<div markdown="span" class="alert alert-primary">:bulb: **Tip:**
A student can have any number of tags (including 0)
</div>

Examples:
* `add student n/John Doe p/98765432 e/johnd@example.com a/John street, block 123, #01-01`
* `add student n/Betsy Crowe t/friend e/betsycrowe@example.com a/Newgate Prison p/1234567 t/criminal`

### Recording payment date : `add payment`

Records a tuition payment date for an existing student contact.

Format: `add payment INDEX d/DATE`

* Records payment for the person at the specified `INDEX`. The index refers to the index number shown in the displayed person list. The index **must be a positive integer** 1, 2, 3, ...
* `d/` accepts ISO 8601 local date (`YYYY-MM-DD`).
* This command records payment history and advances the billing due date based on recurrence.

Examples:
* `add payment 1 d/2026-03-05`

### Listing all persons : `list`

Shows a list of all persons in the address book.

Format: `list`

### Editing a student : `edit student`

Edits an existing student in the address book.

Format: `edit student INDEX [n/NAME] [p/PHONE] [e/EMAIL] [a/ADDRESS]`

* Edits the person at the specified `INDEX`. The index refers to the index number shown in the displayed person list. The index **must be a positive integer** 1, 2, 3, …​
* At least one of the optional fields must be provided.
* Existing values will be updated to the input values.

Examples:
* `edit student 1 p/91234567 e/johndoe@example.com` edits the phone number and email address of the 1st person.
* `edit student 2 n/Betsy Crower` edits the name of the 2nd person.

### Editing billing amount : `edit billing`

Updates tuition fee amount for an existing student contact.

Format: `edit billing INDEX a/AMOUNT`

* Updates billing amount for the person at the specified `INDEX`.
* `a/` must be a non-negative number.
* This command updates tuition fee only and does not change payment history.

Examples:
* `edit billing 1 a/250`

### Adding an appointment : `add appt`

Adds an appointment to an existing student contact.

Format: `add appt INDEX d/DATETIME [r/RECURRENCE] dsc/DESCRIPTION`

* Adds the appointment to the person at the specified `INDEX`. The index refers to the index number shown in the displayed person list. The index **must be a positive integer** 1, 2, 3, ...
* `d/` accepts ISO 8601 local date-time.
* `r/` is optional. Supported values are `NONE`, `WEEKLY`, `BIWEEKLY`, and `MONTHLY`. If omitted, `NONE` is used.
* `dsc/` is required and stores a short appointment description.
* Adding a new appointment replaces any existing appointment for that student.

Examples:
* `add appt 1 d/2026-01-29T08:00:00 dsc/Weekly algebra practice`
* `add appt 2 d/2026-02-02T15:30:00 r/WEEKLY dsc/Physics consultation`

### Recording appointment attendance : `add attd`

Records attendance for the current appointment of an existing student contact.

Format: `add attd INDEX [y|n] [d/DATE]`

* Records attendance for the person at the specified `INDEX`. The index refers to the index number shown in the displayed person list. The index **must be a positive integer** 1, 2, 3, ...
* If `y` or `n` is omitted, `y` is assumed.
* `y` records that the student attended the current appointment.
* `n` records that the student was absent for the current appointment.
* If `d/DATE` is omitted, the current appointment date is taken from the appointment's `next` date.
* `d/DATE` is only allowed together with `y`.
* `d/DATE` must be in ISO local date format (`YYYY-MM-DD`).
* Attendance cannot be recorded for a future date.
* This command only works when the student currently has an appointment.
* Non-recurring appointments can only have attendance recorded once.

Examples:
* `add attd 1`
* `add attd 1 y`
* `add attd 1 y d/2026-01-29`
* `add attd 1 n`

### Locating persons by name: `find person`

Finds persons whose names contain any of the given keywords.

Format: `find person KEYWORD [MORE_KEYWORDS]`

* The search is case-insensitive. e.g `hans` will match `Hans`
* The order of the keywords does not matter. e.g. `Hans Bo` will match `Bo Hans`
* Only the name is searched.
* Only full words will be matched e.g. `Han` will not match `Hans`
* Persons matching at least one keyword will be returned (i.e. `OR` search).
  e.g. `Hans Bo` will return `Hans Gruber`, `Bo Yang`

Examples:
* `find person John` returns `john` and `John Doe`
* `find person alex david` returns `Alex Yeoh`, `David Li`<br>
  ![result for 'find person alex david'](images/findAlexDavidResult.png)


### Locating persons by tag: `find tag`

Finds persons whose tags match any of the given keywords.

Format: `find tag t/TAG [t/MORE_TAGS]...`

* At least one `t/` prefix must be provided.
* Multiple `t/` prefixes are allowed.
* Tags can contain spaces (e.g. `t/Upper Sec`, `t/JC Year 1`).
* The search is case-insensitive.
* Partial matching is supported (e.g. `t/math` matches `Mathematics`).
* Persons matching **at least one** tag keyword will be returned (i.e. `OR` search).
* The displayed list is updated to show only matching persons.

### Locating persons by subject: `find subject`

Finds persons whose subjects match any of the given keywords.

Format: `find subject s/SUBJECT [s/MORE_SUBJECTS]...`

* At least one `s/` prefix must be provided.
* Multiple `s/` prefixes are allowed.
* Subjects can contain spaces (e.g. `s/Additional Math`, `s/Computer Science`).
* The search is case-insensitive.
* Partial matching is supported.
* Persons matching **at least one** subject keyword will be returned (i.e. `OR` search).
* The displayed list is updated to show only matching persons.


### Finding payment due by month: `find payment`

Finds all persons whose billing payment due date falls within the specified year-month.

Format: `find payment d/YYYY-MM`

* Exactly one `d/` prefix must be provided.
* Duplicate `d/` prefixes are invalid (e.g., `d/2026-03 d/2026-04`).
* `YYYY-MM` must be a valid year-month (e.g., `2026-03`).
* The search matches any displayed payment due month (ignores day of month).

Examples:
* `find payment d/2026-03` returns all students in currently displayed list with payment due dates in March 2026.
* `find payment d/2025-12` returns students with due dates in December 2025.

### Viewing appointments for a week: `viewappt`

Shows all students whose appointment date falls within the Monday-Sunday week containing the given date.

Format: `viewappt [d/DATE]`

* If `d/DATE` is omitted, the current local date is used.
* `DATE` must be in ISO format (`YYYY-MM-DD`).
* At most one `d/` prefix may be provided.
* Text outside the optional `d/` prefix is invalid.
* The displayed list switches to appointment view and shows the recorded appointment for each matching student.

Examples:
* `viewappt`
* `viewappt d/2026-02-13`

### Deleting a person : `delete student`

Deletes the specified person from the address book.

Format: `delete student INDEX`

* Deletes the person at the specified `INDEX`.
* The index refers to the index number shown in the displayed person list.
* The index **must be a positive integer** 1, 2, 3, …​

Examples:
* `list` followed by `delete student 2` deletes the 2nd person in the address book.
* `find person Betsy` followed by `delete student 1` deletes the 1st person in the results of the `find` command.

### Deleting a recorded payment date : `delete payment`

Deletes a recorded payment date for an existing student contact.

Format: `delete payment INDEX d/DATE`

* Deletes the payment date for the person at the specified `INDEX`.
* The index refers to the index number shown in the displayed person list. The index **must be a positive integer** 1, 2, 3, ...
* Exactly one `d/` prefix must be provided.
* `DATE` must be in ISO 8601 local date format (`YYYY-MM-DD`).
* The date cannot be later than today.
* If the date is not recorded for the student, the command fails with an error.
* If the deleted date is the latest recorded payment date, the billing due date is rolled back by one recurrence cycle.
* If the deleted date is not the latest recorded payment date, the billing due date remains unchanged.

Examples:
* `delete payment 1 d/2026-03-01`
* `delete payment 2 d/2025-12-15`

### Clearing all entries : `clear`

Clears all entries from the address book.

Format: `clear`

### Exiting the program : `exit`

Exits the program.

Format: `exit`

### Saving the data

Address book data are saved in the hard disk automatically after any command that changes the data. There is no need to save manually.

### Editing the data file

Address book data are saved automatically as a JSON file `[JAR file location]/data/tutorflow.json`. Advanced users are welcome to update data directly by editing that data file.

<div markdown="span" class="alert alert-warning">:exclamation: **Caution:**
If your changes to the data file makes its format invalid, TutorFlow will discard all address book data and start with an empty data file at the next run. Hence, it is recommended to take a backup of the file before editing it.<br>
Furthermore, certain edits can cause the address book to behave in unexpected ways (e.g., if a value entered is outside of the acceptable range). Therefore, edit the data file only if you are confident that you can update it correctly.
</div>

### Archiving data files `[coming in v2.0]`

_Details coming soon ..._

--------------------------------------------------------------------------------------------------------------------

## FAQ

**Q**: How do I transfer my data to another Computer?<br>
**A**: Install the app in the other computer and overwrite the empty data file it creates with the file that contains the data of your previous TutorFlow home folder.

--------------------------------------------------------------------------------------------------------------------

## Known issues

1. **When using multiple screens**, if you move the application to a secondary screen, and later switch to using only the primary screen, the GUI will open off-screen. The remedy is to delete the `preferences.json` file created by the application before running the application again.
2. **If you minimize the Help Window** and then run the `help` command (or use the `Help` menu, or the keyboard shortcut `F1`) again, the original Help Window will remain minimized, and no new Help Window will appear. The remedy is to manually restore the minimized Help Window.

--------------------------------------------------------------------------------------------------------------------

## Command summary

Action | Format, Examples
--------|------------------
**Add** | `add student n/NAME p/PHONE_NUMBER e/EMAIL a/ADDRESS [t/TAG]…​` <br> e.g., `add student n/James Ho p/22224444 e/jamesho@example.com a/123, Clementi Rd, 1234665 t/friend t/colleague`
**Add Appointment** | `add appt INDEX d/ISO8601_DATETIME [r/RECURRENCE] dsc/DESCRIPTION`<br> e.g., `add appt 1 d/2026-01-29T08:00:00 dsc/Weekly algebra practice`
**Add Attendance** | `add attd INDEX [y\|n] [d/DATE]`<br> e.g., `add attd 1`, `add attd 1 n`, `add attd 1 d/2026-01-29`
**Add Payment** | `add payment INDEX d/ISO8601_DATE`<br> e.g., `add payment 1 d/2026-03-05`
**Clear** | `clear`
**Delete Student** | `delete student INDEX`<br> e.g., `delete student 3`
**Delete Payment** | `delete payment INDEX d/ISO8601_DATE`<br> e.g., `delete payment 1 d/2026-03-01`
**Edit Student** | `edit student INDEX [n/NAME] [p/PHONE_NUMBER] [e/EMAIL] [a/ADDRESS]`<br> e.g., `edit student 2 n/James Lee e/jameslee@example.com`
**Edit Billing** | `edit billing INDEX a/AMOUNT`<br> e.g., `edit billing 1 a/250`
**Find** | `find person KEYWORD [MORE_KEYWORDS]`<br> e.g., `find person James Jake`
**Find Payment** | `find payment d/YYYY-MM`<br> e.g., `find payment d/2026-03`
**View Appointments** | `viewappt [d/DATE]`<br> e.g., `viewappt d/2026-02-13`
**List** | `list`
**Help** | `help`
