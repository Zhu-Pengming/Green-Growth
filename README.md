# Plant-Growth-Tracker
Plant Growth Tracker - An Application to Record and Monitor Plant Growth

The Plant Growth Tracker app is a tool designed to help users track and record the growth of their plants, making it easy to monitor changes and identify any potential problems. With this app, you can easily keep track of your plants' growth, visualize growth trends, and compare current size to previous measurements.

ABSTRACT
Accurate measurement of plant growth is important to botanical research and various fields. However, traditional measurement tools are laborious and may not be always accessible. To address this issue, the Plant Growth Android App utilizes image processing techniques to automatically monitor plant height from photos, leveraging the smartphone camera and the OpenCV library. By referencing target object heights, scaling conversions enable a more rapid and accurate estimation. The application, available in the Google Play Store, can thus be used to rapidly evaluate plant growth in height with applications in botanical studies and practical applications in a user-friendly and convenient manner. 

INTRODUCTION
The popularity of smartphones has fueled and been fueled by the many app developments. In the recent COVID-19 pandemic, innovations in the form of smartphone apps [1] [2] [3] and associated devices grew in number [4] [5]
While the lockdown measures increased the use of apps and associated devices, the apps developed for the pandemic use were built on the back of tool apps to measure or detect. Apps that calculated distance (e.g., GelApp [12], and are [6] or volume [7] laid the fundamentals of the image analysis that coupled with more advanced image analysis allowed novel applications like skin monitoring [8]. 
The importance of food security and productivity as demonstrated during the recent pandemic catalyzed high technology-based farming. The evaluation of the effectiveness of these novel farming methods requires measurement methods that are fast and accurate. In agriculture and botanical research, the growth of plant height is often an indicator of water balance, carbohydrate transport, and light inception [9]. Given its easy visual detection, it can easily benefit from the use of image processing. Traditional measurement tools may not be readily available or convenient to use. Therefore, the development of a convenient and accurate measurement tool is essential. With the widespread use of smartphones, designing mobile applications with image processing techniques has become a convenient and effective way to monitor the growth of plants.
Image processing has been used to measure plant health parameters such as leaf area [10]. These capture methods generally utilize a smartphone with further analysis using the more power processing power of larger devices e.g., desktops/laptops. 
Here we created the Plant Growth app to rapidly measure and monitor plant height leveraging on the smartphone camera and OpenCV image processing libraries. The height of the targeted plant object could be easily calculated by a scaling conversion with the known pot height (actual height of the flowerpot/pixel height of the flowerpot in the image = actual height of the flower/pixel height of the flower). In cases were 
pots were not used, a reference object near the plant of known height could be used。

MATERIALS AND METHODS
The plant growth app was developed using Android Studio 2022.2.1 incorporating SQLiteOpenHelper and OpenCV libraries. SQLite was used as the database for fields such as the plant name, species, pot height, and measurement value to facilitate data management and query.
Through smartphone camera image capture, plant images can be uploaded and associated with the corresponding plant entry. 
 
Plant Segmentation
For the method this application used to measure the height of the plant, there are two ways. First, we proposed a method for detecting flowerpots and plants using the Haar feature-based cascade classifier implemented in OpenCV. The Haar feature-based cascade classifier, introduced by Lienhart and Maydt [11], is a widely used approach for rapid object detection. It extends the set of Haar-like features and provides an effective solution for detecting objects in images. By leveraging this approach, we achieved XXX% accurate detection of flowerpots and plants in our system. We used 500 related pictures (from where?)  to train the model of flowerpots and plants.
 
Approach2: Manual Edit
  
Height Prediction
Pixel ratio
By leveraging and inputting the known dimensions of the flowerpots or even nearby markers such as poles or buildings, a reference scale could be established for the unknown plant height quickly. This is done by comparing the pixels in the height of the target object and the unknown plant. 
 
Application on Android

Height calculation
Figure 1: a) Typing the plant information. b) Display test results. c) Undetected results   d) The question of whether it is detected

 First, users upload a picture of the plant by entering key information such as the name of the plant, the species, and the height of the pot. Next, the system will display the results generated by the model, and the user will confirm two key elements: first, to confirm that the pot is correctly matched; Second, verify that the plant is correctly identified. Users can confirm both at once in a dialog box. If the results are consistent, the system saves the data to the database. If the results are inconsistent, the user can choose to manually correct the data. This process is designed to ensure an accurate match between the plant and the pot, and to improve the reliability and accuracy of the application through user confirmation and correction.
 
Height recalculation by manual editing
Figure 2: a) Drawing frame for the flowerpot. b) Entering pot height. c) Drawing frame for only the flower. d) Calculated plant height.

RESULTS    OR RESULTS/DISCUSSION

CONCLUSION
The APD Plant Growth App is a convenient mobile tool that utilizes machine learning algorithms and image processing methods to measure plant height and record growth status. While the application shows promise in measuring the heights of plants, further improvements can be made, including refining the performance of object detection algorithms and incorporating advanced techniques such as machine learning to enhance accuracy. This application provides a valuable tool for monitoring and measuring plant growth, which contributes to botanical research and practical applications. 

