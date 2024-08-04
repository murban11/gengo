# Gengo

An Android application for learning kana and kanji written for a class project.

<p align="center">
    <img src="https://github.com/user-attachments/assets/7a7620bb-54ce-4640-83c3-969f98568168" width="49%" />
    <img src="https://github.com/user-attachments/assets/76a94743-ea6a-4c24-a507-4c81690062c1" width="49%" />
</p>

## Features

- Dark and light theme
- Customizable font size
- Authentication
- Profile pictures
- TalkBack support

## Running

1. Create an account at https://firebase.google.com/.
2. Create a Firebase project.
3. Go to Project settings &rarr; General &rarr; Android, and register a new app with package name: `com.example.gengo`.
4. Click "Download google-services.json" and save the file at `app/`.
5. Go to Console &rarr; Storage &rarr; Get started &rarr; Click "Start in test mode" &rarr; Click "Next" &rarr; Choose storage location and click "Done". Then, create an `images/` folder.
6. Go to Storage &rarr; Rules, and replace the default rules with the following:
    ```
    rules_version = '2';

    service firebase.storage {
      match /b/{bucket}/o {
        match /{allPaths=**} {
          allow read, write: if true;
        }
      }
    }
    ```
7. Go to Firestore Database and click "Start collection". Enter `Users` as Collection ID and click "Next". Then, add a new document with ID e.g. `foo.test@email.com` and a `username` field of type `string` and value `Foo`.
8. Go to Firestore Database &rarr; Rules, and replace the default rules with the following:
    ```
    rules_version = '2';

    service cloud.firestore {
      match /databases/{database}/documents {
        match /{document=**} {
          allow read, write: if true;
        }
      }
    }
    ```
9. Add `Lessons` collection with e.g. the following documents: `Hiragana`, `Katakana`, and `Kanji - numbers`. Populate the documents with fields as shown in the pictures below.
10. Go to All products &rarr; Authentication &rarr; Get started &rarr; Sign-in method. Select "Email/Password", and then enable only "Email/Password", and click "Save".
11. Build and run the application using e.g. Adroid Studio.

<p align="center">
  <img src="https://github.com/user-attachments/assets/7db587bc-f5be-4d6e-abf1-e0fd2a3ff7bb" />
</p>
<p align="center">
  <img src="https://github.com/user-attachments/assets/ff88ed60-437b-4e29-92ca-b22f73805072" />
</p>

## License

[MIT](https://github.com/murban11/gengo/blob/main/LICENSE)
