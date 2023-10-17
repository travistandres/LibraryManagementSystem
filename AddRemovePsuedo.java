// Jaxon Elwell
// 10/15/2023
// Psuedocode for the Add and Remove functions of the Library Database
//Works off the assumption the unique db ids are chosen manually as the ISBN and MNumber or similar id system

AddBook(String Title, String Author, String Genre, int ISBN)
{
	//verify all text fields are filled
	bool done = false;
	while(done == false)
	{		
		if(Title == null || Author == null || Genre == null || ISBN == null)
			new RuntimeError("Missing a value!");
		done = true;
	}
	
	//verify book doesn't already exist
	while(rs.next())
	{
		if(ISBN == rs.getInt("ISBN"))
		{
			System.out.println("Error! Book Already Exists");
			Break();
		}
	}
	
	//insert new book into Book database
	String insertQuery = "INSERT INTO Books (Title, Author, Genre, ISBN) 
	VALUES (?, ?, ?, ?)"

	try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) 
	{
		// Set the parameter values for the INSERT statement.
		preparedStatement.setString(1, Title);
		preparedStatement.setString(2, Author);
		preparedStatement.setString(3, Genre);
		preparedStatement.setString(4, ISBN);
				
		// Execute the INSERT statement.
		int rowsAffected = preparedStatement.executeUpdate();
		if (rowsAffected > 0) 
		{
			System.out.println("New book record inserted successfully.");
		} 
		else 
		{
			System.out.println("Insert operation failed.");
		}
	}
}


AddUser(String Title, String Author, String Genre, int ISBN, int UserID)
{
	//verify all text fields are filled
	bool done = false;
	while(done == false)
	{
		if(FN == null || LN == null || PhoneNumber == null || Address == null)
			new RuntimeError("Missing a value!");
		done = true;
	}
	
	//verify user doesn't already exist
	while(rs.next())
	{
		if(UserID == rs.getInt("UserID"))
		{
			System.out.println("Error! User Already Exists");
			Break();
		}
	}
	
	//insert new user into User database
	String insertQuery = "INSERT INTO Users (FN, LN, PhoneNumber, Address) 
	VALUES (?, ?, ?, ?)"
	
	try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) 
	{
		
		// Set the parameter values for the INSERT statement.
		preparedStatement.setString(1, FN);
		preparedStatement.setString(2, LN);
		preparedStatement.setString(3, PhoneNumber);
		preparedStatement.setString(4, Address);
				
		// Execute the INSERT statement.
		int rowsAffected = preparedStatement.executeUpdate();
		if (rowsAffected > 0) 
		{
			System.out.println("New user record inserted successfully.");
		} 
		else 
		{
			System.out.println("Insert operation failed.");
		}
	}
}

void Remove()
{
	switch(MouseListener)
			{
				//compares unique id to database id and then deletes the entry if it exists
				case User -> {
					String deleteQuery = "DELETE FROM Users WHERE UserID = MouseListener";
					
					//Prepare and execute the SQL statement.
					try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) 
					{
						// Execute the INSERT statement.
						int rowsAffected = preparedStatement.executeUpdate();
						if (rowsAffected > 0) 
						{
							System.out.println("User record removed successfully.");
						} 
						else 
						{
							System.out.println("Delete operation failed.");
						}
					}
				}
				case Book -> {
					String deleteQuery = "DELETE FROM Books WHERE BookID = MouseListener";
					
					//Prepare and execute the SQL statement.
					try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) 
					{
						// Execute the INSERT statement.
						int rowsAffected = preparedStatement.executeUpdate();
						if (rowsAffected > 0) 
						{
							System.out.println("Book record removed successfully.");
						} 
						else 
						{
							System.out.println("Delete operation failed.");
						}
					}
				}
				default -> System.out.println("Something Went Wrong!");
			}
}
void AddUser(String Title, String Author, String Genre, int ISBN, int UserID)
{
	//verify all text fields are filled
	boolean done = false;
	while(!done)
	{
		if(FN == null || LN == null || PhoneNumber == null || Address == null)
			throw new RuntimeException("Missing a value!");
		done = true;
	}
	
	//verify user doesn't already exist
	while(rs.next())
	{
		if(UserID == rs.getInt("UserID"))
		{
			System.out.println("Error! User Already Exists");
			break;
		}
	}
	
	//insert new user into User database
	String insertQuery = "INSERT INTO Users (FN, LN, PhoneNumber, Address) VALUES (?, ?, ?, ?)";
	
	try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) 
	{
		
		// Set the parameter values for the INSERT statement.
		preparedStatement.setString(1, FN);
		preparedStatement.setString(2, LN);
		preparedStatement.setString(3, PhoneNumber);
		preparedStatement.setString(4, Address);
				
		// Execute the INSERT statement.
		int rowsAffected = preparedStatement.executeUpdate();
		if (rowsAffected > 0) 
		{
			System.out.println("New user record inserted successfully.");
		} 
		else 
		{
			System.out.println("Insert operation failed.");
		}
	}
}

void Remove()
{
	switch(MouseListener)
	{
		//compares unique id to database id and then deletes the entry if it exists
		case User -> {
			String deleteQuery = "DELETE FROM Users WHERE UserID = MouseListener";
			
			//Prepare and execute the SQL statement.
			try (PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) 
			{
				// Execute the DELETE statement.
				int rowsAffected = preparedStatement.executeUpdate();
				if (rowsAffected > 0) 
				{
					System.out.println("User record removed successfully.");
				} 
				else 
				{
					System.out.println("Delete operation failed.");
				}
			}
		}
		case Book -> {
			String deleteQuery = "DELETE FROM Books WHERE BookID = MouseListener";
			
			//Prepare and execute the SQL statement.
			try (PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) 
			{
				// Execute the DELETE statement.
				int rowsAffected = preparedStatement.executeUpdate();
				if (rowsAffected > 0) 
				{
					System.out.println("Book record removed successfully.");
				} 
				else 
				{
					System.out.println("Delete operation failed.");
				}
			}
		}
		default -> System.out.println("Something Went Wrong!");
	}
}
