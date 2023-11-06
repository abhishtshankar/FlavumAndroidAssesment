# FlavumAndroidAssesment

## DEMO : 


https://github.com/abhishtshankar/FlavumAndroidAssesment/assets/71582884/33b569d7-ebc4-48ee-aec3-09406c83a856



### CODE EXPLANATION :

* FlavumAndroid : This class serves as the entry point of the application and ensures that the application context and preferences are properly initialized.

* FlavumPreferenceKeyEnum: An enum class defining keys for preferences, providing a structured way to access and manage preference values.

* FlavumPreferences: A class for managing preferences, including different types (string, int, float, boolean, and long). It uses property delegates to read and write preference values. Additionally, it has a logout function to clear certain preferences.

* Preferences: An abstract class for managing preferences that provides utility functions for various types of preferences.

* BaseActivity: A base class for activities with features like ViewBinding, shared preferences, and methods for pre-init, view setup, and ViewModel binding.

* BaseFragment: A similar base class for fragments, handling ViewBinding, ViewModel initialization, and view setup, along with utility functions for Flow binding and delayed task execution.

* BaseViewModel: A base class for ViewModels with context and preferences access, coroutine job execution, and handling of coroutine exceptions.

* I've followed MVVM architecture, MVVM stands for Model, View, ViewModel.
          • Model: This holds the data of the application. It cannot directly talk to the View. Generally, it's recommended to expose the data to the ViewModel through Observables.
          • View: It represents the Ul of the application devoid of any Application Logic. It observes the ViewModel.
          • ViewModel: It acts as a link between the Model and the View. It's responsible for transforming the data from the Model. It provides data streams to the View. It also uses hooks or callbacks to. update the View. It'll ask for the data from the Model.
