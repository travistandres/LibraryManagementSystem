//DW 10/23/2023
public class Book {
    //#region variables
    private String title;
    private String author;
    private String category;
    private String ISBN;
    private String datePublished;
    private String publisher;
    private int numOfPages;
    //#endregion
    Book(String title, String author, String category, String ISBN, String datePublished, String publisher, int numOfPages) {
        setTitle(title);
        setAuthor(author);
        setCategory(category);
        setISBN(ISBN);
        setDatePublished(datePublished);
        setPublisher(publisher);
        setNumOfPages(numOfPages);
    }
    //#region Accessors
    public String getTitle(){
        return title;
    }
    public String getAuthor(){
        return author;
    }
    public String getCategory(){
        return category;
    }
    public String getISBN(){
        return ISBN;
    }
    public String getDatePublished(){
        return datePublished;
    }
    public String getPublisher(){
        return publisher;
    }
    public int getNumOfPages(){
        return numOfPages;
    }
    //#endregion
    //#region Mutators (may not be needed)
    public void setTitle(String title){
        this.title = title;
    }
    public void setAuthor(String author){
        this.author = author;
    }
    public void setCategory(String category){
        this.category = category;
    }
    public void setISBN(String ISBN){
        this.ISBN = ISBN;
    }
    public void setDatePublished(String datePublished){
        this.datePublished = datePublished;
    }
    public void setPublisher(String publisher){
        this.publisher = publisher;
    }
    public void setNumOfPages(int numOfPages){
        this.numOfPages = numOfPages;
    }
    //#endregion
}