# Pixabay App

## Functionalities:
- User can search for images entering one or more words in a text field
- In case of no internet and if user has already done some search, it'll cache the last searched result and thus allow some kind of offline handling
- With a click on a list item, it'll ask the user if he wants to see more details using alertdialog. In case of a positive answer a new detail screen will opened.
- The detail screen contain: 
  - A bigger version of the image, the name of the user, A list of image’s tag
  - The number of likes
  - The number of downloads
  - The number of comments
- When the app starts it should trigger a search for the string “fruits”

## Library used:
- Retrofit
- View-Binding
- Navigation component
- Hilt
- Room
- Coroutine
- GSON
- MockK

