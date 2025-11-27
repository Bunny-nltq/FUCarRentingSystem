# Browse Cars Feature - FU Car Renting System

## Overview
The Browse Cars feature allows customers to view all available cars in the rental system, search for specific vehicles, filter by availability, and initiate rental requests.

## Components

### 1. **BrowseCarsController.java**
Main controller for the Browse Cars functionality.

**Key Features:**
- Load and display all cars from the database
- Search functionality (by car name or producer)
- Filter by status (All Cars, Available, etc.)
- Display cars in card format with detailed information
- View car details in a popup dialog
- Create rental requests

**Key Methods:**
- `loadCars()` - Fetches all cars from the service
- `displayCars(List<Car> cars)` - Renders cars as cards in the UI
- `createCarCard(Car car)` - Creates a single car card with info and actions
- `handleSearch()` - Performs search and filter operations
- `viewCarDetails(Car car)` - Opens car details dialog
- `rentCar(Car car)` - Opens rental form dialog

### 2. **CarDetailsController.java**
Handles the car details popup window.

**Features:**
- Displays comprehensive car information
- Shows car specifications (producer, year, color, capacity, license plate)
- Displays rental price per day
- Shows car import date and description
- Status indicator (Available/Rented/Maintenance)

### 3. **RentCarController.java**
Manages the car rental request process.

**Features:**
- Select start and end dates
- Automatic calculation of rental duration
- Real-time price calculation
- Validation of dates (no past dates, proper date order)
- Creates rental records in pending status
- Integrated with CarRentalService

## FXML Files

### CustomerBrowseCars.fxml
Main browse cars interface with:
- Search bar (by car name/producer)
- Filter dropdown
- Search and Refresh buttons
- Scrollable car display area
- Responsive car cards

### CarDetails.fxml
Popup window showing:
- Car name and details
- GridPane layout for specifications
- Large text area for description
- Status indicator with color coding
- Close button

### RentCar.fxml
Rental request form with:
- Selected car information display
- Date pickers for start/end dates
- Spinner for rental days
- Total price calculation display
- Important notes section
- Cancel and Proceed buttons

## User Flow

1. **Browse Page**
   - Customer clicks "Browse Cars" from dashboard
   - System loads all cars and displays them in card format
   - Each card shows: Car name, Producer, Year, Color, Capacity, Status, and Price

2. **Search & Filter**
   - Customer can search by car name or producer
   - Can filter by availability status
   - Results update in real-time

3. **View Details**
   - Customer clicks "View Details" button
   - Popup shows comprehensive car information
   - Can close and return to browse view

4. **Rent Process**
   - Customer clicks "Rent Now" (only available for AVAILABLE cars)
   - Rental form opens
   - Customer selects start and end dates
   - System automatically calculates total price
   - Customer confirms and submits rental request
   - Request enters PENDING status for approval

## Data Models Used

### Car Entity
- carID (Integer)
- carName (String)
- carModelYear (Integer)
- color (String)
- capacity (Integer)
- description (String)
- importDate (LocalDate)
- producer (CarProducer)
- rentPrice (Double)
- status (String) - "AVAILABLE", "RENTED", "MAINTENANCE"
- licensePlate (String)

### CarRental Entity
- rentalID (Integer)
- customer (Customer)
- car (Car)
- rentalDate (LocalDate)
- startDate (LocalDate)
- endDate (LocalDate)
- actualPrice (Double)
- status (String) - "PENDING", "APPROVED", "REJECTED", "COMPLETED"

## Services Used

1. **CarService** - Retrieves car data from repository
2. **CarRentalService** - Creates and manages rental records

## Styling
- Color Scheme:
  - Primary: Teal (#1abc9c)
  - Secondary: Dark gray (#2c3e50)
  - Success: Green (#27ae60)
  - Danger: Red (#e74c3c)
  - Info: Blue (#3498db)
  - Neutral: Gray (#95a5a6)

## Error Handling
- Try-catch blocks for database operations
- User-friendly error dialogs
- Validation of date inputs
- Prevents rental of unavailable cars

## Future Enhancements
- Advanced filtering (by price range, capacity, color)
- Sorting options (by price, year, name)
- Car ratings and reviews display
- Booking history
- Wishlist/Favorites
- Comparison tool for multiple cars
