### GreenGreenGreen Growth Monitor

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

## Overview
The Plant Growth Monitor is an open-source Android application developed to help users easily track and record the growth of their plants. By leveraging image processing techniques and the OpenCV library, the app provides a convenient way to monitor plant height using a smartphone camera. The goal is to offer users a user-friendly tool for evaluating plant growth, contributing to botanical research, and supporting practical applications.

## Features
# Automated Measurement: 
  Utilizes image processing to automatically monitor plant height from photos captured with the smartphone camera.

# Object Detection:  
  Employs a Haar feature-based cascade classifier to detect flowerpots and plants in images, achieving XXX% accuracy.

# Manual Edit: 
  Allows users to manually input known dimensions of flowerpots or reference objects, establishing a scale for accurate height estimation.

# Database Integration: 
  Stores plant-related information such as name, species, pot height, and measurement values using SQLite for easy data management and retrieval.

## Installation
  To install the Plant Growth Monitor app on your Android device, you can download it from the Google Play Store [link here].

## How It Works
# Upload Plant Image: 
  Users upload a picture of the plant along with key information like the plant's name, species, and pot height.

# Automated Detection: 
  The app uses image processing to detect the flowerpot and plant, providing automated results.

# User Confirmation: 
  Users confirm the correct matching of the pot and the accurate identification of the plant. Inconsistencies can be manually corrected.

# Data Storage: 
  Consistent results are saved to the database, ensuring accurate data storage for future reference.

## Development Environment
   Android Studio Version: 2022.2.1
   Libraries Used: SQLiteOpenHelper for database management, OpenCV for image processing.

## Contributing
   We welcome contributions from the community! If you're interested in contributing to the Plant Growth Monitor project, please refer to our Contribution Guidelines.

## Results and Discussion
   The Plant Growth Monitor app shows promise in measuring plant heights, but ongoing efforts are focused on refining object detection algorithms and exploring advanced techniques such as machine learning to enhance accuracy.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Acknowledgments
     We appreciate the open-source community and the contributors who help improve and enhance the Plant Growth Monitor app.

