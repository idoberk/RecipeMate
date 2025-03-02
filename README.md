# RecipeMate

## 📱 Android Recipe Application
<p>RecipeMate is a comprehensive Android recipe application built with Java and Android Studio.<br>
It connects users with thousands of recipes while allowing them to contribute their own culinary creations to the community.</p>

![RecipeMate Welcome Screen](https://github.com/idoberk/RecipeMate/blob/main/welcomeScreen.jpg)

## ⚠️ Disclaimer

This project is developed for **educational and learning purposes only**. It is not intended for production use or commercial deployment.<br>
The application may contain limitations in security, scalability, or feature completeness that would need to be addressed before any real-world implementation.<br>
Users should be aware that:

- Authentication and data storage implementations are simplified for learning
- API usage may be limited by free-tier restrictions
- Performance optimizations may not be fully implemented
- Some features may be implemented as proofs of concept

If you're using this code as a reference for your own projects, please ensure you implement proper security measures and optimizations for production environments.

## 🌟 Features
### Recipe Discovery
- Search for recipes based on categories, ingredients, or keywords
- Browse through thousands of recipes from the Spoonacular API
- View detailed recipe information including:
  - High-quality images
  - Step-by-step cooking instructions
  - Complete ingredients list with measurements
  - Cooking time
  - Serving size

![RecipeMate Register Screen](https://github.com/idoberk/RecipeMate/blob/main/registerScreen.jpg)
![RecipeMate Login Screen](https://github.com/idoberk/RecipeMate/blob/main/loginScreen.jpg)
![RecipeMate Home Screen](https://github.com/idoberk/RecipeMate/blob/main/homeScreen.jpg)
![RecipeMate Add Recipe Screen](https://github.com/idoberk/RecipeMate/blob/main/addNewRecipeScreen.jpg)
![RecipeMate Favorites Screen](https://github.com/idoberk/RecipeMate/blob/main/favoriteRecipesScreen.jpg)
![RecipeMate Recipe Details Screen](https://github.com/idoberk/RecipeMate/blob/main/recipeDetailsScreen.jpg)
![RecipeMate Recipe List Screen](https://github.com/idoberk/RecipeMate/blob/main/recipeListScreen.jpg)
 
### User Accounts
- Secure user registration and authentication through Firebase
- Personal profile management
- Save favorite recipes for quick access

### Recipe Management
- Mark recipes as favorites to build your personal collection
- Upload your own recipes with custom details:
  - Recipe title and description
  - Custom images (stored securely on Firebase Storage)
  - Detailed ingredient lists with measurements
  - Step-by-step cooking instructions
  - Cooking time and serving size
 
## 🔧 Technologies Used
- **Java**: Core programming language
- **Android Studio**: Primary development environment
- **Firebase Authentication**: For user account management
- **Firebase Storage**: For storing user-uploaded images
- **Firestore Database**: For storing user data (favorites, uploaded recipes)
- **Spoonacular API**: External recipe database integration
- **Material Design Components**: For modern UI elements

## 📄 License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments
- [Spoonacular API](https://spoonacular.com/food-api) for providing access to thousands of recipes
- [Firebase](https://firebase.google.com/) for authentication and database services
- [Material Design](https://material.io/design) for UI components and design guidelines
