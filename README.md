# Project: Custom Gallery App

## Overview

This Custom Gallery App is an Android application that efficiently organizes and displays images and videos stored on the device. The app provides a user-friendly interface for browsing media files grouped into albums based on their folder locations. Special emphasis is placed on ease of navigation and intuitive album categorization.

## Clean Architecture Implementation

The app's architecture is structured into layers, each with a distinct role, facilitating easy maintenance and testing.

### Layers

1. **Presentation Layer**: Contains UI components and ViewModels.
2. **Domain Layer**: Includes use cases and domain models.
3. **Data Layer**: Comprises repositories and data sources.

### Key Classes

- **Presentation Layer**:
  - `AlbumsFragment`: Displays the list of albums.
  - `MediaFragment`: Shows media contents of a selected album.
  - `AlbumViewModel`: Manages the UI logic for the `AlbumsFragment`.
  - `MediaViewModel`: Manages the UI logic for the `MediaFragment`.

- **Domain Layer**:
  - `Album`: Represents the domain model for an album.
  - `MediaItem`: Represents individual media items (images/videos).
  - `GetAlbumsUseCase`: Encapsulates the logic to fetch albums.
  - `GetMediaUseCase`: Encapsulates the logic to fetch media items for a given album.

- **Data Layer**:
  - `AlbumRepository`: Interface defining the operations for fetching albums.
  - `MediaRepository`: Interface defining the operations for fetching media items.
  - `AlbumRepositoryImpl`: Implementation of `AlbumRepository`, interacts with MediaStore.
  - `MediaRepositoryImpl`: Implementation of `MediaRepository`, also interacts with MediaStore.

### Dependency Injection

- The app uses Hilt for dependency injection, ensuring that dependencies are provided to the classes that need them, such as ViewModels, Repositories, and Use Cases.

## Features

1. **Album View**: The app features an album view that organizes media files based on their folder locations on the device. For instance, media files in the "Screenshots" folder will be grouped under the "Screenshots" album.

2. **Special Albums for Quick Access**:
   - **All Images**: This album aggregates all images from the device, excluding cache, thumbnails, and non-media files.
   - **All Videos**: Similar to "All Images", this album contains all videos from the device, providing a quick overview of video content.
   - **Camera**: Media captured by the device’s camera will be displayed under the "Camera" album, offering quick access to recently taken photos and videos.

3. **Album Details**:
   - Each album displayed in the gallery shows the name of the album and the number of images or videos it contains.
   - The latest media file (either an image or video) from each album is showcased as a thumbnail for quick identification.

4. **Media Browsing**: Upon selecting an album, users are presented with a detailed screen showcasing the contents of the album. This screen adopts a grid layout for images and videos, allowing users to easily browse and view individual media files.

5. **User Interface**:
   - The UI is designed to be intuitive and responsive, ensuring a seamless user experience.
   - The media browsing screen uses a grid layout for easy navigation and viewing.
   - The application adapts to various screen sizes and orientations, ensuring consistent performance across different devices.

## Implementation Details

- **MediaStore Access**: The app uses Android's MediaStore to query and retrieve media files. This ensures efficient access to device media while respecting user privacy and security.

- **Custom Views**: For specific UI components, such as displaying media with a certain aspect ratio, custom views are implemented.

- **ViewModel and Repository Pattern**: The app adopts the ViewModel and Repository architectural patterns. This separation of concerns ensures that the UI logic is decoupled from the data access logic, promoting maintainability and testability.

- **LiveData and StateFlow**: LiveData and StateFlow are used for reactive UI updates. This ensures that the UI reflects the current state of the app's data, enhancing the user experience.

- **Navigation Component**: The app leverages Android's Navigation Component for handling in-app navigation, providing a consistent and predictable navigation experience.

## Additional Notes

- The app requires runtime permissions to access external storage. Users will be prompted to grant these permissions upon first launch.
- Special attention is given to handling various media types and formats, ensuring broad compatibility with device media files.
- The app has been designed with scalability in mind, allowing for future enhancements such as additional media types, cloud integration, and advanced media management features.

---
